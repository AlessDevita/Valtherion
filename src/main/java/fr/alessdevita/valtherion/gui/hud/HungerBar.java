package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour la barre de nourriture dans le HUD de Valtherion.
 */
public class HungerBar extends HUD.HUDElement {

    // Joueur associé à la barre de nourriture
    private final Player player;

    // Niveau de nourriture actuel
    private float hungerLevel;

    // Niveau maximal de nourriture
    private float maxHungerLevel;

    // Couleur de la barre de nourriture
    private Vector3f hungerColor;

    // Couleur de fond de la barre
    private Vector3f backgroundColor;

    // Couleur de la bordure
    private Vector3f borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    /**
     * Constructeur de la barre de nourriture.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param player Joueur associé.
     */
    public HungerBar(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.hungerLevel = 100.0f; // Niveau initial de nourriture
        this.maxHungerLevel = 100.0f;
        this.hungerColor = new Vector3f(1.0f, 0.5f, 0.0f); // Orange
        this.backgroundColor = new Vector3f(0.2f, 0.2f, 0.2f); // Gris foncé
        this.borderColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.borderThickness = 1.0f;
    }

    /**
     * Met à jour la barre de nourriture.
     */
    @Override
    public void update() {
        // Mettre à jour le niveau de nourriture (ex: diminuer avec le temps)
        // hungerLevel -= 0.1f;
        // hungerLevel = Math.max(0, hungerLevel);
    }

    /**
     * Rend la barre de nourriture.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Calculer le pourcentage de nourriture
        float hungerPercentage = hungerLevel / maxHungerLevel;

        // Calculer la largeur de la barre de nourriture
        float hungerWidth = width * hungerPercentage;

        // Logique pour rendre la barre de nourriture
        System.out.println("Rendu de la barre de nourriture : " + (hungerPercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur de la nourriture : " + hungerColor);

        // Rendre le texte de la nourriture
        String hungerText = (int) hungerLevel + "/" + (int) maxHungerLevel;
        fontRenderer.renderText(
                hungerText,
                x + width / 2 - 20,
                y + height / 2 - 10,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    // --- Getters et Setters ---

    public float getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(float hungerLevel) {
        this.hungerLevel = Math.max(0, Math.min(hungerLevel, maxHungerLevel));
    }

    public float getMaxHungerLevel() {
        return maxHungerLevel;
    }

    public void setMaxHungerLevel(float maxHungerLevel) {
        this.maxHungerLevel = maxHungerLevel;
        this.hungerLevel = Math.min(hungerLevel, maxHungerLevel);
    }

    public Vector3f getHungerColor() {
        return hungerColor;
    }

    public void setHungerColor(Vector3f hungerColor) {
        this.hungerColor = hungerColor;
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vector3f getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Vector3f borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }
}