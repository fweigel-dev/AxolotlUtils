package dev.fweigel.mixin.client;

import dev.fweigel.AxolotlUtilsConfig;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {

    @ModifyVariable(method = "appendItemLayers", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private ItemStack axolotlutils$stripVariantWhenDisabled(ItemStack stack) {
        if (!AxolotlUtilsConfig.isColoredBucketsEnabled() && stack.is(Items.AXOLOTL_BUCKET)) {
            ItemStack copy = stack.copy();
            copy.remove(DataComponents.AXOLOTL_VARIANT);
            return copy;
        }
        return stack;
    }
}
