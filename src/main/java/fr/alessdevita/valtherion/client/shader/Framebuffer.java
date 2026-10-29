package fr.alessdevita.valtherion.client.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Framebuffer Object (FBO) pour le rendu hors-écran et le post-processing.
 * Crée une texture couleur et un renderbuffer de profondeur.
 */
public final class Framebuffer {
    private int framebufferId;
    private int colorTextureId;
    private int depthBufferId;
    private final boolean useDepth;

    private int framebufferWidth;
    private int framebufferHeight;

    private float clearColorR = 0F;
    private float clearColorG = 0F;
    private float clearColorB = 0F;
    private float clearColorA = 0F;

    private int viewportX;
    private int viewportY;
    private int viewportWidth;
    private int viewportHeight;

    /**
     * Construit un framebuffer avec profondeur, de la taille de l'écran.
     *
     * @param displayWidth  largeur en pixels
     * @param displayHeight hauteur en pixels
     */
    public Framebuffer(int displayWidth, int displayHeight) {
        this(displayWidth, displayHeight, true);
    }

    /**
     * Construit un framebuffer de la taille donnée.
     *
     * @param displayWidth  largeur en pixels
     * @param displayHeight hauteur en pixels
     * @param useDepth      {@code true} pour attacher un buffer de profondeur
     */
    public Framebuffer(int displayWidth, int displayHeight, boolean useDepth) {
        this.useDepth = useDepth;
        this.framebufferWidth = Math.max(1, displayWidth);
        this.framebufferHeight = Math.max(1, displayHeight);
        this.viewportWidth = this.framebufferWidth;
        this.viewportHeight = this.framebufferHeight;
        createFramebuffer();
    }

    /** Crée (ou recrée) l'FBO et ses attachments. */
    private void createFramebuffer() {
        deleteFramebuffer();

        this.framebufferId = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebufferId);

        // Texture couleur
        this.colorTextureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.colorTextureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,
                this.framebufferWidth, this.framebufferHeight,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                GL11.GL_TEXTURE_2D, this.colorTextureId, 0);

        // Renderbuffer de profondeur
        if (this.useDepth) {
            this.depthBufferId = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.depthBufferId);
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24,
                    this.framebufferWidth, this.framebufferHeight);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                    GL30.GL_RENDERBUFFER, this.depthBufferId);
        }

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Framebuffer incomplet (status=0x"
                    + Integer.toHexString(status) + ")");
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /** Définit la couleur de nettoyage du framebuffer. */
    public void setFramebufferColor(float r, float g, float b, float a) {
        this.clearColorR = r;
        this.clearColorG = g;
        this.clearColorB = b;
        this.clearColorA = a;
    }

    /** Lie le framebuffer pour le rendu. */
    public void bindFramebuffer(boolean viewport) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.framebufferId);
        if (viewport) {
            GL11.glViewport(0, 0, this.framebufferWidth, this.framebufferHeight);
        }
    }

    /** Détache le framebuffer et restaure le framebuffer par défaut (écran). */
    public void unbindFramebuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(this.viewportX, this.viewportY, this.viewportWidth, this.viewportHeight);
    }

    /**
     * Effectue le rendu du framebuffer à l'écran (blit ou quad plein écran).
     *
     * @param width  largeur de la zone de destination
     * @param height hauteur de la zone de destination
     */
    public void framebufferRender(int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.colorTextureId);

        // Rendu d'un quad plein écran avec la texture du framebuffer.
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        float tx = 0F;
        float ty = 0F;
        float tw = 1F;
        float th = 1F;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(tx, ty);       GL11.glVertex2f(0F, height);
        GL11.glTexCoord2f(tw, ty);       GL11.glVertex2f(width, height);
        GL11.glTexCoord2f(tw, ty + th);  GL11.glVertex2f(width, 0F);
        GL11.glTexCoord2f(tx, ty + th);  GL11.glVertex2f(0F, 0F);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /** Efface le framebuffer avec la couleur définie. */
    public void framebufferClear() {
        GL11.glClearColor(this.clearColorR, this.clearColorG,
                this.clearColorB, this.clearColorA);
        int mask = GL11.GL_COLOR_BUFFER_BIT;
        if (this.useDepth) mask |= GL11.GL_DEPTH_BUFFER_BIT;
        GL11.glClear(mask);
    }

    /** Redimensionne le framebuffer. */
    public void resize(int newWidth, int newHeight) {
        newWidth = Math.max(1, newWidth);
        newHeight = Math.max(1, newHeight);
        if (newWidth == this.framebufferWidth && newHeight == this.framebufferHeight) return;
        this.framebufferWidth = newWidth;
        this.framebufferHeight = newHeight;
        createFramebuffer();
    }

    public void setViewport(int x, int y, int width, int height) {
        this.viewportX = x;
        this.viewportY = y;
        this.viewportWidth = Math.max(1, width);
        this.viewportHeight = Math.max(1, height);
    }

    public int getFramebufferId() { return this.framebufferId; }
    public int getColorTextureId() { return this.colorTextureId; }
    public int getDepthBufferId() { return this.depthBufferId; }
    public int getFramebufferWidth() { return this.framebufferWidth; }
    public int getFramebufferHeight() { return this.framebufferHeight; }

    /** Supprime les ressources OpenGL associées. */
    public void deleteFramebuffer() {
        if (this.colorTextureId > 0) {
            GL11.glDeleteTextures(this.colorTextureId);
            this.colorTextureId = 0;
        }
        if (this.depthBufferId > 0) {
            GL30.glDeleteRenderbuffers(this.depthBufferId);
            this.depthBufferId = 0;
        }
        if (this.framebufferId > 0) {
            GL30.glDeleteFramebuffers(this.framebufferId);
            this.framebufferId = 0;
        }
    }
}
