package fr.alessdevita.valtherion.inventory.recipes;

import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;

import java.util.*;

/**
 * Classe pour une recette de craft dans Valtherion.
 * Définit comment combiner des objets pour créer un nouvel objet.
 */
public class CraftingRecipe {

    // ID unique de la recette
    private final String id;

    // Nom de la recette
    private String name;

    // Objet résultant de la recette
    private final Item result;

    // Quantité de l'objet résultant
    private int resultQuantity;

    // Grille de craft (3x3 par défaut)
    private final Item[][] craftingGrid;

    // Liste des ingrédients nécessaires (alternative à la grille)
    private final Map<Item, Integer> ingredients;

    // Temps nécessaire pour craft l'objet (en secondes)
    private float craftingTime;

    // Niveau requis pour craft l'objet
    private int requiredLevel;

    // Type de table de craft nécessaire (ex: CRAFTING_TABLE, FURNACE, etc.)
    private CraftingTableType tableType;

    // Énumération des types de tables de craft
    public enum CraftingTableType {
        CRAFTING_TABLE,  // Table de craft standard
        FURNACE,         // Fourneau
        ANVIL,          // Enclume
        BREWING_STAND,  // Support de potion
        WORKBENCH       // Établi
    }

    /**
     * Constructeur de la recette de craft avec une grille 3x3.
     * @param id ID unique de la recette.
     * @param result Objet résultant.
     * @param craftingGrid Grille de craft 3x3.
     */
    public CraftingRecipe(String id, Item result, Item[][] craftingGrid) {
        this.id = id;
        this.name = "Recette de " + result.getName();
        this.result = result;
        this.resultQuantity = 1;
        this.craftingGrid = craftingGrid;
        this.ingredients = new HashMap<>();
        this.craftingTime = 0.0f;
        this.requiredLevel = 1;
        this.tableType = CraftingTableType.CRAFTING_TABLE;

        // Extraire les ingrédients de la grille
        extractIngredientsFromGrid();
    }

    /**
     * Constructeur de la recette de craft avec une liste d'ingrédients.
     * @param id ID unique de la recette.
     * @param result Objet résultant.
     * @param ingredients Liste des ingrédients nécessaires.
     */
    public CraftingRecipe(String id, Item result, Map<Item, Integer> ingredients) {
        this.id = id;
        this.name = "Recette de " + result.getName();
        this.result = result;
        this.resultQuantity = 1;
        this.craftingGrid = new Item[3][3]; // Grille vide
        this.ingredients = new HashMap<>(ingredients);
        this.craftingTime = 0.0f;
        this.requiredLevel = 1;
        this.tableType = CraftingTableType.CRAFTING_TABLE;
    }

    /**
     * Extrait les ingrédients de la grille de craft.
     */
    private void extractIngredientsFromGrid() {
        for (int i = 0; i < craftingGrid.length; i++) {
            for (int j = 0; j < craftingGrid[i].length; j++) {
                Item item = craftingGrid[i][j];
                if (item != null) {
                    ingredients.put(item, ingredients.getOrDefault(item, 0) + 1);
                }
            }
        }
    }

    /**
     * Vérifie si la recette peut être craftée avec les objets donnés.
     * @param inventory Inventaire du joueur.
     * @return true si la recette peut être craftée, false sinon.
     */
    public boolean canCraft(Inventory inventory) {
        // Vérifier si le joueur a les ingrédients nécessaires
        for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
            Item ingredient = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (!inventory.hasItem(ingredient, requiredQuantity)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Craft l'objet résultant en utilisant les ingrédients de l'inventaire.
     * @param inventory Inventaire du joueur.
     * @return true si l'objet a été crafté, false sinon.
     */
    public boolean craft(Inventory inventory) {
        if (!canCraft(inventory)) {
            return false;
        }

        // Retirer les ingrédients de l'inventaire
        for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
            Item ingredient = entry.getKey();
            int requiredQuantity = entry.getValue();
            inventory.removeItem(ingredient, requiredQuantity);
        }

        // Ajouter l'objet résultant à l'inventaire
        inventory.addItem(result, 1);

        return true;
    }

    // --- Getters et Setters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item getResult() {
        return result;
    }

    public int getResultQuantity() {
        return resultQuantity;
    }

    public void setResultQuantity(int resultQuantity) {
        this.resultQuantity = resultQuantity;
    }

    public Item[][] getCraftingGrid() {
        return craftingGrid;
    }

    public Map<Item, Integer> getIngredients() {
        return ingredients;
    }

    public float getCraftingTime() {
        return craftingTime;
    }

    public void setCraftingTime(float craftingTime) {
        this.craftingTime = craftingTime;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public CraftingTableType getTableType() {
        return tableType;
    }

    public void setTableType(CraftingTableType tableType) {
        this.tableType = tableType;
    }

    /**
     * Retourne une représentation textuelle de la recette de craft.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "CraftingRecipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", result=" + result.getName() +
                ", resultQuantity=" + resultQuantity +
                ", ingredients=" + ingredients +
                '}';
    }
}