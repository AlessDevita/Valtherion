package fr.alessdevita.valtherion.client.entity;

import fr.alessdevita.valtherion.entity.EntityLivingBase;
import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe abstraite de base pour les joueurs côté client dans Valtherion.
 * Définit les propriétés et méthodes communes à tous les joueurs.
 */
public abstract class AbstractClientPlayer extends EntityLivingBase {

    // Vitesse de déplacement
    protected float moveSpeed;

    // Vitesse de rotation
    protected float rotationSpeed;

    // Vitesse de saut
    protected float jumpForce;

    // Santé du joueur
    protected float health;

    // Santé maximale du joueur
    protected float maxHealth;

    // Mana du joueur
    protected float mana;

    // Mana maximal du joueur
    protected float maxMana;

    // Indique si le joueur est en train de courir
    protected boolean isSprinting;

    // Indique si le joueur est en train de sauter
    protected boolean isJumping;

    // Indique si le joueur est au sol
    protected boolean isOnGround;

    // Indique si le joueur est en mode créatif
    protected boolean isCreativeMode;

    // Monde dans lequel le joueur évolue
    protected World world;

    // Vitesse actuelle du joueur (pour les calculs de mouvement)
    protected Vector3f velocity;

    // Accélération du joueur
    protected Vector3f acceleration;

    /**
     * Constructeur de la classe abstraite AbstractClientPlayer.
     * @param id ID de l'entité.
     * @param name Nom de l'entité.
     * @param world Monde dans lequel le joueur évolue.
     */
    public AbstractClientPlayer(String id, String name, World world) {
        super(id, name, world);
        this.world = world;
        this.moveSpeed = 5.0f;
        this.rotationSpeed = 2.0f;
        this.jumpForce = 10.0f;
        this.health = 100.0f;
        this.maxHealth = 100.0f;
        this.mana = 100.0f;
        this.maxMana = 100.0f;
        this.isSprinting = false;
        this.isJumping = false;
        this.isOnGround = true;
        this.isCreativeMode = false;
        this.velocity = new Vector3f();
        this.acceleration = new Vector3f();
    }

    /**
     * Met à jour le joueur.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {
        // Mettre à jour la position en fonction de la vitesse
        Vector3f newPosition = new Vector3f(getPosition());
        newPosition.add(velocity.mul(deltaTime, new Vector3f()));
        setPosition(newPosition);

        // Appliquer la gravité si le joueur n'est pas au sol
        if (!isOnGround) {
            velocity.y += world.getGravity().y * deltaTime; // Ajouter la gravité (négative)
        }

        // Mettre à jour l'état du joueur (ex: vérifier si le joueur est au sol)
        updatePlayerState();
    }

    public void setPosition(Vector3f newPosition) {
        super.setPosition(newPosition);
    }

    /**
     * Met à jour l'état du joueur (ex: vérifier si le joueur est au sol).
     */
    protected void updatePlayerState() {
        // Vérifier si le joueur est au sol (simplifié)
        // Dans un jeu réel, tu utiliserais un système de collision pour vérifier si le joueur touche le sol
        isOnGround = getPosition().y <= 0.0f; // Exemple simplifié

        // Si le joueur est au sol, réinitialiser la vitesse verticale
        if (isOnGround) {
            velocity.y = 0.0f;
            isJumping = false;
        }
    }

    /**
     * Déplace le joueur.
     * @param direction Direction du déplacement.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void move(Vector3f direction, float deltaTime) {
        // Calculer la vitesse de déplacement en fonction de la direction
        float speed = isSprinting ? moveSpeed * 1.5f : moveSpeed;
        Vector3f movement = new Vector3f(direction).mul(speed * deltaTime);

        // Appliquer le mouvement à la vitesse (uniquement sur les axes X et Z)
        velocity.x = movement.x;
        velocity.z = movement.z;
    }

    /**
     * Fait sauter le joueur.
     */
    public void jump() {
        if (isOnGround) {
            velocity.y = jumpForce;
            isJumping = true;
            isOnGround = false;
        }
    }

    /**
     * Fait courir le joueur.
     * @param sprinting true pour activer la course.
     */
    public void setSprinting(boolean sprinting) {
        this.isSprinting = sprinting;
    }

    /**
     * Inflige des dégâts au joueur.
     * @param damage Montant des dégâts.
     */
    public void damage(float damage) {
        if (!isCreativeMode) {
            health = Math.max(0.0f, health - damage);
            if (health <= 0.0f) {
                die();
            }
        }
    }

    /**
     * Soigne le joueur.
     * @param amount Montant des soins.
     */
    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /**
     * Utilise du mana.
     * @param amount Montant de mana utilisé.
     */
    public void useMana(float amount) {
        mana = Math.max(0.0f, mana - amount);
    }

    /**
     * Restaure du mana.
     * @param amount Montant de mana restauré.
     */
    public void restoreMana(float amount) {
        mana = Math.min(maxMana, mana + amount);
    }

    /**
     * Tue le joueur.
     */
    public void die() {
        System.out.println("Le joueur " + getName() + " est mort !");
        // Logique de mort (ex: réapparition, game over)
    }

    // --- Getters et Setters ---

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.max(0.0f, Math.min(maxHealth, health));
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = Math.max(0.0f, Math.min(maxMana, mana));
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public boolean isCreativeMode() {
        return isCreativeMode;
    }

    public void setCreativeMode(boolean creativeMode) {
        this.isCreativeMode = creativeMode;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Retourne une représentation textuelle du joueur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "AbstractClientPlayer{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", health=" + health +
                ", position=" + getPosition() +
                ", isSprinting=" + isSprinting +
                '}';
    }
}
