package dev.fweigel.mixin.client;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.AxolotlUtilsConfig;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxolotlRenderer.class)
public class AxolotlRendererMixin {

    @Unique
    private static final Identifier TEXTURE_RED = Identifier.fromNamespaceAndPath(
            AxolotlUtils.MOD_ID, "textures/entity/axolotl/axolotl_red.png");

    @Unique
    private static final Identifier TEXTURE_GREEN = Identifier.fromNamespaceAndPath(
            AxolotlUtils.MOD_ID, "textures/entity/axolotl/axolotl_green.png");

    // Replaces the axolotl texture when "Easy Find Blue Axolotls" is enabled.
    // Blue axolotls get a green texture, all others get red.
    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void redirectAxolotlTexture(AxolotlRenderState state, CallbackInfoReturnable<Identifier> cir) {
        if (AxolotlUtilsConfig.isHighlightBlueEnabled()) {
            if (state.variant == Axolotl.Variant.BLUE) {
                cir.setReturnValue(TEXTURE_GREEN);
            } else {
                cir.setReturnValue(TEXTURE_RED);
            }
        }
    }
}
