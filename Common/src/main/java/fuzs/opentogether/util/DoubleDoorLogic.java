package fuzs.opentogether.util;

import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.config.SharedConfig;
import fuzs.opentogether.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public class DoubleDoorLogic implements DoubleBlockLogic {
    public static final DoubleDoorLogic INSTANCE = new DoubleDoorLogic();

    @Override
    public boolean isEnabled(SharedConfig sharedConfig) {
        return sharedConfig.openDoubleDoorsTogether();
    }

    @Override
    public BlockState copyBlockState(BlockState blockState, BlockState neighborBlockState) {
        return blockState.getBlock()
                .withPropertiesOf(neighborBlockState)
                .setValue(DoorBlock.HINGE, blockState.getValue(DoorBlock.HINGE));
    }

    @Override
    public boolean hasAnyNeighborSignal(Level level, BlockPos blockPos, BlockState blockState) {
        if (DoubleBlockLogic.super.hasAnyNeighborSignal(level, blockPos, blockState)) {
            return true;
        } else {
            BlockPos otherBlockPos = blockPos.relative(
                    blockState.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
            BlockState otherBlockState = level.getBlockState(otherBlockPos);
            return otherBlockState.getBlock() instanceof DoorBlock && DoubleBlockLogic.super.hasAnyNeighborSignal(level,
                    otherBlockPos,
                    otherBlockState);
        }
    }

    @Override
    public void forBlockNeighbors(BlockState blockState, Predicate<BlockPos> predicate) {
        predicate.test(new BlockPos(this.getNeighborNormal(blockState)));
    }

    @Override
    public boolean isNeighborDirection(BlockState blockState, Vec3i neighborNormal) {
        return Objects.equals(this.getNeighborNormal(blockState), neighborNormal);
    }

    private Vec3i getNeighborNormal(BlockState blockState) {
        Direction direction = blockState.getValue(DoorBlock.FACING);
        return (blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT ? direction.getClockWise() :
                direction.getCounterClockWise()).getUnitVec3i();
    }

    @Override
    public Class<?> getBlockType() {
        return DoorBlock.class;
    }

    @Override
    public TagKey<Block> getBlockTag() {
        return ModRegistry.DOUBLE_DOORS_BLOCK_TAG;
    }

    @Override
    public boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.@Nullable Axis axis) {
        return ((DoorBlock) blockState.getBlock()).type().canOpenByHand()
                == ((DoorBlock) neighborBlockState.getBlock()).type().canOpenByHand()
                && neighborBlockState.getValue(DoorBlock.HINGE) != blockState.getValue(DoorBlock.HINGE)
                && neighborBlockState.getValue(DoorBlock.FACING) == blockState.getValue(DoorBlock.FACING)
                && neighborBlockState.getValue(DoorBlock.HALF) == blockState.getValue(DoorBlock.HALF);
    }

    @Override
    public int getRecursiveUpdateLimit(ServerConfig serverConfig) {
        return serverConfig.doubleDoorsUpdateLimit;
    }
}
