package fr.alessdevita.valtherion.ai.behavior;

import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Comportement feuille pour les arbres de comportement.
 * Représente une action concrète (ex: attaquer, fuir).
 */
public abstract class LeafBehavior extends Behavior {

    /**
     * Constructeur du comportement feuille.
     * @param name Nom du comportement.
     */
    public LeafBehavior(String name) {
        super(name);
    }

    /**
     * Exécute le comportement feuille.
     * @param entity Entité associée.
     * @return État du comportement.
     */
    @Override
    public abstract Status execute(LivingEntity entity);
}
