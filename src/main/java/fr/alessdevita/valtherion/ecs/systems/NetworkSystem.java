package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.NetworkComponent;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;

import java.util.*;

/**
 * Système pour gérer la synchronisation réseau des entités dans Valtherion.
 */
public class NetworkSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant réseau
    private List<Entity> networkEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Indique si ce système est exécuté côté serveur
    private boolean isServer;

    /**
     * Constructeur du système réseau.
     */
    public NetworkSystem() {
        this.networkEntities = new ArrayList<>();
        this.isEnabled = true;
        this.isServer = false;
    }

    /**
     * Constructeur du système réseau avec un mode.
     * @param isServer true si le système est exécuté côté serveur.
     */
    public NetworkSystem(boolean isServer) {
        this();
        this.isServer = isServer;
    }

    @Override
    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    @Override
    public ECSManager getECSManager() {
        return ecsManager;
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec le composant Network
        networkEntities = ecsManager.getEntitiesWithComponent(NetworkComponent.class);

        // Synchroniser les entités
        for (Entity entity : networkEntities) {
            NetworkComponent networkComponent = entity.getComponent(NetworkComponent.class);
            if (networkComponent != null && networkComponent.isSynced()) {
                syncEntity(entity, networkComponent);
            }
        }
    }

    /**
     * Synchronise une entité.
     * @param entity Entité à synchroniser.
     * @param networkComponent Composant réseau de l'entité.
     */
    private void syncEntity(Entity entity, NetworkComponent networkComponent) {
        if (isServer) {
            // Côté serveur : Envoyer les données des entités aux clients
            // Exemple : NetworkManager.sendEntitySync(entity, networkComponent.getSyncData());
        } else {
            // Côté client : Recevoir les données des entités du serveur
            // Exemple : NetworkManager.receiveEntitySync(entity, networkComponent.getSyncData());
        }
    }

    /**
     * Crée une entité côté serveur et la synchronise avec les clients.
     * @param entityId ID de l'entité.
     * @return Nouvelle entité.
     */
    public Entity createNetworkEntity(String entityId) {
        if (!isServer) {
            return null;
        }

        Entity entity = ecsManager.createEntity(entityId);
        NetworkComponent networkComponent = new NetworkComponent(entityId);
        entity.addComponent(networkComponent);

        // Synchroniser l'entité avec les clients
        // Exemple : NetworkManager.sendEntityCreation(entity);

        return entity;
    }

    /**
     * Détruit une entité côté serveur et la synchronise avec les clients.
     * @param entityId ID de l'entité.
     */
    public void destroyNetworkEntity(String entityId) {
        if (!isServer) {
            return;
        }

        // Synchroniser la destruction de l'entité avec les clients
        // Exemple : NetworkManager.sendEntityDestruction(entityId);

        ecsManager.destroyEntity(entityId);
    }

    /**
     * Ajoute un composant à synchroniser pour une entité.
     * @param entityId ID de l'entité.
     * @param componentClass Classe du composant.
     */
    public void addSyncedComponent(String entityId, Class<? extends fr.alessdevita.valtherion.ecs.Component> componentClass) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(NetworkComponent.class)) {
            NetworkComponent networkComponent = entity.getComponent(NetworkComponent.class);
            networkComponent.addSyncedComponent(componentClass);
        }
    }

    /**
     * Définit si le système est exécuté côté serveur.
     * @param isServer true si le système est exécuté côté serveur.
     */
    public void setServer(boolean isServer) {
        this.isServer = isServer;
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isServer() {
        return isServer;
    }

    public List<Entity> getNetworkEntities() {
        return networkEntities;
    }
}