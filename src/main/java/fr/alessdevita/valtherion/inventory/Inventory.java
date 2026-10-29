package fr.alessdevita.valtherion.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    /** Hotbar/legacy view used by the client controller. */
    public final fr.alessdevita.valtherion.item.ItemStack[] mainInventory;
    public int currentItem;
    private String name;
    private final int size;
    private final InventorySlot[] slots;
    private float maxWeight;
    private float currentWeight;
    private boolean isOpen;
    private final List<InventoryListener> listeners;

    public Inventory(int size) {
        this.name = "Inventory";
        this.size = size;
        this.slots = new InventorySlot[size];
        this.mainInventory = new fr.alessdevita.valtherion.item.ItemStack[Math.max(size, 9)];
        this.currentItem = 0;
        this.maxWeight = 1000.0f;
        this.currentWeight = 0.0f;
        this.isOpen = false;
        this.listeners = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            slots[i] = new InventorySlot(i);
        }
    }

    public Inventory(String name, int size) {
        this(size);
        this.name = name;
    }

    public boolean addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }

        float itemWeight = (float) (item.getWeight() * quantity);
        if (currentWeight + itemWeight > maxWeight) {
            return false;
        }

        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getId().equals(item.getId())) {
                if (slot.getQuantity() + quantity <= item.getMaxStackSize()) {
                    slot.setQuantity((int) (slot.getQuantity() + quantity));
                    currentWeight += itemWeight;
                    notifyListeners();
                    return true;
                }
            }
        }

        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                slot.setItem(item);
                slot.setQuantity((int) Math.min(quantity, item.getMaxStackSize()));
                currentWeight += itemWeight;
                notifyListeners();
                return true;
            }
        }

        return false;
    }

    public boolean removeItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }

        // Retirer l'objet des slots
        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getId().equals(item.getId())) {
                int removedQuantity = Math.min(slot.getQuantity(), quantity);
                slot.setQuantity(slot.getQuantity() - removedQuantity);
                currentWeight -= item.getWeight() * removedQuantity;
                quantity -= removedQuantity;

                if (slot.getQuantity() == 0) {
                    slot.clear();
                }

                if (quantity == 0) {
                    notifyListeners();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Retire un objet de l'inventaire par son index de slot.
     * @param slotIndex Index du slot.
     * @param quantity Quantité de l'objet.
     * @return true si l'objet a été retiré, false sinon.
     */
    public boolean removeItem(int slotIndex, int quantity) {
        if (slotIndex < 0 || slotIndex >= size || quantity <= 0) {
            return false;
        }

        InventorySlot slot = slots[slotIndex];
        if (slot.isEmpty()) {
            return false;
        }

        Item item = slot.getItem();
        int removedQuantity = Math.min(slot.getQuantity(), quantity);
        slot.setQuantity(slot.getQuantity() - removedQuantity);
        currentWeight -= item.getWeight() * removedQuantity;

        if (slot.getQuantity() == 0) {
            slot.clear();
        }

        notifyListeners();
        return true;
    }

    /**
     * Déplace un objet d'un slot à un autre.
     * @param fromSlotIndex Index du slot source.
     * @param toSlotIndex Index du slot destination.
     * @return true si l'objet a été déplacé, false sinon.
     */
    public boolean moveItem(int fromSlotIndex, int toSlotIndex) {
        if (fromSlotIndex < 0 || fromSlotIndex >= size || toSlotIndex < 0 || toSlotIndex >= size) {
            return false;
        }

        InventorySlot fromSlot = slots[fromSlotIndex];
        InventorySlot toSlot = slots[toSlotIndex];

        if (fromSlot.isEmpty()) {
            return false;
        }

        // Si le slot destination est vide, déplacer l'objet
        if (toSlot.isEmpty()) {
            toSlot.setItem(fromSlot.getItem());
            toSlot.setQuantity(fromSlot.getQuantity());
            fromSlot.clear();
            notifyListeners();
            return true;
        }

        // Si le slot destination contient le même objet, fusionner les quantités
        if (toSlot.getItem().getId().equals(fromSlot.getItem().getId())) {
            int totalQuantity = fromSlot.getQuantity() + toSlot.getQuantity();
            if (totalQuantity <= fromSlot.getItem().getMaxStackSize()) {
                toSlot.setQuantity(totalQuantity);
                fromSlot.clear();
                notifyListeners();
                return true;
            }
        }

        // Échanger les objets entre les slots
        Item fromItem = fromSlot.getItem();
        int fromQuantity = fromSlot.getQuantity();

        fromSlot.setItem(toSlot.getItem());
        fromSlot.setQuantity(toSlot.getQuantity());

        toSlot.setItem(fromItem);
        toSlot.setQuantity(fromQuantity);

        notifyListeners();
        return true;
    }

    /**
     * Vérifie si l'inventaire contient un objet.
     * @param item Objet à vérifier.
     * @return true si l'objet est présent, false sinon.
     */
    public boolean hasItem(Item item) {
        if (item == null) {
            return false;
        }

        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getId().equals(item.getId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si l'inventaire contient un objet avec une quantité spécifique.
     * @param item Objet à vérifier.
     * @param quantity Quantité requise.
     * @return true si l'objet est présent avec la quantité requise, false sinon.
     */
    public boolean hasItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }

        int totalQuantity = 0;
        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getId().equals(item.getId())) {
                totalQuantity += slot.getQuantity();
                if (totalQuantity >= quantity) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Récupère un objet par son index de slot.
     * @param slotIndex Index du slot.
     * @return Objet ou null si le slot est vide.
     */
    public Item getItem(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= size) {
            return null;
        }
        return slots[slotIndex].getItem();
    }

    /**
     * Récupère la quantité d'un objet par son index de slot.
     * @param slotIndex Index du slot.
     * @return Quantité de l'objet.
     */
    public int getQuantity(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= size) {
            return 0;
        }
        return slots[slotIndex].getQuantity();
    }

    /**
     * Récupère le slot à un index donné.
     * @param slotIndex Index du slot.
     * @return Slot ou null si l'index est invalide.
     */
    public InventorySlot getSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= size) {
            return null;
        }
        return slots[slotIndex];
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
     * Notifie les observateurs de l'inventaire.
     */
    private void notifyListeners() {
        for (InventoryListener listener : listeners) {
            listener.onInventoryUpdate(this);
        }
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
     * Nettoie les ressources de l'inventaire.
     */
    public void cleanup() {
        for (InventorySlot slot : slots) {
            slot.clear();
        }
        listeners.clear();
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void clear() {
        for (InventorySlot slot : slots) slot.clear();
        java.util.Arrays.fill(mainInventory, null);
        currentWeight = 0.0f;
        notifyListeners();
    }

    public int getSelectedSlot() {
        return currentItem;
    }

    public fr.alessdevita.valtherion.item.ItemStack getCurrentItem() {
        return mainInventory[currentItem];
    }

    public void changeCurrentItem(int delta) {
        int length = Math.min(9, mainInventory.length);
        currentItem = Math.floorMod(currentItem + delta, length);
    }

    // --- Interface pour les observateurs de l'inventaire ---
    public interface InventoryListener {
        void onInventoryUpdate(Inventory inventory);
    }
}
