package fuzs.opentogether.common.network;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.config.CommonConfig;
import fuzs.opentogether.common.config.SharedState;
import fuzs.puzzleslib.common.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.common.api.network.v4.message.configuration.ClientboundConfigurationMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundSharedConfigMessage(boolean openAllBlocksTogether,
                                             SharedState state) implements ClientboundConfigurationMessage {
    public static final StreamCodec<ByteBuf, ClientboundSharedConfigMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientboundSharedConfigMessage::openAllBlocksTogether,
            SharedState.STREAM_CODEC,
            ClientboundSharedConfigMessage::state,
            ClientboundSharedConfigMessage::new);

    public static ClientboundSharedConfigMessage of() {
        return of(OpenTogether.CONFIG.get(CommonConfig.class));
    }

    public static ClientboundSharedConfigMessage of(CommonConfig commonConfig) {
        return new ClientboundSharedConfigMessage(commonConfig.openAllBlocksTogether(),
                SharedState.copyOf(commonConfig));
    }

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                SharedState sharedState = this.fetchSharedState(context.client().isLocalServer());
                OpenTogether.CONFIG.get(CommonConfig.class).setSharedConfig(sharedState);
            }

            private SharedState fetchSharedState(boolean isLocalServer) {
                return isLocalServer ? ClientboundSharedConfigMessage.this.state() :
                        ClientboundSharedConfigMessage.this.state()
                                .openAllBlocksTogether(ClientboundSharedConfigMessage.this.openAllBlocksTogether());
            }
        };
    }
}
