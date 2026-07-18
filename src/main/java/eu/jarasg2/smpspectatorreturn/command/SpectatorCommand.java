package eu.jarasg2.smpspectatorreturn.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import eu.jarasg2.smpspectatorreturn.config.ConfigManager;
import eu.jarasg2.smpspectatorreturn.config.ModConfig;
import eu.jarasg2.smpspectatorreturn.data.ReturnPositionStorage;
import eu.jarasg2.smpspectatorreturn.data.SavedReturnPosition;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Set;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

public final class SpectatorCommand {
    private SpectatorCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("s")
            .executes(context -> toggle(context.getSource()))
            .then(literal("reload")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> reload(context.getSource())))
        );
    }

    private static int reload(ServerCommandSource source) {
        boolean success = ConfigManager.load();
        if (success) {
            source.sendFeedback(() -> Text.literal("SMP Spectator Return configuration reloaded."), false);
            return Command.SINGLE_SUCCESS;
        }

        source.sendError(Text.literal("Configuration could not be loaded. Default settings are active."));
        return 0;
    }

    private static int toggle(ServerCommandSource source) {
        final ServerPlayerEntity player;
        try {
            player = source.getPlayerOrThrow();
        } catch (Exception exception) {
            source.sendError(Text.literal("This command can only be used by a player."));
            return 0;
        }

        UUID uuid = player.getUuid();
        GameMode currentMode = player.interactionManager.getGameMode();

        // Returning is always allowed, even if the mod or world was disabled afterwards.
        if (currentMode == GameMode.SPECTATOR && ReturnPositionStorage.contains(uuid)) {
            return returnToSavedPosition(player, source.getServer(), uuid);
        }

        ModConfig config = ConfigManager.get();
        if (!config.Enabled()) {
            player.sendMessage(Text.literal("The /s command is disabled."), false);
            return 0;
        }

        ServerWorld world = source.getWorld();
        String currentWorld = world.getRegistryKey().getValue().toString();
        if (!config.isWorldAllowed(currentWorld)) {
            player.sendMessage(Text.literal("The /s command is not allowed in this world."), false);
            return 0;
        }

        if (currentMode == GameMode.CREATIVE && !config.AllowOnCreative()) {
            player.sendMessage(Text.literal("The /s command is disabled in Creative mode."), false);
            return 0;
        }

        if (currentMode == GameMode.ADVENTURE && !config.AllowOnAdventure()) {
            player.sendMessage(Text.literal("The /s command is disabled in Adventure mode."), false);
            return 0;
        }

        if (currentMode == GameMode.SPECTATOR) {
            player.sendMessage(Text.literal("No saved return position was found."), false);
            return 0;
        }

        ReturnPositionStorage.put(source.getServer(), uuid, new SavedReturnPosition(
            world.getRegistryKey().getValue().toString(),
            player.getX(),
            player.getY(),
            player.getZ(),
            player.getYaw(),
            player.getPitch(),
            currentMode.asString()
        ));

        player.changeGameMode(GameMode.SPECTATOR);
        player.sendMessage(Text.literal("Spectator enabled. Use /s again to return."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int returnToSavedPosition(ServerPlayerEntity player, MinecraftServer server, UUID uuid) {
        SavedReturnPosition saved = ReturnPositionStorage.get(uuid);
        if (saved == null) {
            player.sendMessage(Text.literal("No saved return position was found."), false);
            return 0;
        }

        Identifier worldId = Identifier.tryParse(saved.world());
        if (worldId == null) {
            player.sendMessage(Text.literal("The saved world ID is invalid."), false);
            return 0;
        }

        RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, worldId);
        ServerWorld targetWorld = server.getWorld(worldKey);
        if (targetWorld == null) {
            player.sendMessage(Text.literal("The saved world is not available on this server."), false);
            return 0;
        }

        GameMode returnMode = parseGameMode(saved.previousGameMode());
        player.changeGameMode(returnMode);
        player.teleport(targetWorld, saved.x(), saved.y(), saved.z(), Set.of(), saved.yaw(), saved.pitch(), true);

        ReturnPositionStorage.remove(server, uuid);
        player.sendMessage(Text.literal("Returned to the saved position."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static GameMode parseGameMode(String name) {
        if (name == null) {
            return GameMode.SURVIVAL;
        }

        return switch (name.toLowerCase()) {
            case "creative" -> GameMode.CREATIVE;
            case "adventure" -> GameMode.ADVENTURE;
            default -> GameMode.SURVIVAL;
        };
    }
}
