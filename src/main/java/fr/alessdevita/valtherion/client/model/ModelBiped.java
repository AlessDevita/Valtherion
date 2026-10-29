package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

public class ModelBiped extends ModelBase
{
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedBodyWear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedLeftLegwear;

    public ModelBiped.RightArmPose heldItemRight;
    public ModelBiped.LeftArmPose heldItemLeft;
    public boolean isSneak;

    public ModelBiped(float scale)
    {
        this(scale, 0.0F, 64, 32);
    }

    public ModelBiped(float scale, float offsetY, int texW, int texH)
    {
        this.textureWidth = texW;
        this.textureHeight = texH;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + offsetY, 0.0F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + offsetY, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + offsetY, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + offsetY, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.setMirror(true);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + offsetY, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + offsetY, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.setMirror(true);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + offsetY, 0.0F);
    }

    public ModelBiped()
    {
        this(0.0F);
        this.heldItemRight = ModelBiped.RightArmPose.EMPTY;
        this.heldItemLeft = ModelBiped.LeftArmPose.EMPTY;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);
        this.bipedHead.render(scale);
        this.bipedBody.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
        this.bipedHeadwear.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        this.bipedHead.rotateAngleY = headYaw / (180.0F / (float) Math.PI);
        this.bipedHead.rotateAngleX = headPitch / (180.0F / (float) Math.PI);
        this.bipedHeadwear.copyModelAngles(this.bipedHead);

        // Bras balances en opposition des jambes
        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F
                * limbSwingAmount * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F
                * limbSwingAmount * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;

        // Jambes
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
                * limbSwingAmount;

        // Pose d'item dans la main droite
        if (this.heldItemRight == ModelBiped.RightArmPose.BLOCK)
        {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
                    - ((float) Math.PI / 10F);
            this.bipedRightArm.rotateAngleY = -0.5F;
        }
        else if (this.heldItemRight == ModelBiped.RightArmPose.ITEM)
        {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
                    - ((float) Math.PI / 10F);
        }

        // Sneak : le corps est penche et descend
        if (this.isSneak)
        {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointY = 10.0F;
            this.bipedLeftLeg.rotationPointY = 10.0F;
            this.bipedRightLeg.rotateAngleX = -1.4F;
            this.bipedLeftLeg.rotateAngleX = -1.4F;
        }
        else
        {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
        }

        // Bras croises (zombies)
        if (this.heldItemRight == ModelBiped.RightArmPose.EMPTY
                && this.heldItemLeft == ModelBiped.LeftArmPose.EMPTY
                && this.onGround > 0.0F)
        {
            float f6 = MathHelper.sin(this.onGround * (float) Math.PI) * 0.5F + 0.5F;
            this.bipedRightArm.rotateAngleY = f6 * -0.4F;
            this.bipedLeftArm.rotateAngleY = f6 * 0.4F;
        }
    }

    /** Copie les angles d'une piece vers une autre (pour headwear, armures...). */
    public void copyModelAngles(ModelRenderer source, ModelRenderer target)
    {
        target.rotateAngleX = source.rotateAngleX;
        target.rotateAngleY = source.rotateAngleY;
        target.rotateAngleZ = source.rotateAngleZ;
    }

    public static enum RightArmPose
    {
        EMPTY,
        ITEM,
        BLOCK;
    }

    public static enum LeftArmPose
    {
        EMPTY,
        ITEM;
    }
}