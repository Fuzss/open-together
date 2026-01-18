package fuzs.opentogether.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.util.OpenTogetherHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.BiConsumer;

@Mixin(FenceGateBlock.class)
abstract class FenceGateBlockMixin extends HorizontalDirectionalBlock {
    @Shadow
    @Final
    private static BooleanProperty OPEN;
    @Shadow
    @Final
    private static BooleanProperty POWERED;

    protected FenceGateBlockMixin(Properties properties) {
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
            if (OpenTogetherHelper.isDoubleFenceGateDirection(blockState.getValue(FACING), direction)) {
                if (OpenTogetherHelper.isCommonDoubleFenceGate(blockState, neighborBlockState)) {
                    return blockState.getBlock().withPropertiesOf(neighborBlockState);
                }
            }
        }

        return blockState;
    }

    @ModifyExpressionValue(method = "onExplosionHit",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/level/Explosion;canTriggerBlocks()Z"))
    protected boolean onExplosionHit(boolean canTriggerBlocks, BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (!canTriggerBlocks || blockState.getValue(POWERED)) {
            return canTriggerBlocks;
        }

        if (!OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !OpenTogether.CONFIG.get(ServerConfig.class)
                .supportsCurrentEnvironment(serverLevel.isClientSide())) {
            return true;
        }

        // The wind charge will trigger multiple fence gates, leading to block state update chaos in #updateShape.
        // To prevent that, we limit interactions to the fence gate with the lowest coordinate.
        // This is problematic when that fence gate is out of reach for the wind charge, but seems about the best we can do here.
        // This is copied from how vanilla limits door interaction to the lower door half.
        Direction direction = blockState.getValue(FACING);
        for (Direction.Axis axis : Direction.Axis.VALUES) {
            if (axis != direction.getAxis()) {
                BlockState neighborBlockState = serverLevel.getBlockState(blockPos.relative(axis.getNegative()));
                if (OpenTogetherHelper.isCommonDoubleFenceGate(blockState, neighborBlockState)) {
                    return false;
                }
            }
        }

        return true;
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
                if (OpenTogetherHelper.hasAnyNeighborFenceGateSignal(context.getLevel(),
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
            return OpenTogetherHelper.hasAnyNeighborFenceGateSignal(level, blockPos, blockState);
        } else {
            return true;
        }
    }
}
