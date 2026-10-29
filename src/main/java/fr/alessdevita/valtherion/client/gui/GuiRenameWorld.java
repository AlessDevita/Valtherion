package fr.alessdevita.valtherion.client.gui;

/**
 * GuiRenameWorld — ecran de renommage d'un monde ("Renommer le monde").
 * Champ texte + bouton confirmer, retour au parent avec le nouveau nom.
 */
public class GuiRenameWorld extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiTextField nameField;
    /** Nom initial du monde. */
    private final String worldName;
    private final String title = "Renommer le monde";

    public GuiRenameWorld(GuiScreen parent, String worldName)
    {
        this.parentScreen = parent;
        this.worldName = worldName;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.nameField = new GuiTextField((FontRenderer) this.valtherion.fontRenderer,
                this.width / 2 - 150, 60, 300, 20);
        this.nameField.setFocused(true);
        this.nameField.setMaxStringLength(32);
        this.nameField.setText(this.worldName);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, 96, 150, 20, "Confirmer"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, 96, 150, 20, "Annuler"));
    }

    @Override
    public void updateScreen()
    {
        this.nameField.updateCursorCounter();
    }

    @Override
    public void keyTyped(int keyCode)
    {
        this.nameField.textboxKeyTyped(keyCode, (char) 0);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0 && this.nameField.getText().trim().length() > 0)
        {
            // TODO : appeler le WorldSelectManager pour renommer le dossier
            // this.mc.getSaveLoader().renameWorld(this.worldName, this.nameField.getText().trim());
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
        this.drawCenteredString((FontRenderer) this.valtherion.fontRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        this.nameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String title, int i, int i1, int i2) {

    }
}