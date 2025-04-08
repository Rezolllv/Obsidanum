package net.rezolv.obsidanum.entity.mutated_gart;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElementalAnimation;

public class MutatedGartModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("obsidanum", "mutated_gart"), "main");
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart beard;
	private final ModelPart body;
	private final ModelPart bone5;
	private final ModelPart book;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public MutatedGartModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = this.bone.getChild("head");
		this.beard = this.head.getChild("beard");
		this.body = this.bone.getChild("body");
		this.bone5 = this.body.getChild("bone5");
		this.book = this.body.getChild("book");
		this.right_arm = this.bone.getChild("right_arm");
		this.left_arm = this.bone.getChild("left_arm");
		this.right_leg = this.bone.getChild("right_leg");
		this.left_leg = this.bone.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-3.2333F, -4.9302F, -4.3321F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(32, 0).addBox(-3.2333F, -4.9302F, -4.3321F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.2333F, -25.0F, -2.3667F));

		PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(52, 40).addBox(-3.0F, 3.1751F, -2.6902F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.2333F, -1.1302F, -2.3321F));

		PartDefinition head_r1 = beard.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(4, 55).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.2667F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.7333F, -1.0F));

		PartDefinition bone5 = body.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 40).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(20, 21).addBox(-2.0F, 4.0F, -1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.7333F, -4.0F));

		PartDefinition book = body.addOrReplaceChild("book", CubeListBuilder.create().texOffs(100, 112).addBox(-2.0F, -3.0F, 2.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(86, 119).addBox(-2.0F, -3.0F, -2.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(90, 114).addBox(-2.0F, -3.0F, -2.5F, 2.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F))
				.texOffs(104, 107).addBox(0.0F, -3.0F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(104, 114).addBox(-2.0F, -3.0F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 3.7333F, 5.05F, 0.0F, -1.5708F, 0.0F));

		PartDefinition right_arm = bone.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 21).addBox(-4.0F, -2.0F, -2.25F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -22.0F, -0.75F));

		PartDefinition left_arm = bone.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 21).addBox(0.0F, -2.0F, -2.25F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -22.0F, -0.75F));

		PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 40).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -10.0F, -1.0F));

		PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 40).addBox(-1.9F, 0.0F, -2.25F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, -10.0F, -0.75F));

		return LayerDefinition.create(meshdefinition, 126, 126);
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return bone;
	}
	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		MutatedGart gart = (MutatedGart) entity;
		if (gart.appearanceAnimationState.isStarted()) {
			this.animate(gart.appearanceAnimationState, MutatedGartAnimation.appearance, ageInTicks, 1f);
		}
		if (gart.isMagicAttacking()) {
			this.animate(gart.magicAttackAnimationState, MutatedGartAnimation.magic_punch, ageInTicks, 1f);
		}
		if (gart.attackAnimationState.isStarted()) {
			this.animate(gart.attackAnimationState, MutatedGartAnimation.punch, ageInTicks, 1f);
		} else if (gart.isMoving()) {
			this.animateWalk(MutatedGartAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		} else {
			this.animate(gart.idleAnimationState, MutatedGartAnimation.idle, ageInTicks, 1f);
		}
	}
}