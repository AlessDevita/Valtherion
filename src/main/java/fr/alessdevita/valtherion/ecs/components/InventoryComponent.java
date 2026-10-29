package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.quest.Quest;

import java.util.*;

/**
 * Composant pour l'inventaire d'une entité dans Valtherion.
 * Stocke les objets, la capacité, et les actions liées à l'inventaire.
 */
public class InventoryComponent implements Component {

    // Inventaire associé
    private Inventory inventory;

    // Capacité maximale de l'inventaire
    private int maxCapacity;

    // Poids maximal de l'inventaire
    private float maxWeight;

    // Indique si l'inventaire est ouvert
    private boolean isOpen;

    // Entité associée
    private Entity entity;

    // Liste des observateurs de l'inventaire
    private final List<InventoryListener> listeners;

    /**
     * Constructeur du composant d'inventaire.
     */
    public InventoryComponent() {
        this.inventory = new Inventory("Inventory", 36); // Inventaire par défaut avec 36 slots
        this.maxCapacity = 36;
        this.maxWeight = 100.0f;
        this.isOpen = false;
        this.listeners = new ArrayList<>();
    }

    /**
     * Constructeur du composant d'inventaire avec une taille.
     * @param size Taille de l'inventaire.
     */
    public InventoryComponent(int size) {
        this();
        this.inventory = new Inventory("Inventory", size);
        this.maxCapacity = size;
    }

    /**
     * Constructeur du composant d'inventaire avec une taille et un poids maximal.
     * @param size Taille de l'inventaire.
     * @param maxWeight Poids maximal.
     */
    public InventoryComponent(int size, float maxWeight) {
        this(size);
        this.maxWeight = maxWeight;
        this.inventory.setMaxWeight(maxWeight);
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
        if (inventory != null) {
            inventory.cleanup();
        }
        listeners.clear();
    }

    /**
     * Ajoute un objet à l'inventaire.
     * @param item Objet à ajouter.
     * @param quantity Quantité de l'objet.
     * @return true si l'objet a été ajouté, false sinon.
     */
    public boolean addItem(Item item, int quantity) {
        boolean success = inventory.addItem(item, quantity);
        if (success) {
            notifyListeners();
        }
        return success;
    }

    /**
     * Retire un objet de l'inventaire.
     * @param item Objet à retirer.
     * @param quantity Quantité de l'objet.
     * @return true si l'objet a été retiré, false sinon.
     */
    public boolean removeItem(Item item, int quantity) {
        boolean success = inventory.removeItem(item, quantity);
        if (success) {
            notifyListeners();
        }
        return success;
    }

    /**
     * Vérifie si l'inventaire contient un objet.
     * @param item Objet à vérifier.
     * @return true si l'inventaire contient l'objet, false sinon.
     */
    public boolean hasItem(Item item) {
        return inventory.hasItem(item);
    }

    /**
     * Vérifie si l'inventaire contient un objet avec une quantité spécifique.
     * @param item Objet à vérifier.
     * @param quantity Quantité requise.
     * @return true si l'inventaire contient l'objet avec la quantité, false sinon.
     */
    public boolean hasItem(Item item, int quantity) {
        return inventory.hasItem(item, quantity);
    }

    /**
     * Ouvre l'inventaire.
     */
    public void open() {
        isOpen = true;
        notifyListeners();
    }

    /**
     * Ferme l'inventaire.
     */
    public void close() {
        isOpen = false;
        notifyListeners();
    }

    /**
     * Bascule l'état de l'inventaire (ouvert/fermé).
     */
    public void toggle() {
        isOpen = !isOpen;
        notifyListeners();
    }

    /**
     * Ajoute un observateur de l'inventaire.
     * @param listener Observateur à ajouter.
     */
    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de l'inventaire.
     * @param listener Observateur à retirer.
     */
    public void removeListener(InventoryListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs de l'inventaire.
     */
    private void notifyListeners() {
        for (InventoryListener listener : listeners) {
            listener.onInventoryUpdate(this);
        }
    }

    // --- Getters et Setters ---

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
        if (inventory != null) {
            inventory.setMaxWeight(maxWeight);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "InventoryComponent{" +
                "inventory=" + inventory.getName() +
                ", maxCapacity=" + maxCapacity +
                ", maxWeight=" + maxWeight +
                ", isOpen=" + isOpen +
                '}';
    }

    public void addQuest(Quest quest) {

    }

    public void removeQuest(String questId) {

    }

    public boolean hasQuest(String questId) {
        return false;
    }

    // --- Interface pour les observateurs de l'inventaire ---
    public interface InventoryListener {
        void onInventoryUpdate(InventoryComponent inventoryComponent);
    }
}