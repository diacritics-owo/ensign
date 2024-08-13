package diacritics.owo.mixin;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import diacritics.owo.Ensign;
import diacritics.owo.block.entity.BannerTypeProvider;
import diacritics.owo.component.type.BannerTypeComponent;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin implements BannerTypeProvider {
  private BannerTypeComponent bannerType = BannerTypeComponent.DEFAULT;

  public void setBannerType(BannerTypeComponent bannerType) {
    this.bannerType = bannerType;
  }

  public BannerTypeComponent getBannerType() {
    return bannerType;
  }

  @Inject(at = @At("TAIL"), method = "readNbt")
  public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup,
      CallbackInfo info) {
    if (nbt.contains("type")) {
      BannerTypeComponent.CODEC.parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("type"))
          .resultOrPartial((type) -> {
            Ensign.LOGGER.error("Failed to parse banner type: '{}'", type);
          }).ifPresent((type) -> {
            this.bannerType = type;
          });
    }
  }

  @Inject(at = @At("TAIL"), method = "writeNbt")
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup,
      CallbackInfo info) {
    nbt.put("type", (NbtElement) BannerTypeComponent.CODEC
        .encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.bannerType).getOrThrow());
  }

  @Inject(at = @At("TAIL"), method = "readComponents")
  protected void readComponents(BlockEntity.ComponentsAccess components, CallbackInfo info) {
    this.bannerType = (BannerTypeComponent) components.getOrDefault(Ensign.BANNER_TYPE,
        BannerTypeComponent.DEFAULT);
  }

  @Inject(at = @At("TAIL"), method = "addComponents")
  protected void addComponents(ComponentMap.Builder componentMapBuilder, CallbackInfo info) {
    componentMapBuilder.add(Ensign.BANNER_TYPE, this.bannerType);
  }

  @Inject(at = @At("TAIL"), method = "removeFromCopiedStackNbt")
  public void removeFromCopiedStackNbt(NbtCompound nbt, CallbackInfo info) {
    nbt.remove("type");
  }
}
