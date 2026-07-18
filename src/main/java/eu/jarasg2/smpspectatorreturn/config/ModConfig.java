package eu.jarasg2.smpspectatorreturn.config;

import java.util.List;

public record ModConfig(
    boolean Enabled,
    boolean AllowOnCreative,
    boolean AllowOnAdventure,
    List<String> AllowedWorlds
) {
    public static ModConfig defaults() {
        return new ModConfig(true, false, false, List.of("*"));
    }

    public ModConfig normalized() {
        List<String> worlds = AllowedWorlds == null || AllowedWorlds.isEmpty()
            ? List.of("*")
            : List.copyOf(AllowedWorlds);
        return new ModConfig(Enabled, AllowOnCreative, AllowOnAdventure, worlds);
    }

    public boolean isWorldAllowed(String worldId) {
        return AllowedWorlds.stream().anyMatch(entry ->
            "*".equals(entry) || worldId.equalsIgnoreCase(entry)
        );
    }
}
