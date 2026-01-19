package fuzs.opentogether.client.util;

import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.util.DoubleFenceGateLogic;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.world.level.block.Block;

public class ClientDoubleFenceGateLogic extends DoubleFenceGateLogic implements ClientDoubleBlockLogic {
    public static final DoubleFenceGateLogic INSTANCE = new ClientDoubleFenceGateLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleFenceGates;
    }
}
