package fuzs.opentogether.data.client;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.client.OpenTogetherClient;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

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
