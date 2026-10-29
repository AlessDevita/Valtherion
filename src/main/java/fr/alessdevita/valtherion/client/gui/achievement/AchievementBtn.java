package fr.alessdevita.valtherion.client.gui.achievement;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.gui.GuiButton;
/**
 * AchievementBtn — un "slot" du panneau des hauts-faits : une case
 * de la grille scrollable, avec l'icone de l'achievement (rendu via
 * l'item texture) et l'etat obtenu/non obtenu.
 */
public class AchievementBtn extends GuiButton
{
    /** Haut-fait affiche par ce bouton (null = case vide). */
    private final Achievement achievement;
    /** Etat d'acquisition (stocke / affiche la date). */
    private final String field_146502_z;
    /** Icone chargee depuis les textures d'items. */
    private final String iconTexture;
    private boolean visible;
    private boolean hovered;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;

    public AchievementBtn(int buttonId, int x, int y, Achievement achievement,
                          String statString, String icon)
    {
        super(buttonId, x, y, 22, 22, "");
        this.achievement = achievement;
        this.field_146502_z = statString;
        this.iconTexture = icon;
    }

    /** Le haut-fait est-il obtenu ? (grille grise sinon). */
    public Achievement getAchievement()
    {
        return this.achievement;
    }

    /** Tooltip : titre + description (dessine par GuiAchievements). */
    public String getStatString()
    {
        return this.field_146502_z;
    }

    public String getIconTexture()
    {
        return this.iconTexture;
    }

    @Override
    public void drawButton(Valtherion mc, int mouseX, int mouseY)
    {
        // Le rendu complet (cadre, icone, etat) est delegue a GuiAchievements :
        // ce bouton sert surtout de holder de position et de donnees.
        if (this.visible)
        {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
                    && mouseX < this.xPosition + this.width
                    && mouseY < this.yPosition + this.height;
        }
    }
}