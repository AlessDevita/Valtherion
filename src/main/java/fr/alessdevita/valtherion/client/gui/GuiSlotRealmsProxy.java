package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;

/**
 * GuiSlotRealmsProxy — wrapper Realms autour d'un GuiSlot.
 * Comme GuiScreenRealmsProxy : delegue tout a la liste emballee.
 */
public class GuiSlotRealmsProxy extends GuiSlot
{
    private final GuiSlot slot;

    public GuiSlotRealmsProxy(GuiSlot slotIn, Valtherion mc,
                              int width, int height, int top, int bottom)
    {
        super(mc, width, height, top, bottom, 0, width, 24);
        this.slot = slotIn;
    }

    public GuiSlot getSlot()
    {
        return this.slot;
    }

    @Override
    protected int getSize()
    {
        return this.slot.getSize();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
    {
        this.slot.elementClicked(slotIndex, doubleClick, mouseX, mouseY);
    }

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return this.slot.isSelected(slotIndex);
    }

    @Override
    protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
    {
        this.slot.drawElement(slotIndex, x, y, l, m, mouseX, mouseY);
    }

    @Override
    public void handleMouseScroll(double yoffset)
    {
        this.slot.handleMouseScroll(yoffset);
    }
}