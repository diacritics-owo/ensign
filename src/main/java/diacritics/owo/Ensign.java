package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import diacritics.owo.block.EnsignBlocks;
import diacritics.owo.block.entity.EnsignBlockEntities;
import diacritics.owo.item.EnsignItems;

public class Ensign implements ModInitializer {
	public static final String MOD_ID = "ensign";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("hello from ensign!");

		EnsignBlocks.initialize();
		EnsignBlockEntities.initialize();
		EnsignItems.initialize();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
