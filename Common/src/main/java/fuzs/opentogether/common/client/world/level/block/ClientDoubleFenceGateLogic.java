package fuzs.opentogether.common.client.world.level.block;

import fuzs.opentogether.common.world.level.block.DoubleFenceGateLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ClientDoubleFenceGateLogic extends DoubleFenceGateLogic {
    public static final ClientDoubleFenceGateLogic INSTANCE = new ClientDoubleFenceGateLogic();

    @Override
    public int getMaxBlockDistance() {
        return Math.max(1, Mth.floor(Minecraft.getInstance().player.blockInteractionRange() - 1));
    }
}
