package fr.alessdevita.valtherion.item.crafting;

import fr.alessdevita.valtherion.item.ItemStack;

public class FurnaceRecipes {
    private static final FurnaceRecipes INSTANCE = new FurnaceRecipes();

    public static FurnaceRecipes instance() {
        return INSTANCE;
    }

    public ItemStack getSmeltingResult(ItemStack stack) {
        return null;
    }
}
