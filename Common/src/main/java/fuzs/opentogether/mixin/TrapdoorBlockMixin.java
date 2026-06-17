package fuzs.opentogether.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.opentogether.util.DoubleTrapDoorLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TrapDoorBlock.class)
abstract class TrapdoorBlockMixin extends HorizontalDirectionalBlock {

    protected TrapdoorBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyReturnValue(method = "updateShape", at = @At("RETURN"))
    protected BlockState updateShape(BlockState blockState, BlockState originalBlockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos blockPos, BlockPos neighborBlockPos) {
        // This specifically catches the super call, which is the only case that returns the unaltered block state.
        if (blockState == originalBlockState) {
            BlockState newBlockState = DoubleTrapDoorLogic.INSTANCE.updateShape(level,
                    blockState,
                    neighborBlockState,
                    direction);
            if (newBlockState != null) {
                return newBlockState;
            }
        }

        return blockState;
    }

    @ModifyReturnValue(method = "getStateForPlacement", at = @At("RETURN"))
    public BlockState getStateForPlacement(@Nullable BlockState blockState, BlockPlaceContext context) {
        if (blockState != null) {
            BlockState newBlockState = DoubleTrapDoorLogic.INSTANCE.getStateForPlacement(context.getLevel(),
                    context.getClickedPos(),
                    blockState);
            if (newBlockState != null) {
                return newBlockState;
            }
        }

        return blockState;
    }

    @ModifyVariable(method = "neighborChanged", at = @At("STORE"), ordinal = 1)
    protected boolean neighborChanged(boolean hasNeighborSignal, BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!hasNeighborSignal) {
            return DoubleTrapDoorLogic.INSTANCE.hasAnyNeighborSignal(level, blockPos, blockState);
        } else {
            return true;
        }
    }
}
