package diacritics.owo.mixin.client;

import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import diacritics.owo.util.BannerType;
import diacritics.owo.util.BannerTypeProvider;

@Mixin(BannerBlockEntityRenderer.class)
public class BannerBlockEntityRendererMixin {
	private static final int WIDTH = 20;
	private static final int HEIGHT = 40;

	private static final int SWALLOWTAIL_HEIGHT = 20;
	private static final int SWALLOWTAIL_STEP_X = 2;
	private static final int SWALLOWTAIL_STEP_Y = 4;

	private final ModelPart banner;
	private final ModelPart pillar;
	private final ModelPart crossbar;

	@Shadow
	public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
			int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner,
			DyeColor color, BannerPatternsComponent patterns) {}

	public BannerBlockEntityRendererMixin() {
		this.banner = null;
		this.pillar = null;
		this.crossbar = null;
	}

	@Overwrite
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("pole",
				ModelPartBuilder.create().uv(44, 0).cuboid(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F),
				ModelTransform.NONE);
		modelPartData.addChild("bar",
				ModelPartBuilder.create().uv(0, 42).cuboid(-10.0F, -32.0F, -1.0F, WIDTH, 2.0F, 2.0F),
				ModelTransform.NONE);

		ModelPartData flag =
				modelPartData.addChild("flag", ModelPartBuilder.create(), ModelTransform.NONE);

		flag.addChild("regular",
				ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F, WIDTH, HEIGHT, 1.0F),
				ModelTransform.NONE);

		ModelPartData swallowtail =
				flag.addChild("swallowtail", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 0.0F, -2.0F,
						WIDTH, HEIGHT - SWALLOWTAIL_HEIGHT, 1.0F), ModelTransform.NONE);

		for (int i = 0; i < SWALLOWTAIL_HEIGHT; i += SWALLOWTAIL_STEP_Y) {
			final int Y = SWALLOWTAIL_HEIGHT - i;
			final int MIDDLE = WIDTH / 2;
			final int I_X = SWALLOWTAIL_STEP_X * (i / SWALLOWTAIL_STEP_Y);
			final int TAIL_WIDTH = MIDDLE - I_X;

			swallowtail.addChild("ltail" + i, ModelPartBuilder.create().uv(0, HEIGHT - Y).cuboid(-10.0F,
					HEIGHT - Y, -2.0F, TAIL_WIDTH, SWALLOWTAIL_STEP_Y, 1.0F), ModelTransform.NONE);

			// TODO: the textures on the back of the right tail and tongue are messed up

			swallowtail.addChild("rtail" + i, ModelPartBuilder.create().uv(MIDDLE + I_X, HEIGHT - Y)
					.cuboid(-10.0F + (MIDDLE + I_X), HEIGHT - Y, -2.0F, TAIL_WIDTH, SWALLOWTAIL_STEP_Y, 1.0F),
					ModelTransform.NONE);

			final int TONGUE_WIDTH =
					TAIL_WIDTH > (WIDTH / 3.0F) ? (WIDTH - (2 * TAIL_WIDTH)) : TAIL_WIDTH;
			final int TONGUE_X = MIDDLE - (TONGUE_WIDTH / 2);

			swallowtail.addChild("tongue" + i, ModelPartBuilder.create().uv(TONGUE_X, HEIGHT - Y)
					.cuboid(-10.0F + TONGUE_X, HEIGHT - Y, -2.0F, TONGUE_WIDTH, SWALLOWTAIL_STEP_Y, 1.0F),
					ModelTransform.NONE);
		}

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Overwrite
	public void render(BannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		this.banner.getChild("regular").visible = false;

		this.banner.getChild("swallowtail").visible = false;
		for (int k = 0; k < SWALLOWTAIL_HEIGHT; k += SWALLOWTAIL_STEP_Y) {
			this.banner.getChild("swallowtail").getChild("ltail" + k).visible = false;
			this.banner.getChild("swallowtail").getChild("rtail" + k).visible = false;
			this.banner.getChild("swallowtail").getChild("tongue" + k).visible = false;
		}

		BannerType bannerType = ((BannerTypeProvider) bannerBlockEntity).getBannerType();
		ModelPart banner = switch (bannerType) {
			case BannerType.REGULAR -> this.banner.getChild("regular");
			case BannerType.SWALLOWTAIL -> this.banner.getChild("swallowtail");
			case BannerType.TONGUED_SWALLOWTAIL -> this.banner.getChild("swallowtail");
		};

		banner.visible = true;

		if (bannerType == BannerType.SWALLOWTAIL || bannerType == BannerType.TONGUED_SWALLOWTAIL) {
			for (int k = 0; k < SWALLOWTAIL_HEIGHT; k += SWALLOWTAIL_STEP_Y) {
				banner.getChild("ltail" + k).visible = true;
				banner.getChild("rtail" + k).visible = true;
				if (bannerType == BannerType.TONGUED_SWALLOWTAIL) {
					banner.getChild("tongue" + k).visible = true;
				}
			}
		}

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
		matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
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
