package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * GuiSlot — liste scrollable generique (serveurs, mondes, resource packs).
 * Surchargez getSize(), getElement(int), drawElement(int, ...), etc.
 */
public abstract class GuiSlot
{
    protected final Valtherion valtherion;
    protected int width;
    protected int height;
    /** Bordures de la zone de liste. */
    public final int top;
    public final int bottom;
    public final int right;
    public final int left;
    /** Hauteur d'une entree. */
    protected final int slotHeight;
    /** Scroll courant (en entrees). */
    protected float amountScrolled;
    protected boolean hasLines;
    protected boolean showSelectionBox;
    protected int selectedIndex = -1;
    private int initialClickY = -2;
    private float scrollMultiplier;
    private int headerPadding;

    public GuiSlot(Valtherion valtherion, int width, int height, int top, int bottom, int left, int right, int slotHeight)
    {
        this.valtherion = valtherion;
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.slotHeight = slotHeight;
        this.showSelectionBox = true;
    }

    /** Nombre d'entrees dans la liste. */
    protected abstract int getSize();

    /** Le element a l'index doit etre dessine : drawElement. */
    protected abstract void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY);

    /** Contenu de l'entree (tooltip, etc.). */
    protected abstract void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY);

    protected abstract boolean isSelected(int slotIndex);

    /** Rendu principal : appel dans le drawScreen parent avec les coords souris. */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int size = this.getSize();
        int listWidth = this.right - this.left;

        // Arriere-plan sombre
        this.valtherion.entityRenderer.toString();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);

        if (this.hasLines)
        {
            this.drawContainerBackground();
        }

        int k = this.amountScrolledY();
        int l = this.top + 3 - (int) this.amountScrolled;

        if (this.headerPadding > 0)
        {
            l += this.headerPadding;
        }

        for (int i = 0; i < size; ++i)
        {
            int y = l + i * this.slotHeight + this.headerPadding;

            if (y <= this.bottom && y + this.slotHeight >= this.top)
            {
                this.drawElement(i, this.left, y, listWidth, this.slotHeight, mouseX, mouseY);
            }
        }

        // Barre de defilement si besoin
        if (size * this.slotHeight > this.bottom - this.top)
        {
            int scrollHeight = this.getScrollBarHeight();
            int barY = this.top + (int) ((float) (this.bottom - this.top - scrollHeight)
                    * this.getScrollAmount());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.valtherion.getTextureManager().bindTexture("textures/gui/widgets.png");
            this.drawTexturedModalRect(this.right, barY, 232, 0, 12, scrollHeight);
        }
    }

    protected int amountScrolledY()
    {
        return 0;
    }

    public int getScrollBarHeight()
    {
        return MathHelper.clamp_int(
                (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top))
                        / (float) (this.getSize() * this.slotHeight)),
                32, this.bottom - this.top - 8);
    }

    protected float getScrollAmount()
    {
        int max = Math.max(0, this.getSize() * this.slotHeight - (this.bottom - this.top));

        if (max <= 0)
        {
            return 0.0F;
        }

        return this.amountScrolled / (float) max;
    }

    /** Molette : scroll de 1 entree par cran. */
    public void handleMouseScroll(double yoffset)
    {
        int max = Math.max(0, this.getSize() * this.slotHeight - (this.bottom - this.top));

        if (yoffset > 0 && this.amountScrolled > 0)
        {
            this.amountScrolled = Math.max(0, this.amountScrolled - this.slotHeight);
        }
        else if (yoffset < 0 && this.amountScrolled < max)
        {
            this.amountScrolled = Math.min(max, this.amountScrolled + this.slotHeight);
        }
    }

    /** Clic dans la liste : detecte l'entree cliquee. */
    public void handleMouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.isMouseInList(mouseX, mouseY))
        {
            int clickedSlot = (mouseY - this.top + (int) this.amountScrolled - this.headerPadding)
                    / this.slotHeight;
            this.elementClicked(clickedSlot, false, mouseX, mouseY);
        }
    }

    protected boolean isMouseInList(int mouseX, int mouseY)
    {
        return mouseY >= this.top && mouseY <= this.bottom && mouseX >= this.left && mouseX <= this.right;
    }

    protected void drawContainerBackground()
    {
    }

    protected void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        this.valtherion.getTextureManager().bindTexture("textures/gui/widgets.png");
    }
}