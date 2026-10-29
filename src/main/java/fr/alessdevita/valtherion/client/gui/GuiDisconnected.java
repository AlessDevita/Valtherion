package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Écran de déconnexion dans Valtherion.
 * Affiche un message de déconnexion et propose des options pour reconnecter ou retourner au menu.
 */
public class GuiDisconnected extends Gui {

    // Message de déconnexion
    private String disconnectMessage;

    // Bouton pour reconnecter
    private GuiButton reconnectButton;

    // Bouton pour retourner au menu principal
    private GuiButton mainMenuButton;

    /**
     * Constructeur de l'écran de déconnexion.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param disconnectMessage Message de déconnexion.
     */
    public GuiDisconnected(Valtherion valtherion, int width, int height, String disconnectMessage) {
        super(valtherion, width, height);
        this.disconnectMessage = disconnectMessage;

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

        // Bouton pour reconnecter
        reconnectButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Reconnecter"
        );
        reconnectButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        reconnectButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        reconnectButton.setOnClick(button -> valtherion.reconnect());

        // Bouton pour retourner au menu principal
        mainMenuButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f - buttonHeight - 20.0f,
                buttonWidth,
                buttonHeight,
                "Retour au Menu"
        );
        mainMenuButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        mainMenuButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        mainMenuButton.setOnClick(button -> valtherion.openMainMenu());

        // Ajouter les boutons à la GUI
        addElement(reconnectButton);
        addElement(mainMenuButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le message de déconnexion
        drawDisconnectMessage();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'écran.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le message de déconnexion.
     */
    private void drawDisconnectMessage() {
        // Dessiner un rectangle pour simuler le texte
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

    public String getDisconnectMessage() {
        return disconnectMessage;
    }

    public void setDisconnectMessage(String disconnectMessage) {
        this.disconnectMessage = disconnectMessage;
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
        return "GuiDisconnected{" +
                "disconnectMessage='" + disconnectMessage + '\'' +
                '}';
    }
}
