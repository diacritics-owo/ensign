package diacritics.owo.item;

import diacritics.owo.Ensign;
import diacritics.owo.block.EnsignBlocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EnsignItems {
  public static void initialize() {}

  public static final Item SWALLOWTAIL_BANNER =
      register(new BannerItem(EnsignBlocks.WHITE_SWALLOWTAIL_BANNER, EnsignBlocks.WHITE_WALL_SWALLOWTAIL_BANNER,
          new Item.Settings().maxCount(16).component(DataComponentTypes.BANNER_PATTERNS,
              BannerPatternsComponent.DEFAULT)),
          "white_swallowtail_banner");

  public static Item register(Item item, String identifier) {
    return Registry.register(Registries.ITEM, Ensign.identifier(identifier), item);
  }
}
