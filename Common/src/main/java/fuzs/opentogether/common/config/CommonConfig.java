package fuzs.opentogether.common.config;

import fuzs.opentogether.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.common.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class CommonConfig implements ConfigCore, SharedConfig {
    private SharedConfig sharedConfig = this;
    @Config(description = "Can double doors open together.", worldRestart = true)
    boolean openDoubleDoorsTogether = true;
    @Config(description = "Can double fence gates open together.", worldRestart = true)
    boolean openDoubleFenceGatesTogether = true;
    @Config(description = "Can double trapdoors open together.", worldRestart = true)
    boolean openDoubleTrapdoorsTogether = true;
    @Config(name = "double_doors", description = "Blocks that may act as double doors and can open together.")
    List<String> doubleDoorsRaw = KeyedValueProvider.<Block>tags()
            .addTag(BlockTags.DOORS)
            .addTag(ModRegistry.DOUBLE_DOORS_BLOCK_TAG)
            .asStringList();
    @Config(name = "double_trapdoors", description = "Blocks that may act as double trapdoors and can open together.")
    List<String> doubleTrapdoorsRaw = KeyedValueProvider.<Block>tags()
            .addTag(BlockTags.TRAPDOORS)
            .addTag(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG)
            .asStringList();
    @Config(name = "double_fence_gates",
            description = "Blocks that may act as double fence gates and can open together.")
    List<String> doubleFenceGatesRaw = KeyedValueProvider.<Block>tags()
            .addTag(BlockTags.FENCE_GATES)
            .addTag(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG)
            .asStringList();

    private ModConfigSpec.ConfigValue<Boolean> openAllBlocksTogetherValue;
    public ConfigDataSet<Block> doubleDoors = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> doubleTrapdoors = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> doubleFenceGates = ConfigDataSet.from(Registries.BLOCK);

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.openAllBlocksTogetherValue = builder.comment(
                "Use blocks that can be opened from interacting or using redstone together with other blocks of the same kind surrounding them.",
                "E.g. this allows double doors to be opened together via all means supported by vanilla.",
                this.getEffectiveEnvironmentLine()).define("open_all_blocks_together", true);
    }

    @Override
    public void afterConfigReload() {
        this.doubleDoors = ConfigDataSet.from(Registries.BLOCK, this.doubleDoorsRaw);
        this.doubleTrapdoors = ConfigDataSet.from(Registries.BLOCK, this.doubleTrapdoorsRaw);
        this.doubleFenceGates = ConfigDataSet.from(Registries.BLOCK, this.doubleFenceGatesRaw);
    }

    public SharedConfig getSharedConfig(boolean isClientSide) {
        return isClientSide ? this.sharedConfig : this;
    }

    public void setSharedConfig(SharedConfig sharedConfig) {
        this.sharedConfig = sharedConfig;
    }

    public void resetSharedConfig() {
        this.sharedConfig = this;
    }

    public boolean toggleOpenBlocksTogether() {
        this.openAllBlocksTogetherValue.set(!this.openAllBlocksTogetherValue.get());
        this.openAllBlocksTogetherValue.save();
        return this.openAllBlocksTogetherValue.get();
    }

    public boolean openAllBlocksTogether() {
        return this.openAllBlocksTogetherValue.get();
    }

    @Override
    public boolean openDoubleDoorsTogether() {
        return this.openDoubleDoorsTogether && this.openAllBlocksTogether();
    }

    @Override
    public boolean openDoubleFenceGatesTogether() {
        return this.openDoubleFenceGatesTogether && this.openAllBlocksTogether();
    }

    @Override
    public boolean openDoubleTrapdoorsTogether() {
        return this.openDoubleTrapdoorsTogether && this.openAllBlocksTogether();
    }

    String getEffectiveEnvironmentLine() {
        return "This option only takes effect either in singleplayer or globally for all players on a multiplayer server.";
    }
}
