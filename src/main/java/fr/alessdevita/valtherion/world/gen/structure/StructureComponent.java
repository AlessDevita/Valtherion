package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.block.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Composant de base pour les structures générées.
 * Chaque composant représente une partie d'une structure (pièce, couloir, etc.).
 */
public abstract class StructureComponent {

    protected StructureBoundingBox boundingBox;
    protected int componentType;
    protected int orientation;
    protected int depth;
    protected List<StructureComponent> components;
    protected StructureComponent componentAlreadyAdded;

    /**
     * Crée un composant de structure.
     */
    public StructureComponent() {
        this.boundingBox = null;
        this.componentType = 0;
        this.orientation = 0;
        this.depth = 0;
        this.components = new ArrayList<>();
        this.componentAlreadyAdded = null;
    }

    /**
     * Crée un composant de structure avec une boîte englobante.
     * @param boundingBox Boîte englobante du composant
     */
    public StructureComponent(StructureBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
        this.componentType = 0;
        this.orientation = 0;
        this.depth = 0;
        this.components = new ArrayList<>();
        this.componentAlreadyAdded = null;
    }

    /**
     * Génère le composant dans le monde.
     * @param world Monde où générer
     * @param random Générateur aléatoire
     * @return true si la génération a réussi
     */
    public abstract boolean generateStructure(World world, Random random);

    /**
     * Initialise le composant avec les données fournies.
     * @param boundingBox Boîte englobante
     * @param orientation Orientation du composant
     */
    public void init(StructureBoundingBox boundingBox, int orientation) {
        this.boundingBox = boundingBox;
        this.orientation = orientation;
    }

    /**
     * Ajoute un composant adjacent.
     * @param component Le composant à ajouter
     * @param direction Direction relative
     * @return Le composant ajouté
     */
    public StructureComponent addComponent(StructureComponent component, int direction) {
        if (component != null) {
            components.add(component);
            return component;
        }
        return null;
    }

    /**
     * Vérifie si le composant peut être généré à cette position.
     * @param components Liste des composants existants
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return true si le composant peut être généré
     */
    public static boolean canSpawnStructure(List<StructureComponent> components, int x, int y, int z) {
        StructureBoundingBox newBox = new StructureBoundingBox(x, y, z, x + 10, y + 10, z + 10);

        for (StructureComponent component : components) {
            if (component.boundingBox != null && component.boundingBox.intersects(newBox)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Place un bloc dans le monde.
     * @param world Monde
     * @param blockType Type de bloc
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     */
    protected void placeBlock(World world, BlockType blockType, int x, int y, int z) {
        if (boundingBox != null && boundingBox.isBlockInBounds(x, y, z)) {
            int worldX = boundingBox.getMinX() + x;
            int worldY = boundingBox.getMinY() + y;
            int worldZ = boundingBox.getMinZ() + z;
            world.setBlock(worldX, worldY, worldZ, blockType);
        }
    }

    public StructureBoundingBox getBoundingBox() {
        return boundingBox;
    }

    public int getComponentType() {
        return componentType;
    }

    public int getOrientation() {
        return orientation;
    }

    public List<StructureComponent> getComponents() {
        return components;
    }
}
