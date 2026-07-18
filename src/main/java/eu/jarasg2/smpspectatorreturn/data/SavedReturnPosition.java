package eu.jarasg2.smpspectatorreturn.data;

public record SavedReturnPosition(
    String world,
    double x,
    double y,
    double z,
    float yaw,
    float pitch,
    String previousGameMode
) {
}
