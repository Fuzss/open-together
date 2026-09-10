package fuzs.opentogether.common.data.tags;

import fuzs.opentogether.common.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v3.tags.AbstractTagAppender;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class ModBlockTagsProvider extends AbstractTagProvider<Block> {

    public ModBlockTagsProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        addCommonDoors(this.tag(ModRegistry.DOUBLE_DOORS_BLOCK_TAG));
        addCommonFenceGates(this.tag(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG));
        addCommonTrapdoors(this.tag(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG));
    }

    public static AbstractTagAppender<Block> addCommonDoors(AbstractTagAppender<Block> tagAppender) {
        return tagAppender.addTag(BlockTags.DOORS);
    }

    public static AbstractTagAppender<Block> addCommonFenceGates(AbstractTagAppender<Block> tagAppender) {
        return tagAppender.addTag(BlockTags.FENCE_GATES);
    }

    public static AbstractTagAppender<Block> addCommonTrapdoors(AbstractTagAppender<Block> tagAppender) {
        return tagAppender.addTag(BlockTags.TRAPDOORS);
    }
}
