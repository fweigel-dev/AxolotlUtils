package dev.fweigel.mixin.client;

import dev.fweigel.BreedingHeuristic;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public class AnimalEntityEventMixin {

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onHandleEntityEvent(byte id, CallbackInfo ci) {
        if (id == 18) {
            BreedingHeuristic.onHeartParticles(((Animal) (Object) this).getId());
        }
    }
}
