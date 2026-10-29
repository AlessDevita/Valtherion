package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;

import org.joml.Vector3f;

import java.util.*;

/**
 * Composant pour afficher des notifications dans le HUD de Valtherion.
 */
public class Notification extends HUD.HUDElement {

    // Texte de la notification
    private String message;

    // Temps restant avant la disparition (en secondes)
    private float remainingTime;

    // Temps total d'affichage (en secondes)
    private float totalTime;

    // Couleur du texte
    private Vector3f textColor;

    // Couleur de fond
    private Vector3f backgroundColor;

    // Opacité de la notification
    private float opacity;

    // Indique si la notification est en train de disparaître
    private boolean isFadingOut;

    /**
     * Constructeur de la notification.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la notification.
     * @param height Hauteur de la notification.
     * @param message Message de la notification.
     * @param totalTime Temps total d'affichage (en secondes).
     */
    public Notification(float x, float y, float width, float height, String message, float totalTime) {
        super(x, y, width, height);
        this.message = message;
        this.remainingTime = totalTime;
        this.totalTime = totalTime;
        this.textColor = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc
        this.backgroundColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.opacity = 1.0f;
        this.isFadingOut = false;
    }

    /**
     * Met à jour la notification.
     */
    @Override
    public void update() {
        // Mettre à jour le temps restant
        remainingTime -= 0.016f; // Supposons un deltaTime de 16ms (60 FPS)

        // Vérifier si la notification doit commencer à disparaître
        if (remainingTime <= 1.0f && !isFadingOut) {
            isFadingOut = true;
        }

        // Mettre à jour l'opacité
        if (isFadingOut) {
            opacity = remainingTime;
        }

        // Vérifier si la notification doit disparaître
        if (remainingTime <= 0) {
            opacity = 0.0f;
        }
    }

    /**
     * Rend la notification.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        if (opacity <= 0) {
            return;
        }

        // Logique pour rendre la notification
        System.out.println("Rendu de la notification : " + message);
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Opacité : " + opacity);

        // Rendre le texte de la notification
        fontRenderer.renderText(
                message,
                x,
                y,
                1.0f,
                new Vector3f(textColor.x, textColor.y, textColor.z)
        );
    }

    // --- Getters et Setters ---

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public Vector3f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector3f textColor) {
        this.textColor = textColor;
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0, Math.min(1, opacity));
    }

    public boolean isFadingOut() {
        return isFadingOut;
    }

    public void setFadingOut(boolean fadingOut) {
        isFadingOut = fadingOut;
    }
}