package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * GuiScreenOptionsSounds — l'ecran "Options de musique et sons" :
 * un slider par categorie audio (volume principal, musiques, blocs,
 * hostile, friendly, joueurs, ambient, meteo).
 */
public class GuiScreenOptionsSounds extends GuiScreen
{
    private final GuiScreen parentGuiScreen;
    private final String screenTitle = "Musique & sons";

    /** Categories de sons (ordre MC 1.7/1.8). */
    private static final GameSettings.Options[] soundOptions = new GameSettings.Options[]
            {
                    GameSettings.Options.MASTER,
                    GameSettings.Options.MUSIC,
                    GameSettings.Options.RECORDS,
                    GameSettings.Options.WEATHER,
                    GameSettings.Options.BLOCKS,
                    GameSettings.Options.MOBS,
                    GameSettings.Options.ANIMALS,
                    GameSettings.Options.PLAYERS,
                    GameSettings.Options.AMBIENT
            };

    /** Slider du volume avec affichage en %. */
    private static class SoundSlider extends GuiOptionSlider
    {
        private final GameSettings.Options option;
        private String displayString;

        public SoundSlider(int id, int x, int y, GameSettings.Options option)
        {
            super(id, x, y, option);
            this.option = option;
            this.displayString = option.getEnumString() + ": " + this.getPercent();
        }

        private String getPercent()
        {
            int pct = (int) (this.getSliderValue() * 100.0F);
            return pct == 0 ? "OFF" : pct + "%";
        }

        @Override
        public void dragSlider(fr.alessdevita.valtherion.client.Valtherion mc, int mouseX)
        {
            super.dragSlider(mc, mouseX);
            this.displayString = this.option.getEnumString() + ": " + this.getPercent();
        }
    }

    public GuiScreenOptionsSounds(GuiScreen parent)
    {
        this.parentGuiScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();

        for (int i = 0; i < soundOptions.length; ++i)
        {
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y = this.height / 6 + 24 * (i >> 1);
            this.buttonList.add(new SoundSlider(100 + i, x, y, soundOptions[i]));
        }

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
                this.height / 6 + 24 * ((soundOptions.length + 1) / 2) + 12,
                200, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 200)
        {
            this.valtherion.gameSettings.saveOptions();
            this.valtherion.displayGuiScreen(this.parentGuiScreen);
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