package fr.alessdevita.valtherion.client.model;

import org.lwjgl.opengl.GL11;

/**
 * ModelChest — le coffre, rendu comme dans Minecraft 1.7/1.8 :
 * une base fixe (lidBase) et un couvercle (lid) qui pivote
 * sur sa charniere arriere (angle = halfsin du progres d'ouverture).
 */
public class ModelChest
{
    public ModelRenderer chestLid = new ModelRenderer(64, 64, 0, 0);
    public ModelRenderer chestBelow = new ModelRenderer(64, 64, 0, 19);
    public ModelRenderer chestKnob;

    public ModelChest()
    {
        // Couvercle : 14x10x14 avec le pivot sur la face arriere
        this.chestLid.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
        this.chestLid.rotationPointY = 9.0F;
        this.chestLid.rotationPointZ = 1.0F;

        // Base : 14x10x14 posee sous le couvercle
        this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
        this.chestBelow.rotationPointY = 0.0F;
        this.chestBelow.rotationPointZ = 1.0F;

        // Loquet au centre de la face avant du couvercle
        this.chestKnob = new ModelRenderer(64, 64, 0, 0);
        this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
        this.chestKnob.rotationPointY = 8.0F;
        this.chestKnob.rotationPointZ = 16.0F;
        this.chestLid.addChild(this.chestKnob);
    }

    /**
     * Rendu du coffre complet.
     * @param lidAngle progress d'ouverture : 0 = ferme, 1 = ouvert
     */
    public void renderAll(float lidAngle)
    {
        // Le couvercle pivote autour de sa charniere arriere
        this.chestLid.rotateAngleX = -(lidAngle * (float) Math.PI / 2.0F);
        this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;

        float f = 1.0F / 16.0F; // conversion bloc -> pixel de modele

        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        this.chestBelow.render(f);
        this.chestLid.render(f);
        GL11.glPopMatrix();
    }
}