package fr.alessdevita.valtherion.client.gui;

/**
 * GuiYesNo — dialogue de confirmation generique ("Etes-vous sur ?").
 * Le parent recoit confirmClicked(result, id) via GuiYesNoCallback.
 */
public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback parentScreen;
    /** Message ligne 1 (titre). */
    private String messageLine1;
    /** Message ligne 2 (details). */
    private String messageLine2;
    /** Id renvoye au callback. */
    protected int parentId;
    /** String du bouton "Oui". */
    private String yesButtonText;
    /** String du bouton "Non". */
    private String noButtonText;
    /** Compteur avant que "Non" devienne actif (evite les double-entrees). */
    protected int field_175999_v = 0;

    public GuiYesNo(GuiYesNoCallback callback, String line1, String line2, int id)
    {
        this.parentScreen = callback;
        this.messageLine1 = line1;
        this.messageLine2 = line2;
        this.parentId = id;
        this.yesButtonText = "Oui";
        this.noButtonText = "Non";
    }

    public GuiYesNo(GuiYesNoCallback callback, String line1, String line2, int id,
                    String yes, String no)
    {
        this.parentScreen = callback;
        this.messageLine1 = line1;
        this.messageLine2 = line2;
        this.parentId = id;
        this.yesButtonText = yes;
        this.noButtonText = no;
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height / 6 + 96,
                150, 20, this.yesButtonText));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 6 + 96,
                150, 20, this.noButtonText));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.messageLine1,
                this.width / 2, 70, 0xFFFFFF);
        this.drawCenteredString(this.valtherion.fontRenderer, this.messageLine2,
                this.width / 2, 90, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String messageLine1, int i, int i1, int i2) {
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.parentScreen.confirmClicked(true, this.parentId);
        } else if (button.id == 1) {
            this.parentScreen.confirmClicked(false, this.parentId);
        }
    }
}