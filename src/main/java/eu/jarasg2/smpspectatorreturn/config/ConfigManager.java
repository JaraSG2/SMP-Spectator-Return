package eu.jarasg2.smpspectatorreturn.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.jarasg2.smpspectatorreturn.SmpSpectatorReturn;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir()
        .resolve("smp-spectator-return.json");

    private static ModConfig config = ModConfig.defaults();

    private ConfigManager() {
    }

    public static ModConfig get() {
        return config;
    }

    public static boolean load() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());

            if (!Files.exists(CONFIG_FILE)) {
                config = ModConfig.defaults();
                save();
                return true;
            }

            try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                config = loaded == null ? ModConfig.defaults() : loaded.normalized();
            }

            save();
            return true;
        } catch (Exception exception) {
            config = ModConfig.defaults();
            SmpSpectatorReturn.LOGGER.error("Failed to load configuration. Defaults will be used.", exception);
            return false;
        }
    }

    private static void save() throws Exception {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        }
    }
}
