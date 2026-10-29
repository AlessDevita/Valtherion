package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.inventory.ContainerHorseInventory;

/**
 * GuiScreenHorseInventory — inventaire du cheval : slot de selle,
 * slot d'armure, inventaire du cheval, + inventaire joueur.
 * La figure du cheval est dessinee a gauche, oriente souris.
 */
public class GuiScreenHorseInventory extends GuiContainer
{
    private final ContainerHorseInventory horseContainer;
    /** Souris pour l'orientation de la figure. */
    private float oldMouseX;
    private float oldMouseY;
    private FontRenderer fontRenderer;

    public GuiScreenHorseInventory(ContainerHorseInventory container)
    {
        super(container);
        this.horseContainer = container;
        this.ySize = 217; // plus haut : figure du cheval + slots
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/horse.png"
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);

        // TODO : rendu du modele du cheval dans le cadre gauche,
        // avec la tete qui suit oldMouseX/oldMouseY (rotationY ~ mouseX * 20 deg)
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Cheval", 8, 6, 0x404040);
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}