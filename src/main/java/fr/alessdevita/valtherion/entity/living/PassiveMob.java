package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.entity.EntityAI;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.inventory.Item;

import java.util.*;

/**
 * Classe pour les mobs passifs dans Valtherion.
 * Représente un mob qui ne attaque pas les joueurs et peut être interagi de manière pacifique.
 */
public abstract class PassiveMob extends LivingEntity {

    // Type de mob passif (ex: COW, PIG, SHEEP, CHICKEN, etc.)
    private PassiveMobType mobType;

    // IA du mob
    private EntityAI ai;

    // Distance de fuite (pour fuir les joueurs ou les mobs hostiles)
    private float fleeRange;

    // Distance de détection des dangers
    private float dangerDetectionRange;

    // Vitesse de déplacement
    private float speed;

    // Temps entre les changements de direction (en ticks)
    private int wanderCooldown;
    private int currentWanderCooldown;

    // État du mob (ex: IDLE, WANDER, FLEEING, EATING, etc.)
    private MobState currentState;

    // Liste des objets que le mob peut lâcher à sa mort
    private final List<Drop> drops;

    // Objet préféré pour nourrir le mob (ex: herbe pour les moutons)
    private String favoriteFood;

    // Temps de croissance (pour les mobs qui grandissent, comme les poulets)
    private int growthTime;
    private int currentGrowthTime;

    // Indique si le mob est adulte
    private boolean isAdult;

    // Indique si le mob peut être nourri
    private boolean canBeFed;

    // Indique si le mob peut être trais (ex: vache)
    private boolean canBeMilked;

    // Indique si le mob peut être tondu (ex: mouton)
    private boolean canBeSheared;

    public PassiveMob(EntityType entityType, World world, double x, double y, double z, Cow.PassiveMobType passiveMobType) {
        super();
        this.drops = getDrops();
    }

    // Énumération des types de mobs passifs
    public enum PassiveMobType {
        COW,        // Vache
        PIG,        // Cochon
        SHEEP,      // Mouton
        CHICKEN,    // Poulet
        RABBIT,     // Lapin
        HORSE,      // Cheval
        CAT,        // Chat
        DOG         // Chien
    }

    // Énumération des états du mob
    public enum MobState {
        IDLE,       // Inactif
        WANDER,     // Errance aléatoire
        FLEEING,    // Fuite d'un danger
        EATING,     // En train de manger
        SLEEPING,   // En train de dormir
        INTERACTING // En train d'interagir avec un joueur
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
     * Constructeur du mob passif.
     * @param type Type de l'entité.
     * @param world Monde dans lequel le mob existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param mobType Type de mob passif.
     */
    public PassiveMob(EntityType type, World world, double x, double y, double z, PassiveMobType mobType) {
        super(type, world, x, y, z);
        this.mobType = mobType;
        this.fleeRange = 8.0f; // Distance de fuite par défaut
        this.dangerDetectionRange = 16.0f; // Distance de détection des dangers
        this.speed = 0.05f;
        this.wanderCooldown = 100; // 5 secondes (100 ticks)
        this.currentWanderCooldown = 0;
        this.currentState = MobState.IDLE;
        this.drops = new ArrayList<>();
        this.isAdult = true;
        this.canBeFed = true;
        this.canBeMilked = false;
        this.canBeSheared = false;

        // Initialiser l'IA du mob
        Pathfinder pathfinder = new Pathfinder(world);
        this.ai = new EntityAI(this, world, pathfinder);
        this.ai.setDetectionRange(dangerDetectionRange);
        this.ai.setFleeRange(fleeRange);
        this.ai.setSpeed(speed);

        // Configurer les statistiques de base en fonction du type de mob
        configureMobStats();
    }

    /**
     * Configure les statistiques de base du mob en fonction de son type.
     */
    private void configureMobStats() {
        switch (mobType) {
            case COW:
                setMaxHealth(30.0f);
                setHealth(30.0f);
                setAttackDamage(0); // Les vaches n'attaquent pas
                setArmor(0);
                setBaseSpeed(0.05f);
                favoriteFood = "WHEAT";
                canBeMilked = true;
                // Ajouter des drops pour la vache
                drops.add(new Drop("LEATHER", 0.8f, 1, 3));
                drops.add(new Drop("BEEF", 0.5f, 1, 2));
                break;
            case PIG:
                setMaxHealth(20.0f);
                setHealth(20.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.07f);
                favoriteFood = "CARROT";
                // Ajouter des drops pour le cochon
                drops.add(new Drop("PORK", 0.8f, 1, 3));
                break;
            case SHEEP:
                setMaxHealth(25.0f);
                setHealth(25.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.06f);
                favoriteFood = "GRASS";
                canBeSheared = true;
                // Ajouter des drops pour le mouton
                drops.add(new Drop("WOOL", 0.8f, 1, 3));
                drops.add(new Drop("MUTTON", 0.5f, 1, 2));
                break;
            case CHICKEN:
                setMaxHealth(15.0f);
                setHealth(15.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.08f);
                favoriteFood = "SEEDS";
                isAdult = false;
                growthTime = 1200; // 60 secondes (1200 ticks)
                // Ajouter des drops pour le poulet
                drops.add(new Drop("FEATHER", 0.8f, 1, 3));
                drops.add(new Drop("CHICKEN", 0.3f, 1, 1));
                break;
            case RABBIT:
                setMaxHealth(10.0f);
                setHealth(10.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.1f);
                favoriteFood = "CARROT";
                // Ajouter des drops pour le lapin
                drops.add(new Drop("RABBIT_MEAT", 0.8f, 1, 2));
                drops.add(new Drop("RABBIT_FOOT", 0.2f, 1, 1));
                break;
            case HORSE:
                setMaxHealth(50.0f);
                setHealth(50.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.15f);
                favoriteFood = "APPLE";
                // Ajouter des drops pour le cheval
                drops.add(new Drop("LEATHER", 0.8f, 1, 2));
                break;
            default:
                setMaxHealth(20.0f);
                setHealth(20.0f);
                setAttackDamage(0);
                setArmor(0);
                setBaseSpeed(0.05f);
                break;
        }
    }

    /**
     * Met à jour le mob passif.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour l'IA du mob
        ai.update();

        // Mettre à jour les cooldowns
        if (currentWanderCooldown > 0) {
            currentWanderCooldown--;
        }

        // Mettre à jour la croissance (pour les mobs qui grandissent)
        if (!isAdult && growthTime > 0) {
            currentGrowthTime++;
            if (currentGrowthTime >= growthTime) {
                isAdult = true;
                System.out.println(getName() + " est maintenant adulte !");
            }
        }

        // Mettre à jour l'état du mob
        updateState();
    }

    /**
     * Met à jour l'état du mob.
     */
    private void updateState() {
        // Vérifier si le mob doit fuir
        LivingEntity danger = findNearestDanger();
        if (danger != null) {
            currentState = MobState.FLEEING;
            ai.setTarget(danger);
            ai.setState(EntityAI.AIState.FLEE);
        } else {
            // Si aucun danger, errer aléatoirement
            if (currentWanderCooldown == 0) {
                currentState = MobState.WANDER;
                ai.setState(EntityAI.AIState.WANDER);
                currentWanderCooldown = wanderCooldown;
            }
        }
    }

    /**
     * Trouve le danger le plus proche (joueur ou mob hostile).
     * @return Danger le plus proche ou null si aucun danger n'est trouvé.
     */
    private LivingEntity findNearestDanger() {
        if (getWorld() == null) {
            return null;
        }

        LivingEntity nearestDanger = null;
        float nearestDistance = Float.MAX_VALUE;

        // Parcourir toutes les entités vivantes du monde
        for (LivingEntity entity : getWorld().getLivingEntities()) {
            // Ignorer le mob lui-même
            if (entity == this) {
                continue;
            }

            // Vérifier si l'entité est un danger (joueur ou mob hostile)
            if (entity instanceof Player || (entity instanceof LivingEntity && ((LivingEntity) entity).isHostile())) {
                float distance = (float) getDistanceTo(entity);
                if (distance < nearestDistance && distance <= dangerDetectionRange) {
                    nearestDistance = distance;
                    nearestDanger = entity;
                }
            }
        }

        return nearestDanger;
    }

    /**
     * Nourrit le mob.
     * @param food Objet utilisé pour nourrir le mob.
     * @return true si le mob a été nourri, false sinon.
     */
    public boolean feed(Item food) {
        if (!canBeFed || food == null || !food.getId().equals(favoriteFood)) {
            return false;
        }

        // Logique pour nourrir le mob
        System.out.println(getName() + " a été nourri avec " + food.getName() + " !");

        // Effets de la nourriture (ex: accélérer la croissance)
        if (!isAdult) {
            currentGrowthTime += 200; // Réduire le temps de croissance de 10 secondes
            if (currentGrowthTime >= growthTime) {
                isAdult = true;
                System.out.println(getName() + " est maintenant adulte grâce à la nourriture !");
            }
        }

        return true;
    }

    /**
     * Traire le mob (si possible).
     * @return true si le mob a été trais, false sinon.
     */
    public boolean milk() {
        if (!canBeMilked || !isAdult) {
            return false;
        }

        // Logique pour traire le mob
        System.out.println(getName() + " a été trais !");
        // Exemple : getWorld().dropItem("MILK", getX(), getY(), getZ(), 1);
        return true;
    }

    /**
     * Tondre le mob (si possible).
     * @return true si le mob a été tondu, false sinon.
     */
    public boolean shear() {
        if (!canBeSheared || !isAdult) {
            return false;
        }

        // Logique pour tondre le mob
        System.out.println(getName() + " a été tondu !");
        // Exemple : getWorld().dropItem("WOOL", getX(), getY(), getZ(), 1);
        return true;
    }

    /**
     * Méthode appelée lorsque le mob meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();

        // Lâcher des objets
        dropItems();
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

    public PassiveMobType getMobType() {
        return mobType;
    }

    public void setMobType(PassiveMobType mobType) {
        this.mobType = mobType;
        configureMobStats();
    }

    public EntityAI getAi() {
        return ai;
    }

    public float getFleeRange() {
        return fleeRange;
    }

    public void setFleeRange(float fleeRange) {
        this.fleeRange = fleeRange;
        ai.setFleeRange(fleeRange);
    }

    public float getDangerDetectionRange() {
        return dangerDetectionRange;
    }

    public void setDangerDetectionRange(float dangerDetectionRange) {
        this.dangerDetectionRange = dangerDetectionRange;
        ai.setDetectionRange(dangerDetectionRange);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        ai.setSpeed(speed);
    }

    public int getWanderCooldown() {
        return wanderCooldown;
    }

    public void setWanderCooldown(int wanderCooldown) {
        this.wanderCooldown = wanderCooldown;
    }

    public MobState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MobState currentState) {
        this.currentState = currentState;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void addDrop(Drop drop) {
        drops.add(drop);
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public boolean canBeFed() {
        return canBeFed;
    }

    public void setCanBeFed(boolean canBeFed) {
        this.canBeFed = canBeFed;
    }

    public boolean canBeMilked() {
        return canBeMilked;
    }

    public void setCanBeMilked(boolean canBeMilked) {
        this.canBeMilked = canBeMilked;
    }

    public boolean canBeSheared() {
        return canBeSheared;
    }

    public void setCanBeSheared(boolean canBeSheared) {
        this.canBeSheared = canBeSheared;
    }

    /**
     * Retourne une représentation textuelle du mob passif.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PassiveMob{" +
                "type=" + mobType +
                ", name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", state=" + currentState +
                ", isAdult=" + isAdult +
                '}';
    }
}
