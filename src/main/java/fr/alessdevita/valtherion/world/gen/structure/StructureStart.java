package fr.alessdevita.valtherion.world.gen.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Point de départ pour la génération des structures.
 * Contient la liste des composants d'une structure.
 */
public class StructureStart {

    protected List<StructureComponent> components;
    protected int x;
    protected int z;
    protected int size;
    protected boolean isFinishedStructure;
    protected boolean isStartedStructure;

    /**
     * Crée un point de départ de structure.
     */
    public StructureStart() {
        this.components = new ArrayList<>();
        this.x = 0;
        this.z = 0;
        this.size = 0;
        this.isFinishedStructure = false;
        this.isStartedStructure = false;
    }

    /**
     * Crée un point de départ de structure avec des coordonnées.
     * @param x Coordonnée X du chunk
     * @param z Coordonnée Z du chunk
     * @param size Taille de la structure
     */
    public StructureStart(int x, int z, int size) {
        this.components = new ArrayList<>();
        this.x = x;
        this.z = z;
        this.size = size;
        this.isFinishedStructure = false;
        this.isStartedStructure = false;
    }

    /**
     * Initialise la structure.
     * @param random Générateur aléatoire
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @param boundingBox Boîte englobante
     */
    public void init(java.util.Random random, int chunkX, int chunkZ, StructureBoundingBox boundingBox) {
        this.isStartedStructure = true;
    }

    /**
     * Génère la structure.
     * @return true si la génération a réussi
     */
    public boolean generateStructure() {
        this.isFinishedStructure = true;
        return true;
    }

    /**
     * Ajoute un composant à la structure.
     * @param component Le composant à ajouter
     */
    public void addComponent(StructureComponent component) {
        if (component != null) {
            components.add(component);
        }
    }

    public List<StructureComponent> getComponents() {
        return components;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getSize() {
        return size;
    }

    public boolean isFinishedStructure() {
        return isFinishedStructure;
    }

    public boolean isStartedStructure() {
        return isStartedStructure;
    }
}
