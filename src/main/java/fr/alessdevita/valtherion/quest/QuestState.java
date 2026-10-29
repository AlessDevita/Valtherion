package fr.alessdevita.valtherion.quest;

/**
 * Énumération des états des quêtes dans Valtherion.
 * Définit les différents états possibles d'une quête.
 */
public enum QuestState {

    // --- États principaux ---

    /**
     * Non commencé : La quête n'a pas encore été démarrée.
     */
    NOT_STARTED("Non commencé", "La quête n'a pas encore été démarrée."),

    /**
     * En cours : La quête est en cours de réalisation.
     */
    IN_PROGRESS("En cours", "La quête est en cours de réalisation."),

    /**
     * Complétée : La quête a été terminée avec succès.
     */
    COMPLETED("Terminée", "La quête a été terminée avec succès."),

    /**
     * Échouée : La quête a échoué.
     */
    FAILED("Échouée", "La quête a échoué."),

    // --- États avancés ---

    /**
     * Disponible : La quête est disponible pour être démarrée.
     */
    AVAILABLE("Disponible", "La quête est disponible pour être démarrée."),

    /**
     * Verrouillée : La quête est verrouillée (nécessite des conditions spécifiques).
     */
    LOCKED("Verrouillée", "La quête est verrouillée."),

    /**
     * Abandonnée : La quête a été abandonnée par le joueur.
     */
    ABANDONED("Abandonnée", "La quête a été abandonnée par le joueur."),

    /**
     * En pause : La quête est temporairement en pause.
     */
    PAUSED("En pause", "La quête est temporairement en pause."),

    /**
     * Répétable : La quête peut être refaite après avoir été complétée.
     */
    REPEATABLE("Répétable", "La quête peut être refaite.");

    // Nom affiché de l'état
    private final String displayName;

    // Description de l'état
    private final String description;

    /**
     * Constructeur de l'énumération.
     * @param displayName Nom affiché de l'état.
     * @param description Description de l'état.
     */
    QuestState(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    // --- Getters ---

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Vérifie si la quête est active (en cours ou disponible).
     * @return true si la quête est active, false sinon.
     */
    public boolean isActive() {
        return this == IN_PROGRESS || this == AVAILABLE;
    }

    /**
     * Vérifie si la quête est terminée (complétée ou échouée).
     * @return true si la quête est terminée, false sinon.
     */
    public boolean isFinished() {
        return this == COMPLETED || this == FAILED;
    }
}