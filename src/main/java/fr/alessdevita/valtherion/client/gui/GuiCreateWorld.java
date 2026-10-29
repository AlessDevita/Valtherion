package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.world.WorldType;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface pour créer un nouveau monde dans Valtherion.
 */
public class GuiCreateWorld extends Gui {
    private final GuiSelectWorld parentScreen;

    // Nom du monde
    private String worldName;

    // Type de monde (NORMAL, FLAT, CUSTOM)
    private WorldType worldType;

    // Graine du monde
    private String worldSeed;

    // Mode de jeu (SURVIVAL, CREATIVE, ADVENTURE, HARDCORE)
    private GameMode gameMode;

    // Bouton pour créer le monde
    private GuiButton createButton;

    // Bouton pour annuler
    private GuiButton cancelButton;

    // Bouton pour le type de monde normal
    private GuiButton normalWorldButton;

    // Bouton pour le type de monde plat
    private GuiButton flatWorldButton;

    // Bouton pour le type de monde personnalisé
    private GuiButton customWorldButton;

    // Bouton pour le mode survie
    private GuiButton survivalModeButton;

    // Bouton pour le mode créatif
    private GuiButton creativeModeButton;

    // Champ de texte pour le nom du monde
    private GuiTextField worldNameField;

    // Champ de texte pour la graine
    private GuiTextField seedField;

    public GuiCreateWorld(GuiSelectWorld guiSelectWorld) {
        super(guiSelectWorld.valtherion, guiSelectWorld.width, guiSelectWorld.height);
        this.parentScreen = guiSelectWorld;
        this.worldName = "Nouveau Monde";
        this.worldType = WorldType.NORMAL;
        this.worldSeed = "";
        this.gameMode = GameMode.SURVIVAL;
        initButtons();
    }

    // Énumération des modes de jeu
    public enum GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        HARDCORE
    }

    /**
     * Constructeur de l'interface de création de monde.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiCreateWorld(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.parentScreen = null;
        this.worldName = "Nouveau Monde";
        this.worldType = WorldType.NORMAL;
        this.worldSeed = "";
        this.gameMode = GameMode.SURVIVAL;

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();

        // Initialiser les champs de texte
        worldNameField = new GuiTextField(100, 100, 200, 20, worldName);
        seedField = new GuiTextField(100, 150, 200, 20, worldSeed);

        addElement(worldNameField);
        addElement(seedField);
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour créer le monde
        createButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Créer"
        );
        createButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        createButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        createButton.setOnClick(button -> createWorld());

        // Bouton pour annuler
        cancelButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Annuler"
        );
        cancelButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        cancelButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        cancelButton.setOnClick(button -> valtherion.displayGuiScreen(parentScreen));

        // Bouton pour le type de monde normal
        normalWorldButton = new GuiButton(
                50.0f,
                height / 2.0f - 50.0f,
                buttonWidth,
                buttonHeight,
                "Monde Normal"
        );
        normalWorldButton.setNormalColor(new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
        normalWorldButton.setHoverColor(new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        normalWorldButton.setOnClick(button -> setWorldType(WorldType.NORMAL));

        // Bouton pour le type de monde plat
        flatWorldButton = new GuiButton(
                50.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Monde Plat"
        );
        flatWorldButton.setNormalColor(new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
        flatWorldButton.setHoverColor(new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        flatWorldButton.setOnClick(button -> setWorldType(WorldType.FLAT));

        // Bouton pour le type de monde personnalisé
        customWorldButton = new GuiButton(
                50.0f,
                height / 2.0f + 50.0f,
                buttonWidth,
                buttonHeight,
                "Monde Personnalisé"
        );
        customWorldButton.setNormalColor(new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
        customWorldButton.setHoverColor(new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        customWorldButton.setOnClick(button -> setWorldType(WorldType.CUSTOM));

        // Bouton pour le mode survie
        survivalModeButton = new GuiButton(
                width - buttonWidth - 50.0f,
                height / 2.0f - 50.0f,
                buttonWidth,
                buttonHeight,
                "Survie"
        );
        survivalModeButton.setNormalColor(new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
        survivalModeButton.setHoverColor(new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        survivalModeButton.setOnClick(button -> setGameMode(GameMode.SURVIVAL));

        // Bouton pour le mode créatif
        creativeModeButton = new GuiButton(
                width - buttonWidth - 50.0f,
                height / 2.0f,
                buttonWidth,
                buttonHeight,
                "Créatif"
        );
        creativeModeButton.setNormalColor(new Vector4f(0.4f, 0.4f, 0.4f, 1.0f));
        creativeModeButton.setHoverColor(new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        creativeModeButton.setOnClick(button -> setGameMode(GameMode.CREATIVE));

        // Ajouter les boutons à la GUI
        addElement(createButton);
        addElement(cancelButton);
        addElement(normalWorldButton);
        addElement(flatWorldButton);
        addElement(customWorldButton);
        addElement(survivalModeButton);
        addElement(creativeModeButton);
    }

    /**
     * Définit le type de monde.
     * @param worldType Type de monde.
     */
    private void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }

    /**
     * Définit le mode de jeu.
     * @param gameMode Mode de jeu.
     */
    private void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Crée le monde.
     */
    private void createWorld() {
        // Récupérer le nom du monde depuis le champ de texte
        worldName = worldNameField.getText();

        // Récupérer la graine depuis le champ de texte
        worldSeed = seedField.getText();

        // Créer le monde
        valtherion.createWorld(worldName, worldType, worldSeed, gameMode);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner les boutons et les champs de texte
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
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
     * Dessine le titre de l'interface.
     */
    protected void drawTitle() {
        String title = "Créer un Nouveau Monde";
        float titleX = (width - 200.0f) / 2.0f;
        float titleY = height - 30.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + 200.0f, titleY);
        GL11.glVertex2f(titleX + 200.0f, titleY + 20.0f);
        GL11.glVertex2f(titleX, titleY + 20.0f);
        GL11.glEnd();
    }

    // --- Getters et Setters ---

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public String getWorldSeed() {
        return worldSeed;
    }

    public void setWorldSeed(String worldSeed) {
        this.worldSeed = worldSeed;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiCreateWorld{" +
                "worldName='" + worldName + '\'' +
                ", worldType=" + worldType +
                ", gameMode=" + gameMode +
                '}';
    }

    // --- Classe interne pour les champs de texte ---

    /**
     * Classe pour les champs de texte de la GUI.
     */
    public class GuiTextField extends GuiElement {

        // Texte du champ
        private String text;

        // Indique si le champ est sélectionné
        private boolean isSelected;

        // Couleur du texte
        private Vector4f textColor;

        // Couleur de fond
        private Vector4f backgroundColor;

        /**
         * Constructeur du champ de texte.
         * @param x Position X.
         * @param y Position Y.
         * @param width Largeur.
         * @param height Hauteur.
         * @param text Texte initial.
         */
        public GuiTextField(float x, float y, float width, float height, String text) {
            super(x, y, width, height);
            this.text = text;
            this.isSelected = false;
            this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.backgroundColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.8f);
        }

        @Override
        public void render() {
            // Dessiner le fond du champ
            GL11.glColor4f(
                    backgroundColor.x,
                    backgroundColor.y,
                    backgroundColor.z,
                    backgroundColor.w
            );
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();

            // Dessiner le texte (simplifié)
            GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x + 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 15.0f);
            GL11.glVertex2f(position.x + 5.0f, position.y + 15.0f);
            GL11.glEnd();
        }

        // --- Getters et Setters ---

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }
    }
}
