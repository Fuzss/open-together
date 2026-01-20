package fuzs.opentogether.util;

import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.config.SharedConfig;
import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class DoubleFenceGateLogic extends AbstractDoubleBlockLogic {
    public static final DoubleFenceGateLogic INSTANCE = new DoubleFenceGateLogic();

    @Override
    boolean isPositionWithinRange(BlockPos blockPos) {
        return blockPos.distManhattan(BlockPos.ZERO) <= this.getMaxBlockDistance();
    }

    @Override
    public boolean isEnabled(SharedConfig sharedConfig) {
        return sharedConfig.openDoubleFenceGatesTogether();
    }

    @Override
    public BlockState copyBlockState(BlockState blockState, BlockState neighborBlockState) {
        return blockState.getBlock().withPropertiesOf(neighborBlockState);
    }

    @Override
    public boolean isNeighborDirection(BlockState blockState, Vec3i neighborNormal) {
        Direction.Axis axis = blockState.getValue(FenceGateBlock.FACING).getAxis();
        return axis.choose(neighborNormal.getX(), neighborNormal.getY(), neighborNormal.getZ()) == 0;
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
    public boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.@Nullable Axis axis) {
        return neighborBlockState.getValue(FenceGateBlock.FACING).getAxis()
                == blockState.getValue(FenceGateBlock.FACING).getAxis();
    }

    @Override
    public int getRecursiveUpdateLimit(ServerConfig serverConfig) {
        return serverConfig.doubleFenceGatesUpdateLimit;
    }
}
