package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

/**
 * Classe pour les boutons de la GUI dans Valtherion.
 * Étend GuiElement pour ajouter des fonctionnalités spécifiques aux boutons.
 */
public class GuiButton extends Gui.GuiElement {

    public int id;
    public boolean enabled;
    public Object displayString;
    // Texte du bouton
    private String text;
    
    // Couleur du bouton (normal)
    private Vector4f normalColor;
    
    // Couleur du bouton (survolé)
    private Vector4f hoverColor;
    
    // Couleur du bouton (cliqué)
    private Vector4f clickColor;
    
    // Couleur du texte
    private Vector4f textColor;
    
    // Indique si le bouton est survolé
    private boolean isHovered;
    
    // Indique si le bouton est cliqué
    private boolean isClicked;
    
    // Action à exécuter lors du clic
    private Consumer<GuiButton> onClickAction;
    
    // Rendu de texte pour le bouton
    private FontRenderer fontRenderer;
    
    // Taille du texte (pour le centrage)
    private float textWidth;
    private float textHeight;
    
    /**
     * Constructeur du bouton de la GUI.
     * @param x Position X.
     * @param y Position Y.
     * @param width Largeur.
     * @param height Hauteur.
     * @param text Texte du bouton.
     */
    public GuiButton(float x, float y, float width, float height, String text) {
        super(x, y, width, height);
        this.text = text;
        this.normalColor = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f); // Gris
        this.hoverColor = new Vector4f(0.6f, 0.6f, 0.6f, 1.0f); // Gris clair
        this.clickColor = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f); // Gris foncé
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc
        this.isHovered = false;
        this.isClicked = false;
        this.fontRenderer = null;
        this.textWidth = 0.0f;
        this.textHeight = 0.0f;
    }
    
    /**
     * Constructeur du bouton de la GUI avec un rendu de texte.
     * @param x Position X.
     * @param y Position Y.
     * @param width Largeur.
     * @param height Hauteur.
     * @param text Texte du bouton.
     * @param fontRenderer Rendu de texte pour le bouton.
     */
    public GuiButton(float x, float y, float width, float height, String text, FontRenderer fontRenderer) {
        this(x, y, width, height, text);
        this.fontRenderer = fontRenderer;
        if (fontRenderer != null) {
            this.textWidth = fontRenderer.getTextWidth(text);
            this.textHeight = fontRenderer.getTextHeight();
        }
    }

    public GuiButton(int buttonId, int x, int y, int width, int height, String text) {
        super();
        this.id = buttonId;
    }

    public GuiButton() {
        super();
    }

    public GuiButton(int i, int i1, int i2, String terminer) {
    }

    @Override
    public void init() {
        super.init();
        if (fontRenderer != null && !fontRenderer.isInitialized()) {
            try {
                fontRenderer.init();
            } catch (Exception e) {
                System.err.println("Impossible d'initialiser le rendu de texte pour le bouton : " + e.getMessage());
            }
        }
    }
    
    @Override
    public void render() {
        if (!isVisible) {
            return;
        }
        
        // Déterminer la couleur du bouton
        Vector4f buttonColor = normalColor;
        if (isClicked) {
            buttonColor = clickColor;
        } else if (isHovered) {
            buttonColor = hoverColor;
        }
        
        // Dessiner le bouton
        GL11.glColor4f(buttonColor.x, buttonColor.y, buttonColor.z, buttonColor.w);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(position.x, position.y);
        GL11.glVertex2f(position.x + size.x, position.y);
        GL11.glVertex2f(position.x + size.x, position.y + size.y);
        GL11.glVertex2f(position.x, position.y + size.y);
        GL11.glEnd();
        
        // Dessiner le texte du bouton
        if (fontRenderer != null) {
            fontRenderer.setTextColor(textColor);
            float textX = position.x + (size.x - textWidth) / 2.0f;
            float textY = position.y + (size.y - textHeight) / 2.0f;
            fontRenderer.renderText(text, textX, textY);
        }
    }
    
    @Override
    public void onClick(int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && onClickAction != null) {
            onClickAction.accept(this);
        }
    }
    
    @Override
    public boolean contains(float x, float y) {
        return super.contains(x, y);
    }
    
    /**
     * Définit l'action à exécuter lors du clic.
     * @param onClickAction Action à exécuter.
     */
    public void setOnClick(Consumer<GuiButton> onClickAction) {
        this.onClickAction = onClickAction;
    }
    
    /**
     * Définit l'état du bouton (survolé ou non).
     * @param hovered true si le bouton est survolé.
     */
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }
    
    /**
     * Définit l'état du bouton (cliqué ou non).
     * @param clicked true si le bouton est cliqué.
     */
    public void setClicked(boolean clicked) {
        this.isClicked = clicked;
    }
    
    // --- Getters et Setters ---
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        if (fontRenderer != null) {
            this.textWidth = fontRenderer.getTextWidth(text);
        }
    }
    
    public Vector4f getNormalColor() {
        return normalColor;
    }
    
    public void setNormalColor(Vector4f normalColor) {
        this.normalColor = normalColor;
    }
    
    public Vector4f getHoverColor() {
        return hoverColor;
    }
    
    public void setHoverColor(Vector4f hoverColor) {
        this.hoverColor = hoverColor;
    }
    
    public Vector4f getClickColor() {
        return clickColor;
    }
    
    public void setClickColor(Vector4f clickColor) {
        this.clickColor = clickColor;
    }
    
    public Vector4f getTextColor() {
        return textColor;
    }
    
    public void setTextColor(Vector4f textColor) {
        this.textColor = textColor;
    }
    
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }
    
    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        if (fontRenderer != null) {
            this.textWidth = fontRenderer.getTextWidth(text);
            this.textHeight = fontRenderer.getTextHeight();
        }
    }
    
    public boolean isHovered() {
        return isHovered;
    }
    
    public boolean isClicked() {
        return isClicked;
    }
    
    /**
     * Retourne une représentation textuelle du bouton.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiButton{" + 
               "text='" + text + '\'' + 
               ", position=" + position + 
               ", size=" + size + 
               '}';
    }

    public void drawButton(Valtherion mc, int mouseX, int mouseY) {
    }

    public boolean mousePressed(Valtherion valtherion, int mouseX, int mouseY) {
        return false;
    }

    public void playPressSound() {
        
    }

    public void mouseReleased(int i, int i1) {

    }
}
