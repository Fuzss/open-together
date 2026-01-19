package fuzs.opentogether.client.util;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.util.DoubleBlockLogic;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ClientDoubleBlockLogic extends DoubleBlockLogic {
    @Override
    default boolean isEnabledGlobally(boolean isClientSide) {
        return OpenTogether.CONFIG.getHolder(ClientConfig.class).isAvailable()
                && OpenTogether.CONFIG.get(ClientConfig.class).supportsCurrentEnvironment(isClientSide)
                && this.isEnabled(OpenTogether.CONFIG.get(ClientConfig.class));
    }

    @Override
    default boolean isValidDoubleBlock(BlockState blockState) {
        return this.getBlockDataSet(OpenTogether.CONFIG.get(ClientConfig.class)).contains(blockState.getBlock());
    }

    ConfigDataSet<Block> getBlockDataSet(ClientConfig clientConfig);
}
