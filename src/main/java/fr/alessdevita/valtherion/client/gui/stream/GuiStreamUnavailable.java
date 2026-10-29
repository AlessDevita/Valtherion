package fr.alessdevita.valtherion.client.gui.stream;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;

/**
 * GuiStreamUnavailable — ecran "streaming indisponible" : affiche
 * la raison de l'echec (twitch non configure, connection perdue...)
 * avec un bouton de retour.
 */
public class GuiStreamUnavailable extends GuiScreen
{
    private final GuiScreen parentScreen;
    /** Raison de l'indisponibilite. */
    private final String reason;
    private final String title = "Diffusion indisponible";

    public GuiStreamUnavailable(GuiScreen parent, String reason)
    {
        this.parentScreen = parent;
        this.reason = reason;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
                this.height / 4 + 120, 200, 20, "Retour"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.title,
                this.width / 2, 80, 0xFFFFFF);
        this.drawCenteredString(this.valtherion.fontRenderer, this.reason,
                this.width / 2, 100, 0xC0C0C0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String title, int i, int i1, int i2) {

    }
}