package diacritics.owo.component.type;

import com.mojang.serialization.Codec;
import diacritics.owo.block.entity.BannerType;
import diacritics.owo.block.entity.BannerTypes;
import diacritics.owo.registry.EnsignRegistries;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record BannerTypeComponent(RegistryEntry<BannerType> type) {
  public static final BannerTypeComponent DEFAULT = new BannerTypeComponent(BannerTypes.REGULAR);
  public static final Codec<BannerTypeComponent> CODEC;
  public static final PacketCodec<RegistryByteBuf, BannerTypeComponent> PACKET_CODEC;

  public BannerTypeComponent(Identifier identifier) {
    this(BannerTypes.get(identifier));
  }

  public BannerTypeComponent(BannerType type) {
    this(EnsignRegistries.BANNER_TYPE.getEntry(type));
  }

  public BannerTypeComponent(RegistryEntry<BannerType> type) {
    this.type = type;
  }

  public MutableText getTooltipText() {
    return Text.translatable(this.type.value().translationKey());
  }

  public RegistryEntry<BannerType> type() {
    return this.type;
  }

  static {
    CODEC = Identifier.CODEC.xmap(BannerTypeComponent::new, (component) -> {
      BannerType type = component.type.value();
      return (type == null ? BannerTypeComponent.DEFAULT.type.value() : type).identifier();
    });
    PACKET_CODEC = PacketCodec.tuple(BannerType.ENTRY_PACKET_CODEC, BannerTypeComponent::type,
        BannerTypeComponent::new);
  }
}
