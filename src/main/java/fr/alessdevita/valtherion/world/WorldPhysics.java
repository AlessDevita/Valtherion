package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.physics.PhysicsEngine;
import fr.alessdevita.valtherion.physics.PhysicsBody;
import fr.alessdevita.valtherion.physics.Collider;
import fr.alessdevita.valtherion.physics.BoxCollider;
import fr.alessdevita.valtherion.physics.Forces;
import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;

import org.joml.Vector3f;

import java.util.*;

/**
 * Classe pour gérer les interactions physiques spécifiques au monde dans Valtherion.
 * Gère les collisions avec les blocs, les effets environnementaux, et les interactions entre les entités et le monde.
 */
public class WorldPhysics {

    // Monde associé
    private final World world;

    // Moteur physique associé
    private final PhysicsEngine physicsEngine;

    // Gravité du monde
    private Vector3f gravity;

    // Coefficient de friction par défaut pour le monde
    private float defaultFriction;

    // Coefficient de restitution par défaut pour le monde
    private float defaultRestitution;

    // Liste des zones avec des propriétés physiques spéciales (ex: eau, lave, glace)
    private final Map<String, PhysicsZone> physicsZones;

    // Classe pour représenter une zone avec des propriétés physiques spéciales
    public static class PhysicsZone {
        private final String name;
        private final Vector3f position;
        private final Vector3f size;
        private final Vector3f gravity;
        private final float friction;
        private final float restitution;

        public PhysicsZone(String name, Vector3f position, Vector3f size, Vector3f gravity, float friction, float restitution) {
            this.name = name;
            this.position = position;
            this.size = size;
            this.gravity = gravity;
            this.friction = friction;
            this.restitution = restitution;
        }

        public boolean contains(Vector3f point) {
            return point.x >= position.x && point.x <= position.x + size.x &&
                    point.y >= position.y && point.y <= position.y + size.y &&
                    point.z >= position.z && point.z <= position.z + size.z;
        }

        // --- Getters ---
        public String getName() {
            return name;
        }

        public Vector3f getPosition() {
            return position;
        }

        public Vector3f getSize() {
            return size;
        }

        public Vector3f getGravity() {
            return gravity;
        }

        public float getFriction() {
            return friction;
        }

        public float getRestitution() {
            return restitution;
        }
    }

    /**
     * Constructeur de la physique du monde.
     * @param world Monde associé.
     */
    public WorldPhysics(World world) {
        this.world = world;
        this.physicsEngine = world.getPhysicsEngine();
        this.gravity = world.getGravity();
        this.defaultFriction = 0.2f;
        this.defaultRestitution = 0.5f;
        this.physicsZones = new HashMap<>();

        // Initialiser les zones physiques par défaut
        initDefaultPhysicsZones();
    }

    /**
     * Initialise les zones physiques par défaut.
     */
    private void initDefaultPhysicsZones() {
        // Zone d'eau (gravité réduite, friction élevée)
        PhysicsZone waterZone = new PhysicsZone(
                "Water",
                new Vector3f(-100, 0, -100),
                new Vector3f(200, 10, 200),
                new Vector3f(0, -3.0f, 0), // Gravité réduite
                0.8f, // Friction élevée
                0.1f  // Restitution faible
        );
        physicsZones.put("Water", waterZone);

        // Zone de glace (friction très faible)
        PhysicsZone iceZone = new PhysicsZone(
                "Ice",
                new Vector3f(-50, 0, -50),
                new Vector3f(100, 1, 100),
                new Vector3f(0, -9.81f, 0), // Gravité normale
                0.05f, // Friction très faible
                0.3f  // Restitution moyenne
        );
        physicsZones.put("Ice", iceZone);

        // Zone de lave (gravité normale, friction moyenne, dégâts)
        PhysicsZone lavaZone = new PhysicsZone(
                "Lava",
                new Vector3f(0, 0, 0),
                new Vector3f(50, 10, 50),
                new Vector3f(0, -9.81f, 0), // Gravité normale
                0.5f, // Friction moyenne
                0.2f  // Restitution faible
        );
        physicsZones.put("Lava", lavaZone);
    }

    /**
     * Met à jour la physique du monde.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        // Mettre à jour les effets physiques sur les entités
        for (Entity entity : world.getEntities()) {
            if (entity.hasPhysicsBody()) {
                updateEntityPhysics(entity, deltaTime);
            }
        }
    }

    /**
     * Met à jour la physique d'une entité.
     * @param entity Entité à mettre à jour.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    private void updateEntityPhysics(Entity entity, float deltaTime) {
        PhysicsBody physicsBody = entity.getPhysicsBody();
        if (physicsBody == null) {
            return;
        }

        // Récupérer la position de l'entité
        Vector3f position = new Vector3f(
                (float) entity.getX(),
                (float) entity.getY(),
                (float) entity.getZ()
        );

        // Vérifier si l'entité est dans une zone physique spéciale
        PhysicsZone zone = getPhysicsZone(position);
        if (zone != null) {
            // Appliquer les propriétés de la zone
            physicsBody.setFriction(zone.getFriction());
            physicsBody.setRestitution(zone.getRestitution());

            // Appliquer la gravité de la zone
            Forces.applyGravity(physicsBody, zone.getGravity());
        } else {
            // Appliquer la gravité par défaut
            Forces.applyGravity(physicsBody, gravity);
        }

        // Vérifier les collisions avec les blocs
        checkBlockCollisions(entity, physicsBody);
    }

    /**
     * Vérifie les collisions entre une entité et les blocs du monde.
     * @param entity Entité à vérifier.
     * @param physicsBody Corps physique de l'entité.
     */
    private void checkBlockCollisions(Entity entity, PhysicsBody physicsBody) {
        Collider collider = physicsBody.getCollider();
        if (!(collider instanceof BoxCollider)) {
            return;
        }

        BoxCollider boxCollider = (BoxCollider) collider;
        Vector3f colliderMin = boxCollider.getAABB().getMin();
        Vector3f colliderMax = boxCollider.getAABB().getMax();

        // Calculer les coordonnées minimales et maximales de l'entité
        int minX = (int) Math.floor(colliderMin.x);
        int minY = (int) Math.floor(colliderMin.y);
        int minZ = (int) Math.floor(colliderMin.z);
        int maxX = (int) Math.ceil(colliderMax.x);
        int maxY = (int) Math.ceil(colliderMax.y);
        int maxZ = (int) Math.ceil(colliderMax.z);

        // Vérifier les collisions avec les blocs dans la zone de l'entité
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block != null && block.isSolid()) {
                        // Résoudre la collision avec le bloc
                        resolveBlockCollision(entity, physicsBody, x, y, z, block);
                    }
                }
            }
        }
    }

    /**
     * Résout une collision entre une entité et un bloc.
     * @param entity Entité en collision.
     * @param physicsBody Corps physique de l'entité.
     * @param blockX Coordonnée X du bloc.
     * @param blockY Coordonnée Y du bloc.
     * @param blockZ Coordonnée Z du bloc.
     * @param block Bloc en collision.
     */
    private void resolveBlockCollision(Entity entity, PhysicsBody physicsBody, int blockX, int blockY, int blockZ, Block block) {
        Collider collider = physicsBody.getCollider();
        if (!(collider instanceof BoxCollider)) {
            return;
        }

        BoxCollider boxCollider = (BoxCollider) collider;

        // Calculer le vecteur de poussée pour résoudre la collision
        Vector3f pushVector = calculatePushVector(boxCollider, blockX, blockY, blockZ);
        if (pushVector == null) {
            return;
        }

        // Appliquer la poussée à l'entité
        entity.move(pushVector.x, pushVector.y, pushVector.z);

        // Mettre à jour la position du collisionneur
        physicsBody.getCollider().setPosition(new Vector3f(
                (float) entity.getX(),
                (float) entity.getY(),
                (float) entity.getZ()
        ));

        // Si l'entité est une entité vivante, appliquer des effets supplémentaires
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            applyBlockEffects(livingEntity, block);
        }
    }

    /**
     * Calcule le vecteur de poussée pour résoudre une collision avec un bloc.
     * @param boxCollider Collisionneur de l'entité.
     * @param blockX Coordonnée X du bloc.
     * @param blockY Coordonnée Y du bloc.
     * @param blockZ Coordonnée Z du bloc.
     * @return Vecteur de poussée ou null si pas de collision.
     */
    private Vector3f calculatePushVector(BoxCollider boxCollider, int blockX, int blockY, int blockZ) {
        // Calculer les coordonnées du bloc
        float blockMinX = blockX;
        float blockMinY = blockY;
        float blockMinZ = blockZ;
        float blockMaxX = blockX + 1;
        float blockMaxY = blockY + 1;
        float blockMaxZ = blockZ + 1;

        // Calculer les chevauchements sur chaque axe
        float overlapX = Math.min(boxCollider.getAABB().getMax().x, blockMaxX) - Math.max(boxCollider.getAABB().getMin().x, blockMinX);
        float overlapY = Math.min(boxCollider.getAABB().getMax().y, blockMaxY) - Math.max(boxCollider.getAABB().getMin().y, blockMinY);
        float overlapZ = Math.min(boxCollider.getAABB().getMax().z, blockMaxZ) - Math.max(boxCollider.getAABB().getMin().z, blockMinZ);

        // Si pas de chevauchement, pas de collision
        if (overlapX <= 0 || overlapY <= 0 || overlapZ <= 0) {
            return null;
        }

        // Trouver l'axe avec le plus petit chevauchement
        if (overlapX < overlapY && overlapX < overlapZ) {
            // Pousser sur l'axe X
            float pushX = (boxCollider.getAABB().getCenter().x < blockX + 0.5f) ? -overlapX : overlapX;
            return new Vector3f(pushX, 0, 0);
        } else if (overlapY < overlapZ) {
            // Pousser sur l'axe Y
            float pushY = (boxCollider.getAABB().getCenter().y < blockY + 0.5f) ? -overlapY : overlapY;
            return new Vector3f(0, pushY, 0);
        } else {
            // Pousser sur l'axe Z
            float pushZ = (boxCollider.getAABB().getCenter().z < blockZ + 0.5f) ? -overlapZ : overlapZ;
            return new Vector3f(0, 0, pushZ);
        }
    }

    /**
     * Applique les effets d'un bloc sur une entité vivante.
     * @param livingEntity Entité vivante.
     * @param block Bloc en collision.
     */
    private void applyBlockEffects(LivingEntity livingEntity, Block block) {
        BlockType blockType = block.getType();

        switch (blockType) {
            case LAVA:
                // Appliquer des dégâts de lave
                livingEntity.damage(5.0f);
                break;
            case FIRE:
                // Appliquer des dégâts de feu
                livingEntity.damage(2.0f);
                break;
            case WATER:
                // Éteindre le feu sur l'entité
                livingEntity.extinguish();
                break;
            case ICE:
                // Ralentir l'entité
                livingEntity.setSpeedMultiplier(0.5f);
                break;
            default:
                // Aucun effet
                break;
        }
    }

    /**
     * Récupère la zone physique pour une position donnée.
     * @param position Position à vérifier.
     * @return Zone physique ou null si non trouvée.
     */
    public PhysicsZone getPhysicsZone(Vector3f position) {
        for (PhysicsZone zone : physicsZones.values()) {
            if (zone.contains(position)) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Ajoute une zone physique.
     * @param zone Zone physique à ajouter.
     */
    public void addPhysicsZone(PhysicsZone zone) {
        if (zone != null) {
            physicsZones.put(zone.getName(), zone);
        }
    }

    /**
     * Retire une zone physique.
     * @param zoneName Nom de la zone physique à retirer.
     */
    public void removePhysicsZone(String zoneName) {
        physicsZones.remove(zoneName);
    }

    /**
     * Vérifie si une position est dans l'eau.
     * @param position Position à vérifier.
     * @return true si la position est dans l'eau, false sinon.
     */
    public boolean isInWater(Vector3f position) {
        PhysicsZone waterZone = physicsZones.get("Water");
        return waterZone != null && waterZone.contains(position);
    }

    /**
     * Vérifie si une position est dans la lave.
     * @param position Position à vérifier.
     * @return true si la position est dans la lave, false sinon.
     */
    public boolean isInLava(Vector3f position) {
        PhysicsZone lavaZone = physicsZones.get("Lava");
        return lavaZone != null && lavaZone.contains(position);
    }

    /**
     * Vérifie si une position est sur la glace.
     * @param position Position à vérifier.
     * @return true si la position est sur la glace, false sinon.
     */
    public boolean isOnIce(Vector3f position) {
        PhysicsZone iceZone = physicsZones.get("Ice");
        return iceZone != null && iceZone.contains(position);
    }

    // --- Getters et Setters ---

    public World getWorld() {
        return world;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
        world.setGravity(gravity);
    }

    public float getDefaultFriction() {
        return defaultFriction;
    }

    public void setDefaultFriction(float defaultFriction) {
        this.defaultFriction = defaultFriction;
    }

    public float getDefaultRestitution() {
        return defaultRestitution;
    }

    public void setDefaultRestitution(float defaultRestitution) {
        this.defaultRestitution = defaultRestitution;
    }

    public Map<String, PhysicsZone> getPhysicsZones() {
        return physicsZones;
    }
}