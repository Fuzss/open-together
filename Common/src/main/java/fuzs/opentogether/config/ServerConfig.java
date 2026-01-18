package fuzs.opentogether.config;

import fuzs.opentogether.OpenTogether;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig implements ConfigCore, SharedConfig {
    protected ModConfigSpec.ConfigValue<Boolean> openBlocksTogetherValue;

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.openBlocksTogetherValue = builder.comment(
                "Use blocks that can be opened from interacting or using redstone together with other blocks of the same kind surrounding them.",
                "E.g. this allows double doors to be opened together via all means supported by vanilla.",
                this.getEffectiveEnvironmentLine()).define("open_blocks_together", true);
    }

    @Override
    public boolean supportsCurrentEnvironment(boolean isClientSide) {
        if (!this.openBlocksTogetherValue.get()) {
            return false;
        } else if (isClientSide && !NetworkingHelper.isModPresentServerside(OpenTogether.MOD_ID)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean flipOpenBlocksTogether() {
        this.openBlocksTogetherValue.set(!this.openBlocksTogetherValue.get());
        this.openBlocksTogetherValue.save();
        return this.openBlocksTogetherValue.get();
    }

    String getEffectiveEnvironmentLine() {
        return "This option only takes effect in singleplayer and for players on a multiplayer server which does not have this mod installed.";
    }
}
