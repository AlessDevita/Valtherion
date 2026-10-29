package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * ModelSkeleton — herite de ModelBiped mais reconstruit bras et jambes
 * fins (2x12x2), avec les poses d'arc (aimedBow) et d'item.
 */
public class ModelSkeleton extends ModelBiped
{
    public ModelSkeleton()
    {
        this(0.0F);
    }

    public ModelSkeleton(float scale)
    {
        super(scale, 0.0F, 64, 32);
        // Bras fins : 2 x 12 x 2
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, scale);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.setMirror(true);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        // Jambes fines : 2 x 12 x 2
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, scale);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.setMirror(true);
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, scale);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.heldItemRight = ModelBiped.RightArmPose.EMPTY;
        this.heldItemLeft = ModelBiped.LeftArmPose.EMPTY;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entity);

        // Les bras du squelette restent plus pres du corps (balancement reduit)
        this.bipedRightArm.rotateAngleX *= 0.5F;
        this.bipedLeftArm.rotateAngleX *= 0.5F;
        this.bipedRightArm.rotateAngleZ *= 0.5F;
        this.bipedLeftArm.rotateAngleZ *= 0.5F;

        // Pose d'arc : bras gauche tendu, bras droit qui tire (aimedBow)
        if (this.heldItemRight == ModelBiped.RightArmPose.BLOCK)
        {
            // Pose "epieu/bloc" du squelette : bras leves
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
                    - ((float) Math.PI / 10F);
        }

        // Marche : les fines jambes balancent un peu plus largement
        float f = 1.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F
                * limbSwingAmount / 0.25F * 0.25F * f;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
                * limbSwingAmount / 0.25F * 0.25F * f;
    }

    /**
     * Pose de tir a l'arc : appelee par le renderer quand le squelette
     * vise le joueur (swingProgress / aimedBow dans MC).
     */
    public void postRenderArc(float partialTicks)
    {
        // Bras gauche tendu vers l'avant, bras droit replie vers l'arriere
        this.bipedLeftArm.rotateAngleX = -1.2F;
        this.bipedRightArm.rotateAngleX = -0.4F;
        this.bipedRightArm.rotateAngleY = 0.3F;
    }
}