package fr.alessdevita.valtherion.rendering;

import org.lwjgl.opengl.*;

import java.nio.*;

/**
 * Classe pour gérer les framebuffers dans Valtherion.
 * Permet de rendre dans une texture pour le post-processing.
 */
public class Framebuffer {

    // ID du framebuffer
    private final int fboId;

    // ID de la texture de couleur
    private int colorTextureId;

    // ID du buffer de profondeur
    private int depthBufferId;

    // Largeur du framebuffer
    private int width;

    // Hauteur du framebuffer
    private int height;

    // Indique si le framebuffer a un buffer de profondeur
    private boolean hasDepthBuffer;

    // Indique si le framebuffer est complet
    private boolean isComplete;

    /**
     * Constructeur du framebuffer.
     * @param width Largeur du framebuffer.
     * @param height Hauteur du framebuffer.
     */
    public Framebuffer(int width, int height) {
        this(width, height, true);
    }

    /**
     * Constructeur du framebuffer avec un buffer de profondeur optionnel.
     * @param width Largeur du framebuffer.
     * @param height Hauteur du framebuffer.
     * @param hasDepthBuffer Indique si le framebuffer a un buffer de profondeur.
     */
    public Framebuffer(int width, int height, boolean hasDepthBuffer) {
        this.width = width;
        this.height = height;
        this.hasDepthBuffer = hasDepthBuffer;

        // Créer le framebuffer
        this.fboId = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

        // Créer la texture de couleur
        this.colorTextureId = createColorTexture();

        // Créer le buffer de profondeur si nécessaire
        if (hasDepthBuffer) {
            this.depthBufferId = createDepthBuffer();
        }

        // Vérifier si le framebuffer est complet
        this.isComplete = checkFramebufferStatus();

        // Délier le framebuffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Crée une texture de couleur.
     * @return ID de la texture de couleur.
     */
    private int createColorTexture() {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Allouer de la mémoire pour la texture
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                width,
                height,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                (ByteBuffer) null
        );

        // Définir les paramètres de la texture
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        // Attacher la texture au framebuffer
        GL32.glFramebufferTexture2D(
                GL30.GL_FRAMEBUFFER,
                GL30.GL_COLOR_ATTACHMENT0,
                GL11.GL_TEXTURE_2D,
                textureId,
                0
        );

        return textureId;
    }

    /**
     * Crée un buffer de profondeur.
     * @return ID du buffer de profondeur.
     */
    private int createDepthBuffer() {
        int depthBufferId = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferId);

        // Allouer de la mémoire pour le buffer de profondeur
        GL30.glRenderbufferStorage(
                GL30.GL_RENDERBUFFER,
                GL11.GL_DEPTH_COMPONENT,
                width,
                height
        );

        // Attacher le buffer de profondeur au framebuffer
        GL30.glFramebufferRenderbuffer(
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER,
                depthBufferId
        );

        return depthBufferId;
    }

    /**
     * Vérifie si le framebuffer est complet.
     * @return true si le framebuffer est complet, false sinon.
     */
    private boolean checkFramebufferStatus() {
        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        return status == GL30.GL_FRAMEBUFFER_COMPLETE;
    }

    /**
     * Lie le framebuffer.
     */
    public void bind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Délie le framebuffer.
     */
    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Lie la texture de couleur à une unité de texture.
     * @param textureUnit Unité de texture (GL_TEXTURE0, GL_TEXTURE1, etc.).
     */
    public void bindColorTexture(int textureUnit) {
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureId);
    }

    /**
     * Lie la texture de couleur à l'unité de texture par défaut (GL_TEXTURE0).
     */
    public void bindColorTexture() {
        bindColorTexture(GL13.GL_TEXTURE0);
    }

    // --- Getters ---

    public int getFboId() {
        return fboId;
    }

    public int getColorTextureId() {
        return colorTextureId;
    }

    public int getDepthBufferId() {
        return depthBufferId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasDepthBuffer() {
        return hasDepthBuffer;
    }

    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Libère les ressources du framebuffer.
     */
    public void cleanup() {
        GL30.glDeleteFramebuffers(fboId);
        GL11.glDeleteTextures(colorTextureId);
        if (hasDepthBuffer) {
            GL30.glDeleteRenderbuffers(depthBufferId);
        }
    }

    /**
     * Redimensionne le framebuffer.
     * @param width Nouvelle largeur.
     * @param height Nouvelle hauteur.
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        // Lier le framebuffer
        bind();

        // Re-créer la texture de couleur
        GL11.glDeleteTextures(colorTextureId);
        this.colorTextureId = createColorTexture();

        // Re-créer le buffer de profondeur si nécessaire
        if (hasDepthBuffer) {
            GL30.glDeleteRenderbuffers(depthBufferId);
            this.depthBufferId = createDepthBuffer();
        }

        // Vérifier si le framebuffer est complet
        this.isComplete = checkFramebufferStatus();

        // Délier le framebuffer
        unbind();
    }

    /**
     * Retourne une représentation textuelle du framebuffer.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Framebuffer{" +
                "fboId=" + fboId +
                ", colorTextureId=" + colorTextureId +
                ", width=" + width +
                ", height=" + height +
                ", hasDepthBuffer=" + hasDepthBuffer +
                ", isComplete=" + isComplete +
                '}';
    }
}