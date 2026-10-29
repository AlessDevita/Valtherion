package fr.alessdevita.valtherion.inventory;

/**
 * Classe pour un slot dans l'inventaire de Valtherion.
 * Représente un emplacement dans l'inventaire qui peut contenir un objet.
 */
public class InventorySlot {

    // Index du slot dans l'inventaire
    private final int index;

    // Objet dans le slot
    private Item item;

    // Quantité de l'objet dans le slot
    private int quantity;

    // Indique si le slot est verrouillé (ne peut pas être modifié)
    private boolean isLocked;

    // Indique si le slot est sélectionné
    private boolean isSelected;

    /**
     * Constructeur du slot d'inventaire.
     * @param index Index du slot.
     */
    public InventorySlot(int index) {
        this.index = index;
        this.item = null;
        this.quantity = 0;
        this.isLocked = false;
        this.isSelected = false;
    }

    /**
     * Définit l'objet du slot.
     * @param item Objet à définir.
     */
    public void setItem(Item item) {
        if (!isLocked) {
            this.item = item;
        }
    }

    /**
     * Définit la quantité de l'objet.
     * @param quantity Quantité à définir.
     */
    public void setQuantity(int quantity) {
        if (!isLocked) {
            this.quantity = Math.max(0, quantity);
        }
    }

    /**
     * Ajoute une quantité à l'objet du slot.
     * @param quantity Quantité à ajouter.
     */
    public void addQuantity(int quantity) {
        if (!isLocked && item != null) {
            this.quantity = Math.min(this.quantity + quantity, item.getMaxStackSize());
        }
    }

    /**
     * Retire une quantité de l'objet du slot.
     * @param quantity Quantité à retirer.
     */
    public void removeQuantity(int quantity) {
        if (!isLocked) {
            this.quantity = Math.max(0, this.quantity - quantity);
            if (this.quantity == 0) {
                clear();
            }
        }
    }

    /**
     * Vide le slot.
     */
    public void clear() {
        if (!isLocked) {
            this.item = null;
            this.quantity = 0;
        }
    }

    /**
     * Vérifie si le slot est vide.
     * @return true si le slot est vide, false sinon.
     */
    public boolean isEmpty() {
        return item == null || quantity == 0;
    }

    /**
     * Vérifie si le slot est plein.
     * @return true si le slot est plein, false sinon.
     */
    public boolean isFull() {
        return item != null && quantity >= item.getMaxStackSize();
    }

    // --- Getters et Setters ---

    public int getIndex() {
        return index;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}