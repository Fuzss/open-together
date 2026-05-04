plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(sharedLibs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin(
            "DoorBlockMixin", "FenceGateBlockMixin", "LevelMixin", "TrapdoorBlockMixin"
        )
    }
}
