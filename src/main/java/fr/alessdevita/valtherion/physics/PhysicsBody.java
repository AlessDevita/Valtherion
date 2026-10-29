package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;
import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector4f;

/**
 * Classe pour un corps physique dans Valtherion.
 * Représente une entité avec des propriétés physiques (masse, vélocité, accélération, etc.).
 */
public class PhysicsBody {

    // Entité associée au corps physique
    private Entity entity;

    // Collisionneur du corps physique
    private Collider collider;

    // Masse du corps physique
    private float mass;

    // Vélocité du corps physique
    private Vector3f velocity;

    // Accélération du corps physique
    private Vector3f acceleration;

    // Force appliquée au corps physique
    private Vector3f force;

    // Coefficient de friction
    private float friction;

    // Coefficient de rebond
    private float restitution;

    // Indique si le corps est statique (ne bouge pas)
    private boolean isStatic;

    // Indique si le corps est affecté par la gravité
    private boolean isAffectedByGravity;

    // Indique si le corps est activé (peut bouger)
    private boolean isEnabled;

    // Type de matériau du corps (pour les interactions physiques)
    private Material material;


    // Énumération des types de matériaux
    public enum Material {
        STONE,       // Pierre
        WOOD,        // Bois
        METAL,       // Métal
        GLASS,       // Verre
        RUBBER,      // Caoutchouc
        ICE,         // Glace
        FLESH        // Chair
        ;

        public void bindTextures() {
            // À implémenter pour les matériaux
        }

        public Vector4f getDiffuseColor() {
            // À implémenter pour les matériaux
            return new Vector4f(1, 1, 1, 1);
        }

        public Vector4f getAmbientColor() {
            // À implémenter pour les matériaux
            return new Vector4f();
        }

        public Vector4f getSpecularColor() {
            // À implémenter pour les matériaux
            return new Vector4f();
        }

        public float getShininess() {
            // À implémenter pour les matériaux
            return 0;
        }
    }

    /**
     * Constructeur du corps physique.
     * @param entity Entité associée.
     * @param collider Collisionneur du corps.
     */
    public PhysicsBody(Entity entity, Collider collider) {
        this.entity = entity;
        this.collider = collider;
        this.collider.setBody(this);
        this.mass = 1.0f;
        this.velocity = new Vector3f(0, 0, 0);
        this.acceleration = new Vector3f(0, 0, 0);
        this.force = new Vector3f(0, 0, 0);
        this.friction = 0.2f;
        this.restitution = 0.5f;
        this.isStatic = false;
        this.isAffectedByGravity = true;
        this.isEnabled = true;
        this.material = Material.STONE;
    }

    /**
     * Met à jour le corps physique.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isEnabled || isStatic) {
            return;
        }

        // Appliquer la friction
        if (velocity.length() > 0) {
            Vector3f frictionForce = new Vector3f(velocity).mul(-friction);
            force.add(frictionForce);
        }

        // Calculer l'accélération en fonction de la force et de la masse
        acceleration = new Vector3f(force).div(mass);

        // Mettre à jour la vélocité
        velocity.add(new Vector3f(acceleration).mul(deltaTime));

        // Mettre à jour la position de l'entité
        Vector3f displacement = new Vector3f(velocity).mul(deltaTime);
        entity.move(displacement.x, displacement.y, displacement.z);

        // Mettre à jour la position du collisionneur
        collider.setPosition(new Vector3f(
                (float) entity.getX(),
                (float) entity.getY(),
                (float) entity.getZ()
        ));

        // Réinitialiser la force
        force.set(0, 0, 0);
    }

    /**
     * Applique une force au corps physique.
     * @param force Force à appliquer.
     */
    public void applyForce(Vector3f force) {
        this.force.add(force);
    }

    /**
     * Applique une impulsion au corps physique.
     * @param impulse Impulsion à appliquer.
     */
    public void applyImpulse(Vector3f impulse) {
        velocity.add(new Vector3f(impulse).div(mass));
    }

    /**
     * Applique une force à un point spécifique du corps physique.
     * @param force Force à appliquer.
     * @param point Point d'application de la force.
     */
    public void applyForceAtPoint(Vector3f force, Vector3f point) {
        // Appliquer la force
        applyForce(force);

        // Appliquer un couple (torque) si nécessaire
        // À implémenter pour les corps rigides
    }

    /**
     * Résout une collision avec un autre corps physique.
     * @param other Autre corps physique.
     * @param collisionPoint Point de collision.
     */
    public void resolveCollision(PhysicsBody other, Vector3f collisionPoint) {
        if (isStatic && other.isStatic) {
            return;
        }

        // Calculer le vecteur de poussée pour résoudre la collision
        Vector3f pushVector = collider.getCollisionPush(other.collider);
        if (pushVector == null) {
            return;
        }

        // Appliquer la poussée à l'entité associée
        if (!isStatic) {
            entity.move(pushVector.x, pushVector.y, pushVector.z);
            collider.setPosition(new Vector3f(
                    (float) entity.getX(),
                    (float) entity.getY(),
                    (float) entity.getZ()
            ));
        }

        if (!other.isStatic) {
            other.getEntity().move(
                    -pushVector.x,
                    -pushVector.y,
                    -pushVector.z
            );
            other.collider.setPosition(new Vector3f(
                    (float) other.getEntity().getX(),
                    (float) other.getEntity().getY(),
                    (float) other.getEntity().getZ()
            ));
        }

        // Appliquer une impulsion en fonction de la vélocité et de la restitution
        Vector3f relativeVelocity = new Vector3f(
                other.velocity.x - velocity.x,
                other.velocity.y - velocity.y,
                other.velocity.z - velocity.z
        );

        float impulseMagnitude = (1 + Math.min(restitution, other.restitution)) *
                relativeVelocity.dot(new Vector3f(pushVector).normalize());

        Vector3f impulse = new Vector3f(pushVector).normalize().mul(impulseMagnitude);

        if (!isStatic) {
            applyImpulse(new Vector3f(impulse).mul(other.mass / (mass + other.mass)));
        }

        if (!other.isStatic) {
            other.applyImpulse(new Vector3f(impulse).mul(-mass / (mass + other.mass)));
        }
    }

    // --- Getters et Setters ---

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
        this.collider.setBody(this);
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
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

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean isAffectedByGravity) {
        this.isAffectedByGravity = isAffectedByGravity;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}