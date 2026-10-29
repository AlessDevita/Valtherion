package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.physics.PhysicsBody;
import fr.alessdevita.valtherion.physics.Collider;
import fr.alessdevita.valtherion.physics.BoxCollider;

import org.joml.Vector3f;

/**
 * Composant pour la physique d'une entité.
 */
public class PhysicsComponent implements Component {

    // Corps physique associé
    private PhysicsBody physicsBody;

    // Collisionneur de l'entité
    private Collider collider;

    // Masse de l'entité
    private float mass;

    // Vélocité de l'entité
    private Vector3f velocity;

    // Accélération de l'entité
    private Vector3f acceleration;

    // Indique si l'entité est affectée par la gravité
    private boolean isAffectedByGravity;

    // Indique si l'entité est statique (ne bouge pas)
    private boolean isStatic;

    // Coefficient de friction
    private float friction;

    // Coefficient de rebond
    private float restitution;

    // Entité associée
    private Entity entity;

    /**
     * Constructeur du composant physique.
     */
    public PhysicsComponent() {
        this.physicsBody = null;
        this.collider = null;
        this.mass = 1.0f;
        this.velocity = new Vector3f(0, 0, 0);
        this.acceleration = new Vector3f(0, 0, 0);
        this.isAffectedByGravity = true;
        this.isStatic = false;
        this.friction = 0.2f;
        this.restitution = 0.5f;
    }

    /**
     * Constructeur du composant physique avec une masse.
     * @param mass Masse de l'entité.
     */
    public PhysicsComponent(float mass) {
        this();
        this.mass = mass;
    }

    @Override
    public void init() {
        // Initialiser le corps physique et le collisionneur
        if (entity != null && entity.hasComponent(TransformComponent.class)) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            Vector3f position = transform.getPosition();

            // Créer un collisionneur de type boîte (AABB)
            this.collider = new BoxCollider(position, new Vector3f(0.5f, 0.5f, 0.5f));

            // Créer le corps physique
            this.physicsBody = new PhysicsBody(entity, collider);
            physicsBody.setMass(mass);
            physicsBody.setFriction(friction);
            physicsBody.setRestitution(restitution);
            physicsBody.setAffectedByGravity(isAffectedByGravity);
            physicsBody.setStatic(isStatic);
        }
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void cleanup() {
        if (physicsBody != null) {
            physicsBody = null;
        }
        if (collider != null) {
            collider = null;
        }
    }

    // --- Méthodes pour appliquer des forces ---

    /**
     * Applique une force au corps physique.
     * @param force Force à appliquer.
     */
    public void applyForce(Vector3f force) {
        if (physicsBody != null) {
            physicsBody.applyForce(force);
        }
    }

    /**
     * Applique une impulsion au corps physique.
     * @param impulse Impulsion à appliquer.
     */
    public void applyImpulse(Vector3f impulse) {
        if (physicsBody != null) {
            physicsBody.applyImpulse(impulse);
        }
    }

    // --- Getters et Setters ---

    public PhysicsBody getPhysicsBody() {
        return physicsBody;
    }

    public void setPhysicsBody(PhysicsBody physicsBody) {
        this.physicsBody = physicsBody;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
        if (physicsBody != null) {
            physicsBody.setCollider(collider);
        }
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if (physicsBody != null) {
            physicsBody.setMass(mass);
        }
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
        if (physicsBody != null) {
            physicsBody.setVelocity(velocity);
        }
    }

    public Vector3f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean isAffectedByGravity) {
        this.isAffectedByGravity = isAffectedByGravity;
        if (physicsBody != null) {
            physicsBody.setAffectedByGravity(isAffectedByGravity);
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        if (physicsBody != null) {
            physicsBody.setStatic(isStatic);
        }
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
        if (physicsBody != null) {
            physicsBody.setFriction(friction);
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
        if (physicsBody != null) {
            physicsBody.setRestitution(restitution);
        }
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PhysicsComponent{" +
                "mass=" + mass +
                ", velocity=" + velocity +
                ", isAffectedByGravity=" + isAffectedByGravity +
                ", isStatic=" + isStatic +
                '}';
    }
}