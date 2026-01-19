package fuzs.opentogether.client.util;

import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.util.DoubleTrapDoorLogic;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.world.level.block.Block;

public class ClientDoubleTrapDoorLogic extends DoubleTrapDoorLogic implements ClientDoubleBlockLogic {
    public static final DoubleTrapDoorLogic INSTANCE = new ClientDoubleTrapDoorLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleTrapdoors;
    }
}
