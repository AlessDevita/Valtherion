package fr.alessdevita.valtherion.quest;

import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.world.World;

/**
 * Classe pour les objectifs d'une quête dans Valtherion.
 * Définit les conditions à remplir pour compléter une quête.
 */
public class QuestObjective {

    // Type de l'objectif
    private final QuestObjectiveType type;

    // Description de l'objectif
    private String description;

    // Cible de l'objectif (ex: entité à tuer, objet à collecter, etc.)
    private String targetId;

    // Quantité requise pour compléter l'objectif
    private int requiredQuantity;

    // Quantité actuelle (progression)
    private int currentQuantity;

    // Indique si l'objectif est complété
    private boolean isCompleted;

    // Monde associé à l'objectif (pour les vérifications)
    private World world;

    // Énumération des types d'objectifs
    public enum QuestObjectiveType {
        COLLECT_ITEM,      // Collecter un objet
        KILL_ENTITY,       // Tuer une entité
        TALK_TO_NPC,       // Parler à un PNJ
        REACH_LOCATION,    // Atteindre un lieu
        USE_ITEM,          // Utiliser un objet
        CRAFT_ITEM,        // Fabriquer un objet
        DELIVER_ITEM,      // Livrer un objet
        EXPLORE_AREA,      // Explorer une zone
        SURVIVE_DURATION,  // Survivre pendant une durée
        SOLVE_PUZZLE       // Résoudre une énigme
    }

    /**
     * Constructeur de l'objectif de quête.
     * @param type Type de l'objectif.
     * @param description Description de l'objectif.
     * @param targetId ID de la cible (ex: ID de l'objet ou de l'entité).
     * @param requiredQuantity Quantité requise.
     */
    public QuestObjective(QuestObjectiveType type, String description, String targetId, int requiredQuantity) {
        this.type = type;
        this.description = description;
        this.targetId = targetId;
        this.requiredQuantity = requiredQuantity;
        this.currentQuantity = 0;
        this.isCompleted = false;
    }

    /**
     * Met à jour la progression de l'objectif.
     * @param world Monde associé.
     */
    public void update(World world) {
        this.world = world;

        // Vérifier si l'objectif est déjà complété
        if (isCompleted) {
            return;
        }

        // Mettre à jour la progression en fonction du type d'objectif
        switch (type) {
            case COLLECT_ITEM:
                updateCollectItem();
                break;
            case KILL_ENTITY:
                updateKillEntity();
                break;
            case TALK_TO_NPC:
                updateTalkToNPC();
                break;
            case REACH_LOCATION:
                updateReachLocation();
                break;
            case USE_ITEM:
                updateUseItem();
                break;
            case CRAFT_ITEM:
                updateCraftItem();
                break;
            case DELIVER_ITEM:
                updateDeliverItem();
                break;
            case EXPLORE_AREA:
                updateExploreArea();
                break;
            case SURVIVE_DURATION:
                updateSurviveDuration();
                break;
            case SOLVE_PUZZLE:
                updateSolvePuzzle();
                break;
        }

        // Vérifier si l'objectif est complété
        if (currentQuantity >= requiredQuantity) {
            isCompleted = true;
        }
    }

    /**
     * Met à jour la progression pour un objectif de collecte d'objet.
     */
    private void updateCollectItem() {
        // Vérifier si le joueur a l'objet dans son inventaire
        // Exemple : Vérifier dans l'inventaire du joueur
        // À implémenter selon ton système d'inventaire
    }

    /**
     * Met à jour la progression pour un objectif de tuer une entité.
     */
    private void updateKillEntity() {
        // Vérifier si l'entité a été tuée
        // Exemple : Vérifier dans la liste des entités tuées
        // À implémenter selon ton système de combat
    }

    /**
     * Met à jour la progression pour un objectif de parler à un PNJ.
     */
    private void updateTalkToNPC() {
        // Vérifier si le joueur a parlé au PNJ
        // Exemple : Vérifier dans la liste des PNJ avec qui le joueur a interagi
        // À implémenter selon ton système de dialogue
    }

    /**
     * Met à jour la progression pour un objectif d'atteindre un lieu.
     */
    private void updateReachLocation() {
        // Vérifier si le joueur a atteint le lieu
        // Exemple : Vérifier la position du joueur
        // À implémenter selon ton système de monde
    }

    /**
     * Met à jour la progression pour un objectif d'utiliser un objet.
     */
    private void updateUseItem() {
        // Vérifier si le joueur a utilisé l'objet
        // Exemple : Vérifier dans la liste des objets utilisés
        // À implémenter selon ton système d'inventaire
    }

    /**
     * Met à jour la progression pour un objectif de fabriquer un objet.
     */
    private void updateCraftItem() {
        // Vérifier si le joueur a fabriqué l'objet
        // Exemple : Vérifier dans la liste des objets fabriqués
        // À implémenter selon ton système de craft
    }

    /**
     * Met à jour la progression pour un objectif de livrer un objet.
     */
    private void updateDeliverItem() {
        // Vérifier si le joueur a livré l'objet
        // Exemple : Vérifier dans la liste des objets livrés
        // À implémenter selon ton système de quête
    }

    /**
     * Met à jour la progression pour un objectif d'explorer une zone.
     */
    private void updateExploreArea() {
        // Vérifier si le joueur a exploré la zone
        // Exemple : Vérifier dans la liste des zones explorées
        // À implémenter selon ton système de monde
    }

    /**
     * Met à jour la progression pour un objectif de survivre pendant une durée.
     */
    private void updateSurviveDuration() {
        // Incrémenter la durée de survie
        // Exemple : Incrémenter currentQuantity toutes les secondes
        // À implémenter selon ton système de temps
    }

    /**
     * Met à jour la progression pour un objectif de résoudre une énigme.
     */
    private void updateSolvePuzzle() {
        // Vérifier si le joueur a résolu l'énigme
        // Exemple : Vérifier dans la liste des énigmes résolues
        // À implémenter selon ton système d'énigmes
    }

    /**
     * Incrémente la quantité actuelle.
     */
    public void incrementQuantity() {
        currentQuantity++;
    }

    /**
     * Incrémente la quantité actuelle d'une valeur spécifique.
     * @param amount Quantité à ajouter.
     */
    public void incrementQuantity(int amount) {
        currentQuantity += amount;
    }

    /**
     * Réinitialise la progression de l'objectif.
     */
    public void reset() {
        currentQuantity = 0;
        isCompleted = false;
    }

    // --- Getters et Setters ---

    public QuestObjectiveType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * Retourne la progression de l'objectif en pourcentage.
     * @return Pourcentage de progression.
     */
    public float getProgress() {
        return (float) currentQuantity / requiredQuantity;
    }

    /**
     * Retourne une représentation textuelle de l'objectif.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "QuestObjective{" +
                "type=" + type +
                ", description='" + description + '\'' +
                ", targetId='" + targetId + '\'' +
                ", currentQuantity=" + currentQuantity +
                "/" + requiredQuantity +
                ", isCompleted=" + isCompleted +
                '}';
    }
}