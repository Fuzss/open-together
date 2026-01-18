package fuzs.opentogether.neoforge.client;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.client.OpenTogetherClient;
import fuzs.opentogether.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
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
