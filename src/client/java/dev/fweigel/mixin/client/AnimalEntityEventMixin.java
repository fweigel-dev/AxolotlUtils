package dev.fweigel.mixin.client;

import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingHeuristic;
import dev.fweigel.BreedingTracker;
import dev.fweigel.TrackingMode;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public class AnimalEntityEventMixin {

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onHandleEntityEvent(byte id, CallbackInfo ci) {
        if (id == 18) {
            Animal self = (Animal) (Object) this;
            BreedingHeuristic.onHeartParticles(self.getId());

            if (self instanceof Axolotl && BreedingTracker.getMode() == TrackingMode.CLIENT_ONLY) {
                BreedingTracker.incrementFishUsed();
                AxolotlUtilsStorage.save();
            }
        }
    }
}
