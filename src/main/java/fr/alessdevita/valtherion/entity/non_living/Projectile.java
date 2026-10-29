package fr.alessdevita.valtherion.entity.non_living;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Classe de base pour les projectiles dans Valtherion.
 * Représente un projectile comme une flèche, une boule de feu, ou un trident.
 */
public abstract class Projectile extends Entity {

    // Entité qui a tiré le projectile
    private LivingEntity shooter;

    // Cible du projectile (optionnelle)
    private LivingEntity target;

    // Dégâts infligés par le projectile
    private int damage;

    // Vitesse du projectile
    private float speed;

    // Distance maximale parcourue par le projectile
    private float maxDistance;
    private float traveledDistance;

    // Indique si le projectile est en mouvement
    private boolean isMoving;

    // Indique si le projectile a touché une cible
    private boolean hasHit;

    // Gravité appliquée au projectile
    private float gravity;

    // Vitesse sur les axes X, Y, Z
    private float velocityX, velocityY, velocityZ;

    /**
     * Constructeur du projectile.
     * @param type Type de l'entité.
     * @param world Monde dans lequel le projectile existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param shooter Entité qui a tiré le projectile.
     */
    public Projectile(EntityType type, World world, double x, double y, double z, LivingEntity shooter) {
        super(type, x, y, z);
        this.shooter = shooter;
        this.target = null;
        this.damage = 10;
        this.speed = 0.3f;
        this.maxDistance = 100.0f;
        this.traveledDistance = 0.0f;
        this.isMoving = true;
        this.hasHit = false;
        this.gravity = 0.02f;
        this.velocityX = 0.0f;
        this.velocityY = 0.0f;
        this.velocityZ = 0.0f;
    }

    /**
     * Met à jour le projectile.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        if (!isMoving || hasHit) {
            return;
        }

        // Appliquer la gravité
        velocityY -= gravity;

        // Calculer la nouvelle position
        double newX = getX() + velocityX;
        double newY = getY() + velocityY;
        double newZ = getZ() + velocityZ;

        // Calculer la distance parcourue
        float distance = (float) Math.sqrt(
                velocityX * velocityX +
                        velocityY * velocityY +
                        velocityZ * velocityZ
        );
        traveledDistance += distance;

        // Vérifier si le projectile a atteint sa distance maximale
        if (traveledDistance >= maxDistance) {
            destroy();
            return;
        }

        // Vérifier les collisions
        if (checkCollisions(newX, newY, newZ)) {
            onHit();
            hasHit = true;
            destroy();
            return;
        }

        // Mettre à jour la position
        setPosition(newX, newY, newZ);
    }

    /**
     * Vérifie les collisions avec le monde ou les entités.
     * @param newX Nouvelle coordonnée X.
     * @param newY Nouvelle coordonnée Y.
     * @param newZ Nouvelle coordonnée Z.
     * @return true si une collision est détectée, false sinon.
     */
    private boolean checkCollisions(double newX, double newY, double newZ) {
        if (getWorld() == null) {
            return false;
        }

        // Vérifier les collisions avec les blocs
        if (getWorld().isBlockSolid((int) newX, (int) newY, (int) newZ)) {
            return true;
        }

        // Vérifier les collisions avec les entités
        for (Entity entity : getWorld().getEntities()) {
            if (entity == this || entity == shooter) {
                continue;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                float distance = (float) getDistanceTo(livingEntity);
                if (distance <= 1.0f) {
                    target = livingEntity;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Méthode appelée lorsque le projectile touche une cible ou un bloc.
     */
    public void onHit() {
        if (target != null) {
            // Infliger des dégâts à la cible
            target.damage(damage);
        }
    }

    /**
     * Tire le projectile vers une cible.
     * @param target Cible du projectile.
     */
    public void shoot(LivingEntity target) {
        this.target = target;

        // Calculer la direction vers la cible
        double dx = target.getX() - getX();
        double dy = target.getY() - getY();
        double dz = target.getZ() - getZ();

        // Normaliser la direction
        float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length > 0) {
            velocityX = (float) (dx / length * speed);
            velocityY = (float) (dy / length * speed);
            velocityZ = (float) (dz / length * speed);
        }

        isMoving = true;
    }

    /**
     * Tire le projectile dans une direction spécifique.
     * @param velocityX Vitesse en X.
     * @param velocityY Vitesse en Y.
     * @param velocityZ Vitesse en Z.
     */
    public void shoot(float velocityX, float velocityY, float velocityZ) {
        this.velocityX = velocityX * speed;
        this.velocityY = velocityY * speed;
        this.velocityZ = velocityZ * speed;
        isMoving = true;
    }

    // --- Getters et Setters ---

    public LivingEntity getShooter() {
        return shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean hasHit() {
        return hasHit;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    /**
     * Retourne une représentation textuelle du projectile.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Projectile{" +
                "type=" + getType() +
                ", shooter=" + (shooter != null ? shooter.getName() : "null") +
                ", damage=" + damage +
                ", position=(" + getX() + ", " + getY() + ", " + getZ() + ")" +
                '}';
    }
}