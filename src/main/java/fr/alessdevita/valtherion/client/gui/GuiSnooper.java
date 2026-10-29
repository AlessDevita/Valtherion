package fr.alessdevita.valtherion.client.gui;

/**
 * GuiSnooper — l'ecran "Snooper" : liste scrollable des donnees
 * anonymes collectees, avec le toggle d'activation.
 */
public class GuiSnooper extends GuiScreen
{
    private final GuiScreen parentGuiScreen;
    private final String title = "Collection de donnees";
    /** Liste scrollable des entrees snooper (cle : valeur). */
    private GuiSlotSnooper snooperList;
    /** Toggle on/off. */
    private GuiButton snooperEnabledButton;

    public GuiSnooper(GuiScreen parent)
    {
        this.parentGuiScreen = parent;
    }

    @Override
    public void displayProgressMessage(String message) {

    }

    @Override
    public void setLoadingProgress(int progress) {

    }

    @Override
    public void initGui()
    {
        this.snooperList = new GuiSlotSnooper(this);
        this.buttonList.add(this.snooperEnabledButton = new GuiButton(200, this.width / 2 - 155,
                this.height - 28, 150, 20, "Snooper : "
                + (this.valtherion.gameSettings.snooperEnabled ? "ON" : "OFF")));
        this.buttonList.add(new GuiButton(201, this.width / 2 + 5, this.height - 28,
                150, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 200)
        {
            this.valtherion.gameSettings.snooperEnabled = !this.valtherion.gameSettings.snooperEnabled;
            button.displayString = "Snooper : "
                    + (this.valtherion.gameSettings.snooperEnabled ? "ON" : "OFF");
        }
        else if (button.id == 201)
        {
            this.valtherion.displayGuiScreen(this.parentGuiScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.snooperList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.valtherion.fontRenderer, this.title,
                this.width / 2, 15, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String title, int i, int i1, int i2) {
    }

    /** La liste interne des entrees snooper. */
    public static class GuiSlotSnooper extends GuiSlot
    {
        private final GuiSnooper parent;

        public GuiSlotSnooper(GuiSnooper parent)
        {
            super(parent.valtherion, parent.width, parent.height, 32, parent.height - 64, 0, parent.width, 12);
            this.parent = parent;
        }

        @Override
        protected int getSize()
        {
            // TODO : retourner la taille de la map snooper du client
            return 0;
        }

        @Override
        protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
        {
        }

        @Override
        protected boolean isSelected(int slotIndex)
        {
            return false;
        }

        @Override
        protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
        {
            // TODO : dessiner l'entree "cle : valeur"
        }
    }
}