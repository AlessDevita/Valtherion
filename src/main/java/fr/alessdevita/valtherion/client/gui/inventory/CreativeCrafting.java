package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.item.ItemStack;

/**
 * CreativeCrafting — le conteneur de craft du mode creatif : un
 * InventoryCrafting 2x2 (ou 3x3 selon l'onglet) qui donne acces a
 * toutes les recettes sans table de craft, et ne consomme pas
 * d'ingredients (le resultat est copie).
 */
public class CreativeCrafting extends Container
{
    /** Inventaire de craft 3x3 (comme MC creatif). */
    public final InventoryCrafting craftMatrix;

    public CreativeCrafting(InventoryCrafting matrix)
    {
        this.craftMatrix = matrix;
        this.onCraftMatrixChanged(matrix);
    }

    @Override
    public void onCraftMatrixChanged(InventoryCrafting inventory)
    {
        // TODO : trouver le resultat de la recette correspondant a la
        // grille (CraftingManager.findMatchingRecipe) et le mettre dans
        // le slot resultat
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode)
    {
        // Le mode creatif copie au lieu de deplacer les items
        return super.slotClick(slotId, clickedButton, mode);
    }

    @Override
    public boolean canInteractWith(Object player)
    {
        return true; // toujours utilisable en creatif
    }
}