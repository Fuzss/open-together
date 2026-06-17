package fuzs.opentogether.util;

import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.config.SharedConfig;
import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DoubleTrapDoorLogic extends AbstractDoubleBlockLogic {
    public static final DoubleTrapDoorLogic INSTANCE = new DoubleTrapDoorLogic();

    @Override
    boolean isPositionWithinRange(BlockPos blockPos) {
        return blockPos.getY() == 0 && (Math.abs(blockPos.getX()) <= 1 || Math.abs(blockPos.getZ()) <= 1);
    }

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
    public Direction.Axis chooseAxis(BlockState blockState, BlockPos blockPos) {
        Direction direction = blockState.getValue(TrapDoorBlock.FACING);
        return direction.getAxis().choose(blockPos.getX(), blockPos.getY(), blockPos.getZ()) != 0 ?
                direction.getAxis() : direction.getClockWise(Direction.Axis.Y).getAxis();
    }

    @Override
    public boolean isNeighborDirection(BlockState blockState, Vec3i neighborNormal) {
        Direction direction = blockState.getValue(TrapDoorBlock.FACING);
        int coordinate = direction.getAxis()
                .choose(neighborNormal.getX(), neighborNormal.getY(), neighborNormal.getZ());
        return coordinate == 0 || coordinate == direction.getAxis()
                .choose(direction.getStepX(), direction.getStepY(), direction.getStepZ());
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
    public boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.@Nullable Axis axis) {
        return ((TrapDoorBlock) blockState.getBlock()).getType().canOpenByHand()
                == ((TrapDoorBlock) neighborBlockState.getBlock()).getType().canOpenByHand()
                && neighborBlockState.getValue(TrapDoorBlock.HALF) == blockState.getValue(TrapDoorBlock.HALF)
                && this.isDoubleBlock(blockState.getValue(TrapDoorBlock.FACING),
                neighborBlockState.getValue(TrapDoorBlock.FACING),
                axis);
    }

    private boolean isDoubleBlock(Direction direction, Direction neighborDirection, Direction.@Nullable Axis axis) {
        Objects.requireNonNull(axis, "axis is null");
        return direction == (direction.getAxis() != axis ? neighborDirection : neighborDirection.getOpposite());
    }

    @Override
    public int getRecursiveUpdateLimit(ServerConfig serverConfig) {
        return serverConfig.doubleTrapdoorsUpdateLimit;
    }
}
