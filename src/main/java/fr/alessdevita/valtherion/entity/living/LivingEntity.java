package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.economy.Wallet;

/**
 * Classe de base pour toutes les entités vivantes dans Valtherion.
 * Représente une entité avec une santé, un inventaire, et des capacités de base.
 */
public class LivingEntity extends Entity {

    // Santé actuelle de l'entité
    private float health;

    // Santé maximale de l'entité
    private float maxHealth;

    // Dégâts d'attaque de l'entité
    private int attackDamage;

    // Armure de l'entité (réduit les dégâts reçus)
    private int armor;

    // Inventaire de l'entité
    private Inventory inventory;

    // Portefeuille de l'entité (pour l'économie)
    private Wallet wallet;

    // État de l'entité (vivante ou morte)
    private boolean isAlive;

    // Indique si l'entité est hostile (ex: mobs, boss)
    private boolean isHostile;

    // Indique si l'entité est invulnérable
    private boolean isInvulnerable;

    // Temps d'invulnérabilité (en ticks)
    private int invulnerabilityTime;
    private int currentInvulnerabilityTime;

    // Niveau de l'entité
    private int level;

    // Expérience de l'entité
    private int experience;

    // Expérience nécessaire pour monter de niveau
    private int experienceToNextLevel;
    private float mana = 100.0F;
    private float maxMana = 100.0F;

    // Vitesse de déplacement de base
    private float baseSpeed;

    // Multiplicateur de vitesse (pour les effets temporaires)
    private float speedMultiplier;

    // Temps de régénération de la santé (en ticks)
    private int healthRegenTime;
    private int currentHealthRegenTime;

    // Quantité de santé régénérée à chaque intervalle
    private float healthRegenAmount;

    /**
     * Constructeur de l'entité vivante.
     * @param type Type de l'entité.
     * @param world Monde dans lequel l'entité existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public LivingEntity(EntityType type, World world, double x, double y, double z) {
        super();
        this.health = 100.0f;
        this.maxHealth = 100.0f;
        this.attackDamage = 10;
        this.armor = 0;
        this.inventory = new Inventory(36); // Inventaire avec 36 slots par défaut
        this.isAlive = true;
        this.isHostile = false;
        this.isInvulnerable = false;
        this.invulnerabilityTime = 0;
        this.currentInvulnerabilityTime = 0;
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.baseSpeed = 0.1f;
        this.speedMultiplier = 1.0f;
        this.healthRegenTime = 200; // 10 secondes (200 ticks)
        this.currentHealthRegenTime = 0;
        this.healthRegenAmount = 1.0f;
    }

    public LivingEntity() {
        super();
    }

    /** Constructor for named entities created by client and network code. */
    public LivingEntity(String id, String name, World world) {
        super(id, name);
        this.health = 100.0f;
        this.maxHealth = 100.0f;
        this.attackDamage = 1;
        this.armor = 0;
        this.inventory = new Inventory(36);
        this.isAlive = true;
        this.isHostile = false;
        this.isInvulnerable = false;
        this.invulnerabilityTime = 0;
        this.currentInvulnerabilityTime = 0;
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.baseSpeed = 0.1f;
        this.speedMultiplier = 1.0f;
        this.healthRegenTime = 200;
        this.currentHealthRegenTime = 0;
        this.healthRegenAmount = 1.0f;
        setWorld(world);
    }

    public LivingEntity(String entityType, World world, int x, int y, int z) {
        super(entityType, world, x, y, z);
    }

    /**
     * Met à jour l'entité vivante.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour l'invulnérabilité
        if (isInvulnerable) {
            currentInvulnerabilityTime--;
            if (currentInvulnerabilityTime <= 0) {
                isInvulnerable = false;
            }
        }

        // Mettre à jour la régénération de la santé
        if (isAlive && health < maxHealth) {
            currentHealthRegenTime++;
            if (currentHealthRegenTime >= healthRegenTime) {
                health = Math.min(health + healthRegenAmount, maxHealth);
                currentHealthRegenTime = 0;
            }
        }
    }

    @Override
    public void update(float deltaTime) {

        // Mettre à jour l'invulnérabilité
        if (isInvulnerable) {
            currentInvulnerabilityTime -= deltaTime;
            if (currentInvulnerabilityTime <= 0) {
                isInvulnerable = false;
            }
        }

        // Mettre à jour la régénération de la santé
        if (isAlive && health < maxHealth) {
            currentHealthRegenTime += deltaTime;
            if (currentHealthRegenTime >= healthRegenTime) {
                health = Math.min(health + healthRegenAmount, maxHealth);
                currentHealthRegenTime = 0;
            }
        }
    }

    /**
     * Inflige des dégâts à l'entité.
     *
     * @param damage Montant des dégâts.
     */
    public void damage(float damage) {
        if (!isAlive || isInvulnerable) {
            return;
        }

        // Réduire les dégâts en fonction de l'armure
        float reducedDamage = Math.max(1.0f, damage - armor);

        // Appliquer les dégâts
        health -= reducedDamage;

        // Vérifier si l'entité est morte
        if (health <= 0) {
            health = 0;
            isAlive = false;
            onDeath();
        } else {
            // Rendre l'entité temporairement invulnérable
            isInvulnerable = true;
            currentInvulnerabilityTime = invulnerabilityTime;
        }
    }

    /**
     * Soigne l'entité.
     * @param amount Montant de santé à restaurer.
     */
    public void heal(float amount) {
        if (!isAlive) {
            return;
        }
        health = Math.min(health + amount, maxHealth);
    }

    /**
     * Méthode appelée lorsque l'entité meurt.
     */
    public void onDeath() {
        // Logique à exécuter lorsque l'entité meurt
        // Exemple : Lâcher des objets, notifier les joueurs, etc.
    }

    /**
     * Attaque une autre entité.
     * @param target Cible à attaquer.
     */
    public void attack(LivingEntity target) {
        if (!isAlive || target == null || !target.isAlive()) {
            return;
        }

        // Infliger des dégâts à la cible
        target.damage(attackDamage);
    }

    /**
     * Ajoute de l'expérience à l'entité.
     * @param amount Montant d'expérience à ajouter.
     */
    public void addExperience(int amount) {
        if (!isAlive) {
            return;
        }

        experience += amount;

        // Vérifier si l'entité monte de niveau
        if (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    /**
     * Fait monter l'entité de niveau.
     */
    protected void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = (int) (experienceToNextLevel * 1.5); // Augmenter l'expérience nécessaire pour le prochain niveau

        // Augmenter les statistiques de l'entité
        maxHealth += 10;
        health = maxHealth;
        attackDamage += 2;
        armor += 1;

        // Notifier que l'entité est montée de niveau
        onLevelUp();
    }

    /**
     * Méthode appelée lorsque l'entité monte de niveau.
     */
    public void onLevelUp() {
        // Logique à exécuter lorsque l'entité monte de niveau
    }

    /**
     * Déplace l'entité vers une position.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void moveTo(double x, double y, double z) {
        setPosition(x, y, z);
    }

    /**
     * Déplace l'entité vers une position avec une vitesse.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param speed Vitesse de déplacement.
     */
    public void moveTowards(double x, double y, double z, float speed) {
        double dx = x - getX();
        double dy = y - getY();
        double dz = z - getZ();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance > 0) {
            double moveSpeed = speed * speedMultiplier * baseSpeed;
            move(dx / distance * moveSpeed, dy / distance * moveSpeed, dz / distance * moveSpeed);
        }
    }

    // --- Getters et Setters ---

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getMana() { return mana; }
    public float getMaxMana() { return maxMana; }
    public void setMana(float value) { mana = Math.max(0.0F, Math.min(maxMana, value)); }
    public void setMaxMana(float value) { maxMana = Math.max(0.0F, value); mana = Math.min(mana, maxMana); }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = Math.min(this.health, maxHealth);
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        if (!alive) {
            onDeath();
        }
    }

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean hostile) {
        isHostile = hostile;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        isInvulnerable = invulnerable;
    }

    public int getInvulnerabilityTime() {
        return invulnerabilityTime;
    }

    public void setInvulnerabilityTime(int invulnerabilityTime) {
        this.invulnerabilityTime = invulnerabilityTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public int getXP() { return experience; }
    public int getMaxXP() { return experienceToNextLevel; }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public void setExperienceToNextLevel(int experienceToNextLevel) {
        this.experienceToNextLevel = experienceToNextLevel;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public int getHealthRegenTime() {
        return healthRegenTime;
    }

    public void setHealthRegenTime(int healthRegenTime) {
        this.healthRegenTime = healthRegenTime;
    }

    public float getHealthRegenAmount() {
        return healthRegenAmount;
    }

    public void setHealthRegenAmount(float healthRegenAmount) {
        this.healthRegenAmount = healthRegenAmount;
    }

    /**
     * Retourne une représentation textuelle de l'entité vivante.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "LivingEntity{" +
                "type=" + getType() +
                ", name='" + getName() + '\'' +
                ", health=" + health + "/" + maxHealth +
                ", level=" + level +
                ", isAlive=" + isAlive +
                '}';
    }

    public void extinguish() {
    }
}
