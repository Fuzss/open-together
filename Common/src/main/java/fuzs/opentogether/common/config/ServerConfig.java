package fuzs.opentogether.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;

public class ServerConfig extends CommonConfig {
    @Config(description = "The amount of recursive block updates that can be caused from opening a single door.")
    @Config.IntRange(min = 1, max = 512)
    public int doubleDoorsUpdateLimit = 512;
    @Config(description = "The amount of recursive block updates that can be caused from opening a single fence gate.")
    @Config.IntRange(min = 1, max = 512)
    public int doubleFenceGatesUpdateLimit = 32;
    @Config(description = "The amount of recursive block updates that can be caused from opening a single trapdoor.")
    @Config.IntRange(min = 1, max = 512)
    public int doubleTrapdoorsUpdateLimit = 32;
}
