package fr.alessdevita.valtherion.inventory;

import fr.alessdevita.valtherion.client.gui.GuiButton;

/**
 * Classe pour une pile d'objets dans Valtherion.
 * Représente une pile d'objets identiques dans un slot d'inventaire.
 */
public class ItemStack {

    // Objet de la pile
    private final Item item;

    // Quantité d'objets dans la pile
    private int quantity;

    /**
     * Constructeur de la pile d'objets.
     * @param item Objet de la pile.
     * @param quantity Quantité d'objets.
     */
    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.quantity = Math.min(quantity, item.getMaxStackSize());
    }

    /**
     * Constructeur de la pile d'objets avec une quantité de 1.
     * @param item Objet de la pile.
     */
    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(String itemId, int quantity) {
        this.item = ItemRegistry.getItem(itemId);
        this.quantity = Math.min(quantity, item.getMaxStackSize());
    }

    public static boolean areItemStacksEqual(fr.alessdevita.valtherion.item.ItemStack itemStack, ItemStack current) {
        return false;
    }

    /**
     * Ajoute des objets à la pile.
     * @param quantity Quantité à ajouter.
     */
    public void add(int quantity) {
        this.quantity = Math.min(this.quantity + quantity, item.getMaxStackSize());
    }

    /**
     * Retire des objets de la pile.
     * @param quantity Quantité à retirer.
     */
    public void remove(int quantity) {
        this.quantity = Math.max(0, this.quantity - quantity);
    }

    /**
     * Vérifie si la pile est vide.
     * @return true si la pile est vide, false sinon.
     */
    public boolean isEmpty() {
        return quantity == 0;
    }

    /**
     * Vérifie si la pile est pleine.
     * @return true si la pile est pleine, false sinon.
     */
    public boolean isFull() {
        return quantity >= item.getMaxStackSize();
    }

    /**
     * Vérifie si la pile peut être fusionnée avec une autre pile.
     * @param other Pile à vérifier.
     * @return true si les piles peuvent être fusionnées, false sinon.
     */
    public boolean canMergeWith(ItemStack other) {
        if (other == null) {
            return false;
        }
        return item.getId().equals(other.getItem().getId()) && quantity + other.getQuantity() <= item.getMaxStackSize();
    }

    /**
     * Fusionne cette pile avec une autre pile.
     * @param other Pile à fusionner.
     */
    public void mergeWith(ItemStack other) {
        if (canMergeWith(other)) {
            quantity += other.getQuantity();
        }
    }

    // --- Getters et Setters ---

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, Math.min(quantity, item.getMaxStackSize()));
    }

    /**
     * Retourne une représentation textuelle de la pile d'objets.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ItemStack{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }

    public Item getItemId() {
        return item;
    }

    public int getCount() {
        return quantity;
    }

    public fr.alessdevita.valtherion.item.ItemStack copy() {
        return new fr.alessdevita.valtherion.item.ItemStack((fr.alessdevita.valtherion.item.Item) item, quantity);
    }
}