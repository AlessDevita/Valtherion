package fr.alessdevita.valtherion.client.model;

public class ModelPig extends ModelQuadruped
{
    public ModelPig()
    {
        this(10, false); // corps de 10 de haut, pas de pis
    }

    /**
     * @param par1 hauteur du corps
     * @param hasUdder true pour afficher le pis (variante vache du modele)
     */
    public ModelPig(int par1, boolean hasUdder)
    {
        super(par1, 0.0F);
        // Tete du cochon : plus courte que la vache, avec le groin
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 12.0F, -6.0F);
        // Groin
        this.head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, 0.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, 0.0F);
        this.body.setRotationPoint(0.0F, 11.0F, 2.0F);
        // 4 pattes plus courtes que la vache (6 de haut)
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg1.setRotationPoint(-3.0F, 18.0F, 4.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg2.setRotationPoint(3.0F, 18.0F, 4.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg3.setRotationPoint(-3.0F, 18.0F, -4.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.leg4.setRotationPoint(3.0F, 18.0F, -4.0F);
    }
}