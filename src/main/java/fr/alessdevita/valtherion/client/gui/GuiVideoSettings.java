package fr.alessdevita.valtherion.client.gui;

import java.io.IOException;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * GuiVideoSettings — ecran "Parametres Video" : utilise un
 * GuiOptionsRowList pour ranger les options 2 par ligne.
 */
public class GuiVideoSettings extends GuiScreen
{
    private final GuiScreen parentGuiScreen;
    private GuiOptionsRowList optionsRowList;
    /** Titre de l'ecran. */
    private String screenTitle = "Parametres video";

    /** Options affichees dans la liste (ordre MC 1.7/1.8). */
    private static final GameSettings.Options[] videoOptions = new GameSettings.Options[]
            {
                    GameSettings.Options.GRAPHICS_FANCY,
                    GameSettings.Options.RENDER_DISTANCE,
                    GameSettings.Options.AMBIENT_OCCLUSION,
                    GameSettings.Options.FXAA,
                    GameSettings.Options.BRIGHTNESS,
                    GameSettings.Options.ANAGLYPH,
                    GameSettings.Options.GUI_SCALE,
                    GameSettings.Options.FRAMERATE_LIMIT,
                    GameSettings.Options.VIEW_BOBBING,
                    GameSettings.Options.CLOUDS,
                    GameSettings.Options.PARTICLES,
                    GameSettings.Options.USE_VBO,
                    GameSettings.Options.SMOOTH_FPS,
                    GameSettings.Options.FAST_RENDER,
                    GameSettings.Options.TRANSLUCENT_FANCY
            };

    public GuiVideoSettings(GuiScreen parent, GameSettings settings)
    {
        this.parentGuiScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.screenTitle = "Parametres video";
        int i = 0;
        this.optionsRowList = new GuiOptionsRowList(this.valtherion, this.width, this.height,
                32, this.height - 32 + 24, videoOptions);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
                this.height - 27, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 200)
            {
                this.valtherion.gameSettings.saveOptions();
                this.valtherion.displayGuiScreen(this.parentGuiScreen);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.screenTitle, this.width / 2, 5, 0xFFFFFF);

        if (!this.optionsRowList.isMouseInList(mouseX, mouseY))
        {
            this.optionsRowList.handleMouseScroll(0);
        }

        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String screenTitle, int i, int i1, int i2) {

    }
}