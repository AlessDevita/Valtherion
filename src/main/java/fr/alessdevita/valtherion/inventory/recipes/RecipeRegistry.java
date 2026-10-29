package fr.alessdevita.valtherion.inventory.recipes;

import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.ItemRegistry;

import java.util.*;

/**
 * Registre central pour toutes les recettes du jeu.
 * Gère les recettes de craft, de fusion, etc.
 */
public class RecipeRegistry {

    private static RecipeRegistry instance;

    // Map des recettes de craft (ID -> Recette)
    private final Map<String, CraftingRecipe> craftingRecipes;

    // Map des recettes de fusion (Input ID -> Recette)
    private final Map<String, SmeltingRecipe> smeltingRecipes;

    private RecipeRegistry() {
        this.craftingRecipes = new HashMap<>();
        this.smeltingRecipes = new HashMap<>();
    }

    public static synchronized RecipeRegistry getInstance() {
        if (instance == null) {
            instance = new RecipeRegistry();
        }
        return instance;
    }

    /**
     * Initialise les recettes par défaut du jeu.
     */
    public void initDefaultRecipes() {
        ItemRegistry ir = ItemRegistry.getInstance();

        // Exemple : Recette pour l'arc
        Item stick = ir.getItem("STICK");
        // Supposons qu'il existe un item STRING (fil) dans le registre
        Item string = ir.getItem("STRING");
        Item bowResult = ir.getItem("BOW");

        if (stick != null && string != null && bowResult != null) {
            Item[][] bowGrid = new Item[3][3];
            // Schéma simplifié d'un arc
            bowGrid[0][1] = stick;
            bowGrid[1][0] = stick;
            bowGrid[1][2] = string;
            bowGrid[2][1] = stick;
            
            registerCraftingRecipe(new CraftingRecipe("RECIPE_BOW", bowResult, bowGrid));
        }

        // Exemple : Recette de fusion (Fer)
        Item ironOre = ir.getItem("IRON_ORE");
        Item ironIngot = ir.getItem("IRON_INGOT");
        if (ironOre != null && ironIngot != null) {
            registerSmeltingRecipe(new SmeltingRecipe("SMELT_IRON", ironOre, ironIngot, 1));
        }

        System.out.println("Recettes par défaut enregistrées.");
    }

    /**
     * Enregistre une recette de craft.
     */
    public void registerCraftingRecipe(CraftingRecipe recipe) {
        craftingRecipes.put(recipe.getId(), recipe);
    }

    /**
     * Enregistre une recette de fusion.
     */
    public void registerSmeltingRecipe(SmeltingRecipe recipe) {
        smeltingRecipes.put(recipe.getInput().getId(), recipe);
    }

    /**
     * Récupère une recette de fusion à partir d'un item d'entrée.
     */
    public SmeltingRecipe getSmeltingRecipe(Item input) {
        return smeltingRecipes.get(input.getId());
    }

    public Collection<CraftingRecipe> getCraftingRecipes() {
        return craftingRecipes.values();
    }

    public Collection<SmeltingRecipe> getSmeltingRecipes() {
        return smeltingRecipes.values();
    }

    /**
     * Recherche une recette de craft correspondant à une grille donnée.
     */
    public CraftingRecipe findMatchingRecipe(Item[][] grid) {
        for (CraftingRecipe recipe : craftingRecipes.values()) {
            if (Arrays.deepEquals(recipe.getCraftingGrid(), grid)) {
                return recipe;
            }
        }
        return null;
    }
}
