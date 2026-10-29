package fr.alessdevita.valtherion.client.gui;

/**
 * GuiScreenAddServer — "Ajouter un serveur" : nom + adresse IP,
 * enregistre dans le ServerList et retour a GuiMultiplayer.
 */
public class GuiScreenAddServer extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiTextField serverName;
    private GuiTextField serverAddress;
    /** ServerData en cours d'edition (null = nouveau). */
    private final Object serverData;

    public GuiScreenAddServer(GuiScreen parent, Object serverDataIn)
    {
        this.parentScreen = parent;
        this.serverData = serverDataIn;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.serverName = new GuiTextField((FontRenderer) this.valtherion.fontRenderer,
                this.width / 2 - 100, 66, 200, 20);
        this.serverName.setFocused(true);
        this.serverName.setMaxStringLength(32);
        this.serverAddress = new GuiTextField((FontRenderer) this.valtherion.fontRenderer,
                this.width / 2 - 100, 106, 200, 20);
        this.serverAddress.setMaxStringLength(254);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 142, 200, 20, "Terminer"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 168, 200, 20, "Annuler"));
    }

    @Override
    public void updateScreen()
    {
        this.serverName.updateCursorCounter();
        this.serverAddress.updateCursorCounter();
    }

    @Override
    public void keyTyped(int keyCode)
    {
        // Focus alternant avec Tab
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_TAB)
        {
            if (this.serverName.isFocused())
            {
                this.serverName.setFocused(false);
                this.serverAddress.setFocused(true);
            }
            else
            {
                this.serverName.setFocused(true);
                this.serverAddress.setFocused(false);
            }

            return;
        }

        if (this.serverName.isFocused())
        {
            this.serverName.textboxKeyTyped(keyCode, (char) 0);
        }
        else if (this.serverAddress.isFocused())
        {
            this.serverAddress.textboxKeyTyped(keyCode, (char) 0);
        }
        else
        {
            super.keyTyped(keyCode);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            // TODO : enregistrer via ServerList.addServerData / setServerData
            // (nom, ip) puis saveServerList()
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 1)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, "Ajouter un serveur",
                this.width / 2, 30, 0xFFFFFF);
        this.drawString(this.valtherion.fontRenderer, "Nom du serveur",
                this.width / 2 - 100, 53, 0xA0A0A0);
        this.drawString(this.valtherion.fontRenderer, "Adresse du serveur",
                this.width / 2 - 100, 93, 0xA0A0A0);
        this.serverName.drawTextBox();
        this.serverAddress.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawString(Object fontRenderer, String nomDuServeur, int i, int i1, int i2) {

    }

    private void drawCenteredString(Object fontRenderer, String ajouterUnServeur, int i, int i1, int i2) {
    }
}