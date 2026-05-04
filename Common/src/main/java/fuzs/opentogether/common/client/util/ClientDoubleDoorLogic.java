package fuzs.opentogether.common.client.util;

import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.util.DoubleDoorLogic;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.world.level.block.Block;

public class ClientDoubleDoorLogic extends DoubleDoorLogic implements ClientDoubleBlockLogic {
    public static final ClientDoubleDoorLogic INSTANCE = new ClientDoubleDoorLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleDoors;
    }
}
