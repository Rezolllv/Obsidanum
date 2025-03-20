package net.rezolv.obsidanum.chests.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rezolv.obsidanum.chests.block.AbstractObsidanumChestBlock;
import net.rezolv.obsidanum.chests.block.ObsidanumChestsTypes;
import net.rezolv.obsidanum.chests.block.entity.AbstractObsidanumChestBlockEntity;
import net.rezolv.obsidanum.chests.client.model.ObsidanumChestsModels;
import net.rezolv.obsidanum.chests.client.model.inventory.ModelItem;
import net.rezolv.obsidanum.chests.events.ObsidanumChestsClientEvents;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ObsidanumChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

  private final ModelPart lid;
  private final ModelPart bottom;
  private final ModelPart lock;

  private final BlockEntityRenderDispatcher renderer;

  private static final List<ModelItem> MODEL_ITEMS = Arrays.asList(
    new ModelItem(new Vector3f(0.3F, 0.45F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.45F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.45F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.45F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.1F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.1F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.1F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.1F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.5F, 0.32F, 0.5F), 3.0F)
  );

  public ObsidanumChestRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart modelPart = context.bakeLayer(ObsidanumChestsClientEvents.IRON_CHEST);

    this.renderer = context.getBlockEntityRenderDispatcher();
    this.bottom = modelPart.getChild("iron_bottom");
    this.lid = modelPart.getChild("iron_lid");
    this.lock = modelPart.getChild("iron_lock");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshDefinition = new MeshDefinition();
    PartDefinition partDefinition = meshDefinition.getRoot();

    partDefinition.addOrReplaceChild("iron_bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
    partDefinition.addOrReplaceChild("iron_lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
    partDefinition.addOrReplaceChild("iron_lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

    return LayerDefinition.create(meshDefinition, 64, 64);
  }

  @Override
  public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
    AbstractObsidanumChestBlockEntity tileEntity = (AbstractObsidanumChestBlockEntity) tileEntityIn;

    Level level = tileEntity.getLevel();
    boolean useTileEntityBlockState = level != null;

    BlockState blockState = useTileEntityBlockState ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(AbstractObsidanumChestBlock.FACING, Direction.SOUTH);
    Block block = blockState.getBlock();
    ObsidanumChestsTypes chestType = ObsidanumChestsTypes.OBSIDIAN;
    ObsidanumChestsTypes actualType = AbstractObsidanumChestBlock.getTypeFromBlock(block);

    if (actualType != null) {
      chestType = actualType;
    }

    if (block instanceof AbstractObsidanumChestBlock abstractChestBlock) {
      poseStack.pushPose();

      float f = blockState.getValue(AbstractObsidanumChestBlock.FACING).toYRot();

      poseStack.translate(0.5D, 0.5D, 0.5D);
      poseStack.mulPose(Axis.YP.rotationDegrees(-f));
      poseStack.translate(-0.5D, -0.5D, -0.5D);

      DoubleBlockCombiner.NeighborCombineResult<? extends AbstractObsidanumChestBlockEntity> neighborCombineResult;

      if (useTileEntityBlockState) {
        neighborCombineResult = abstractChestBlock.combine(blockState, level, tileEntityIn.getBlockPos(), true);
      } else {
        neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
      }

      float openness = neighborCombineResult.<Float2FloatFunction>apply(AbstractObsidanumChestBlock.opennessCombiner(tileEntity)).get(partialTicks);
      openness = 1.0F - openness;
      openness = 1.0F - openness * openness * openness;

      int brightness = neighborCombineResult.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);


      Material material = new Material(Sheets.CHEST_SHEET, ObsidanumChestsModels.chooseChestTexture(chestType));

      VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutout);

      this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, openness, brightness, combinedOverlayIn);

      poseStack.popPose();
    }
  }

  private void render(PoseStack poseStack, VertexConsumer vertexConsumer, ModelPart lid, ModelPart lock, ModelPart bottom, float openness, int brightness, int combinedOverlayIn) {
    lid.xRot = -(openness * ((float) Math.PI / 2F));
    lock.xRot = lid.xRot;

    lid.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    lock.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    bottom.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
  }

  /**
   * Renders a single item in a TESR
   *
   * @param matrices  Matrix stack instance
   * @param buffer    Buffer instance
   * @param item      Item to render
   * @param modelItem Model items for render information
   * @param light     Model light
   */
  public static void renderItem(PoseStack matrices, MultiBufferSource buffer, ItemStack item, ModelItem modelItem, float rotation, int light) {
    // if no stack, skip
    if (item.isEmpty()) return;

    // start rendering
    matrices.pushPose();
    Vector3f center = modelItem.getCenter();
    matrices.translate(center.x(), center.y(), center.z());

    matrices.mulPose(Axis.YP.rotationDegrees(rotation));

    // scale
    float scale = modelItem.getSizeScaled();
    matrices.scale(scale, scale, scale);

    // render the actual item
    Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, matrices, buffer, null, 0);

    matrices.popPose();
  }
}
