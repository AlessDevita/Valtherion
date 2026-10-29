package fr.alessdevita.valtherion.item;

public class Item {
    private int maxStackSize = 64;
    private int armorSlot = -1;

    public boolean isValidArmor(ItemStack stack, int armorSlot, Object player) {
        return stack != null && stack.getItem() == this && this.armorSlot == armorSlot;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int size) {
        if (size < 1) throw new IllegalArgumentException("La taille de pile doit être positive");
        this.maxStackSize = size;
    }

    public Item setArmorSlot(int slot) {
        if (slot < -1 || slot > 3) throw new IllegalArgumentException("Emplacement d'armure invalide");
        this.armorSlot = slot;
        return this;
    }
}
