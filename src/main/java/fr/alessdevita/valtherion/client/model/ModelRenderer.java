package fr.alessdevita.valtherion.client.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ModelRenderer
{
    /** Taille de la texture du modele, 64x32 par defaut. */
    public float textureWidth = 64.0F;
    public float textureHeight = 32.0F;

    /** Decalage de texture dans la sprite sheet. */
    public int textureOffsetX;
    public int textureOffsetY;

    /** Point de pivot de la piece (en 1/16 de bloc). */
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;

    /** Rotation de la piece en radians. */
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;

    /** Miroir (inversion des faces, pour bras jambes opposees). */
    private boolean mirror;

    /** ID de la display list OpenGL, 0 = pas compilee. */
    private int displayList;
    private boolean compiled;

    /** Liste des cubes composant cette piece. */
    public List<ModelBox> cubeList = new ArrayList<ModelBox>();
    /** Modeles enfants attaches a cette piece. */
    private final List<ModelRenderer> childModels = new ArrayList<ModelRenderer>();

    public ModelRenderer(ModelBase model, int offsetX, int offsetY)
    {
        this.textureOffsetX = offsetX;
        this.textureOffsetY = offsetY;
        this.textureWidth = model.textureWidth;
        this.textureHeight = model.textureHeight;
        model.boxList.add(this);
    }

    public ModelRenderer(ModelBase model)
    {
        this(model, 0, 0);
    }

    public ModelRenderer(int i, int i1, int i2, int i3) {
    }

    public ModelRenderer(ModelCow modelCow, int offsetX, int offsetY) {
    }

    /**
     * Definit le decalage de texture (setTextureOffset).
     */
    public ModelRenderer setTextureOffset(int x, int y)
    {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    /**
     * addBox — ajoute un cube a cette piece, identique a Minecraft 1.7/1.8.
     */
    public ModelRenderer addBox(String name, float x, float y, float z, int w, int h, int d)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY,
                x, y, z, w, h, d, 0.0F, this.mirror));
        return this;
    }

    public ModelRenderer addBox(float x, float y, float z, int w, int h, int d)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY,
                x, y, z, w, h, d, 0.0F, this.mirror));
        return this;
    }

    public ModelRenderer addBox(float x, float y, float z, int w, int h, int d, float expansion)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY,
                x, y, z, w, h, d, expansion, this.mirror));
        return this;
    }

    public void setRotationPoint(float x, float y, float z)
    {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    public void setRotation(float x, float y, float z)
    {
        this.rotateAngleX = x;
        this.rotateAngleY = y;
        this.rotateAngleZ = z;
    }

    /**
     * Attache un modele enfant : il herite des transformations du parent (addChild).
     */
    public void addChild(ModelRenderer renderer)
    {
        this.childModels.add(renderer);
    }

    /**
     * Rendu de la piece : compilation paresseuse en display list, puis rotations
     * dans l'ordre Y, X, Z exactement comme Minecraft.
     */
    public void render(float scale)
    {
        if (!this.compiled)
        {
            this.displayList = GL11.glGenLists(1);
            GL11.glNewList(this.displayList, GL11.GL_COMPILE);
            this.compileDisplayList(scale);
            GL11.glEndList();
            this.compiled = true;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale,
                this.rotationPointZ * scale);

        if (this.rotateAngleY != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
        }

        if (this.rotateAngleX != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
        }

        if (this.rotateAngleZ != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
        }

        GL11.glCallList(this.displayList);

        for (ModelRenderer modelrenderer : this.childModels)
        {
            modelrenderer.render(scale);
        }

        GL11.glPopMatrix();
    }

    /**
     * Applique seulement les transformations (postRender) — utilise pour
     * attacher des items dans la main, les pieces d'armure, etc.
     */
    public void postRender(float scale)
    {
        if (!this.compiled)
        {
            this.displayList = GL11.glGenLists(1);
            GL11.glNewList(this.displayList, GL11.GL_COMPILE);
            this.compileDisplayList(scale);
            GL11.glEndList();
            this.compiled = true;
        }

        GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale,
                this.rotationPointZ * scale);

        if (this.rotateAngleY != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
        }

        if (this.rotateAngleX != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
        }

        if (this.rotateAngleZ != 0.0F)
        {
            GL11.glRotatef(this.rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
        }
    }

    private void compileDisplayList(float scale)
    {
        for (ModelBox modelbox : this.cubeList)
        {
            modelbox.render(scale);
        }
    }

    /**
     * Libere la display list OpenGL (appele au changement de resource pack).
     */
    public void deleteDisplayList()
    {
        if (this.compiled)
        {
            GL11.glDeleteLists(this.displayList, 1);
            this.compiled = false;
            this.displayList = 0;
        }
    }

    public void setMirror(boolean mirror)
    {
        this.mirror = mirror;
    }

    public void copyModelAngles(ModelRenderer bipedHead) {

    }
}