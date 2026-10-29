package fr.alessdevita.valtherion.client.model;

import org.lwjgl.opengl.GL11;

/**
 * ModelSign — le panneau (plancher de signs / pancartes).
 * Contrairement aux autres modeles, il n'extend pas ModelBase :
 * il est rendu manuellement par TileEntitySignRenderer avec deux
 * sous-ensembles : le support (pied) et la planche (board),
 * pour gerer les 3 orientations (mur, sol, et version "simple").
 */
public class ModelSign
{
    public ModelRenderer signBoard = new ModelRenderer(64, 32, 0, 0);
    public ModelRenderer signStick;

    public ModelSign()
    {
        // Planche du panneau : 24 x 12 x 2 (pixels de modele), UV 0,0 -> 24x12
        this.signBoard.addBox(-12.0F, -12.0F, -2.0F, 24, 12, 2, 0.0F);
        this.signBoard.rotationPointY = 0.0F;
        // Pied central derriere la planche
        this.signStick = new ModelRenderer(64, 32, 0, 14);
        this.signStick.addBox(-1.0F, -14.0F, -1.0F, 2, 14, 2, 0.0F);
        this.signStick.rotationPointY = 0.0F;
    }

    /**
     * Rendu du panneau pose sur le sol (sur un poteau).
     * @param scale 0.0625F (1/16) dans le renderer de TileEntity
     */
    public void renderSignBoard(float scale)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.75F * scale - 0.5F + 0.0F, 0.5F);
        this.signBoard.render(scale);
        GL11.glPopMatrix();
    }

    /**
     * Rendu du pied (poteau) sous la planche.
     */
    public void renderSignStick(float scale)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        this.signStick.render(scale);
        GL11.glPopMatrix();
    }
}