package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import org.joml.Vector3f;

/**
 * Classe pour les événements du monde dans Valtherion.
 * Représente les événements qui se produisent dans le monde (changements de temps, météo, etc.).
 */
public class WorldEvent {

    // Type de l'événement
    private final WorldEventType type;

    // Monde associé à l'événement
    private final World world;

    // Données supplémentaires de l'événement
    private Object data;

    // Temps de l'événement (en secondes)
    private float time;

    // Position associée à l'événement (optionnelle)
    private Vector3f position;

    // Entité associée à l'événement (optionnelle)
    private Entity entity;

    // Chunk associé à l'événement (optionnelle)
    private Chunk chunk;

    // Énumération des types d'événements du monde
    public enum WorldEventType {
        // --- Événements de temps ---
        TIME_CHANGED,          // Le temps a changé
        DAY_STARTED,           // Le jour a commencé
        NIGHT_STARTED,         // La nuit a commencé
        DAWN_STARTED,          // L'aube a commencé
        DUSK_STARTED,          // Le crépuscule a commencé

        // --- Événements de météo ---
        WEATHER_CHANGED,       // La météo a changé
        RAIN_STARTED,          // La pluie a commencé
        RAIN_STOPPED,          // La pluie s'est arrêtée
        SNOW_STARTED,          // La neige a commencé
        SNOW_STOPPED,          // La neige s'est arrêtée
        STORM_STARTED,         // La tempête a commencé
        STORM_STOPPED,         // La tempête s'est arrêtée

        // --- Événements de chunks ---
        CHUNK_LOADED,          // Un chunk a été chargé
        CHUNK_UNLOADED,        // Un chunk a été déchargé
        CHUNK_GENERATED,       // Un chunk a été généré

        // --- Événements d'entités ---
        ENTITY_ADDED,          // Une entité a été ajoutée
        ENTITY_REMOVED,        // Une entité a été retirée
        ENTITY_SPAWNED,        // Une entité est apparue
        ENTITY_DESPAWNED,      // Une entité a disparu

        // --- Événements de blocs ---
        BLOCK_PLACED,          // Un bloc a été placé
        BLOCK_BROKEN,          // Un bloc a été cassé
        BLOCK_UPDATED,         // Un bloc a été mis à jour

        // --- Événements globaux ---
        WORLD_INITIALIZED,     // Le monde a été initialisé
        WORLD_CLEANED,         // Le monde a été nettoyé
        WORLD_SAVED,           // Le monde a été sauvegardé
        WORLD_LOADED           // Le monde a été chargé
    }

    /**
     * Constructeur de l'événement du monde.
     * @param type Type de l'événement.
     * @param world Monde associé.
     */
    public WorldEvent(WorldEventType type, World world) {
        this.type = type;
        this.world = world;
        this.data = null;
        this.time = world != null ? world.getWorldTime() : 0.0f;
        this.position = null;
        this.entity = null;
        this.chunk = null;
    }

    /**
     * Constructeur de l'événement du monde avec des données supplémentaires.
     * @param type Type de l'événement.
     * @param world Monde associé.
     * @param data Données supplémentaires.
     */
    public WorldEvent(WorldEventType type, World world, Object data) {
        this(type, world);
        this.data = data;
    }

    /**
     * Constructeur de l'événement du monde avec une position.
     * @param type Type de l'événement.
     * @param world Monde associé.
     * @param position Position associée.
     */
    public WorldEvent(WorldEventType type, World world, Vector3f position) {
        this(type, world);
        this.position = position;
    }

    /**
     * Constructeur de l'événement du monde avec une entité.
     * @param type Type de l'événement.
     * @param world Monde associé.
     * @param entity Entité associée.
     */
    public WorldEvent(WorldEventType type, World world, Entity entity) {
        this(type, world);
        this.entity = entity;
        if (entity != null) {
            this.position = new Vector3f(
                    (float) entity.getX(),
                    (float) entity.getY(),
                    (float) entity.getZ()
            );
        }
    }

    /**
     * Constructeur de l'événement du monde avec un chunk.
     * @param type Type de l'événement.
     * @param world Monde associé.
     * @param chunk Chunk associé.
     */
    public WorldEvent(WorldEventType type, World world, Chunk chunk) {
        this(type, world);
        this.chunk = chunk;
        if (chunk != null) {
            this.position = new Vector3f(
                    chunk.getX() * world.getChunkSize(),
                    0,
                    chunk.getZ() * world.getChunkSize()
            );
        }
    }

    // --- Getters et Setters ---

    public WorldEventType getType() {
        return type;
    }

    public World getWorld() {
        return world;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    /**
     * Retourne une représentation textuelle de l'événement du monde.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "WorldEvent{" +
                "type=" + type +
                ", world=" + (world != null ? world.getName() : "null") +
                ", time=" + time +
                ", position=" + position +
                ", entity=" + (entity != null ? entity.getId() : "null") +
                ", chunk=" + (chunk != null ? chunk.getX() + "," + chunk.getZ() : "null") +
                '}';
    }

    // --- Méthodes statiques pour créer des événements courants ---

    /**
     * Crée un événement de changement de temps.
     * @param world Monde associé.
     * @param newTime Nouveau temps.
     * @return Événement de changement de temps.
     */
    public static WorldEvent createTimeChangedEvent(World world, float newTime) {
        return new WorldEvent(WorldEventType.TIME_CHANGED, world, newTime);
    }

    /**
     * Crée un événement de début de jour.
     * @param world Monde associé.
     * @return Événement de début de jour.
     */
    public static WorldEvent createDayStartedEvent(World world) {
        return new WorldEvent(WorldEventType.DAY_STARTED, world);
    }

    /**
     * Crée un événement de début de nuit.
     * @param world Monde associé.
     * @return Événement de début de nuit.
     */
    public static WorldEvent createNightStartedEvent(World world) {
        return new WorldEvent(WorldEventType.NIGHT_STARTED, world);
    }

    /**
     * Crée un événement de changement de météo.
     * @param world Monde associé.
     * @param newState Nouveau état du monde.
     * @return Événement de changement de météo.
     */
    public static WorldEvent createWeatherChangedEvent(World world, World.WorldState newState) {
        return new WorldEvent(WorldEventType.WEATHER_CHANGED, world, newState);
    }

    /**
     * Crée un événement de chargement de chunk.
     * @param world Monde associé.
     * @param chunk Chunk chargé.
     * @return Événement de chargement de chunk.
     */
    public static WorldEvent createChunkLoadedEvent(World world, Chunk chunk) {
        return new WorldEvent(WorldEventType.CHUNK_LOADED, world, chunk);
    }

    /**
     * Crée un événement d'ajout d'entité.
     * @param world Monde associé.
     * @param entity Entité ajoutée.
     * @return Événement d'ajout d'entité.
     */
    public static WorldEvent createEntityAddedEvent(World world, Entity entity) {
        return new WorldEvent(WorldEventType.ENTITY_ADDED, world, entity);
    }

    /**
     * Crée un événement de placement de bloc.
     * @param world Monde associé.
     * @param position Position du bloc.
     * @param blockType Type de bloc.
     * @return Événement de placement de bloc.
     */
    public static WorldEvent createBlockPlacedEvent(World world, Vector3f position, Object blockType) {
        WorldEvent event = new WorldEvent(WorldEventType.BLOCK_PLACED, world, position);
        event.setData(blockType);
        return event;
    }

    /**
     * Crée un événement de casse de bloc.
     * @param world Monde associé.
     * @param position Position du bloc.
     * @param blockType Type de bloc.
     * @return Événement de casse de bloc.
     */
    public static WorldEvent createBlockBrokenEvent(World world, Vector3f position, Object blockType) {
        WorldEvent event = new WorldEvent(WorldEventType.BLOCK_BROKEN, world, position);
        event.setData(blockType);
        return event;
    }
}