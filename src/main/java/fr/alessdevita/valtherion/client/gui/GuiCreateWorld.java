package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import fr.alessdevita.valtherion.world.WorldType;
import org.lwjgl.glfw.GLFW;

/**
 * GuiCreateWorld — Ecran de creation de monde au theme spatial :
 * fond cosmique bleu, panneau central avec bordures lumineuses,
 * sections pour le nom du monde, le mode de jeu avec icones,
 * la difficulte, le type de monde, et les options avancees.
 */
public class GuiCreateWorld extends GuiScreen {

    private static final String BACKGROUND_RESOURCE = "/assets/valtherion/textures/gui/title/background/background_1.png";
    private static final String LOGO_RESOURCE = "/assets/valtherion/textures/gui/title/devitastudios.png";
    private static final String BUTTON_TEXTURE_RESOURCE = "/assets/valtherion/textures/gui/sprites/widget/button.png";

    private static int backgroundTexture = 0;
    private static int backgroundWidth = 0;
    private static int backgroundHeight = 0;
    private static int logoTexture = 0;
    private static int logoWidth = 0;
    private static int logoHeight = 0;
    private final GuiScreen parentScreen;
    private Layout layout;

    // Enumeration des modes de jeu
    public enum GameMode {
        SURVIVAL("Survie", "[S]", "Survivez dans un monde hostile"),
        CREATIVE("Creatif", "[C]", "Construisez sans limites"),
        ADVENTURE("Aventure", "[A]", "Explorez des quetes et donjons"),
        HARDCORE("Hardcore", "[H]", "Mode permanent : une seule vie");

        private final String displayName;
        private final String icon;
        private final String description;

        GameMode(String displayName, String icon, String description) {
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
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

    // Enumeration des types de monde
    public enum WorldPreset {
        CLASSIC("Classique"),
        INFINITE("Infini");

        private final String displayName;

        WorldPreset(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Type de monde selectionne
    private WorldType worldType;

    // Mode de jeu selectionne
    private GameMode gameMode;

    // Difficulte selectionnee
    private Difficulty difficulty;

    // Type de monde selectionne
    private WorldPreset worldPreset;

    // Options avancees deployees
    private boolean advancedOptionsExpanded;

    // Champ de texte pour le nom du monde
    private GuiTextField worldNameField;

    // Champ de texte pour la graine
    private GuiTextField seedField;

    // Bouton options avancees
    private GuiButton advancedOptionsButton;

    // Boutons
    private GuiButton createButton;
    private GuiButton cancelButton;

    // Boutons de mode de jeu
    private GuiButton survivalButton;
    private GuiButton creativeButton;
    private GuiButton adventureButton;
    private GuiButton hardcoreButton;

    // Boutons de difficulte
    private GuiButton easyButton;
    private GuiButton normalButton;
    private GuiButton hardButton;
    private GuiButton extremeButton;

    // Boutons de type de monde
    private GuiButton classicButton;
    private GuiButton infiniteButton;

    private static final int COLOR_PANEL_BG = 0xD9081025;
    private static final int COLOR_PANEL_BORDER = 0xFF43BFFF;
    private static final int COLOR_PANEL_GLOW = 0x70309FFF;
    private static final int COLOR_TEXT = 0xFFF1F6FF;
    private static final int COLOR_TEXT_DIM = 0xFF9FB9E8;
    private static final int COLOR_CYAN = 0xFF45C8FF;
    private static final int COLOR_SELECTED_GLOW = 0xA0008CFF;
    private static final org.joml.Vector4f BUTTON_TINT_NORMAL = new org.joml.Vector4f(0.16f, 0.24f, 0.42f, 1f);
    private static final org.joml.Vector4f BUTTON_TINT_SELECTED = new org.joml.Vector4f(0.58f, 0.82f, 1f, 1f);
    private static final org.joml.Vector4f BUTTON_TINT_HOVER = new org.joml.Vector4f(0.30f, 0.55f, 0.82f, 1f);
    private static final org.joml.Vector4f BUTTON_TINT_CLICK = new org.joml.Vector4f(0.42f, 0.66f, 0.92f, 1f);

    public GuiCreateWorld(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.worldType = WorldType.NORMAL;
        this.gameMode = GameMode.SURVIVAL;
        this.difficulty = Difficulty.NORMAL;
        this.worldPreset = WorldPreset.CLASSIC;
        this.advancedOptionsExpanded = false;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.layout = calculateLayout();

        this.worldNameField = new GuiTextField(this.valtherion.fontRenderer,
                this.layout.fieldX + 10, this.layout.nameFieldY + 4,
                this.layout.fieldWidth - 20, Math.max(12, this.layout.fieldHeight - 8));
        this.worldNameField.setEnableBackgroundDrawing(false);
        this.worldNameField.setText("");
        this.worldNameField.setMaxStringLength(128);

        this.seedField = new GuiTextField(this.valtherion.fontRenderer,
                this.layout.seedX + 10, this.layout.seedY + 4,
                this.layout.seedWidth - 20, Math.max(12, this.layout.seedHeight - 8));
        this.seedField.setEnableBackgroundDrawing(false);
        this.seedField.setText("");
        this.seedField.setMaxStringLength(15);

        this.buttonList.add(this.createButton = new GuiButton(1, 0, 0, 1, 1,
                "CREER LE MONDE", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.cancelButton = new GuiButton(2, 0, 0, 1, 1,
                "<  RETOUR", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.advancedOptionsButton = new GuiButton(40, 0, 0, 1, 1,
                "v  OPTIONS AVANCEES", BUTTON_TEXTURE_RESOURCE));

        this.buttonList.add(this.survivalButton = new GuiButton(10, 0, 0, 1, 1,
                "[" + GameMode.SURVIVAL.getIcon() + "]  SURVIE", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.creativeButton = new GuiButton(11, 0, 0, 1, 1,
                "[" + GameMode.CREATIVE.getIcon() + "]  CREATIF", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.adventureButton = new GuiButton(12, 0, 0, 1, 1,
                "[" + GameMode.ADVENTURE.getIcon() + "]  AVENTURE", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.hardcoreButton = new GuiButton(13, 0, 0, 1, 1,
                "[" + GameMode.HARDCORE.getIcon() + "]  HARDCORE", BUTTON_TEXTURE_RESOURCE));

        this.buttonList.add(this.easyButton = new GuiButton(20, 0, 0, 1, 1,
                "Facile", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.normalButton = new GuiButton(21, 0, 0, 1, 1,
                "Normale", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.hardButton = new GuiButton(22, 0, 0, 1, 1,
                "Difficile", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.extremeButton = new GuiButton(23, 0, 0, 1, 1,
                "EXTREME", BUTTON_TEXTURE_RESOURCE));

        this.buttonList.add(this.classicButton = new GuiButton(30, 0, 0, 1, 1,
                "CLASSIQUE", BUTTON_TEXTURE_RESOURCE));
        this.buttonList.add(this.infiniteButton = new GuiButton(31, 0, 0, 1, 1,
                "INFINI", BUTTON_TEXTURE_RESOURCE));

        for (GuiButton guiButton : this.buttonList) {
            guiButton.setTextureTintEnabled(true);
        }
        applyLayout();
        setButtonAppearance(this.createButton, true);
        setButtonAppearance(this.cancelButton, false);
        setButtonAppearance(this.advancedOptionsButton, false);
        updateModeButtons();
        updateOptionButtonStyles();
    }

    private Layout calculateLayout() {
        int verticalOffset = getVerticalOffset();
        int sideMargin = Math.max(12, Math.round(this.width * 0.035f));
        int panelWidth = Math.min(Math.round(this.width * 0.55f), this.width - sideMargin * 2);
        int panelHeight = Math.round(this.height * (this.advancedOptionsExpanded ? 0.70f : 0.63f));
        int panelX = (this.width - panelWidth) / 2;
        int panelY = Math.round(this.height * 0.20f) + verticalOffset;
        int padding = Math.max(10, Math.round(panelWidth * 0.045f));
        int contentX = panelX + padding;
        int contentWidth = panelWidth - padding * 2;
        int gap = Math.max(4, Math.round(contentWidth * 0.018f));
        int modeWidth = (contentWidth - gap * 2) / 3;
        int difficultyGap = Math.max(3, Math.round(contentWidth * 0.014f));
        int difficultyWidth = (contentWidth - difficultyGap * 3) / 4;
        int presetGap = Math.max(6, Math.round(contentWidth * 0.03f));
        int presetWidth = (contentWidth - presetGap) / 2;
        int footerWidth = Math.round(this.width * 0.23f);
        int footerGap = Math.round(this.width * 0.03f);
        int footerX = (this.width - footerWidth * 2 - footerGap) / 2;
        int footerY = Math.round(this.height * (this.advancedOptionsExpanded ? 0.925f : 0.866f)) + verticalOffset;
        int footerHeight = Math.max(22, Math.round(this.height * (this.advancedOptionsExpanded ? 0.06f : 0.085f)));
        int seedWidth = Math.round(contentWidth * 0.68f);

        return new Layout(panelX, panelY, panelWidth, panelHeight, contentX, contentWidth,
                Math.round(this.height * 0.229f) + verticalOffset,
                contentX, Math.round(this.height * 0.267f) + verticalOffset, contentWidth, Math.max(18, Math.round(this.height * 0.058f)),
                Math.round(this.height * 0.371f) + verticalOffset, Math.round(this.height * 0.412f) + verticalOffset,
                modeWidth, Math.max(22, Math.round(this.height * 0.072f)), gap,
                Math.round(this.height * 0.518f) + verticalOffset, Math.round(this.height * 0.554f) + verticalOffset,
                difficultyWidth, Math.max(19, Math.round(this.height * 0.054f)), difficultyGap,
                Math.round(this.height * 0.634f) + verticalOffset, Math.round(this.height * 0.672f) + verticalOffset,
                presetWidth, Math.max(21, Math.round(this.height * 0.06f)), presetGap,
                Math.round(this.height * 0.748f) + verticalOffset, Math.max(20, Math.round(this.height * 0.054f)),
                Math.round(this.height * 0.806f) + verticalOffset, Math.round(this.height * 0.834f) + verticalOffset,
                seedWidth, Math.max(18, Math.round(this.height * 0.046f)), gap,
                footerX, footerY, footerWidth, footerHeight, footerGap);
    }

    private int getVerticalOffset() {
        return Math.round(this.height * 0.015f);
    }

    private void applyLayout() {
        Layout l = this.layout;
        setButtonBounds(this.createButton, l.footerX + l.footerWidth + l.footerGap, l.footerY, l.footerWidth, l.footerHeight);
        setButtonBounds(this.cancelButton, l.footerX, l.footerY, l.footerWidth, l.footerHeight);
        setButtonBounds(this.advancedOptionsButton, l.contentX, l.advancedY, l.contentWidth, l.advancedHeight);
        setButtonBounds(this.survivalButton, l.contentX, l.modeY, l.modeWidth, l.modeHeight);
        setButtonBounds(this.creativeButton, l.contentX + l.modeWidth + l.modeGap, l.modeY, l.modeWidth, l.modeHeight);
        setButtonBounds(this.adventureButton, l.contentX + (l.modeWidth + l.modeGap) * 2, l.modeY, l.modeWidth, l.modeHeight);

        int difficultyY = l.difficultyY;
        setButtonBounds(this.easyButton, l.contentX, difficultyY, l.difficultyWidth, l.difficultyHeight);
        setButtonBounds(this.normalButton, l.contentX + l.difficultyWidth + l.difficultyGap, difficultyY, l.difficultyWidth, l.difficultyHeight);
        setButtonBounds(this.hardButton, l.contentX + (l.difficultyWidth + l.difficultyGap) * 2, difficultyY, l.difficultyWidth, l.difficultyHeight);
        setButtonBounds(this.extremeButton, l.contentX + (l.difficultyWidth + l.difficultyGap) * 3, difficultyY, l.difficultyWidth, l.difficultyHeight);

        setButtonBounds(this.classicButton, l.contentX, l.presetY, l.presetWidth, l.presetHeight);
        setButtonBounds(this.infiniteButton, l.contentX + l.presetWidth + l.presetGap, l.presetY, l.presetWidth, l.presetHeight);
        setButtonBounds(this.hardcoreButton, l.seedX + l.seedWidth + l.seedGap, l.seedY,
                l.contentX + l.contentWidth - (l.seedX + l.seedWidth + l.seedGap), l.seedHeight);
        this.hardcoreButton.enabled = this.advancedOptionsExpanded;
        this.seedField.setPosition(l.seedX + 10, l.seedY + 4, l.seedWidth - 20, Math.max(12, l.seedHeight - 8));
        this.seedField.setVisible(this.advancedOptionsExpanded);
    }

    private void setButtonBounds(GuiButton button, int x, int y, int buttonWidth, int buttonHeight) {
        button.setPosition(new org.joml.Vector2f(x, y));
        button.setSize(new org.joml.Vector2f(buttonWidth, buttonHeight));
    }

    /**
     * Met a jour l'etat active/desactive des boutons de mode selon le mode selectionne.
     */
    private void updateModeButtons() {
        if (this.survivalButton != null) this.survivalButton.enabled = true;
        if (this.creativeButton != null) this.creativeButton.enabled = true;
        if (this.adventureButton != null) this.adventureButton.enabled = true;
        if (this.hardcoreButton != null) this.hardcoreButton.enabled = this.advancedOptionsExpanded;
        if (this.survivalButton != null) setButtonAppearance(this.survivalButton, this.gameMode == GameMode.SURVIVAL);
        if (this.creativeButton != null) setButtonAppearance(this.creativeButton, this.gameMode == GameMode.CREATIVE);
        if (this.adventureButton != null) setButtonAppearance(this.adventureButton, this.gameMode == GameMode.ADVENTURE);
        if (this.hardcoreButton != null) setButtonAppearance(this.hardcoreButton, this.gameMode == GameMode.HARDCORE);
    }

    private void updateOptionButtonStyles() {
        setButtonAppearance(this.easyButton, this.difficulty == Difficulty.EASY);
        setButtonAppearance(this.normalButton, this.difficulty == Difficulty.NORMAL);
        setButtonAppearance(this.hardButton, this.difficulty == Difficulty.HARD);
        setButtonAppearance(this.extremeButton, this.difficulty == Difficulty.EXTREME);
        setButtonAppearance(this.classicButton, this.worldPreset == WorldPreset.CLASSIC);
        setButtonAppearance(this.infiniteButton, this.worldPreset == WorldPreset.INFINITE);
    }

    private void setButtonAppearance(GuiButton button, boolean selected) {
        button.setNormalColor(selected ? BUTTON_TINT_SELECTED : BUTTON_TINT_NORMAL);
        button.setHoverColor(BUTTON_TINT_HOVER);
        button.setClickColor(BUTTON_TINT_CLICK);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                String worldName = this.worldNameField.getText().trim();
                String seed = this.seedField.getText().trim();
                if (worldName.isEmpty()) {
                    worldName = "Mon monde";
                }
                this.valtherion.createWorld(worldName, this.worldType, seed, this.gameMode);
                break;
            case 2:
                this.valtherion.displayGuiScreen(this.parentScreen);
                break;
            case 10:
                this.gameMode = GameMode.SURVIVAL;
                updateModeButtons();
                break;
            case 11:
                this.gameMode = GameMode.CREATIVE;
                updateModeButtons();
                break;
            case 12:
                this.gameMode = GameMode.ADVENTURE;
                updateModeButtons();
                break;
            case 13:
                this.gameMode = GameMode.HARDCORE;
                updateModeButtons();
                break;
            case 20:
                this.difficulty = Difficulty.EASY;
                updateOptionButtonStyles();
                break;
            case 21:
                this.difficulty = Difficulty.NORMAL;
                updateOptionButtonStyles();
                break;
            case 22:
                this.difficulty = Difficulty.HARD;
                updateOptionButtonStyles();
                break;
            case 23:
                this.difficulty = Difficulty.EXTREME;
                updateOptionButtonStyles();
                break;
            case 30:
                this.worldPreset = WorldPreset.CLASSIC;
                this.worldType = WorldType.NORMAL;
                updateOptionButtonStyles();
                break;
            case 31:
                this.worldPreset = WorldPreset.INFINITE;
                this.worldType = WorldType.CUSTOM;
                updateOptionButtonStyles();
                break;
            case 40:
                this.advancedOptionsExpanded = !this.advancedOptionsExpanded;
                this.layout = calculateLayout();
                applyLayout();
                updateModeButtons();
                this.advancedOptionsButton.setText(this.advancedOptionsExpanded
                        ? "^  OPTIONS AVANCEES" : "v  OPTIONS AVANCEES");
                break;
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
            } else {
                this.seedField.setFocused(false);
            }
        }
        super.handleMouseInput(button, action);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground();
        Gui.drawRect(0, 0, this.width, this.height, 0x30020A20);

        drawMainPanel();
        drawTitlePanel();
        drawSectionLabel("Nom du monde :", this.layout.contentX, this.layout.nameLabelY);
        drawSectionLabel("Mode de jeu :", this.layout.contentX, this.layout.modeLabelY);
        drawSectionLabel("Difficulte :", this.layout.contentX, this.layout.difficultyLabelY);
        drawSectionLabel("Type de monde :", this.layout.contentX, this.layout.presetLabelY);
        drawSectionSeparator(this.layout.contentX, Math.round(this.height * 0.348f) + getVerticalOffset(), this.layout.contentWidth);
        drawSectionSeparator(this.layout.contentX, Math.round(this.height * 0.500f) + getVerticalOffset(), this.layout.contentWidth);
        drawSectionSeparator(this.layout.contentX, Math.round(this.height * 0.622f) + getVerticalOffset(), this.layout.contentWidth);
        drawSectionSeparator(this.layout.contentX, Math.round(this.height * 0.740f) + getVerticalOffset(), this.layout.contentWidth);

        drawInputFrame(this.layout.fieldX, this.layout.nameFieldY, this.layout.fieldWidth,
                this.layout.fieldHeight, this.worldNameField.isFocused());
        this.worldNameField.drawTextBox();
        if (this.worldNameField.getText().isEmpty() && !this.worldNameField.isFocused()) {
            this.valtherion.fontRenderer.drawStringWithShadow("Mon monde...", this.layout.fieldX + 10,
                    this.layout.nameFieldY + (this.layout.fieldHeight - 8) / 2 + 5, COLOR_TEXT_DIM);
        }

        if (this.advancedOptionsExpanded) {
            drawSectionLabel("Graine (optionnel) :", this.layout.seedX, this.layout.seedLabelY);
            drawInputFrame(this.layout.seedX, this.layout.seedY, this.layout.seedWidth,
                    this.layout.seedHeight, this.seedField.isFocused());
            this.seedField.drawTextBox();
        }

        if (this.gameMode == GameMode.SURVIVAL) drawSelectedButton(this.survivalButton);
        if (this.gameMode == GameMode.CREATIVE) drawSelectedButton(this.creativeButton);
        if (this.gameMode == GameMode.ADVENTURE) drawSelectedButton(this.adventureButton);
        if (this.gameMode == GameMode.HARDCORE && this.advancedOptionsExpanded) drawSelectedButton(this.hardcoreButton);
        if (this.difficulty == Difficulty.EASY) drawSelectedButton(this.easyButton);
        if (this.difficulty == Difficulty.NORMAL) drawSelectedButton(this.normalButton);
        if (this.difficulty == Difficulty.HARD) drawSelectedButton(this.hardButton);
        if (this.difficulty == Difficulty.EXTREME) drawSelectedButton(this.extremeButton);
        if (this.worldPreset == WorldPreset.CLASSIC) drawSelectedButton(this.classicButton);
        if (this.worldPreset == WorldPreset.INFINITE) drawSelectedButton(this.infiniteButton);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTitlePanel() {
        drawLogo();

        int titleY = Math.round(this.height * 0.064f) + getVerticalOffset();
        int titleHeight = Math.max(24, Math.round(this.height * 0.09f));
        int titleWidth = Math.round(this.width * 0.42f);
        int titleX = (this.width - titleWidth) / 2;
        int lineY = titleY + titleHeight / 2;
        Gui.drawRect(Math.round(this.width * 0.16f), lineY - 1, titleX - 8, lineY + 1, COLOR_CYAN);
        Gui.drawRect(titleX + titleWidth + 8, lineY - 1,
                Math.round(this.width * 0.84f), lineY + 1, COLOR_CYAN);
        Gui.drawRect(titleX, titleY, titleX + titleWidth, titleY + titleHeight, 0xD9081025);
        drawBeveledFrame(titleX, titleY, titleWidth, titleHeight,
                Math.max(8, Math.round(titleHeight * 0.22f)), COLOR_CYAN);

        String title = "CREER UN MONDE";
        this.valtherion.fontRenderer.drawCenteredString(title, this.width / 2,
                titleY + (titleHeight - 8) / 2 + 5, COLOR_TEXT);
        drawDiamond(this.width / 2, titleY - 1, 8, COLOR_CYAN);
        drawDiamond(this.width / 2, titleY + titleHeight + 1, 7, COLOR_PANEL_BORDER);
    }

    private void drawLogo() {
        int texture = getLogoTexture();
        if (texture == 0 || logoHeight == 0) return;

        int displayHeight = Math.max(22, Math.round(this.height * 0.06f));
        int displayWidth = logoWidth * displayHeight / logoHeight;
        int x = Math.round(this.width * 0.03f);
        int y = Math.round(this.height * 0.03f) + getVerticalOffset();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(x + displayWidth, y);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(x + displayWidth, y + displayHeight);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(x, y + displayHeight);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void drawMainPanel() {
        Layout l = this.layout;
        Gui.drawRect(l.panelX - 3, l.panelY - 3,
                l.panelX + l.panelWidth + 3, l.panelY + l.panelHeight + 3, COLOR_PANEL_GLOW);
        this.drawGradientRect(l.panelX, l.panelY, l.panelX + l.panelWidth, l.panelY + l.panelHeight,
                COLOR_PANEL_BG, 0xE808142C);

        int corner = Math.max(14, Math.round(l.panelWidth * 0.025f));
        drawBeveledFrame(l.panelX, l.panelY, l.panelWidth, l.panelHeight, corner, COLOR_PANEL_BORDER);
        Gui.drawRect(l.panelX + 2, l.panelY + corner, l.panelX + 5, l.panelY + corner + 13, COLOR_CYAN);
        Gui.drawRect(l.panelX + l.panelWidth - 5, l.panelY + corner,
                l.panelX + l.panelWidth - 2, l.panelY + corner + 13, COLOR_CYAN);
    }

    private void drawBeveledFrame(int x, int y, int width, int height, int cut, int color) {
        boolean blendWasEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean textureWasEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f((color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f,
                (color & 255) / 255f, (color >>> 24) / 255f);
        GL11.glLineWidth(2f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x + cut, y);
        GL11.glVertex2f(x + width - cut, y);
        GL11.glVertex2f(x + width, y + cut);
        GL11.glVertex2f(x + width, y + height - cut);
        GL11.glVertex2f(x + width - cut, y + height);
        GL11.glVertex2f(x + cut, y + height);
        GL11.glVertex2f(x, y + height - cut);
        GL11.glVertex2f(x, y + cut);
        GL11.glEnd();
        GL11.glLineWidth(1f);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        if (textureWasEnabled) GL11.glEnable(GL11.GL_TEXTURE_2D);
        else GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (blendWasEnabled) GL11.glEnable(GL11.GL_BLEND);
        else GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawSectionLabel(String text, int x, int y) {
        drawSectionIcon(x + 7, y + 7, text);
        this.valtherion.fontRenderer.drawStringWithShadow(text, x + 22, y + 5, COLOR_TEXT);
    }

    private void drawSectionIcon(int centerX, int centerY, String section) {
        int iconSize = Math.max(10, Math.round(this.height * 0.015f));
        if (section.startsWith("Nom")) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(0.27f, 0.78f, 1f, 1f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            for (int i = 0; i < 20; i++) {
                double angle = Math.PI * 2.0 * i / 20.0;
                GL11.glVertex2d(centerX + Math.cos(angle) * iconSize / 2.0,
                        centerY + Math.sin(angle) * iconSize / 2.0);
            }
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(centerX - iconSize / 2f, centerY);
            GL11.glVertex2f(centerX + iconSize / 2f, centerY);
            GL11.glVertex2f(centerX, centerY - iconSize / 2f);
            GL11.glVertex2f(centerX, centerY + iconSize / 2f);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            return;
        }

        if (section.startsWith("Mode")) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(0.27f, 0.78f, 1f, 1f);
            GL11.glLineWidth(2f);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(centerX - iconSize / 2f, centerY + iconSize / 2f);
            GL11.glVertex2f(centerX + iconSize / 2f, centerY - iconSize / 2f);
            GL11.glVertex2f(centerX - iconSize / 2f, centerY - iconSize / 2f);
            GL11.glVertex2f(centerX + iconSize / 2f, centerY + iconSize / 2f);
            GL11.glEnd();
            GL11.glLineWidth(1f);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            return;
        }

        if (section.startsWith("Difficulte")) {
            int barWidth = Math.max(2, iconSize / 4);
            int gap = Math.max(1, iconSize / 12);
            for (int i = 0; i < 3; i++) {
                int height = (i + 1) * iconSize / 3;
                Gui.drawRect(centerX - iconSize / 2 + i * (barWidth + gap),
                        centerY + iconSize / 2 - height,
                        centerX - iconSize / 2 + i * (barWidth + gap) + barWidth,
                        centerY + iconSize / 2, COLOR_CYAN);
            }
            return;
        }

        drawDiamond(centerX, centerY, iconSize, COLOR_CYAN);
        Gui.drawRect(centerX - 1, centerY - iconSize / 3, centerX + 1, centerY + iconSize / 3,
                COLOR_PANEL_BG);
    }

    private void drawSectionSeparator(int x, int y, int width) {
        Gui.drawRect(x, y, x + width, y + 1, 0x604CAFFF);
        Gui.drawRect(x + width / 3, y, x + width * 2 / 3, y + 1, 0xB043C8FF);
    }

    private void drawDiamond(int centerX, int centerY, int radius, int color) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f((color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f,
                (color & 255) / 255f, (color >>> 24) / 255f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(centerX, centerY - radius);
        GL11.glVertex2f(centerX + radius, centerY);
        GL11.glVertex2f(centerX, centerY + radius);
        GL11.glVertex2f(centerX - radius, centerY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void drawInputFrame(int x, int y, int inputWidth, int inputHeight, boolean focused) {
        int border = focused ? 0xFF7BE0FF : COLOR_CYAN;
        Gui.drawRect(x - 1, y - 1, x + inputWidth + 1, y + inputHeight + 1, 0x80309FFF);
        Gui.drawRect(x, y, x + inputWidth, y + inputHeight, border);
        Gui.drawRect(x + 2, y + 2, x + inputWidth - 2, y + inputHeight - 2, 0xF0061228);
    }

    private void drawSelectedButton(GuiButton button) {
        org.joml.Vector2f position = button.getPosition();
        org.joml.Vector2f size = button.getSize();
        int x = (int) position.x;
        int y = (int) position.y;
        int buttonWidth = (int) size.x;
        int buttonHeight = (int) size.y;
        Gui.drawRect(x + 8, y + buttonHeight - 3, x + buttonWidth - 8, y + buttonHeight - 2, COLOR_CYAN);
        Gui.drawRect(x + buttonWidth / 3, y + buttonHeight - 2,
                x + buttonWidth * 2 / 3, y + buttonHeight - 1, COLOR_SELECTED_GLOW);
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

    private static final class Layout {
        private final int panelX;
        private final int panelY;
        private final int panelWidth;
        private final int panelHeight;
        private final int contentX;
        private final int contentWidth;
        private final int nameLabelY;
        private final int fieldX;
        private final int nameFieldY;
        private final int fieldWidth;
        private final int fieldHeight;
        private final int modeLabelY;
        private final int modeY;
        private final int modeWidth;
        private final int modeHeight;
        private final int modeGap;
        private final int difficultyLabelY;
        private final int difficultyY;
        private final int difficultyWidth;
        private final int difficultyHeight;
        private final int difficultyGap;
        private final int presetLabelY;
        private final int presetY;
        private final int presetWidth;
        private final int presetHeight;
        private final int presetGap;
        private final int advancedY;
        private final int advancedHeight;
        private final int seedLabelY;
        private final int seedX;
        private final int seedY;
        private final int seedWidth;
        private final int seedHeight;
        private final int seedGap;
        private final int footerX;
        private final int footerY;
        private final int footerWidth;
        private final int footerHeight;
        private final int footerGap;

        private Layout(int panelX, int panelY, int panelWidth, int panelHeight, int contentX, int contentWidth,
                       int nameLabelY, int fieldX, int nameFieldY, int fieldWidth, int fieldHeight,
                       int modeLabelY, int modeY, int modeWidth, int modeHeight, int modeGap,
                       int difficultyLabelY, int difficultyY, int difficultyWidth, int difficultyHeight,
                       int difficultyGap, int presetLabelY, int presetY, int presetWidth, int presetHeight,
                       int presetGap, int advancedY, int advancedHeight, int seedLabelY, int seedY,
                       int seedWidth, int seedHeight, int seedGap, int footerX, int footerY,
                       int footerWidth, int footerHeight, int footerGap) {
            this.panelX = panelX;
            this.panelY = panelY;
            this.panelWidth = panelWidth;
            this.panelHeight = panelHeight;
            this.contentX = contentX;
            this.contentWidth = contentWidth;
            this.nameLabelY = nameLabelY;
            this.fieldX = fieldX;
            this.nameFieldY = nameFieldY;
            this.fieldWidth = fieldWidth;
            this.fieldHeight = fieldHeight;
            this.modeLabelY = modeLabelY;
            this.modeY = modeY;
            this.modeWidth = modeWidth;
            this.modeHeight = modeHeight;
            this.modeGap = modeGap;
            this.difficultyLabelY = difficultyLabelY;
            this.difficultyY = difficultyY;
            this.difficultyWidth = difficultyWidth;
            this.difficultyHeight = difficultyHeight;
            this.difficultyGap = difficultyGap;
            this.presetLabelY = presetLabelY;
            this.presetY = presetY;
            this.presetWidth = presetWidth;
            this.presetHeight = presetHeight;
            this.presetGap = presetGap;
            this.advancedY = advancedY;
            this.advancedHeight = advancedHeight;
            this.seedLabelY = seedLabelY;
            this.seedX = contentX;
            this.seedY = seedY;
            this.seedWidth = seedWidth;
            this.seedHeight = seedHeight;
            this.seedGap = seedGap;
            this.footerX = footerX;
            this.footerY = footerY;
            this.footerWidth = footerWidth;
            this.footerHeight = footerHeight;
            this.footerGap = footerGap;
        }
    }

    /**
     * Dessine le background du menu en remplissant l'ecran tout en conservant le ratio.
     */
    private void drawMenuBackground() {
        int texture = getBackgroundTexture();
        if (texture == 0 || backgroundWidth == 0 || backgroundHeight == 0) {
            // Fallback : fond degrade si la texture ne peut pas etre chargee
            this.drawGradientRect(0, 0, this.width, this.height, 0xFF0A0A1A, 0xFF1A1A3E);
            return;
        }

        // Ajuster le background pour remplir l'ecran tout en conservant le ratio
        float scaleX = (float) this.width / backgroundWidth;
        float scaleY = (float) this.height / backgroundHeight;
        float scale = Math.max(scaleX, scaleY);

        int drawWidth = (int) (backgroundWidth * scale);
        int drawHeight = (int) (backgroundHeight * scale);
        int x = (this.width - drawWidth) / 2;
        int y = (this.height - drawHeight) / 2;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(x + drawWidth, y);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(x + drawWidth, y + drawHeight);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(x, y + drawHeight);
        GL11.glEnd();
    }

    private static int getLogoTexture() {
        if (logoTexture != 0) return logoTexture;
        try (InputStream input = GuiCreateWorld.class.getResourceAsStream(LOGO_RESOURCE)) {
            if (input == null) {
                System.err.println("Logo non trouve : " + LOGO_RESOURCE);
                return 0;
            }
            byte[] bytes = input.readAllBytes();
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger le logo : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                logoWidth = width.get(0);
                logoHeight = height.get(0);
                try {
                    logoTexture = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, logoTexture);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            logoWidth, logoHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (Exception exception) {
            System.err.println("Impossible de charger le logo : " + exception.getMessage());
            return 0;
        }
        return logoTexture;
    }

    /**
     * Charge la texture de background depuis les resources.
     */
    private static int getBackgroundTexture() {
        if (backgroundTexture != 0) return backgroundTexture;
        try (InputStream input = GuiCreateWorld.class.getResourceAsStream(BACKGROUND_RESOURCE)) {
            if (input == null) {
                System.err.println("Background non trouve : " + BACKGROUND_RESOURCE);
                return 0;
            }
            byte[] bytes = input.readAllBytes();
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger le background : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                backgroundWidth = width.get(0);
                backgroundHeight = height.get(0);
                try {
                    backgroundTexture = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            backgroundWidth, backgroundHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (Exception exception) {
            System.err.println("Impossible de charger le background : " + exception.getMessage());
            return 0;
        }
        return backgroundTexture;
    }
}
