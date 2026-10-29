package fr.alessdevita.valtherion.api.events.entity;

import fr.alessdevita.valtherion.api.events.CancelableEvent;
import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

/**
 * Événement déclenché lorsqu'une entité apparaît dans le monde.
 * Peut être annulé pour empêcher l'apparition de l'entité.
 */
public class EntitySpawnEvent extends CancelableEvent {

    // Entité qui apparaît
    private final Entity entity;

    // Coordonnées de l'entité
    private final double x, y, z;

    // Type d'entité (ex: "ZOMBIE", "PLAYER", "CREEPER")
    private final String entityType;

    // Cause de l'apparition (ex: "NATURAL", "SPAWNER", "COMMAND")
    private String spawnCause;

    /**
     * Constructeur de l'événement d'apparition d'une entité.
     * @param entity Entité qui apparaît.
     * @param x Coordonnée X de l'entité.
     * @param y Coordonnée Y de l'entité.
     * @param z Coordonnée Z de l'entité.
     */
    public EntitySpawnEvent(Entity entity, double x, double y, double z) {
        super("EntitySpawnEvent");
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityType = entity != null ? entity.getClass().getSimpleName() : "UNKNOWN";
        this.spawnCause = "NATURAL"; // Cause par défaut
    }

    /**
     * Retourne l'entité qui apparaît.
     * @return Entité.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Retourne la coordonnée X de l'entité.
     * @return Coordonnée X.
     */
    public double getX() {
        return x;
    }

    /**
     * Retourne la coordonnée Y de l'entité.
     * @return Coordonnée Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Retourne la coordonnée Z de l'entité.
     * @return Coordonnée Z.
     */
    public double getZ() {
        return z;
    }

    /**
     * Retourne le type d'entité.
     * @return Type d'entité.
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Retourne la cause de l'apparition.
     * @return Cause de l'apparition.
     */
    public String getSpawnCause() {
        return spawnCause;
    }

    /**
     * Définit la cause de l'apparition.
     * @param spawnCause Cause de l'apparition.
     */
    public void setSpawnCause(String spawnCause) {
        this.spawnCause = spawnCause;
    }

    /**
     * Vérifie si l'entité est une entité vivante (ex: joueur, mob).
     * @return true si l'entité est vivante.
     */
    public boolean isLivingEntity() {
        return entity instanceof LivingEntity;
    }

    /**
     * Retourne l'entité sous forme de LivingEntity si applicable.
     * @return LivingEntity ou null.
     */
    public LivingEntity getLivingEntity() {
        return isLivingEntity() ? (LivingEntity) entity : null;
    }

    /**
     * Retourne une représentation textuelle de l'événement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntitySpawnEvent{" +
                "entityType='" + entityType + '\'' +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                ", spawnCause='" + spawnCause + '\'' +
                ", cancelled=" + isCancelled() +
                '}';
    }
}