package fr.alessdevita.valtherion.client.model;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class ModelBox
{
    /** Nombre de faces d'un cube, et coordonnees UV de chaque face. */
    private final PositionTextureVertex[] vertexPositions;
    private final TexturedQuad[] quadList;
    private final float posX1;
    private final float posY1;
    private final float posZ1;
    private final float posX2;
    private final float posY2;
    private final float posZ2;
    public final String boxName;

    public ModelBox(ModelRenderer renderer, int texU, int texV,
                    float x, float y, float z, int width, int height, int depth,
                    float expansion, boolean mirror)
    {
        this((String) null, texU, texV, x, y, z, width, height, depth, expansion, mirror,
                renderer != null ? renderer.textureWidth : 16.0F,
                renderer != null ? renderer.textureHeight : 16.0F);
    }

    public ModelBox(String name, int texU, int texV,
                    float x, float y, float z, int width, int height, int depth,
                    float expansion, boolean mirror)
    {
        this(name, texU, texV, x, y, z, width, height, depth, expansion, mirror, 16.0F, 16.0F);
    }

    private ModelBox(String name, int texU, int texV,
                    float x, float y, float z, int width, int height, int depth,
                    float expansion, boolean mirror, float textureWidth, float textureHeight)
    {
        this.boxName = name;
        float f = expansion;
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + width;
        this.posY2 = y + height;
        this.posZ2 = z + depth;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f1 = x + width;
        float f2 = y + height;
        float f3 = z + depth;
        x -= f;
        y -= f;
        z -= f;
        f1 += f;
        f2 += f;
        f3 += f;

        if (mirror)
        {
            float f4 = f1;
            f1 = x;
            x = f4;
        }

        PositionTextureVertex vertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex vertex = new PositionTextureVertex(f1, y, z, 0.0F, 8.0F);
        PositionTextureVertex vertex1 = new PositionTextureVertex(f1, f2, z, 8.0F, 8.0F);
        PositionTextureVertex vertex2 = new PositionTextureVertex(x, f2, z, 8.0F, 0.0F);
        PositionTextureVertex vertex3 = new PositionTextureVertex(x, y, f3, 0.0F, 0.0F);
        PositionTextureVertex vertex4 = new PositionTextureVertex(f1, y, f3, 0.0F, 8.0F);
        PositionTextureVertex vertex5 = new PositionTextureVertex(f1, f2, f3, 8.0F, 8.0F);
        PositionTextureVertex vertex6 = new PositionTextureVertex(x, f2, f3, 8.0F, 0.0F);
        this.vertexPositions[0] = vertex2;
        this.vertexPositions[1] = vertex7;
        this.vertexPositions[2] = vertex;
        this.vertexPositions[3] = vertex1;
        this.vertexPositions[4] = vertex6;
        this.vertexPositions[5] = vertex3;
        this.vertexPositions[6] = vertex4;
        this.vertexPositions[7] = vertex5;

        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { vertex, vertex4, vertex5, vertex1 },
                texU + depth + width, texV + depth, texU + depth + width + width, texV + depth + height,
                textureWidth, textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { vertex7, vertex2, vertex6, vertex3 },
                texU, texV + depth, texU + width, texV + depth + height,
                textureWidth, textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { vertex, vertex7, vertex3, vertex4 },
                texU + depth, texV, texU + depth + width, texV + depth,
                textureWidth, textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { vertex1, vertex5, vertex6, vertex2 },
                texU + depth + width, texV, texU + depth + width + width, texV + depth,
                textureWidth, textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { vertex, vertex1, vertex5, vertex4 },
                texU + depth, texV + depth, texU + depth + width, texV + depth + height,
                textureWidth, textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { vertex7, vertex3, vertex6, vertex2 },
                texU + depth + width + width, texV + depth,
                texU + depth + width + width + width, texV + depth + height,
                textureWidth, textureHeight);

        if (mirror)
        {
            for (TexturedQuad quad : this.quadList)
            {
                quad.flipVertically();
            }
        }
    }

    /**
     * Rendu du cube en GL11 immediat, identique a Minecraft 1.7/1.8.
     */
    public void render(float scale)
    {
        GL11.glBegin(GL11.GL_QUADS);

        for (TexturedQuad quad : this.quadList)
        {
            for (PositionTextureVertex vertex : quad.vertexPositions)
            {
                GL11.glTexCoord2f(vertex.texturePositionX * quad.textureScaleX,
                        vertex.texturePositionY * quad.textureScaleY);
                GL11.glVertex3f(vertex.vector3D.xCoord * scale,
                        vertex.vector3D.yCoord * scale,
                        vertex.vector3D.zCoord * scale);
            }
        }

        GL11.glEnd();
    }

    /**
     * Retourne un point aleatoire a la surface du cube (particules de mort des mobs).
     */
    public Vec3 getRandomCube(Random rand)
    {
        float f = rand.nextFloat();
        float f1 = rand.nextFloat();

        if (this.posX1 == this.posX2)
        {
            return new Vec3(this.posX1,
                    this.posY1 + (this.posY2 - this.posY1) * f1,
                    this.posZ1 + (this.posZ2 - this.posZ1) * f);
        }
        else if (this.posY1 == this.posY2)
        {
            return new Vec3(this.posX1 + (this.posX2 - this.posX1) * f,
                    this.posY1,
                    this.posZ1 + (this.posZ2 - this.posZ1) * f1);
        }
        else
        {
            return new Vec3(this.posX1 + (this.posX2 - this.posX1) * f,
                    this.posY1 + (this.posY2 - this.posY1) * f1,
                    this.posZ1);
        }
    }

    /* -------------------------------------------------------------- */
    /* Classes internes (comme dans Minecraft : PositionTextureVertex, */
    /* TexturedQuad, Vec3 minimal)                                     */
    /* -------------------------------------------------------------- */

    public static class PositionTextureVertex
    {
        public final Vec3 vector3D;
        public float texturePositionX;
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

        public PositionTextureVertex setTexturePosition(float u, float v)
        {
            this.texturePositionX = u;
            this.texturePositionY = v;
            return this;
        }
    }

    public static class TexturedQuad
    {
        public final PositionTextureVertex[] vertexPositions;
        public final float textureScaleX;
        public final float textureScaleY;

        public TexturedQuad(PositionTextureVertex[] vertices, int u1, int v1, int u2, int v2,
                            float texWidth, float texHeight)
        {
            this.vertexPositions = vertices;
            float f = 0.0F / texWidth;
            float f1 = 0.0F / texHeight;
            this.textureScaleX = 1.0F / texWidth;
            this.textureScaleY = 1.0F / texHeight;
            vertices[0] = vertices[0].setTexturePosition((float) u2 / texWidth,
                    (float) v1 / texHeight);
            vertices[1] = vertices[1].setTexturePosition((float) u1 / texWidth,
                    (float) v1 / texHeight);
            vertices[2] = vertices[2].setTexturePosition((float) u1 / texWidth,
                    (float) v2 / texHeight);
            vertices[3] = vertices[3].setTexturePosition((float) u2 / texWidth,
                    (float) v2 / texHeight);
        }

        public void flipVertically()
        {
            for (int i = 0; i < this.vertexPositions.length / 2; ++i)
            {
                PositionTextureVertex tmp = this.vertexPositions[i];
                this.vertexPositions[i] = this.vertexPositions[this.vertexPositions.length - 1 - i];
                this.vertexPositions[this.vertexPositions.length - 1 - i] = tmp;
            }
        }
    }

    /** Vec3 minimal, a remplacer par votre classe utilitaire si elle existe. */
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