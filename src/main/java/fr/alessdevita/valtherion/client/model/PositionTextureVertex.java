package fr.alessdevita.valtherion.client.model;

/**
 * PositionTextureVertex — un sommet 3D avec ses coordonnees UV,
 * utilise par TexturedQuad pour dessiner les faces des ModelBox.
 * (Classe separee chez vous, interne a ModelBox dans Minecraft.)
 */
public class PositionTextureVertex
{
    /** Position 3D du sommet. */
    public final Vec3 vector3D;
    /** Coordonnee U de texture (0..1 ou 0..textureWidth selon l'usage). */
    public float texturePositionX;
    /** Coordonnee V de texture. */
    public float texturePositionY;

    public PositionTextureVertex(float x, float y, float z, float u, float v)
    {
        this(new Vec3(x, y, z), u, v);
    }

    public PositionTextureVertex(Vec3 vector, float u, float v)
    {
        this.vector3D = vector;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }

    /**
     * Redefinit les UV du sommet et retourne this (chainage).
     */
    public PositionTextureVertex setTexturePosition(float u, float v)
    {
        this.texturePositionX = u;
        this.texturePositionY = v;
        return this;
    }

    /**
     * Redefinit la position 3D du sommet et retourne this (chainage).
     */
    public PositionTextureVertex setVector3D(Vec3 vector)
    {
        return new PositionTextureVertex(vector, this.texturePositionX, this.texturePositionY);
    }

    /**
     * Vec3 minimal (float) — meme version que celui de ModelBox.
     */
    public static class Vec3
    {
        public final float xCoord;
        public final float yCoord;
        public final float zCoord;

        public Vec3(float x, float y, float z)
        {
            this.xCoord = x;
            this.yCoord = y;
            this.zCoord = z;
        }
    }
}