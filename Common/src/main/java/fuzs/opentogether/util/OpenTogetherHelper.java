package fuzs.opentogether.util;

import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.redstone.NeighborUpdater;

public class OpenTogetherHelper {

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

    public static boolean hasAnyNeighborDoorSignal(Level level, BlockPos blockPos, BlockState blockState) {
        Direction neighborDirection = OpenTogetherHelper.getDoubleDoorDirection(blockState);
        BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
        BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
        if (isCommonDoubleDoor(blockState, neighborBlockState)) {
            return hasNeighborDoorSignal(level, neighborBlockPos, neighborBlockState);
        } else {
            return false;
        }
    }

    public static Direction getDoubleDoorDirection(BlockState blockState) {
        Direction direction = blockState.getValue(DoorBlock.FACING);
        return blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT ? direction.getClockWise() :
                direction.getCounterClockWise();
    }

    public static boolean isCommonDoubleFenceGate(BlockState blockState, BlockState neighborBlockState) {
        return blockState.is(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG)
                && neighborBlockState.is(ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG) && isDoubleFenceGate(blockState,
                neighborBlockState);
    }

    public static boolean isDoubleFenceGate(BlockState blockState, BlockState neighborBlockState) {
        return neighborBlockState.getBlock() instanceof FenceGateBlock
                && neighborBlockState.getValue(FenceGateBlock.FACING).getAxis()
                == blockState.getValue(FenceGateBlock.FACING).getAxis();
    }

    public static boolean hasAnyNeighborFenceGateSignal(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FenceGateBlock.FACING);
        for (Direction neighborDirection : NeighborUpdater.UPDATE_ORDER) {
            if (isDoubleFenceGateDirection(direction, neighborDirection)) {
                BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
                BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                if (isCommonDoubleFenceGate(blockState, neighborBlockState)
                        && level.hasNeighborSignal(neighborBlockPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isDoubleFenceGateDirection(Direction direction, Direction neighborDirection) {
        return direction.getAxis() != neighborDirection.getAxis();
    }

    public static boolean isCommonDoubleTrapDoor(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis) {
        return blockState.is(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG)
                && neighborBlockState.is(ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG) && isDoubleTrapDoor(blockState,
                neighborBlockState,
                axis);
    }

    public static boolean isDoubleTrapDoor(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis) {
        return neighborBlockState.getBlock() instanceof TrapDoorBlock trapDoorBlock
                && ((TrapDoorBlock) blockState.getBlock()).getType().canOpenByHand() == trapDoorBlock.getType()
                .canOpenByHand()
                && neighborBlockState.getValue(TrapDoorBlock.HALF) == blockState.getValue(TrapDoorBlock.HALF)
                && isDoubleTrapDoor(blockState.getValue(DoorBlock.FACING),
                neighborBlockState.getValue(DoorBlock.FACING),
                axis);
    }

    public static boolean isDoubleTrapDoor(Direction direction, Direction neighborDirection, Direction.Axis axis) {
        return direction == (direction.getAxis() != axis ? neighborDirection : neighborDirection.getOpposite());
    }

    public static boolean hasAnyNeighborTrapDoorSignal(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(TrapDoorBlock.FACING);
        for (Direction neighborDirection : Direction.Plane.HORIZONTAL) {
            if (isDoubleTrapDoorDirection(direction, neighborDirection)) {
                BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
                BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                if (isCommonDoubleTrapDoor(blockState, neighborBlockState, neighborDirection.getAxis())
                        && level.hasNeighborSignal(neighborBlockPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isDoubleTrapDoorDirection(Direction direction, Direction neighborDirection) {
        return direction != neighborDirection.getOpposite();
    }
}
