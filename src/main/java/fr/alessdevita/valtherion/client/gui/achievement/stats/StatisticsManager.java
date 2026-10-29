package fr.alessdevita.valtherion.client.gui.achievement.stats;

import java.util.IdentityHashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import fr.alessdevita.valtherion.client.gui.achievement.Achievement;

/**
 * StatisticsManager — le stockage des valeurs de stats cote client :
 * map statId -> valeur, increment/retrait, et sauvegarde/relecture
 * (NBT ou fichier de sauvegarde via statsFile).
 */
public class StatisticsManager
{
    /** Valeurs des stats (nom MC : statsMap). */
    protected final Map<StatBase, Integer> statsMap = Maps.newIdentityHashMap();
    /** Stats min/max (pour les stats cumulables comme MC). */
    private final Map<StatBase, Integer> field_150761_a = new IdentityHashMap<StatBase, Integer>();

    public StatisticsManager()
    {
    }

    /** Incremente une stat (nom MC : increaseStat / func_150871_a). */
    public void increaseStat(StatBase stat, int amount)
    {
        if (stat != null)
        {
            int current = this.readStat(stat);
            this.statsMap.put(stat, current + amount);
        }
    }

    /** Incremente avec plafond optionnel (0 = illimite). */
    public void func_150871_a(StatBase stat, int amount, int cap)
    {
        if (cap == 0 || this.readStat(stat) < cap)
        {
            this.increaseStat(stat, amount);
        }
    }

    /** Lit la valeur d'une stat (0 si absente). */
    public int readStat(StatBase stat)
    {
        Integer value = this.statsMap.get(stat);
        return value == null ? 0 : value.intValue();
    }

    public Map<StatBase, Integer> getStatsMap()
    {
        return this.statsMap;
    }

    /** Reinitialise une stat. */
    public void resetStat(StatBase stat)
    {
        this.statsMap.remove(stat);
    }

    /**
     * Debloque un haut-fait : incremente et notifie l'observer
     * (nom MC : unlockAchievement / func_150873_a).
     */
    public void unlockAchievement(Achievement achievement)
    {
        if (!achievement.isUnlocked())
        {
            achievement.unlock();
            this.increaseStat(achievement, 1);
        }
    }

    /**
     * Sauvegarde des stats dans le dossier de sauvegarde.
     * (nom MC : saveStatsFile) — format libre : NBT ou JSON.
     */
    public void saveStatsFile(java.io.File statsFile)
    {
        // TODO : serialiser statsMap dans statsFile
        // format JSON simple : { "stat.walkOneCm": 123456, ... }
    }

    /**
     * Relit les stats depuis le fichier de sauvegarde.
     * (nom MC : readStatsFile)
     */
    public void readStatsFile(java.io.File statsFile)
    {
        // TODO : deserialiser et remplir statsMap
    }
}