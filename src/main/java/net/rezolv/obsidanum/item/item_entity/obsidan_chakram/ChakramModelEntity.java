package net.rezolv.obsidanum.item.item_entity.obsidan_chakram;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ChakramModelEntity extends EntityModel<ObsidianChakramEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("obsidanum", "entity/obsidian_chakram"), "main");
	private final ModelPart group2;
	private final ModelPart group3;
	private final ModelPart group4;
	private final ModelPart group;

	public ChakramModelEntity(ModelPart root) {
		this.group2 = root.getChild("group2");
		this.group3 = this.group2.getChild("group3");
		this.group4 = this.group3.getChild("group4");
		this.group = this.group3.getChild("group");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition group2 = partdefinition.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, 7.0F, -7.5F, 15.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

		PartDefinition group3 = group2.addOrReplaceChild("group3", CubeListBuilder.create(), PartPose.offset(-7.5F, 8.0F, 7.5F));

		PartDefinition group4 = group3.addOrReplaceChild("group4", CubeListBuilder.create().texOffs(0, 92).addBox(0.0F, -1.0F, -15.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(150, 91).addBox(0.0F, -1.0F, -13.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(120, 91).addBox(0.0F, -1.0F, -14.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(90, 91).addBox(0.0F, -1.0F, -12.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(60, 91).addBox(0.0F, -1.0F, -11.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(30, 91).addBox(0.0F, -1.0F, -10.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 91).addBox(0.0F, -1.0F, -9.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(150, 90).addBox(0.0F, -1.0F, -8.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(120, 90).addBox(0.0F, -1.0F, -7.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(90, 90).addBox(0.0F, -1.0F, -6.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(60, 90).addBox(0.0F, -1.0F, -5.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(30, 90).addBox(0.0F, -1.0F, -4.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).addBox(0.0F, -1.0F, -3.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(68, 74).addBox(0.0F, -1.0F, -2.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(68, 73).addBox(0.0F, -1.0F, -1.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(68, 72).addBox(0.0F, -1.0F, 0.0F, 15.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition group = group3.addOrReplaceChild("group", CubeListBuilder.create().texOffs(34, 72).addBox(-15.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(0, 72).addBox(-14.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(102, 54).addBox(-13.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(68, 54).addBox(-12.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(34, 54).addBox(-11.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(0, 54).addBox(-10.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(102, 36).addBox(-9.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(68, 36).addBox(-8.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(34, 36).addBox(-7.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(0, 36).addBox(-6.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(102, 18).addBox(-5.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(68, 18).addBox(-4.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(34, 18).addBox(-3.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-2.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(94, 0).addBox(-1.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(60, 0).addBox(0.0F, -1.0F, -0.5F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 0.0F, -15.5F));

		return LayerDefinition.create(meshdefinition, 192, 192);
	}

	@Override
	public void setupAnim(ObsidianChakramEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isStopped()) {
			float rotation = ageInTicks * -32.0F; // Увеличиваем угол вращения в зависимости от времени анимации
			this.group2.yRot = (float) Math.toRadians(rotation);
		} else {
			this.group2.yRot = 0; // Останавливаем вращение
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		group2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

	}
}