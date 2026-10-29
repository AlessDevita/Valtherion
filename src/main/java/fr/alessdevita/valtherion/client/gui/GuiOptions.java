package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * GuiOptions — l'ecran "Options" principal (MC_MENU):
 * Music/Sound, FOV, Difficulte, GUI Scale, Fullscreen, etc.
 */
public class GuiOptions extends GuiScreen
{
    /** Boutons de la 1ere colonne. */
    private final GuiScreen parentGuiScreen;
    protected String screenTitle = "Options";
    /** Options numeriques affichees. */
    private static final GameSettings.Options[] optionsNumeric = {
            GameSettings.Options.MUSIC,
            GameSettings.Options.SOUND,
            GameSettings.Options.FOV_SETTING,
            GameSettings.Options.GAMMA
    };

    public GuiOptions(GuiScreen parent)
    {
        this.parentGuiScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.screenTitle = "Options";
        this.buttonList.clear();

        int i = 0;

        // Boutons d'options numeriques (sliders)
        for (GameSettings.Options option : optionsNumeric)
        {
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y = this.height / 6 + 24 * (i >> 1);
            this.buttonList.add(new GuiOptionSlider(200 + i, x, y, option));
            ++i;
        }

        // Boutons "on/off" et enumeres
        this.buttonList.add(new GuiOptionButton(109, this.width / 2 - 155, this.height / 6 + 24 * (i >> 1),
                GameSettings.Options.DIFFICULTY, ""));
        this.buttonList.add(new GuiOptionButton(110, this.width / 2 + 5, this.height / 6 + 24 * (i >> 1),
                GameSettings.Options.GUI_SCALE, ""));

        // Navigation
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / 6 + 96 + 24, 150, 20,
                "Controles..."));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height / 6 + 96 + 24, 150, 20,
                "Langue..."));
        this.buttonList.add(new GuiButton(103, this.width / 2 - 155, this.height / 6 + 96 + 48, 150, 20,
                "Packs de ressources..."));
        this.buttonList.add(new GuiButton(104, this.width / 2 + 5, this.height / 6 + 96 + 48, 150, 20,
                "Snooper..."));
        // Retour
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 96 + 72,
                200, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button instanceof GuiOptionButton)
            {
                // Bouton d'option : incremente la valeur (GameSettings.setOptionValue)
                GameSettings.Options option = ((GuiOptionButton) button).returnEnumOptions();
                this.valtherion.gameSettings.setOptionValue(option);
                button.displayString = GameSettings.getKeyBinding(option);
            }
            else if (button.id == 200)
            {
                this.valtherion.gameSettings.notifySettingsChanged();
                this.valtherion.displayGuiScreen(this.parentGuiScreen);
            }
            else if (button.id == 101)
            {
                this.valtherion.displayGuiScreen(new GuiControls(this.valtherion, this.width, this.height));
            }
            else if (button.id == 102)
            {
                this.valtherion.displayGuiScreen(new GuiLanguage(this.valtherion, this.width, this.height));
            }
            else if (button.id == 103)
            {
                this.valtherion.displayGuiScreen(new GuiScreenResourcePacks(this));
            }
            else if (button.id == 104)
            {
                this.valtherion.displayGuiScreen(new GuiSnooper(this));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.screenTitle,
                this.width / 2, 15, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String screenTitle, int i, int i1, int i2) {

    }
}