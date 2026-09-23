package fuzs.opentogether.common.data.client;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.OpenTogetherClient;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.addKeyCategory(OpenTogether.MOD_ID, OpenTogether.MOD_NAME);
        this.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING,
                "Toggle Opening Blocks Together");
        this.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_STATUS_TRANSLATION_KEY,
                "Open Blocks Together: %s");
        this.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_UNAVAILABLE_TRANSLATION_KEY,
                "Opening blocks together is controlled by the server.");
    }
}
