package fuzs.opentogether.client;

import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.client.handler.BlockInteractionHandler;
import fuzs.opentogether.config.ClientConfig;
import fuzs.opentogether.config.ServerConfig;
import fuzs.opentogether.config.SharedConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.entity.player.InteractionInputEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class OpenTogetherClient implements ClientModConstructor {
    public static final KeyMapping TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING = KeyMappingHelper.registerUnboundKeyMapping(
            OpenTogether.id("toggle_opening_blocks_together"));
    private static final Component ON_COMPONENT = CommonComponents.OPTION_ON.copy().withStyle(ChatFormatting.GREEN);
    private static final Component OFF_COMPONENT = CommonComponents.OPTION_OFF.copy().withStyle(ChatFormatting.RED);
    public static final String TOGGLE_OPENING_BLOCKS_TOGETHER_STATUS_TRANSLATION_KEY =
            TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING.getName() + ".message";
    public static final String TOGGLE_OPENING_BLOCKS_TOGETHER_UNAVAILABLE_TRANSLATION_KEY =
            TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING.getName() + ".unavailable";

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        InteractionInputEvents.USE.register(BlockInteractionHandler::onUseInteraction);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING,
                KeyActivationHandler.forGame((Minecraft minecraft) -> {
                    SharedConfig sharedConfig = chooseSharedConfig(minecraft);
                    Component component = chooseFeedbackComponent(sharedConfig);
                    minecraft.gui.setOverlayMessage(component, false);
                }));
    }

    /**
     * @see fuzs.reachbehind.handler.AbstractMenuProviderInteraction
     */
    private static @Nullable SharedConfig chooseSharedConfig(Minecraft minecraft) {
        if (!NetworkingHelper.isModPresentServerside(OpenTogether.MOD_ID)) {
            return OpenTogether.CONFIG.get(ClientConfig.class);
        } else if (minecraft.isLocalServer()) {
            return OpenTogether.CONFIG.get(ServerConfig.class);
        } else {
            return null;
        }
    }

    private static Component chooseFeedbackComponent(SharedConfig sharedConfig) {
        if (sharedConfig != null) {
            return Component.translatable(TOGGLE_OPENING_BLOCKS_TOGETHER_STATUS_TRANSLATION_KEY,
                    sharedConfig.flipOpenBlocksTogether() ? ON_COMPONENT : OFF_COMPONENT);
        } else {
            return Component.translatable(TOGGLE_OPENING_BLOCKS_TOGETHER_UNAVAILABLE_TRANSLATION_KEY);
        }
    }
}
