package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.GuiButton;

/**
 * GuiInventory — l'inventaire du joueur : armure (4 slots a gauche),
 * figure du joueur, grille 2x2 de craft + resultat, inventaire 3x9
 * et hotbar. Le bouton potion ouvre l'onglet de recettes.
 */
public class GuiInventory extends GuiContainer
{
    /** True = un effet de potion est affiche (decale la grille). */
    private float oldMouseX;
    private float oldMouseY;
    /** Bouton vers les recettes (MC 1.8+). */
    private GuiButton buttonRecipeTab;
    private FontRenderer fontRenderer;

    public GuiInventory(ContainerPlayer container)
    {
        super(container);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
        this.buttonList.add(this.buttonRecipeTab = new GuiButton(0,
                this.guiLeft + this.xSize - 24, this.guiTop + 4, 20, 20, "R"));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        // TODO : si le joueur devient mort, ouvrir GuiGameOver
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
        // TODO : bind "textures/gui/container/inventory.png"
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);

        // TODO : rendu de la figure du joueur (le "model") dans le cadre,
        // oriente selon oldMouseX/oldMouseY comme MC :
        // rotateArmPitch = -(y - guiTop - height/2) / height * 180 ...
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            // TODO : ouvrir l'ecran de recettes si implemente
        }
        else
        {
            super.actionPerformed(button);
        }
    }
}