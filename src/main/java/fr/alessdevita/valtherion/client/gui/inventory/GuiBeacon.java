package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.inventory.ContainerBeacon;

/**
 * GuiBeacon — la balise : slot de paiement (item) + selection du
 * buff principal et secondaire via des boutons, bouton Confirmer.
 */
public class GuiBeacon extends GuiContainer
{
    private final ContainerBeacon beaconContainer;
    /** Buff principal selectionne (index). */
    private int primaryEffect = -1;
    /** Buff secondaire selectionne (index). */
    private int secondaryEffect = -1;
    private GuiButton doneBtn;
    private GuiButton primaryBtn;
    private GuiButton secondaryBtn;
    private FontRenderer fontRenderer;

    public GuiBeacon(ContainerBeacon container)
    {
        super(container);
        this.beaconContainer = container;
        this.ySize = 194; // plus haut que la GUI standard
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.doneBtn = new GuiButton(1, this.guiLeft + 152, this.guiTop + 107, 100, 20, "Confirmer");
        this.primaryBtn = new GuiButton(2, this.guiLeft + 62, this.guiTop + 47, 100, 20,
                "Effet principal");
        this.secondaryBtn = new GuiButton(3, this.guiLeft + 62, this.guiTop + 67, 100, 20,
                "Effet secondaire");
        this.buttonList.add(this.primaryBtn);
        this.buttonList.add(this.secondaryBtn);
        this.buttonList.add(this.doneBtn);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            // TODO : envoyer la selection au TileEntityBeacon
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
        else if (button.id == 2)
        {
            // Cycle sur les effets disponibles selon le niveau de la balise
            this.primaryEffect = (this.primaryEffect + 1) % 5;
            button.displayString = "Effet : " + this.primaryEffect;
        }
        else if (button.id == 3)
        {
            // Effet secondaire : seulement avec balise niveau 4
            this.secondaryEffect = this.secondaryEffect == -1 ? 0 : -1;
            button.displayString = this.secondaryEffect == -1
                    ? "Effet secondaire" : "Effet II";
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO : bind "textures/gui/container/beacon.png"
        this.drawRect(this.guiLeft, this.guiTop,
                this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xFFC6C6C6);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Balise", 8, 6, 0x404040);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}