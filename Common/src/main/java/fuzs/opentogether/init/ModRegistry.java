package fuzs.opentogether.init;

import fuzs.opentogether.OpenTogether;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    static final TagFactory TAGS = TagFactory.make(OpenTogether.MOD_ID);
    /**
     * Blocks that may act as double doors and can open together.
     * <p>
     * Limited to {@link net.minecraft.world.level.block.DoorBlock}.
     */
    public static final TagKey<Block> DOUBLE_DOORS_BLOCK_TAG = TAGS.registerBlockTag("double_doors");
    /**
     * Blocks that may act as double trapdoors and can open together.
     * <p>
     * Limited to {@link net.minecraft.world.level.block.TrapDoorBlock}.
     */
    public static final TagKey<Block> DOUBLE_TRAPDOORS_BLOCK_TAG = TAGS.registerBlockTag("double_trapdoors");
    /**
     * Blocks that may act as double fence gates and can open together.
     * <p>
     * Limited to {@link net.minecraft.world.level.block.FenceGateBlock}.
     */
    public static final TagKey<Block> DOUBLE_FENCE_GATES_BLOCK_TAG = TAGS.registerBlockTag("double_fence_gates");

    public static void bootstrap() {
        // NO-OP
    }
}
