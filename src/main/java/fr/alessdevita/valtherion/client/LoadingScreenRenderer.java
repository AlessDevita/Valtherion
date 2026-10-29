package fr.alessdevita.valtherion.client;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

/**
 * Classe pour gérer le rendu de l'écran de chargement dans Valtherion.
 * Affiche une barre de progression et un logo pendant le chargement.
 */
public class LoadingScreenRenderer {

    // ID de la fenêtre GLFW
    private final long windowId;

    // Largeur de la fenêtre
    private final int windowWidth;

    // Hauteur de la fenêtre
    private final int windowHeight;

    // Progression du chargement (0.0 à 1.0)
    private float progress;

    // Couleur de la barre de progression
    private float[] progressBarColor = {0.2f, 0.6f, 1.0f, 1.0f}; // Bleu clair

    // Couleur de fond
    private float[] backgroundColor = {0.0f, 0.0f, 0.0f, 1.0f}; // Noir

    // Indique si l'écran de chargement est initialisé
    private boolean isInitialized;

    /**
     * Constructeur de l'écran de chargement.
     * @param windowId ID de la fenêtre GLFW.
     * @param windowWidth Largeur de la fenêtre.
     * @param windowHeight Hauteur de la fenêtre.
     */
    public LoadingScreenRenderer(long windowId, int windowWidth, int windowHeight) {
        this.windowId = windowId;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public LoadingScreenRenderer(Valtherion valtherion, long windowId, int windowWidth, int windowHeight) {
        this.windowId = windowId;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void displayProgressMessage(String message) {
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    public void resetProgressAndMessage(String message) {
        this.progress = 0.0F;
        displayProgressMessage(message);
    }

    /**
     * Initialise l'écran de chargement.
     */
    public void init() {
        if (isInitialized) {
            return;
        }

        // Lier le contexte OpenGL
        GLFW.glfwMakeContextCurrent(windowId);

        // Créer les capacités OpenGL
        if (GL.getCapabilities() == null) {
            GL.createCapabilities();
        }

        // Configurer le viewport
        GL11.glViewport(0, 0, windowWidth, windowHeight);

        // Configurer la matrice de projection (orthographique pour le 2D)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Désactiver le depth test (pas nécessaire en 2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        isInitialized = true;
    }

    /**
     * Rend l'écran de chargement.
     */
    public void render() {
        if (!isInitialized) {
            init();
        }

        // Effacer l'écran
        GL11.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Dessiner la barre de progression
        drawProgressBar();

        // Dessiner le texte de chargement
        drawLoadingText();
    }

    /**
     * Dessine la barre de progression.
     */
    private void drawProgressBar() {
        // Calculer la largeur de la barre de progression
        int barWidth = (int) (windowWidth * 0.6f);
        int barHeight = 20;
        int barX = (windowWidth - barWidth) / 2;
        int barY = windowHeight / 2 + 20;

        // Dessiner le fond de la barre (gris foncé)
        GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + barWidth, barY);
        GL11.glVertex2i(barX + barWidth, barY + barHeight);
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glEnd();

        // Dessiner la barre de progression (bleu clair)
        int progressWidth = (int) (barWidth * progress);
        GL11.glColor4f(progressBarColor[0], progressBarColor[1], progressBarColor[2], progressBarColor[3]);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + progressWidth, barY);
        GL11.glVertex2i(barX + progressWidth, barY + barHeight);
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le texte de chargement.
     */
    private void drawLoadingText() {
        // Dessiner le texte "Chargement..." (simplifié, sans police)
        int textX = windowWidth / 2 - 50;
        int textY = windowHeight / 2 - 20;

        // Dessiner un rectangle pour simuler le texte
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(textX, textY);
        GL11.glVertex2i(textX + 100, textY);
        GL11.glVertex2i(textX + 100, textY + 20);
        GL11.glVertex2i(textX, textY + 20);
        GL11.glEnd();
    }

    /**
     * Met à jour la progression.
     * @param progress Progression (0.0 à 1.0).
     */
    public void setProgress(float progress) {
        this.progress = Math.max(0.0f, Math.min(1.0f, progress));
    }

    /**
     * Incrémente la progression.
     * @param increment Incrément de progression.
     */
    public void incrementProgress(float increment) {
        setProgress(progress + increment);
    }

    /**
     * Définit la couleur de la barre de progression.
     * @param r Composante rouge.
     * @param g Composante verte.
     * @param b Composante bleue.
     * @param a Composante alpha.
     */
    public void setProgressBarColor(float r, float g, float b, float a) {
        this.progressBarColor = new float[] {r, g, b, a};
    }

    /**
     * Définit la couleur de fond.
     * @param r Composante rouge.
     * @param g Composante verte.
     * @param b Composante bleue.
     * @param a Composante alpha.
     */
    public void setBackgroundColor(float r, float g, float b, float a) {
        this.backgroundColor = new float[] {r, g, b, a};
    }

    /**
     * Nettoie les ressources.
     */
    public void cleanup() {
        // Rien à nettoyer pour l'instant
    }

    // --- Getters ---

    public float getProgress() {
        return progress;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
