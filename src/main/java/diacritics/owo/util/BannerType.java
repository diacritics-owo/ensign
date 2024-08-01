package diacritics.owo.util;

import java.util.function.IntFunction;
import org.jetbrains.annotations.Nullable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum BannerType implements StringIdentifiable {
  REGULAR(0, "regular"), SWALLOWTAIL(1, "swallowtail");

  public static final BannerType DEFAULT = REGULAR;

  private static final IntFunction<BannerType> BY_ID = ValueLists
      .createIdToValueFunction(BannerType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO);
  @SuppressWarnings("deprecation")
  public static final StringIdentifiable.EnumCodec<BannerType> CODEC =
      StringIdentifiable.createCodec(BannerType::values);
  public static final PacketCodec<ByteBuf, BannerType> PACKET_CODEC =
      PacketCodecs.indexed(BY_ID, BannerType::getId);

  private final int id;
  private final String name;

  private BannerType(final int id, final String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public MutableText getTooltipText() {
    return Text.translatable("banner_type.ensign." + this.name);
  }

  public static BannerType byId(int id) {
    return (BannerType) BY_ID.apply(id);
  }

  @Nullable
  public static BannerType byName(String name, @Nullable BannerType defaultType) {
    @SuppressWarnings("deprecation")
    BannerType dyeColor = (BannerType) CODEC.byId(name);
    return dyeColor != null ? dyeColor : defaultType;
  }

  public String toString() {
    return this.name;
  }

  @Override
  public String asString() {
    return this.name;
  }
}
