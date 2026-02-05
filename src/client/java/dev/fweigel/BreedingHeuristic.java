package dev.fweigel;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public final class BreedingHeuristic {

    private static final int FEED_WINDOW = 600;
    private static final int WATCH_WINDOW = 100;
    private static final double RADIUS = 10.0;

    private static int firstFedId = -1;
    private static long firstFedTick = -1;
    private static boolean watchingForBaby = false;
    private static long watchStartTick = -1;
    private static double watchX, watchY, watchZ;

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

            long currentTick = level.getGameTime();
            int entityId = entity.getId();

            if (firstFedId == -1 || currentTick - firstFedTick > FEED_WINDOW) {
                firstFedId = entityId;
                firstFedTick = currentTick;
                watchingForBaby = false;
            } else if (entityId != firstFedId) {
                watchingForBaby = true;
                watchStartTick = currentTick;
                watchX = (entity.getX() + mc.player.getX()) / 2.0;
                watchY = (entity.getY() + mc.player.getY()) / 2.0;
                watchZ = (entity.getZ() + mc.player.getZ()) / 2.0;
                firstFedId = -1;
                firstFedTick = -1;
            }

            return InteractionResult.PASS;
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (BreedingTracker.getMode() != TrackingMode.CLIENT_ONLY) {
                return;
            }
            if (!watchingForBaby || !(entity instanceof Axolotl axolotl)) {
                return;
            }
            if (!axolotl.isBaby()) {
                return;
            }

            long currentTick = level.getGameTime();
            if (currentTick - watchStartTick > WATCH_WINDOW) {
                watchingForBaby = false;
                return;
            }

            double dx = entity.getX() - watchX;
            double dy = entity.getY() - watchY;
            double dz = entity.getZ() - watchZ;
            if (dx * dx + dy * dy + dz * dz <= RADIUS * RADIUS) {
                BreedingTracker.increment();
                AxolotlUtilsStorage.save();
                watchingForBaby = false;
            }
        });
    }

    public static void reset() {
        firstFedId = -1;
        firstFedTick = -1;
        watchingForBaby = false;
        watchStartTick = -1;
    }

    private BreedingHeuristic() {}
}
