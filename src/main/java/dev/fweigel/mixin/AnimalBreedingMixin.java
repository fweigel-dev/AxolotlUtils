package dev.fweigel.mixin;

import dev.fweigel.network.ServerBreedingNetworkHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public abstract class AnimalBreedingMixin {

    @Shadow
    public abstract ServerPlayer getLoveCause();

    @Inject(method = "finalizeSpawnChildFromBreeding", at = @At("HEAD"))
    private void onAxolotlBred(ServerLevel level, Animal partner, AgeableMob baby, CallbackInfo ci) {
        if (!(baby instanceof Axolotl)) {
            return;
        }

        ServerPlayer cause1 = this.getLoveCause();
        ServerPlayer cause2 = partner.getLoveCause();

        if (cause1 != null) {
            ServerBreedingNetworkHandler.notifyBreedingEvent(cause1);
        }
        if (cause2 != null && cause2 != cause1) {
            ServerBreedingNetworkHandler.notifyBreedingEvent(cause2);
        }
    }
}
