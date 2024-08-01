package diacritics.owo.block;

import diacritics.owo.Ensign;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;

public class EnsignBlocks {
  public static void initialize() {}

  public static final BlockWithItem<SwallowtailBannerBlock> WHITE_SWALLOWTAIL_BANNER =
      register(new SwallowtailBannerBlock(DyeColor.WHITE, AbstractBlock.Settings.create()), "white_swallowtail_banner");

  public static Block register(Block block, String identifier, Boolean registerItem) {
    if (registerItem) {
      return register(block, identifier).block;
    }

    return Registry.register(Registries.BLOCK, Ensign.identifier(identifier), block);
  }

  public static <T extends Block> BlockWithItem<T> register(T block, String identifier) {
    return new BlockWithItem<T>(
        Registry.register(Registries.BLOCK, Ensign.identifier(identifier), block),
        registerItem(block, identifier));
  }

  public static BlockItem registerItem(Block block, String identifier) {
    return Registry.register(Registries.ITEM, Ensign.identifier(identifier),
        new BlockItem(block, new Item.Settings()));
  }

  public static record BlockWithItem<T extends Block>(T block, BlockItem item) {
  }
}
