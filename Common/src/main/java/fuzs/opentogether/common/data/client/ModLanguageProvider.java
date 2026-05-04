package fuzs.opentogether.common.data.client;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.OpenTogetherClient;
import fuzs.puzzleslib.common.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.addKeyCategory(OpenTogether.MOD_ID, OpenTogether.MOD_NAME);
        translationBuilder.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING,
                "Toggle Opening Blocks Together");
        translationBuilder.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_STATUS_TRANSLATION_KEY,
                "Open Blocks Together: %s");
        translationBuilder.add(OpenTogetherClient.TOGGLE_OPENING_BLOCKS_TOGETHER_UNAVAILABLE_TRANSLATION_KEY,
                "Opening blocks together is controlled by the server.");
    }
}
