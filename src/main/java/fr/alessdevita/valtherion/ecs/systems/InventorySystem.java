package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.InventoryComponent;
import fr.alessdevita.valtherion.inventory.Item;

import java.util.*;

/**
 * Système pour gérer les inventaires des entités dans Valtherion.
 */
public class InventorySystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant d'inventaire
    private List<Entity> inventoryEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système d'inventaire.
     */
    public InventorySystem() {
        this.inventoryEntities = new ArrayList<>();
        this.isEnabled = true;
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

        // Récupérer les entités avec le composant Inventory
        inventoryEntities = ecsManager.getEntitiesWithComponent(InventoryComponent.class);
    }

    /**
     * Ajoute un objet à l'inventaire d'une entité.
     * @param entityId ID de l'entité.
     * @param item Objet à ajouter.
     * @param quantity Quantité de l'objet.
     * @return true si l'objet a été ajouté, false sinon.
     */
    public boolean addItemToInventory(String entityId, Item item, int quantity) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            return inventoryComponent.addItem(item, quantity);
        }
        return false;
    }

    /**
     * Retire un objet de l'inventaire d'une entité.
     * @param entityId ID de l'entité.
     * @param item Objet à retirer.
     * @param quantity Quantité de l'objet.
     * @return true si l'objet a été retiré, false sinon.
     */
    public boolean removeItemFromInventory(String entityId, Item item, int quantity) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            return inventoryComponent.removeItem(item, quantity);
        }
        return false;
    }

    /**
     * Vérifie si une entité a un objet dans son inventaire.
     * @param entityId ID de l'entité.
     * @param item Objet à vérifier.
     * @return true si l'entité a l'objet, false sinon.
     */
    public boolean hasItemInInventory(String entityId, Item item) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            return inventoryComponent.hasItem(item);
        }
        return false;
    }

    /**
     * Ouvre l'inventaire d'une entité.
     * @param entityId ID de l'entité.
     */
    public void openInventory(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            inventoryComponent.open();
        }
    }

    /**
     * Ferme l'inventaire d'une entité.
     * @param entityId ID de l'entité.
     */
    public void closeInventory(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            inventoryComponent.close();
        }
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getInventoryEntities() {
        return inventoryEntities;
    }
}