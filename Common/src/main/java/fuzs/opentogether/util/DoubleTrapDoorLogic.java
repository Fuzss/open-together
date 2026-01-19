package fuzs.opentogether.util;

import fuzs.opentogether.config.SharedConfig;
import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class DoubleTrapDoorLogic implements DoubleBlockLogic {
    public static final DoubleTrapDoorLogic INSTANCE = new DoubleTrapDoorLogic();

    @Override
    public boolean isEnabled(SharedConfig sharedConfig) {
        return sharedConfig.openDoubleTrapdoorsTogether();
    }

    @Override
    public BlockState copyBlockState(BlockState blockState, BlockState neighborBlockState) {
        return blockState.getBlock()
                .withPropertiesOf(neighborBlockState)
                .setValue(TrapDoorBlock.FACING, blockState.getValue(TrapDoorBlock.FACING));
    }

    @Override
    public void forBlockNeighbors(BlockState blockState, Predicate<Direction> predicate) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.isConnectedDirection(blockState, direction) && predicate.test(direction)) {
                break;
            }
        }
    }

    @Override
    public boolean isConnectedDirection(BlockState blockState, Direction neighborDirection) {
        return blockState.getValue(TrapDoorBlock.FACING) != neighborDirection.getOpposite();
    }

    @Override
    public Class<?> getBlockType() {
        return TrapDoorBlock.class;
    }

    @Override
    public TagKey<Block> getBlockTag() {
        return ModRegistry.DOUBLE_TRAPDOORS_BLOCK_TAG;
    }

    @Override
    public boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis) {
        return ((TrapDoorBlock) blockState.getBlock()).getType().canOpenByHand()
                == ((TrapDoorBlock) neighborBlockState.getBlock()).getType().canOpenByHand()
                && neighborBlockState.getValue(TrapDoorBlock.HALF) == blockState.getValue(TrapDoorBlock.HALF)
                && this.isDoubleBlock(blockState.getValue(TrapDoorBlock.FACING),
                neighborBlockState.getValue(TrapDoorBlock.FACING),
                axis);
    }

    private boolean isDoubleBlock(Direction direction, Direction neighborDirection, Direction.Axis axis) {
        return direction == (direction.getAxis() != axis ? neighborDirection : neighborDirection.getOpposite());
    }
}
