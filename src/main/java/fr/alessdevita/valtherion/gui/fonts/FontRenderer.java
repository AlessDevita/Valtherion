package fr.alessdevita.valtherion.gui.fonts;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import fr.alessdevita.valtherion.client.settings.GameSettings;
import fr.alessdevita.valtherion.rendering.Shader;
import fr.alessdevita.valtherion.rendering.Texture;

import fr.alessdevita.valtherion.utils.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.*;

/**
 * Classe pour le rendu des textes dans Valtherion.
 * Permet d'afficher du texte avec différentes polices, tailles, couleurs, et effets.
 */
public class FontRenderer {

    // Shader utilisé pour le rendu du texte
    private Shader textShader;

    // Map des textures de caractères (police -> texture)
    private final Map<String, Texture> fontTextures;

    // Map des caractères (police -> map de caractères)
    private final Map<String, Font> fonts;

    // Matrice de projection pour le rendu 2D
    private Matrix4f projectionMatrix;

    // Couleur par défaut du texte
    private Vector3f defaultColor;

    public FontRenderer(GameSettings gameSettings, ResourceLocation resourceLocation, TextureManager renderEngine, boolean b, Map<String, Texture> fontTextures, Map<String, Font> fonts) {
        this.fontTextures = fontTextures;
        this.fonts = fonts;
    }

    // Classe pour représenter une police
    public static class Font {
        private final String name;
        private final Texture texture;
        private final float[] charWidths;
        private final float charHeight;
        private final float spaceWidth;

        public Font(String name, Texture texture, float[] charWidths, float charHeight, float spaceWidth) {
            this.name = name;
            this.texture = texture;
            this.charWidths = charWidths;
            this.charHeight = charHeight;
            this.spaceWidth = spaceWidth;
        }

        public String getName() {
            return name;
        }

        public Texture getTexture() {
            return texture;
        }

        public float[] getCharWidths() {
            return charWidths;
        }

        public float getCharWidth(char c) {
            int index = c - 32; // Supposons que les caractères commencent à l'espace (ASCII 32)
            if (index >= 0 && index < charWidths.length) {
                return charWidths[index];
            }
            return spaceWidth; // Retourner la largeur d'un espace si le caractère n'est pas trouvé
        }

        public float getCharHeight() {
            return charHeight;
        }

        public float getSpaceWidth() {
            return spaceWidth;
        }
    }

    // Classe pour représenter un caractère
    private static class Character {
        private final float x; // Coordonnée X de la texture
        private final float y; // Coordonnée Y de la texture
        private final float width; // Largeur du caractère
        private final float height; // Hauteur du caractère
        private final float xOffset; // Décalage X
        private final float yOffset; // Décalage Y
        private final float xAdvance; // Avancée X

        public Character(float x, float y, float width, float height, float xOffset, float yOffset, float xAdvance) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public float getXOffset() {
            return xOffset;
        }

        public float getYOffset() {
            return yOffset;
        }

        public float getXAdvance() {
            return xAdvance;
        }
    }

    /**
     * Constructeur du rendu des textes.
     */
    public FontRenderer() throws IOException {
        this.fontTextures = new HashMap<>();
        this.fonts = new HashMap<>();
        this.projectionMatrix = new Matrix4f();
        this.defaultColor = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc

        // Initialiser le shader pour le texte
        this.textShader = new Shader("text_vertex.glsl", "text_fragment.glsl");
    }

    /**
     * Initialise le rendu des textes.
     */
    public void init() throws IOException {
        // Charger les polices par défaut
        loadDefaultFonts();

        // Configurer la matrice de projection pour le rendu 2D
        projectionMatrix = new Matrix4f().ortho(0, 1280, 0, 720, -1, 1); // Exemple : Résolution 1280x720
    }

    /**
     * Charge les polices par défaut.
     */
    private void loadDefaultFonts() throws IOException {
        // Exemple : Charger une police par défaut
        // À implémenter selon ta logique de chargement des textures de police
        Texture defaultFontTexture = new Texture("default_font.png");
        float[] charWidths = new float[96]; // 96 caractères imprimables (ASCII 32 à 127)
        Arrays.fill(charWidths, 16.0f); // Largeur par défaut de 16 pixels
        Font defaultFont = new Font("DEFAULT", defaultFontTexture, charWidths, 32.0f, 16.0f);
        fonts.put("DEFAULT", defaultFont);
        fontTextures.put("DEFAULT", defaultFontTexture);
    }

    /**
     * Charge une police.
     * @param name Nom de la police.
     * @param texture Texture de la police.
     * @param charWidths Largeurs des caractères.
     * @param charHeight Hauteur des caractères.
     * @param spaceWidth Largeur de l'espace.
     */
    public void loadFont(String name, Texture texture, float[] charWidths, float charHeight, float spaceWidth) {
        Font font = new Font(name, texture, charWidths, charHeight, spaceWidth);
        fonts.put(name, font);
        fontTextures.put(name, texture);
    }

    /**
     * Rend du texte.
     * @param text Texte à rendre.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param scale Échelle du texte.
     * @param color Couleur du texte.
     */
    public void renderText(String text, float x, float y, float scale, Vector3f color) {
        if (text == null || text.isEmpty()) {
            return;
        }

        // Utiliser la police par défaut
        Font font = fonts.get("DEFAULT");
        if (font == null) {
            return;
        }

        // Activer le shader
        textShader.bind();

        // Configurer la matrice de projection
        textShader.setMatrix4("projection", projectionMatrix);

        // Configurer la couleur
        textShader.setVector3f("textColor", color);

        // Activer la texture de la police
        font.getTexture().bind();

        // Calculer la position initiale
        float currentX = x;
        float currentY = y;

        // Rendre chaque caractère
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Récupérer les informations du caractère
            float charWidth = font.getCharWidth(c);
            float charHeight = font.getCharHeight();

            // Calculer les coordonnées de la texture
            float texX = 0; // À calculer en fonction du caractère
            float texY = 0; // À calculer en fonction du caractère
            float texWidth = charWidth / font.getTexture().getWidth();
            float texHeight = charHeight / font.getTexture().getHeight();

            // Rendre le caractère
            renderCharacter(c, currentX, currentY, charWidth, charHeight, scale, texX, texY, texWidth, texHeight);

            // Mettre à jour la position pour le caractère suivant
            currentX += charWidth * scale;
        }

        // Désactiver la texture et le shader
        font.getTexture().unbind();
        textShader.unbind();
    }

    /**
     * Rend un caractère.
     * @param c Caractère à rendre.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur du caractère.
     * @param height Hauteur du caractère.
     * @param scale Échelle du caractère.
     * @param texX Coordonnée X de la texture.
     * @param texY Coordonnée Y de la texture.
     * @param texWidth Largeur de la texture.
     * @param texHeight Hauteur de la texture.
     */
    private void renderCharacter(char c, float x, float y, float width, float height, float scale,
                                 float texX, float texY, float texWidth, float texHeight) {
        // Calculer les coordonnées des sommets
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float[] vertices = {
                x, y + scaledHeight, 0, texX, texY + texHeight, // Haut-gauche
                x + scaledWidth, y, 0, texX + texWidth, texY, // Bas-droite
                x, y, 0, texX, texY, // Bas-gauche
                x, y + scaledHeight, 0, texX, texY + texHeight, // Haut-gauche
                x + scaledWidth, y + scaledHeight, 0, texX + texWidth, texY + texHeight, // Haut-droite
                x + scaledWidth, y, 0, texX + texWidth, texY // Bas-droite
        };

        // Logique pour rendre le caractère avec OpenGL
        // Exemple : Utiliser un VBO et un VAO pour rendre les sommets
        System.out.println("Rendu du caractère : " + c + " à (" + x + ", " + y + ")");
    }

    /**
     * Rend du texte avec une police spécifique.
     * @param text Texte à rendre.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param scale Échelle du texte.
     * @param color Couleur du texte.
     * @param fontName Nom de la police.
     */
    public void renderText(String text, float x, float y, float scale, Vector3f color, String fontName) {
        Font font = fonts.get(fontName);
        if (font == null) {
            font = fonts.get("DEFAULT");
        }

        if (font != null) {
            renderText(text, x, y, scale, color);
        }
    }

    /**
     * Rend du texte avec une ombre.
     * @param text Texte à rendre.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param scale Échelle du texte.
     * @param color Couleur du texte.
     * @param shadowColor Couleur de l'ombre.
     * @param shadowOffset Décalage de l'ombre.
     */
    public void renderTextWithShadow(String text, float x, float y, float scale, Vector3f color,
                                     Vector3f shadowColor, Vector2f shadowOffset) {
        // Rendre l'ombre
        renderText(text, x + shadowOffset.x, y + shadowOffset.y, scale, shadowColor);

        // Rendre le texte
        renderText(text, x, y, scale, color);
    }

    /**
     * Rend du texte avec un contour.
     * @param text Texte à rendre.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param scale Échelle du texte.
     * @param color Couleur du texte.
     * @param outlineColor Couleur du contour.
     * @param outlineThickness Épaisseur du contour.
     */
    public void renderTextWithOutline(String text, float x, float y, float scale, Vector3f color,
                                      Vector3f outlineColor, float outlineThickness) {
        // Rendre le contour (plusieurs fois pour simuler l'épaisseur)
        for (float dx = -outlineThickness; dx <= outlineThickness; dx += outlineThickness) {
            for (float dy = -outlineThickness; dy <= outlineThickness; dy += outlineThickness) {
                if (dx != 0 || dy != 0) {
                    renderText(text, x + dx, y + dy, scale, outlineColor);
                }
            }
        }

        // Rendre le texte
        renderText(text, x, y, scale, color);
    }

    /**
     * Définit la matrice de projection.
     * @param projectionMatrix Matrice de projection.
     */
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    /**
     * Définit la couleur par défaut du texte.
     * @param defaultColor Couleur par défaut.
     */
    public void setDefaultColor(Vector3f defaultColor) {
        this.defaultColor = defaultColor;
    }

    /**
     * Nettoie les ressources du rendu des textes.
     */
    public void cleanup() {
        // Nettoyer les textures de police
        for (Texture texture : fontTextures.values()) {
            texture.cleanup();
        }

        // Nettoyer le shader
        textShader.cleanup();
    }
}