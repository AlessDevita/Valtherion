package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
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
    
    // Texture du bouton (chemin vers le PNG)
    private String buttonTexturePath;
    private int buttonTextureId;
    private int buttonTextureWidth;
    private int buttonTextureHeight;
    private boolean textureTintEnabled;
    
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
        this.buttonTexturePath = "/assets/valtherion/textures/gui/sprites/widget/button.png";
        this.buttonTextureId = 0;
        this.buttonTextureWidth = 0;
        this.buttonTextureHeight = 0;
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
        this(buttonId, x, y, width, height, text, null);
    }

    public GuiButton(int buttonId, int x, int y, int width, int height, String text, String buttonTexturePath) {
        super();
        this.id = buttonId;
        this.position = new Vector2f(x, y);
        this.size = new Vector2f(width, height);
        this.text = text;
        this.normalColor = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
        this.hoverColor = new Vector4f(0.6f, 0.6f, 0.6f, 1.0f);
        this.clickColor = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.enabled = true;
        this.isHovered = false;
        this.isClicked = false;
        if (buttonTexturePath != null) {
            this.buttonTexturePath = buttonTexturePath;
        } else {
            this.buttonTexturePath = "/assets/valtherion/textures/gui/sprites/widget/button.png";
        }
        this.buttonTextureId = 0;
        this.buttonTextureWidth = 0;
        this.buttonTextureHeight = 0;
    }

    public GuiButton() {
        super();
        this.normalColor = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
        this.hoverColor = new Vector4f(0.6f, 0.6f, 0.6f, 1.0f);
        this.clickColor = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.enabled = true;
        this.isHovered = false;
        this.isClicked = false;
        this.buttonTexturePath = "/assets/valtherion/textures/gui/sprites/widget/button.png";
        this.buttonTextureId = 0;
        this.buttonTextureWidth = 0;
        this.buttonTextureHeight = 0;
    }

    public GuiButton(int i, int i1, int i2, String terminer) {
        super();
        this.id = i;
        this.position = new Vector2f(i1, i2);
        this.text = terminer;
        this.normalColor = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
        this.hoverColor = new Vector4f(0.6f, 0.6f, 0.6f, 1.0f);
        this.clickColor = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.enabled = true;
        this.isHovered = false;
        this.isClicked = false;
        this.buttonTexturePath = "/assets/valtherion/textures/gui/sprites/widget/button.png";
        this.buttonTextureId = 0;
        this.buttonTextureWidth = 0;
        this.buttonTextureHeight = 0;
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

    public void setTextureTintEnabled(boolean enabled) {
        this.textureTintEnabled = enabled;
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
        if (!this.enabled) {
            return;
        }
        // Déterminer si la souris est sur le bouton
        this.isHovered = mouseX >= (int)position.x && mouseX < (int)(position.x + size.x)
                && mouseY >= (int)position.y && mouseY < (int)(position.y + size.y);

        // Déterminer la couleur du bouton
        Vector4f buttonColor = normalColor;
        if (isClicked) {
            buttonColor = clickColor;
        } else if (isHovered) {
            buttonColor = hoverColor;
        }

        // Chargement paresseux de la texture si elle n'a pas encore été chargée
        if (this.buttonTextureId == 0 && this.buttonTexturePath != null) {
            this.buttonTextureId = loadButtonTexture(this.buttonTexturePath);
        }

        // Si une texture est disponible, l'utiliser
        if (this.buttonTextureId != 0 && this.buttonTextureWidth > 0 && this.buttonTextureHeight > 0) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.buttonTextureId);
            
            // Couleur de surimposition selon l'état du bouton
            if (this.textureTintEnabled) {
                GL11.glColor4f(buttonColor.x, buttonColor.y, buttonColor.z, buttonColor.w);
            } else if (isClicked) {
                GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
            } else if (isHovered) {
                GL11.glColor4f(1.2f, 1.2f, 1.2f, 1.0f);
            } else {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            
            // Recadrer la texture pour supprimer les marges transparentes
            // La texture originale fait 707x353, la zone utile est environ 695x253 centrée
            float uMargin = (707f - 695f) / 2f / 707f;  // marge horizontale en UV
            float vMarginTop = (353f - 253f) / 2f / 353f;  // marge haute en UV
            float vMarginBottom = 1f - vMarginTop - 253f / 353f;  // marge basse en UV
            
            float u0 = vMarginTop; // gauche
            float u1 = 1f - vMarginBottom; // droite
            float v0 = uMargin; // haut
            float v1 = 1f - uMargin; // bas
            
            // Dessiner la texture recadrée à la taille du bouton
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(v0, u0); GL11.glVertex2f(position.x, position.y);
            GL11.glTexCoord2f(v1, u0); GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glTexCoord2f(v1, u1); GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glTexCoord2f(v0, u1); GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();
        } else {
            // Fallback : bouton coloré sans texture
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            
            // Contour blanc (bordure extérieure)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            // Haut
            GL11.glVertex2f(position.x - 1, position.y - 1);
            GL11.glVertex2f(position.x + size.x + 1, position.y - 1);
            GL11.glVertex2f(position.x + size.x + 1, position.y);
            GL11.glVertex2f(position.x - 1, position.y);
            // Bas
            GL11.glVertex2f(position.x - 1, position.y + size.y);
            GL11.glVertex2f(position.x + size.x + 1, position.y + size.y);
            GL11.glVertex2f(position.x + size.x + 1, position.y + size.y + 1);
            GL11.glVertex2f(position.x - 1, position.y + size.y + 1);
            // Gauche
            GL11.glVertex2f(position.x - 1, position.y);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glVertex2f(position.x - 1, position.y + size.y);
            // Droite
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x + 1, position.y);
            GL11.glVertex2f(position.x + size.x + 1, position.y + size.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glEnd();

            // Remplissage du bouton
            GL11.glColor4f(buttonColor.x, buttonColor.y, buttonColor.z, buttonColor.w);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        // Dessiner le texte du bouton si présent avec une ombre pour la lisibilité
        if (text != null && !text.isEmpty() && mc.fontRenderer != null) {
            // Le FontRenderer utilise des textures pour les glyphes, donc garder la texture active
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            
            int textX = (int)(position.x + size.x / 2.0f);
            int textY = (int)(position.y + (size.y - 8) / 2.0f + 5);
            float textWidth = mc.fontRenderer.getTextWidth(text);
            float textHeight = mc.fontRenderer.getTextHeight();
            float availableWidth = Math.max(1f, size.x - 24f);
            float availableHeight = Math.max(1f, size.y - 12f);
            float textScale = Math.min(1f, Math.min(availableWidth / Math.max(1f, textWidth),
                    availableHeight / Math.max(1f, textHeight)));

            if (textScale < 0.999f) {
                GL11.glPushMatrix();
                GL11.glTranslatef(textX, textY + 4, 0f);
                GL11.glScalef(textScale, textScale, 1f);
                mc.fontRenderer.drawCenteredString(text, 1, -3, 0x000000);
                mc.fontRenderer.drawCenteredString(text, 0, -4, 0xFFFFFF);
                GL11.glPopMatrix();
            } else {
                mc.fontRenderer.drawCenteredString(text, textX + 1, textY + 1, 0x000000);
                mc.fontRenderer.drawCenteredString(text, textX, textY, 0xFFFFFF);
            }
        }
    }

    private int loadButtonTexture(String texturePath) {
        InputStream input = null;
        String resourcePath = texturePath.startsWith("/") ? texturePath.substring(1) : texturePath;
        
        // Essayer d'abord avec le ClassLoader
        input = GuiButton.class.getClassLoader().getResourceAsStream(resourcePath);
        
        // Si ça ne marche pas, essayer avec le chemin absolu
        if (input == null) {
            input = GuiButton.class.getResourceAsStream(texturePath);
        }
        
        // Dernier essai avec le ClassLoader système
        if (input == null) {
            input = ClassLoader.getSystemResourceAsStream(resourcePath);
        }
        
        try (InputStream inputStream = input) {
            if (inputStream == null) {
                System.err.println("Texture de bouton non trouvée : " + texturePath);
                return 0;
            }
            byte[] bytes = inputStream.readAllBytes();
            
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger la texture du bouton : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                this.buttonTextureWidth = width.get(0);
                this.buttonTextureHeight = height.get(0);
                
                try {
                    int textureId = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            this.buttonTextureWidth, this.buttonTextureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                    return textureId;
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (IOException | RuntimeException exception) {
            System.err.println("Impossible de charger la texture du bouton : " + exception.getMessage());
            return 0;
        }
    }

    public boolean mousePressed(Valtherion valtherion, int mouseX, int mouseY) {
        if (!this.enabled) {
            return false;
        }
        return mouseX >= (int)position.x && mouseX < (int)(position.x + size.x)
                && mouseY >= (int)position.y && mouseY < (int)(position.y + size.y);
    }

    public void playPressSound() {
        
    }

    public void mouseReleased(int i, int i1) {

    }
}
