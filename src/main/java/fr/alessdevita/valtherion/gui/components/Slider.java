package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

/**
 * Composant pour un curseur dans Valtherion.
 * Permet de sélectionner une valeur dans une plage.
 */
public class Slider extends GUI.GUIElement {

    // Valeur actuelle du curseur
    private float value;

    // Valeur minimale du curseur
    private float minValue;

    // Valeur maximale du curseur
    private float maxValue;

    // Couleur de la piste
    private int trackColor;

    // Couleur de la partie remplie de la piste
    private int filledTrackColor;

    // Couleur du pouce (bouton du curseur)
    private int thumbColor;

    // Rayon du pouce
    private float thumbRadius;

    // Indique si le pouce est en train d'être déplacé
    private boolean isDragging;

    // Action à exécuter lorsque la valeur change
    private Runnable onChangeAction;

    /**
     * Constructeur du curseur.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur du curseur.
     * @param height Hauteur du curseur.
     * @param minValue Valeur minimale.
     * @param maxValue Valeur maximale.
     */
    public Slider(float x, float y, float width, float height, float minValue, float maxValue) {
        super(x, y, width, height);
        this.value = minValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.trackColor = 0x303030; // Gris foncé
        this.filledTrackColor = 0x00FF00; // Vert
        this.thumbColor = 0xFFFFFF; // Blanc
        this.thumbRadius = 8.0f;
        this.isDragging = false;
        this.onChangeAction = null;
    }

    /**
     * Met à jour le curseur.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour le curseur
    }

    /**
     * Rend le curseur.
     */
    @Override
    public void render() {
        // Calculer la position du pouce en fonction de la valeur
        float thumbX = x + (value - minValue) / (maxValue - minValue) * width;

        // Logique pour rendre le curseur
        System.out.println("Rendu du curseur : " + value);
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Position du pouce : " + thumbX);
        System.out.println("Couleur de la piste remplie : " + Integer.toHexString(filledTrackColor));
    }

    /**
     * Gère un clic sur le curseur.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        if (contains(mouseX, mouseY)) {
            // Calculer la nouvelle valeur en fonction de la position de la souris
            float newValue = minValue + (mouseX - x) / width * (maxValue - minValue);
            setValue(newValue);
            isDragging = true;
        }
    }

    /**
     * Définit la valeur du curseur.
     * @param value Nouvelle valeur.
     */
    public void setValue(float value) {
        this.value = Math.max(minValue, Math.min(maxValue, value));

        // Exécuter l'action si elle est définie
        if (onChangeAction != null) {
            onChangeAction.run();
        }
    }

    /**
     * Définit l'action à exécuter lorsque la valeur change.
     * @param action Action à exécuter.
     */
    public void setOnChangeAction(Runnable action) {
        this.onChangeAction = action;
    }

    /**
     * Met à jour l'état de glisser-déposer du pouce.
     * @param isDragging true si le pouce est en train d'être déplacé, false sinon.
     */
    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    // --- Getters et Setters ---

    public float getValue() {
        return value;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        this.value = Math.max(value, minValue);
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        this.value = Math.min(value, maxValue);
    }

    public int getTrackColor() {
        return trackColor;
    }

    public void setTrackColor(int trackColor) {
        this.trackColor = trackColor;
    }

    public int getFilledTrackColor() {
        return filledTrackColor;
    }

    public void setFilledTrackColor(int filledTrackColor) {
        this.filledTrackColor = filledTrackColor;
    }

    public int getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(int thumbColor) {
        this.thumbColor = thumbColor;
    }

    public float getThumbRadius() {
        return thumbRadius;
    }

    public void setThumbRadius(float thumbRadius) {
        this.thumbRadius = thumbRadius;
    }

    public boolean isDragging() {
        return isDragging;
    }
}