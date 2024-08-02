package diacritics.owo.client.render.block.entity;

import java.util.function.BiConsumer;
import org.apache.commons.lang3.function.TriConsumer;
import diacritics.owo.util.ModelParameters;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.util.Identifier;

public record BannerTypeModel(Identifier identifier,
    BiConsumer<ModelPartData, ModelParameters> modelPartData,
    TriConsumer<ModelPart, ModelParameters, Boolean> setVisibility) {

  public BannerTypeModel(Identifier identifier) {
    this(identifier, (flag, parameters) -> {
    }, (flag, parameters, visible) -> {
    });
  }

  public BannerTypeModel(Identifier identifier,
      BiConsumer<ModelPartData, ModelParameters> modelPartData,
      TriConsumer<ModelPart, ModelParameters, Boolean> setVisibility) {
    this.identifier = identifier;
    this.modelPartData = modelPartData;
    this.setVisibility = setVisibility;
  }

  public Identifier identifier() {
    return this.identifier;
  }

  public BiConsumer<ModelPartData, ModelParameters> modelPartData() {
    return this.modelPartData;
  }
}

