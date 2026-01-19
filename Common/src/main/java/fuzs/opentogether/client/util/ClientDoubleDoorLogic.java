package fuzs.opentogether.client.util;

import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.util.DoubleDoorLogic;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.world.level.block.Block;

public class ClientDoubleDoorLogic extends DoubleDoorLogic implements ClientDoubleBlockLogic {
    public static final DoubleDoorLogic INSTANCE = new ClientDoubleDoorLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleDoors;
    }
}
