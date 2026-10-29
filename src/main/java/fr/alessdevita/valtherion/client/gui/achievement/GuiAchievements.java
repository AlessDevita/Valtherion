package fr.alessdevita.valtherion.client.gui.achievement;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;

/**
 * GuiAchievements — le panneau des hauts-faits : grille scrollable
 * (meme systeme de scroll map que la sélection de texture de MC),
 * 161x151 pixels, avec la grille des achievements et le zoom du
 * panneau par defaut (scale).
 */
public class GuiAchievements extends GuiScreen
{
    /** Deplacement horizontal/vertical du scroll (nom MC : field_146556_y/_z). */
    private float scrollX = 0.0F;
    private float scrollY = 0.0F;
    /** Position du scroll pendant le drag. */
    private float field_146557_v = 0.0F;
    private float field_146558_w = 0.0F;
    private int field_146563_u;
    private int field_146562_v;
    /** Zoom du panneau (echelle des cases). */
    private float scale = 1.0F;

    public GuiAchievements(GuiScreen parent)
    {
        this.parentScreen = parent;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 + 24, this.height / 2 + 74,
                80, 20, "Statistiques"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 104, this.height / 2 + 74,
                80, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        // Fond du panneau (texture achievement_page.png dans MC)
        int panelX = (this.width - GuiAchievements.ACHIEVEMENT_PANEL_WIDTH) / 2;
        int panelY = (this.height - GuiAchievements.ACHIEVEMENT_PANEL_HEIGHT) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawRect(panelX, panelY,
                panelX + ACHIEVEMENT_PANEL_WIDTH, panelY + ACHIEVEMENT_PANEL_HEIGHT,
                0xFFC6C6C6);
        Gui.drawRect(panelX + 4, panelY + 4,
                panelX + ACHIEVEMENT_PANEL_WIDTH - 4, panelY + ACHIEVEMENT_PANEL_HEIGHT - 4,
                0xFF212121);

        // Grille des achievements : affichage selon la liste statique
        // TODO : iterer Achievement.achievementList et dessiner chaque
        // AchievementBtn a sa position (achievementGridX/Y), l'icone et
        // le tooltip au survol de la souris.

        this.drawCenteredString(this.valtherion.fontRenderer, "Hauts-faits",
                this.width / 2, panelY - 12, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String s, int i, int i1, int i2) {
    }

    /** Dimensions du panneau (meme MC : 161x151... ici 252x202 approx). */
    public static final int ACHIEVEMENT_PANEL_WIDTH = 252;
    public static final int ACHIEVEMENT_PANEL_HEIGHT = 202;
}