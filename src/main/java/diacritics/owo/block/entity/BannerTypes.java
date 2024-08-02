package diacritics.owo.block.entity;

import diacritics.owo.Ensign;
import diacritics.owo.registry.EnsignRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BannerTypes {
  public static void initialize() {}

  public static final BannerType REGULAR = register("regular");
  public static final BannerType SWALLOWTAIL = register("swallowtail");
  public static final BannerType TONGUED_SWALLOWTAIL = register("tongued_swallowtail");

  public static final BannerType DEFAULT = REGULAR;

  public static BannerType get(Identifier identifier) {
    BannerType type = EnsignRegistries.BANNER_TYPE.get(identifier);
    return type == null ? DEFAULT : type;
  }

  public static BannerType register(String name) {
    // TODO: translation key
    return Registry.register(EnsignRegistries.BANNER_TYPE, Ensign.identifier(name),
        new BannerType(Ensign.identifier(name), "banner_type.ensign." + name));
  }
}

