package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import diacritics.owo.component.type.BannerTypeComponent;
import diacritics.owo.resource.EnsignResources;

public class Ensign implements ModInitializer {
	public static final String MOD_ID = "ensign";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ComponentType<BannerTypeComponent> BANNER_TYPE =
			Registry.register(Registries.DATA_COMPONENT_TYPE, identifier("banner_type"),
					ComponentType.<BannerTypeComponent>builder().codec(BannerTypeComponent.CODEC).build());

	@Override
	public void onInitialize() {
		LOGGER.info("hello from ensign!");

		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(new EnsignResources());
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
