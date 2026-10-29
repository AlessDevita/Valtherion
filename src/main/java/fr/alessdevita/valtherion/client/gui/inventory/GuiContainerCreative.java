package fr.alessdevita.valtherion.client.gui.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;
import fr.alessdevita.valtherion.client.gui.GuiTextField;
import fr.alessdevita.valtherion.item.ItemStack;

/**
 * GuiContainerCreative — l'inventaire du mode creatif : onglets
 * (Construction, Decoration, Combats, Divers, Nourriture, Outils...),
 * grille scrollable d'items, champ de recherche, barre d'items et
 * destruction d'items.
 */
public class GuiContainerCreative extends GuiScreen
{
    /** Index de l'onglet selectionne. */
    private int selectedTabIndex = 0;
    /** Liste des items affiches (filtree par onglet + recherche). */
    private final List<ItemStack> itemGrid = Lists.newArrayList();
    /** Scroll de la grille. */
    private float currentScroll = 0.0F;
    private boolean isScrolling;
    private boolean wasClicking;
    /** Champ de recherche. */
    private GuiTextField searchField;
    /** Nombre d'onglets. */
    private static final int TAB_COUNT = 7;

    public GuiContainerCreative()
    {
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.searchField = new GuiTextField(this.valtherion.fontRenderer,
                this.width / 2 - 100, this.height - 28, 200, 20);
        this.searchField.setFocused(false);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 108, this.height - 28,
                100, 20, "Retour au jeu"));
        this.refreshItems();
    }

    /** Recharge la grille selon l'onglet et la recherche. */
    private void refreshItems()
    {
        this.itemGrid.clear();
        // TODO : iterer Item.itemList, filtrer par onglet
        // (selectedTabIndex -> creativeTab de l'item) et par searchField.getText()
    }

    @Override
    public void keyTyped(int keyCode)
    {
        if (this.searchField.isFocused())
        {
            this.searchField.textboxKeyTyped(keyCode, (char) 0);
            this.refreshItems();
        }
        else
        {
            super.keyTyped(keyCode);
        }
    }

    /** Molette : scroll de la grille. */
    public void handleMouseScroll(double yoffset)
    {
        int max = Math.max(0, (this.itemGrid.size() + 8) / 9 - 5);
        this.currentScroll = (float) Math.min(max,
                Math.max(0.0F, this.currentScroll - (float) yoffset));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
    }

    /** Clic sur un item : le met dans le curseur (copie). */
    @Override
    public void handleMouseInput(int button, int action)
    {
        super.handleMouseInput(button, action);

        if (action == org.lwjgl.glfw.GLFW.GLFW_PRESS && button == 0)
        {
            // TODO : calculer la case cliquee dans la grille et copier
            // l'itemStack dans le curseur (mode creatif : copie infinie)
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        // Fond + onglets
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/creative_all.png" et
        // dessiner le fond 195x136 + les onglets (normaux + recherche)

        // Grille d'items : 9 colonnes x 5 rangees visibles
        int baseX = this.width / 2 - 90;
        int baseY = this.height / 2 - 60;
        int first = (int) (this.currentScroll * 9.0F);

        for (int row = 0; row < 5; ++row)
        {
            for (int col = 0; col < 9; ++col)
            {
                int index = first + row * 9 + col;

                if (index >= 0 && index < this.itemGrid.size())
                {
                    // TODO : rendu de l'item (RenderItem) dans la case
                    this.drawRect(baseX + col * 18, baseY + row * 18,
                            baseX + col * 18 + 16, baseY + row * 18 + 16, 0xFF8B8B8B);
                }
            }
        }

        this.searchField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public int getSelectedTabIndex()
    {
        return this.selectedTabIndex;
    }

    public void setSelectedTabIndex(int tab)
    {
        this.selectedTabIndex = tab;
        this.refreshItems();
    }
}