package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * GuiScreenResourcePacks — l'ecran de selection des resource packs :
 * deux listes cote a cote (disponibles a gauche, actifs a droite),
 * fleches pour deplacer, et bouton Terminer qui recharge les ressources.
 */
public class GuiScreenResourcePacks extends GuiScreen
{
    /** Boutons fleches 2 (deplacer vers la droite), 1 (vers la gauche). */
    private static final int BUTTON_MOVE_RIGHT = 0;
    private static final int BUTTON_MOVE_LEFT = 1;
    private GuiResourcePackList availablePacksList;
    private GuiResourcePackList selectedPacksList;
    private final GuiScreen parentGuiScreen;

    public GuiScreenResourcePacks(GuiScreen parent)
    {
        this.parentGuiScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.availablePacksList = new GuiResourcePackList(this, this.valtherion,
                this.width, this.height, 32, this.height - 60);
        this.selectedPacksList = new GuiResourcePackList(this, this.valtherion,
                this.width, this.height, 32, this.height - 60);
        // TODO : remplir availablePacksList depuis dossier resourcepacks/
        // et selectedPacksList depuis mc.gameSettings.resourcePacks
        this.buttonList.add(new GuiButton(2, this.width / 2 + 54, this.height - 26, 200, 20,
                "Terminer"));
    }

    /** Appele par GuiResourcePackList quand un pack est clique. */
    public void resourcePackSelected(int slotIndex)
    {
        // TODO : deplacer le pack entre les deux listes selon la colonne
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2)
        {
            // Applique la nouvelle liste de packs : rechargement des ressources
            // TODO : mc.gameSettings.resourcePacks = liste ; mc.refreshResources()
            this.valtherion.displayGuiScreen(this.parentGuiScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, "Selection des resource packs",
                this.width / 2, 16, 0xFFFFFF);
        this.drawString(this.valtherion.fontRenderer, "Disponibles",
                this.width / 2 - 160, 26, 0xC0C0C0);
        this.drawString(this.valtherion.fontRenderer, "Selectionnes",
                this.width / 2 + 10, 26, 0xC0C0C0);
        this.availablePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedPacksList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawString(Object fontRenderer, String disponibles, int i, int i1, int i2) {

    }

    private void drawCenteredString(Object fontRenderer, String selectionDesResourcePacks, int i, int i1, int i2) {
    }
}