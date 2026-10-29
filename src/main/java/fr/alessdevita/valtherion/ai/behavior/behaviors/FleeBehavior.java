package fr.alessdevita.valtherion.ai.behavior.behaviors;

import fr.alessdevita.valtherion.ai.behavior.Behavior;
import fr.alessdevita.valtherion.ai.behavior.LeafBehavior;
import fr.alessdevita.valtherion.ai.goals.FleeGoal;
import fr.alessdevita.valtherion.ai.goals.Goal;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Comportement de fuite pour les arbres de comportement.
 * Permet à une entité de fuir une menace en utilisant un objectif de fuite (FleeGoal).
 */
public class FleeBehavior extends LeafBehavior {

    // Objectif de fuite associé à ce comportement
    private FleeGoal fleeGoal;

    // Menace à fuir
    private LivingEntity threat;

    // Distance à laquelle l'entité commence à fuir
    private float fleeDistance;

    // Distance maximale pour arrêter de fuir
    private float safeDistance;

    // Vitesse de déplacement
    private float speed;

    // Pathfinder pour calculer les chemins
    private Pathfinder pathfinder;

    /**
     * Constructeur du comportement de fuite.
     * @param threat Menace à fuir.
     * @param fleeDistance Distance à laquelle l'entité commence à fuir.
     * @param safeDistance Distance maximale pour arrêter de fuir.
     * @param speed Vitesse de déplacement.
     * @param pathfinder Pathfinder pour calculer les chemins.
     */
    public FleeBehavior(
            LivingEntity threat,
            float fleeDistance,
            float safeDistance,
            float speed,
            Pathfinder pathfinder
    ) {
        super("Flee from " + (threat != null ? threat.getClass().getSimpleName() : "null"));
        this.threat = threat;
        this.fleeDistance = fleeDistance;
        this.safeDistance = safeDistance;
        this.speed = speed;
        this.pathfinder = pathfinder;
    }

    /**
     * Exécute le comportement pour une entité.
     * @param entity Entité associée au comportement.
     * @return État du comportement (SUCCESS, FAILURE, ou RUNNING).
     */
    @Override
    public Behavior.Status execute(LivingEntity entity) {
        // Vérifier si l'entité ou la menace est nulle
        if (entity == null || threat == null) {
            return Behavior.Status.FAILURE;
        }

        // Si l'objectif de fuite n'existe pas encore, le créer
        if (fleeGoal == null || fleeGoal.getEntity() != entity) {
            fleeGoal = new FleeGoal(
                    entity,
                    threat,
                    fleeDistance,
                    safeDistance,
                    speed,
                    0, // Priorité (non utilisée ici, car gérée par le BehaviorTree)
                    pathfinder
            );
        }

        // Calculer la distance entre l'entité et la menace
        float distanceToThreat = (float) entity.getDistanceTo(threat);

        // Si la menace est trop proche, activer l'objectif de fuite
        if (distanceToThreat <= fleeDistance) {
            if (fleeGoal.getStatus() != Goal.Status.ACTIVE) {
                fleeGoal.activate();
            }
            fleeGoal.update();

            // Si l'objectif est accompli, retourner SUCCESS
            if (fleeGoal.getStatus() == Goal.Status.COMPLETED) {
                return Behavior.Status.SUCCESS;
            }

            // Si l'objectif a échoué, retourner FAILURE
            if (fleeGoal.getStatus() == Goal.Status.FAILED) {
                return Behavior.Status.FAILURE;
            }

            // Sinon, l'objectif est toujours en cours
            return Behavior.Status.RUNNING;
        }

        // Si la menace est hors de portée, désactiver l'objectif
        if (fleeGoal != null && fleeGoal.getStatus() == Goal.Status.ACTIVE) {
            fleeGoal.deactivate();
        }

        // Retourner SUCCESS si on n'a pas besoin de fuir
        return Behavior.Status.SUCCESS;
    }

    // --- Getters et Setters ---

    public FleeGoal getFleeGoal() {
        return fleeGoal;
    }

    public LivingEntity getThreat() {
        return threat;
    }

    public void setThreat(LivingEntity threat) {
        this.threat = threat;
    }

    public float getFleeDistance() {
        return fleeDistance;
    }

    public void setFleeDistance(float fleeDistance) {
        this.fleeDistance = fleeDistance;
    }

    public float getSafeDistance() {
        return safeDistance;
    }

    public void setSafeDistance(float safeDistance) {
        this.safeDistance = safeDistance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }
}
