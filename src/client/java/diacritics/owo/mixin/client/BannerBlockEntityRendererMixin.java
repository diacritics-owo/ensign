package diacritics.owo.mixin.client;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import diacritics.owo.block.entity.BannerTypeProvider;
import diacritics.owo.resource.EnsignClientResources;

@Mixin(BannerBlockEntityRenderer.class)
public class BannerBlockEntityRendererMixin {
	private static final ModelPart EMPTY_MODEL =
			TexturedModelData.of(new ModelData(), 64, 64).createModel();

	private final ModelPart pillar;
	private final ModelPart crossbar;

	@Shadow
	public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
			int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner,
			DyeColor color, BannerPatternsComponent patterns) {}

	public BannerBlockEntityRendererMixin() {
		this.pillar = null;
		this.crossbar = null;
	}

	@Overwrite
	public void render(BannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		Identifier identifier = ((BannerTypeProvider) bannerBlockEntity).getBannerType().type();

		ModelPart banner = EnsignClientResources.BANNER_SHAPES.get(identifier) == null ? EMPTY_MODEL
				: EnsignClientResources.BANNER_SHAPES.get(identifier).createModel();

		// begin slightly-modified rendering code

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
			if (blockState.getBlock() instanceof BannerBlock) {
				matrixStack.translate(0.5F, 0.5F, 0.5F);
				h = -RotationPropertyHelper.toDegrees((Integer) blockState.get(BannerBlock.ROTATION));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
				this.pillar.visible = true;
			} else {
				matrixStack.translate(0.5F, -g / 4, 0.5F);
				h = -((Direction) blockState.get(WallBannerBlock.FACING)).asRotation();
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
				matrixStack.translate(0.0F, -0.3125F, -0.4375F);
				this.pillar.visible = false;
			}
		}

		matrixStack.push();
		matrixStack.scale(g, -g, -g);
		VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE
				.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
		this.pillar.render(matrixStack, vertexConsumer, i, j);
		this.crossbar.render(matrixStack, vertexConsumer, i, j);
		BlockPos blockPos = bannerBlockEntity.getPos();
		float k = ((float) Math.floorMod(
				(long) (blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + l, 100L) + f)
				/ 100.0F;
		banner.pitch = (-0.0125F + 0.01F * MathHelper.cos(6.2831855F * k)) * 3.1415927F;
		banner.pivotY = -32.0F;
		renderCanvas(matrixStack, vertexConsumerProvider, i, j, banner, ModelLoader.BANNER_BASE, true,
				bannerBlockEntity.getColorForState(), bannerBlockEntity.getPatterns());
		matrixStack.pop();
		matrixStack.pop();
	}
}
