package diacritics.owo.registry;

import java.util.Map;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import diacritics.owo.Ensign;
import diacritics.owo.block.entity.BannerTypes;
import diacritics.owo.client.render.block.entity.BannerTypeModel;
import net.minecraft.Bootstrap;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.Registries.Initializer;
import net.minecraft.util.Identifier;

public class EnsignClientRegistries {
  private static final Map<Identifier, Supplier<?>> DEFAULT_ENTRIES = Maps.newLinkedHashMap();
  public static final Registry<BannerTypeModel> BANNER_TYPE_MODEL;

  public EnsignClientRegistries() {}

  private static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key,
      Initializer<T> initializer) {
    return create(key, new SimpleRegistry<>(key, Lifecycle.stable(), false), initializer);
  }

  private static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key,
      R registry, Initializer<T> initializer) {
    Bootstrap.ensureBootstrapped(() -> {
      return "registry " + String.valueOf(key);
    });
    Identifier identifier = key.getValue();
    DEFAULT_ENTRIES.put(identifier, () -> {
      return initializer.run(registry);
    });
    return registry;
  }

  public static void bootstrap() {
    init();
  }

  private static void init() {
    DEFAULT_ENTRIES.forEach((id, initializer) -> {
      if (initializer.get() == null) {
        Ensign.LOGGER.error("Unable to bootstrap registry '{}'", id);
      }
    });
  }

  static {
    BANNER_TYPE_MODEL = create(EnsignClientRegistryKeys.BANNER_TYPE_MODEL, (registry) -> {
      return BannerTypes.REGULAR;
    });
  }
}
