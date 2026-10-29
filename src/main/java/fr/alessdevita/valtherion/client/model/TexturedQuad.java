package fr.alessdevita.valtherion.client.model;

/**
 * TexturedQuad — une face texturable d'un ModelBox : 4 sommets
 * (PositionTextureVertex) avec leurs UV normalises par la taille
 * de la texture. Le miroir (flipVertically) inverse l'ordre des
 * sommets pour les pieces mirror=true.
 */
public class TexturedQuad
{
    public final PositionTextureVertex[] vertexPositions;
    public final int nVertices;
    /** Flag d'inversion (mis a true par flipVertically, pour le rendu GL). */
    public boolean invertNormal;

    public TexturedQuad(PositionTextureVertex[] vertices, int u1, int v1, int u2, int v2,
                        float texWidth, float texHeight)
    {
        this.nVertices = vertices.length;
        this.vertexPositions = vertices;
        this.invertNormal = false;

        // UV : u2/v2 en haut-gauche, u1/v1 en bas-droite (mapping MC 1.7/1.8)
        vertices[0] = vertices[0].setTexturePosition((float) u2 / texWidth, (float) v1 / texHeight);
        vertices[1] = vertices[1].setTexturePosition((float) u1 / texWidth, (float) v1 / texHeight);
        vertices[2] = vertices[2].setTexturePosition((float) u1 / texWidth, (float) v2 / texHeight);
        vertices[3] = vertices[3].setTexturePosition((float) u2 / texWidth, (float) v2 / texHeight);
    }

    public TexturedQuad(PositionTextureVertex[] vertices, float u1, float v1,
                        float u2, float v2, float texWidth, float texHeight)
    {
        this.nVertices = vertices.length;
        this.vertexPositions = vertices;
        this.invertNormal = false;

        // Variante float (expansions fractionnaires)
        vertices[0] = vertices[0].setTexturePosition(u2 / texWidth, v1 / texHeight);
        vertices[1] = vertices[1].setTexturePosition(u1 / texWidth, v1 / texHeight);
        vertices[2] = vertices[2].setTexturePosition(u1 / texWidth, v2 / texHeight);
        vertices[3] = vertices[3].setTexturePosition(u2 / texWidth, v2 / texHeight);
    }

    /**
     * Inverse l'ordre des sommets (pour mirror=true) : la face est
     * vue "de l'autre cote" et la texture reste coherente.
     */
    public void flipVertically()
    {
        for (int i = 0; i < this.nVertices / 2; ++i)
        {
            PositionTextureVertex tmp = this.vertexPositions[i];
            this.vertexPositions[i] = this.vertexPositions[this.nVertices - 1 - i];
            this.vertexPositions[this.nVertices - 1 - i] = tmp;
        }
    }
}