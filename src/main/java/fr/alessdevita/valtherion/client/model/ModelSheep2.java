package fr.alessdevita.valtherion.client.model;

/**
 * ModelSheep2 — la SURCOUCHE de laine du mouton.
 * Ne rend que les pieces "wool" ; le renderer l'utilise en deuxieme passe
 * avec la texture de laine (teintee) par-dessus ModelSheep1.
 * Les angles sont copies depuis le modele de base a chaque frame
 * via copyModelAngles, exactement comme dans Minecraft.
 */
public class ModelSheep2 extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer body;
    private ModelRenderer leg1;
    private ModelRenderer leg2;
    private ModelRenderer leg3;
    private ModelRenderer leg4;

    public ModelSheep2()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;

        // Laine de la tete (expansion +1.75 pour entourer la tete nue)
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.5F, -4.5F, -6.5F, 7, 7, 7, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -8.0F);

        // Laine du corps (expansion importante autour du corps de ModelSheep1)
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-4.5F, -13.5F, -7.5F, 9, 18, 8, 0.0F);
        this.body.setRotationPoint(0.0F, 11.0F, 2.0F);

        // Laine des pattes (expansion autour des pattes nues)
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.5F, -0.5F, -2.5F, 5, 9, 5, 0.0F);
        this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.5F, -0.5F, -2.5F, 5, 9, 5, 0.0F);
        this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.5F, -0.5F, -2.5F, 5, 9, 5, 0.0F);
        this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.5F, -0.5F, -2.5F, 5, 9, 5, 0.0F);
        this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
    }

    /**
     * Copie tous les angles du modele de base (peau) vers la laine,
     * pour que la laine suive exactement les animations.
     */
    public void setModelAttributes(ModelSheep1 base)
    {
        super.copyModelAnglesTo(base);
        this.head.rotateAngleX = base.head.rotateAngleX;
        this.head.rotateAngleY = base.head.rotateAngleY;
        this.head.rotateAngleZ = base.head.rotateAngleZ;
        this.body.rotateAngleX = base.body.rotateAngleX;
        this.body.rotateAngleY = base.body.rotateAngleY;
        this.body.rotateAngleZ = base.body.rotateAngleZ;
        this.leg1.rotateAngleX = base.leg1.rotateAngleX;
        this.leg1.rotateAngleY = base.leg1.rotateAngleY;
        this.leg1.rotateAngleZ = base.leg1.rotateAngleZ;
        this.leg2.rotateAngleX = base.leg2.rotateAngleX;
        this.leg2.rotateAngleY = base.leg2.rotateAngleY;
        this.leg2.rotateAngleZ = base.leg2.rotateAngleZ;
        this.leg3.rotateAngleX = base.leg3.rotateAngleX;
        this.leg3.rotateAngleY = base.leg3.rotateAngleY;
        this.leg3.rotateAngleZ = base.leg3.rotateAngleZ;
        this.leg4.rotateAngleX = base.leg4.rotateAngleX;
        this.leg4.rotateAngleY = base.leg4.rotateAngleY;
        this.leg4.rotateAngleZ = base.leg4.rotateAngleZ;
    }

    /**
     * Rendu de la laine uniquement — appele par le renderer apres
     * le rendu de la peau (ModelSheep1) avec la texture de laine bindee.
     */
    public void renderWool(float scale)
    {
        this.head.render(scale);
        this.body.render(scale);
        this.leg1.render(scale);
        this.leg2.render(scale);
        this.leg3.render(scale);
        this.leg4.render(scale);
    }
}