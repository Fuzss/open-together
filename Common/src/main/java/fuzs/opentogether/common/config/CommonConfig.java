package fuzs.opentogether.common.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Objects;

public class CommonConfig implements ConfigCore, GlobalSharedConfig {
    private static final String BLOCK_TYPE_DESCRIPTION = "This applies to both manual interactions and using redstone.";

    @Config(description = {
            "Can doors open together with other blocks of the same kind surrounding them.", BLOCK_TYPE_DESCRIPTION
    }, worldRestart = true)
    boolean openDoorsTogether = true;
    @Config(description = {
            "Can fence gates open together with other blocks of the same kind surrounding them.", BLOCK_TYPE_DESCRIPTION
    }, worldRestart = true)
    boolean openFenceGatesTogether = true;
    @Config(description = {
            "Can trapdoors open together with other blocks of the same kind surrounding them.", BLOCK_TYPE_DESCRIPTION
    }, worldRestart = true)
    boolean openTrapdoorsTogether = true;

    private ModConfigSpec.ConfigValue<Boolean> anyBlocksValue;
    private SharedConfig sharedConfig;

    public CommonConfig() {
        this.resetSharedConfig();
    }

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.anyBlocksValue = builder.comment(
                        "Can any kind of blocks open together with other blocks of the same kind surrounding them.",
                        "This serves as a global toggle and must be enabled in addition to the individual block types.")
                .define("open_any_blocks_together", true);
    }

    public SharedConfig getSharedConfig(boolean isClientSide) {
        return isClientSide ? Objects.requireNonNull(this.sharedConfig) : this;
    }

    public void setSharedConfig(SharedConfig sharedConfig) {
        this.sharedConfig = sharedConfig;
    }

    public void resetSharedConfig() {
        this.sharedConfig = this;
    }

    public boolean toggleAnyBlocks() {
        this.anyBlocksValue.set(!this.getAnyBlocksOption());
        this.anyBlocksValue.save();
        return this.getAnyBlocksOption();
    }

    @Override
    public boolean getAnyBlocksOption() {
        return this.anyBlocksValue.get();
    }

    @Override
    public boolean getDoubleDoorsOption() {
        return this.openDoorsTogether;
    }

    @Override
    public boolean getDoubleFenceGatesOption() {
        return this.openFenceGatesTogether;
    }

    @Override
    public boolean getDoubleTrapdoorsOption() {
        return this.openTrapdoorsTogether;
    }
}
