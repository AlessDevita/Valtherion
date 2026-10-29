package fr.alessdevita.valtherion.client.gui;

/**
 * GuiScreenServerList — ecran intermediaire : la liste des serveurs
 * avec detection LAN, utilise par GuiMultiplayer en interne.
 * Dans MC 1.7/1.8 cette classe gere l'etat "liste en cours de ping".
 */
public class GuiScreenServerList extends GuiScreen
{
    protected GuiMultiplayer parentScreen;
    /** Index selectionne dans la liste. */
    private int selectedServerIndex = -1;
    /** true = un ping de serveur est en cours. */
    private boolean pinging = false;

    public GuiScreenServerList(GuiMultiplayer parent)
    {
        this.parentScreen = parent;
    }

    /**
     * Selectionne un serveur (nom MC : selectServerIndex).
     */
    public void selectServerIndex(int index)
    {
        this.selectedServerIndex = index;
    }

    public int getSelectedServerIndex()
    {
        return this.selectedServerIndex;
    }

    /** True si un ping est en cours (nom MC : func_146790_b). */
    public boolean isPinging()
    {
        return this.pinging;
    }

    public void setPinging(boolean pinging)
    {
        this.pinging = pinging;
    }

    /** Met a jour la liste depuis le ServerList sauvegarde. */
    public void refreshServerList()
    {
        // TODO : relire servers.dat via votre ServerList
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}