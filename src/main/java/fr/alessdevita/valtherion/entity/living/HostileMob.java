package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.entity.EntityAI;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;

import java.util.*;

/**
 * Classe pour les mobs hostiles dans Valtherion.
 * Représente un mob qui attaque les joueurs ou d'autres entités vivantes.
 */
public class HostileMob extends LivingEntity {

    // Type de mob hostile (ex: ZOMBIE, SKELETON, CREEPER, etc.)
    private HostileMobType mobType;

    // IA du mob
    private EntityAI ai;

    // Cible actuelle du mob (joueur ou autre entité vivante)
    private LivingEntity target;

    // Distance de détection des joueurs
    private float detectionRange;

    // Distance d'attaque
    private float attackRange;

    // Cooldown entre les attaques (en ticks)
    private int attackCooldown;
    private int currentAttackCooldown;

    // Dégâts de l'attaque spéciale (si applicable)
    private int specialAttackDamage;

    // Cooldown de l'attaque spéciale (en ticks)
    private int specialAttackCooldown;
    private int currentSpecialAttackCooldown;

    // Probabilité de déclencher une attaque spéciale (0.0 à 1.0)
    private float specialAttackProbability;

    // Expérience donnée au joueur après la mort du mob
    private int experienceReward;

    // Liste des objets que le mob peut lâcher à sa mort
    private final List<Drop> drops;

    // État du mob (ex: IDLE, CHASING, ATTACKING, FLEEING)
    private MobState currentState;

    // Énumération des types de mobs hostiles
    public enum HostileMobType {
        ZOMBIE,       // Zombie
        SKELETON,     // Squelette
        CREEPER,      // Creeper
        SPIDER,       // Araignée
        ENDERMAN,     // Enderman
        WITCH,        // Sorcière
        GHOST,        // Fantôme
        DRAGON        // Dragon
    }

    // Énumération des états du mob
    public enum MobState {
        IDLE,       // Inactif
        WANDER,     // Errance aléatoire
        CHASING,    // Poursuite d'une cible
        ATTACKING,  // Attaque d'une cible
        FLEEING,    // Fuite (si le mob a peur)
        SPECIAL_ATTACK // Attaque spéciale
    }

    // Classe pour les objets lâchés par le mob
    public static class Drop {
        private final String itemId;
        private final float probability;
        private final int minQuantity;
        private final int maxQuantity;

        public Drop(String itemId, float probability, int minQuantity, int maxQuantity) {
            this.itemId = itemId;
            this.probability = probability;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
        }

        public String getItemId() {
            return itemId;
        }

        public float getProbability() {
            return probability;
        }

        public int getMinQuantity() {
            return minQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        /**
         * Vérifie si cet objet doit être lâché.
         * @return true si l'objet doit être lâché, false sinon.
         */
        public boolean shouldDrop() {
            return new Random().nextFloat() <= probability;
        }

        /**
         * Génère une quantité aléatoire d'objets à lâcher.
         * @return Quantité d'objets.
         */
        public int getRandomQuantity() {
            return minQuantity + new Random().nextInt(maxQuantity - minQuantity + 1);
        }
    }

    /**
     * Constructeur du mob hostile.
     *
     * @param type  Type de l'entité.
     * @param world Monde dans lequel le mob existe.
     * @param x     Coordonnée X.
     * @param y     Coordonnée Y.
     * @param z     Coordonnée Z.
     */
    public HostileMob(EntityType type, World world, double x, double y, double z) {
        super(type, world, x, y, z);
        this.mobType = mobType;
        this.detectionRange = 16.0f; // Distance de détection par défaut
        this.attackRange = 2.0f;    // Distance d'attaque par défaut
        this.attackCooldown = 20;    // Cooldown de 1 seconde (20 ticks)
        this.currentAttackCooldown = 0;
        this.specialAttackDamage = 0;
        this.specialAttackCooldown = 60; // Cooldown de 3 secondes (60 ticks)
        this.currentSpecialAttackCooldown = 0;
        this.specialAttackProbability = 0.1f; // 10% de chance de déclencher une attaque spéciale
        this.experienceReward = 10;
        this.drops = new ArrayList<>();
        this.currentState = MobState.IDLE;
        this.setHostile(true);

        // Initialiser l'IA du mob
        Pathfinder pathfinder = new Pathfinder(world);
        this.ai = new EntityAI(this, world, pathfinder);
        this.ai.setDetectionRange(detectionRange);
        this.ai.setAttackRange(attackRange);
        this.ai.setSpeed(0.1f);

        // Configurer les statistiques de base en fonction du type de mob
        configureMobStats();
    }

    /**
     * Configure les statistiques de base du mob en fonction de son type.
     */
    private void configureMobStats() {
        switch (mobType) {
            case ZOMBIE:
                setMaxHealth(40.0f);
                setHealth(40.0f);
                setAttackDamage(8);
                setArmor(2);
                setBaseSpeed(0.1f);
                experienceReward = 10;
                // Ajouter des drops pour le zombie
                drops.add(new Drop("ROTTEN_FLESH", 0.8f, 1, 3));
                drops.add(new Drop("IRON_INGOT", 0.1f, 1, 1));
                break;
            case SKELETON:
                setMaxHealth(30.0f);
                setHealth(30.0f);
                setAttackDamage(10);
                setArmor(1);
                setBaseSpeed(0.12f);
                experienceReward = 15;
                // Ajouter des drops pour le squelette
                drops.add(new Drop("BONE", 0.8f, 1, 3));
                drops.add(new Drop("BOW", 0.2f, 1, 1));
                break;
            case CREEPER:
                setMaxHealth(50.0f);
                setHealth(50.0f);
                setAttackDamage(20);
                setArmor(0);
                setBaseSpeed(0.15f);
                specialAttackDamage = 40; // Dégâts de l'explosion
                specialAttackProbability = 0.3f; // 30% de chance d'exploser
                experienceReward = 25;
                // Ajouter des drops pour le creeper
                drops.add(new Drop("GUNPOWDER", 0.8f, 1, 2));
                break;
            case SPIDER:
                setMaxHealth(25.0f);
                setHealth(25.0f);
                setAttackDamage(6);
                setArmor(1);
                setBaseSpeed(0.18f);
                experienceReward = 12;
                // Ajouter des drops pour l'araignée
                drops.add(new Drop("STRING", 0.8f, 1, 3));
                drops.add(new Drop("SPIDER_EYE", 0.2f, 1, 1));
                break;
            case ENDERMAN:
                setMaxHealth(80.0f);
                setHealth(80.0f);
                setAttackDamage(15);
                setArmor(5);
                setBaseSpeed(0.2f);
                experienceReward = 30;
                // Ajouter des drops pour l'enderman
                drops.add(new Drop("ENDER_PEARL", 0.5f, 1, 1));
                break;
            default:
                setMaxHealth(20.0f);
                setHealth(20.0f);
                setAttackDamage(5);
                setArmor(0);
                setBaseSpeed(0.1f);
                break;
        }
    }

    /**
     * Met à jour le mob hostile.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour l'IA du mob
        ai.update();

        // Mettre à jour les cooldowns
        if (currentAttackCooldown > 0) {
            currentAttackCooldown--;
        }
        if (currentSpecialAttackCooldown > 0) {
            currentSpecialAttackCooldown--;
        }

        // Mettre à jour l'état du mob
        updateState();
    }

    /**
     * Met à jour l'état du mob.
     */
    private void updateState() {
        // Si le mob n'a pas de cible, chercher une cible
        if (target == null || !target.isAlive()) {
            target = findNearestTarget();
            if (target != null) {
                currentState = MobState.CHASING;
                ai.setTarget(target);
                ai.setState(EntityAI.AIState.CHASE);
            } else {
                currentState = MobState.WANDER;
                ai.setState(EntityAI.AIState.WANDER);
            }
        } else {
            // Si le mob a une cible, vérifier la distance
            float distanceToTarget = (float) getDistanceTo(target);

            if (distanceToTarget <= attackRange) {
                currentState = MobState.ATTACKING;
                ai.setState(EntityAI.AIState.ATTACK);

                // Attaquer la cible si le cooldown est terminé
                if (currentAttackCooldown == 0) {
                    attack(target);
                    currentAttackCooldown = attackCooldown;

                    // Déclencher une attaque spéciale avec une certaine probabilité
                    if (specialAttackDamage > 0 &&
                            currentSpecialAttackCooldown == 0 &&
                            new Random().nextFloat() <= specialAttackProbability) {
                        specialAttack(target);
                        currentSpecialAttackCooldown = specialAttackCooldown;
                    }
                }
            } else {
                currentState = MobState.CHASING;
                ai.setState(EntityAI.AIState.CHASE);
            }
        }
    }

    /**
     * Trouve la cible la plus proche.
     * @return Cible la plus proche ou null si aucune cible n'est trouvée.
     */
    private LivingEntity findNearestTarget() {
        if (getWorld() == null) {
            return null;
        }

        LivingEntity nearestTarget = null;
        float nearestDistance = Float.MAX_VALUE;

        // Parcourir toutes les entités vivantes du monde
        for (LivingEntity entity : getWorld().getLivingEntities()) {
            // Ignorer le mob lui-même
            if (entity == this) {
                continue;
            }

            // Vérifier si l'entité est une cible valide (ex: joueur)
            if (entity instanceof Player) {
                float distance = (float) getDistanceTo(entity);
                if (distance < nearestDistance && distance <= detectionRange) {
                    nearestDistance = distance;
                    nearestTarget = entity;
                }
            }
        }

        return nearestTarget;
    }

    /**
     * Attaque une cible.
     * @param target Cible à attaquer.
     */
    @Override
    public void attack(LivingEntity target) {
        if (!isAlive() || target == null || !target.isAlive()) {
            return;
        }

        // Infliger des dégâts à la cible
        target.damage(getAttackDamage());
    }

    /**
     * Attaque spéciale du mob.
     * @param target Cible de l'attaque spéciale.
     */
    public void specialAttack(LivingEntity target) {
        if (!isAlive() || target == null || !target.isAlive()) {
            return;
        }

        // Infliger des dégâts spéciaux à la cible
        target.damage(specialAttackDamage);

        // Effet spécial en fonction du type de mob
        switch (mobType) {
            case CREEPER:
                // Explosion
                System.out.println(getName() + " explose !");
                break;
            case ENDERMAN:
                // Téléportation
                System.out.println(getName() + " se téléporte !");
                break;
            default:
                // Attaque spéciale générique
                System.out.println(getName() + " utilise une attaque spéciale !");
                break;
        }
    }

    /**
     * Méthode appelée lorsque le mob meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();

        // Lâcher des objets
        dropItems();

        // Donner de l'expérience au joueur qui a tué le mob
        if (target != null && target instanceof Player) {
            Player player = (Player) target;
            player.addExperience(experienceReward);
        }
    }

    @Override
    protected void levelUp() {

    }

    /**
     * Lâche des objets à la mort du mob.
     */
    private void dropItems() {
        for (Drop drop : drops) {
            if (drop.shouldDrop()) {
                int quantity = drop.getRandomQuantity();
                // Logique pour lâcher les objets dans le monde
                // Exemple : getWorld().dropItem(drop.getItemId(), getX(), getY(), getZ(), quantity);
                System.out.println(getName() + " lâche " + quantity + " x " + drop.getItemId());
            }
        }
    }

    // --- Getters et Setters ---

    public HostileMobType getMobType() {
        return mobType;
    }

    public void setMobType(HostileMobType mobType) {
        this.mobType = mobType;
        configureMobStats();
    }

    public EntityAI getAi() {
        return ai;
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
        ai.setDetectionRange(detectionRange);
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
        ai.setAttackRange(attackRange);
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getSpecialAttackDamage() {
        return specialAttackDamage;
    }

    public void setSpecialAttackDamage(int specialAttackDamage) {
        this.specialAttackDamage = specialAttackDamage;
    }

    public int getSpecialAttackCooldown() {
        return specialAttackCooldown;
    }

    public void setSpecialAttackCooldown(int specialAttackCooldown) {
        this.specialAttackCooldown = specialAttackCooldown;
    }

    public float getSpecialAttackProbability() {
        return specialAttackProbability;
    }

    public void setSpecialAttackProbability(float specialAttackProbability) {
        this.specialAttackProbability = specialAttackProbability;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(int experienceReward) {
        this.experienceReward = experienceReward;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void addDrop(Drop drop) {
        drops.add(drop);
    }

    public MobState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MobState currentState) {
        this.currentState = currentState;
    }

    /**
     * Retourne une représentation textuelle du mob hostile.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "HostileMob{" +
                "type=" + mobType +
                ", name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", state=" + currentState +
                ", target=" + (target != null ? target.getName() : "null") +
                '}';
    }
}