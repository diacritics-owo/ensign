package diacritics.owo.mixin;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Iterator;
import org.spongepowered.asm.mixin.Mixin;
import diacritics.owo.block.entity.BannerType;
import diacritics.owo.block.entity.BannerTypeProvider;
import diacritics.owo.component.type.BannerTypeComponent;
import diacritics.owo.registry.EnsignRegistries;

@Mixin(AbstractBannerBlock.class)
abstract public class AbstractBannerBlockMixin extends BlockWithEntity {
  protected AbstractBannerBlockMixin(Settings settings) {
    super(settings);
  }

  @Override
  protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world,
      BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    if (stack.itemMatches(Registries.ITEM.getEntry(Items.SHEARS))) {
      if (!world.isClient) {
        BannerTypeProvider blockEntity = ((BannerTypeProvider) world.getBlockEntity(pos));

        Iterator<BannerType> iterator = EnsignRegistries.BANNER_TYPE.iterator();
        BannerType first = iterator.next();
        boolean found =
            first.identifier().equals(blockEntity.getBannerType().type().value().identifier());

        while (iterator.hasNext()) {
          if (found) {
            break;
          }

          BannerType type = iterator.next();
          if (type.identifier().equals(blockEntity.getBannerType().type().value().identifier())) {
            found = true;
          }
        }

        BannerTypeComponent newType =
            found ? new BannerTypeComponent(iterator.hasNext() ? iterator.next() : first)
                : BannerTypeComponent.DEFAULT;

        blockEntity.setBannerType(newType);
        world.markDirty(pos);
        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
      }

      return ItemActionResult.SUCCESS;
    }

    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }
}
