package fuzs.opentogether.common.config;

public abstract class SharedConfig {
    abstract boolean getDoubleDoorsOption();

    abstract boolean getDoubleFenceGatesOption();

    abstract boolean getDoubleTrapdoorsOption();

    public abstract boolean getAllBlocksOption();

    public final boolean openDoubleDoorsTogether() {
        return this.getAllBlocksOption() && this.getDoubleDoorsOption();
    }

    public final boolean openDoubleFenceGatesTogether() {
        return this.getAllBlocksOption() && this.getDoubleFenceGatesOption();
    }

    public final boolean openDoubleTrapdoorsTogether() {
        return this.getAllBlocksOption() && this.getDoubleTrapdoorsOption();
    }

    public final SharedConfig copy() {
        return new SharedState(this.getDoubleDoorsOption(),
                this.getDoubleFenceGatesOption(),
                this.getDoubleTrapdoorsOption());
    }

    public SharedConfig setAllBlocks(boolean allBlocks) {
        return this;
    }
}
