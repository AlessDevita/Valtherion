package fr.alessdevita.valtherion.client.gui;

/**
 * GuiScreenWorking — ecran de progression ("Sauvegarde..." / "Chargement...").
 * Barre de progression + message, utilisable par IProgressMeter.
 */
public class GuiScreenWorking extends GuiScreen implements IProgressMeter
{
    /** Titre affiche. */
    private String title = "";
    /** Message sous la barre. */
    private String message = "";
    /** Progression 0..1. */
    private float progress = 0.0F;

    public GuiScreenWorking()
    {
    }

    public GuiScreenWorking(String title, String message)
    {
        this.title = title;
        this.message = message;
    }

    /** Nom MC : setTitle / func_146619_a. */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /** Nom MC : setMessage / func_146204_c. */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /** Nom MC : setProgress / func_146186_b. */
    public void setProgress(float progress)
    {
        this.progress = progress;
    }

    @Override
    public void displayProgressMessage(String message)
    {
        this.setMessage(message);
    }

    @Override
    public void setLoadingProgress(int progress)
    {
        this.setProgress(progress / 100.0F);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
                this.height / 4 + 132, 200, 20, "Annuler"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.valtherion.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.title,
                this.width / 2, this.height / 4 - 20, 0xFFFFFF);
        this.drawCenteredString(this.valtherion.fontRenderer, this.message,
                this.width / 2, this.height / 4, 0xC0C0C0);
        // Barre de progression (fond gris, remplissage vert)
        int barX = this.width / 2 - 100;
        int barY = this.height / 4 + 24;
        Gui.drawRect(barX, barY, barX + 200, barY + 5, 0xFF808080);
        Gui.drawRect(barX, barY, barX + (int) (this.progress * 200.0F), barY + 5, 0xFF80FF80);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String title, int i, int i1, int i2) {

    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}