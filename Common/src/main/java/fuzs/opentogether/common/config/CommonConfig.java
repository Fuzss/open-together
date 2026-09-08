package fuzs.opentogether.common.config;

import fuzs.opentogether.common.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;

public class CommonConfig implements ConfigCore, GlobalSharedConfig {
    @Config(description = "Can double doors open together.")
    public final DoubleBlockConfig doubleDoors = new DoubleBlockConfig(BlockTags.DOORS,
            ModRegistry.DOUBLE_DOORS_BLOCK_TAG);
    @Config(description = "Can double fence gates open together.")
    public final DoubleBlockConfig doubleFenceGates = new DoubleBlockConfig(BlockTags.FENCE_GATES,
            ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG);
    @Config(description = "Can double trapdoors open together.")
    public final DoubleBlockConfig doubleTrapdoors = new DoubleBlockConfig(BlockTags.TRAPDOORS,
            ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG);

    private ModConfigSpec.ConfigValue<Boolean> allBlocksValue;
    private SharedConfig sharedConfig;

    public CommonConfig() {
        this.resetSharedConfig();
    }

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.allBlocksValue = builder.comment(
                        "Use blocks that can be opened from interacting or using redstone together with other blocks of the same kind surrounding them.",
                        "E.g. this allows double doors to be opened together via all means supported by vanilla.")
                .define("open_all_blocks_together", true);
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

    public boolean toggleAllBlocks() {
        this.allBlocksValue.set(!this.getAllBlocksOption());
        this.allBlocksValue.save();
        return this.getAllBlocksOption();
    }

    @Override
    public boolean getAllBlocksOption() {
        return this.allBlocksValue.get();
    }

    @Override
    public boolean getDoubleDoorsOption() {
        return this.doubleDoors.openTogether;
    }

    @Override
    public boolean getDoubleFenceGatesOption() {
        return this.doubleFenceGates.openTogether;
    }

    @Override
    public boolean getDoubleTrapdoorsOption() {
        return this.doubleTrapdoors.openTogether;
    }

    public static class DoubleBlockConfig implements ConfigCore {
        @Config(description = "Can double blocks open together.", worldRestart = true)
        boolean openTogether = true;
        @Config(name = "valid_blocks", description = "Blocks that may act as double blocks which can open together.")
        List<String> validBlocksRaw;

        public ConfigDataSet<Block> validBlocks = ConfigDataSet.from(Registries.BLOCK);

        public DoubleBlockConfig(TagKey<Block> vanillaBlocks, TagKey<Block> allBlocks) {
            this.validBlocksRaw = KeyedValueProvider.<Block>tags(Registries.BLOCK)
                    .addTag(vanillaBlocks)
                    .addTag(allBlocks)
                    .asStringList();
        }

        @Override
        public void afterConfigReload() {
            this.validBlocks = ConfigDataSet.from(Registries.BLOCK, this.validBlocksRaw);
        }
    }
}
