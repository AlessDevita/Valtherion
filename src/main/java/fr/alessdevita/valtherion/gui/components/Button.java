package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

/**
 * Composant pour un bouton interactif dans Valtherion.
 * Peut être cliqué pour déclencher une action.
 */
public class Button extends GUI.GUIElement {

    // Texte du bouton
    private String text;

    // Couleur du texte
    private int textColor;

    // Couleur de fond du bouton
    private int backgroundColor;

    // Couleur de fond du bouton au survol
    private int hoverBackgroundColor;

    // Couleur de fond du bouton au clic
    private int clickBackgroundColor;

    // Action à exécuter lors du clic
    private Runnable onClickAction;

    // Indique si le bouton est en survol
    private boolean isHovered;

    // Indique si le bouton est cliqué
    private boolean isClicked;

    // Police du texte
    private String font;

    // Taille du texte
    private float fontSize;

    /**
     * Constructeur du bouton.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur du bouton.
     * @param height Hauteur du bouton.
     * @param text Texte du bouton.
     */
    public Button(float x, float y, float width, float height, String text) {
        super(x, y, width, height);
        this.text = text;
        this.textColor = 0xFFFFFF; // Blanc
        this.backgroundColor = 0x505050; // Gris
        this.hoverBackgroundColor = 0x707070; // Gris clair
        this.clickBackgroundColor = 0x303030; // Gris foncé
        this.onClickAction = null;
        this.isHovered = false;
        this.isClicked = false;
        this.font = "Arial";
        this.fontSize = 12.0f;
    }

    /**
     * Définit l'action à exécuter lors du clic.
     * @param action Action à exécuter.
     */
    public void setOnClickAction(Runnable action) {
        this.onClickAction = action;
    }

    /**
     * Met à jour le bouton.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour le bouton
    }

    /**
     * Rend le bouton.
     */
    @Override
    public void render() {
        // Logique pour rendre le bouton
        // Exemple : Dessiner un rectangle avec le texte au centre
        int currentBackgroundColor = backgroundColor;

        if (isClicked) {
            currentBackgroundColor = clickBackgroundColor;
        } else if (isHovered) {
            currentBackgroundColor = hoverBackgroundColor;
        }

        System.out.println("Rendu du bouton : " + text + " (" + x + ", " + y + ", " + width + ", " + height + ")");
        System.out.println("Couleur de fond : " + Integer.toHexString(currentBackgroundColor));
        System.out.println("Couleur du texte : " + Integer.toHexString(textColor));
    }

    /**
     * Gère un clic sur le bouton.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        if (contains(mouseX, mouseY)) {
            isClicked = true;
            if (onClickAction != null) {
                onClickAction.run();
            }
        }
    }

    /**
     * Met à jour l'état de survol du bouton.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    public void updateHover(float mouseX, float mouseY) {
        isHovered = contains(mouseX, mouseY);
    }

    /**
     * Réinitialise l'état du bouton.
     */
    public void resetState() {
        isClicked = false;
    }

    // --- Getters et Setters ---

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(int hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public int getClickBackgroundColor() {
        return clickBackgroundColor;
    }

    public void setClickBackgroundColor(int clickBackgroundColor) {
        this.clickBackgroundColor = clickBackgroundColor;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }
}