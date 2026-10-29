package fr.alessdevita.valtherion.client.model;

import org.lwjgl.opengl.GL11;

/**
 * ModelSkeletonHead — la tete de squelette / crane, utilise par le
 * TileEntitySkullRenderer pour le rendu des tetes posees au sol/mur,
 * et par le Wither pour ses 3 tetes.
 * Modele simple : une seule box tete 8x8x8 en rendu immediat.
 */
public class ModelSkeletonHead extends ModelBase
{
    public ModelRenderer head;

    public ModelSkeletonHead()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.head = new ModelRenderer(this, 0, 0);
        // Tete de squelette : box 8x8x8 avec expansion +1 pour recouvrir
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    /**
     * Rendu du crane (UV standard des tetes de mob 64x32, colonne 0,0).
     * @param scale 0.0625F (1/16) dans le renderer de TileEntity
     * @param rotationYaw rotation du crane pose
     */
    public void renderHead(float scale, float rotationYaw)
    {
        GL11.glPushMatrix();
        GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
        this.head.render(scale);
        GL11.glPopMatrix();
    }

    /**
     * Variante pour le rendu "au sol" avec le decalage standard du crane.
     */
    public void renderSkull(float scale, float rotationYaw)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.0F, 0.5F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
        this.head.render(scale);
        GL11.glPopMatrix();
    }
}