package fuzs.opentogether.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractDoubleBlockLogic implements DoubleBlockLogic {

    @Override
    public void forBlockNeighbors(BlockState blockState, Predicate<BlockPos> predicate) {
        for (BlockPos blockPos : this.getPositions()) {
            if (this.isNeighborDirection(blockState, blockPos)) {
                if (predicate.test(blockPos)) {
                    return;
                }
            }
        }
    }

    private List<BlockPos> getPositions() {
        return BlockPos.betweenClosedStream(-this.getMaxBlockDistance(),
                        -this.getMaxBlockDistance(),
                        -this.getMaxBlockDistance(),
                        this.getMaxBlockDistance(),
                        this.getMaxBlockDistance(),
                        this.getMaxBlockDistance())
                .filter((BlockPos blockPos) -> !Objects.equals(blockPos, BlockPos.ZERO) && this.isPositionWithinRange(
                        blockPos))
                .map(BlockPos::immutable)
                .toList();
    }

    protected int getMaxBlockDistance() {
        return 1;
    }

    abstract boolean isPositionWithinRange(BlockPos blockPos);
}
