package fr.alessdevita.valtherion.client.gui.inventory;

import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.FontRenderer;

/**
 * GuiCrafting — la table de craft : grille 3x3 + slot resultat,
 * + inventaire joueur. Texture container/crafting_table.png dans MC.
 */
public class GuiCrafting extends GuiContainer
{
    private static final String __OBFID = "cl_00000000";
    private FontRenderer fontRenderer;

    public GuiCrafting(ContainerWorkbench container)
    {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/crafting_table.png"
        // drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Fabrication", 28, 6, 0x404040);
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}