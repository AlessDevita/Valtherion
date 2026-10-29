package fr.alessdevita.valtherion.inventory.containers;

import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.InventorySlot;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.recipes.RecipeRegistry;
import fr.alessdevita.valtherion.inventory.recipes.SmeltingRecipe;

/**
 * Représente un fourneau dans Valtherion.
 * Possède 3 slots : Entrée, Combustible, Sortie.
 */
public class Furnace {

    // Index des slots
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;

    private final Inventory inventory;

    // Progression de la cuisson (0.0 à 1.0)
    private float cookProgress;
    
    // Temps de combustion restant pour le combustible actuel
    private float burnTime;
    
    // Temps de combustion total du combustible actuel
    private float totalBurnTime;

    private boolean isBurning;

    public Furnace() {
        this.inventory = new Inventory("Fourneau", 3);
        this.cookProgress = 0.0f;
        this.burnTime = 0.0f;
        this.isBurning = false;
    }

    /**
     * Met à jour l'état du fourneau (tick).
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    public void update(float deltaTime) {
        boolean needsUpdate = false;

        if (burnTime > 0) {
            burnTime -= deltaTime;
            needsUpdate = true;
        }

        if (burnTime <= 0) {
            isBurning = false;
            // Essayer de consommer du nouveau combustible si on a quelque chose à cuire
            if (canSmelt()) {
                consumeFuel();
            }
        }

        if (isBurning && canSmelt()) {
            SmeltingRecipe recipe = RecipeRegistry.getInstance().getSmeltingRecipe(inventory.getItem(SLOT_INPUT));
            if (recipe != null) {
                cookProgress += deltaTime / recipe.getSmeltingTime();
                if (cookProgress >= 1.0f) {
                    smeltItem(recipe);
                    cookProgress = 0.0f;
                }
                needsUpdate = true;
            }
        } else {
            if (cookProgress > 0) {
                cookProgress = Math.max(0, cookProgress - deltaTime * 0.5f); // Refroidissement lent
                needsUpdate = true;
            }
        }
    }

    private boolean canSmelt() {
        Item input = inventory.getItem(SLOT_INPUT);
        if (input == null) return false;

        SmeltingRecipe recipe = RecipeRegistry.getInstance().getSmeltingRecipe(input);
        if (recipe == null) return false;

        InventorySlot outputSlot = inventory.getSlot(SLOT_OUTPUT);
        if (outputSlot.isEmpty()) return true;

        Item result = recipe.getOutput();
        return outputSlot.getItem().getId().equals(result.getId()) &&
               (outputSlot.getQuantity() + recipe.getOutputQuantity() <= result.getMaxStackSize());
    }

    private void consumeFuel() {
        InventorySlot fuelSlot = inventory.getSlot(SLOT_FUEL);
        if (!fuelSlot.isEmpty()) {
            // Ici, on devrait vérifier la valeur calorifique de l'item
            // Pour l'exemple, on considère que tout item en SLOT_FUEL dure 20 secondes
            this.burnTime = 20.0f; 
            this.totalBurnTime = 20.0f;
            this.isBurning = true;
            inventory.removeItem(SLOT_FUEL, 1);
        }
    }

    private void smeltItem(SmeltingRecipe recipe) {
        inventory.removeItem(SLOT_INPUT, 1);
        inventory.addItem(recipe.getOutput(), 1);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public float getCookProgress() {
        return cookProgress;
    }

    public float getBurnTime() {
        return burnTime;
    }

    public boolean isBurning() {
        return isBurning;
    }
}
