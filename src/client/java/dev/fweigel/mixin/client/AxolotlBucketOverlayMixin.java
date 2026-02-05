package dev.fweigel.mixin.client;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.AxolotlUtilsConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(GuiGraphics.class)
public class AxolotlBucketOverlayMixin {

    @Unique
    private static final Map<Axolotl.Variant, Identifier> OVERLAY_TEXTURES = Map.of(
            Axolotl.Variant.WILD, Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "textures/item/axolotl_bucket/axolotl_bucket_1.png"),
            Axolotl.Variant.GOLD, Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "textures/item/axolotl_bucket/axolotl_bucket_2.png"),
            Axolotl.Variant.CYAN, Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "textures/item/axolotl_bucket/axolotl_bucket_3.png"),
            Axolotl.Variant.BLUE, Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "textures/item/axolotl_bucket/axolotl_bucket_4.png")
    );

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"))
    private void axolotlutils$renderBucketOverlay(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (!AxolotlUtilsConfig.isColoredBucketsEnabled()) {
            return;
        }

        if (!stack.is(Items.AXOLOTL_BUCKET)) {
            return;
        }

        Axolotl.Variant variant = stack.get(DataComponents.AXOLOTL_VARIANT);
        if (variant == null || variant == Axolotl.Variant.LUCY) {
            return;
        }

        Identifier overlay = OVERLAY_TEXTURES.get(variant);
        if (overlay == null) {
            return;
        }

        GuiGraphics self = (GuiGraphics) (Object) this;
        self.blit(RenderPipelines.GUI_TEXTURED, overlay, x, y, 0, 0, 16, 16, 16, 16);
    }
}
