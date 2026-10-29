package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

/**
 * Composant pour une case à cocher dans Valtherion.
 * Permet d'activer ou désactiver une option.
 */
public class Checkbox extends GUI.GUIElement {

    // Texte de la case à cocher
    private String label;

    // État de la case à cocher (cochée ou non)
    private boolean isChecked;

    // Couleur du texte
    private int textColor;

    // Couleur de la case
    private int boxColor;

    // Couleur de la case quand elle est cochée
    private int checkedColor;

    // Taille de la case
    private float boxSize;

    // Action à exécuter lorsque l'état change
    private Runnable onChangeAction;

    /**
     * Constructeur de la case à cocher.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur totale (incluant le texte).
     * @param height Hauteur totale.
     * @param label Texte de la case à cocher.
     */
    public Checkbox(float x, float y, float width, float height, String label) {
        super(x, y, width, height);
        this.label = label;
        this.isChecked = false;
        this.textColor = 0xFFFFFF; // Blanc
        this.boxColor = 0x505050; // Gris
        this.checkedColor = 0x00FF00; // Vert
        this.boxSize = 16.0f;
        this.onChangeAction = null;
    }

    /**
     * Met à jour la case à cocher.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la case à cocher
    }

    /**
     * Rend la case à cocher.
     */
    @Override
    public void render() {
        // Logique pour rendre la case à cocher
        System.out.println("Rendu de la case à cocher : " + label);
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("État : " + (isChecked ? "cochée" : "non cochée"));
        System.out.println("Couleur de la case : " + Integer.toHexString(isChecked ? checkedColor : boxColor));
    }

    /**
     * Gère un clic sur la case à cocher.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        if (contains(mouseX, mouseY)) {
            // Inverser l'état de la case à cocher
            isChecked = !isChecked;

            // Exécuter l'action si elle est définie
            if (onChangeAction != null) {
                onChangeAction.run();
            }
        }
    }

    /**
     * Définit l'action à exécuter lorsque l'état change.
     * @param action Action à exécuter.
     */
    public void setOnChangeAction(Runnable action) {
        this.onChangeAction = action;
    }

    // --- Getters et Setters ---

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
    }

    public int getCheckedColor() {
        return checkedColor;
    }

    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
    }

    public float getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(float boxSize) {
        this.boxSize = boxSize;
    }
}