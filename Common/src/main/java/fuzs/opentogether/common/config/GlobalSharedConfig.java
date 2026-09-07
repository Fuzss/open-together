package fuzs.opentogether.common.config;

public interface GlobalSharedConfig extends SharedConfig {
    default boolean openDoubleDoorsTogether() {
        return this.getAllBlocksOption() && this.getDoubleDoorsOption();
    }

    default boolean openDoubleFenceGatesTogether() {
        return this.getAllBlocksOption() && this.getDoubleFenceGatesOption();
    }

    default boolean openDoubleTrapdoorsTogether() {
        return this.getAllBlocksOption() && this.getDoubleTrapdoorsOption();
    }

    boolean getAllBlocksOption();

    boolean getDoubleDoorsOption();

    boolean getDoubleFenceGatesOption();

    boolean getDoubleTrapdoorsOption();

    default GlobalSharedConfig plainCopy() {
        return new SharedState(this.getDoubleDoorsOption(),
                this.getDoubleFenceGatesOption(),
                this.getDoubleTrapdoorsOption());
    }

    default GlobalSharedConfig setAllBlocks(boolean allBlocks) {
        return this;
    }
}
