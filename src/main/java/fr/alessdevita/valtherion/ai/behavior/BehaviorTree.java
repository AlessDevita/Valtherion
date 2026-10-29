package fr.alessdevita.valtherion.ai.behavior;

import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Arbre de comportement pour les entités IA.
 * Permet de créer des hiérarchies de comportements (ex: Sequence, Selector).
 */
public class BehaviorTree {

    // Logger pour les messages de debug
    private final Logger logger;

    // Racine de l'arbre de comportement
    private CompositeBehavior root;

    // Liste de tous les comportements dans l'arbre
    private final List<Behavior> behaviors;

    /**
     * Constructeur de l'arbre de comportement.
     */
    public BehaviorTree() {
        this.logger = new Logger("BehaviorTree");
        this.behaviors = new ArrayList<>();
    }

    /**
     * Définit la racine de l'arbre de comportement.
     * @param root Comportement racine.
     */
    public void setRoot(CompositeBehavior root) {
        this.root = root;
    }

    /**
     * Ajoute un comportement à l'arbre.
     * @param behavior Comportement à ajouter.
     */
    public void addBehavior(Behavior behavior) {
        if (behavior == null) {
            logger.warn("Impossible d'ajouter un comportement nul.");
            return;
        }

        if (!behaviors.contains(behavior)) {
            behaviors.add(behavior);
            logger.debug("Comportement ajouté : " + behavior.getClass().getSimpleName());
        }
    }

    /**
     * Supprime un comportement de l'arbre.
     * @param behavior Comportement à supprimer.
     */
    public void removeBehavior(Behavior behavior) {
        if (behavior == null) {
            logger.warn("Impossible de supprimer un comportement nul.");
            return;
        }

        behaviors.remove(behavior);
        logger.debug("Comportement supprimé : " + behavior.getClass().getSimpleName());
    }

    /**
     * Met à jour l'arbre de comportement pour une entité.
     * @param entity Entité associée à l'arbre.
     */
    public void update(LivingEntity entity) {
        if (root != null) {
            root.execute(entity);
        }
    }

    /**
     * Retourne la racine de l'arbre.
     * @return Comportement racine.
     */
    public CompositeBehavior getRoot() {
        return root;
    }

    /**
     * Retourne la liste de tous les comportements.
     * @return Liste des comportements.
     */
    public List<Behavior> getBehaviors() {
        return new ArrayList<>(behaviors);
    }
}
