package diacritics.owo.block;

import diacritics.owo.Ensign;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;

public class EnsignBlocks {
  public static void initialize() {}

  public static final Block WHITE_SWALLOWTAIL_BANNER = register(
      new SwallowtailBannerBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_BANNER)),
      "white_swallowtail_banner");
  public static final Block WHITE_WALL_SWALLOWTAIL_BANNER =
      register(
          new WallSwallowtailBannerBlock(DyeColor.WHITE,
              AbstractBlock.Settings.copy(Blocks.WHITE_WALL_BANNER)),
          "white_wall_swallowtail_banner");

  public static Block register(Block block, String identifier) {
    return Registry.register(Registries.BLOCK, Ensign.identifier(identifier), block);
  }
}
