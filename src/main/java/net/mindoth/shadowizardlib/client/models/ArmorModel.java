package net.mindoth.shadowizardlib.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ArmorModel extends BipedModel<LivingEntity> {

    public EquipmentSlotType slot;

    public ModelRenderer Head;
    public ModelRenderer Body;
    public ModelRenderer RightArm;
    public ModelRenderer LeftArm;
    public ModelRenderer RightLeg;
    public ModelRenderer LeftLeg;
    public ModelRenderer RightFoot;
    public ModelRenderer LeftFoot;

    public ArmorModel(EquipmentSlotType slot) {
        super(0.0F, 1.0f, 64, 64);
        this.texHeight = 64;
        this.texWidth = 64;
        this.slot = slot;
        this.young = false;
        Head = new ModelRenderer(this);
        Body = new ModelRenderer(this);
        RightArm = new ModelRenderer(this);
        LeftArm = new ModelRenderer(this);
        RightLeg = new ModelRenderer(this);
        LeftLeg = new ModelRenderer(this);
        RightFoot = new ModelRenderer(this);
        LeftFoot = new ModelRenderer(this);
    }

    @Override
    public void setupAnim(LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof ArmorStandEntity) {
            ArmorStandEntity armorStand = (ArmorStandEntity) livingEntity;
            this.Head.xRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getX();
            this.Head.yRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getY();
            this.Head.zRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getZ();
            this.Head.setPos(0.0F, 1.0F, 0.0F);
            this.Body.xRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getX();
            this.Body.yRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getY();
            this.Body.zRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getZ();
            this.LeftArm.xRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getX();
            this.LeftArm.yRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getY();
            this.LeftArm.zRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getZ();
            this.RightArm.xRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getX();
            this.RightArm.yRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getY();
            this.RightArm.zRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getZ();
            this.LeftLeg.xRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getX();
            this.LeftLeg.yRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getY();
            this.LeftLeg.zRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getZ();
            this.LeftLeg.setPos(1.9F, 11.0F, 0.0F);
            this.RightLeg.xRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getX();
            this.RightLeg.yRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getY();
            this.RightLeg.zRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getZ();
            this.RightLeg.setPos(-1.9F, 11.0F, 0.0F);
            this.hat.copyFrom(this.Head);
        }
        else {
            super.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        matrixStack.pushPose();

        this.setHeadRotation();
        this.setChestRotation();
        this.setLegsRotation();
        this.setBootRotation();

        Head.visible = slot == EquipmentSlotType.HEAD;
        Body.visible = slot == EquipmentSlotType.CHEST;
        RightArm.visible = slot == EquipmentSlotType.CHEST;
        LeftArm.visible = slot == EquipmentSlotType.CHEST;
        RightLeg.visible = slot == EquipmentSlotType.LEGS;
        LeftLeg.visible = slot == EquipmentSlotType.LEGS;
        RightFoot.visible = slot == EquipmentSlotType.FEET;
        LeftFoot.visible = slot == EquipmentSlotType.FEET;
        if (this.young) {
            float f = 2.0F;
            matrixStack.scale(1.5F / f, 1.5F / f, 1.5F / f);
            matrixStack.translate(0.0F, 16.0F * 1, 0.0F);
            Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.scale(1.0F / f, 1.0F / f, 1.0F / f);
            matrixStack.translate(0.0F, 24.0F * 1, 0.0F);
            Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        else {
            Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        RightFoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftFoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
    }

    public void setHeadRotation() {
        Head.x = head.x;
        Head.y = head.y;
        Head.z = head.z;
        setRotation(Head, head.xRot, head.yRot, head.zRot);
    }

    public void setChestRotation() {
        /* if (e instanceof EntityPlayer){ ((EntityPlayer)e).get } */
        Body.x = body.x;
        Body.y = body.y;
        Body.z = body.z;
        RightArm.x = rightArm.x;
        RightArm.y = rightArm.y;
        RightArm.z = rightArm.z;
        LeftArm.x = leftArm.x;
        LeftArm.y = leftArm.y;
        LeftArm.z = leftArm.z;
        setRotation(Body, body.xRot, body.yRot, body.zRot);
        setRotation(RightArm, rightArm.xRot, rightArm.yRot, rightArm.zRot);
        setRotation(LeftArm, leftArm.xRot, leftArm.yRot, leftArm.zRot);
    }

    public void setLegsRotation() {
        RightLeg.x = rightLeg.x;
        RightLeg.y = rightLeg.y;
        RightLeg.z = rightLeg.z;
        LeftLeg.x = leftLeg.x;
        LeftLeg.y = leftLeg.y;
        LeftLeg.z = leftLeg.z;
        setRotation(RightLeg, rightLeg.xRot, rightLeg.yRot, rightLeg.zRot);
        setRotation(LeftLeg, leftLeg.xRot, leftLeg.yRot, leftLeg.zRot);
    }

    public void setBootRotation() {
        RightFoot.x = rightLeg.x;
        RightFoot.y = rightLeg.y;
        RightFoot.z = rightLeg.z;
        LeftFoot.x = leftLeg.x;
        LeftFoot.y = leftLeg.y;
        LeftFoot.z = leftLeg.z;
        setRotation(RightFoot, rightLeg.xRot, rightLeg.yRot, rightLeg.zRot);
        setRotation(LeftFoot, leftLeg.xRot, leftLeg.yRot, leftLeg.zRot);
    }

    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}