package fuzs.opentogether.common;

import fuzs.opentogether.common.config.ClientConfig;
import fuzs.opentogether.common.config.ServerConfig;
import fuzs.opentogether.common.init.ModRegistry;
import fuzs.opentogether.common.util.DoubleBlockLogic;
import fuzs.opentogether.common.util.DoubleDoorLogic;
import fuzs.opentogether.common.util.DoubleFenceGateLogic;
import fuzs.opentogether.common.util.DoubleTrapDoorLogic;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class OpenTogether implements ModConstructor {
    public static final String MOD_ID = "opentogether";
    public static final String MOD_NAME = "Open Together";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .server(ServerConfig.class);
    public static final Collection<DoubleBlockLogic> DOUBLE_BLOCK_LOGIC = List.of(DoubleDoorLogic.INSTANCE,
            DoubleFenceGateLogic.INSTANCE,
            DoubleTrapDoorLogic.INSTANCE);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
