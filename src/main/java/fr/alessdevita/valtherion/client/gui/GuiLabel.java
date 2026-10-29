package fr.alessdevita.valtherion.client.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Classe pour les étiquettes de texte dans Valtherion.
 * Permet d'afficher du texte statique dans les interfaces.
 */
public class GuiLabel extends Gui.GuiElement {

    // Texte de l'étiquette
    private String text;

    // Couleur du texte
    private Vector4f textColor;

    // Taille du texte
    private float textSize;

    // Alignement du texte (LEFT, CENTER, RIGHT)
    private TextAlignment alignment;

    // Énumération pour l'alignement du texte
    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Constructeur de l'étiquette de texte.
     * @param x Position X.
     * @param y Position Y.
     * @param text Texte de l'étiquette.
     */
    public GuiLabel(float x, float y, String text) {
        super(x, y, 0, 0); // Largeur et hauteur calculées dynamiquement
        this.text = text;
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc
        this.textSize = 12.0f;
        this.alignment = TextAlignment.LEFT;
    }

    @Override
    public void render() {
        if (!isVisible) {
            return;
        }

        // Calculer la position en fonction de l'alignement
        float x = position.x;
        float y = position.y;

        switch (alignment) {
            case CENTER:
                x -= getTextWidth() / 2.0f;
                break;
            case RIGHT:
                x -= getTextWidth();
                break;
        }

        // Dessiner le texte (simplifié : rectangle)
        GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + getTextWidth(), y);
        GL11.glVertex2f(x + getTextWidth(), y + textSize);
        GL11.glVertex2f(x, y + textSize);
        GL11.glEnd();
    }

    /**
     * Calcule la largeur du texte.
     * @return Largeur du texte.
     */
    private float getTextWidth() {
        return text.length() * textSize * 0.6f;
    }

    // --- Getters et Setters ---

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Vector4f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector4f textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    /**
     * Retourne une représentation textuelle de l'étiquette.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiLabel{" +
                "text='" + text + '\'' +
                ", textColor=" + textColor +
                ", alignment=" + alignment +
                '}';
    }
}
