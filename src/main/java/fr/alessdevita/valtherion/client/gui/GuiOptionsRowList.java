package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * GuiOptionsRowList — liste d'options arrangee en rang de 2 boutons
 * par ligne (comme l'ecran Video Settings de MC).
 */
public class GuiOptionsRowList extends GuiSlot
{
    /** Largeur d'un bouton d'option. */
    private static final int BUTTON_WIDTH = 150;
    /** Ecart vertical entre deux boutons d'une meme ligne. */
    private static final int BUTTON_HEIGHT = 20;
    /** Ecart horizontal entre les deux boutons d'une ligne. */
    private static final int BUTTON_SPACING = 24;
    /** Largeur totale d'une ligne (2 boutons + espacement). */
    private static final int ROW_WIDTH = 2 * BUTTON_WIDTH + BUTTON_SPACING;
    /** Espacement entre deux lignes. */
    private static final int ROW_SPACING = 4;

    private final List<GuiOptionsRowList.Row> optionsRows = Lists.newArrayList();

    public GuiOptionsRowList(Valtherion mc, int width, int height, int top, int bottom,
                             GameSettings.Options... options)
    {
        super(mc, width, height, top, bottom, (width - ROW_WIDTH) / 2, (width + ROW_WIDTH) / 2, 24);
        this.showSelectionBox = false;

        // Regroupe les options deux par deux en "rows"
        for (int i = 0; i < options.length; i += 2)
        {
            GuiOptionsRowList.Row row = new GuiOptionsRowList.Row(
                    new GuiOptionButton(200 + i, 0, 0, options[i], ""),
                    i + 1 < options.length
                            ? new GuiOptionButton(201 + i, 0, 0, options[i + 1], "")
                            : null);
            this.optionsRows.add(row);
        }
    }

    @Override
    protected int getSize()
    {
        return this.optionsRows.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
    {
    }

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return false;
    }

    @Override
    protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
    {
        GuiOptionsRowList.Row row = this.optionsRows.get(slotIndex);
        int centerX = (this.left + this.right) / 2;
        int half = ROW_WIDTH / 2;
        row.buttonA.xPosition = centerX - half;
        row.buttonA.yPosition = y;
        row.buttonA.drawButton(this.valtherion, mouseX, mouseY);

        if (row.buttonB != null)
        {
            row.buttonB.xPosition = centerX + half - BUTTON_WIDTH;
            row.buttonB.yPosition = y;
            row.buttonB.drawButton(this.valtherion, mouseX, mouseY);
        }
    }

    /** Rend la liste clic-able : distribue les clics aux boutons de la ligne. */
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
    {
        int clickedSlot = (mouseY - this.top + (int) this.amountScrolled) / this.slotHeight;

        if (clickedSlot >= 0 && clickedSlot < this.optionsRows.size())
        {
            GuiOptionsRowList.Row row = this.optionsRows.get(clickedSlot);
            row.buttonA.mousePressed(this.valtherion, mouseX, mouseY);
        }
    }

    public static class Row
    {
        public final GuiOptionButton buttonA;
        public final GuiOptionButton buttonB;

        public Row(GuiOptionButton a, GuiOptionButton b)
        {
            this.buttonA = a;
            this.buttonB = b;
        }
    }
}