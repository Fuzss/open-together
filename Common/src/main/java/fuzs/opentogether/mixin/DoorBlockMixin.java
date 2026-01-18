package fuzs.opentogether.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.util.OpenTogetherHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DoorBlock.class)
abstract class DoorBlockMixin extends Block {
    @Shadow
    @Final
    private static BooleanProperty OPEN;
    @Shadow
    @Final
    private static EnumProperty<DoorHingeSide> HINGE;
    @Shadow
    @Final
    private static BooleanProperty POWERED;

    public DoorBlockMixin(Properties properties) {
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
            if (OpenTogetherHelper.isCommonDoubleDoor(blockState, neighborBlockState)) {
                return blockState.getBlock()
                        .withPropertiesOf(neighborBlockState)
                        .setValue(HINGE, blockState.getValue(HINGE));
            }
        }

        return blockState;
    }

    @WrapWithCondition(method = "onExplosionHit",
                       at = @At(value = "INVOKE",
                                target = "Lnet/minecraft/world/level/block/DoorBlock;setOpen(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Z)V"))
    protected boolean onExplosionHit(DoorBlock doorBlock, Entity entity, Level level, BlockState blockState, BlockPos blockPos, boolean isOpen) {
        if (!OpenTogether.CONFIG.getHolder(ServerConfig.class).isAvailable()
                || !OpenTogether.CONFIG.get(ServerConfig.class).supportsCurrentEnvironment(level.isClientSide())) {
            return true;
        }

        // The wind charge will trigger multiple block parts of the door, leading to block state update chaos in #updateShape.
        // To prevent that, vanilla limits the interaction to the lower door half. We limit it even further to the left door side for double doors.
        if (blockState.getValue(HINGE) == DoorHingeSide.LEFT) {
            return true;
        } else {
            Direction neighborDirection = OpenTogetherHelper.getDoubleDoorDirection(blockState);
            BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
            BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
            return !OpenTogetherHelper.isCommonDoubleDoor(blockState, neighborBlockState);
        }
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
                Level level = context.getLevel();
                BlockPos blockPos = context.getClickedPos();
                Direction neighborDirection = OpenTogetherHelper.getDoubleDoorDirection(blockState);
                BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
                BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
                if (OpenTogetherHelper.isCommonDoubleDoor(blockState, neighborBlockState)) {
                    if (OpenTogetherHelper.hasNeighborDoorSignal(level, neighborBlockPos, neighborBlockState)) {
                        return blockState.setValue(POWERED, Boolean.TRUE).setValue(OPEN, Boolean.TRUE);
                    }
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
            Direction neighborDirection = OpenTogetherHelper.getDoubleDoorDirection(blockState);
            BlockPos neighborBlockPos = blockPos.relative(neighborDirection);
            BlockState neighborBlockState = level.getBlockState(neighborBlockPos);
            if (OpenTogetherHelper.isCommonDoubleDoor(blockState, neighborBlockState)) {
                return OpenTogetherHelper.hasNeighborDoorSignal(level, neighborBlockPos, neighborBlockState);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
