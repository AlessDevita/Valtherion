package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;

import org.joml.Vector3f;

/**
 * Composant pour afficher les FPS dans le HUD de Valtherion.
 */
public class FPSCounter extends HUD.HUDElement {

    // Nombre de FPS actuels
    private int fps;

    // Temps écoulé depuis la dernière mise à jour (en secondes)
    private float elapsedTime;

    // Nombre de frames depuis la dernière mise à jour
    private int frameCount;

    // Couleur du texte des FPS
    private Vector3f textColor;

    /**
     * Constructeur du compteur de FPS.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de l'élément.
     * @param height Hauteur de l'élément.
     */
    public FPSCounter(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.fps = 0;
        this.elapsedTime = 0.0f;
        this.frameCount = 0;
        this.textColor = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc
    }

    /**
     * Met à jour le compteur de FPS.
     */
    @Override
    public void update() {
        // Incrémenter le compteur de frames
        frameCount++;
    }

    /**
     * Met à jour les FPS avec le temps écoulé.
     * @param deltaTime Temps écoulé depuis le dernier tick (en secondes).
     */
    public void updateFPS(float deltaTime) {
        elapsedTime += deltaTime;

        // Mettre à jour les FPS toutes les secondes
        if (elapsedTime >= 1.0f) {
            fps = frameCount;
            frameCount = 0;
            elapsedTime = 0.0f;
        }
    }

    /**
     * Rend le compteur de FPS.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Rendre le texte des FPS
        String fpsText = "FPS: " + fps;
        fontRenderer.renderText(
                fpsText,
                x,
                y,
                1.0f,
                textColor
        );

        System.out.println("Rendu du compteur de FPS : " + fps);
    }

    // --- Getters et Setters ---

    public int getFPS() {
        return fps;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    public Vector3f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector3f textColor) {
        this.textColor = textColor;
    }
}