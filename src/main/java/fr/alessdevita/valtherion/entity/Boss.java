package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.entity.living.HostileMob;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

public class Boss extends HostileMob {

    // Nom du boss
    private String bossName;

    // Description du boss
    private String description;

    // Niveau du boss (pour ajuster la difficulté)
    private int level;

    // Récompenses données aux joueurs après la défaite du boss
    private final List<Item> rewards;

    // Expérience donnée aux joueurs après la défaite du boss
    private int experienceReward;

    // Compétences spéciales du boss
    private final List<BossSkill> skills;

    // Cooldown entre les compétences spéciales (en ticks)
    private int skillCooldown;
    private int currentSkillCooldown;

    // État du boss (en combat ou non)
    private boolean isInCombat;

    // Joueurs en combat avec le boss
    private final Set<Player> playersInCombat;

    // Barre de vie personnalisée
    private BossHealthBar healthBar;

    // Phase actuelle du boss (pour les boss avec plusieurs phases)
    private int currentPhase;
    private final List<BossPhase> phases;
    private int attackDamage;
    private int maxHealth;

    /**
     * Constructeur du boss.
     * @param world Monde dans lequel le boss existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param bossName Nom du boss.
     * @param description Description du boss.
     * @param level Niveau du boss.
     */
    public Boss(World world, double x, double y, double z, String bossName, String description, int level) {
        super(EntityType.BOSS, world, x, y, z);
        this.bossName = bossName;
        this.description = description;
        this.level = level;
        this.rewards = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.skillCooldown = 100; // Cooldown par défaut : 100 ticks (5 secondes)
        this.currentSkillCooldown = 0;
        this.isInCombat = false;
        this.playersInCombat = new HashSet<>();
        this.healthBar = new BossHealthBar(this);
        this.currentPhase = 0;
        this.phases = new ArrayList<>();

        // Initialiser les points de vie en fonction du niveau
        setMaxHealth(100 + level * 50);
        getHealthBar((int) getMaxHealth());

        // Initialiser les dégâts en fonction du niveau
        setAttackDamage(10 + level * 5);
    }

    public void setAttackDamage(int i) {
        this.attackDamage = i;
    }

    public float getMaxHealth() {
        float maxHealth = 0;
        return maxHealth;
    }

    private void setMaxHealth(int i) {
        this.maxHealth = i;
    }

    /**
     * Met à jour le boss.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {

        // Mettre à jour le cooldown des compétences
        if (currentSkillCooldown > 0) {
            currentSkillCooldown--;
        }

        // Mettre à jour la barre de vie
        healthBar.update();

        // Vérifier si le boss est en combat
        if (!playersInCombat.isEmpty()) {
            isInCombat = true;

            // Utiliser une compétence spéciale si le cooldown est terminé
            if (currentSkillCooldown == 0 && !skills.isEmpty()) {
                useRandomSkill();
                currentSkillCooldown = skillCooldown;
            }
        } else {
            isInCombat = false;
        }

        // Vérifier les phases du boss
        updatePhase();
    }

    /**
     * Ajoute une récompense au boss.
     * @param item Objet à ajouter comme récompense.
     */
    public void addReward(Item item) {
        if (item != null) {
            rewards.add(item);
        }
    }

    /**
     * Définit les récompenses du boss.
     * @param rewards Liste des objets à ajouter comme récompenses.
     */
    public void setRewards(List<Item> rewards) {
        this.rewards.clear();
        this.rewards.addAll(rewards);
    }

    /**
     * Définit l'expérience donnée aux joueurs après la défaite du boss.
     * @param experienceReward Expérience à donner.
     */
    public void setExperienceReward(int experienceReward) {
        this.experienceReward = experienceReward;
    }

    /**
     * Ajoute une compétence spéciale au boss.
     * @param skill Compétence à ajouter.
     */
    public void addSkill(BossSkill skill) {
        if (skill != null) {
            skills.add(skill);
        }
    }

    /**
     * Utilise une compétence spéciale aléatoire.
     */
    private void useRandomSkill() {
        if (skills.isEmpty()) {
            return;
        }

        // Sélectionner une compétence aléatoire
        BossSkill skill = skills.get(new Random().nextInt(skills.size()));
        skill.use(this);
    }

    /**
     * Ajoute une phase au boss.
     * @param phase Phase à ajouter.
     */
    public void addPhase(BossPhase phase) {
        if (phase != null) {
            phases.add(phase);
        }
    }

    /**
     * Met à jour la phase actuelle du boss.
     */
    private void updatePhase() {
        if (phases.isEmpty()) {
            return;
        }

        // Vérifier si la santé du boss est inférieure au seuil de la phase actuelle
        BossPhase currentPhase = phases.get(this.currentPhase);
        if (getHealth() <= currentPhase.getHealthThreshold()) {
            // Passer à la phase suivante
            this.currentPhase++;
            if (this.currentPhase < phases.size()) {
                BossPhase nextPhase = phases.get(this.currentPhase);
                nextPhase.onPhaseStart(this);
            }
        }
    }

    public float getHealth() {
        return 0;
    }

    /**
     * Ajoute un joueur en combat avec le boss.
     * @param player Joueur à ajouter.
     */
    public void addPlayerInCombat(Player player) {
        if (player != null) {
            playersInCombat.add(player);
        }
    }

    /**
     * Retire un joueur du combat avec le boss.
     * @param player Joueur à retirer.
     */
    public void removePlayerInCombat(Player player) {
        if (player != null) {
            playersInCombat.remove(player);
        }
    }

    /**
     * Méthode appelée lorsque le boss meurt.
     */
    @Override
    public void onDeath() {

        // Donner les récompenses aux joueurs en combat
        for (Player player : playersInCombat) {
            for (Item reward : rewards) {
                player.getInventory().addItem(reward, 1);
            }
            player.addExperience(experienceReward);
        }

        // Nettoyer les joueurs en combat
        playersInCombat.clear();

        // Désactiver la barre de vie
        healthBar.setActive(false);
    }

    // --- Getters et Setters ---

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Item> getRewards() {
        return rewards;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public List<BossSkill> getSkills() {
        return skills;
    }

    public int getSkillCooldown() {
        return skillCooldown;
    }

    public void setSkillCooldown(int skillCooldown) {
        this.skillCooldown = skillCooldown;
    }

    public boolean isInCombat() {
        return isInCombat;
    }

    public Set<Player> getPlayersInCombat() {
        return playersInCombat;
    }

    public BossHealthBar getHealthBar(int maxHealth) {
        return healthBar;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public List<BossPhase> getPhases() {
        return phases;
    }

    /**
     * Retourne une représentation textuelle du boss.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Boss{" +
                "name='" + bossName + '\'' +
                ", level=" + level +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", isInCombat=" + isInCombat +
                '}';
    }

    // --- Classes internes ---

    /**
     * Classe pour les compétences spéciales du boss.
     */
    public static abstract class BossSkill {
        public abstract void use(Boss boss);
    }

    /**
     * Classe pour les phases du boss.
     */
    public static class BossPhase {
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

        public void onPhaseStart(Boss boss) {
            // Logique à exécuter au début de la phase
        }
    }

    /**
     * Classe pour la barre de vie personnalisée du boss.
     */
    public static class BossHealthBar {
        private final Boss boss;
        private boolean isActive;

        public BossHealthBar(Boss boss) {
            this.boss = boss;
            this.isActive = true;
        }

        public void update() {
            // Mettre à jour l'affichage de la barre de vie
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public boolean isActive() {
            return isActive;
        }
    }
}
