package fuzs.opentogether.fabric;

import fuzs.opentogether.common.OpenTogether;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class OpenTogetherFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(OpenTogether.MOD_ID, OpenTogether::new);
    }
}
