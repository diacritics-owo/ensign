package diacritics.owo.mixin;

import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import diacritics.owo.Ensign;
import diacritics.owo.component.type.BannerTypeComponent;
import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {
  @Inject(at = @At("HEAD"), method = "appendBannerTooltip")
  // it doesn't like public static methods? setting it to private works though so eh
  private static void appendBannerTooltip(ItemStack stack, List<Text> tooltip, CallbackInfo info) {
    // TODO: is dark gray better?
    tooltip.add(
        ((BannerTypeComponent) stack.getOrDefault(Ensign.BANNER_TYPE, BannerTypeComponent.DEFAULT))
            .getTooltipText().formatted(Formatting.DARK_AQUA));
  }
}
