package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

/**
 * GuiChest — l'ecran du coffre : grille du coffre (3 rangees de 9)
 * + inventaire du joueur (3x9 + hotbar). Fonctionne pour les grands
 * coffres (6 rangees) grace au Container passe au constructeur.
 */
public class GuiChest extends GuiContainer
{
    /** Nombre de rangees du coffre (3 = simple, 6 = double). */
    private final int inventoryRows;
    /** Titre affiche ("Coffre" / nom du coffre). */
    private final String upperChestInventoryTitle;
    private FontRenderer fontRenderer;

    public GuiChest(Container container, int rows, String title)
    {
        super(container);
        this.inventoryRows = rows;
        this.upperChestInventoryTitle = title;
        // Un coffre simple est plus bas (166 - 54 = 112 + rows * 18...)
        this.ySize = 114 + this.inventoryRows * 18;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/generic_54.png" et dessiner
        // le fond avec drawTexturedModalRect : coffre (rangees x 9 slots)
        // puis inventaire joueur. Version rect temporaire :
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        // Titre du coffre en haut a gauche
        this.fontRenderer.drawString(this.upperChestInventoryTitle, 8, 6, 0x404040);
        // Titre "Inventaire" au-dessus de la grille joueur
        this.fontRenderer.drawString("Inventaire", 8, this.ySize - 96 + 2, 0x404040);
    }
}