package fr.alessdevita.valtherion.inventory;

import fr.alessdevita.valtherion.inventory.recipes.CraftingRecipe;

import java.util.*;

/**
 * Classe pour gérer les recettes de craft dans Valtherion.
 * Permet de registrer, rechercher, et craft des objets.
 */
public class CraftingManager {

    // Map des recettes de craft (ID -> recette)
    private final Map<String, CraftingRecipe> recipes;

    // Map des recettes par type de table de craft
    private final Map<CraftingRecipe.CraftingTableType, List<CraftingRecipe>> recipesByTableType;

    // Map des recettes par objet résultant
    private final Map<Item, List<CraftingRecipe>> recipesByResult;

    // Inventaire du joueur
    private Inventory inventory;

    /**
     * Constructeur du gestionnaire des recettes de craft.
     */
    public CraftingManager() {
        this.recipes = new HashMap<>();
        this.recipesByTableType = new HashMap<>();
        this.recipesByResult = new HashMap<>();
    }

    /**
     * Initialise les recettes de craft par défaut.
     */
    public void initDefaultRecipes() {
        // Exemple : Recette pour craft une épée en bois
        CraftingRecipe woodenSwordRecipe = getWoodenSwordRecipe();
        registerRecipe(woodenSwordRecipe);

        // Exemple : Recette pour craft un bloc de pierre
        Item stoneBlock = new Item("STONE_BLOCK", "Bloc de pierre", Item.ItemType.BLOCK);
        Item stone = new Item("STONE", "Pierre", Item.ItemType.MATERIAL);

        Map<Item, Integer> stoneBlockIngredients = new HashMap<>();
        stoneBlockIngredients.put(stone, 4);

        CraftingRecipe stoneBlockRecipe = new CraftingRecipe(
                "stone_block", stoneBlock, stoneBlockIngredients
        );
        stoneBlockRecipe.setResultQuantity(1);
        stoneBlockRecipe.setTableType(CraftingRecipe.CraftingTableType.CRAFTING_TABLE);
        registerRecipe(stoneBlockRecipe);
    }

    private static CraftingRecipe getWoodenSwordRecipe() {
        Item woodenSword = new Item("WOODEN_SWORD", "Épée en bois", Item.ItemType.WEAPON);
        woodenSword.setDamage(5);
        woodenSword.setDurability(100);

        Item stick = new Item("STICK", "Bâton", Item.ItemType.MATERIAL);
        Item plank = new Item("PLANK", "Planche en bois", Item.ItemType.MATERIAL);

        Map<Item, Integer> ingredients = new HashMap<>();
        ingredients.put(plank, 2);
        ingredients.put(stick, 1);

        CraftingRecipe woodenSwordRecipe = new CraftingRecipe(
                "wooden_sword", woodenSword, ingredients
        );
        woodenSwordRecipe.setTableType(CraftingRecipe.CraftingTableType.CRAFTING_TABLE);
        return woodenSwordRecipe;
    }

    /**
     * Enregistre une recette de craft.
     * @param recipe Recette à enregistrer.
     */
    public void registerRecipe(CraftingRecipe recipe) {
        if (recipe == null) {
            return;
        }

        recipes.put(recipe.getId(), recipe);

        // Ajouter la recette à la map des recettes par type de table
        recipesByTableType.computeIfAbsent(recipe.getTableType(), k -> new ArrayList<>()).add(recipe);

        // Ajouter la recette à la map des recettes par objet résultant
        recipesByResult.computeIfAbsent(recipe.getResult(), k -> new ArrayList<>()).add(recipe);
    }

    /**
     * Retire une recette de craft.
     * @param recipeId ID de la recette à retirer.
     */
    public void unregisterRecipe(String recipeId) {
        CraftingRecipe recipe = recipes.get(recipeId);
        if (recipe != null) {
            recipes.remove(recipeId);

            // Retirer la recette de la map des recettes par type de table
            List<CraftingRecipe> tableRecipes = recipesByTableType.get(recipe.getTableType());
            if (tableRecipes != null) {
                tableRecipes.remove(recipe);
            }

            // Retirer la recette de la map des recettes par objet résultant
            List<CraftingRecipe> resultRecipes = recipesByResult.get(recipe.getResult());
            if (resultRecipes != null) {
                resultRecipes.remove(recipe);
            }
        }
    }

    /**
     * Récupère une recette de craft par son ID.
     * @param recipeId ID de la recette.
     * @return Recette de craft ou null si non trouvée.
     */
    public CraftingRecipe getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }

    /**
     * Récupère toutes les recettes de craft.
     * @return Map des recettes de craft.
     */
    public Map<String, CraftingRecipe> getRecipes() {
        return recipes;
    }

    /**
     * Récupère les recettes de craft pour un type de table spécifique.
     * @param tableType Type de table de craft.
     * @return Liste des recettes de craft.
     */
    public List<CraftingRecipe> getRecipesByTableType(CraftingRecipe.CraftingTableType tableType) {
        return recipesByTableType.getOrDefault(tableType, new ArrayList<>());
    }

    /**
     * Récupère les recettes de craft pour un objet résultant spécifique.
     * @param result Objet résultant.
     * @return Liste des recettes de craft.
     */
    public List<CraftingRecipe> getRecipesByResult(Item result) {
        return recipesByResult.getOrDefault(result, new ArrayList<>());
    }

    /**
     * Définit l'inventaire du joueur.
     * @param inventory Inventaire du joueur.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Vérifie si un objet peut être crafté avec l'inventaire actuel.
     * @param item Objet à craft.
     * @return true si l'objet peut être crafté, false sinon.
     */
    public boolean canCraft(Item item) {
        if (inventory == null) {
            return false;
        }

        List<CraftingRecipe> recipes = recipesByResult.get(item);
        if (recipes == null) {
            return false;
        }

        for (CraftingRecipe recipe : recipes) {
            if (recipe.canCraft(inventory)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Craft un objet avec l'inventaire actuel.
     * @param item Objet à craft.
     * @return true si l'objet a été crafté, false sinon.
     */
    public boolean craft(Item item) {
        if (inventory == null) {
            return false;
        }

        List<CraftingRecipe> recipes = recipesByResult.get(item);
        if (recipes == null) {
            return false;
        }

        for (CraftingRecipe recipe : recipes) {
            if (recipe.canCraft(inventory)) {
                return recipe.craft(inventory);
            }
        }

        return false;
    }

    /**
     * Craft un objet avec une recette spécifique.
     * @param recipe Recette de craft.
     * @return true si l'objet a été crafté, false sinon.
     */
    public boolean craft(CraftingRecipe recipe) {
        if (inventory == null || recipe == null) {
            return false;
        }

        return recipe.craft(inventory);
    }

    /**
     * Récupère toutes les recettes de craft possibles avec l'inventaire actuel.
     * @return Liste des recettes de craft possibles.
     */
    public List<CraftingRecipe> getPossibleRecipes() {
        List<CraftingRecipe> possibleRecipes = new ArrayList<>();

        if (inventory == null) {
            return possibleRecipes;
        }

        for (CraftingRecipe recipe : recipes.values()) {
            if (recipe.canCraft(inventory)) {
                possibleRecipes.add(recipe);
            }
        }

        return possibleRecipes;
    }

    /**
     * Nettoie les ressources du gestionnaire des recettes de craft.
     */
    public void cleanup() {
        recipes.clear();
        recipesByTableType.clear();
        recipesByResult.clear();
    }
}