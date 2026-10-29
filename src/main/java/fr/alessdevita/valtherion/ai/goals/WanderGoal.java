package fr.alessdevita.valtherion.ai.goals;

import fr.alessdevita.valtherion.ai.pathfinding.Node;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

import java.util.List;
import java.util.Random;

/**
 * Objectif d'errance aléatoire pour les entités IA.
 * Permet à une entité de se déplacer aléatoirement dans le monde.
 */
public class WanderGoal extends Goal {

    // Rayon de déplacement aléatoire autour de la position de départ
    private float wanderRadius;

    // Vitesse de déplacement de l'entité
    private float speed;

    // Pathfinder pour calculer les chemins
    private Pathfinder pathfinder;

    // Chemin actuel (liste de nœuds)
    private List<Node> currentPath;

    // Index du nœud actuel dans le chemin
    private int currentPathIndex;

    // Position de départ pour l'errance
    private int startX, startY, startZ;

    // Position cible aléatoire
    private int targetX, targetY, targetZ;

    // Générateur aléatoire
    private final Random random;

    // Temps entre les changements de direction (en ticks)
    private int changeDirectionCooldown;
    private int cooldownCounter;

    /**
     * Constructeur de l'objectif d'errance.
     * @param entity Entité qui erre.
     * @param wanderRadius Rayon de déplacement aléatoire.
     * @param speed Vitesse de déplacement de l'entité.
     * @param priority Priorité de l'objectif.
     * @param pathfinder Pathfinder pour calculer les chemins.
     * @param changeDirectionCooldown Temps entre les changements de direction (en ticks).
     */
    public WanderGoal(
            LivingEntity entity,
            float wanderRadius,
            float speed,
            int priority,
            Pathfinder pathfinder,
            int changeDirectionCooldown
    ) {
        super("Wander", entity, priority);
        this.wanderRadius = wanderRadius;
        this.speed = speed;
        this.pathfinder = pathfinder;
        this.random = new Random();
        this.changeDirectionCooldown = changeDirectionCooldown;
        this.cooldownCounter = 0;

        // Initialiser la position de départ
        this.startX = (int) entity.getX();
        this.startY = (int) entity.getY();
        this.startZ = (int) entity.getZ();

        // Générer une première position cible
        generateRandomTarget();
    }

    /**
     * Appelé lorsque l'objectif est activé.
     */
    @Override
    protected void onActivate() {
        logger.debug(entity.getClass().getSimpleName() + " commence à errer.");
        generateRandomTarget();
        recalculatePath();
    }

    /**
     * Appelé lorsque l'objectif est désactivé.
     */
    @Override
    protected void onDeactivate() {
        logger.debug(entity.getClass().getSimpleName() + " arrête d'errer.");
        currentPath = null;
        currentPathIndex = 0;
    }

    /**
     * Met à jour l'objectif.
     * @return Nouveau statut de l'objectif.
     */
    @Override
    protected Status onUpdate() {
        // Décrémenter le cooldown
        if (cooldownCounter > 0) {
            cooldownCounter--;
        }

        // Si le chemin est vide ou si on a atteint la fin du chemin, générer une nouvelle cible
        if (currentPath == null || currentPathIndex >= currentPath.size() || cooldownCounter == 0) {
            generateRandomTarget();
            recalculatePath();
            cooldownCounter = changeDirectionCooldown;

            if (currentPath == null || currentPath.isEmpty()) {
                logger.debug("Aucun chemin d'errance trouvé.");
                return Status.FAILED;
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
        return true; // Toujours activable
    }

    /**
     * Réinitialise l'objectif.
     */
    @Override
    protected void onReset() {
        currentPath = null;
        currentPathIndex = 0;
        cooldownCounter = 0;
    }

    /**
     * Génère une position cible aléatoire dans le rayon de déplacement.
     */
    private void generateRandomTarget() {
        if (entity == null) {
            return;
        }

        // Générer des coordonnées aléatoires dans un cube autour de la position de départ
        float angle = random.nextFloat() * 2 * (float) Math.PI;
        float distance = random.nextFloat() * wanderRadius;

        // Calculer les coordonnées en 2D (X et Z)
        targetX = (int) (startX + Math.cos(angle) * distance);
        targetZ = (int) (startZ + Math.sin(angle) * distance);

        // Garder la même hauteur (Y) ou ajouter une légère variation
        targetY = startY + random.nextInt(3) - 1; // Variation de -1 à +1
    }

    /**
     * Recalcule le chemin vers la position cible aléatoire.
     */
    private void recalculatePath() {
        if (pathfinder == null || entity == null) {
            currentPath = null;
            return;
        }

        // Calculer le chemin depuis la position de l'entité vers la position cible
        int startX = (int) entity.getX();
        int startY = (int) entity.getY();
        int startZ = (int) entity.getZ();

        currentPath = pathfinder.findPath(startX, startY, startZ, targetX, targetY, targetZ);
        currentPathIndex = 0;

        if (currentPath != null && !currentPath.isEmpty()) {
            logger.debug("Nouveau chemin d'errance calculé avec " + currentPath.size() + " nœuds.");
        }
    }

    // --- Getters et Setters ---

    public float getWanderRadius() {
        return wanderRadius;
    }

    public void setWanderRadius(float wanderRadius) {
        this.wanderRadius = wanderRadius;
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

    public int getChangeDirectionCooldown() {
        return changeDirectionCooldown;
    }

    public void setChangeDirectionCooldown(int changeDirectionCooldown) {
        this.changeDirectionCooldown = changeDirectionCooldown;
    }
}
