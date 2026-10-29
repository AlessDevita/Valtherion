package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.inventory.ItemStack;

/**
 * GuiSleepMP — l'ecran "Vous êtes au lit / Quitter le lit" :
 * affiche le chat et le bouton pour se lever.
 */
public class GuiSleepMP extends GuiChat
{
    private final java.util.List<GuiButton> buttonList = new java.util.ArrayList<>();

    /**
     * Constructeur de l'interface de chat.
     *
     * @param valtherion Instance du jeu Valtherion.
     * @param width      Largeur de la fenêtre.
     * @param height     Hauteur de la fenêtre.
     */
    public GuiSleepMP(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40,
                200, 20, "Quitter le lit"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            // TODO : envoyer le packet "wake up" au serveur
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Le bouton est deja rendu par la buttonList du GuiChat
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}