package net.rezolv.obsidanum.entity.meat_beetle;


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

public class MeetBeetleModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("obsidanum", "meat_beetle"), "main");
	private final ModelPart bone;
	private final ModelPart body;
	private final ModelPart leg_three_right;
	private final ModelPart head;
	private final ModelPart ant_right;
	private final ModelPart ant_left;
	private final ModelPart leg_two_right;
	private final ModelPart leg_one_right;
	private final ModelPart leg_three_left;
	private final ModelPart leg_two_left;
	private final ModelPart leg_one_left;

	public MeetBeetleModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.body = this.bone.getChild("body");
		this.leg_three_right = this.body.getChild("leg_three_right");
		this.head = this.body.getChild("head");
		this.ant_right = this.head.getChild("ant_right");
		this.ant_left = this.head.getChild("ant_left");
		this.leg_two_right = this.body.getChild("leg_two_right");
		this.leg_one_right = this.body.getChild("leg_one_right");
		this.leg_three_left = this.body.getChild("leg_three_left");
		this.leg_two_left = this.body.getChild("leg_two_left");
		this.leg_one_left = this.body.getChild("leg_one_left");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-3.0F, 22.0F, -4.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.5F, -7.0F, -6.0F, 9.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(13, 40).addBox(-3.5F, -6.0F, 9.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 2.0F, 3.0F));

		PartDefinition leg_three_right = body.addOrReplaceChild("leg_three_right", CubeListBuilder.create(), PartPose.offset(-3.0F, -2.0F, 5.0F));

		PartDefinition cube_r1 = leg_three_right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(31, 5).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(33, 0).addBox(-2.5F, -1.0F, -3.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -5.0F));

		PartDefinition ant_right = head.addOrReplaceChild("ant_right", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5813F, -0.23F, -4.9733F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, -0.0436F, 0.1309F, 0.0F));

		PartDefinition cube_r2 = ant_right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 5).addBox(-1.2389F, -1.55F, -4.0904F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3423F, 1.5217F, -5.3744F, 0.3054F, 0.0F, 0.0F));

		PartDefinition ant_left = head.addOrReplaceChild("ant_left", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.9246F, -1.2797F, -6.4289F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 0.0F, -0.5F, -0.0436F, -0.1309F, 0.0F));

		PartDefinition cube_r3 = ant_left.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1.7611F, -1.55F, -4.0904F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.8365F, 0.4721F, -6.83F, 0.3054F, 0.0F, 0.0F));

		PartDefinition leg_two_right = body.addOrReplaceChild("leg_two_right", CubeListBuilder.create(), PartPose.offset(-3.0F, -2.0F, 1.0F));

		PartDefinition cube_r4 = leg_two_right.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(31, 5).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition leg_one_right = body.addOrReplaceChild("leg_one_right", CubeListBuilder.create(), PartPose.offset(-3.0F, -2.0F, -3.0F));

		PartDefinition cube_r5 = leg_one_right.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(31, 5).addBox(-5.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition leg_three_left = body.addOrReplaceChild("leg_three_left", CubeListBuilder.create(), PartPose.offset(3.0F, -2.0F, 5.0F));

		PartDefinition cube_r6 = leg_three_left.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(31, 5).mirror().addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition leg_two_left = body.addOrReplaceChild("leg_two_left", CubeListBuilder.create(), PartPose.offset(3.0F, -2.0F, 1.0F));

		PartDefinition cube_r7 = leg_two_left.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(31, 5).mirror().addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition leg_one_left = body.addOrReplaceChild("leg_one_left", CubeListBuilder.create(), PartPose.offset(3.0F, -2.0F, -3.0F));

		PartDefinition cube_r8 = leg_one_left.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(31, 5).mirror().addBox(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(MeetBeetleAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((MeetBeetle) entity).idleAnimationState, MeetBeetleAnimation.idle, ageInTicks, 1f);
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
		return body;
	}

}