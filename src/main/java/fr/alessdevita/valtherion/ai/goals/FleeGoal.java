package fr.alessdevita.valtherion.ai.goals;

import fr.alessdevita.valtherion.ai.pathfinding.Node;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

import java.util.List;

/**
 * Objectif de fuite pour les entités IA.
 * Permet à une entité de fuir une menace (joueur, mob hostile, etc.) en utilisant le pathfinding.
 */
public class FleeGoal extends Goal {

    // Menace à fuir
    private LivingEntity threat;

    // Distance à laquelle l'entité commence à fuir
    private float fleeDistance;

    // Distance maximale pour arrêter de fuir
    private float safeDistance;

    // Vitesse de déplacement de l'entité
    private float speed;

    // Pathfinder pour calculer les chemins
    private Pathfinder pathfinder;

    // Chemin actuel (liste de nœuds)
    private List<Node> currentPath;

    // Index du nœud actuel dans le chemin
    private int currentPathIndex;

    // Position de fuite (opposée à la menace)
    private int fleeX, fleeY, fleeZ;

    /**
     * Constructeur de l'objectif de fuite.
     * @param entity Entité qui fuit.
     * @param threat Menace à fuir.
     * @param fleeDistance Distance à laquelle l'entité commence à fuir.
     * @param safeDistance Distance maximale pour arrêter de fuir.
     * @param speed Vitesse de déplacement de l'entité.
     * @param priority Priorité de l'objectif.
     * @param pathfinder Pathfinder pour calculer les chemins.
     */
    public FleeGoal(
            LivingEntity entity,
            LivingEntity threat,
            float fleeDistance,
            float safeDistance,
            float speed,
            int priority,
            Pathfinder pathfinder
    ) {
        super(
                "Flee from " + (threat != null ? threat.getClass().getSimpleName() : "null"),
                entity,
                priority
        );
        this.threat = threat;
        this.fleeDistance = fleeDistance;
        this.safeDistance = safeDistance;
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
                entity.getClass().getSimpleName() + " commence à fuir " +
                        (threat != null ? threat.getClass().getSimpleName() : "null")
        );
        calculateFleePosition();
        recalculatePath();
    }

    /**
     * Appelé lorsque l'objectif est désactivé.
     */
    @Override
    protected void onDeactivate() {
        logger.debug(
                entity.getClass().getSimpleName() + " arrête de fuir " +
                        (threat != null ? threat.getClass().getSimpleName() : "null")
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
        // Vérifier si la menace est valide
        if (threat == null || !threat.isAlive()) {
            logger.debug("Menace invalide ou morte. Objectif accompli.");
            return Status.COMPLETED;
        }

        // Calculer la distance entre l'entité et la menace
        float distanceToThreat = (float) entity.getDistanceTo(threat);

        // Si la distance est supérieure à la distance de sécurité, l'objectif est accompli
        if (distanceToThreat > safeDistance) {
            return Status.COMPLETED;
        }

        // Si la distance est inférieure à la distance de fuite, recalculer la position de fuite
        if (distanceToThreat <= fleeDistance) {
            calculateFleePosition();
            recalculatePath();
        }

        // Si le chemin est vide ou si on a atteint la fin du chemin, recalculer
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            recalculatePath();
            if (currentPath == null || currentPath.isEmpty()) {
                logger.debug("Aucun chemin de fuite trouvé.");
                return Status.FAILED;
            }
            currentPathIndex = 0;
        }

        // Suivre le chemin de fuite
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
        return threat != null && threat.isAlive();
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
     * Calcule une position de fuite (opposée à la menace).
     */
    private void calculateFleePosition() {
        if (threat == null || entity == null) {
            return;
        }

        // Calculer la direction opposée à la menace
        float entityX = (float) entity.getX();
        float entityY = (float) entity.getY();
        float entityZ = (float) entity.getZ();
        float threatX = (float) threat.getX();
        float threatY = (float) threat.getY();
        float threatZ = (float) threat.getZ();

        // Calculer la direction de la menace vers l'entité
        float dirX = entityX - threatX;
        float dirY = entityY - threatY;
        float dirZ = entityZ - threatZ;

        // Normaliser la direction (optionnel, pour une fuite dans une direction précise)
        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        if (length > 0) {
            dirX /= length;
            dirY /= length;
            dirZ /= length;
        }

        // Calculer une position de fuite à une distance de sécurité
        float fleeMultiplier = safeDistance;
        fleeX = (int) (entityX + dirX * fleeMultiplier);
        fleeY = (int) (entityY + dirY * fleeMultiplier);
        fleeZ = (int) (entityZ + dirZ * fleeMultiplier);
    }

    /**
     * Recalcule le chemin vers la position de fuite.
     */
    private void recalculatePath() {
        if (pathfinder == null || entity == null) {
            currentPath = null;
            return;
        }

        // Calculer le chemin depuis la position de l'entité vers la position de fuite
        int startX = (int) entity.getX();
        int startY = (int) entity.getY();
        int startZ = (int) entity.getZ();

        currentPath = pathfinder.findPath(startX, startY, startZ, fleeX, fleeY, fleeZ);
        currentPathIndex = 0;

        if (currentPath != null && !currentPath.isEmpty()) {
            logger.debug("Nouveau chemin de fuite calculé avec " + currentPath.size() + " nœuds.");
        }
    }

    // --- Getters et Setters ---

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