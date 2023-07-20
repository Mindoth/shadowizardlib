package net.mindoth.shadowizardlib.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ArmorModel <T extends LivingEntity> extends HumanoidModel<T> implements IClientItemExtensions {
    public ArmorItem.Type slot;
    ModelPart root;
    ModelPart modelHead;
    ModelPart modelBody;
    ModelPart modelLeft_arm;
    ModelPart modelRight_arm;
    ModelPart modelBelt;
    ModelPart modelLeft_leg;
    ModelPart modelRight_leg;
    ModelPart modelLeft_foot;
    ModelPart modelRight_foot;

    public ArmorModel(ModelPart root, ArmorItem.Type slot) {
        super(root);
        this.slot = slot;
        this.root = root;
        this.modelBelt = root.getChild("Belt");
        this.modelBody = root.getChild("Body");
        this.modelRight_foot = root.getChild("RightBoot");
        this.modelLeft_foot = root.getChild("LeftBoot");
        this.modelLeft_arm = root.getChild("LeftArm");
        this.modelRight_arm = root.getChild("RightArm");
        this.modelRight_leg = root.getChild("LeftLeg");
        this.modelLeft_leg = root.getChild("RightLeg");
        this.modelHead = root.getChild("Head");

    }

    public static PartDefinition createHumanoidAlias(MeshDefinition mesh) {
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("Belt", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("Head", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("LeftLeg", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("LeftBoot", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("RightLeg", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("RightBoot", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("LeftArm", new CubeListBuilder(), PartPose.ZERO);
        root.addOrReplaceChild("RightArm", new CubeListBuilder(), PartPose.ZERO);

        return root;
    }

    protected Iterable<ModelPart> headParts() {
        return this.slot == ArmorItem.Type.HELMET ? ImmutableList.of(this.modelHead) : ImmutableList.of();
    }

    protected Iterable<ModelPart> bodyParts() {
        if (this.slot == ArmorItem.Type.HELMET) {
            return ImmutableList.of(this.modelBody, this.modelLeft_arm, this.modelRight_arm);
        } else if (this.slot == ArmorItem.Type.LEGGINGS) {
            return ImmutableList.of(this.modelLeft_leg, this.modelRight_leg, this.modelBelt);
        } else {
            return this.slot == ArmorItem.Type.BOOTS ? ImmutableList.of(this.modelLeft_foot, this.modelRight_foot) : ImmutableList.of();
        }
    }

    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        if (this.slot == ArmorItem.Type.HELMET) {
            this.modelHead.copyFrom(this.head);
            this.modelHead.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slot == ArmorItem.Type.CHESTPLATE) {
            this.modelBody.copyFrom(this.body);
            this.modelBody.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.modelRight_arm.copyFrom(this.rightArm);
            this.modelRight_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.modelLeft_arm.copyFrom(this.leftArm);
            this.modelLeft_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slot == ArmorItem.Type.LEGGINGS) {
            this.modelBelt.copyFrom(this.body);
            this.modelBelt.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.modelRight_leg.copyFrom(this.rightLeg);
            this.modelRight_leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.modelLeft_leg.copyFrom(this.leftLeg);
            this.modelLeft_leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else if (this.slot == ArmorItem.Type.BOOTS) {
            this.modelRight_foot.copyFrom(this.rightLeg);
            this.modelRight_foot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.modelLeft_foot.copyFrom(this.leftLeg);
            this.modelLeft_foot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        matrixStack.popPose();
    }

    public void copyFromDefault(HumanoidModel model) {
        modelBody.copyFrom(model.body);
        modelBelt.copyFrom(model.body);
        modelHead.copyFrom(model.head);
        modelLeft_arm.copyFrom(model.leftArm);
        modelRight_arm.copyFrom(model.rightArm);
        modelLeft_leg.copyFrom(leftLeg);
        modelRight_leg.copyFrom(rightLeg);
        modelLeft_foot.copyFrom(leftLeg);
        modelRight_foot.copyFrom(rightLeg);
    }
}