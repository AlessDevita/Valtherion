package fr.alessdevita.valtherion.item.crafting;

import fr.alessdevita.valtherion.item.ItemStack;

public class CraftingManager {
    private static final CraftingManager INSTANCE = new CraftingManager();

    public static CraftingManager getInstance() {
        return INSTANCE;
    }

    public ItemStack findMatchingRecipe(Object matrix, Object world) {
        return null;
    }
}
