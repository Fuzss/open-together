package fuzs.opentogether.fabric.client;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.OpenTogetherClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class OpenTogetherFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(OpenTogether.MOD_ID, OpenTogetherClient::new);
    }
}
