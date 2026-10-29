package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

/**
 * MapItemRenderer — rend une carte tenue en main ou en cadre :
 * texture dynamique generee en GL (128x128 pixels de carte),
 * mise a jour par le MapData cote serveur.
 */
public class MapItemRenderer
{
    private final int textureWidth = 128;
    private final int textureHeight = 128;

    public MapItemRenderer()
    {
    }

    /**
     * Met a jour la texture de la carte (nom MC : updateMapTexture).
     * @param colors tableau 128x128 de couleurs ARGB
     */
    public void updateMapTexture(int[] colors)
    {
        // TODO : upload du tableau dans une texture GL (glTexImage2D)
        // ou via votre TextureManager dynamique
    }

    /**
     * Rend la carte dans le monde (nom MC : renderMap / renderMapItem).
     * @param isHeld true = tenue en main (perspective inclinee), false = cadre
     */
    public void renderMap(boolean isHeld, float scale)
    {
        GL11.glPushMatrix();

        if (isHeld)
        {
            // Perspective de la carte tenue : legerement inclinee
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
        }

        // Fond papier de la carte
        Gui.drawRect(0, 0, (int) (this.textureWidth * scale),
                (int) (this.textureHeight * scale), 0xFFF6E7C1);

        // TODO : bind de la texture dynamique de la carte
        // et rendu du contenu pixelise

        GL11.glPopMatrix();
    }
}