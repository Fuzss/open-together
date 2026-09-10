package fuzs.opentogether.common.config;

public interface GlobalSharedConfig extends SharedConfig {
    default boolean openDoubleDoorsTogether() {
        return this.getAnyBlocksOption() && this.getDoubleDoorsOption();
    }

    default boolean openDoubleFenceGatesTogether() {
        return this.getAnyBlocksOption() && this.getDoubleFenceGatesOption();
    }

    default boolean openDoubleTrapdoorsTogether() {
        return this.getAnyBlocksOption() && this.getDoubleTrapdoorsOption();
    }

    boolean getAnyBlocksOption();

    boolean getDoubleDoorsOption();

    boolean getDoubleFenceGatesOption();

    boolean getDoubleTrapdoorsOption();

    default GlobalSharedConfig plainCopy() {
        return new SharedState(this.getDoubleDoorsOption(),
                this.getDoubleFenceGatesOption(),
                this.getDoubleTrapdoorsOption());
    }

    default GlobalSharedConfig setAnyBlocks(boolean allBlocks) {
        return this;
    }
}
