package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

/**
 * GuiFurnace — le fourneau : slot input, slot combustible, slot resultat,
 * fleches de cuisson (progress) et flamme (combustible restant).
 */
public class GuiFurnace extends GuiContainer
{
    private final ContainerFurnace furnaceContainer;
    private FontRenderer fontRenderer;

    public GuiFurnace(fr.alessdevita.valtherion.client.gui.inventory.ContainerFurnace container)
    {
        super(container);
        this.furnaceContainer = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/furnace.png"
        // drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);

        // Fleche de progression (24 px de large) :
        // cookTime 0..200 -> fleche remplie de gauche a droite
        int cookProgress = this.furnaceContainer.getCookProgress(); // 0..24
        this.drawRect(this.guiLeft + 79, this.guiTop + 34,
                this.guiLeft + 79 + cookProgress, this.guiTop + 34 + 17, 0xFFE0E0E0);

        // Flamme du combustible (14 px de haut) : burnTime restant
        int burnTime = this.furnaceContainer.getBurnTimeRemaining(); // 0..13
        this.drawRect(this.guiLeft + 56, this.guiTop + 36 + 13 - burnTime,
                this.guiLeft + 56 + 14, this.guiTop + 36 + 13, 0xFFFF8000);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Fourneau", 60, 6, 0x404040);
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}