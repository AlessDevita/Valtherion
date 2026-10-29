package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Menu en jeu (accessible via ESC) dans Valtherion.
 * Permet de reprendre le jeu, accéder aux options, sauvegarder, ou quitter.
 */
public class GuiIngameMenu extends Gui {

    // Bouton pour reprendre le jeu
    private GuiButton resumeButton;

    // Bouton pour accéder aux options
    private GuiButton optionsButton;

    // Bouton pour sauvegarder et quitter
    private GuiButton saveAndQuitButton;

    // Bouton pour quitter le jeu
    private GuiButton quitButton;

    /**
     * Constructeur du menu en jeu.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiIngameMenu(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les boutons du menu.
     */
    private void initButtons() {
        float buttonWidth = 200.0f;
        float buttonHeight = 40.0f;
        float centerX = width / 2.0f - buttonWidth / 2.0f;

        // Bouton pour reprendre le jeu
        resumeButton = new GuiButton(
                centerX,
                height / 2.0f + 20.0f,
                buttonWidth,
                buttonHeight,
                "Rependre"
        );
        resumeButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        resumeButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        resumeButton.setOnClick(button -> valtherion.closeIngameMenu());

        // Bouton pour accéder aux options
        optionsButton = new GuiButton(
                centerX,
                height / 2.0f - 20.0f,
                buttonWidth,
                buttonHeight,
                "Options"
        );
        optionsButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.8f, 1.0f));
        optionsButton.setHoverColor(new Vector4f(0.4f, 0.8f, 1.0f, 1.0f));
        optionsButton.setOnClick(button -> valtherion.openOptionsMenu());

        // Bouton pour sauvegarder et quitter
        saveAndQuitButton = new GuiButton(
                centerX,
                height / 2.0f - 60.0f,
                buttonWidth,
                buttonHeight,
                "Sauvegarder et Quitter"
        );
        saveAndQuitButton.setNormalColor(new Vector4f(0.8f, 0.6f, 0.2f, 1.0f));
        saveAndQuitButton.setHoverColor(new Vector4f(1.0f, 0.8f, 0.4f, 1.0f));
        saveAndQuitButton.setOnClick(button -> {
            valtherion.saveGame();
            valtherion.openMainMenu();
        });

        // Bouton pour quitter le jeu
        quitButton = new GuiButton(
                centerX,
                height / 2.0f - 100.0f,
                buttonWidth,
                buttonHeight,
                "Quitter le Jeu"
        );
        quitButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        quitButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        quitButton.setOnClick(button -> valtherion.stop());

        // Ajouter les boutons à la GUI
        addElement(resumeButton);
        addElement(optionsButton);
        addElement(saveAndQuitButton);
        addElement(quitButton);
    }

    @Override
    public void render() {
        // Dessiner le fond semi-transparent
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond du menu.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le titre du menu.
     */
    private void drawTitle() {
        // Dessiner un rectangle pour simuler le titre "Menu"
        float titleWidth = 100.0f;
        float titleHeight = 20.0f;
        float titleX = (width - titleWidth) / 2.0f;
        float titleY = height / 2.0f + 80.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY + titleHeight);
        GL11.glVertex2f(titleX, titleY + titleHeight);
        GL11.glEnd();
    }

    // --- Getters et Setters ---

    public GuiButton getResumeButton() {
        return resumeButton;
    }

    public GuiButton getOptionsButton() {
        return optionsButton;
    }

    public GuiButton getSaveAndQuitButton() {
        return saveAndQuitButton;
    }

    public GuiButton getQuitButton() {
        return quitButton;
    }

    /**
     * Nettoie les ressources du menu.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Retourne une représentation textuelle du menu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiIngameMenu{" +
                "resumeButton=" + resumeButton +
                ", optionsButton=" + optionsButton +
                '}';
    }
}