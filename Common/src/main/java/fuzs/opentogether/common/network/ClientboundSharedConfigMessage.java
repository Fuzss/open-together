package fuzs.opentogether.common.network;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.config.CommonConfig;
import fuzs.opentogether.common.config.GlobalSharedConfig;
import fuzs.opentogether.common.config.SharedConfig;
import fuzs.opentogether.common.config.SharedState;
import fuzs.puzzleslib.common.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.common.api.network.v4.message.configuration.ClientboundConfigurationMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundSharedConfigMessage(boolean openAllBlocksTogether,
                                             GlobalSharedConfig sharedConfig) implements ClientboundConfigurationMessage {
    public static final StreamCodec<ByteBuf, ClientboundSharedConfigMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientboundSharedConfigMessage::openAllBlocksTogether,
            SharedState.STREAM_CODEC,
            ClientboundSharedConfigMessage::sharedConfig,
            ClientboundSharedConfigMessage::new);

    public static ClientboundSharedConfigMessage of() {
        return of(OpenTogether.CONFIG.get(CommonConfig.class));
    }

    public static ClientboundSharedConfigMessage of(GlobalSharedConfig sharedConfig) {
        return new ClientboundSharedConfigMessage(sharedConfig.getAllBlocksOption(), sharedConfig.plainCopy());
    }

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                SharedConfig sharedConfig = this.fetchSharedConfig(context.client().isLocalServer());
                OpenTogether.CONFIG.get(CommonConfig.class).setSharedConfig(sharedConfig);
            }

            private SharedConfig fetchSharedConfig(boolean isLocalServer) {
                return isLocalServer ? ClientboundSharedConfigMessage.this.sharedConfig() :
                        ClientboundSharedConfigMessage.this.sharedConfig()
                                .setAllBlocks(ClientboundSharedConfigMessage.this.openAllBlocksTogether());
            }
        };
    }
}
