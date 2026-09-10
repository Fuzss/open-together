package fuzs.opentogether.common.client.world.level.block;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.world.level.block.DoubleFenceGateLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class ClientDoubleFenceGateLogic extends DoubleFenceGateLogic {
    public static final ClientDoubleFenceGateLogic INSTANCE = new ClientDoubleFenceGateLogic();

    @Override
    public boolean isValidDoubleBlock(BlockState blockState) {
        return OpenTogether.CONFIG.get(ClientConfig.class).validDoubleFenceGates.contains(blockState.getBlock());
    }

    @Override
    public int getMaxBlockDistance() {
        return Math.max(1, Mth.floor(Minecraft.getInstance().player.blockInteractionRange() - 1));
    }
}
