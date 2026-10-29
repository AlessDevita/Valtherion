package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * ModelZombieVillager — le zombie villageois : corps de zombie (ModelBiped)
 * avec la robe du villageois et le grand nez.
 */
public class ModelZombieVillager extends ModelVillager
{
    public ModelZombieVillager()
    {
        this(0.0F, 0.0F, 64, 64);
    }

    public ModelZombieVillager(float scale, float offsetY, int texW, int texH)
    {
        super(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        // On recupere d'abord les angles du villageois (robe, nez, bras croises)
        super.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entity);

        // Puis on remplace les bras croises par les bras de zombie tendus devant :
        // on reutilise les bras du bipede sous-jacent, en pose "zombie".
        this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F)
                + MathHelper.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F)
                + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.15F * limbSwingAmount;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);
        // Tete (le nez villageois est un enfant de la tete)
        this.bipedHead.render(scale);
        // Robe du villageois
        this.villagerRobe.render(scale);
        // Bras de zombie (les bras croises du villageois ne sont pas rendus)
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
    }
}