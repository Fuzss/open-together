package fuzs.opentogether.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.util.OpenTogetherHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TrapDoorBlock.class)
abstract class TrapdoorBlockMixin extends HorizontalDirectionalBlock {
    @Shadow
    @Final
    private static BooleanProperty OPEN;
    @Shadow
    @Final
    private static BooleanProperty POWERED;

    protected TrapdoorBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyReturnValue(method = "updateShape", at = @At("RETURN"))
    protected BlockState updateShape(BlockState blockState, BlockState originalBlockState, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborBlockPos, BlockState neighborBlockState, RandomSource randomSource) {
        if (!OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !OpenTogether.CONFIG.get(ServerConfig.class).supportsCurrentEnvironment(level.isClientSide())) {
            return blockState;
        }

        // This specifically catches the super call, which is the only case that returns the unaltered block state.
        if (blockState == originalBlockState) {
            if (OpenTogetherHelper.isDoubleTrapDoorDirection(blockState.getValue(FACING), direction)) {
                if (OpenTogetherHelper.isCommonDoubleTrapDoor(blockState, neighborBlockState, direction.getAxis())) {
                    return blockState.getBlock()
                            .withPropertiesOf(neighborBlockState)
                            .setValue(FACING, blockState.getValue(FACING));
                }
            }
        }

        return blockState;
    }

    @ModifyReturnValue(method = "getStateForPlacement", at = @At("RETURN"))
    public BlockState getStateForPlacement(@Nullable BlockState blockState, BlockPlaceContext context) {
        if (!OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !OpenTogether.CONFIG.get(ServerConfig.class)
                .supportsCurrentEnvironment(context.getLevel().isClientSide())) {
            return blockState;
        }

        if (blockState != null) {
            if (!blockState.getValue(POWERED) && !blockState.getValue(OPEN)) {
                if (OpenTogetherHelper.hasAnyNeighborTrapDoorSignal(context.getLevel(),
                        context.getClickedPos(),
                        blockState)) {
                    return blockState.setValue(POWERED, Boolean.TRUE).setValue(OPEN, Boolean.TRUE);
                }
            }

            return blockState;
        } else {
            return null;
        }
    }

    @ModifyVariable(method = "neighborChanged", at = @At("STORE"), ordinal = 1)
    protected boolean neighborChanged(boolean hasNeighborSignal, BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !OpenTogether.CONFIG.get(ServerConfig.class).supportsCurrentEnvironment(level.isClientSide())) {
            return hasNeighborSignal;
        }

        if (!hasNeighborSignal) {
            return OpenTogetherHelper.hasAnyNeighborTrapDoorSignal(level, blockPos, blockState);
        } else {
            return true;
        }
    }
}
