package fr.alessdevita.valtherion.physics;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.world.World;

import org.joml.Vector3f;

import java.util.*;

/**
 * Moteur physique principal pour Valtherion.
 * Gère les corps physiques, les collisions, et les forces.
 */
public class PhysicsEngine {

    // Liste des corps physiques
    private final List<PhysicsBody> bodies;

    // Gravité du monde
    private Vector3f gravity;

    // Liste des collisions détectées
    private final List<Collision> collisions;

    // Indique si le moteur physique est activé
    private boolean isEnabled;

    // Monde associé au moteur physique
    private World world;

    /**
     * Constructeur du moteur physique.
     */
    public PhysicsEngine() {
        this.bodies = new ArrayList<>();
        this.gravity = new Vector3f(Forces.GRAVITY);
        this.collisions = new ArrayList<>();
        this.isEnabled = true;
    }

    /**
     * Initialise le moteur physique.
     * @param world Monde associé.
     */
    public void init(World world) {
        this.world = world;

        // Ajouter les corps physiques des entités du monde
        for (Entity entity : world.getEntities()) {
            if (entity.hasPhysicsBody()) {
                addBody(entity.getPhysicsBody());
            }
        }
    }

    /**
     * Met à jour le moteur physique.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isEnabled) {
            return;
        }

        // Réinitialiser les collisions
        collisions.clear();

        // Appliquer les forces aux corps physiques
        for (PhysicsBody body : bodies) {
            if (body.isAffectedByGravity()) {
                Forces.applyGravity(body, gravity);
            }
        }

        // Détecter les collisions
        detectCollisions();

        // Résoudre les collisions
        resolveCollisions();

        // Mettre à jour les corps physiques
        for (PhysicsBody body : bodies) {
            body.update(deltaTime);
        }
    }

    /**
     * Détecte les collisions entre les corps physiques.
     */
    private void detectCollisions() {
        for (int i = 0; i < bodies.size(); i++) {
            PhysicsBody bodyA = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                PhysicsBody bodyB = bodies.get(j);

                if (bodyA.getCollider().intersects(bodyB.getCollider())) {
                    collisions.add(new Collision(bodyA, bodyB));
                }
            }
        }
    }

    /**
     * Résout les collisions détectées.
     */
    private void resolveCollisions() {
        for (Collision collision : collisions) {
            PhysicsBody bodyA = collision.getBodyA();
            PhysicsBody bodyB = collision.getBodyB();

            // Résoudre la collision
            bodyA.resolveCollision(bodyB, collision.getContactPoint());
        }
    }

    /**
     * Effectue un raycast dans le monde.
     * @param origin Point de départ du rayon.
     * @param direction Direction du rayon.
     * @param maxDistance Longueur maximale du rayon.
     * @return Résultat du raycast ou null si pas d'impact.
     */
    public Raycast.RaycastHit raycast(Vector3f origin, Vector3f direction, float maxDistance) {
        Raycast raycast = new Raycast(origin, direction, maxDistance);
        if (raycast.cast(bodies)) {
            return raycast.getHit();
        }
        return null;
    }

    /**
     * Ajoute un corps physique au moteur.
     * @param body Corps physique à ajouter.
     */
    public void addBody(PhysicsBody body) {
        if (body != null && !bodies.contains(body)) {
            bodies.add(body);
        }
    }

    /**
     * Retire un corps physique du moteur.
     * @param body Corps physique à retirer.
     */
    public void removeBody(PhysicsBody body) {
        bodies.remove(body);
    }

    /**
     * Définit la gravité du monde.
     * @param gravity Vecteur de gravité.
     */
    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    /**
     * Définit la gravité du monde à partir d'un type prédéfinis.
     * @param gravityType Type de gravité.
     */
    public void setGravity(Forces gravityType) {
        // À implémenter selon tes besoins
        // Exemple : this.gravity = new Vector3f(gravityType);
    }

    // --- Getters ---

    public List<PhysicsBody> getBodies() {
        return bodies;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public Collection<Collision> getCollisions() {
        return collisions;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public World getWorld() {
        return world;
    }

    /**
     * Nettoie les ressources du moteur physique.
     */
    public void cleanup() {
        bodies.clear();
        collisions.clear();
    }

}