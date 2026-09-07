package fuzs.opentogether.common;

import fuzs.opentogether.common.config.CommonConfig;
import fuzs.opentogether.common.config.ServerConfig;
import fuzs.opentogether.common.init.ModRegistry;
import fuzs.opentogether.common.network.ClientboundSharedConfigMessage;
import fuzs.opentogether.common.network.SharedConfigTask;
import fuzs.opentogether.common.world.level.block.DoubleBlockLogic;
import fuzs.opentogether.common.world.level.block.DoubleDoorLogic;
import fuzs.opentogether.common.world.level.block.DoubleFenceGateLogic;
import fuzs.opentogether.common.world.level.block.DoubleTrapDoorLogic;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.common.api.event.v1.server.RegisterConfigurationTasksCallback;
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
            .common(CommonConfig.class)
            .server(ServerConfig.class);
    public static final Collection<DoubleBlockLogic> DOUBLE_BLOCK_LOGIC = List.of(DoubleDoorLogic.INSTANCE,
            DoubleFenceGateLogic.INSTANCE,
            DoubleTrapDoorLogic.INSTANCE);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        RegisterConfigurationTasksCallback.EVENT.register(SharedConfigTask::onRegisterConfigurationTasks);
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.optional();
        context.configurationToClient(ClientboundSharedConfigMessage.class,
                ClientboundSharedConfigMessage.STREAM_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
