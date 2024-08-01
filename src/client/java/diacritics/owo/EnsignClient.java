package diacritics.owo;

import diacritics.owo.block.entity.EnsignBlockEntities;
import diacritics.owo.render.block.entity.SwallowtailBannerBlockEntityRenderer;
import diacritics.owo.render.entity.model.EnsignEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class EnsignClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EnsignEntityModelLayers.initialize();

		BlockEntityRendererFactories.register(EnsignBlockEntities.SWALLOWTAIL_BANNER,
				SwallowtailBannerBlockEntityRenderer::new);
	}
}
