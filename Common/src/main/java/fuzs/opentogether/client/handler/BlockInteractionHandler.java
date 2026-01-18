package fuzs.opentogether.client.handler;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.util.OpenTogetherHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlockInteractionHandler {
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
                if (blockState.getBlock() instanceof DoorBlock) {
                    Direction neighborDirection = OpenTogetherHelper.getDoubleDoorDirection(blockState);
                    BlockPos neighborPosition = blockPos.relative(neighborDirection);
                    BlockState neighborBlockState = minecraft.level.getBlockState(neighborPosition);
                    if (isDoubleDoor(blockState, neighborBlockState)
                            && blockState.getValue(DoorBlock.OPEN) == neighborBlockState.getValue(DoorBlock.OPEN)) {
                        isProcessingInteraction = true;
                        minecraft.hitResult = new BlockHitResult(Vec3.atCenterOf(neighborPosition),
                                blockHitResult.getDirection(),
                                neighborPosition,
                                false);
                        minecraft.startUseItem();
                        isProcessingInteraction = false;
                        minecraft.hitResult = blockHitResult;
                    }
                }
            }
        }

        return EventResult.PASS;
    }

    private static boolean isDoubleDoor(BlockState blockState, BlockState neighborBlockState) {
        if (!OpenTogether.CONFIG.get(ClientConfig.class).doubleDoors.contains(blockState.getBlock())) {
            return false;
        }

        if (!OpenTogether.CONFIG.get(ClientConfig.class).doubleDoors.contains(neighborBlockState.getBlock())) {
            return false;
        }

        return OpenTogetherHelper.isDoubleDoor(blockState, neighborBlockState);
    }
}
