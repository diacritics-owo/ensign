package diacritics.owo.render.block.entity;

import diacritics.owo.block.SwallowtailBannerBlock;
import diacritics.owo.block.WallSwallowtailBannerBlock;
import diacritics.owo.block.entity.SwallowtailBannerBlockEntity;
import diacritics.owo.render.entity.model.EnsignEntityModelLayers;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;

public class SwallowtailBannerBlockEntityRenderer
    implements BlockEntityRenderer<SwallowtailBannerBlockEntity> {
  private static final int WIDTH = 20;
  private static final int HEIGHT = 40;
  private static final int ROTATIONS = 16;
  public static final String BANNER = "flag";
  private static final String PILLAR = "pole";
  private static final String CROSSBAR = "bar";
  private final ModelPart banner;
  private final ModelPart pillar;
  private final ModelPart crossbar;

  public static final int TAIL_HEIGHT = 10;
  public static final int TAIL_STEP = 2;

  public SwallowtailBannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    ModelPart modelPart = ctx.getLayerModelPart(EnsignEntityModelLayers.SWALLOWTAIL_BANNER);
    this.banner = modelPart.getChild("flag");
    this.pillar = modelPart.getChild("pole");
    this.crossbar = modelPart.getChild("bar");
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();

    modelPartData.addChild(PILLAR,
        ModelPartBuilder.create().uv(44, 0).cuboid(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F),
        ModelTransform.NONE);
    modelPartData.addChild(CROSSBAR,
        ModelPartBuilder.create().uv(0, 42).cuboid(-10.0F, -32.0F, -1.0F, WIDTH, 2.0F, 2.0F),
        ModelTransform.NONE);

    ModelPartData banner = modelPartData.addChild(BANNER, ModelPartBuilder.create().uv(0, 0)
        .cuboid(-10.0F, 0.0F, -2.0F, WIDTH, HEIGHT - TAIL_HEIGHT, 1.0F), ModelTransform.NONE);

    for (int i = 0; i < TAIL_HEIGHT; i += TAIL_STEP) {
      banner.addChild("ltail" + i,
          ModelPartBuilder.create().uv(0, HEIGHT - (TAIL_HEIGHT - i)).cuboid(-10.0F,
              HEIGHT - (TAIL_HEIGHT - i), -2.0F, (WIDTH / 2) - i, TAIL_STEP, 1.0F),
          ModelTransform.NONE);

      // TODO: the texture on the back of the right tail is messed up
      banner.addChild("rtail" + i,
          ModelPartBuilder.create().uv((WIDTH / 2) + i, HEIGHT - (TAIL_HEIGHT - i)).cuboid(
              -10.0F + ((WIDTH / 2) + i), HEIGHT - (TAIL_HEIGHT - i), -2.0F, (WIDTH / 2) - i,
              TAIL_STEP, 1.0F),
          ModelTransform.NONE);
    }

    return TexturedModelData.of(modelData, 64, 64);
  }

  public void render(SwallowtailBannerBlockEntity bannerBlockEntity, float f,
      MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
    float g = 0.6666667F;
    boolean bl = bannerBlockEntity.getWorld() == null;
    matrixStack.push();
    long l;
    if (bl) {
      l = 0L;
      matrixStack.translate(0.5F, 0.5F, 0.5F);
      this.pillar.visible = true;
    } else {
      l = bannerBlockEntity.getWorld().getTime();
      BlockState blockState = bannerBlockEntity.getCachedState();
      float h;
      if (blockState.getBlock() instanceof SwallowtailBannerBlock) {
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        h = -RotationPropertyHelper
            .toDegrees((Integer) blockState.get(SwallowtailBannerBlock.ROTATION));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
        this.pillar.visible = true;
      } else {
        matrixStack.translate(0.5F, -g / 4, 0.5F);
        h = -((Direction) blockState.get(WallSwallowtailBannerBlock.FACING)).asRotation();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
        matrixStack.translate(0.0F, -0.3125F, -0.4375F);
        this.pillar.visible = false;
      }
      // TODO: wallswallowtailbannerblock
    }

    matrixStack.push();
    matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
    VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE
        .getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
    this.pillar.render(matrixStack, vertexConsumer, i, j);
    this.crossbar.render(matrixStack, vertexConsumer, i, j);
    BlockPos blockPos = bannerBlockEntity.getPos();
    float k = ((float) Math.floorMod(
        (long) (blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + l, 100L) + f)
        / 100.0F;
    this.banner.pitch = (-0.0125F + 0.01F * MathHelper.cos(6.2831855F * k)) * 3.1415927F;
    this.banner.pivotY = -32.0F;
    renderCanvas(matrixStack, vertexConsumerProvider, i, j, this.banner, ModelLoader.BANNER_BASE,
        true, bannerBlockEntity.getColorForState(), bannerBlockEntity.getPatterns());
    matrixStack.pop();
    matrixStack.pop();
  }

  public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
      int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner,
      DyeColor color, BannerPatternsComponent patterns) {
    renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, color,
        patterns, false);
  }

  public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
      int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner,
      DyeColor color, BannerPatternsComponent patterns, boolean glint) {
    canvas.render(matrices,
        baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light,
        overlay);
    renderLayer(matrices, vertexConsumers, light, overlay, canvas,
        isBanner ? TexturedRenderLayers.BANNER_BASE : TexturedRenderLayers.SHIELD_BASE, color);

    for (int i = 0; i < ROTATIONS && i < patterns.layers().size(); ++i) {
      BannerPatternsComponent.Layer layer =
          (BannerPatternsComponent.Layer) patterns.layers().get(i);
      SpriteIdentifier spriteIdentifier =
          isBanner ? TexturedRenderLayers.getBannerPatternTextureId(layer.pattern())
              : TexturedRenderLayers.getShieldPatternTextureId(layer.pattern());
      renderLayer(matrices, vertexConsumers, light, overlay, canvas, spriteIdentifier,
          layer.color());
    }

  }

  private static void renderLayer(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
      int light, int overlay, ModelPart canvas, SpriteIdentifier textureId, DyeColor color) {
    int i = color.getEntityColor();
    canvas.render(matrices,
        textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light,
        overlay, i);
  }
}

