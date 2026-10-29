package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

/**
 * Composant pour une étiquette de texte dans Valtherion.
 * Affiche du texte statique à l'écran.
 */
public class Label extends GUI.GUIElement {

    // Texte de l'étiquette
    private String text;

    // Couleur du texte
    private int textColor;

    // Police du texte
    private String font;

    // Taille du texte
    private float fontSize;

    // Alignement du texte (LEFT, CENTER, RIGHT)
    private TextAlignment alignment;

    // Énumération pour l'alignement du texte
    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Constructeur de l'étiquette.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de l'étiquette.
     * @param height Hauteur de l'étiquette.
     * @param text Texte de l'étiquette.
     */
    public Label(float x, float y, float width, float height, String text) {
        super(x, y, width, height);
        this.text = text;
        this.textColor = 0xFFFFFF; // Blanc
        this.font = "Arial";
        this.fontSize = 12.0f;
        this.alignment = TextAlignment.LEFT;
    }

    /**
     * Met à jour l'étiquette.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour l'étiquette
    }

    /**
     * Rend l'étiquette.
     */
    @Override
    public void render() {
        // Logique pour rendre l'étiquette
        System.out.println("Rendu de l'étiquette : " + text);
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Couleur du texte : " + Integer.toHexString(textColor));
    }

    /**
     * Gère un clic sur l'étiquette.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        // Pas d'action par défaut pour un clic sur l'étiquette
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

    public TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }
}