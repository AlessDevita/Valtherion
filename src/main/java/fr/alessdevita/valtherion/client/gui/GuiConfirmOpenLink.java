package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Classe pour la fenêtre de confirmation d'ouverture de lien dans Valtherion.
 * Affiche un message de confirmation et propose des boutons "Oui" et "Non".
 */
public class GuiConfirmOpenLink extends Gui {

    // Lien à ouvrir
    private String link;

    // Message de confirmation
    private String message;

    // Bouton "Oui"
    private GuiButton yesButton;

    // Bouton "Non"
    private GuiButton noButton;

    // Couleur du fond de la fenêtre
    private Vector4f backgroundColor;

    // Rendu de texte pour la fenêtre
    private FontRenderer fontRenderer;

    // Indique si la fenêtre est ouverte
    private boolean isOpen;

    /**
     * Constructeur de la fenêtre de confirmation d'ouverture de lien.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param link Lien à ouvrir.
     */
    public GuiConfirmOpenLink(Valtherion valtherion, int width, int height, String link) {
        super(valtherion, width, height);
        this.link = link;
        this.message = "Voulez-vous ouvrir ce lien dans votre navigateur ?\n\n" + link;
        this.backgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.8f); // Noir semi-transparent
        this.isOpen = false;

        // Initialiser les boutons
        initButtons();
    }

    public GuiConfirmOpenLink(GuiMainMenu guiMainMenu, String warningURL, int height, boolean b) {
    }

    @Override
    public void init() {
        super.init();
        try {
            this.fontRenderer = new FontRenderer(16.0f);
            fontRenderer.init();
        } catch (Exception e) {
            System.err.println("Impossible d'initialiser le rendu de texte pour la confirmation de lien : " + e.getMessage());
        }
    }

    /**
     * Initialise les boutons de la fenêtre.
     */
    private void initButtons() {
        float buttonWidth = 100.0f;
        float buttonHeight = 30.0f;
        float buttonSpacing = 20.0f;
        float buttonY = (height - buttonHeight) / 2.0f - 30.0f;

        // Bouton "Oui"
        yesButton = new GuiButton(
                (width - buttonWidth * 2 - buttonSpacing) / 2.0f,
                buttonY,
                buttonWidth,
                buttonHeight,
                "Oui"
        );
        yesButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f)); // Vert
        yesButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        yesButton.setOnClick(button -> {
            openLink();
            close();
        });

        // Bouton "Non"
        noButton = new GuiButton(
                (width - buttonWidth * 2 - buttonSpacing) / 2.0f + buttonWidth + buttonSpacing,
                buttonY,
                buttonWidth,
                buttonHeight,
                "Non"
        );
        noButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f)); // Rouge
        noButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        noButton.setOnClick(button -> close());

        // Ajouter les boutons à la GUI
        addElement(yesButton);
        addElement(noButton);
    }

    @Override
    public void render() {
        if (!isVisible || !isOpen) {
            return;
        }

        // Dessiner le fond de la fenêtre
        drawBackground();

        // Dessiner le message
        drawMessage();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de la fenêtre.
     */
    protected void drawBackground() {
        float bgWidth = width / 2.0f;
        float bgHeight = height / 3.0f;
        float bgX = (width - bgWidth) / 2.0f;
        float bgY = (height - bgHeight) / 2.0f;

        GL11.glColor4f(
                backgroundColor.x,
                backgroundColor.y,
                backgroundColor.z,
                backgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(bgX, bgY);
        GL11.glVertex2f(bgX + bgWidth, bgY);
        GL11.glVertex2f(bgX + bgWidth, bgY + bgHeight);
        GL11.glVertex2f(bgX, bgY + bgHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le message de confirmation.
     */
    private void drawMessage() {
        if (fontRenderer == null) {
            return;
        }

        float messageX = (width - fontRenderer.getTextWidth(message)) / 2.0f;
        float messageY = (height + fontRenderer.getTextHeight()) / 2.0f - 30.0f;

        fontRenderer.setTextColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
        fontRenderer.renderText(message, messageX, messageY);
    }

    /**
     * Ouvre le lien dans le navigateur par défaut.
     */
    private void openLink() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(link));
            } else {
                System.out.println("Impossible d'ouvrir le lien : " + link);
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Erreur lors de l'ouverture du lien : " + e.getMessage());
        }
    }

    /**
     * Ouvre la fenêtre de confirmation.
     */
    public void open() {
        isOpen = true;
        isActive = true;
    }

    /**
     * Ferme la fenêtre de confirmation.
     */
    public void close() {
        isOpen = false;
        isActive = false;
    }

    // --- Getters et Setters ---

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        this.message = "Voulez-vous ouvrir ce lien dans votre navigateur ?\n\n" + link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    /**
     * Nettoie les ressources de la fenêtre de confirmation.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (fontRenderer != null) {
            fontRenderer.cleanup();
        }
    }

    /**
     * Retourne une représentation textuelle de la fenêtre de confirmation.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiConfirmOpenLink{" +
                "link='" + link + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
