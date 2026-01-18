package fuzs.opentogether.util;

import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class OpenTogetherHelper {

    public static Direction getDoubleDoorDirection(BlockState blockState) {
        Direction direction = blockState.getValue(DoorBlock.FACING);
        return blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT ? direction.getClockWise() :
                direction.getCounterClockWise();
    }

    public static boolean isCommonDoubleDoor(BlockState blockState, BlockState neighborBlockState) {
        return blockState.is(ModRegistry.DOUBLE_DOORS_BLOCK_TAG)
                && neighborBlockState.is(ModRegistry.DOUBLE_DOORS_BLOCK_TAG) && isDoubleDoor(blockState,
                neighborBlockState);
    }

    public static boolean isDoubleDoor(BlockState blockState, BlockState neighborBlockState) {
        return neighborBlockState.getBlock() instanceof DoorBlock doorBlock
                && ((DoorBlock) blockState.getBlock()).type().canOpenByHand() == doorBlock.type().canOpenByHand()
                && neighborBlockState.getValue(DoorBlock.HINGE) != blockState.getValue(DoorBlock.HINGE)
                && neighborBlockState.getValue(DoorBlock.FACING) == blockState.getValue(DoorBlock.FACING)
                && neighborBlockState.getValue(DoorBlock.HALF) == blockState.getValue(DoorBlock.HALF);
    }

    public static boolean hasNeighborDoorSignal(Level level, BlockPos blockPos, BlockState blockState) {
        return level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.relative(
                blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
    }
}
