package fr.alessdevita.valtherion.client.model;

/**
 * TextureOffset — un decalage (x, y) dans la texture d'un modele.
 * Enregistre via ModelBase.setTextureOffset("nom", x, y), utilise
 * notamment par les modeles avec pieces nommees (ex : groin du cochon,
 * tetes du Wither) pour placer plusieurs boxes sur les memes UV.
 */
public class TextureOffset
{
    public final int textureOffsetX;
    public final int textureOffsetY;

    public TextureOffset(int x, int y)
    {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
    }

    @Override
    public String toString()
    {
        return "TextureOffset{x=" + this.textureOffsetX + ", y=" + this.textureOffsetY + "}";
    }
}