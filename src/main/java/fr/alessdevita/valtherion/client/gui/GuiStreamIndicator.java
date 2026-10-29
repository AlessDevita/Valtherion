package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

/**
 * GuiStreamIndicator — l'indicateur de streaming Twitch integré
 * (MC 1.7/1.8) : petit compteur de viewers / statut de stream.
 * Valtherion : conserve pour compatibilite de structure, le rendu
 * est desactive tant qu'aucun service de stream n'est branche.
 */
public class GuiStreamIndicator
{
    /** Statuts possibles (equivalents MC). */
    public static final int OFF = 0;
    public static final int PAUSED = 1;
    public static final int LIVE = 2;

    /** Statut courant. */
    private int streamIndicator = OFF;
    /** Nombre de viewers (affiche en direct). */
    private int viewers = 0;
    /** Nom de la chaine. */
    private String channelName = "";

    public void setStreamIndicator(int status)
    {
        this.streamIndicator = status;
    }

    public int getStreamIndicator()
    {
        return this.streamIndicator;
    }

    public void setViewers(int viewers)
    {
        this.viewers = viewers;
    }

    public void setChannelName(String name)
    {
        this.channelName = name;
    }

    /**
     * Rend l'indicateur dans le HUD (appele par GuiIngame si actif).
     * @param x coin gauche
     * @param y coin haut
     * @return largeur consommee (0 si indicateur OFF)
     */
    public int render(int x, int y)
    {
        if (this.streamIndicator == OFF)
        {
            return 0;
        }

        // Pastille verte (LIVE) ou orange (PAUSED)
        int dotColor = this.streamIndicator == LIVE ? 0xFF00FF00 : 0xFFFFA500;
        Gui.drawRect(x, y, x + 6, y + 6, dotColor);
        this.mcFont().drawStringWithShadow(
                this.streamIndicator == LIVE
                        ? this.channelName + " (" + this.viewers + ")"
                        : this.channelName,
                x + 9, y, 0xFFFFFF);
        return 9 + this.mcFont().getStringWidth(this.channelName) + 20;
    }

    private fr.alessdevita.valtherion.client.gui.FontRenderer mcFont()
    {
        return null; // TODO : recuperer le FontRenderer depuis GuiIngame
    }
}