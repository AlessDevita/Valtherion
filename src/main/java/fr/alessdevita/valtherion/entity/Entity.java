package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.ecs.components.DamageComponent;
import fr.alessdevita.valtherion.physics.Collider;
import fr.alessdevita.valtherion.physics.PhysicsBody;
import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe de base pour toutes les entités dans Valtherion.
 * Une entité représente un objet ou une créature dans le monde.
 */
public abstract class Entity {

    // ID unique de l'entité
    private int id = 0;

    // Type de l'entité
    private EntityType type = null;

    // Position de l'entité
    private double x, y, z;

    // Vitesse de l'entité
    private double velocityX, velocityY, velocityZ;

    // Monde dans lequel l'entité existe
    private World world;

    // État de l'entité (active ou non)
    private boolean isActive;

    // Nom de l'entité (optionnel)
    private String name;

    // Compteur pour l'ID des entités
    private static int nextId = 1;

    /**
     * Constructeur de l'entité.
     */
    public Entity(EntityType entityType, double x, double y, double z) {
        this.id = nextId++;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isActive = true;
        this.name = "Entity_" + id;
    }

    public Entity() {

        this.id = id;
    }

    public Entity(String entityType, World world, int x, int y, int z) {
        this.id = nextId++;
        this.type = EntityType.valueOf(entityType);
    }

    public Entity(String id, String name) {
        this.id = parseId(id);
        this.name = name == null || name.isEmpty() ? "Entity_" + this.id : name;
        this.isActive = true;
    }

    private static int parseId(String id) {
        if (id != null) {
            try {
                return Integer.parseInt(id);
            } catch (NumberFormatException ignored) {
                // Named entity identifiers are not numeric; allocate a local ID.
            }
        }
        return nextId++;
    }

    public int getEntityId() {
        return this.id;
    }

    public void setEntityId(int entityId) {
        this.id = entityId;
    }

    /**
     * Met à jour l'entité.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        // Mettre à jour la position en fonction de la vitesse
        x += velocityX;
        y += velocityY;
        z += velocityZ;
    }

    /**
     * Rend l'entité.
     */
    public void render() {
        // Logique de rendu (à implémenter selon ta bibliothèque graphique)
    }

    /**
     * Détruit l'entité.
     */
    public void destroy() {
        this.isActive = false;
    }

    // --- Méthodes de mouvement ---

    /**
     * Déplace l'entité à une nouvelle position.
     * @param x Nouvelle coordonnée X.
     * @param y Nouvelle coordonnée Y.
     * @param z Nouvelle coordonnée Z.
     */
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Déplace l'entité de manière relative.
     * @param dx Déplacement en X.
     * @param dy Déplacement en Y.
     * @param dz Déplacement en Z.
     */
    public void move(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
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

    // --- Getters et Setters ---

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public abstract void update(float deltaTime);

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Calcul la distance entre cette entité et une autre.
     * @param other Autre entité.
     * @return Distance entre les deux entités.
     */
    public double getDistanceTo(Entity other) {
        if (other == null) {
            return Double.MAX_VALUE;
        }
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        double dz = this.z - other.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calcul la distance entre cette entité et des coordonnées.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return Distance entre l'entité et les coordonnées.
     */
    public double getDistanceTo(double x, double y, double z) {
        double dx = this.x - x;
        double dy = this.y - y;
        double dz = this.z - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Retourne une représentation textuelle de l'entité.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                '}';
    }

    public boolean isAlive() {
        return isActive;
    }

    public Vector3f getPosition() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public boolean hasPhysicsBody() {
        return false;
    }

    public PhysicsBody getPhysicsBody() {
        return null;
    }

    public Collider getCollider() {
        return null;
    }

    public void cleanup() {

    }

    public boolean hasComponent(Class<DamageComponent> damageComponentClass) {
        // Game entity doesn't support ECS components; return false to maintain compatibility
        return false;
    }

    protected void setPosition(Vector3f newPosition) {

    }
}
