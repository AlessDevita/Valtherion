package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

/**
 * Composant pour une barre de progression dans Valtherion.
 * Utilisé pour afficher la santé, l'expérience, la nourriture, etc.
 */
public class ProgressBar extends GUI.GUIElement {

    // Valeur actuelle de la barre
    private float currentValue;

    // Valeur maximale de la barre
    private float maxValue;

    // Couleur de la barre de progression
    private int progressColor;

    // Couleur de fond de la barre
    private int backgroundColor;

    // Couleur de la bordure
    private int borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    // Orientation de la barre (HORIZONTAL ou VERTICAL)
    private Orientation orientation;

    // Énumération pour l'orientation de la barre
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Constructeur de la barre de progression.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param maxValue Valeur maximale.
     */
    public ProgressBar(float x, float y, float width, float height, float maxValue) {
        super(x, y, width, height);
        this.currentValue = 0.0f;
        this.maxValue = maxValue;
        this.progressColor = 0x00FF00; // Vert
        this.backgroundColor = 0x303030; // Gris foncé
        this.borderColor = 0x000000; // Noir
        this.borderThickness = 1.0f;
        this.orientation = Orientation.HORIZONTAL;
    }

    /**
     * Met à jour la valeur actuelle de la barre.
     * @param currentValue Nouvelle valeur actuelle.
     */
    public void setCurrentValue(float currentValue) {
        this.currentValue = Math.max(0, Math.min(currentValue, maxValue));
    }

    /**
     * Met à jour la barre de progression.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la barre de progression
    }

    /**
     * Rend la barre de progression.
     */
    @Override
    public void render() {
        // Calculer le pourcentage de progression
        float progressPercentage = currentValue / maxValue;

        // Logique pour rendre la barre de progression
        System.out.println("Rendu de la barre de progression : " + (progressPercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur de progression : " + Integer.toHexString(progressColor));
    }

    /**
     * Gère un clic sur la barre de progression.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        // Pas d'action par défaut pour un clic sur la barre de progression
    }

    // --- Getters et Setters ---

    public float getCurrentValue() {
        return currentValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        this.currentValue = Math.min(currentValue, maxValue);
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}