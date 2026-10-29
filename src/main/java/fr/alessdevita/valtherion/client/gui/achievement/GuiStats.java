package fr.alessdevita.valtherion.client.gui.achievement;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;
import fr.alessdevita.valtherion.client.gui.GuiSlot;
import fr.alessdevita.valtherion.client.gui.achievement.stats.StatisticsManager;

/**
 * GuiStats — l'ecran des statistiques : onglets generaux/blocs/items/mobs,
 * liste scrollable des stats avec leur valeur formatee.
 */
public class GuiStats extends GuiScreen
{
    /** Onglets de l'ecran des stats (meme ordre que MC). */
    private static final String[] statTabs = new String[]
            {
                    "Generales", "Blocs", "Objets", "Mobs"
            };
    /** Onglet selectionne. */
    private int selectedTab = 0;
    /** Liste scrollable des stats visibles. */
    private GuiSlotStats statsList;
    /** Le gestionnaire de stats cote client. */
    private final StatisticsManager statsManager;

    public GuiStats(GuiScreen parent, StatisticsManager statsManager)
    {
        this.parentScreen = parent;
        this.statsManager = statsManager;
    }

    public GuiStats(GuiScreen parent)
    {
        this(parent, null); // TODO : recuperer le StatisticsManager du joueur
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.statsList = new GuiSlotStats(this);

        // Boutons d'onglets
        for (int i = 0; i < statTabs.length; ++i)
        {
            this.buttonList.add(new GuiButton(100 + i, this.width / 2 - 150 + i * 78,
                    this.height - 28, 74, 20, statTabs[i]));
        }

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 52,
                200, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id >= 100 && button.id < 100 + statTabs.length)
        {
            this.selectedTab = button.id - 100;
        }
        else if (button.id == 200)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.statsList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.valtherion.fontRenderer, "Statistiques",
                this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String statistiques, int i, int i1, int i2) {
    }

    /** La liste interne des stats de l'onglet courant. */
    public static class GuiSlotStats extends GuiSlot
    {
        private final GuiStats parent;

        public GuiSlotStats(GuiStats parent)
        {
            super(parent.valtherion, parent.width, parent.height, 32, parent.height - 64, 32, 32, 12);
            this.parent = parent;
        }

        @Override
        protected int getSize()
        {
            // TODO : retourner le nombre de stats de l'onglet selectionne
            return 0;
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
            // TODO : dessiner "Nom de la stat" a gauche et la valeur formatee a droite
            // (StatBase.getStatName / statsManager.readStat(stat))
        }
    }
}