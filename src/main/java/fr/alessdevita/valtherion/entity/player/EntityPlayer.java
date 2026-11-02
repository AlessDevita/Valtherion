package fr.alessdevita.valtherion.entity.player;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.world.World;

/**
 * Classe de base pour les entités joueurs dans Valtherion.
 * Sert de pont entre le système d'entités et le système de joueurs côté client/serveur.
 * Étend LivingEntity pour hériter de toutes les capacités des entités vivantes.
 */
public class EntityPlayer extends LivingEntity {

    /**
     * Nom d'affichage du joueur.
     */
    protected String playerName;

    /**
     * Indique si le joueur est en train de sprinter.
     */
    protected boolean isSprinting;

    /**
     * Indique si le joueur est en train de se faufiler.
     */
    protected boolean isSneaking;

    /**
     * Indique si le joueur peut voler.
     */
    protected boolean canFly;

    /**
     * Indique si le joueur est en train de voler.
     */
    protected boolean isFlying;

    /**
     * Indicateur pour savoir si le joueur est invincible (mode créatif/spectateur).
     */
    protected boolean isInvincible;

    /**
     * Mode de jeu actuel du joueur.
     */
    protected GameMode gameMode;

    /**
     * Position de spawn du joueur.
     */
    protected double spawnX;
    protected double spawnY;
    protected double spawnZ;

    /**
     * Monde de spawn du joueur.
     */
    protected World spawnWorld;

    /**
     * Énumération des modes de jeu disponibles.
     */
    public enum GameMode {
        SURVIVAL,    // Mode survie : gestion de la santé, de la faim, des ressources
        CREATIVE,    // Mode créatif : vol, invulnérabilité, ressources illimitées
        ADVENTURE,   // Mode aventure : similaire à la survie mais avec des restrictions de destruction
        SPECTATOR    // Mode spectateur : observation sans interaction
    }

    /**
     * Constructeur de l'entité joueur.
     *
     * @param world      Monde dans lequel le joueur existe.
     * @param x          Coordonnée X.
     * @param y          Coordonnée Y.
     * @param z          Coordonnée Z.
     * @param playerName Nom du joueur.
     */
    public EntityPlayer(World world, double x, double y, double z, String playerName) {
        super(EntityType.PLAYER, world, x, y, z);
        this.playerName = playerName != null && !playerName.isEmpty() ? playerName : "Joueur";
        this.isSprinting = false;
        this.isSneaking = false;
        this.canFly = false;
        this.isFlying = false;
        this.isInvincible = false;
        this.gameMode = GameMode.SURVIVAL;
        this.spawnX = x;
        this.spawnY = y;
        this.spawnZ = z;
        this.spawnWorld = world;

        // Configurer les statistiques de base du joueur
        setMaxHealth(20.0f);
        setHealth(20.0f);
        setAttackDamage(1);
        setArmor(0);
        setBaseSpeed(0.1f);
    }

    /**
     * Met à jour l'entité joueur.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Appliquer les effets du mode de jeu
        applyGameModeEffects();
    }

    /**
     * Applique les effets spécifiques au mode de jeu actuel.
     */
    protected void applyGameModeEffects() {
        switch (gameMode) {
            case CREATIVE:
                isInvincible = true;
                canFly = true;
                break;
            case SPECTATOR:
                isInvincible = true;
                canFly = true;
                break;
            case SURVIVAL:
            case ADVENTURE:
            default:
                isInvincible = false;
                canFly = false;
                isFlying = false;
                break;
        }
    }

    /**
     * Définit le mode de jeu du joueur.
     *
     * @param gameMode Nouveau mode de jeu.
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        applyGameModeEffects();
    }

    /**
     * Fait respawner le joueur à sa position de spawn.
     */
    public void respawn() {
        if (spawnWorld != null) {
            setPosition(spawnX, spawnY, spawnZ);
            setWorld(spawnWorld);
            setHealth(getMaxHealth());
            setAlive(true);
        }
    }

    /**
     * Définit le point de spawn du joueur.
     *
     * @param x     Coordonnée X.
     * @param y     Coordonnée Y.
     * @param z     Coordonnée Z.
     * @param world Monde de spawn.
     */
    public void setSpawnPoint(double x, double y, double z, World world) {
        this.spawnX = x;
        this.spawnY = y;
        this.spawnZ = z;
        this.spawnWorld = world;
    }

    /**
     * Téléporte le joueur à une position donnée.
     *
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void teleport(double x, double y, double z) {
        setPosition(x, y, z);
    }

    /**
     * Méthode appelée lorsque le joueur meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();

        // En mode survie/aventure, le joueur respawn
        if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            respawn();
        }
        // En mode créatif/spectateur, le joueur ne meurt pas
        else {
            setAlive(true);
            setHealth(getMaxHealth());
        }
    }

    /**
     * Active ou désactive le sprint.
     *
     * @param sprinting true pour activer le sprint, false pour le désactiver.
     */
    public void setSprinting(boolean sprinting) {
        this.isSprinting = sprinting;
    }

    /**
     * Active ou désactive le furtif.
     *
     * @param sneaking true pour activer le furtif, false pour le désactiver.
     */
    public void setSneaking(boolean sneaking) {
        this.isSneaking = sneaking;
    }

    /**
     * Active ou désactive le vol.
     *
     * @param flying true pour activer le vol, false pour le désactiver.
     */
    public void setFlying(boolean flying) {
        if (canFly) {
            this.isFlying = flying;
        }
    }

    // --- Getters ---

    /**
     * Retourne le nom du joueur.
     *
     * @return Nom du joueur.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Définit le nom du joueur.
     *
     * @param playerName Nouveau nom du joueur.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName != null && !playerName.isEmpty() ? playerName : "Joueur";
    }

    /**
     * Retourne le mode de jeu actuel.
     *
     * @return Mode de jeu.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Retourne true si le joueur sprint.
     *
     * @return true si le joueur sprint.
     */
    public boolean isSprinting() {
        return isSprinting;
    }

    /**
     * Retourne true si le joueur est en mode furtif.
     *
     * @return true si le joueur est en mode furtif.
     */
    public boolean isSneaking() {
        return isSneaking;
    }

    /**
     * Retourne true si le joueur peut voler.
     *
     * @return true si le joueur peut voler.
     */
    public boolean canFly() {
        return canFly;
    }

    /**
     * Retourne true si le joueur est en train de voler.
     *
     * @return true si le joueur est en train de voler.
     */
    public boolean isFlying() {
        return isFlying;
    }

    /**
     * Retourne true si le joueur est invincible.
     *
     * @return true si le joueur est invincible.
     */
    public boolean isInvincible() {
        return isInvincible;
    }

    /**
     * Retourne la coordonnée X de spawn.
     *
     * @return Coordonnée X de spawn.
     */
    public double getSpawnX() {
        return spawnX;
    }

    /**
     * Retourne la coordonnée Y de spawn.
     *
     * @return Coordonnée Y de spawn.
     */
    public double getSpawnY() {
        return spawnY;
    }

    /**
     * Retourne la coordonnée Z de spawn.
     *
     * @return Coordonnée Z de spawn.
     */
    public double getSpawnZ() {
        return spawnZ;
    }

    /**
     * Retourne le monde de spawn.
     *
     * @return Monde de spawn.
     */
    public World getSpawnWorld() {
        return spawnWorld;
    }

    /**
     * Retourne une représentation textuelle de l'entité joueur.
     *
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityPlayer{" +
                "name='" + playerName + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", gameMode=" + gameMode +
                ", position=(" + getX() + ", " + getY() + ", " + getZ() + ")" +
                ", isSprinting=" + isSprinting +
                ", isFlying=" + isFlying +
                '}';
    }
}
