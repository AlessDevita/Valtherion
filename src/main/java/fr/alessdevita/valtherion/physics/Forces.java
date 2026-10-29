package fr.alessdevita.valtherion.physics;

import org.joml.Vector3f;

/**
 * Classe pour gérer les forces physiques dans Valtherion.
 * Définit les forces courantes comme la gravité, la friction, et les forces externes.
 */
public class Forces {

    // Constante de gravité (par défaut : 9.81 m/s², mais ajustée pour le jeu)
    public static final Vector3f GRAVITY = new Vector3f(0, -9.81f, 0);

    // Constante de gravité pour les mondes spécifiques (ex: espace, sous l'eau)
    public static final Vector3f ZERO_GRAVITY = new Vector3f(0, 0, 0);
    public static final Vector3f LOW_GRAVITY = new Vector3f(0, -3.0f, 0);
    public static final Vector3f HIGH_GRAVITY = new Vector3f(0, -20.0f, 0);
    public static final Vector3f UNDERWATER_GRAVITY = new Vector3f(0, -5.0f, 0);

    // Constante de friction par défaut
    public static final float DEFAULT_FRICTION = 0.2f;
    public static final float ICE_FRICTION = 0.05f;
    public static final float RUBBER_FRICTION = 0.8f;

    // Constante de restitution par défaut (rebond)
    public static final float DEFAULT_RESTITUTION = 0.5f;
    public static final float BOUNCY_RESTITUTION = 0.8f;
    public static final float NO_BOUNCE_RESTITUTION = 0.0f;

    /**
     * Applique la gravité à un corps physique.
     * @param body Corps physique.
     * @param gravity Vecteur de gravité.
     */
    public static void applyGravity(PhysicsBody body, Vector3f gravity) {
        if (body.isAffectedByGravity() && !body.isStatic()) {
            body.applyForce(new Vector3f(gravity).mul(body.getMass()));
        }
    }

    /**
     * Applique une force de friction à un corps physique.
     * @param body Corps physique.
     * @param friction Coefficient de friction.
     */
    public static void applyFriction(PhysicsBody body, float friction) {
        if (body.isStatic()) {
            return;
        }

        Vector3f velocity = body.getVelocity();
        if (velocity.length() > 0) {
            Vector3f frictionForce = new Vector3f(velocity).mul(-friction);
            body.applyForce(frictionForce);
        }
    }

    /**
     * Applique une force de ressort à un corps physique.
     * @param body Corps physique.
     * @param anchorPoint Point d'ancrage du ressort.
     * @param restLength Longueur au repos du ressort.
     * @param stiffness Raideur du ressort.
     */
    public static void applySpringForce(PhysicsBody body, Vector3f anchorPoint,
                                        float restLength, float stiffness) {
        Vector3f displacement = new Vector3f(
                (float) (body.getEntity().getX() - anchorPoint.x),
                (float) (body.getEntity().getY() - anchorPoint.y),
                (float) (body.getEntity().getZ() - anchorPoint.z)
        );

        float currentLength = displacement.length();
        float deltaLength = currentLength - restLength;

        Vector3f springForce = new Vector3f(displacement).normalize().mul(-stiffness * deltaLength);
        body.applyForce(springForce);
    }

    /**
     * Applique une force de traînée (drag) à un corps physique.
     * @param body Corps physique.
     * @param dragCoefficient Coefficient de traînée.
     */
    public static void applyDragForce(PhysicsBody body, float dragCoefficient) {
        if (body.isStatic()) {
            return;
        }

        Vector3f velocity = body.getVelocity();
        float speed = velocity.length();
        if (speed > 0) {
            Vector3f dragForce = new Vector3f(velocity).mul(-dragCoefficient * speed);
            body.applyForce(dragForce);
        }
    }

    /**
     * Applique une force de poussée à un corps physique.
     * @param body Corps physique.
     * @param direction Direction de la poussée.
     * @param magnitude Magnitude de la poussée.
     */
    public static void applyThrustForce(PhysicsBody body, Vector3f direction, float magnitude) {
        if (body.isStatic()) {
            return;
        }

        Vector3f thrustForce = new Vector3f(direction).normalize().mul(magnitude);
        body.applyForce(thrustForce);
    }

    /**
     * Applique une explosion à un corps physique.
     * @param body Corps physique.
     * @param explosionCenter Centre de l'explosion.
     * @param explosionRadius Rayon de l'explosion.
     * @param explosionStrength Force de l'explosion.
     */
    public static void applyExplosionForce(PhysicsBody body, Vector3f explosionCenter,
                                           float explosionRadius, float explosionStrength) {
        if (body.isStatic()) {
            return;
        }

        Vector3f displacement = new Vector3f(
                (float) (body.getEntity().getX() - explosionCenter.x),
                (float) (body.getEntity().getY() - explosionCenter.y),
                (float) (body.getEntity().getZ() - explosionCenter.z)
        );

        float distance = displacement.length();
        if (distance <= explosionRadius) {
            float forceMultiplier = 1.0f - (distance / explosionRadius);
            Vector3f explosionForce = new Vector3f(displacement).normalize().mul(explosionStrength * forceMultiplier);
            body.applyForce(explosionForce);
        }
    }
}