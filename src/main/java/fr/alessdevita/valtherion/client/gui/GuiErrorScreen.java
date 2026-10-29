package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Écran d'erreur dans Valtherion.
 * Affiche un message d'erreur et propose des options pour retourner au menu ou quitter le jeu.
 */
public class GuiErrorScreen extends Gui {

    // Titre de l'erreur
    private String errorTitle;

    // Message d'erreur
    private String errorMessage;

    // Bouton pour retourner au menu principal
    private GuiButton mainMenuButton;

    // Bouton pour quitter le jeu
    private GuiButton quitButton;

    /**
     * Constructeur de l'écran d'erreur.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param errorTitle Titre de l'erreur.
     * @param errorMessage Message d'erreur.
     */
    public GuiErrorScreen(Valtherion valtherion, int width, int height, String errorTitle, String errorMessage) {
        super(valtherion, width, height);
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les boutons de l'écran.
     */
    private void initButtons() {
        float buttonWidth = 200.0f;
        float buttonHeight = 40.0f;

        // Bouton pour retourner au menu principal
        mainMenuButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Retour au Menu"
        );
        mainMenuButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        mainMenuButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        mainMenuButton.setOnClick(button -> valtherion.openMainMenu());

        // Bouton pour quitter le jeu
        quitButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f - buttonHeight - 20.0f,
                buttonWidth,
                buttonHeight,
                "Quitter le Jeu"
        );
        quitButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        quitButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        quitButton.setOnClick(button -> valtherion.stop());

        // Ajouter les boutons à la GUI
        addElement(mainMenuButton);
        addElement(quitButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le titre de l'erreur
        drawErrorTitle();

        // Dessiner le message d'erreur
        drawErrorMessage();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'écran.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.2f, 0.0f, 0.0f, 0.8f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le titre de l'erreur.
     */
    protected void drawErrorTitle() {
        // Dessiner un rectangle pour simuler le titre
        float titleWidth = 200.0f;
        float titleHeight = 30.0f;
        float titleX = (width - titleWidth) / 2.0f;
        float titleY = height / 2.0f + 50.0f;

        GL11.glColor4f(1.0f, 0.5f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY + titleHeight);
        GL11.glVertex2f(titleX, titleY + titleHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le message d'erreur.
     */
    private void drawErrorMessage() {
        // Dessiner un rectangle pour simuler le message
        float messageWidth = 300.0f;
        float messageHeight = 20.0f;
        float messageX = (width - messageWidth) / 2.0f;
        float messageY = height / 2.0f + 20.0f;

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(messageX, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY + messageHeight);
        GL11.glVertex2f(messageX, messageY + messageHeight);
        GL11.glEnd();
    }

    // --- Getters et Setters ---

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Nettoie les ressources de l'écran.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Retourne une représentation textuelle de l'écran.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiErrorScreen{" +
                "errorTitle='" + errorTitle + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
