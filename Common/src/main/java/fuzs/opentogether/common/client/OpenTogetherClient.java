package fuzs.opentogether.common.client;

import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.client.handler.BlockInteractionHandler;
import fuzs.opentogether.common.config.CommonConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.player.InteractionInputEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.network.v4.NetworkingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

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
        ClientPlayerNetworkEvents.LEAVE.register(OpenTogetherClient::onPlayerLeave);
    }

    private static void onPlayerLeave(LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) {
        OpenTogether.CONFIG.get(CommonConfig.class).resetSharedConfig();
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(TOGGLE_OPENING_BLOCKS_TOGETHER_KEY_MAPPING,
                KeyActivationHandler.forGame((Minecraft minecraft) -> {
                    boolean mayUseToggleKeybind = mayUseToggleKeybind(minecraft);
                    Component component = pickFeedbackComponent(mayUseToggleKeybind);
                    minecraft.gui.hud.setOverlayMessage(component, false);
                }));
    }

    /**
     * @see fuzs.reachbehind.handler.AbstractMenuProviderInteraction
     */
    private static boolean mayUseToggleKeybind(Minecraft minecraft) {
        if (!NetworkingHelper.isModPresentServerside(OpenTogether.MOD_ID)) {
            // The mod is only installed client side, we are using client-only mode which mimics player interactions.
            return true;
        } else if (minecraft.isLocalServer()) {
            // The mod is running in singleplayer, we have full control over both the client & server.
            return true;
        } else {
            // The mod is installed on the multiplayer server, control is out of our hands.
            return false;
        }
    }

    private static Component pickFeedbackComponent(boolean mayUseToggleKeybind) {
        if (mayUseToggleKeybind) {
            return Component.translatable(TOGGLE_OPENING_BLOCKS_TOGETHER_STATUS_TRANSLATION_KEY,
                    OpenTogether.CONFIG.get(CommonConfig.class).toggleAllBlocks() ? ON_COMPONENT :
                            OFF_COMPONENT);
        } else {
            return Component.translatable(TOGGLE_OPENING_BLOCKS_TOGETHER_UNAVAILABLE_TRANSLATION_KEY);
        }
    }
}
