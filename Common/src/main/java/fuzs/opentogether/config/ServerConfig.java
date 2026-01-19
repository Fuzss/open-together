package fuzs.opentogether.config;

import fuzs.opentogether.OpenTogether;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig implements ConfigCore, SharedConfig {
    @Config(description = "Can double doors open together.")
    public boolean openDoubleDoorsTogether = true;
    @Config(description = "Can double fence gates open together.")
    public boolean openDoubleFenceGatesTogether = true;
    @Config(description = "Can double trapdoors open together.")
    public boolean openDoubleTrapdoorsTogether = true;
    protected ModConfigSpec.ConfigValue<Boolean> openAllBlocksTogetherValue;

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.openAllBlocksTogetherValue = builder.comment(
                "Use blocks that can be opened from interacting or using redstone together with other blocks of the same kind surrounding them.",
                "E.g. this allows double doors to be opened together via all means supported by vanilla.",
                this.getEffectiveEnvironmentLine()).define("open_all_blocks_together", true);
    }

    @Override
    public boolean supportsCurrentEnvironment(boolean isClientSide) {
        if (!this.openAllBlocksTogetherValue.get()) {
            return false;
        } else if (isClientSide && !NetworkingHelper.isModPresentServerside(OpenTogether.MOD_ID)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean flipOpenBlocksTogether() {
        this.openAllBlocksTogetherValue.set(!this.openAllBlocksTogetherValue.get());
        this.openAllBlocksTogetherValue.save();
        return this.openAllBlocksTogetherValue.get();
    }

    @Override
    public boolean openDoubleDoorsTogether() {
        return this.openDoubleDoorsTogether;
    }

    @Override
    public boolean openDoubleFenceGatesTogether() {
        return this.openDoubleFenceGatesTogether;
    }

    @Override
    public boolean openDoubleTrapdoorsTogether() {
        return this.openDoubleTrapdoorsTogether;
    }

    String getEffectiveEnvironmentLine() {
        return "This option only takes effect in singleplayer and for players on a multiplayer server which does not have this mod installed.";
    }
}
