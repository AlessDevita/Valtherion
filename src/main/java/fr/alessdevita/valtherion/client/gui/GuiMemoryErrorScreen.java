package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Écran d'erreur de mémoire dans Valtherion.
 * Affiche un message d'erreur lié à la mémoire et propose des options pour libérer de la mémoire ou quitter.
 */
public class GuiMemoryErrorScreen extends Gui {

    // Message d'erreur de mémoire
    private String errorMessage;

    // Bouton pour libérer de la mémoire
    private GuiButton freeMemoryButton;

    // Bouton pour quitter le jeu
    private GuiButton quitButton;

    /**
     * Constructeur de l'écran d'erreur de mémoire.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param errorMessage Message d'erreur de mémoire.
     */
    public GuiMemoryErrorScreen(Valtherion valtherion, int width, int height, String errorMessage) {
        super(valtherion, width, height);
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

        // Bouton pour libérer de la mémoire
        freeMemoryButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Libérer de la Mémoire"
        );
        freeMemoryButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        freeMemoryButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        freeMemoryButton.setOnClick(button -> freeMemory());

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
        addElement(freeMemoryButton);
        addElement(quitButton);
    }

    /**
     * Libère de la mémoire.
     */
    private void freeMemory() {
        // Libérer les ressources inutilisées
        valtherion.freeUnusedResources();

        // Retourner au menu principal
        valtherion.openMainMenu();
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le message d'erreur
        drawErrorMessage();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'écran.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.3f, 0.0f, 0.0f, 0.8f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
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
        float messageY = height / 2.0f + 50.0f;

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(messageX, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY + messageHeight);
        GL11.glVertex2f(messageX, messageY + messageHeight);
        GL11.glEnd();
    }

    // --- Getters et Setters ---

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
        return "GuiMemoryErrorScreen{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
