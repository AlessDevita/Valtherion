package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import fr.alessdevita.valtherion.client.resources.ResourceManager;
import fr.alessdevita.valtherion.client.settings.GameSettings;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Classe pour rendre du texte de manière basique dans Valtherion.
 * Utilise OpenGL pour dessiner des rectangles représentant les caractères.
 * (Version simplifiée sans STB TrueType)
 */
public class FontRenderer {

    public static final int FONT_HEIGHT = 8;
    // Taille de la police
    private float fontSize;

    // Espacement entre les caractères
    private float charSpacing;

    // Couleur du texte
    private Vector4f textColor;

    // Largeur des caractères (en pixels)
    private float charWidth;

    // Hauteur des caractères (en pixels)
    private float charHeight;

    // Indique si le rendu de texte est initialisé
    private boolean isInitialized;

    /**
     * Constructeur du rendu de texte.
     *
     * @param fontSize Taille de la police.
     */
    public FontRenderer(float fontSize) {
        this.fontSize = fontSize;
        this.charSpacing = 2.0f;
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc
        this.charWidth = fontSize * 0.6f; // Largeur proportionnelle à la taille
        this.charHeight = fontSize;
        this.isInitialized = true; // Pas besoin d'initialisation pour cette version
    }

    public FontRenderer(GameSettings gameSettings, String s, ResourceManager resourceManager, boolean b) {
    }

    public FontRenderer() {
        this(16.0f);
    }

    public FontRenderer(GameSettings gameSettings, String s, TextureManager renderEngine, boolean b) {
    }

    public static boolean isAllowedCharacter(char typedChar) {
        return false;
    }

    /**
     * Initialise le rendu de texte.
     * (Pas nécessaire pour cette version simplifiée)
     */
    public void init() {
        isInitialized = true;
    }

    /**
     * Rend du texte.
     * @param text Texte à rendre.
     * @param x Position X.
     * @param y Position Y.
     */
    public void renderText(String text, float x, float y) {
        if (!isInitialized) {
            return;
        }

        // Sauvegarder l'état OpenGL
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Définir la couleur du texte
        GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);

        // Calculer la position de départ
        float startX = x;
        float startY = y;

        // Rendre chaque caractère du texte
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Dessiner un rectangle pour le caractère
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, startY);
            GL11.glVertex2f(startX + charWidth, startY);
            GL11.glVertex2f(startX + charWidth, startY + charHeight);
            GL11.glVertex2f(startX, startY + charHeight);
            GL11.glEnd();

            // Mettre à jour la position X
            startX += charWidth + charSpacing;
        }

        // Restaurer l'état OpenGL
        GL11.glPopAttrib();
    }

    /**
     * Calcule la largeur d'un texte.
     * @param text Texte.
     * @return Largeur du texte.
     */
    public float getTextWidth(String text) {
        return text.length() * (charWidth + charSpacing);
    }

    /**
     * Calcule la hauteur d'un texte.
     * @return Hauteur du texte.
     */
    public float getTextHeight() {
        return charHeight;
    }

    // --- Getters et Setters ---

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        this.charWidth = fontSize * 0.6f;
        this.charHeight = fontSize;
    }

    public float getCharSpacing() {
        return charSpacing;
    }

    public void setCharSpacing(float charSpacing) {
        this.charSpacing = charSpacing;
    }

    public Vector4f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector4f textColor) {
        this.textColor = textColor;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Nettoie les ressources du rendu de texte.
     * (Pas nécessaire pour cette version simplifiée)
     */
    public void cleanup() {
        isInitialized = false;
    }

    /**
     * Retourne une représentation textuelle du rendu de texte.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "FontRenderer{" +
                "fontSize=" + fontSize +
                ", charSpacing=" + charSpacing +
                ", isInitialized=" + isInitialized +
                '}';
    }

    public int getCharWidth(char c) {
        return 0;
    }

    public int getStringWidth(String text) {
        return Math.round(text.length() * (charWidth + charSpacing));
    }

    public void drawStringWithShadow(String renderText, int textX, int textY, int textColor) {
        drawString(renderText, textX + 1, textY + 1, 0x000000);
        drawString(renderText, textX, textY, textColor);
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        drawString(text, x - getStringWidth(text) / 2, y, color);
    }

    public void drawString(String text, int x, int y, int color) {
        Vector4f previous = this.textColor;
        this.textColor = new Vector4f((color >> 16 & 255) / 255.0F,
                (color >> 8 & 255) / 255.0F,
                (color & 255) / 255.0F,
                1.0F);
        renderText(text, x, y);
        this.textColor = previous;
    }
}