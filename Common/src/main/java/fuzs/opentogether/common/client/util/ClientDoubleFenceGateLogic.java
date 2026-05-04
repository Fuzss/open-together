package fuzs.opentogether.common.client.util;

import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.util.DoubleFenceGateLogic;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

public class ClientDoubleFenceGateLogic extends DoubleFenceGateLogic implements ClientDoubleBlockLogic {
    public static final ClientDoubleFenceGateLogic INSTANCE = new ClientDoubleFenceGateLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleFenceGates;
    }

    @Override
    public int getMaxBlockDistance() {
        return Math.max(1, Mth.floor(Minecraft.getInstance().player.blockInteractionRange() - 1));
    }
}
