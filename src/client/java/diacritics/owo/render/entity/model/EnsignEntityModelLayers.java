package diacritics.owo.render.entity.model;

import diacritics.owo.Ensign;
import diacritics.owo.render.block.entity.SwallowtailBannerBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class EnsignEntityModelLayers {
  public static void initialize() {}

  public static final EntityModelLayer SWALLOWTAIL_BANNER =
      register("swallowtail_banner", SwallowtailBannerBlockEntityRenderer::getTexturedModelData);

  public static EntityModelLayer register(String identifier, TexturedModelDataProvider provider) {
    EntityModelLayer entityModelLayer = new EntityModelLayer(Ensign.identifier(identifier), "main");
    EntityModelLayerRegistry.registerModelLayer(entityModelLayer, provider);

    return entityModelLayer;
  }
}
