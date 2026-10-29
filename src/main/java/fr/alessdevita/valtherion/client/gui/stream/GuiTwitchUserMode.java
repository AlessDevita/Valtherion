package fr.alessdevita.valtherion.client.gui.stream;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;

/**
 * GuiTwitchUserMode — choix du mode d'authentification Twitch :
 * "Voir uniquement" (viewer) ou "Diffuser" (streamer). Fidele a
 * MC 1.7/1.8, inactif sans service de stream branche.
 */
public class GuiTwitchUserMode extends GuiScreen
{
    private final GuiScreen parentScreen;
    private final String title = "Mode Twitch";

    public GuiTwitchUserMode(GuiScreen parent)
    {
        this.parentScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48,
                200, 20, "Voir uniquement"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72,
                200, 20, "Diffuser"));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 4 + 96,
                200, 20, "Retour"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            // TODO : authentification Twitch en mode viewer
        }
        else if (button.id == 2)
        {
            // TODO : authentification Twitch en mode streamer
        }
        else if (button.id == 200)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.title,
                this.width / 2, 60, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String title, int i, int i1, int i2) {

    }
}