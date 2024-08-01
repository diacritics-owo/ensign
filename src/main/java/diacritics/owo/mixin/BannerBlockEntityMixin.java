package diacritics.owo.mixin;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import diacritics.owo.Ensign;
import diacritics.owo.util.BannerType;
import diacritics.owo.util.BannerTypeProvider;

// TODO: /data modify requires leaving and rejoining the world (maybe just reloading the chunk?) to
// visually take effect when setting the type to regular
@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin implements BannerTypeProvider {
  private BannerType bannerType = BannerType.DEFAULT;

  public void setBannerType(BannerType bannerType) {
    this.bannerType = bannerType;
  }

  public BannerType getBannerType() {
    return bannerType;
  }

  @Inject(at = @At("TAIL"), method = "readNbt")
  public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup,
      CallbackInfo info) {
    if (nbt.contains("type")) {
      BannerType.CODEC.parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("type"))
          .resultOrPartial(
              patterns -> Ensign.LOGGER.error("Failed to parse banner type: '{}'", patterns))
          .ifPresent(bannerType -> this.bannerType = bannerType);
    }
  }

  @Inject(at = @At("TAIL"), method = "writeNbt")
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup,
      CallbackInfo info) {
    if (!this.bannerType.equals(BannerType.DEFAULT)) {
      nbt.put("type", BannerType.CODEC
          .encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.bannerType).getOrThrow());
    }
  }

  @Inject(at = @At("TAIL"), method = "readComponents")
  protected void readComponents(BlockEntity.ComponentsAccess components, CallbackInfo info) {
    this.bannerType = components.getOrDefault(Ensign.BANNER_TYPE, BannerType.DEFAULT);
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
