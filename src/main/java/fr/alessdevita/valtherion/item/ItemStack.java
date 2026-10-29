package fr.alessdevita.valtherion.item;

import fr.alessdevita.valtherion.client.gui.GuiEnchantment;

public class ItemStack {
    public Item item;
    public int stackSize;

    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.stackSize = quantity;
    }

    public static boolean areItemStacksEqual(ItemStack itemStack, ItemStack current) {
        if (itemStack == current) {
            return true;
        }
        if (itemStack == null || current == null) {
            return itemStack == current;
        }
        return itemStack.item == current.item && itemStack.stackSize == current.stackSize;
    }

    public Item getItem() {
        return this.item;
    }

    public void addEnchantment(GuiEnchantment.Enchantment selectedEnchantment) {
    }

    public ItemStack copy() {
        return new ItemStack(this.item, this.stackSize);
    }

    public void onCrafting(Object player, int amount) {
    }
}
