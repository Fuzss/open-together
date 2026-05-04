package fuzs.opentogether.neoforge;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.data.tags.ModBlockTagProvider;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(OpenTogether.MOD_ID)
public class OpenTogetherNeoForge {

    public OpenTogetherNeoForge() {
        // This is for testing the client-only functionality in a development environment.
        if (!ModLoaderEnvironment.INSTANCE.isDevelopmentEnvironment(OpenTogether.MOD_ID)
                || ModLoaderEnvironment.INSTANCE.isClient()) {
            ModConstructor.construct(OpenTogether.MOD_ID, OpenTogether::new);
        }

        DataProviderHelper.registerDataProviders(OpenTogether.MOD_ID, ModBlockTagProvider::new);
    }
}
