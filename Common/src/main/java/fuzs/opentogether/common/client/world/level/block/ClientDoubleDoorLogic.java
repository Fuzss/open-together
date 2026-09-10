package fuzs.opentogether.common.client.world.level.block;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.world.level.block.DoubleDoorLogic;
import net.minecraft.world.level.block.state.BlockState;

public class ClientDoubleDoorLogic extends DoubleDoorLogic {
    public static final ClientDoubleDoorLogic INSTANCE = new ClientDoubleDoorLogic();

    @Override
    public boolean isValidDoubleBlock(BlockState blockState) {
        return OpenTogether.CONFIG.get(ClientConfig.class).validDoubleDoors.contains(blockState.getBlock());
    }
}
