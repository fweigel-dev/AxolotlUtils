package dev.fweigel;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public final class BreedingHeuristic {

    static final BreedingStateMachine state = new BreedingStateMachine(() -> {
        BreedingTracker.increment();
        AxolotlUtilsStorage.save();
    });

    public static void registerEvents() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (BreedingTracker.getMode() != TrackingMode.CLIENT_ONLY) {
                return InteractionResult.PASS;
            }
            Minecraft mc = Minecraft.getInstance();
            if (player != mc.player || !(entity instanceof Axolotl)) {
                return InteractionResult.PASS;
            }
            if (!player.getItemInHand(hand).is(ItemTags.AXOLOTL_FOOD)) {
                return InteractionResult.PASS;
            }

            state.handleFeed(entity.getId(), level.getGameTime());
            return InteractionResult.PASS;
        });
    }

    public static void onHeartParticles(int entityId) {
        if (BreedingTracker.getMode() != TrackingMode.CLIENT_ONLY) {
            return;
        }
        state.handleHeartParticles(entityId);
    }

    public static void reset() {
        state.reset();
    }

    private BreedingHeuristic() {}
}
