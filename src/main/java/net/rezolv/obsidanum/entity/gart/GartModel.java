package net.rezolv.obsidanum.entity.gart;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetleAnimation;

public class GartModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("obsidanum", "gart"), "main");
	private final ModelPart bone;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public GartModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.body = this.bone.getChild("body");
		this.head = this.bone.getChild("head");
		this.right_arm = this.bone.getChild("right_arm");
		this.left_arm = this.bone.getChild("left_arm");
		this.right_leg = this.bone.getChild("right_leg");
		this.left_leg = this.bone.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 11.2F, -1.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 3.8F, 1.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -4.0F, -1.5F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.7F, 0.0436F, 0.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -6.0F, -3.5F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -0.2F, -0.2F));

		PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, -1.0F, -1.75F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.8F, 0.25F));

		PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(18, 12).addBox(0.0F, -1.0F, -1.75F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.8F, 0.25F));

		PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(24, 23).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 7.8F, 0.5F));

		PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 23).addBox(-2.0F, 0.0F, -1.75F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 7.8F, 0.75F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}



	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(GartAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((Gart) entity).idleAnimationState, GartAnimation.idle, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return bone;
	}
}