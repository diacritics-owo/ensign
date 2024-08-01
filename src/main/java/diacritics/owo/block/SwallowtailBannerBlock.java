package diacritics.owo.block;

import java.util.Map;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import diacritics.owo.block.entity.SwallowtailBannerBlockEntity;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class SwallowtailBannerBlock extends AbstractBannerBlock {
  public static final MapCodec<SwallowtailBannerBlock> CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance
          .group(DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor),
              createSettingsCodec())
          .apply(instance, SwallowtailBannerBlock::new));
  public static final IntProperty ROTATION = Properties.ROTATION;
  private static final Map<DyeColor, Block> COLORED_SWALLOWTAIL_BANNERS =
      Maps.<DyeColor, Block>newHashMap();
  private static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

  private final DyeColor color;

  @Override
  public MapCodec<SwallowtailBannerBlock> getCodec() {
    return CODEC;
  }

  public SwallowtailBannerBlock(DyeColor dyeColor, Settings settings) {
    super(dyeColor, settings);
    this.color = dyeColor;
    COLORED_SWALLOWTAIL_BANNERS.put(dyeColor, this);
  }

  @Override
  protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return world.getBlockState(pos.down()).isSolid();
  }

  @Override
  protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos,
      ShapeContext context) {
    return SHAPE;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(ROTATION,
        Integer.valueOf(RotationPropertyHelper.fromYaw(ctx.getPlayerYaw() + 180.0F)));
  }

  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction,
      BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return direction == Direction.DOWN && !state.canPlaceAt(world, pos)
        ? Blocks.AIR.getDefaultState()
        : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @Override
  protected BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(ROTATION,
        Integer.valueOf(rotation.rotate((Integer) state.get(ROTATION), 16)));
  }

  @Override
  protected BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(ROTATION, Integer.valueOf(mirror.mirror((Integer) state.get(ROTATION), 16)));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(ROTATION);
  }

  public static Block getForColor(DyeColor color) {
    return (Block) COLORED_SWALLOWTAIL_BANNERS.getOrDefault(color,
        EnsignBlocks.WHITE_SWALLOWTAIL_BANNER);
  }

  // other methods from abstractbannerblock not overriden by bannerblock that we need to override

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    // TODO: can we just use bannerblockentity?
    return new SwallowtailBannerBlockEntity(pos, state, this.color);
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return world.getBlockEntity(pos) instanceof SwallowtailBannerBlockEntity bannerBlockEntity
        ? bannerBlockEntity.getPickStack()
        : super.getPickStack(world, pos, state);
  }
}
