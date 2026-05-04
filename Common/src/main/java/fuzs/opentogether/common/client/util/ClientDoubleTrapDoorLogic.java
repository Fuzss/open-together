package fuzs.opentogether.common.client.util;

import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.util.DoubleTrapDoorLogic;
import fuzs.puzzleslib.common.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

public class ClientDoubleTrapDoorLogic extends DoubleTrapDoorLogic implements ClientDoubleBlockLogic {
    public static final ClientDoubleTrapDoorLogic INSTANCE = new ClientDoubleTrapDoorLogic();

    @Override
    public ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig) {
        return clientConfig.doubleTrapdoors;
    }

    @Override
    public int getMaxBlockDistance() {
        return Math.max(1, Mth.floor(Minecraft.getInstance().player.blockInteractionRange() - 1));
    }
}
