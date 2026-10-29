package fr.alessdevita.valtherion.client.gui.achievement.stats;

/**
 * StatBase — une statistique de base (compteur simple ou formatee).
 * Chaque stat possede un identifiant ("stat.xxx"), un nom affiche,
 * et un formatteur (INT, TIME, DISTANCE...).
 */
public class StatBase
{
    /** ID de la stat (ex : "stat.walkOneCm"). */
    public final String statId;
    /** Cle de traduction du nom. */
    private final String statName;
    /** Formateur de valeur (nom MC : statFormatter). */
    private final IStatType formatter;
    /** true = stat "indépendante" affichee en haut des stats generales. */
    private boolean isIndependent;

    public StatBase(String statId, String statName, IStatType formatter)
    {
        this.statId = statId;
        this.statName = statName;
        this.formatter = formatter;
        this.isIndependent = false;
    }

    public StatBase(String statId, String statName)
    {
        this(statId, statName, IStatType.simple);
    }

    /** Marque la stat comme independante (nom MC : setIndependent). */
    public StatBase initIndependentStat()
    {
        this.isIndependent = true;
        return this;
    }

    public String getStatName()
    {
        return this.statName;
    }

    public boolean isIndependent()
    {
        return this.isIndependent;
    }

    /** Formate la valeur pour l'affichage (nom MC : func_75968_a). */
    public String format(int value)
    {
        return this.formatter.format(value);
    }

    @Override
    public String toString()
    {
        return "StatBase{id=" + this.statId + "}";
    }

    /** Les formateurs standards (nom MC : IStatType et ses constantes). */
    public interface IStatType
    {
        String format(int value);

        /** Entier simple. */
        IStatType simple = new IStatType()
        {
            @Override
            public String format(int value)
            {
                return String.valueOf(value);
            }
        };

        /** Distance : cm -> affiche en blocs (division par 100). */
        IStatType distance = new IStatType()
        {
            @Override
            public String format(int value)
            {
                return String.format("%.2f blocs", (double) value / 100.0D);
            }
        };

        /** Duree : ticks -> affiche en minutes/heures (20 TPS). */
        IStatType time = new IStatType()
        {
            @Override
            public String format(int value)
            {
                double minutes = (double) value / 1200.0D; // 20 TPS * 60 s

                if (minutes < 1.0D)
                {
                    return String.valueOf((int) minutes + " s");
                }
                else
                {
                    return String.format("%.1f min", minutes);
                }
            }
        };

        /** Entier avec separateur de milliers. */
        IStatType breakStat = new IStatType()
        {
            @Override
            public String format(int value)
            {
                return String.format("%,d", value);
            }
        };
    }
}