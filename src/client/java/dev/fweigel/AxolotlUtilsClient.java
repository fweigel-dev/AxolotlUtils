package dev.fweigel;

import dev.fweigel.network.ClientBreedingNetworkHandler;
import dev.fweigel.ui.AxolotlUtilsScreen;
import dev.fweigel.ui.BreedingOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

public class AxolotlUtilsClient implements ClientModInitializer {
    private static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        configKey = registerConfigKey();

        ClientBreedingNetworkHandler.register();
        BreedingHeuristic.registerEvents();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            AxolotlUtilsStorage.loadForWorld(client);
            ClientBreedingNetworkHandler.onJoin();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            BreedingTracker.resetSession();
            BreedingHeuristic.reset();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientBreedingNetworkHandler.tick();
            while (configKey.consumeClick()) {
                client.setScreen(new AxolotlUtilsScreen());
            }
        });

        HudRenderCallback.EVENT.register(BreedingOverlayRenderer::render);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (AxolotlUtilsConfig.isFishBucketLockEnabled()
                    && player.getItemInHand(hand).is(Items.TROPICAL_FISH_BUCKET)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }

    private static KeyMapping registerConfigKey() {
        KeyMapping.Category keyCategory = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "general"));
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.axolotlutils.config",
                GLFW.GLFW_KEY_U,
                keyCategory));
    }
}
