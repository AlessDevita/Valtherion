package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.core.Logger;

import java.util.*;

/**
 * Registre des entités pour Valtherion.
 * Gère l'enregistrement et la récupération des entités par leur ID ou leur type.
 */
public class EntityRegistry {

    // Logger pour les messages de debug
    private final Logger logger;

    // Map des entités par leur ID
    private final Map<Integer, Entity> entitiesById;

    // Map des entités par leur type
    private final Map<EntityType, List<Entity>> entitiesByType;

    // Compteur pour les IDs des entités
    private int nextEntityId;

    // Instance unique du registre (singleton)
    private static EntityRegistry instance;

    /**
     * Constructeur privé pour le singleton.
     */
    private EntityRegistry() {
        this.logger = new Logger("EntityRegistry");
        this.entitiesById = new HashMap<>();
        this.entitiesByType = new HashMap<>();
        this.nextEntityId = 1;
    }

    /**
     * Retourne l'instance unique du registre.
     * @return Instance de EntityRegistry.
     */
    public static EntityRegistry getInstance() {
        if (instance == null) {
            instance = new EntityRegistry();
        }
        return instance;
    }

    /**
     * Enregistre une entité.
     * @param entity Entité à enregistrer.
     */
    public void registerEntity(Entity entity) {
        if (entity == null) {
            logger.warn("Impossible d'enregistrer une entité nulle.");
            return;
        }

        // Enregistrer l'entité par son ID
        entitiesById.put(entity.getId(), entity);

        // Enregistrer l'entité par son type
        entitiesByType.computeIfAbsent(entity.getType(), k -> new ArrayList<>()).add(entity);

        logger.debug("Entité enregistrée : " + entity.getName() + " (ID: " + entity.getId() + ")");
    }

    /**
     * Désenregistre une entité.
     * @param entity Entité à désenregistrer.
     */
    public void unregisterEntity(Entity entity) {
        if (entity == null) {
            logger.warn("Impossible de désenregistrer une entité nulle.");
            return;
        }

        // Désenregistrer l'entité par son ID
        entitiesById.remove(entity.getId());

        // Désenregistrer l'entité par son type
        List<Entity> entities = entitiesByType.get(entity.getType());
        if (entities != null) {
            entities.remove(entity);
        }

        logger.debug("Entité désenregistrée : " + entity.getName() + " (ID: " + entity.getId() + ")");
    }

    /**
     * Récupère une entité par son ID.
     * @param id ID de l'entité.
     * @return Entité ou null si introuvable.
     */
    public Entity getEntityById(int id) {
        return entitiesById.get(id);
    }

    /**
     * Récupère toutes les entités d'un type donné.
     * @param type Type d'entité.
     * @return Liste des entités du type donné.
     */
    public List<Entity> getEntitiesByType(EntityType type) {
        return entitiesByType.getOrDefault(type, new ArrayList<>());
    }

    /**
     * Récupère toutes les entités.
     * @return Liste de toutes les entités.
     */
    public List<Entity> getAllEntities() {
        return new ArrayList<>(entitiesById.values());
    }

    /**
     * Vérifie si une entité est enregistrée.
     * @param entity Entité à vérifier.
     * @return true si l'entité est enregistrée, false sinon.
     */
    public boolean isEntityRegistered(Entity entity) {
        if (entity == null) {
            return false;
        }
        return entitiesById.containsKey(entity.getId());
    }

    /**
     * Génère un nouvel ID pour une entité.
     * @return Nouvel ID.
     */
    public int generateEntityId() {
        return nextEntityId++;
    }

    /**
     * Nettoie le registre (supprime toutes les entités).
     */
    public void clear() {
        entitiesById.clear();
        entitiesByType.clear();
        logger.info("Registre des entités nettoyé.");
    }
}
