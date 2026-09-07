package fuzs.opentogether.common.config;

import fuzs.opentogether.common.OpenTogether;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public final class SharedState extends SharedConfig {
    public static final StreamCodec<ByteBuf, SharedConfig> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL,
            SharedConfig::getDoubleDoorsOption,
            ByteBufCodecs.BOOL,
            SharedConfig::getDoubleFenceGatesOption,
            ByteBufCodecs.BOOL,
            SharedConfig::getDoubleTrapdoorsOption,
            SharedState::new);

    private final Optional<Boolean> allBlocks;
    private final boolean doubleDoors;
    private final boolean doubleFenceGates;
    private final boolean doubleTrapdoors;

    SharedState(boolean doubleDoors, boolean doubleFenceGates, boolean doubleTrapdoors) {
        this(Optional.empty(), doubleDoors, doubleFenceGates, doubleTrapdoors);
    }

    SharedState(Optional<Boolean> allBlocks, boolean doubleDoors, boolean doubleFenceGates, boolean doubleTrapdoors) {
        this.allBlocks = allBlocks;
        this.doubleDoors = doubleDoors;
        this.doubleFenceGates = doubleFenceGates;
        this.doubleTrapdoors = doubleTrapdoors;
    }

    @Override
    boolean getDoubleDoorsOption() {
        return this.doubleDoors;
    }

    @Override
    boolean getDoubleFenceGatesOption() {
        return this.doubleFenceGates;
    }

    @Override
    boolean getDoubleTrapdoorsOption() {
        return this.doubleTrapdoors;
    }

    @Override
    public boolean getAllBlocksOption() {
        return this.allBlocks.orElseGet(() -> OpenTogether.CONFIG.get(CommonConfig.class).getAllBlocksOption());
    }

    @Override
    public SharedConfig setAllBlocks(boolean allBlocks) {
        return new SharedState(Optional.of(allBlocks),
                this.getDoubleDoorsOption(),
                this.getDoubleFenceGatesOption(),
                this.getDoubleTrapdoorsOption());
    }
}
