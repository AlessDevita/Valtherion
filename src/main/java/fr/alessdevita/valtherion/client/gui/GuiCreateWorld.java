package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.glfw.GLFW;

import fr.alessdevita.valtherion.world.WorldType;

/**
 * GuiCreateWorld — Ecran de creation de monde au style Minecraft vanilla :
 * fond sombre, titre "Creer un monde", champ de nom, boutons pour le mode
 * de jeu et la difficulte, options avancees, et boutons en bas.
 */
public class GuiCreateWorld extends GuiScreen {

    private final GuiScreen parentScreen;

    // Mode de jeu selectionne
    private GameMode gameMode;

    // Difficulte selectionnee
    private Difficulty difficulty;

    // Type de monde selectionne
    private WorldType worldType;

    // Options avancees deployees
    private boolean advancedOptionsExpanded;

    // Champ de texte pour le nom du monde
    private GuiTextField worldNameField;

    // Champ de texte pour la graine
    private GuiTextField seedField;

    // Boutons principaux
    private GuiButton createButton;
    private GuiButton cancelButton;
    private GuiButton advancedOptionsButton;

    // Boutons de mode de jeu
    private GuiButton survivalButton;
    private GuiButton creativeButton;
    private GuiButton adventureButton;

    // Boutons de difficulte
    private GuiButton easyButton;
    private GuiButton normalButton;
    private GuiButton hardButton;
    private GuiButton extremeButton;

    // Enumeration des modes de jeu
    public enum GameMode {
        SURVIVAL("Survie"),
        CREATIVE("Creatif"),
        ADVENTURE("Aventure"),
        HARDCORE("Hardcore");

        private final String displayName;

        GameMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Enumeration des difficultes
    public enum Difficulty {
        EASY("Facile"),
        NORMAL("Normale"),
        HARD("Difficile"),
        EXTREME("Extrême");

        private final String displayName;

        Difficulty(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public GuiCreateWorld(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.worldType = WorldType.NORMAL;
        this.gameMode = GameMode.SURVIVAL;
        this.difficulty = Difficulty.NORMAL;
        this.advancedOptionsExpanded = false;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int centerX = this.width / 2;
        int btnW = 200;
        int btnH = 20;

        // Champ de nom du monde
        this.worldNameField = new GuiTextField(this.valtherion.fontRenderer,
                centerX - 150, 50, 300, 20);
        this.worldNameField.setEnableBackgroundDrawing(true);
        this.worldNameField.setText("");
        this.worldNameField.setMaxStringLength(128);

        // Champ de graine (options avancees)
        this.seedField = new GuiTextField(this.valtherion.fontRenderer,
                centerX - 150, 0, 200, 20);
        this.seedField.setEnableBackgroundDrawing(true);
        this.seedField.setText("");
        this.seedField.setMaxStringLength(15);
        this.seedField.setVisible(false);

        // Mode de jeu — bouton principal
        this.buttonList.add(this.survivalButton = new GuiButton(
                10, centerX - 100, 80, 200, 20,
                "Mode de jeu : " + this.gameMode.getDisplayName()));

        // Difficulte
        this.buttonList.add(this.easyButton = new GuiButton(
                20, centerX - 100, 110, 95, 20, "Facile"));
        this.buttonList.add(this.normalButton = new GuiButton(
                21, centerX, 110, 95, 20, "Normale"));
        this.buttonList.add(this.hardButton = new GuiButton(
                22, centerX - 100, 135, 95, 20, "Difficile"));
        this.buttonList.add(this.extremeButton = new GuiButton(
                23, centerX, 135, 95, 20, "Extrême"));

        // Options avancees
        this.buttonList.add(this.advancedOptionsButton = new GuiButton(
                40, centerX - 100, 165, 200, 20,
                "Options avancees ..."));

        // Boutons en bas
        this.buttonList.add(this.createButton = new GuiButton(
                1, centerX - 100, this.height - 30, 200, 20, "Creer le monde"));
        this.buttonList.add(this.cancelButton = new GuiButton(
                2, centerX - 100, this.height - 58, 200, 20, "Annuler"));

        // Mettre a jour l'apparence des boutons
        updateDifficultyButtons();
    }

    private void updateDifficultyButtons() {
        this.easyButton.enabled = this.difficulty != Difficulty.EASY;
        this.normalButton.enabled = this.difficulty != Difficulty.NORMAL;
        this.hardButton.enabled = this.difficulty != Difficulty.HARD;
        this.extremeButton.enabled = this.difficulty != Difficulty.EXTREME;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            // Creer le monde
            String worldName = this.worldNameField.getText().trim();
            String seed = this.seedField.getText().trim();
            if (worldName.isEmpty()) {
                worldName = "Monde 1";
            }
            this.valtherion.createWorld(worldName, this.worldType, seed, this.gameMode);
        }
        else if (button.id == 2) {
            // Annuler
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 10) {
            // Changer le mode de jeu (cycle)
            GameMode[] modes = GameMode.values();
            int nextIndex = (this.gameMode.ordinal() + 1) % modes.length;
            this.gameMode = modes[nextIndex];
            this.survivalButton.setText("Mode de jeu : " + this.gameMode.getDisplayName());
        }
        else if (button.id == 20) {
            this.difficulty = Difficulty.EASY;
            updateDifficultyButtons();
        }
        else if (button.id == 21) {
            this.difficulty = Difficulty.NORMAL;
            updateDifficultyButtons();
        }
        else if (button.id == 22) {
            this.difficulty = Difficulty.HARD;
            updateDifficultyButtons();
        }
        else if (button.id == 23) {
            this.difficulty = Difficulty.EXTREME;
            updateDifficultyButtons();
        }
        else if (button.id == 40) {
            // Basculer les options avancees
            this.advancedOptionsExpanded = !this.advancedOptionsExpanded;
            this.seedField.setVisible(this.advancedOptionsExpanded);
            if (this.advancedOptionsExpanded) {
                this.seedField.setPosition(this.width / 2 + 5, 170, 200, 20);
            }
            this.advancedOptionsButton.setText(
                    this.advancedOptionsExpanded ? "Options avancees v" : "Options avancees ...");
        }
    }

    @Override
    public void handleMouseInput(int button, int action) {
        if (action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            double[] xPos = new double[1];
            double[] yPos = new double[1];
            GLFW.glfwGetCursorPos(this.valtherion.getWindowHandle(), xPos, yPos);
            int mouseX = (int) (xPos[0] * this.width / Math.max(1, this.valtherion.displayWidth));
            int mouseY = (int) (yPos[0] * this.height / Math.max(1, this.valtherion.displayHeight));
            this.worldNameField.mouseClicked(mouseX, mouseY, button);
            if (this.advancedOptionsExpanded) {
                this.seedField.mouseClicked(mouseX, mouseY, button);
            }
        }
        super.handleMouseInput(button, action);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Fond degrade style Minecraft
        this.drawGradientRect(0, 0, this.width, this.height, -1060545408, -1185516288);

        // Titre
        this.valtherion.fontRenderer.drawCenteredString(
                "Creer un monde",
                this.width / 2, 10, 0xFFFFFF);

        // Label "Nom du monde"
        this.valtherion.fontRenderer.drawString(
                "Nom du monde",
                this.width / 2 - 150, 42, 0xFFFFFF);

        // Champ de nom
        this.worldNameField.drawTextBox();

        // Label "Mode de jeu"
        this.valtherion.fontRenderer.drawString(
                "Mode de jeu",
                this.width / 2 - 100, 72, 0xAAAAAA);

        // Label "Difficulte"
        this.valtherion.fontRenderer.drawString(
                "Difficulte",
                this.width / 2 - 100, 102, 0xAAAAAA);

        // Options avancees
        if (this.advancedOptionsExpanded) {
            // Label "Graine du monde"
            this.valtherion.fontRenderer.drawString(
                    "Graine du monde",
                    this.width / 2 - 100, 172, 0xAAAAAA);

            // Description de la graine
            this.valtherion.fontRenderer.drawString(
                    "(Laisser vide pour une graine aleatoire)",
                    this.width / 2 - 100, 195, 0x888888);

            // Champ de graine
            this.seedField.drawTextBox();

            // Label "Generateur de monde"
            this.valtherion.fontRenderer.drawString(
                    "Generateur de monde",
                    this.width / 2 - 100, 220, 0xAAAAAA);

            // Bouton de type de monde
            this.valtherion.fontRenderer.drawString(
                    "Defaut",
                    this.width / 2 - 100, 235, 0xFFFFFF);

            // Label "Regles du jeu"
            this.valtherion.fontRenderer.drawString(
                    "Regles du jeu",
                    this.width / 2 - 100, 260, 0xAAAAAA);

            // Cheats
            this.valtherion.fontRenderer.drawString(
                    "Triches : Activees",
                    this.width / 2 - 100, 275, 0xFFFFFF);

            // Bonus creatif
            this.valtherion.fontRenderer.drawString(
                    "Bonus creatif : Desactive",
                    this.width / 2 - 100, 290, 0xFFFFFF);
        }

        // Boutons
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        this.worldNameField.updateCursorCounter();
        this.seedField.updateCursorCounter();
    }

    @Override
    public void keyTyped(int keyCode) {
        char typedCharacter = 0;
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            typedCharacter = ' ';
        } else {
            String keyName = GLFW.glfwGetKeyName(keyCode, 0);
            if (keyName != null && keyName.length() == 1) {
                typedCharacter = keyName.charAt(0);
                boolean shiftPressed = GLFW.glfwGetKey(this.valtherion.getWindowHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                        || GLFW.glfwGetKey(this.valtherion.getWindowHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
                typedCharacter = shiftPressed
                        ? Character.toUpperCase(typedCharacter) : Character.toLowerCase(typedCharacter);
            }
        }

        if (this.worldNameField.isFocused()) {
            this.worldNameField.textboxKeyTyped(keyCode, typedCharacter);
        }
        if (this.seedField.isFocused()) {
            this.seedField.textboxKeyTyped(keyCode, typedCharacter);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
