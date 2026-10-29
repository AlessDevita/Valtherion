package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

/**
 * GuiRepair — l'ecran de l'enclume (rename + repair d'items).
 * Grille d'inventaire 3x9 + zones d'input + nom d'item editable.
 */
public class GuiRepair extends GuiScreen
{
    /** Emplacements : input gauche, input droite, resultat. */
    public static final int INPUT_LEFT = 0;
    public static final int INPUT_RIGHT = 1;
    public static final int OUTPUT = 2;
    /** Coordonnees du coin haut-gauche de la grille d'enclume. */
    private int xSize = 176;
    private int ySize = 166;
    private int guiLeft;
    private int guiTop;
    /** Champ de renommage. */
    private GuiTextField nameField;
    /** Nom affiche au-dessus des slots (nom du conteneur). */
    private String field_147091_x = "Enclume";
    private String repairCost = "";
    /** Item present dans le slot resultat. */
    private Object stackedItemIcon;

    public GuiRepair()
    {
    }

    @Override
    public void initGui()
    {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.nameField = new GuiTextField((FontRenderer) this.valtherion.fontRenderer,
                this.guiLeft + 62, this.guiTop + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setMaxStringLength(30);
        this.buttonList.clear();
        // Bouton inutile ici mais structure MC conserve
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.nameField.updateCursorCounter();
    }

    @Override
    public void keyTyped(int keyCode)
    {
        if (this.nameField.isFocused())
        {
            this.nameField.textboxKeyTyped(keyCode, (char) 0);
            // TODO : envoyer le nom au conteneur (sendInventoryNamePacket)
        }
        else
        {
            super.keyTyped(keyCode);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
    }

    @Override
    public void handleMouseInput(int button, int action)
    {
        super.handleMouseInput(button, action);

        // TODO : gerer les clics sur les 3 slots de l'enclume
        // (pickup/depot via le ContainerRepair)
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Fond de l'enclume
        this.valtherion.getTextureManager().bindTexture("textures/gui/anvil.png");
        this.drawTexturedModalRectDim(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRectDim(this.guiLeft + 59, this.guiTop + 20, 0, this.ySize, 110, 16);
        this.drawCenteredString((FontRenderer) this.valtherion.fontRenderer, this.field_147091_x,
                this.width / 2, this.guiTop - 12, 0xFFFFFF);
        this.nameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String field147091X, int i, int i1, int i2) {

    }

    private void drawTexturedModalRectDim(int x, int y, int u, int v, int w, int h)
    {
        Gui.drawRect(x, y, x + w, y + h, 0xFFC6C6C6);
    }

    /** Met a jour le cout affiche (nom MC : updateRepairOutput). */
    public void updateRepairOutput(int cost)
    {
        this.repairCost = cost > 0 ? "Cout : " + cost + " niveaux" : "";
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}