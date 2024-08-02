package diacritics.owo.client.render.block.entity;

import java.util.function.BiConsumer;
import org.apache.commons.lang3.function.TriConsumer;
import diacritics.owo.Ensign;
import diacritics.owo.registry.EnsignClientRegistries;
import diacritics.owo.util.ModelParameters;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

// TODO: the textures on the back of the right tail and tongue are messed up

public class BannerTypeModels {
  public static void initialize() {}

  public static final BannerTypeModel REGULAR = register("regular", (flag, parameters) -> {
    flag.addChild("regular", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F,
        parameters.width(), parameters.height(), 1.0F), ModelTransform.NONE);
  }, (flag, parameters, visible) -> {
    flag.getChild("regular").visible = visible;
  });
  public static final BannerTypeModel SWALLOWTAIL = register("swallowtail", (flag, parameters) -> {
    ModelPartData swallowtail = flag.addChild("swallowtail",
        ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F, parameters.width(),
            parameters.height() - parameters.swallowtailParameters().height(), 1.0F),
        ModelTransform.NONE);

    for (int i = 0; i < parameters.swallowtailParameters().height(); i +=
        parameters.swallowtailParameters().stepY()) {
      final int Y = parameters.swallowtailParameters().height() - i;
      final int MIDDLE = parameters.width() / 2;
      final int I_X = parameters.swallowtailParameters().stepX()
          * (i / parameters.swallowtailParameters().stepY());
      final int TAIL_WIDTH = MIDDLE - I_X;

      swallowtail.addChild("ltail" + i,
          ModelPartBuilder.create().uv(0, parameters.height() - Y).cuboid(-10.0F,
              parameters.height() - Y, -2.0F, TAIL_WIDTH,
              parameters.swallowtailParameters().stepY(), 1.0F),
          ModelTransform.NONE);

      swallowtail.addChild("rtail" + i,
          ModelPartBuilder.create().uv(MIDDLE + I_X, parameters.height() - Y).cuboid(
              -10.0F + (MIDDLE + I_X), parameters.height() - Y, -2.0F, TAIL_WIDTH,
              parameters.swallowtailParameters().stepY(), 1.0F),
          ModelTransform.NONE);
    }
  }, (flag, parameters, visible) -> {
    ModelPart swallowtail = flag.getChild("swallowtail");

    swallowtail.visible = visible;
    for (int i = 0; i < parameters.swallowtailParameters().height(); i +=
        parameters.swallowtailParameters().stepY()) {
      swallowtail.getChild("ltail" + i).visible = visible;
      swallowtail.getChild("rtail" + i).visible = visible;
    }
  });
  public static final BannerTypeModel TONGUED_SWALLOWTAIL =
      register("tongued_swallowtail", (flag, parameters) -> {
        ModelPartData swallowtail = flag.addChild("tongued_swallowtail",
            ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F, parameters.width(),
                parameters.height() - parameters.swallowtailParameters().height(), 1.0F),
            ModelTransform.NONE);

        for (int i = 0; i < parameters.swallowtailParameters().height(); i +=
            parameters.swallowtailParameters().stepY()) {
          final int Y = parameters.swallowtailParameters().height() - i;
          final int MIDDLE = parameters.width() / 2;
          final int I_X = parameters.swallowtailParameters().stepX()
              * (i / parameters.swallowtailParameters().stepY());
          final int TAIL_WIDTH = MIDDLE - I_X;

          swallowtail.addChild("ltail" + i,
              ModelPartBuilder.create().uv(0, parameters.height() - Y).cuboid(-10.0F,
                  parameters.height() - Y, -2.0F, TAIL_WIDTH,
                  parameters.swallowtailParameters().stepY(), 1.0F),
              ModelTransform.NONE);

          swallowtail.addChild("rtail" + i,
              ModelPartBuilder.create().uv(MIDDLE + I_X, parameters.height() - Y).cuboid(
                  -10.0F + (MIDDLE + I_X), parameters.height() - Y, -2.0F, TAIL_WIDTH,
                  parameters.swallowtailParameters().stepY(), 1.0F),
              ModelTransform.NONE);

          final int TONGUE_WIDTH =
              TAIL_WIDTH > (parameters.width() / 3.0F) ? (parameters.width() - (2 * TAIL_WIDTH))
                  : TAIL_WIDTH;
          final int TONGUE_X = MIDDLE - (TONGUE_WIDTH / 2);

          swallowtail.addChild("tongue" + i,
              ModelPartBuilder.create().uv(TONGUE_X, parameters.height() - Y).cuboid(
                  -10.0F + TONGUE_X, parameters.height() - Y, -2.0F, TONGUE_WIDTH,
                  parameters.swallowtailParameters().stepY(), 1.0F),
              ModelTransform.NONE);
        }
      }, (flag, parameters, visible) -> {
        ModelPart tonguedSwallowtail = flag.getChild("tongued_swallowtail");

        tonguedSwallowtail.visible = visible;
        for (int i = 0; i < parameters.swallowtailParameters().height(); i +=
            parameters.swallowtailParameters().stepY()) {
          tonguedSwallowtail.getChild("ltail" + i).visible = visible;
          tonguedSwallowtail.getChild("rtail" + i).visible = visible;
          tonguedSwallowtail.getChild("tongue" + i).visible = visible;
        }
      });
  public static final BannerTypeModel ROUNDED = register("rounded", (flag, parameters) -> {
    ModelPartData rounded =
        flag.addChild("rounded", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F,
            parameters.width(), parameters.height() - 10.0F, 1.0F), ModelTransform.NONE);

    int[] widths = {20, 20, 20, 18, 18, 16, 16, 14, 10, 6};

    for (int i = 0; i < 10; i++) {
      final int Y = 10 - i;
      final int MIDDLE = parameters.width() / 2;

      rounded.addChild("chord" + i,
          ModelPartBuilder.create().uv(MIDDLE - (widths[i] / 2), parameters.height() - Y).cuboid(
              -10.0F + MIDDLE - (widths[i] / 2), parameters.height() - Y, -2.0F, widths[i], 1.0F,
              1.0F),
          ModelTransform.NONE);
    }
  }, (flag, parameters, visible) -> {
    ModelPart rounded = flag.getChild("rounded");

    rounded.visible = visible;
    for (int i = 0; i < 10; i++) {
      rounded.getChild("chord" + i).visible = visible;
    }
  });

  public static final BannerTypeModel DEFAULT = REGULAR;

  public static BannerTypeModel get(Identifier identifier) {
    BannerTypeModel type = EnsignClientRegistries.BANNER_TYPE_MODEL.get(identifier);
    return type == null ? DEFAULT : type;
  }

  public static BannerTypeModel register(String name,
      BiConsumer<ModelPartData, ModelParameters> modelPartData,
      TriConsumer<ModelPart, ModelParameters, Boolean> setVisibility) {
    return Registry.register(EnsignClientRegistries.BANNER_TYPE_MODEL, Ensign.identifier(name),
        new BannerTypeModel(Ensign.identifier(name), modelPartData, setVisibility));
  }
}

