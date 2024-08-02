package diacritics.owo.registry;

import diacritics.owo.client.render.block.entity.BannerTypeModel;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class EnsignClientRegistryKeys {
  public static final Identifier ROOT = Identifier.ofVanilla("root");
  public static final RegistryKey<Registry<BannerTypeModel>> BANNER_TYPE_MODEL =
      of("banner_type_model");

  public EnsignClientRegistryKeys() {}

  private static <T> RegistryKey<Registry<T>> of(String id) {
    return RegistryKey.ofRegistry(Identifier.ofVanilla(id));
  }

  public static String getPath(RegistryKey<? extends Registry<?>> registryRef) {
    return registryRef.getValue().getPath();
  }

  public static String getTagPath(RegistryKey<? extends Registry<?>> registryRef) {
    return "tags/" + registryRef.getValue().getPath();
  }
}

