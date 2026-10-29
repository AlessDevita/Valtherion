package fr.alessdevita.valtherion.ai.behavior;

import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Classe de base pour les comportements dans l'arbre de comportement.
 * Tous les comportements doivent hériter de cette classe.
 */
public abstract class Behavior {

    // État du comportement
    public enum Status {
        SUCCESS,   // Le comportement a réussi
        FAILURE,   // Le comportement a échoué
        RUNNING    // Le comportement est en cours d'exécution
    }

    // Nom du comportement (pour le debug)
    private final String name;

    /**
     * Constructeur du comportement.
     * @param name Nom du comportement.
     */
    public Behavior(String name) {
        this.name = name;
    }

    /**
     * Exécute le comportement pour une entité.
     * @param entity Entité associée au comportement.
     * @return État du comportement (SUCCESS, FAILURE, ou RUNNING).
     */
    public abstract Status execute(LivingEntity entity);

    /**
     * Retourne le nom du comportement.
     * @return Nom du comportement.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne une représentation textuelle du comportement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Behavior{" + name + "}";
    }
}