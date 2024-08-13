package diacritics.owo.registry;

import diacritics.owo.Ensign;
import diacritics.owo.block.entity.BannerType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class EnsignRegistryKeys {
  public static final Identifier ROOT = Identifier.ofVanilla("root");
  public static final RegistryKey<Registry<BannerType>> BANNER_TYPE = of("banner_type");

  public EnsignRegistryKeys() {}

  private static <T> RegistryKey<Registry<T>> of(String id) {
    return RegistryKey.ofRegistry(Ensign.identifier(id));
  }

  public static String getPath(RegistryKey<? extends Registry<?>> registryRef) {
    return registryRef.getValue().getPath();
  }

  public static String getTagPath(RegistryKey<? extends Registry<?>> registryRef) {
    return "tags/" + registryRef.getValue().getPath();
  }
}

