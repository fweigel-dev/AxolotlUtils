package dev.fweigel.mixin.client;

import dev.fweigel.AxolotlUtilsConfig;
import dev.fweigel.sound.WrappedSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SoundEngine.class)
public class AxolotlSoundMixin {

    @ModifyVariable(method = "play", at = @At("HEAD"), argsOnly = true)
    private SoundInstance modifyAxolotlVolume(SoundInstance sound) {
        String path = sound.getIdentifier().getPath();
        if (path.startsWith("entity.axolotl.")) {
            float configVolume = AxolotlUtilsConfig.getAxolotlVolume();
            if (configVolume < 1.0f) {
                return new WrappedSoundInstance(sound, configVolume);
            }
        }
        return sound;
    }
}
