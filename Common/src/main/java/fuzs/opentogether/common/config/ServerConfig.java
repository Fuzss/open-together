package fuzs.opentogether.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "The amount of recursive block updates that can be caused from opening a single door.")
    @Config.IntRange(min = 1, max = 512)
    public int doorsUpdateLimit = 512;
    @Config(description = "The amount of recursive block updates that can be caused from opening a single fence gate.")
    @Config.IntRange(min = 1, max = 512)
    public int fenceGatesUpdateLimit = 32;
    @Config(description = "The amount of recursive block updates that can be caused from opening a single trapdoor.")
    @Config.IntRange(min = 1, max = 512)
    public int trapdoorsUpdateLimit = 32;
}
