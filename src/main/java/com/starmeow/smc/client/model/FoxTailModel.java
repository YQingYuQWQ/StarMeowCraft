package com.starmeow.smc.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class FoxTailModel extends HumanoidModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("smc", "fox_tail"), "main");
	private final ModelPart tail_root;
	private final ModelPart tail_start;
	private final ModelPart tail_middle;
	private final ModelPart tail_end;
	private final ModelPart body;

	public FoxTailModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.tail_root = this.body.getChild("tail_root");
		this.tail_start = this.tail_root.getChild("tail_start");
		this.tail_middle = this.tail_start.getChild("tail_middle");
		this.tail_end = this.tail_middle.getChild("tail_end");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition tail_root = body.addOrReplaceChild("tail_root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tail_start = tail_root.addOrReplaceChild("tail_start", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -1.5176F, -0.0305F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.1F, 1.5F));

		PartDefinition tail_middle = tail_start.addOrReplaceChild("tail_middle", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0446F, -0.1611F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 17).addBox(-2.0F, -2.0446F, 5.8389F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 3.25F));

		PartDefinition cube_r1 = tail_middle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 17).mirror().addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.9554F, 0.8389F, -0.3819F, -0.4084F, 0.1582F));

		PartDefinition cube_r2 = tail_middle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 17).mirror().addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.9554F, 3.8389F, -0.3604F, -0.2457F, 0.0914F));

		PartDefinition cube_r3 = tail_middle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 17).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.9554F, 0.8389F, -0.3819F, 0.4084F, -0.1582F));

		PartDefinition cube_r4 = tail_middle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 17).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.9554F, 3.8389F, -0.3604F, 0.2457F, -0.0914F));

		PartDefinition tail_end = tail_middle.addOrReplaceChild("tail_end", CubeListBuilder.create().texOffs(14, 10).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.0446F, 5.8389F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float time = ageInTicks * 0.12F;
		float flap = Mth.sin(time);
		float flap1 = Mth.sin(time - 1);
		float flap2 = Mth.sin(time - 2);
		this.tail_start.xRot = -0.45F;
		this.tail_start.yRot = flap * 0.25F;
		this.tail_middle.yRot = flap1 * 0.25F;
		this.tail_end.yRot = flap2 * 0.25F;
		this.tail_start.zRot = - flap * 0.25F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}