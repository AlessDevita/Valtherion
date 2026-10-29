package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

/**
 * GuiDispenser — le distributeur : grille 3x3 de slots + inventaire
 * joueur. Texture dispenser.png dans MC (grille au centre-haut).
 */
public class GuiDispenser extends GuiContainer
{
    private FontRenderer fontRenderer;

    public GuiDispenser(ContainerDispenser container)
    {
        super(container);
        this.fontRenderer = new FontRenderer();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/dispenser.png"
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Distributeur", 60, 6, 0x404040);
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}