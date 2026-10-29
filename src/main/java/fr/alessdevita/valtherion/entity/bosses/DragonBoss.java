package fr.alessdevita.valtherion.entity.bosses;

import fr.alessdevita.valtherion.entity.Boss;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour le boss Dragon dans Valtherion.
 * Un boss puissant avec des compétences spéciales, des phases de combat, et des récompenses épiques.
 */
public class DragonBoss extends Boss {

    // Phase actuelle du combat
    private DragonPhase currentPhase;

    // Temps écoulé depuis le début du combat (en ticks)
    private int combatTime;

    // Temps entre les attaques de feu (en ticks)
    private int fireAttackCooldown;
    private int currentFireAttackCooldown;

    // Temps entre les attaques de vol (en ticks)
    private int flyAttackCooldown;
    private int currentFlyAttackCooldown;

    // Temps entre les attaques de queue (en ticks)
    private int tailAttackCooldown;
    private int currentTailAttackCooldown;

    // Dégâts des attaques spéciales
    private int fireAttackDamage;
    private int flyAttackDamage;
    private int tailAttackDamage;

    // Rayon des attaques de feu
    private float fireAttackRadius;

    // Hauteur de vol du dragon
    private float flyHeight;

    // Indique si le dragon est en train de voler
    private boolean isFlying;

    // Vitesse de vol du dragon
    private float flySpeed;

    // Énumération des phases du dragon
    public enum DragonPhase {
        PHASE_1,  // Phase 1 : Attaques de base
        PHASE_2,  // Phase 2 : Attaques de feu
        PHASE_3,  // Phase 3 : Vol et attaques aériennes
        PHASE_4   // Phase 4 : Furie finale
    }

    /**
     * Constructeur du boss Dragon.
     * @param world Monde dans lequel le dragon existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public DragonBoss(World world, double x, double y, double z) {
        super(
                world,
                x, y, z,
                "Dragon de l'End",
                "Un dragon puissant qui garde les trésors de l'End.",
                10  // Niveau
        );

        // Configurer les propriétés spécifiques du dragon
        setBossName("Dragon de l'End");
        setMaxHealth(500.0f);
        setHealth(500.0f);
        setAttackDamage(20);
        setArmor(10);
        setDetectionRange(30.0f);
        setAttackRange(5.0f);

        // Configurer les récompenses
        setExperienceReward(1000);
        addReward(new Item("DRAGON_EGG"));
        addReward(new Item("DRAGON_SCALE"));
        addReward(new Item("DIAMOND_SWORD"));
        addReward(new Item("ENCHANTED_BOOK"));

        // Configurer les phases du dragon
        this.currentPhase = DragonPhase.PHASE_1;
        this.combatTime = 0;

        // Configurer les cooldowns et dégâts des attaques spéciales
        this.fireAttackCooldown = 100; // 5 secondes (100 ticks)
        this.currentFireAttackCooldown = 0;
        this.flyAttackCooldown = 200; // 10 secondes (200 ticks)
        this.currentFlyAttackCooldown = 0;
        this.tailAttackCooldown = 150; // 7.5 secondes (150 ticks)
        this.currentTailAttackCooldown = 0;

        this.fireAttackDamage = 30;
        this.flyAttackDamage = 40;
        this.tailAttackDamage = 25;
        this.fireAttackRadius = 8.0f;
        this.flyHeight = 20.0f;
        this.isFlying = false;
        this.flySpeed = 0.2f;

        // Configurer les phases
        addPhase(new Boss.BossPhase(0.7f, "Phase 1: Attaques de base"));
        addPhase(new Boss.BossPhase(0.5f, "Phase 2: Attaques de feu"));
        addPhase(new Boss.BossPhase(0.3f, "Phase 3: Vol et attaques aériennes"));
        addPhase(new Boss.BossPhase(0.0f, "Phase 4: Furie finale"));
    }

    /**
     * Met à jour le dragon.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour le temps de combat
        combatTime++;

        // Mettre à jour les cooldowns des attaques spéciales
        if (currentFireAttackCooldown > 0) {
            currentFireAttackCooldown--;
        }
        if (currentFlyAttackCooldown > 0) {
            currentFlyAttackCooldown--;
        }
        if (currentTailAttackCooldown > 0) {
            currentTailAttackCooldown--;
        }

        // Mettre à jour la phase du dragon
        updatePhase();

        // Exécuter des actions en fonction de la phase actuelle
        executePhaseActions();
    }

    /**
     * Met à jour la phase du dragon en fonction de sa santé.
     */
    private void updatePhase() {
        float healthPercentage = getHealth() / getMaxHealth();

        if (healthPercentage <= 0.3f && currentPhase != DragonPhase.PHASE_4) {
            currentPhase = DragonPhase.PHASE_4;
            System.out.println(getBossName() + " entre en phase 4: Furie finale !");
        } else if (healthPercentage <= 0.5f && currentPhase != DragonPhase.PHASE_3) {
            currentPhase = DragonPhase.PHASE_3;
            System.out.println(getBossName() + " entre en phase 3: Vol et attaques aériennes !");
        } else if (healthPercentage <= 0.7f && currentPhase != DragonPhase.PHASE_2) {
            currentPhase = DragonPhase.PHASE_2;
            System.out.println(getBossName() + " entre en phase 2: Attaques de feu !");
        }
    }

    /**
     * Exécute des actions en fonction de la phase actuelle.
     */
    private void executePhaseActions() {
        switch (currentPhase) {
            case PHASE_1:
                // Attaques de base
                break;
            case PHASE_2:
                // Attaques de feu
                if (currentFireAttackCooldown == 0) {
                    fireBreath();
                    currentFireAttackCooldown = fireAttackCooldown;
                }
                break;
            case PHASE_3:
                // Vol et attaques aériennes
                if (!isFlying && currentFlyAttackCooldown == 0) {
                    startFlying();
                }
                if (isFlying && currentFlyAttackCooldown == 0) {
                    diveAttack();
                    currentFlyAttackCooldown = flyAttackCooldown;
                }
                break;
            case PHASE_4:
                // Furie finale
                if (currentFireAttackCooldown == 0) {
                    fireBreath();
                    currentFireAttackCooldown = fireAttackCooldown / 2; // Cooldown réduit
                }
                if (currentTailAttackCooldown == 0) {
                    tailSweep();
                    currentTailAttackCooldown = tailAttackCooldown / 2; // Cooldown réduit
                }
                break;
        }
    }

    /**
     * Attaque de souffle de feu.
     */
    private void fireBreath() {
        System.out.println(getBossName() + " crache du feu !");

        // Infliger des dégâts à tous les joueurs dans le rayon
        for (Player player : getPlayersInCombat()) {
            float distance = (float) getDistanceTo(player);
            if (distance <= fireAttackRadius) {
                player.damage(fireAttackDamage);
            }
        }
    }

    /**
     * Commence à voler.
     */
    private void startFlying() {
        isFlying = true;
        System.out.println(getBossName() + " décolle et commence à voler !");

        // Se déplacer à la hauteur de vol
        setPosition(getX(), flyHeight, getZ());
    }

    /**
     * Attaque en piqué.
     */
    private void diveAttack() {
        System.out.println(getBossName() + " plonge vers les joueurs !");

        // Infliger des dégâts à tous les joueurs dans le rayon
        for (Player player : getPlayersInCombat()) {
            float distance = (float) getDistanceTo(player);
            if (distance <= 5.0f) {
                player.damage(flyAttackDamage);
            }
        }

        // Arrêter de voler après l'attaque
        isFlying = false;
        setPosition(getX(), 0, getZ()); // Retour au sol
    }

    /**
     * Attaque de balayage de queue.
     */
    private void tailSweep() {
        System.out.println(getBossName() + " balance sa queue !");

        // Infliger des dégâts à tous les joueurs dans le rayon
        for (Player player : getPlayersInCombat()) {
            float distance = (float) getDistanceTo(player);
            if (distance <= 6.0f) {
                player.damage(tailAttackDamage);
            }
        }
    }

    /**
     * Méthode appelée lorsque le dragon meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getBossName() + " a été vaincu ! Les joueurs reçoivent des récompenses épiques !");
    }

    // --- Getters et Setters ---

    public int getCurrentPhase() {
        return currentPhase.ordinal();
    }

    public void setCurrentPhase(DragonPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getCombatTime() {
        return combatTime;
    }

    public void setCombatTime(int combatTime) {
        this.combatTime = combatTime;
    }

    public int getFireAttackCooldown() {
        return fireAttackCooldown;
    }

    public void setFireAttackCooldown(int fireAttackCooldown) {
        this.fireAttackCooldown = fireAttackCooldown;
    }

    public int getFlyAttackCooldown() {
        return flyAttackCooldown;
    }

    public void setFlyAttackCooldown(int flyAttackCooldown) {
        this.flyAttackCooldown = flyAttackCooldown;
    }

    public int getTailAttackCooldown() {
        return tailAttackCooldown;
    }

    public void setTailAttackCooldown(int tailAttackCooldown) {
        this.tailAttackCooldown = tailAttackCooldown;
    }

    public int getFireAttackDamage() {
        return fireAttackDamage;
    }

    public void setFireAttackDamage(int fireAttackDamage) {
        this.fireAttackDamage = fireAttackDamage;
    }

    public int getFlyAttackDamage() {
        return flyAttackDamage;
    }

    public void setFlyAttackDamage(int flyAttackDamage) {
        this.flyAttackDamage = flyAttackDamage;
    }

    public int getTailAttackDamage() {
        return tailAttackDamage;
    }

    public void setTailAttackDamage(int tailAttackDamage) {
        this.tailAttackDamage = tailAttackDamage;
    }

    public float getFireAttackRadius() {
        return fireAttackRadius;
    }

    public void setFireAttackRadius(float fireAttackRadius) {
        this.fireAttackRadius = fireAttackRadius;
    }

    public float getFlyHeight() {
        return flyHeight;
    }

    public void setFlyHeight(float flyHeight) {
        this.flyHeight = flyHeight;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    /**
     * Retourne une représentation textuelle du dragon.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "DragonBoss{" +
                "name='" + getBossName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", phase=" + currentPhase +
                ", isFlying=" + isFlying +
                '}';
    }

    // --- Classe interne pour les phases du dragon ---
    private static class BossPhase {
        private final float healthThreshold;
        private final String phaseName;

        public BossPhase(float healthThreshold, String phaseName) {
            this.healthThreshold = healthThreshold;
            this.phaseName = phaseName;
        }

        public float getHealthThreshold() {
            return healthThreshold;
        }

        public String getPhaseName() {
            return phaseName;
        }
    }
}