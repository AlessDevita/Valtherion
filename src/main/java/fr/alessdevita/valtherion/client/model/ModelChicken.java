package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;

public class ModelChicken extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer beak;
    public ModelRenderer chin;
    /** Poussin (echelle 0.5) : tete plus grosse proportionnellement. */
    public boolean isChild;

    public ModelChicken()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        byte b0 = 0;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
        this.head.setRotationPoint(0.0F, (float) b0, -4.0F);
        this.beak = new ModelRenderer(this, 14, 0);
        this.beak.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
        this.beak.setRotationPoint(0.0F, (float) b0, -4.0F);
        this.chin = new ModelRenderer(this, 14, 0);
        this.chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 1, 0.0F);
        this.chin.setRotationPoint(0.0F, (float) b0, -4.0F);
        this.body = new ModelRenderer(this, 0, 9);
        this.body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 26, 0);
        this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F);
        this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
        this.leftLeg = new ModelRenderer(this, 26, 0);
        this.leftLeg.setMirror(true);
        this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F);
        this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
        this.rightWing = new ModelRenderer(this, 24, 13);
        this.rightWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
        this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
        this.rightWing.rotateAngleZ = 0.17453292519943295F;
        this.leftWing = new ModelRenderer(this, 24, 13);
        this.leftWing.setMirror(true);
        this.leftWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
        this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
        this.leftWing.rotateAngleZ = -0.17453292519943295F;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);

        if (this.isChild)
        {
            // Poussin : tete et beac sur echelle 0.5, corps sur 0.5 aussi
            float f = 2.0F;
            GL11PushScale(scale, scale, scale, 0.5F);
            this.head.render(scale);
            this.beak.render(scale);
            this.chin.render(scale);
            this.body.render(scale);
            this.rightLeg.render(scale);
            this.leftLeg.render(scale);
            this.rightWing.render(scale);
            this.leftWing.render(scale);
            GL11PopScale();
        }
        else
        {
            this.head.render(scale);
            this.beak.render(scale);
            this.chin.render(scale);
            this.body.render(scale);
            this.rightLeg.render(scale);
            this.leftLeg.render(scale);
            this.rightWing.render(scale);
            this.leftWing.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        this.head.rotateAngleY = headYaw / (180.0F / (float) Math.PI);
        this.head.rotateAngleX = headPitch / (180.0F / (float) Math.PI);
        this.beak.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleY = this.head.rotateAngleY;

        // Battement des ailes : en vol (limbSwingAmount eleve) elles claquent vite
        fr.alessdevita.valtherion.utils.MathHelper MathHelper = new fr.alessdevita.valtherion.utils.MathHelper();
        this.rightWing.rotateAngleZ = 0.17453292519943295F
                + MathHelper.cos(age * 7.0F) * (float) Math.PI * limbSwingAmount * 0.2F;
        this.leftWing.rotateAngleZ = -0.17453292519943295F
                - MathHelper.cos(age * 7.0F) * (float) Math.PI * limbSwingAmount * 0.2F;

        // Les ailes s'ecartent en chute (wingSpreading)
        this.rightWing.rotateAngleZ = this.rightWing.rotateAngleZ + limbSwingAmount * 0.3F;
        this.leftWing.rotateAngleZ = this.leftWing.rotateAngleZ - limbSwingAmount * 0.3F;

        // Marche des pattes
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F
                * limbSwingAmount;
    }

    /* Helpers d'echelle poussin (glPushMatrix/glScalef de LWJGL 3). */
    private void GL11PushScale(float x, float y, float z, float factor)
    {
        org.lwjgl.opengl.GL11.glPushMatrix();
        org.lwjgl.opengl.GL11.glScalef(factor, factor, factor);
    }

    private void GL11PopScale()
    {
        org.lwjgl.opengl.GL11.glPopMatrix();
    }
}