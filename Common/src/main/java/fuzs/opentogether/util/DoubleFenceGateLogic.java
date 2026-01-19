package fuzs.opentogether.util;

import fuzs.opentogether.config.SharedConfig;
import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.NeighborUpdater;

import java.util.function.Predicate;

public class DoubleFenceGateLogic implements DoubleBlockLogic {
    public static final DoubleFenceGateLogic INSTANCE = new DoubleFenceGateLogic();

    @Override
    public boolean isEnabled(SharedConfig sharedConfig) {
        return sharedConfig.openDoubleFenceGatesTogether();
    }

    @Override
    public BlockState copyBlockState(BlockState blockState, BlockState neighborBlockState) {
        return blockState.getBlock().withPropertiesOf(neighborBlockState);
    }

    @Override
    public void forBlockNeighbors(BlockState blockState, Predicate<Direction> predicate) {
        for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
            if (this.isConnectedDirection(blockState, direction) && predicate.test(direction)) {
                break;
            }
        }
    }

    @Override
    public boolean isConnectedDirection(BlockState blockState, Direction neighborDirection) {
        return blockState.getValue(FenceGateBlock.FACING).getAxis() != neighborDirection.getAxis();
    }

    @Override
    public Class<?> getBlockType() {
        return FenceGateBlock.class;
    }

    @Override
    public TagKey<Block> getBlockTag() {
        return ModRegistry.DOUBLE_FENCE_GATES_BLOCK_TAG;
    }

    @Override
    public boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis) {
        return neighborBlockState.getValue(FenceGateBlock.FACING).getAxis()
                == blockState.getValue(FenceGateBlock.FACING).getAxis();
    }
}
