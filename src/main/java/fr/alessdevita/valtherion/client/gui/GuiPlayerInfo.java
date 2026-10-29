package fr.alessdevita.valtherion.client.gui;

/**
 * GuiPlayerInfo — entree d'un joueur dans la liste des joueurs connectes
 * (tab list). Holder de donnees simple, rempli par le serveur.
 */
public class GuiPlayerInfo
{
    /** Nom affiche. */
    public final String name;
    /** Score affiche a droite (response time en ms ou score). */
    public int responseTime;
    /** true = le joueur fait partie de l'equipe de l'utilisateur. */
    public boolean isReady;

    public GuiPlayerInfo(String name)
    {
        this.name = name;
        this.responseTime = 0;
        this.isReady = false;
    }

    /**
     * Nom MC : getName (func_178861_a).
     */
    public String getName()
    {
        return this.name;
    }
}