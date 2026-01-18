package fuzs.opentogether.config;

public interface SharedConfig {
    boolean supportsCurrentEnvironment(boolean isClientSide);

    boolean flipOpenBlocksTogether();
}
