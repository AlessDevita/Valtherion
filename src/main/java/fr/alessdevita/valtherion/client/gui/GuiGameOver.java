package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Écran de fin de jeu (Game Over) dans Valtherion.
 * Affiche un message de fin de jeu, le score, et propose des options pour recommencer ou retourner au menu.
 */
public class GuiGameOver extends Gui {

    // Message de fin de jeu
    private String gameOverMessage;

    // Score du joueur
    private int score;

    // Bouton pour recommencer
    private GuiButton restartButton;

    // Bouton pour retourner au menu principal
    private GuiButton mainMenuButton;

    /**
     * Constructeur de l'écran de fin de jeu.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param score Score du joueur.
     */
    public GuiGameOver(Valtherion valtherion, int width, int height, int score) {
        super(valtherion, width, height);
        this.gameOverMessage = "Game Over";
        this.score = score;

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

        // Bouton pour recommencer
        restartButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Recommencer"
        );
        restartButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        restartButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        restartButton.setOnClick(button -> valtherion.restartGame());

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
        addElement(restartButton);
        addElement(mainMenuButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le message de fin de jeu
        drawGameOverMessage();

        // Dessiner le score
        drawScore();

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
     * Dessine le message de fin de jeu.
     */
    private void drawGameOverMessage() {
        // Dessiner un rectangle pour simuler le message
        float messageWidth = 200.0f;
        float messageHeight = 40.0f;
        float messageX = (width - messageWidth) / 2.0f;
        float messageY = height / 2.0f + 50.0f;

        GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(messageX, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY + messageHeight);
        GL11.glVertex2f(messageX, messageY + messageHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le score.
     */
    private void drawScore() {
        // Dessiner un rectangle pour simuler le score
        float scoreWidth = 150.0f;
        float scoreHeight = 20.0f;
        float scoreX = (width - scoreWidth) / 2.0f;
        float scoreY = height / 2.0f + 20.0f;

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(scoreX, scoreY);
        GL11.glVertex2f(scoreX + scoreWidth, scoreY);
        GL11.glVertex2f(scoreX + scoreWidth, scoreY + scoreHeight);
        GL11.glVertex2f(scoreX, scoreY + scoreHeight);
        GL11.glEnd();
    }

    // --- Getters et Setters ---

    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public void setGameOverMessage(String gameOverMessage) {
        this.gameOverMessage = gameOverMessage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
        return "GuiGameOver{" +
                "gameOverMessage='" + gameOverMessage + '\'' +
                ", score=" + score +
                '}';
    }
}