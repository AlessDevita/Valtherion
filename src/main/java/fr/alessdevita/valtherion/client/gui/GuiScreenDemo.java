package fr.alessdevita.valtherion.client.gui;

/**
 * GuiScreenDemo — ecran de fin du mode demo : propose d'acheter le jeu
 * ou de continuer en mode demo, retour au menu principal sinon.
 */
public class GuiScreenDemo extends GuiScreen
{
    /** Progression du monde demo. */
    private final int demoTime;

    public GuiScreenDemo(int demoTimeIn)
    {
        this.demoTime = demoTimeIn;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48,
                200, 20, "Acheter le jeu..."));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72,
                200, 20, "Continuer en mode demo"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96,
                200, 20, "Retour au menu"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            // Ouvre la page d'achat (navigateur externe dans MC)
            // Valtherion : action libre
        }
        else if (button.id == 2)
        {
            // Relance le monde demo
            // TODO : relancer le monde integre demo
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
        else if (button.id == 3)
        {
            this.valtherion.loadWorld(null);
            this.valtherion.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, "Temps de demo ecoule",
                this.width / 2, this.height / 4, 0xFFFFFF);
        this.drawCenteredString(this.valtherion.fontRenderer,
                "Votre periode d'essai est terminee (" + this.demoTime + " min).",
                this.width / 2, this.height / 4 + 16, 0xB0B0B0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String tempsDeDemoEcoule, int i, int i1, int i2) {

    }
}