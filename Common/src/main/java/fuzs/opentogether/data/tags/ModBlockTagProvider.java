package fuzs.opentogether.data.tags;

import fuzs.opentogether.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class ModBlockTagProvider extends AbstractTagProvider<Block> {

    public ModBlockTagProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModRegistry.DOUBLE_DOORS_BLOCK_TAG).addTag(BlockTags.DOORS);
        this.tag(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG).addTag(BlockTags.TRAPDOORS);
        this.tag(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG).addTag(BlockTags.FENCE_GATES);
    }
}
