package fuzs.opentogether.common.world.level.block;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.config.CommonConfig;
import fuzs.opentogether.common.config.ServerConfig;
import fuzs.opentogether.common.config.SharedConfig;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface DoubleBlockLogic {
    default boolean isEnabledGlobally(boolean isClientSide) {
        return this.isEnabled(OpenTogether.CONFIG.get(CommonConfig.class).getSharedConfig(isClientSide));
    }

    boolean isEnabled(SharedConfig sharedConfig);

    default @Nullable BlockState updateShape(LevelReader level, BlockState blockState, BlockState neighborBlockState, Direction direction) {
        if (this.isEnabledGlobally(level.isClientSide())) {
            if (this.isNeighborDirection(blockState, direction.getUnitVec3i()) && this.isValidDoubleBlock(blockState,
                    neighborBlockState,
                    direction.getAxis())) {
                return this.copyBlockState(blockState, neighborBlockState);
            }
        }

        return null;
    }

    BlockState copyBlockState(BlockState blockState, BlockState neighborBlockState);

    default @Nullable BlockState getStateForPlacement(Level level, BlockPos blockPos, BlockState blockState) {
        if (!blockState.getValue(BlockStateProperties.POWERED) && !blockState.getValue(BlockStateProperties.OPEN)) {
            if (this.hasAnyNeighborSignal(level, blockPos, blockState)) {
                return blockState.setValue(BlockStateProperties.POWERED, Boolean.TRUE)
                        .setValue(BlockStateProperties.OPEN, Boolean.TRUE);
            }
        }

        return null;
    }

    default Collection<BlockPos> getValidDoubleNeighbors(Level level, BlockPos blockPos, BlockState blockState, BiPredicate<Level, BlockPos> predicate, boolean shortCircuit) {
        if (this.isEnabledGlobally(level.isClientSide())) {
            Set<BlockPos> neighborBlockPositions = new HashSet<>();
            this.forBlockNeighbors(blockState, (BlockPos offsetBlockPos) -> {
                BlockPos neighborBlockPos = blockPos.offset(offsetBlockPos);
                BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                Direction.Axis axis = this.chooseAxis(blockState, offsetBlockPos);
                if (this.isValidDoubleBlock(blockState, neighborBlockState, axis) && predicate.test(level,
                        neighborBlockPos)) {
                    neighborBlockPositions.add(neighborBlockPos);
                    return shortCircuit;
                } else {
                    return false;
                }
            });
            return neighborBlockPositions;
        } else {
            return Collections.emptySet();
        }
    }

    default boolean hasAnyNeighborSignal(Level level, BlockPos blockPos, BlockState blockState) {
        return !this.getValidDoubleNeighbors(level, blockPos, blockState, Level::hasNeighborSignal, true).isEmpty();
    }

    void forBlockNeighbors(BlockState blockState, Predicate<BlockPos> predicate);

    default Direction.@Nullable Axis chooseAxis(BlockState blockState, BlockPos blockPos) {
        return null;
    }

    boolean isNeighborDirection(BlockState blockState, Vec3i neighborNormal);

    Class<?> getBlockType();

    default boolean isValidDoubleBlock(BlockState blockState) {
        return this.getBlockDataSet(OpenTogether.CONFIG.get(CommonConfig.class)).contains(blockState.getBlock());
    }

    ConfigDataSet<Block> getBlockDataSet(CommonConfig commonConfig);

    boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.@Nullable Axis axis);

    default boolean isValidDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.@Nullable Axis axis) {
        return this.getBlockType().isInstance(blockState.getBlock()) && this.getBlockType()
                .isInstance(neighborBlockState.getBlock()) && this.isValidDoubleBlock(blockState)
                && this.isValidDoubleBlock(neighborBlockState) && this.isDoubleBlock(blockState,
                neighborBlockState,
                axis);
    }

    int getRecursiveUpdateLimit(ServerConfig serverConfig);

    default int getRecursionLeft(Level level, BlockState blockState) {
        if (this.isEnabledGlobally(level.isClientSide())) {
            if (this.getBlockType().isInstance(blockState.getBlock()) && this.isValidDoubleBlock(blockState)) {
                return this.getRecursiveUpdateLimit(OpenTogether.CONFIG.get(ServerConfig.class));
            }
        }

        return Integer.MAX_VALUE;
    }
}
