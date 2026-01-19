package fuzs.opentogether.util;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.config.SharedConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface DoubleBlockLogic {
    default boolean isEnabledGlobally(boolean isClientSide) {
        return OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                && OpenTogether.CONFIG.get(ServerConfig.class).supportsCurrentEnvironment(isClientSide)
                && this.isEnabled(OpenTogether.CONFIG.get(ServerConfig.class));
    }

    boolean isEnabled(SharedConfig sharedConfig);

    default @Nullable BlockState updateShape(LevelReader level, BlockState blockState, BlockState neighborBlockState, Direction direction) {
        if (this.isEnabledGlobally(level.isClientSide())) {
            if (this.isConnectedDirection(blockState, direction) && this.isValidDoubleBlock(blockState,
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
            this.forBlockNeighbors(blockState, (Direction direction) -> {
                BlockPos neighborBlockPos = blockPos.relative(direction);
                BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                if (this.isValidDoubleBlock(blockState, neighborBlockState, direction.getAxis())
                        && predicate.test(level, neighborBlockPos)) {
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

    void forBlockNeighbors(BlockState blockState, Predicate<Direction> predicate);

    boolean isConnectedDirection(BlockState blockState, Direction neighborDirection);

    Class<?> getBlockType();

    TagKey<Block> getBlockTag();

    default boolean isValidDoubleBlock(BlockState blockState) {
        return blockState.is(this.getBlockTag());
    }

    boolean isDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis);

    default boolean isValidDoubleBlock(BlockState blockState, BlockState neighborBlockState, Direction.Axis axis) {
        return this.getBlockType().isInstance(blockState.getBlock()) && this.getBlockType()
                .isInstance(neighborBlockState.getBlock()) && this.isValidDoubleBlock(blockState)
                && this.isValidDoubleBlock(neighborBlockState) && this.isDoubleBlock(blockState,
                neighborBlockState,
                axis);
    }
}
