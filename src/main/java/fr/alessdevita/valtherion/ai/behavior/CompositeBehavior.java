package fr.alessdevita.valtherion.ai.behavior;

import fr.alessdevita.valtherion.entity.living.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Comportement composite pour les arbres de comportement.
 * Peut contenir d'autres comportements (ex: Sequence, Selector).
 */
public abstract class CompositeBehavior extends Behavior {

    // Liste des comportements enfants
    protected final List<Behavior> children;

    /**
     * Constructeur du comportement composite.
     * @param name Nom du comportement.
     */
    public CompositeBehavior(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    /**
     * Ajoute un comportement enfant.
     * @param behavior Comportement à ajouter.
     */
    public void addChild(Behavior behavior) {
        if (behavior != null) {
            children.add(behavior);
        }
    }

    /**
     * Supprime un comportement enfant.
     * @param behavior Comportement à supprimer.
     */
    public void removeChild(Behavior behavior) {
        children.remove(behavior);
    }

    /**
     * Retourne la liste des comportements enfants.
     * @return Liste des comportements enfants.
     */
    public List<Behavior> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Exécute le comportement composite.
     * @param entity Entité associée.
     * @return État du comportement.
     */
    @Override
    public abstract Status execute(LivingEntity entity);
}