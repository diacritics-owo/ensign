package diacritics.owo.block.entity;

import diacritics.owo.Ensign;
import diacritics.owo.block.EnsignBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

// TODO: uhhh the auto-formatter did weird things
public class EnsignBlockEntities {
        public static void initialize() {}

        public static final BlockEntityType<SwallowtailBannerBlockEntity> SWALLOWTAIL_BANNER =
                        Registry.register(Registries.BLOCK_ENTITY_TYPE,
                                        Ensign.identifier("swallowtail_banner_block_entity"),
                                        BlockEntityType.Builder.create(
                                                        SwallowtailBannerBlockEntity::new,
                                                        EnsignBlocks.WHITE_SWALLOWTAIL_BANNER,
                                                        EnsignBlocks.WHITE_WALL_SWALLOWTAIL_BANNER)
                                                        .build());
}
