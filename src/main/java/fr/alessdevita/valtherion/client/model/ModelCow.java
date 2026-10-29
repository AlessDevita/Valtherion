package fr.alessdevita.valtherion.client.model;

public class ModelCow extends ModelQuadruped
{
    public ModelRenderer udder;

    public ModelCow()
    {
        super(12, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
        this.head.rotateAngleX = 0.17453292519943295F;
        this.body = new ModelRenderer(this, 18, 4);
        this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.body.setRotationPoint(0.0F, 11.0F, 8.0F);
        this.udder = new ModelRenderer(this, 18, 4);
        this.udder.addBox(-4.0F, 7.0F, -4.0F, 8, 6, 1, 0.0F);
        this.udder.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.udder.rotateAngleX = 0.17453292519943295F;
        // Le pis (mammelle) suit le corps (rendu automatique en tant qu'enfant)
        this.body.addChild(this.udder);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg1.setRotationPoint(-4.0F, 14.0F, 6.0F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg2.setRotationPoint(4.0F, 14.0F, 6.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg3.setRotationPoint(-4.0F, 14.0F, -3.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.leg4.setRotationPoint(4.0F, 14.0F, -3.0F);
    }
}