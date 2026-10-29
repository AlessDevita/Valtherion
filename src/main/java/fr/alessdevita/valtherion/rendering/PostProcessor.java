package fr.alessdevita.valtherion.rendering;

import org.joml.*;
import org.lwjgl.opengl.*;

import java.util.*;

/**
 * Classe pour gérer les effets post-processing dans Valtherion.
 * Applique des effets comme le blur, le bloom, ou la correction gamma sur la scène rendue.
 */
public class PostProcessor {

    // Framebuffer pour le rendu de la scène
    private Framebuffer sceneFramebuffer;

    // Framebuffer pour le rendu final
    private Framebuffer finalFramebuffer;

    // Shader pour le post-processing
    private Shader postProcessingShader;

    // Liste des effets post-processing
    private final List<PostProcessingEffect> effects;

    // Largeur de la fenêtre
    private int width;

    // Hauteur de la fenêtre
    private int height;

    // Indique si le post-processing est actif
    private boolean isActive;

    // Énumération des effets post-processing
    public enum PostProcessingEffect {
        NONE,           // Aucun effet
        GAMMA_CORRECTION, // Correction gamma
        BLUR,           // Flou
        BLOOM,          // Bloom (lueur)
        GRAYSCALE,      // Niveaux de gris
        INVERT,         // Inversion des couleurs
        SEPIA           // Sépia
    }

    /**
     * Constructeur du post-processor.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public PostProcessor(int width, int height) {
        this.width = width;
        this.height = height;
        this.effects = new ArrayList<>();
        this.isActive = true;

        try {
            // Créer les framebuffers
            this.sceneFramebuffer = new Framebuffer(width, height);
            this.finalFramebuffer = new Framebuffer(width, height);

            // Charger le shader de post-processing
            this.postProcessingShader = new Shader(
                    "shaders/post_processing_vertex.glsl",
                    "shaders/post_processing_fragment.glsl"
            );
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le post-processor : " + e.getMessage(), e);
        }
    }

    /**
     * Début du rendu de la scène.
     */
    public void beginScene() {
        if (!isActive) {
            return;
        }

        // Lier le framebuffer de la scène
        sceneFramebuffer.bind();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Fin du rendu de la scène et application des effets post-processing.
     */
    public void endScene() {
        if (!isActive) {
            return;
        }

        // Délier le framebuffer de la scène
        sceneFramebuffer.unbind();

        // Lier le framebuffer final
        finalFramebuffer.bind();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Appliquer les effets post-processing
        applyPostProcessing();

        // Délier le framebuffer final
        finalFramebuffer.unbind();
    }

    /**
     * Applique les effets post-processing.
     */
    private void applyPostProcessing() {
        // Utiliser le shader de post-processing
        postProcessingShader.bind();

        // Lier la texture de la scène
        sceneFramebuffer.bindColorTexture(GL13.GL_TEXTURE0);
        postProcessingShader.setUniform("sceneTexture", 0);

        // Définir les paramètres des effets
        for (PostProcessingEffect effect : effects) {
            setEffectParameters(effect);
        }

        // Dessiner un quad plein écran
        renderQuad();

        // Désactiver le shader
        postProcessingShader.unbind();
    }

    /**
     * Définit les paramètres d'un effet.
     * @param effect Effet post-processing.
     */
    private void setEffectParameters(PostProcessingEffect effect) {
        switch (effect) {
            case GAMMA_CORRECTION:
                postProcessingShader.setUniform("gamma", 2.2f);
                break;
            case BLUR:
                postProcessingShader.setUniform("blurEnabled", 1);
                break;
            case BLOOM:
                postProcessingShader.setUniform("bloomEnabled", 1);
                postProcessingShader.setUniform("bloomThreshold", 0.8f);
                postProcessingShader.setUniform("bloomIntensity", 1.5f);
                break;
            case GRAYSCALE:
                postProcessingShader.setUniform("grayscaleEnabled", 1);
                break;
            case INVERT:
                postProcessingShader.setUniform("invertEnabled", 1);
                break;
            case SEPIA:
                postProcessingShader.setUniform("sepiaEnabled", 1);
                break;
        }
    }

    /**
     * Rend un quad plein écran.
     */
    private void renderQuad() {
        // Vertices du quad plein écran
        float[] vertices = {
                -1.0f, -1.0f,  0.0f, 0.0f,
                1.0f, -1.0f,  1.0f, 0.0f,
                1.0f,  1.0f,  1.0f, 1.0f,
                -1.0f,  1.0f,  0.0f, 1.0f
        };

        // Indices pour les triangles
        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        // Créer le VAO
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Créer le VBO
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Créer l'EBO
        int eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Définir les attributs des vertices
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Dessiner le quad
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);

        // Délier le VAO
        GL30.glBindVertexArray(0);

        // Libérer les ressources
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        GL15.glDeleteBuffers(eboId);
    }

    /**
     * Ajoute un effet post-processing.
     * @param effect Effet à ajouter.
     */
    public void addEffect(PostProcessingEffect effect) {
        if (!effects.contains(effect)) {
            effects.add(effect);
        }
    }

    /**
     * Retire un effet post-processing.
     * @param effect Effet à retirer.
     */
    public void removeEffect(PostProcessingEffect effect) {
        effects.remove(effect);
    }

    /**
     * Active ou désactive le post-processing.
     * @param active true pour activer le post-processing.
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Redimensionne le post-processor.
     * @param width Nouvelle largeur.
     * @param height Nouvelle hauteur.
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        if (sceneFramebuffer != null) {
            sceneFramebuffer.resize(width, height);
        }
        if (finalFramebuffer != null) {
            finalFramebuffer.resize(width, height);
        }
    }

    // --- Getters ---

    public Framebuffer getSceneFramebuffer() {
        return sceneFramebuffer;
    }

    public Framebuffer getFinalFramebuffer() {
        return finalFramebuffer;
    }

    public Shader getPostProcessingShader() {
        return postProcessingShader;
    }

    public List<PostProcessingEffect> getEffects() {
        return effects;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Libère les ressources du post-processor.
     */
    public void cleanup() {
        if (sceneFramebuffer != null) {
            sceneFramebuffer.cleanup();
        }
        if (finalFramebuffer != null) {
            finalFramebuffer.cleanup();
        }
        if (postProcessingShader != null) {
            postProcessingShader.cleanup();
        }
        effects.clear();
    }

    /**
     * Retourne une représentation textuelle du post-processor.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PostProcessor{" +
                "width=" + width +
                ", height=" + height +
                ", effects=" + effects +
                ", isActive=" + isActive +
                '}';
    }
}