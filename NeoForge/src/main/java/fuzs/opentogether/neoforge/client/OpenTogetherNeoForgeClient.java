package fuzs.opentogether.neoforge.client;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.OpenTogetherClient;
import fuzs.opentogether.common.data.client.ModLanguageProvider;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = OpenTogether.MOD_ID, dist = Dist.CLIENT)
public class OpenTogetherNeoForgeClient {

    public OpenTogetherNeoForgeClient() {
        ClientModConstructor.construct(OpenTogether.MOD_ID, OpenTogetherClient::new);
        DataProviderHelper.registerDataProviders(OpenTogether.MOD_ID, ModLanguageProvider::new);
    }
}
