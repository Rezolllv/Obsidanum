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
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "mutated_gart"), "main");
	private final ModelPart head;
	private final ModelPart beard;
	private final ModelPart bone;
	private final ModelPart mushroms_head;
	private final ModelPart body;
	private final ModelPart bone5;
	private final ModelPart book;
	private final ModelPart mush2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone2;
	private final ModelPart right_arm;
	private final ModelPart mush;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public MutatedGartModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.body = bone.getChild("body");
		this.bone5 = body.getChild("bone5");
		this.book = body.getChild("book");
		this.mush2 = body.getChild("mush2");
		this.bone3 = mush2.getChild("bone3");
		this.bone4 = bone3.getChild("bone4");
		this.bone2 = mush2.getChild("bone2");
		this.head = body.getChild("head");
		this.beard = head.getChild("beard");
		this.mushroms_head = head.getChild("mushroms_head");
		this.left_leg = body.getChild("left_leg");
		this.right_leg = body.getChild("right_leg");
		this.left_arm = body.getChild("left_arm");
		this.right_arm = body.getChild("right_arm");
		this.mush = right_arm.getChild("mush");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, -1.9302F, -4.6988F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.2667F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.1969F, 3.6988F));

		PartDefinition bone5 = body.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 40).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(20, 21).addBox(-2.0F, 4.0F, -1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.7333F, -4.0F));

		PartDefinition book = body.addOrReplaceChild("book", CubeListBuilder.create().texOffs(100, 112).addBox(-2.0F, -3.0F, 2.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(86, 119).addBox(-2.0F, -3.0F, -2.5F, 2.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(90, 114).addBox(-2.0F, -3.0F, -2.5F, 2.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F))
				.texOffs(104, 107).addBox(0.0F, -3.0F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(104, 114).addBox(-2.0F, -3.0F, -2.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 3.7333F, 5.05F, 0.0F, -1.5708F, 0.0F));

		PartDefinition mush2 = body.addOrReplaceChild("mush2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.7F, -5.2667F, 3.8F, 0.0F, 0.7854F, 0.0F));

		PartDefinition bone3 = mush2.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, -0.1542F, -0.0033F, 0.0312F));

		PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(78, 1).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, 0.0F, 0.0F, -1.4399F, 0.0F));

		PartDefinition cube_r2 = bone3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(78, 1).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(118, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, -1.4399F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.6032F, 1.5424F, 2.3744F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bone4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(96, 12).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, 0.0F, -3.1416F, 3.0107F, 3.1416F));

		PartDefinition cube_r6 = bone4.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(96, 12).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, 0.0F, 0.0F, -1.4399F, 0.0F));

		PartDefinition cube_r7 = bone4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(114, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -6.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r8 = bone4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r9 = bone4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(118, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, -1.4399F, 0.0F));

		PartDefinition bone2 = mush2.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(114, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8071F, -3.0F, -5.6713F, 1.6468F, -1.4831F, -1.5851F));

		PartDefinition cube_r10 = bone2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(99, 6).addBox(-3.0F, -4.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(99, 6).addBox(-3.0F, -4.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 21).addBox(-3.2333F, -4.9302F, -4.3321F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(32, 0).addBox(-3.2333F, -4.9302F, -4.3321F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.2333F, -7.2667F, -1.3667F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(96, 13).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4667F, 4.0698F, -3.3321F, -0.049F, 0.7143F, -0.0692F));

		PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(52, 40).addBox(-3.0F, 3.1751F, -2.6902F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.2333F, -1.1302F, -2.3321F));

		PartDefinition head_r2 = beard.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(3, 55).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition mushroms_head = head.addOrReplaceChild("mushroms_head", CubeListBuilder.create().texOffs(122, 8).addBox(-1.7003F, -1.0464F, -0.6463F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(122, 11).addBox(-1.7003F, -4.0464F, -1.6463F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(78, 8).addBox(-5.7003F, -10.0464F, -2.6463F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4667F, -0.9302F, -3.1821F, -3.0799F, -0.7844F, -3.1416F));

		PartDefinition cube_r12 = mushroms_head.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(78, 8).addBox(-5.0F, -3.0F, 0.0F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7003F, -7.0464F, -2.6463F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r13 = mushroms_head.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(122, 11).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7003F, -4.0464F, -1.6463F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r14 = mushroms_head.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(122, 8).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7003F, -1.0464F, -0.6463F, 0.0F, -1.5708F, 0.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 40).addBox(-1.9F, 0.0F, -2.25F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 7.7333F, 0.25F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(36, 40).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 7.7333F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 21).addBox(0.0F, -2.0F, -2.25F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.2667F, 0.25F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 21).addBox(-4.0F, -2.0F, -2.25F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -4.2667F, 0.25F));

		PartDefinition mush = right_arm.addOrReplaceChild("mush", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-2.0F, -6.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(98, 0).addBox(-5.0F, -11.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7F, 3.0F, 0.3F, -0.1825F, 0.7713F, -0.2595F));

		PartDefinition cube_r15 = mush.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(98, 0).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(114, 0).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r16 = mush.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(118, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 126, 126);
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return body;
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
		this.animate(((MutatedGart) entity).attackAnimationState, MutatedGartAnimation.punch, ageInTicks, 1f);

		this.animateWalk(MutatedGartAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((MutatedGart) entity).idleAnimationState, MutatedGartAnimation.idle, ageInTicks, 1f);
	}
}