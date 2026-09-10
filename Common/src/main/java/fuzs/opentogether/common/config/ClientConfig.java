package fuzs.opentogether.common.config;

import fuzs.opentogether.common.data.tags.ModBlockTagsProvider;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ClientConfig implements ConfigCore {
    private static final String CLIENT_ONLY_DISCLAIMER = "This option only takes effect when playing on a multiplayer server without this mod installed. In all other scenarios, configure the respective tag instead.";

    @Config(name = "valid_double_doors",
            description = {"Blocks that may act as double doors which can open together.", CLIENT_ONLY_DISCLAIMER})
    List<String> validDoubleDoorsRaw = ModBlockTagsProvider.addCommonDoors(KeyedValueProvider.<Block>tags(Registries.BLOCK))
            .asStringList();
    @Config(name = "valid_double_fence_gates", description = {
            "Blocks that may act as double fence gates which can open together.", CLIENT_ONLY_DISCLAIMER
    })
    List<String> validDoubleFenceGatesRaw = ModBlockTagsProvider.addCommonFenceGates(KeyedValueProvider.<Block>tags(
            Registries.BLOCK)).asStringList();
    @Config(name = "valid_double_trapdoors",
            description = {"Blocks that may act as double trapdoors which can open together.", CLIENT_ONLY_DISCLAIMER})
    List<String> validDoubleTrapdoorsRaw = ModBlockTagsProvider.addCommonTrapdoors(KeyedValueProvider.<Block>tags(
            Registries.BLOCK)).asStringList();

    public ConfigDataSet<Block> validDoubleDoors = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> validDoubleFenceGates = ConfigDataSet.from(Registries.BLOCK);
    public ConfigDataSet<Block> validDoubleTrapdoors = ConfigDataSet.from(Registries.BLOCK);

    @Override
    public void afterConfigReload() {
        this.validDoubleDoors = ConfigDataSet.from(Registries.BLOCK, this.validDoubleDoorsRaw);
        this.validDoubleFenceGates = ConfigDataSet.from(Registries.BLOCK, this.validDoubleFenceGatesRaw);
        this.validDoubleTrapdoors = ConfigDataSet.from(Registries.BLOCK, this.validDoubleTrapdoorsRaw);
    }
}
