package fuzs.opentogether.config;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ClientConfig extends ServerConfig {
    @Config(name = "double_doors", description = "Blocks that may act as double doors and can open together.")
    List<String> doubleDoorsRaw = KeyedValueProvider.tagAppender(Registries.BLOCK)
            .addTag(BlockTags.DOORS)
            .addTag(ModRegistry.DOUBLE_DOORS_BLOCK_TAG)
            .asStringList();
    @Config(name = "double_trapdoors", description = "Blocks that may act as double trapdoors and can open together.")
    List<String> doubleTrapdoorsRaw = KeyedValueProvider.tagAppender(Registries.BLOCK)
            .addTag(BlockTags.TRAPDOORS)
            .addTag(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG)
            .asStringList();
    @Config(name = "double_fence_gates",
            description = "Blocks that may act as double fence gates and can open together.")
    List<String> doubleFenceGatesRaw = KeyedValueProvider.tagAppender(Registries.BLOCK)
            .addTag(BlockTags.FENCE_GATES)
            .addTag(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG)
            .asStringList();

    public ConfigDataSet<Block> doubleDoors = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> doubleTrapdoors = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> doubleFenceGates = ConfigDataSet.from(Registries.BLOCK);

    @Override
    public void afterConfigReload() {
        this.doubleDoors = ConfigDataSet.from(Registries.BLOCK, this.doubleDoorsRaw);
        this.doubleTrapdoors = ConfigDataSet.from(Registries.BLOCK, this.doubleTrapdoorsRaw);
        this.doubleFenceGates = ConfigDataSet.from(Registries.BLOCK, this.doubleFenceGatesRaw);
    }

    @Override
    public boolean supportsCurrentEnvironment(boolean isClientSide) {
        if (!this.openAllBlocksTogetherValue.get()) {
            return false;
        } else if (NetworkingHelper.isModPresentServerside(OpenTogether.MOD_ID)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    String getEffectiveEnvironmentLine() {
        return "This option only takes effect for yourself playing on a multiplayer server which does not have this mod installed.";
    }
}
