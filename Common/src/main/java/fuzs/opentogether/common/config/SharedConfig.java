package fuzs.opentogether.common.config;

public interface SharedConfig {
    boolean supportsCurrentEnvironment(boolean isClientSide);

    boolean flipOpenBlocksTogether();

    boolean openDoubleDoorsTogether();

    boolean openDoubleFenceGatesTogether();

    boolean openDoubleTrapdoorsTogether();
}
