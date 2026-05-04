package fuzs.opentogether.common.client.handler;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.util.ClientDoubleBlockLogic;
import fuzs.opentogether.common.client.util.ClientDoubleDoorLogic;
import fuzs.opentogether.common.client.util.ClientDoubleFenceGateLogic;
import fuzs.opentogether.common.client.util.ClientDoubleTrapDoorLogic;
import fuzs.opentogether.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;

public class BlockInteractionHandler {
    private static final Collection<ClientDoubleBlockLogic> CLIENT_DOUBLE_BLOCK_LOGIC = List.of(ClientDoubleDoorLogic.INSTANCE,
            ClientDoubleFenceGateLogic.INSTANCE,
            ClientDoubleTrapDoorLogic.INSTANCE);
    private static boolean isProcessingInteraction;

    public static EventResult onUseInteraction(Minecraft minecraft, LocalPlayer player, InteractionHand interactionHand, HitResult hitResult) {
        if (!OpenTogether.CONFIG.get(ClientConfig.class).supportsCurrentEnvironment(true)) {
            return EventResult.PASS;
        }

        if (!isProcessingInteraction && hitResult.getType() == HitResult.Type.BLOCK) {
            if (player.getItemInHand(interactionHand).isEmpty() || !player.isSecondaryUseActive()) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = minecraft.level.getBlockState(blockPos);
                isProcessingInteraction = true;
                for (ClientDoubleBlockLogic doubleBlockLogic : CLIENT_DOUBLE_BLOCK_LOGIC) {
                    if (processInteraction(minecraft,
                            doubleBlockLogic,
                            blockPos,
                            blockState,
                            blockHitResult.getDirection())) {
                        break;
                    }
                }

                isProcessingInteraction = false;
                minecraft.hitResult = blockHitResult;
            }
        }

        return EventResult.PASS;
    }

    private static boolean processInteraction(Minecraft minecraft, ClientDoubleBlockLogic doubleBlockLogic, BlockPos blockPos, BlockState blockState, Direction direction) {
        if (doubleBlockLogic.getBlockType().isInstance(blockState.getBlock())) {
            Collection<BlockPos> neighborBlockPositions = doubleBlockLogic.getValidDoubleNeighbors(minecraft.level,
                    blockPos,
                    blockState,
                    (Level level, BlockPos neighborBlockPos) -> {
                        return blockState.getValue(BlockStateProperties.OPEN) == level.getBlockState(neighborBlockPos)
                                .getValue(BlockStateProperties.OPEN);
                    },
                    false);
            if (!neighborBlockPositions.isEmpty()) {
                for (BlockPos neighborBlockPos : neighborBlockPositions) {
                    minecraft.hitResult = new BlockHitResult(Vec3.atCenterOf(neighborBlockPos),
                            direction,
                            neighborBlockPos,
                            false);
                    minecraft.startUseItem();
                }

                return true;
            }
        }

        return false;
    }
}
