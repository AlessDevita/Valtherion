package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.Block;

/**
 * Classe pour gérer la physique des entités dans Valtherion.
 * Gère la gravité, les collisions, les mouvements, et les interactions avec le monde.
 */
public class EntityPhysics {

    // Entité associée à cette physique
    private final Entity entity;

    // Monde dans lequel l'entité existe
    private final World world;

    // Vitesse de l'entité
    private double velocityX, velocityY, velocityZ;

    // Accélération de l'entité
    private double accelerationX, accelerationY, accelerationZ;

    // Gravité appliquée à l'entité
    private double gravity;

    // Vitesse maximale de l'entité
    private double maxSpeed;

    // Coefficient de frottement (pour ralentir l'entité)
    private double friction;

    // État de l'entité (au sol, en l'air, dans l'eau, etc.)
    private boolean isOnGround;
    private boolean isInWater;
    private boolean isInLava;

    // Taille de l'entité (pour les collisions)
    private double width;
    private double height;

    // Vitesse de saut
    private double jumpStrength;

    // Vitesse de marche
    private double walkSpeed;

    // Vitesse de course
    private double sprintSpeed;

    // Vitesse de nage
    private double swimSpeed;

    // Vitesse de chute
    private double fallSpeed;

    // Indique si l'entité peut sauter
    private boolean canJump;

    // Indique si l'entité peut voler
    private boolean canFly;

    /**
     * Constructeur de la physique de l'entité.
     * @param entity Entité associée.
     * @param world Monde dans lequel l'entité existe.
     */
    public EntityPhysics(Entity entity, World world) {
        this.entity = entity;
        this.world = world;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        this.accelerationX = 0.0;
        this.accelerationY = 0.0;
        this.accelerationZ = 0.0;
        this.gravity = 0.08; // Accélération due à la gravité
        this.maxSpeed = 1.0;
        this.friction = 0.98; // Coefficient de frottement
        this.isOnGround = false;
        this.isInWater = false;
        this.isInLava = false;
        this.width = 0.6; // Largeur par défaut
        this.height = 1.8; // Hauteur par défaut
        this.jumpStrength = 0.42; // Force de saut
        this.walkSpeed = 0.1;
        this.sprintSpeed = 0.18;
        this.swimSpeed = 0.05;
        this.fallSpeed = 0.0;
        this.canJump = true;
        this.canFly = false;
    }

    /**
     * Met à jour la physique de l'entité.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        // Appliquer la gravité si l'entité n'est pas en vol ou dans l'eau
        if (!canFly && !isInWater && !isInLava) {
            velocityY -= gravity;
        }

        // Appliquer les accélérations
        velocityX += accelerationX;
        velocityY += accelerationY;
        velocityZ += accelerationZ;

        // Appliquer le frottement
        velocityX *= friction;
        velocityZ *= friction;

        // Limiter la vitesse maximale
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        if (speed > maxSpeed) {
            velocityX = (velocityX / speed) * maxSpeed;
            velocityY = (velocityY / speed) * maxSpeed;
            velocityZ = (velocityZ / speed) * maxSpeed;
        }

        // Calculer la nouvelle position
        double newX = entity.getX() + velocityX;
        double newY = entity.getY() + velocityY;
        double newZ = entity.getZ() + velocityZ;

        // Vérifier les collisions avec le monde
        if (checkCollisions(newX, newY, newZ)) {
            entity.setPosition(newX, newY, newZ);
        }

        // Mettre à jour l'état de l'entité
        updateEntityState();

        // Réinitialiser les accélérations
        accelerationX = 0.0;
        accelerationY = 0.0;
        accelerationZ = 0.0;
    }

    /**
     * Vérifie les collisions avec le monde.
     * @param newX Nouvelle coordonnée X.
     * @param newY Nouvelle coordonnée Y.
     * @param newZ Nouvelle coordonnée Z.
     * @return true si le mouvement est valide, false sinon.
     */
    private boolean checkCollisions(double newX, double newY, double newZ) {
        if (world == null) {
            return true;
        }

        // Vérifier les collisions avec les blocs
        if (checkBlockCollisions(newX, newY, newZ)) {
            return false;
        }

        // Vérifier les collisions avec les liquides
        checkLiquidCollisions(newX, newY, newZ);

        return true;
    }

    /**
     * Vérifie les collisions avec les blocs.
     * @param newX Nouvelle coordonnée X.
     * @param newY Nouvelle coordonnée Y.
     * @param newZ Nouvelle coordonnée Z.
     * @return true si une collision est détectée, false sinon.
     */
    private boolean checkBlockCollisions(double newX, double newY, double newZ) {
        // Calculer les limites de l'entité
        double minX = newX - width / 2;
        double maxX = newX + width / 2;
        double minY = newY;
        double maxY = newY + height;
        double minZ = newZ - width / 2;
        double maxZ = newZ + width / 2;

        // Vérifier les blocs autour de l'entité
        for (int x = (int) Math.floor(minX); x <= Math.ceil(maxX); x++) {
            for (int y = (int) Math.floor(minY); y <= Math.ceil(maxY); y++) {
                for (int z = (int) Math.floor(minZ); z <= Math.ceil(maxZ); z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block != null && block.isSolid()) {
                        // Collision détectée
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Vérifie les collisions avec les liquides.
     * @param newX Nouvelle coordonnée X.
     * @param newY Nouvelle coordonnée Y.
     * @param newZ Nouvelle coordonnée Z.
     */
    private void checkLiquidCollisions(double newX, double newY, double newZ) {
        // Calculer les limites de l'entité
        double minX = newX - width / 2;
        double maxX = newX + width / 2;
        double minY = newY;
        double maxY = newY + height;
        double minZ = newZ - width / 2;
        double maxZ = newZ + width / 2;

        // Réinitialiser les états des liquides
        isInWater = false;
        isInLava = false;

        // Vérifier les blocs de liquide autour de l'entité
        for (int x = (int) Math.floor(minX); x <= Math.ceil(maxX); x++) {
            for (int y = (int) Math.floor(minY); y <= Math.ceil(maxY); y++) {
                for (int z = (int) Math.floor(minZ); z <= Math.ceil(maxZ); z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block != null) {
                        if (block.isLiquid()) {
                            if (block.isWater()) {
                                isInWater = true;
                            } else if (block.isLava()) {
                                isInLava = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Met à jour l'état de l'entité (au sol, en l'air, etc.).
     */
    private void updateEntityState() {
        // Vérifier si l'entité est au sol
        double minY = entity.getY();
        double maxY = entity.getY() + height;
        double minX = entity.getX() - width / 2;
        double maxX = entity.getX() + width / 2;
        double minZ = entity.getZ() - width / 2;
        double maxZ = entity.getZ() + width / 2;

        isOnGround = false;

        // Vérifier les blocs sous l'entité
        for (int x = (int) Math.floor(minX); x <= Math.ceil(maxX); x++) {
            for (int z = (int) Math.floor(minZ); z <= Math.ceil(maxZ); z++) {
                Block block = world.getBlock(x, (int) Math.floor(minY - 0.1), z);
                if (block != null && block.isSolid()) {
                    isOnGround = true;
                    break;
                }
            }
            if (isOnGround) {
                break;
            }
        }

        // Si l'entité est au sol, réinitialiser la vitesse de chute
        if (isOnGround) {
            fallSpeed = 0.0;
        } else {
            fallSpeed = -velocityY;
        }
    }

    /**
     * Applique une force à l'entité.
     * @param forceX Force en X.
     * @param forceY Force en Y.
     * @param forceZ Force en Z.
     */
    public void applyForce(double forceX, double forceY, double forceZ) {
        accelerationX += forceX;
        accelerationY += forceY;
        accelerationZ += forceZ;
    }

    /**
     * Fait sauter l'entité.
     */
    public void jump() {
        if (canJump && isOnGround && !isInWater && !isInLava) {
            velocityY = jumpStrength;
            isOnGround = false;
        }
    }

    /**
     * Définit la vitesse de l'entité.
     * @param velocityX Vitesse en X.
     * @param velocityY Vitesse en Y.
     * @param velocityZ Vitesse en Z.
     */
    public void setVelocity(double velocityX, double velocityY, double velocityZ) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    /**
     * Définit la vitesse de marche.
     * @param walkSpeed Vitesse de marche.
     */
    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    /**
     * Définit la vitesse de course.
     * @param sprintSpeed Vitesse de course.
     */
    public void setSprintSpeed(double sprintSpeed) {
        this.sprintSpeed = sprintSpeed;
    }

    /**
     * Définit la vitesse de nage.
     * @param swimSpeed Vitesse de nage.
     */
    public void setSwimSpeed(double swimSpeed) {
        this.swimSpeed = swimSpeed;
    }

    /**
     * Définit si l'entité peut sauter.
     * @param canJump true si l'entité peut sauter, false sinon.
     */
    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    /**
     * Définit si l'entité peut voler.
     * @param canFly true si l'entité peut voler, false sinon.
     */
    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    // --- Getters ---

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public boolean isInWater() {
        return isInWater;
    }

    public boolean isInLava() {
        return isInLava;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }

    public double getSprintSpeed() {
        return sprintSpeed;
    }

    public double getSwimSpeed() {
        return swimSpeed;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    public boolean canJump() {
        return canJump;
    }

    public boolean canFly() {
        return canFly;
    }

    public Entity getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }
}