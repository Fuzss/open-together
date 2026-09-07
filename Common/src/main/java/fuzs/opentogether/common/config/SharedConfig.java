package fuzs.opentogether.common.config;

public sealed interface SharedConfig permits CommonConfig, SharedState {
    boolean openDoubleDoorsTogether();

    boolean openDoubleFenceGatesTogether();

    boolean openDoubleTrapdoorsTogether();
}
