package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;

/**
 * Classe pour les zombies dans Valtherion.
 * Un mob hostile qui attaque les joueurs et peut infecter d'autres entités.
 */
public class Zombie extends HostileMob {

    // Indique si le zombie est en train de brûler (sous le soleil)
    private boolean isBurning;

    // Temps avant que le zombie ne brûle (en ticks)
    private int burnTime;
    private int currentBurnTime;

    // Indique si le zombie peut infecter d'autres entités
    private boolean canInfect;

    // Probabilité d'infecter une entité (0.0 à 1.0)
    private float infectProbability;

    // Type de zombie (ex: NORMAL, HUSK, DROWNED, etc.)
    private ZombieType zombieType;

    // Énumération des types de zombies
    public enum ZombieType {
        NORMAL,   // Zombie normal
        HUSK,     // Zombie du désert (brûle sous le soleil)
        DROWNED,  // Zombie noyé (apparaît dans l'eau)
        VILLAGER  // Zombie villageois
    }

    /**
     * Constructeur du zombie.
     * @param world Monde dans lequel le zombie existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Zombie(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z
        );

        // Configurer les propriétés spécifiques du zombie
        setName("Zombie");
        this.zombieType = ZombieType.NORMAL;
        this.isBurning = false;
        this.burnTime = 0;
        this.currentBurnTime = 0;
        this.canInfect = true;
        this.infectProbability = 0.2f; // 20% de chance d'infecter

        // Configurer les drops du zombie
        addDrop(new Drop("ROTTEN_FLESH", 0.8f, 1, 3));
        addDrop(new Drop("IRON_INGOT", 0.1f, 1, 1));

        // Configurer les propriétés en fonction du type de zombie
        configureZombieStats();
    }

    /**
     * Configure les statistiques du zombie en fonction de son type.
     */
    private void configureZombieStats() {
        switch (zombieType) {
            case HUSK:
                setMaxHealth(50.0f);
                setHealth(50.0f);
                setAttackDamage(10);
                setBaseSpeed(0.12f);
                isBurning = true; // Les Husk brûlent sous le soleil
                burnTime = 200; // 10 secondes (200 ticks)
                break;
            case DROWNED:
                setMaxHealth(45.0f);
                setHealth(45.0f);
                setAttackDamage(8);
                setBaseSpeed(0.1f);
                canInfect = false; // Les Drowned n'infectent pas
                break;
            case VILLAGER:
                setMaxHealth(35.0f);
                setHealth(35.0f);
                setAttackDamage(6);
                setBaseSpeed(0.1f);
                break;
            default: // NORMAL
                setMaxHealth(40.0f);
                setHealth(40.0f);
                setAttackDamage(8);
                setBaseSpeed(0.1f);
                break;
        }
    }

    /**
     * Met à jour le zombie.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour l'état de brûlure
        if (isBurning && currentBurnTime > 0) {
            currentBurnTime--;
            if (currentBurnTime == 0) {
                // Le zombie meurt s'il brûle trop longtemps
                damage(100.0f); // Dégâts suffisants pour tuer le zombie
            }
        }
    }

    /**
     * Attaque une cible.
     * @param target Cible à attaquer.
     */
    @Override
    public void attack(LivingEntity target) {
        super.attack(target);

        // Essayer d'infecter la cible
        if (canInfect && new java.util.Random().nextFloat() <= infectProbability) {
            infect(target);
        }
    }

    /**
     * Infecte une entité.
     * @param target Entité à infecter.
     */
    private void infect(LivingEntity target) {
        if (target == null || !(target instanceof Player)) {
            return;
        }

        Player player = (Player) target;
        // Logique pour infecter le joueur (ex: appliquer un effet de malus)
        System.out.println(getName() + " a infecté " + player.getPlayerName() + " !");
    }

    /**
     * Définit si le zombie brûle.
     * @param burning true si le zombie brûle, false sinon.
     */
    public void setBurning(boolean burning) {
        isBurning = burning;
        if (burning) {
            currentBurnTime = burnTime;
        }
    }

    /**
     * Méthode appelée lorsque le zombie meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getName() + " est mort et lâche de la chair pourrie et peut-être du fer.");
    }

    // --- Getters et Setters ---

    public boolean isBurning() {
        return isBurning;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public boolean canInfect() {
        return canInfect;
    }

    public void setCanInfect(boolean canInfect) {
        this.canInfect = canInfect;
    }

    public float getInfectProbability() {
        return infectProbability;
    }

    public void setInfectProbability(float infectProbability) {
        this.infectProbability = infectProbability;
    }

    public ZombieType getZombieType() {
        return zombieType;
    }

    public void setZombieType(ZombieType zombieType) {
        this.zombieType = zombieType;
        configureZombieStats();
    }

    /**
     * Retourne une représentation textuelle du zombie.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Zombie{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", zombieType=" + zombieType +
                ", isBurning=" + isBurning +
                '}';
    }
}