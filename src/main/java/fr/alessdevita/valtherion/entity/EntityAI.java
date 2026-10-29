package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.ai.pathfinding.Node;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;

import java.util.List;
import java.util.Random;

public class EntityAI {

    // Entité associée à cette IA
    private final LivingEntity entity;

    // Monde dans lequel l'entité existe
    private final World world;

    // Pathfinder pour calculer les chemins
    private final Pathfinder pathfinder;

    // Cible actuelle de l'entité (pour la poursuite ou l'attaque)
    private LivingEntity target;

    // Distance de détection (pour détecter les joueurs ou autres entités)
    private float detectionRange;

    // Distance d'attaque (pour les entités hostiles)
    private float attackRange;

    // Distance de fuite (pour les entités passives)
    private float fleeRange;

    // Vitesse de déplacement
    private float speed;

    // État de l'IA
    private AIState currentState;

    // Générateur aléatoire pour les décisions
    private final Random random;

    // Chemin actuel (pour le déplacement)
    private List<Node> currentPath;
    private int currentPathIndex;

    /**
     * Énumération des états possibles de l'IA.
     */
    public enum AIState {
        IDLE,       // Inactif
        WANDER,     // Errance aléatoire
        CHASE,      // Poursuite d'une cible
        ATTACK,     // Attaque d'une cible
        FLEE,       // Fuite d'une menace
        PATROL,     // Patrouille entre des points
        RETURN      // Retour à une position de départ
    }

    /**
     * Constructeur de l'IA de l'entité.
     * @param entity Entité associée.
     * @param world Monde dans lequel l'entité existe.
     * @param pathfinder Pathfinder pour calculer les chemins.
     */
    public EntityAI(LivingEntity entity, World world, Pathfinder pathfinder) {
        this.entity = entity;
        this.world = world;
        this.pathfinder = pathfinder;
        this.detectionRange = 10.0f;  // Distance de détection par défaut
        this.attackRange = 2.0f;     // Distance d'attaque par défaut
        this.fleeRange = 5.0f;       // Distance de fuite par défaut
        this.speed = 0.1f;           // Vitesse de déplacement par défaut
        this.currentState = AIState.IDLE;
        this.random = new Random();
        this.currentPath = null;
        this.currentPathIndex = 0;
    }

    /**
     * Met à jour l'IA de l'entité.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        if (entity == null || !entity.isAlive()) {
            return;
        }

        // Mettre à jour en fonction de l'état actuel
        switch (currentState) {
            case IDLE:
                updateIdle();
                break;
            case WANDER:
                updateWander();
                break;
            case CHASE:
                updateChase();
                break;
            case ATTACK:
                updateAttack();
                break;
            case FLEE:
                updateFlee();
                break;
            case PATROL:
                updatePatrol();
                break;
            case RETURN:
                updateReturn();
                break;
        }
    }

    /**
     * Met à jour l'état IDLE (inactif).
     */
    private void updateIdle() {
        // Rechercher une cible si l'entité est hostile
        if (entity.isHostile()) {
            LivingEntity newTarget = findNearestTarget();
            if (newTarget != null) {
                setTarget(newTarget);
                setState(AIState.CHASE);
            }
        }
    }

    /**
     * Met à jour l'état WANDER (errance aléatoire).
     */
    private void updateWander() {
        // Si le chemin est vide ou terminé, en générer un nouveau
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            generateRandomPath();
            currentPathIndex = 0;
        }

        // Suivre le chemin
        if (currentPath != null && !currentPath.isEmpty()) {
            Node node = currentPath.get(currentPathIndex);
            entity.moveTowards(node.getX(), node.getY(), node.getZ(), speed);

            // Passer au nœud suivant si on est assez proche
            if (entity.getDistanceTo(node.getX(), node.getY(), node.getZ()) < 1.0f) {
                currentPathIndex++;
            }
        }

        // Rechercher une cible si l'entité est hostile
        if (entity.isHostile()) {
            LivingEntity newTarget = findNearestTarget();
            if (newTarget != null) {
                setTarget(newTarget);
                setState(AIState.CHASE);
            }
        }
    }

    /**
     * Met à jour l'état CHASE (poursuite d'une cible).
     */
    private void updateChase() {
        if (target == null || !target.isAlive()) {
            setState(AIState.WANDER);
            return;
        }

        // Calculer la distance à la cible
        float distanceToTarget = (float) entity.getDistanceTo(target);

        // Si la cible est à portée d'attaque, passer en mode ATTACK
        if (distanceToTarget <= attackRange) {
            setState(AIState.ATTACK);
            return;
        }

        // Si la cible est trop loin, revenir en mode WANDER ou IDLE
        if (distanceToTarget > detectionRange) {
            setState(AIState.WANDER);
            return;
        }

        // Recalculer le chemin vers la cible
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            currentPath = pathfinder.findPath(
                    (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                    (int) target.getX(), (int) target.getY(), (int) target.getZ()
            );
            currentPathIndex = 0;
        }

        // Suivre le chemin vers la cible
        if (currentPath != null && !currentPath.isEmpty()) {
            Node node = currentPath.get(currentPathIndex);
            entity.moveTowards(node.getX(), node.getY(), node.getZ(), speed);

            if (entity.getDistanceTo(node.getX(), node.getY(), node.getZ()) < 1.0f) {
                currentPathIndex++;
            }
        }
    }

    /**
     * Met à jour l'état ATTACK (attaque d'une cible).
     */
    private void updateAttack() {
        if (target == null || !target.isAlive()) {
            setState(AIState.WANDER);
            return;
        }

        // Calculer la distance à la cible
        float distanceToTarget = (float) entity.getDistanceTo(target);

        // Si la cible est hors de portée d'attaque, revenir en mode CHASE
        if (distanceToTarget > attackRange) {
            setState(AIState.CHASE);
            return;
        }

        // Attaquer la cible
        entity.attack(target);
    }

    /**
     * Met à jour l'état FLEE (fuite d'une menace).
     */
    private void updateFlee() {
        if (target == null || !target.isAlive()) {
            setState(AIState.WANDER);
            return;
        }

        // Calculer la distance à la menace
        float distanceToThreat = (float) entity.getDistanceTo(target);

        // Si la menace est assez loin, revenir en mode WANDER
        if (distanceToThreat > fleeRange) {
            setState(AIState.WANDER);
            return;
        }

        // Calculer une position de fuite (opposée à la menace)
        double targetX = entity.getX() + (entity.getX() - target.getX()) * 2;
        double targetY = (double) entity.getY();
        double targetZ = entity.getZ() + (entity.getZ() - target.getZ()) * 2;

        // Recalculer le chemin vers la position de fuite
        if (currentPath == null || currentPathIndex >= currentPath.size()) {
            currentPath = pathfinder.findPath(
                    (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                    (int) targetX, (int) targetY, (int) targetZ
            );
            currentPathIndex = 0;
        }

        // Suivre le chemin de fuite
        if (currentPath != null && !currentPath.isEmpty()) {
            Node node = currentPath.get(currentPathIndex);
            entity.moveTowards(node.getX(), node.getY(), node.getZ(), speed);

            if (entity.getDistanceTo(node.getX(), node.getY(), node.getZ()) < 1.0f) {
                currentPathIndex++;
            }
        }
    }

    /**
     * Met à jour l'état PATROL (patrouille entre des points).
     */
    private void updatePatrol() {
        // Logique pour la patrouille (à implémenter selon tes besoins)
    }

    /**
     * Met à jour l'état RETURN (retour à une position de départ).
     */
    private void updateReturn() {
        // Logique pour le retour (à implémenter selon tes besoins)
    }

    /**
     * Trouve la cible la plus proche.
     * @return Cible la plus proche ou null si aucune cible n'est trouvée.
     */
    private LivingEntity findNearestTarget() {
        if (world == null) {
            return null;
        }

        LivingEntity nearestTarget = null;
        float nearestDistance = Float.MAX_VALUE;

        // Parcourir toutes les entités vivantes du monde
        for (LivingEntity livingEntity : world.getLivingEntities()) {
            // Ignorer l'entité elle-même
            if (livingEntity == entity) {
                continue;
            }

            // Vérifier si l'entité est une cible valide
            if (isValidTarget(livingEntity)) {
                float distance = (float) entity.getDistanceTo(livingEntity);
                if (distance < nearestDistance && distance <= detectionRange) {
                    nearestDistance = distance;
                    nearestTarget = livingEntity;
                }
            }
        }

        return nearestTarget;
    }

    /**
     * Vérifie si une entité est une cible valide.
     * @param target Entité à vérifier.
     * @return true si la cible est valide, false sinon.
     */
    private boolean isValidTarget(LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return false;
        }

        // Si l'entité est hostile, elle peut cibler les joueurs et les mobs passifs
        if (entity.isHostile()) {
            return target instanceof Player || !target.isHostile();
        }

        // Si l'entité est passive, elle peut fuir les joueurs et les mobs hostiles
        return target.isHostile();
    }

    /**
     * Génère un chemin aléatoire pour l'errance.
     */
    private void generateRandomPath() {
        if (pathfinder == null || world == null) {
            return;
        }

        // Générer une position aléatoire autour de l'entité
        int targetX = (int) (entity.getX() + (random.nextFloat() - 0.5f) * 20);
        int targetY = (int) entity.getY();
        int targetZ = (int) (entity.getZ() + (random.nextFloat() - 0.5f) * 20);

        currentPath = pathfinder.findPath(
                (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                targetX, targetY, targetZ
        );
    }

    // --- Getters et Setters ---

    public LivingEntity getEntity() {
        return entity;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getFleeRange() {
        return fleeRange;
    }

    public void setFleeRange(float fleeRange) {
        this.fleeRange = fleeRange;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public AIState getCurrentState() {
        return currentState;
    }

    public void setState(AIState state) {
        this.currentState = state;
    }

    public World getWorld() {
        return world;
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

}
