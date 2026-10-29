package fr.alessdevita.valtherion.client.gui.stream;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;

/**
 * GuiIngestServers — ecran Twitch : selection du serveur d'ingest
 * (point d'entree de stream le plus proche). Conservé pour la
 * compatibilite de structure MC 1.7/1.8, rendu inactif sans Twitch.
 */
public class GuiIngestServers extends GuiScreen
{
    private final GuiScreen parentScreen;
    private final String title = "Selection du serveur de diffusion";
    /** Index du serveur selectionne. */
    private int selectedIndex = -1;
    /** Serveurs d'ingest detectes (nom + qualite). */
    private String[] ingestServers = new String[] {};

    public GuiIngestServers(GuiScreen parent)
    {
        this.parentScreen = parent;
    }

    public void setIngestServers(String[] servers)
    {
        this.ingestServers = servers;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();

        for (int i = 0; i < this.ingestServers.length; ++i)
        {
            this.buttonList.add(new GuiButton(100 + i, this.width / 2 - 155,
                    this.height / 6 + 24 * i, 300, 20, this.ingestServers[i]));
        }

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
                this.height / 6 + 24 * this.ingestServers.length + 12,
                200, 20, "Retour"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id >= 100 && button.id < 100 + this.ingestServers.length)
        {
            this.selectedIndex = button.id - 100;
            // TODO : appliquer le serveur d'ingest au service de stream
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
                this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String title, int i, int i1, int i2) {

    }
}