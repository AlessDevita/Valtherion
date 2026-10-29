package fr.alessdevita.valtherion.world;

/**
 * Classe pour gérer le temps du monde dans Valtherion.
 * Gère les cycles jour/nuit, les saisons, et les événements temporels.
 */
public class WorldTime {

    // Temps actuel du monde en ticks (1 tick = 1 seconde)
    private long currentTime;

    // Temps total écoulé depuis la création du monde (en ticks)
    private long totalTime;

    // Heure actuelle du monde (0-23)
    private int hour;

    // Minute actuelle du monde (0-59)
    private int minute;

    // Seconde actuelle du monde (0-59)
    private int second;

    // Jour actuel du monde (1-30)
    private int day;

    // Mois actuel du monde (1-12)
    private int month;

    // Année actuelle du monde
    private int year;

    // Saison actuelle du monde
    private Season currentSeason;

    // Phase de la lune actuelle
    private MoonPhase currentMoonPhase;

    // Vitesse du temps (1.0 = temps réel, 2.0 = 2x plus rapide, etc.)
    private float timeSpeed;

    // Indique si le temps est en pause
    private boolean isPaused;

    // Énumération des saisons
    public enum Season {
        SPRING("Printemps", 3),   // Mars, avril, mai
        SUMMER("Été", 3),        // Juin, juillet, août
        AUTUMN("Automne", 3),     // Septembre, octobre, novembre
        WINTER("Hiver", 3)       // Décembre, janvier, février
        ;

        private final String displayName;
        private final int durationInMonths;

        Season(String displayName, int durationInMonths) {
            this.displayName = displayName;
            this.durationInMonths = durationInMonths;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getDurationInMonths() {
            return durationInMonths;
        }
    }

    // Énumération des phases de la lune
    public enum MoonPhase {
        NEW_MOON("Nouvelle Lune"),
        WAXING_CRESCENT("Premier Croissant"),
        FIRST_QUARTER("Premier Quartier"),
        WAXING_GIBBOUS("Lune Gibbeuse Croissante"),
        FULL_MOON("Pleine Lune"),
        WANING_GIBBOUS("Lune Gibbeuse Décroissante"),
        LAST_QUARTER("Dernier Quartier"),
        WANING_CRESCENT("Dernier Croissant")
        ;

        private final String displayName;

        MoonPhase(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Durée d'un jour en ticks (24 heures)
    private static final long DAY_DURATION = 24 * 60 * 60;

    // Durée d'un mois en jours (30 jours)
    private static final int MONTH_DURATION = 30;

    // Durée d'une année en mois (12 mois)
    private static final int YEAR_DURATION = 12;

    // Durée d'une saison en mois (3 mois)
    private static final int SEASON_DURATION = 3;

    // Durée d'une phase de la lune en jours (environ 3.7 jours)
    private static final int MOON_PHASE_DURATION = 4;

    // Liste des observateurs du temps du monde
    private final java.util.List<WorldTimeListener> listeners;

    /**
     * Constructeur du temps du monde.
     */
    public WorldTime() {
        this.currentTime = 0;
        this.totalTime = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.day = 1;
        this.month = 1;
        this.year = 1;
        this.currentSeason = Season.SPRING;
        this.currentMoonPhase = MoonPhase.NEW_MOON;
        this.timeSpeed = 1.0f;
        this.isPaused = false;
        this.listeners = new java.util.ArrayList<>();

        updateTimeValues();
    }

    /**
     * Met à jour le temps du monde.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (isPaused) {
            return;
        }

        // Mettre à jour le temps actuel
        currentTime += (long) (deltaTime * timeSpeed);
        totalTime += (long) (deltaTime * timeSpeed);

        // Mettre à jour les valeurs de temps
        updateTimeValues();

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Met à jour les valeurs de temps (heure, minute, seconde, jour, mois, année, saison, phase de la lune).
     */
    private void updateTimeValues() {
        // Calculer l'heure, la minute et la seconde
        long totalSeconds = currentTime % DAY_DURATION;
        hour = (int) (totalSeconds / 3600);
        minute = (int) ((totalSeconds % 3600) / 60);
        second = (int) (totalSeconds % 60);

        // Calculer le jour, le mois et l'année
        long totalDays = currentTime / DAY_DURATION;
        day = (int) (totalDays % MONTH_DURATION) + 1;
        month = (int) ((totalDays / MONTH_DURATION) % YEAR_DURATION) + 1;
        year = (int) (totalDays / (MONTH_DURATION * YEAR_DURATION)) + 1;

        // Calculer la saison
        int seasonIndex = ((month - 1) / SEASON_DURATION) % 4;
        currentSeason = Season.values()[seasonIndex];

        // Calculer la phase de la lune
        long totalMoonDays = totalDays % (MOON_PHASE_DURATION * 8);
        int moonPhaseIndex = (int) (totalMoonDays / MOON_PHASE_DURATION) % 8;
        currentMoonPhase = MoonPhase.values()[moonPhaseIndex];
    }

    /**
     * Définit le temps actuel.
     * @param hour Heure.
     * @param minute Minute.
     * @param second Seconde.
     * @param day Jour.
     * @param month Mois.
     * @param year Année.
     */
    public void setTime(int hour, int minute, int second, int day, int month, int year) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.day = day;
        this.month = month;
        this.year = year;

        // Calculer le temps actuel en ticks
        currentTime = ((year - 1) * YEAR_DURATION * MONTH_DURATION +
                (month - 1) * MONTH_DURATION +
                (day - 1)) * DAY_DURATION +
                hour * 3600 +
                minute * 60 +
                second;

        updateTimeValues();
        notifyListeners();
    }

    /**
     * Définit la vitesse du temps.
     * @param timeSpeed Vitesse du temps (1.0 = temps réel).
     */
    public void setTimeSpeed(float timeSpeed) {
        this.timeSpeed = timeSpeed;
    }

    /**
     * Met le temps en pause.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Reprend le temps.
     */
    public void resume() {
        isPaused = false;
    }

    /**
     * Vérifie si le temps est en pause.
     * @return true si le temps est en pause, false sinon.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Vérifie si c'est le jour.
     * @return true si c'est le jour (6h-18h), false sinon.
     */
    public boolean isDay() {
        return hour >= 6 && hour < 18;
    }

    /**
     * Vérifie si c'est la nuit.
     * @return true si c'est la nuit (18h-6h), false sinon.
     */
    public boolean isNight() {
        return hour >= 18 || hour < 6;
    }

    /**
     * Vérifie si c'est l'aube.
     * @return true si c'est l'aube (4h-6h), false sinon.
     */
    public boolean isDawn() {
        return hour >= 4 && hour < 6;
    }

    /**
     * Vérifie si c'est le crépuscule.
     * @return true si c'est le crépuscule (18h-20h), false sinon.
     */
    public boolean isDusk() {
        return hour >= 18 && hour < 20;
    }

    /**
     * Vérifie si c'est midi.
     * @return true si c'est midi (12h), false sinon.
     */
    public boolean isNoon() {
        return hour == 12;
    }

    /**
     * Vérifie si c'est minuit.
     * @return true si c'est minuit (0h), false sinon.
     */
    public boolean isMidnight() {
        return hour == 0;
    }

    /**
     * Ajoute un observateur du temps du monde.
     * @param listener Observateur à ajouter.
     */
    public void addListener(WorldTimeListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du temps du monde.
     * @param listener Observateur à retirer.
     */
    public void removeListener(WorldTimeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs du temps du monde.
     */
    private void notifyListeners() {
        for (WorldTimeListener listener : listeners) {
            listener.onWorldTimeUpdate(this);
        }
    }

    // --- Getters ---

    public long getCurrentTime() {
        return currentTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public MoonPhase getCurrentMoonPhase() {
        return currentMoonPhase;
    }

    public float getTimeSpeed() {
        return timeSpeed;
    }

    /**
     * Retourne une représentation textuelle du temps du monde.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return String.format(
                "WorldTime{year=%d, month=%d, day=%d, hour=%02d:%02d:%02d, season=%s, moonPhase=%s}",
                year, month, day, hour, minute, second,
                currentSeason.getDisplayName(),
                currentMoonPhase.getDisplayName()
        );
    }

    /**
     * Retourne une représentation formatée de l'heure.
     * @return Chaîne de caractères.
     */
    public String getFormattedTime() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Retourne une représentation formatée de la date.
     * @return Chaîne de caractères.
     */
    public String getFormattedDate() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Retourne une représentation formatée de la date et de l'heure.
     * @return Chaîne de caractères.
     */
    public String getFormattedDateTime() {
        return getFormattedDate() + " " + getFormattedTime();
    }

    // --- Interface pour les observateurs du temps du monde ---
    public interface WorldTimeListener {
        void onWorldTimeUpdate(WorldTime worldTime);
    }
}