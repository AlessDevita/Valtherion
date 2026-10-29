package fr.alessdevita.valtherion.client.model;

public class ModelPlayer extends ModelBiped
{
    /** true = modele "Alex" (bras fins, 3px), false = modele "Steve" (4px). */
    public final boolean smallArms;

    public ModelPlayer(float scale, boolean smallArmsIn)
    {
        super(scale, 0.0F, 64, 64);
        this.smallArms = smallArmsIn;

        // Le modele player utilise une texture 64x64 : on reconstruit les bras/jambes
        // avec les emplacements UV sur 2 lignes, et les surcouches (wear).

        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

        this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
        this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

        this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
        this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale + 0.25F);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 0.0F);

        // Bras en fonction du modele : Steve (12px de large en tex) ou Alex (3px)
        this.bipedLeftArm = new ModelRenderer(this, 32, 48);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
    }

    @Override
    public void render(fr.alessdevita.valtherion.entity.Entity entityIn, float limbSwing,
                       float limbSwingAmount, float age, float headYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        this.bipedBodyWear.render(scale);
        this.bipedRightArmwear.render(scale);
        this.bipedLeftArmwear.render(scale);
        this.bipedRightLegwear.render(scale);
        this.bipedLeftLegwear.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, fr.alessdevita.valtherion.entity.Entity entity)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entity);
        this.copyModelAngles(this.bipedBody, this.bipedBodyWear);
        this.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        this.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        this.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        this.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);

        if (this.smallArms)
        {
            // Legere inclinaison du bras fin vers l'exterieur
            this.bipedRightArmwear.rotateAngleZ += 0.05F;
        }
    }
}