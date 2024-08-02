package diacritics.owo.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import diacritics.owo.registry.EnsignRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public record BannerType(Identifier identifier, String translationKey) {

  public static final Codec<BannerType> CODEC = RecordCodecBuilder.create((instance) -> {
    return instance
        .group(Identifier.CODEC.fieldOf("identifier").forGetter(BannerType::identifier),
            Codec.STRING.fieldOf("translation_key").forGetter(BannerType::translationKey))
        .apply(instance, BannerType::new);
  });
  public static final PacketCodec<RegistryByteBuf, BannerType> PACKET_CODEC;
  public static final Codec<RegistryEntry<BannerType>> ENTRY_CODEC;
  public static final PacketCodec<RegistryByteBuf, RegistryEntry<BannerType>> ENTRY_PACKET_CODEC;

  public BannerType(Identifier identifier, String translationKey) {
    this.identifier = identifier;
    this.translationKey = translationKey;
  }

  public Identifier identifier() {
    return this.identifier;
  }

  public String translationKey() {
    return this.translationKey;
  }

  static {
    PACKET_CODEC = PacketCodec.tuple(Identifier.PACKET_CODEC, BannerType::identifier,
        PacketCodecs.STRING, BannerType::translationKey, BannerType::new);
    ENTRY_CODEC = RegistryElementCodec.of(EnsignRegistryKeys.BANNER_TYPE, CODEC);
    ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(EnsignRegistryKeys.BANNER_TYPE, PACKET_CODEC);
  }
}
