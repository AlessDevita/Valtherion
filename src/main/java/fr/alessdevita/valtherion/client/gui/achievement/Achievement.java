package fr.alessdevita.valtherion.client.gui.achievement;

import java.util.ArrayList;
import java.util.List;

import fr.alessdevita.valtherion.client.gui.achievement.stats.StatBase;

/**
 * Achievement — un haut-fait : stat derivee de StatBase avec position
 * dans la grille du panneau, prerequis (chaines de hauts-faits),
 * colonne speciale, et observer pour la notification toast.
 */
public class Achievement extends StatBase
{
    /** Tous les hauts-faits enregistres (nom MC : achievementList). */
    public static List<Achievement> achievementList = new ArrayList<Achievement>();
    /** Observer des deblocages (set par GuiAchievement). */
    public static IAchievementObserver observer;

    /** Position dans la grille du panneau (colonnes x, rangees y). */
    public final int achievementGridX;
    public final int achievementGridY;
    /** true = affiche dans la colonne speciale a droite (nom MC : isSpecial). */
    public boolean isSpecial;
    /** Haut-fait prerequis (null = racine). */
    public final Achievement parentAchievement;
    /** Description affichee dans le toast. */
    private final String achievementDescription;
    /** Texture de l'icone. */
    public final String theItemIcon;
    /** Id du haut-fait. */
    private boolean isUnlocked;

    public Achievement(String statId, String name, String description, int gridX, int gridY,
                       Achievement parent, String icon)
    {
        super(statId, name);
        this.achievementDescription = description;
        this.achievementGridX = gridX;
        this.achievementGridY = gridY;
        this.parentAchievement = parent;
        this.theItemIcon = icon;
        this.isSpecial = false;
        this.isUnlocked = false;
        achievementList.add(this);

        // Valide que le prerequis existe (comme MC : Validate)
        //Validate.notNull(icon);
    }

    /** Variante colonne speciale (nom MC : setSpecial). */
    public Achievement(String statId, String name, String description, int gridX, int gridY,
                       Achievement parent, String icon, boolean special)
    {
        this(statId, name, description, gridX, gridY, parent, icon);
        this.isSpecial = special;
        //Validate.notNull(icon);
    }

    /** Statistique derivee : le haut-fait compte le nombre d'obtentions. */
    public Achievement initIndependentStat()
    {
        super.initIndependentStat();
        return this;
    }

    /** Description (nom MC : getDescription). */
    public String getDescription()
    {
        return this.achievementDescription;
    }

    /** Enregistre l'observer de notification (nom MC : setNotificationObserver). */
    public static void setNotificationObserver(IAchievementObserver obs)
    {
        observer = obs;
    }

    /** Debloque le haut-fait et notifie l'observer (nom MC : unlock). */
    public void unlock()
    {
        if (!this.isUnlocked && observer != null)
        {
            this.isUnlocked = true;
            observer.notifyAchievementGet(this);
        }
        else
        {
            this.isUnlocked = true;
        }
    }

    public boolean isUnlocked()
    {
        return this.isUnlocked;
    }

    /**
     * Renvoie la profondeur de la chaine de prerequis (nom MC :
     * getDepth / countAch) — utilisee pour griser les hauts-faits
     * dont les prerequis ne sont pas debloques.
     */
    public int getDepth()
    {
        return this.parentAchievement == null ? 1 : this.parentAchievement.getDepth() + 1;
    }

    /** Interface du toast (implementee par GuiAchievement). */
    public interface IAchievementObserver
    {
        void notifyAchievementGet(Achievement achievement);
    }
}