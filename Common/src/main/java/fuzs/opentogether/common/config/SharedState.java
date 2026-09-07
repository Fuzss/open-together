package fuzs.opentogether.common.config;

import fuzs.opentogether.common.OpenTogether;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record SharedState(Optional<Boolean> openAllBlocksTogether,
                          boolean openDoubleDoorsTogether,
                          boolean openDoubleFenceGatesTogether,
                          boolean openDoubleTrapdoorsTogether) implements SharedConfig {
    public static final StreamCodec<ByteBuf, SharedState> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL,
            SharedState::openDoubleDoorsTogether,
            ByteBufCodecs.BOOL,
            SharedState::openDoubleFenceGatesTogether,
            ByteBufCodecs.BOOL,
            SharedState::openDoubleTrapdoorsTogether,
            SharedState::new);

    public SharedState(boolean openDoubleDoorsTogether, boolean openDoubleFenceGatesTogether, boolean openDoubleTrapdoorsTogether) {
        this(Optional.empty(), openDoubleDoorsTogether, openDoubleFenceGatesTogether, openDoubleTrapdoorsTogether);
    }

    public SharedState openAllBlocksTogether(boolean openAllBlocksTogether) {
        return this.openAllBlocksTogether.isPresent() && this.openAllBlocksTogether.get() == openAllBlocksTogether ?
                this : new SharedState(Optional.of(openAllBlocksTogether),
                this.openDoubleDoorsTogether,
                this.openDoubleFenceGatesTogether,
                this.openDoubleTrapdoorsTogether);
    }

    private boolean openAllBlocksTogetherOverride() {
        return this.openAllBlocksTogether.orElseGet(() -> OpenTogether.CONFIG.get(CommonConfig.class)
                .openAllBlocksTogether());
    }

    @Override
    public boolean openDoubleDoorsTogether() {
        return this.openDoubleDoorsTogether && this.openAllBlocksTogetherOverride();
    }

    @Override
    public boolean openDoubleFenceGatesTogether() {
        return this.openDoubleFenceGatesTogether && this.openAllBlocksTogetherOverride();
    }

    @Override
    public boolean openDoubleTrapdoorsTogether() {
        return this.openDoubleTrapdoorsTogether && this.openAllBlocksTogetherOverride();
    }

    public static SharedState copyOf(SharedConfig sharedConfig) {
        return switch (sharedConfig) {
            case SharedState sharedState -> sharedState;
            case CommonConfig commonConfig -> new SharedState(commonConfig.doubleDoors.openTogether,
                    commonConfig.doubleFenceGates.openTogether,
                    commonConfig.doubleTrapdoors.openTogether);
        };
    }
}
