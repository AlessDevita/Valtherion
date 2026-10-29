package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.economy.Wallet;
import fr.alessdevita.valtherion.entity.EntityPhysics;
import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.quest.Quest;
import fr.alessdevita.valtherion.quest.QuestLog;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

public class Player extends LivingEntity {

    private final ArrayList<Object> completedQuests;
    private String playerName;
    private UUID uuid;
    private Inventory inventory;
    private Wallet wallet;

    private int level;
    private int experience;
    private int experienceToNextLevel;
    private int skillPoints;

    private final Map<String, Integer> skills;
    private final List<Quest> quests;

    private double spawnX, spawnY, spawnZ;
    private double lastX, lastY, lastZ;

    private World spawnWorld;
    private PlayerState currentState;
    private GameMode gameMode;

    private float sprintSpeed;
    private float walkSpeed;
    private float jumpStrength;

    private boolean isSprinting;
    private boolean isSneaking;
    private boolean isFlying;
    private boolean canFly;
    private boolean isInvincible;

    public EntityPhysics getPhysics() {
        return null;
    }

    public QuestLog getQuestLog() {
        return null;
    }


    public void teleport(double x, double y, double z) {
        setPosition(x, y, z);
    }

    public void kick(String reason) {
        // Logique pour expulser le joueur du serveur
    }

    public void ban(String reason) {
        // Logique pour bannir le joueur du serveur
    }

    public enum PlayerState {
        IDLE,
        WALKING,
        RUNNING,
        JUMPING,
        ATTACKING,
        SNEAKING,
        SPRINTING,
        FLYING,
        USING,
        INTERACTING,
    }

    public enum GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SPECTATOR,
    }

    public Player(World world, double x, double y, double z, String playerName) {
        super(EntityType.PLAYER, world, x, y, z);
        this.playerName = playerName;
        this.uuid = UUID.randomUUID();
        this.inventory = new Inventory(46); // Inventaire avec 46 slots (comme Minecraft)
        this.wallet = new Wallet(this);
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.skillPoints = 0;
        this.skills = new HashMap<>();
        this.quests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.spawnX = x;
        this.spawnY = y;
        this.spawnZ = z;
        this.spawnWorld = world;
        this.currentState = PlayerState.IDLE;
        this.gameMode = GameMode.SURVIVAL;
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        this.walkSpeed = 0.1f;
        this.sprintSpeed = 0.18f;
        this.jumpStrength = 0.42f;
        this.isSprinting = false;
        this.isSneaking = false;
        this.isFlying = false;
        this.canFly = false;
        this.isInvincible = false;

        // Initialiser les statistiques de base
        setMaxHealth(100.0f);
        setHealth(100.0f);
        setAttackDamage(5);
        setArmor(0);

        // Initialiser les compétences de base
        skills.put("MINING", 0);
        skills.put("WOODCUTTING", 0);
        skills.put("COMBAT", 0);
        skills.put("FISHING", 0);
        skills.put("FARMING", 0);
    }

    @Override
    public void update() {
        super.update();

        updateState();

        updateSkills();
    }

    private void updateSkills() {
        // Logique pour mettre à jour les compétences
        // Exemple : Si le joueur mine un bloc, augmmenter la compétence de "MINING"
    }

    private void updateState() {
        if (getX() != lastX || getY() != lastY || getZ() != lastZ) {
            if (isSprinting) {
                currentState = PlayerState.RUNNING;
            } else if (isSneaking) {
                currentState = PlayerState.SNEAKING;
            } else {
                currentState = PlayerState.WALKING;
            }
        } else {
            currentState = PlayerState.IDLE;
        }

        lastX = getX();
        lastY = getY();
        lastZ = getZ();
    }

    @Override
    public void addExperience(int amount) {
        super.addExperience(amount);

        if (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    @Override
    protected void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = (int) (experienceToNextLevel * 1.5);
        skillPoints += 5;

        setMaxHealth(getMaxHealth() + 10);
        setHealth(getMaxHealth());
        setAttackDamage(getAttackDamage() + 1);

        System.out.println(playerName + " est passé au niveau " + level + " !");
    }

    public void addQuest(Quest quest) {
        if (quest != null && !quests.contains(quest) && !completedQuests.contains(quest)) {
            quests.add(quest);
            System.out.println(playerName + " a reçu une nouvelle quête : " + quest.getName());
        }
    }

    public void completeQuest(Quest quest) {
        if (quest != null && quests.contains(quest)) {
            quests.remove(quest);
            completedQuests.add(quest);

            // Donner les récompenses de la quête
            addExperience(quest.getExperienceReward());
            if (wallet != null) {
                wallet.addBalance(quest.getCurrencyReward(), quest.getRewardAmount());
            }

            System.out.println(playerName + " a terminé la quête : " + quest.getName());
        }
    }

    public void addItem(Item item, int quantity) {
        if (inventory != null) {
            inventory.addItem(item, quantity);
        }
    }

    public boolean removeItem(Item item, int quantity) {
        if (inventory != null) {
            return inventory.removeItem(item, quantity);
        }
        return false;
    }

    public void useItem(Item item) {
        if (inventory != null && inventory.hasItem(item, 1)) {
            inventory.removeItem(item, 1);
            // Logique pour utiliser l'objet
            System.out.println(playerName + " utilise " + item.getName());
        }
    }

    @Override
    public void attack(LivingEntity target) {
        if (!isAlive() || target == null || !target.isAlive()) {
            return;
        }

        // Infliger des dégâts à la cible
        target.damage(getAttackDamage());

        // Mettre à jour l'état du joueur
        currentState = PlayerState.ATTACKING;
    }

    @Override
    public void onDeath() {
        super.onDeath();

        // Réinitialiser le joueur à sa position de spawn
        respawn();
    }

    public void respawn() {
        if (spawnWorld != null) {
            setPosition(spawnX, spawnY, spawnZ);
            setWorld(spawnWorld);
            setHealth(getMaxHealth());
            boolean isAlive = true;
            System.out.println(playerName + " a été réinitialisé à sa position de spawn.");
        }
    }

    public void setSpawnPoint(double x, double y, double z, World world) {
        this.spawnX = x;
        this.spawnY = y;
        this.spawnZ = z;
        this.spawnWorld = world;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        // Mettre à jour les capacités du joueur en fonction du mode de jeu
        switch (gameMode) {
            case CREATIVE:
                canFly = true;
                isInvincible = true;
                break;
            case SURVIVAL:
            case ADVENTURE:
                canFly = false;
                isInvincible = false;
                break;
            case SPECTATOR:
                canFly = true;
                isInvincible = true;
                break;
        }
    }

    public void setSprinting(boolean sprinting) {
        isSprinting = sprinting;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public void addSkillPoints(int amount) {
        skillPoints += amount;
    }

    public void upgradeSkill(String skillName, int amount) {
        if (skillPoints >= amount && skills.containsKey(skillName)) {
            skillPoints -= amount;
            skills.put(skillName, skills.get(skillName) + amount);
            System.out.println(playerName + " a amélioré la compétence " + skillName + " de " + amount + " points.");
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public ArrayList<Object> getCompletedQuests() {
        return completedQuests;
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public float getSprintSpeed() {
        return sprintSpeed;
    }

    public void setSprintSpeed(float sprintSpeed) {
        this.sprintSpeed = sprintSpeed;
    }

    public float getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(float jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public boolean canFly() {
        return canFly;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + playerName + '\'' +
                ", uuid=" + uuid +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", level=" + level +
                ", experience=" + experience + "/" + experienceToNextLevel +
                ", gameMode=" + gameMode +
                ", state=" + currentState +
                '}';
    }
}
