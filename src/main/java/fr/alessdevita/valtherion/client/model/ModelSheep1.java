package fr.alessdevita.valtherion.client.model;

/**
 * ModelSheep1 — le corps du mouton SANS la laine (la "peau").
 * Le rendu de la laine est delegue a ModelSheep2 (surcouche).
 * Heritage direct de ModelQuadruped avec les UV du mouton.
 */
public class ModelSheep1 extends ModelQuadruped
{
    public ModelSheep1()
    {
        super(12, 0.0F);
        // Tete du mouton : avec la "toison" du front (box fine sur le dessus)
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
        // Corps : texture 40x0 (le modele mouton est plus fin que la vache)
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-4.0F, -12.0F, -7.0F, 8, 16, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 11.0F, 2.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
    }
}