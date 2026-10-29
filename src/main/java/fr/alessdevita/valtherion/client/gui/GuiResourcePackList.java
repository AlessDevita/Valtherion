package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.client.Valtherion;

/**
 * GuiResourcePackList — liste scrollable de resource packs.
 * Les entrees sont des GuiResourcePackAvailable (disponibles) ou
 * GuiResourcePackSelected (actifs), selon la colonne.
 */
public class GuiResourcePackList extends GuiSlot
{
    /** L'eccran parent (GuiScreenResourcePacks). */
    private final GuiScreenResourcePacks parent;
    /** Entrees de la liste. */
    private final List<GuiResourcePackAvailable> entries = Lists.newArrayList();

    public GuiResourcePackList(GuiScreenResourcePacks parent, Valtherion mc,
                               int width, int height, int top, int bottom)
    {
        super(mc, width, height, top, bottom, width / 2 - 160, width / 2 + 160, 24);
        this.parent = parent;
    }

    @Override
    protected int getSize()
    {
        return this.entries.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
    {
        // Double-clic (ou simple selon la variante MC) : deplace le pack
        this.parent.resourcePackSelected(slotIndex);
    }

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return false;
    }

    @Override
    protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
    {
        GuiResourcePackAvailable entry = this.entries.get(slotIndex);
        // Nom du pack en gras, description en dessous
        this.valtherion.fontRenderer.toString(
        );
        this.valtherion.fontRenderer.toString(
        );
    }

    public List<GuiResourcePackAvailable> getEntries()
    {
        return this.entries;
    }

    /** Rafraichit la liste depuis le dossier resourcepacks/. */
    public void refreshList(List<GuiResourcePackAvailable> newEntries)
    {
        this.entries.clear();
        this.entries.addAll(newEntries);
    }
}