package diacritics.owo.component.type;

import com.mojang.serialization.Codec;
import diacritics.owo.Ensign;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record BannerTypeComponent(Identifier type) {
  public static final BannerTypeComponent DEFAULT =
      new BannerTypeComponent(Ensign.identifier("regular"));
  public static final Codec<BannerTypeComponent> CODEC;
  public static final PacketCodec<RegistryByteBuf, BannerTypeComponent> PACKET_CODEC;

  public BannerTypeComponent(Identifier type) {
    this.type = type;
  }

  public MutableText getTooltipText() {
    return Text.translatable(this.type.toTranslationKey("banner.shape"));
  }

  public Identifier type() {
    return this.type;
  }

  static {
    CODEC = Identifier.CODEC.xmap(BannerTypeComponent::new, (component) -> {
      return (component == null ? BannerTypeComponent.DEFAULT : component).type;
    });
    PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, BannerTypeComponent::type,
        BannerTypeComponent::new);
  }
}
