package fr.alessdevita.valtherion.quest;

/**
 * Énumération des types de quêtes dans Valtherion.
 * Définit les catégories principales des quêtes.
 */
public enum QuestType {

    // --- Types de quêtes principaux ---

    /**
     * Quête de collecte : Récolter des objets ou des ressources.
     * Exemple : "Récolter 10 blés"
     */
    COLLECT("Collecte", "Récolter des objets ou des ressources."),

    /**
     * Quête d'élimination : Tuer des ennemis ou des boss.
     * Exemple : "Tuer 5 zombies"
     */
    KILL("Élimination", "Tuer des ennemis ou des boss."),

    /**
     * Quête d'exploration : Explorer une zone ou un lieu.
     * Exemple : "Explorer la forêt interdite"
     */
    EXPLORE("Exploration", "Explorer une zone ou un lieu."),

    /**
     * Quête de livraison : Apporter un objet à un PNJ ou un lieu.
     * Exemple : "Apporter une potion au villageois"
     */
    DELIVER("Livraison", "Apporter un objet à un PNJ ou un lieu."),

    /**
     * Quête de dialogue : Parler à un PNJ ou un groupe de PNJ.
     * Exemple : "Parler au maire du village"
     */
    TALK("Dialogue", "Parler à un PNJ ou un groupe de PNJ."),

    /**
     * Quête de craft : Fabriquer un objet spécifique.
     * Exemple : "Fabriquer une épée en diamant"
     */
    CRAFT("Craft", "Fabriquer un objet spécifique."),

    /**
     * Quête de construction : Construire une structure.
     * Exemple : "Construire une maison"
     */
    BUILD("Construction", "Construire une structure."),

    // --- Types de quêtes avancés ---

    /**
     * Quête de survie : Survivre pendant une durée ou dans des conditions spécifiques.
     * Exemple : "Survivre 10 minutes dans le désert"
     */
    SURVIVE("Survie", "Survivre pendant une durée ou dans des conditions spécifiques."),

    /**
     * Quête d'énigme : Résoudre une énigme ou un puzzle.
     * Exemple : "Résoudre l'énigme du temple"
     */
    PUZZLE("Énigme", "Résoudre une énigme ou un puzzle."),

    /**
     * Quête de quête secondaire : Quête qui fait partie d'une quête principale.
     * Exemple : "Trouver les ingrédients pour la potion"
     */
    SUB_QUEST("Quête secondaire", "Quête qui fait partie d'une quête principale."),

    /**
     * Quête de compétition : Battre un record ou un autre joueur.
     * Exemple : "Battre le record de temps sur le parcours"
     */
    COMPETITION("Compétition", "Battre un record ou un autre joueur."),

    /**
     * Quête d'événement : Quête liée à un événement spécial.
     * Exemple : "Participer au festival du village"
     */
    EVENT("Événement", "Quête liée à un événement spécial."),

    /**
     * Quête de découverte : Découvrir un secret ou une information.
     * Exemple : "Découvrir le secret de la forêt"
     */
    DISCOVER("Découverte", "Découvrir un secret ou une information.");

    // Nom affiché du type de quête
    private final String displayName;

    // Description du type de quête
    private final String description;

    /**
     * Constructeur de l'énumération.
     * @param displayName Nom affiché du type de quête.
     * @param description Description du type de quête.
     */
    QuestType(String displayName, String description) {
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
}