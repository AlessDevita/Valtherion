package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * ModelVillager — herite de ModelBiped : tete avec grand nez,
 * longue robe qui remplace les jambes, bras croises dans les manches
 * et une ceinture (villagerArmsCrossed dans les angles).
 */
public class ModelVillager extends ModelBiped
{
    public ModelRenderer villagerArmsCrossed;
    public ModelRenderer villagerRobe;
    public ModelRenderer villagerBigNose;

    public ModelVillager(float scale)
    {
        super(scale, 0.0F, 64, 64);
        // Grand nez (box fine sous la tete)
        this.villagerBigNose = new ModelRenderer(this, 24, 0);
        this.villagerBigNose.addBox(-1.0F, -4.0F, -10.0F, 2, 4, 2, scale);
        this.villagerBigNose.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addChild(this.villagerBigNose);

        // Robe : corps allonge jusqu'au sol (remplace visuellement les jambes)
        this.villagerRobe = new ModelRenderer(this, 0, 38);
        this.villagerRobe.addBox(-5.0F, 0.0F, -3.0F, 10, 20, 6, scale);
        this.villagerRobe.setRotationPoint(0.0F, 2.0F, 0.0F);

        // Bras croises : box horizontale en travers du corps
        this.villagerArmsCrossed = new ModelRenderer(this, 44, 28);
        this.villagerArmsCrossed.addBox(-3.0F, -2.0F, -12.0F, 6, 4, 12, scale);
        this.villagerArmsCrossed.setRotationPoint(0.0F, 6.0F, 0.0F);
    }

    public ModelVillager()
    {
        this(0.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);
        // Tete (le nez est un enfant, rendu automatiquement)
        this.bipedHead.render(scale);
        // Robe a la place du corps et des jambes
        this.villagerRobe.render(scale);
        // Bras croises (on masque les bras biped classiques)
        this.villagerArmsCrossed.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        this.bipedHead.rotateAngleY = headYaw / (180.0F / (float) Math.PI);
        this.bipedHead.rotateAngleX = headPitch / (180.0F / (float) Math.PI);

        // Legere oscillation de la robe pendant la marche
        this.villagerRobe.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;

        // Les bras croises suivent la rotation du corps mais restent calmes
        this.villagerArmsCrossed.rotationPointY = 4.0F
                + MathHelper.cos(age * 0.09F) * 0.05F + 0.05F;

        // Sneak : la robe se penche comme le reste du corps
        if (this.isSneak)
        {
            this.villagerRobe.rotateAngleX += 0.4F;
        }
    }

    @Override
    public void copyModelAnglesTo(ModelBase target)
    {
        super.copyModelAnglesTo(target);

        if (target instanceof ModelVillager)
        {
            ModelVillager villager = (ModelVillager) target;
            villager.villagerRobe.rotateAngleX = this.villagerRobe.rotateAngleX;
            villager.villagerArmsCrossed.rotationPointY = this.villagerArmsCrossed.rotationPointY;
        }
    }
}