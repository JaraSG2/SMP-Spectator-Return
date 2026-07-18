package eu.jarasg2.smpspectatorreturn;

import eu.jarasg2.smpspectatorreturn.command.SpectatorCommand;
import eu.jarasg2.smpspectatorreturn.config.ConfigManager;
import eu.jarasg2.smpspectatorreturn.data.ReturnPositionStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SmpSpectatorReturn implements ModInitializer {
    public static final String MOD_ID = "smpspectatorreturn";
    public static final String MOD_NAME = "SMP Spectator Return";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        ConfigManager.load();

        ServerLifecycleEvents.SERVER_STARTED.register(ReturnPositionStorage::load);
        ServerLifecycleEvents.SERVER_STOPPING.register(ReturnPositionStorage::save);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            SpectatorCommand.register(dispatcher)
        );

        LOGGER.info("{} initialized.", MOD_NAME);
    }
}
