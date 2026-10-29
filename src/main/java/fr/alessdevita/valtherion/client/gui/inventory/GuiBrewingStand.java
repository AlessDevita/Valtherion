package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

/**
 * GuiBrewingStand — l'alambic : slot ingredient, 3 slots de fioles,
 * barre de progres de la distillation (gauche) et indicateur de
 * fuel (poudre). Texture brewing_stand.png dans MC.
 */
public class GuiBrewingStand extends GuiContainer
{
    private final ContainerBrewingStand brewingStandContainer;
    private FontRenderer fontRenderer;

    public GuiBrewingStand(ContainerBrewingStand container)
    {
        super(container);
        this.brewingStandContainer = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/brewing_stand.png"
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);

        // Barre de progres de la distillation (hauteur 29, colonne 62)
        // brewTime 400 -> 0 pendant la cuisson
        int progress = this.brewingStandContainer.getBrewProgress(); // 0..29
        this.drawRect(this.guiLeft + 60, this.guiTop + 18,
                this.guiLeft + 62, this.guiTop + 18 + progress, 0xFF808080);

        // Indicateur de poudre blaze (fuel)
        int fuel = this.brewingStandContainer.getFuelRemaining(); // 0..20
        this.drawRect(this.guiLeft + 144, this.guiTop + 22,
                this.guiLeft + 146, this.guiTop + 22 + fuel, 0xFFFFC000);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Alambic", 60, 6, 0x404040);
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}