package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * GuiSelectWorld — l'ecran de selection de monde : liste scrollable
 * des sauvegardes, boutons Jouer/Creer/Supprimer/Reactualiser,
 * et double-clic pour lancer le monde.
 */
public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    protected GuiScreen parentScreen;
    /** Liste des mondes detectes dans le dossier saves/. */
    protected List<String> worldList = Lists.newArrayList();
    /** Liste scrollable. */
    private GuiSlotWorldSelect worldSelectionList;
    /** Index selectionne. */
    private int selectedWorld = -1;
    /** Boutons d'action. */
    private GuiButton selectButton;
    private GuiButton deleteButton;

    public GuiSelectWorld(GuiScreen parent)
    {
        this.parentScreen = parent;
    }

    @Override
    public void displayProgressMessage(String message) {

    }

    @Override
    public void setLoadingProgress(int progress) {

    }

    @Override
    public void initGui()
    {
        this.worldSelectionList = new GuiSlotWorldSelect(this);
        this.buttonList.add(this.selectButton = new GuiButton(1, this.width / 2 - 154,
                this.height - 76, 150, 20, "Jouer"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 4, this.height - 76,
                150, 20, "Creer un nouveau monde"));
        this.buttonList.add(this.deleteButton = new GuiButton(3, this.width / 2 - 154,
                this.height - 52, 150, 20, "Supprimer"));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 4, this.height - 52,
                150, 20, "Reactialiser"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 100, this.height - 28,
                200, 20, "Annuler"));

        this.worldList.clear();
        if (this.valtherion != null && this.valtherion.getSaveFormat() != null)
            this.worldList.addAll(this.valtherion.getSaveFormat().getWorldList());
        if (this.selectedWorld >= this.worldList.size()) this.selectedWorld = -1;
        this.updateButtonStates();
    }

    /** Active/desactive les boutons selon la selection. */
    private void updateButtonStates()
    {
        this.selectButton.enabled = this.selectedWorld != -1;
        this.deleteButton.enabled = this.selectedWorld != -1;
    }

    /** Appele par la liste quand un monde est clique. */
    public void selectWorld(int index)
    {
        this.selectedWorld = index;
        this.updateButtonStates();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1 && this.selectedWorld != -1)
        {
            String folder = this.worldList.get(this.selectedWorld);
            fr.alessdevita.valtherion.world.storage.WorldInfo info = this.valtherion.getSaveFormat().getWorldInfo(folder);
            this.valtherion.launchIntegratedServer(folder, info == null ? folder : info.getWorldName(),
                    new fr.alessdevita.valtherion.world.WorldSettings(info == null ? 0L : info.getSeed()));
        }
        else if (button.id == 2)
        {
            this.valtherion.displayGuiScreen(new GuiCreateWorld(this));
        }
        else if (button.id == 3 && this.selectedWorld != -1)
        {
            // Confirmation avant suppression (GuiYesNo)
            String worldName = this.worldList.get(this.selectedWorld);
            this.valtherion.displayGuiScreen(new GuiYesNo((GuiYesNoCallback) this,
                    "Supprimer le monde", "'" + worldName + "' sera perdu definitivement !",
                    this.selectedWorld));
        }
        else if (button.id == 4)
        {
            // Rescanner saves/
            this.valtherion.displayGuiScreen(new GuiSelectWorld(this.parentScreen));
        }
        else if (button.id == 8)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (id >= 0 && id < this.worldList.size()) {
                String folder = this.worldList.get(id);
                this.valtherion.getSaveFormat().deleteWorldDirectory(folder);
                this.worldList.remove(id);
                this.selectedWorld = -1;
                this.updateButtonStates();
            }
        }

        this.valtherion.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.valtherion.fontRenderer, "Selectionner un monde",
                this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String selectionnerUnMonde, int i, int i1, int i2) {
        if (fontRenderer instanceof FontRenderer) ((FontRenderer)fontRenderer).drawCenteredString(selectionnerUnMonde, i, i1, i2);
    }

    /**
     * La liste scrollable interne des mondes (nom MC : GuiListWorldSelect).
     */
    public static class GuiSlotWorldSelect extends GuiSlot
    {
        private final GuiSelectWorld parent;

        public GuiSlotWorldSelect(GuiSelectWorld parent)
        {
            super(parent.valtherion, parent.width, parent.height, 32, parent.height - 90, 32, 32, 36);
            this.parent = parent;
        }

        @Override
        protected int getSize()
        {
            return this.parent.worldList.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
        {
            this.parent.selectWorld(slotIndex);
            if (doubleClick) this.parent.actionPerformed(this.parent.selectButton);
        }

        @Override
        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == this.parent.selectedWorld;
        }

        @Override
        protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
        {
            String name = this.parent.worldList.get(slotIndex);
            this.valtherion.fontRenderer.drawString(name, x + 2, y + 2, 0xFFFFFF);
        }
    }
}
