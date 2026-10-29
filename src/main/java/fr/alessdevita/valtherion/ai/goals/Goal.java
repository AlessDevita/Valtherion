package fr.alessdevita.valtherion.ai.goals;

import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.quest.QuestState;

/**
 * Classe de base pour les objectifs des entités IA.
 * Un objectif représente une tâche ou un but que l'entité doit accomplir.
 * Exemples : suivre une entité, attaquer, fuir, explorer, etc.
 */
public abstract class Goal {

    // Logger pour les messages de debug
    protected final Logger logger;

    // Nom de l'objectif (pour le debug)
    protected final String name;

    // Entité associée à cet objectif
    protected LivingEntity entity;

    // Priorité de l'objectif (plus la valeur est élevée, plus la priorité est haute)
    protected int priority;

    // État de l'objectif
    public enum Status {
        ACTIVE,      // L'objectif est en cours d'exécution
        COMPLETED,   // L'objectif a été accompli
        FAILED,      // L'objectif a échoué
        INACTIVE     // L'objectif n'est pas actif
        ;
        public static final boolean NOT_STARTED = true;
        public static final boolean AVAILABLE = true;
    }

    protected Status status;

    /**
     * Constructeur de l'objectif.
     * @param name Nom de l'objectif.
     * @param entity Entité associée.
     * @param priority Priorité de l'objectif.
     */
    public Goal(String name, LivingEntity entity, int priority) {
        this.logger = new Logger("Goal: " + name);
        this.name = name;
        this.entity = entity;
        this.priority = priority;
        this.status = Status.INACTIVE;
    }

    /**
     * Active l'objectif.
     */
    public void activate() {
        if (status != Status.ACTIVE) {
            status = Status.ACTIVE;
            onActivate();
            logger.debug("Objectif activé : " + name);
        }
    }

    /**
     * Désactive l'objectif.
     */
    public void deactivate() {
        if (status == Status.ACTIVE) {
            status = Status.INACTIVE;
            onDeactivate();
            logger.debug("Objectif désactivé : " + name);
        }
    }

    /**
     * Met à jour l'objectif.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        if (status == Status.ACTIVE) {
            Status newStatus = onUpdate();
            if (newStatus != status) {
                status = newStatus;
                if (status == Status.COMPLETED) {
                    logger.debug("Objectif accompli : " + name);
                } else if (status == Status.FAILED) {
                    logger.debug("Objectif échoué : " + name);
                }
            }
        }
    }

    /**
     * Vérifie si l'objectif peut être activé.
     * @return true si l'objectif peut être activé, false sinon.
     */
    public boolean canActivate() {
        return status != Status.ACTIVE && onCanActivate();
    }

    /**
     * Réinitialise l'objectif.
     */
    public void reset() {
        status = Status.INACTIVE;
        onReset();
        logger.debug("Objectif réinitialisé : " + name);
    }

    // --- Méthodes à implémenter par les classes filles ---

    /**
     * Appelé lorsque l'objectif est activé.
     */
    protected abstract void onActivate();

    /**
     * Appelé lorsque l'objectif est désactivé.
     */
    protected abstract void onDeactivate();

    /**
     * Appelé à chaque mise à jour de l'objectif.
     * @return Nouveau statut de l'objectif.
     */
    protected abstract Status onUpdate();

    /**
     * Vérifie si l'objectif peut être activé.
     * @return true si l'objectif peut être activé.
     */
    protected abstract boolean onCanActivate();

    /**
     * Appelé lorsque l'objectif est réinitialisé.
     */
    protected abstract void onReset();

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Retourne une représentation textuelle de l'objectif.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", entity=" + (entity != null ? entity.getClass().getSimpleName() : "null") +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
