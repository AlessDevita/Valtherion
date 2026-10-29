package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * ModelZombie — herite de ModelBiped : bras tendus devant (isAttacking /
 * armsRaised), ne se balancent pas comme un bipede normal.
 */
public class ModelZombie extends ModelBiped
{
    public ModelZombie()
    {
        this(0.0F, 0.0F, 64, 32);
    }

    public ModelZombie(float scale, float offsetY, int texW, int texH)
    {
        super(scale, offsetY, texW, texH);
        // Compression de la liste de boxes : le zombie reutilise
        // exactement les pieces du bipede (tete, corps, bras, jambes).
    }

    /**
     * Pose "bras leves" du zombie (classique MC) : les deux bras
     * tendus horizontalement devant, legerement balancants.
     */
    public void setArmsRaised(boolean raised)
    {
        if (raised)
        {
            this.bipedRightArm.rotateAngleX = -1.5F + MathHelper.sin(this.onGround * 5.0F) * 0.05F;
            this.bipedLeftArm.rotateAngleX = -1.5F + MathHelper.cos(this.onGround * 5.0F) * 0.05F;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entity);

        // Les deux bras tendus devant, exactement comme dans MC :
        // -PI/2 (horizontal) + un leger dandinement oppose des deux bras
        this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F)
                + MathHelper.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F)
                + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.15F * limbSwingAmount;
        this.bipedRightArm.rotateAngleZ = MathHelper.cos(age * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ = -MathHelper.cos(age * 0.09F) * 0.05F - 0.05F;
    }

    /**
     * Rendu complet du zombie (redefini car les bras croises du ModelBiped
     * de base ne doivent pas s'appliquer).
     */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);
        this.bipedHead.render(scale);
        this.bipedHeadwear.render(scale);
        this.bipedBody.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
    }
}