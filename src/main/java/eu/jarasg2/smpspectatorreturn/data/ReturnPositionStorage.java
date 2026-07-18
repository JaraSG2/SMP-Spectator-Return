package eu.jarasg2.smpspectatorreturn.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eu.jarasg2.smpspectatorreturn.SmpSpectatorReturn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ReturnPositionStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type DATA_TYPE = new TypeToken<Map<UUID, SavedReturnPosition>>() { }.getType();
    private static final Map<UUID, SavedReturnPosition> POSITIONS = new HashMap<>();

    private ReturnPositionStorage() {
    }

    public static SavedReturnPosition get(UUID uuid) {
        return POSITIONS.get(uuid);
    }

    public static boolean contains(UUID uuid) {
        return POSITIONS.containsKey(uuid);
    }

    public static void put(MinecraftServer server, UUID uuid, SavedReturnPosition position) {
        POSITIONS.put(uuid, position);
        save(server);
    }

    public static void remove(MinecraftServer server, UUID uuid) {
        POSITIONS.remove(uuid);
        save(server);
    }

    private static Path dataFile(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT)
            .resolve("data")
            .resolve("smpspectatorreturn.json");
    }

    public static void load(MinecraftServer server) {
        Path file = dataFile(server);
        POSITIONS.clear();

        if (!Files.exists(file)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            Map<UUID, SavedReturnPosition> loaded = GSON.fromJson(reader, DATA_TYPE);
            if (loaded != null) {
                POSITIONS.putAll(loaded);
            }
            SmpSpectatorReturn.LOGGER.info("Loaded {} saved return position(s).", POSITIONS.size());
        } catch (Exception exception) {
            SmpSpectatorReturn.LOGGER.error("Failed to load saved return positions.", exception);
        }
    }

    public static void save(MinecraftServer server) {
        if (server == null) {
            return;
        }

        Path file = dataFile(server);
        Path temporaryFile = file.resolveSibling(file.getFileName() + ".tmp");

        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(temporaryFile)) {
                GSON.toJson(POSITIONS, DATA_TYPE, writer);
            }

            try {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception ignored) {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            SmpSpectatorReturn.LOGGER.error("Failed to save return positions.", exception);
        }
    }
}
