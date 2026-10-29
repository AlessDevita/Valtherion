package fr.alessdevita.valtherion.ai.goals.goals;

import fr.alessdevita.valtherion.ai.goals.Goal;
import fr.alessdevita.valtherion.ai.pathfinding.Node;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

import java.util.List;

/**
 * Objectif de poursuite pour les entités IA.
 * Permet à une entité de suivre une cible (joueur, mob, etc.) en utilisant le pathfinding.
 */
public class FollowGoal extends Goal {

    // Cible à suivre
    private LivingEntity target;

    // Distance idéale à maintenir entre l'entité et la cible
    private float followDistance;

    // Distance maximale pour abandonner la poursuite
    private float maxDistance;

    // Vitesse de déplacement de l'entité
    private float speed;

    // Pathfinder pour calculer les chemins
    private Pathfinder pathfinder;

    // Chemin actuel (liste de nœuds)
    private List<Node> currentPath;

    // Index du nœud actuel dans le chemin
    private int currentPathIndex;

    /**
     * Constructeur de l'objectif de poursuite.
     * @param entity Entité qui doit suivre la cible.
     * @param target Cible à suivre.
     * @param followDistance Distance idéale à maintenir.
     * @param maxDistance Distance maximale pour abandonner la poursuite.
     * @param speed Vitesse de déplacement de l'entité.
     * @param priority Priorité de l'objectif.
     * @param pathfinder Pathfinder pour calculer les chemins.
     */
    public FollowGoal(
            LivingEntity entity,
            LivingEntity target,
            float followDistance,
            float maxDistance,
            float speed,
            int priority,
            Pathfinder pathfinder
    ) {
        super("Follow " + (target != null ? target.getClass().getSimpleName() : "null"), entity, priority);
        this.target = target;
        this.followDistance = followDistance;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.pathfinder = pathfinder;
        this.currentPath = null;
        this.currentPathIndex = 0;
    }

    /**
     * Appelé lorsque l'objectif est activé.
     */
    @Override
    protected void onActivate() {
        logger.debug(
                entity.getClass().getSimpleName() + " commence à suivre " +
                        (target != null ? target.getClass().getSimpleName() : "null")
        );
        recalculatePath();
    }

    /**
     * Appelé lorsque l'objectif est désactivé.
     */
    @Override
    protected void onDeactivate() {
        logger.debug(
                entity.getClass().getSimpleName() + " arrête de suivre " +
                        (target != null ? target.getClass().getSimpleName() : "null")
        );
        currentPath = null;
        currentPathIndex = 0;
    }

    /**
     * Met à jour l'objectif.
     * @return Nouveau statut de l'objectif.
     */
    @Override
    protected Status onUpdate() {
        // Vérifier si la cible est valide
        if (target == null || !target.isAlive()) {
            logger.debug("Cible invalide ou morte. Objectif échoué.");
            return Status.FAILED;
        }

        // Calculer la distance entre l'entité et la cible
        float distanceToTarget = (float) entity.getDistanceTo(target);

        // Si la distance dépasse la distance maximale, abandonner
        if (distanceToTarget > maxDistance) {
            logger.debug("Cible trop éloignée. Objectif échoué.");
            return Status.FAILED;
        }

        // Si on est assez proche de la cible, l'objectif est accompli
        if (distanceToTarget <= followDistance) {
            return Status.COMPLETED;
        }

        // Si le chemin est vide ou si on a atteint la fin du chemin, recalculer
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            recalculatePath();
            if (currentPath == null || currentPath.isEmpty()) {
                logger.debug("Aucun chemin trouvé vers la cible.");
                return Goal.Status.FAILED;
            }
            currentPathIndex = 0;
        }

        // Suivre le chemin
        Node currentNode = currentPath.get(currentPathIndex);
        entity.moveTowards(
                currentNode.getX(),
                currentNode.getY(),
                currentNode.getZ(),
                speed
        );

        // Passer au nœud suivant si on est assez proche
        float distanceToNode = (float) entity.getDistanceTo(currentNode.getX(), currentNode.getY(), currentNode.getZ());
        if (distanceToNode <= 1.0f) { // Seuil de 1 bloc
            currentPathIndex++;
        }

        return Status.ACTIVE;
    }

    /**
     * Vérifie si l'objectif peut être activé.
     * @return true si l'objectif peut être activé.
     */
    @Override
    protected boolean onCanActivate() {
        return target != null && target.isAlive();
    }

    /**
     * Réinitialise l'objectif.
     */
    @Override
    protected void onReset() {
        currentPath = null;
        currentPathIndex = 0;
    }

    /**
     * Recalcule le chemin vers la cible.
     */
    private void recalculatePath() {
        if (pathfinder == null || entity == null || target == null) {
            currentPath = null;
            return;
        }

        // Calculer le chemin depuis la position de l'entité vers la position de la cible
        int startX = (int) entity.getX();
        int startY = (int) entity.getY();
        int startZ = (int) entity.getZ();
        int endX = (int) target.getX();
        int endY = (int) target.getY();
        int endZ = (int) target.getZ();

        currentPath = pathfinder.findPath(startX, startY, startZ, endX, endY, endZ);
        currentPathIndex = 0;

        if (currentPath != null && !currentPath.isEmpty()) {
            logger.debug("Nouveau chemin calculé avec " + currentPath.size() + " nœuds.");
        }
    }

    // --- Getters et Setters ---

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public float getFollowDistance() {
        return followDistance;
    }

    public void setFollowDistance(float followDistance) {
        this.followDistance = followDistance;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
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
