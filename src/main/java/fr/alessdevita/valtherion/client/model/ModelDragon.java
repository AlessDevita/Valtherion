package fr.alessdevita.valtherion.client.model;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * Le dragon : tete articulee + cou en 3 segments + corps + queue en 8 segments
 * + ailes en 3 pieces chacune (os principal, os externe, membrane).
 * Les angles d'ailes sont calcules dans setRotationAngles avec des sinus
 * decales, comme dans Minecraft 1.7/1.8.
 */
public class ModelDragon extends ModelBase
{
    private ModelRenderer head;
    private ModelRenderer jaw;
    private ModelRenderer[] neckSegments = new ModelRenderer[3];
    private ModelRenderer body;
    private ModelRenderer[] tailSegments = new ModelRenderer[8];
    private ModelRenderer[] wings = new ModelRenderer[2];
    private ModelRenderer[] wingBones = new ModelRenderer[2];
    private ModelRenderer[] wingTips = new ModelRenderer[2];
    private float[] partialOffsets = new float[24];
    private float lastYaw;
    private float lastPitch;

    public ModelDragon()
    {
        this.textureWidth = 256;
        this.textureHeight = 256;

        // Tete (UV 0, 0 sur une texture 256x256)
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-6.0F, -4.0F, -14.0F, 12, 8, 14, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -20.0F);
        // Machoire (enfant de la tete, s'ouvre)
        this.jaw = new ModelRenderer(this, 44, 0);
        this.jaw.addBox(-4.0F, 2.0F, -14.0F, 8, 4, 14, 0.0F);
        this.jaw.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.head.addChild(this.jaw);

        // Cou en 3 segments, chaque segment enfant du precedent
        this.neckSegments[0] = new ModelRenderer(this, 0, 22);
        this.neckSegments[0].addBox(-5.0F, -3.0F, -6.0F, 10, 6, 12, 0.0F);
        this.neckSegments[0].setRotationPoint(0.0F, 8.0F, -20.0F);
        this.neckSegments[1] = new ModelRenderer(this, 0, 40);
        this.neckSegments[1].addBox(-5.0F, -3.0F, -6.0F, 10, 6, 12, 0.0F);
        this.neckSegments[1].setRotationPoint(0.0F, 2.0F, -8.0F);
        this.neckSegments[2] = new ModelRenderer(this, 0, 58);
        this.neckSegments[2].addBox(-5.0F, -3.0F, -6.0F, 10, 6, 12, 0.0F);
        this.neckSegments[2].setRotationPoint(0.0F, 2.0F, -8.0F);
        this.neckSegments[0].addChild(this.neckSegments[1]);
        this.neckSegments[1].addChild(this.neckSegments[2]);
        this.neckSegments[2].addChild(this.head);

        // Corps
        this.body = new ModelRenderer(this, 20, 76);
        this.body.addBox(-16.0F, -12.0F, -6.0F, 32, 24, 12, 0.0F);
        this.body.setRotationPoint(0.0F, 4.0F, 0.0F);

        // Queue en 8 segments, chaque segment enfant du precedent
        this.tailSegments[0] = new ModelRenderer(this, 0, 136);
        this.tailSegments[0].addBox(-9.0F, -7.0F, -8.0F, 18, 14, 16, 0.0F);
        this.tailSegments[0].setRotationPoint(0.0F, 8.0F, 8.0F);
        int texY = 152;

        for (int i = 1; i < this.tailSegments.length; ++i)
        {
            this.tailSegments[i] = new ModelRenderer(this, 0, texY + i * 16);

            if (i < 4)
            {
                this.tailSegments[i].addBox(-9.0F, -7.0F, -8.0F, 18, 14, 16, 0.0F);
                this.tailSegments[i].setRotationPoint(0.0F, 0.0F, 14.0F);
            }
            else if (i < 6)
            {
                this.tailSegments[i].addBox(-7.0F, -6.0F, -8.0F, 14, 12, 16, 0.0F);
                this.tailSegments[i].setRotationPoint(0.0F, 0.0F, 14.0F);
            }
            else
            {
                this.tailSegments[i].addBox(-5.0F, -5.0F, -8.0F, 10, 10, 16, 0.0F);
                this.tailSegments[i].setRotationPoint(0.0F, 0.0F, 12.0F);
            }

            this.tailSegments[i - 1].addChild(this.tailSegments[i]);
        }

        // Ailes : os principal (attache au corps), os externe, membrane
        for (int side = 0; side < 2; ++side)
        {
            boolean mirror = side == 0;
            this.wings[side] = new ModelRenderer(this, 112, 0);
            this.wings[side].addBox(-12.0F, -4.0F, -4.0F, 12, 8, 16, 0.0F);
            this.wings[side].setRotationPoint(0.0F, 0.0F, 0.0F);
            this.wingBones[side] = new ModelRenderer(this, 112, 24);
            this.wingBones[side].addBox(-12.0F, -2.0F, -2.0F, 10, 4, 10, 0.0F);
            this.wingBones[side].setRotationPoint(-10.0F, 0.0F, 0.0F);
            this.wingTips[side] = new ModelRenderer(this, 148, 0);
            this.wingTips[side].addBox(-2.0F, -6.0F, -26.0F, 24, 12, 18, 0.0F);
            this.wingTips[side].setRotationPoint(-8.0F, 0.0F, 0.0F);
            this.wings[side].addChild(this.wingBones[side]);
            this.wings[side].addChild(this.wingTips[side]);

            if (mirror)
            {
                // cote gauche : miroir sur Y
                this.wings[side].rotateAngleY = (float) Math.PI;
            }
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, age, headYaw, headPitch, scale, entityIn);
        this.body.render(scale);
        this.neckSegments[0].render(scale);
        this.tailSegments[0].render(scale);
        this.wings[0].render(scale);
        this.wings[1].render(scale);
    }

    /**
     * Interpole la direction du vol pour orienter le corps du dragon.
     */
    private float interpolateAngle(float prev, float current, float partialTicks)
    {
        float f = current - prev;

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        while (f < -180.0F)
        {
            f += 360.0F;
        }

        return prev + f * partialTicks;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
        // --- Ondulation du cou (segments qui serpentent) ---
        for (int i = 0; i < 3; ++i)
        {
            this.neckSegments[i].rotateAngleY = MathHelper.cos(age * 0.45F + (float) i * 0.45F) * 0.15F;
            this.neckSegments[i].rotateAngleX = MathHelper.sin(age * 0.3F + (float) i * 0.3F) * 0.05F;
        }

        // --- Queue : vague qui se propage ---
        for (int i = 0; i < this.tailSegments.length; ++i)
        {
            this.tailSegments[i].rotateAngleY = MathHelper.cos(age * 0.35F + (float) i * 0.4F) * 0.18F
                    * (float) (i + 1) / 8.0F;
        }

        // --- Tete suit le regard du dragon ---
        this.head.rotateAngleY = (headYaw - this.lastYaw) / (180.0F / (float) Math.PI);
        this.head.rotateAngleX = (headPitch - this.lastPitch) / (180.0F / (float) Math.PI);
        // Machoire legèrement entrouverte
        this.jaw.rotateAngleX = 0.1F + MathHelper.sin(age * 0.2F) * 0.05F;
        this.lastYaw = headYaw;
        this.lastPitch = headPitch;

        // --- Battement d'ailes : deux phases decalees pour un effet realiste ---
        float flap = MathHelper.sin(age * 0.35F);
        this.wings[0].rotateAngleZ = 0.6F - flap * 0.5F;
        this.wings[1].rotateAngleZ = -(0.6F - flap * 0.5F);
        // Les pointes d'ailes suivent avec un leger retard
        this.wingTips[0].rotateAngleY = flap * 0.25F;
        this.wingTips[1].rotateAngleY = flap * 0.25F;
        this.wingBones[0].rotateAngleZ = flap * 0.15F;
        this.wingBones[1].rotateAngleZ = flap * 0.15F;

        // Le corps suit la direction du vol
        this.body.rotateAngleX = MathHelper.sin(age * 0.2F) * 0.05F;
    }
}