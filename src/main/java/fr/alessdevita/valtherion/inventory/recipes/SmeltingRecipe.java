package fr.alessdevita.valtherion.inventory.recipes;

import fr.alessdevita.valtherion.inventory.Item;

/**
 * Représente une recette de fusion (cuisson) dans Valtherion.
 * Utilisée principalement pour les fours.
 */
public class SmeltingRecipe {

    // ID unique de la recette
    private final String id;

    // Ingrédient d'entrée
    private final Item input;

    // Produit de sortie
    private final Item output;

    // Quantité produite
    private final int outputQuantity;

    // Expérience gagnée lors de la fusion
    private float experience;

    // Temps de cuisson en secondes
    private float smeltingTime;

    /**
     * Constructeur pour une recette de fusion.
     * @param id ID de la recette.
     * @param input Ingrédient à cuire.
     * @param output Résultat de la cuisson.
     * @param outputQuantity Quantité du résultat.
     */
    public SmeltingRecipe(String id, Item input, Item output, int outputQuantity) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.outputQuantity = outputQuantity;
        this.experience = 0.1f;
        this.smeltingTime = 10.0f; // 10 secondes par défaut
    }

    // --- Getters et Setters ---

    public String getId() {
        return id;
    }

    public Item getInput() {
        return input;
    }

    public Item getOutput() {
        return output;
    }

    public int getOutputQuantity() {
        return outputQuantity;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public float getSmeltingTime() {
        return smeltingTime;
    }

    public void setSmeltingTime(float smeltingTime) {
        this.smeltingTime = smeltingTime;
    }

    @Override
    public String toString() {
        return "SmeltingRecipe{" +
                "id='" + id + '\'' +
                ", input=" + input.getName() +
                ", output=" + output.getName() +
                " x" + outputQuantity +
                '}';
    }
}
