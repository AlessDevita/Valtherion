package fr.alessdevita.valtherion.inventory.containers;

import fr.alessdevita.valtherion.inventory.Inventory;

/**
 * Représente un coffre dans Valtherion.
 * Un coffre est un conteneur simple possédant un inventaire.
 */
public class Chest {

    // Inventaire associé au coffre
    private final Inventory inventory;

    // Nom du coffre
    private String name;

    /**
     * Constructeur pour un coffre standard.
     * @param name Nom du coffre.
     * @param size Nombre de slots dans le coffre.
     */
    public Chest(String name, int size) {
        this.name = name;
        this.inventory = new Inventory(name, size);
    }

    /**
     * Récupère l'inventaire du coffre.
     * @return Inventaire.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Ouvre le coffre.
     */
    public void open() {
        System.out.println("Coffre '" + name + "' ouvert.");
        inventory.open();
    }

    /**
     * Ferme le coffre.
     */
    public void close() {
        System.out.println("Coffre '" + name + "' fermé.");
        inventory.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.inventory.setName(name);
    }
}
