package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.block.BlockType;

/**
 * Boîte englobante pour les structures.
 * Définit un volume rectangulaire dans le monde.
 */
public class StructureBoundingBox {

    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;

    /**
     * Crée une boîte englobante vide.
     */
    public StructureBoundingBox() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Crée une boîte englobante avec les coordonnées spécifiées.
     * @param minX Coordonnée X minimale
     * @param minY Coordonnée Y minimale
     * @param minZ Coordonnée Z minimale
     * @param maxX Coordonnée X maximale
     * @param maxY Coordonnée Y maximale
     * @param maxZ Coordonnée Z maximale
     */
    public StructureBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    /**
     * Place un bloc dans la boîte englobante.
     * @param world Objet World pour placer le bloc
     * @param blockType Type de bloc
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     */
    public void placeBlockAt(Object world, BlockType blockType, int x, int y, int z) {
        int worldX = minX + x;
        int worldY = minY + y;
        int worldZ = minZ + z;

        if (isBlockInBounds(x, y, z)) {
            // Utilise reflection ou interface pour appeler world.setBlock
            try {
                java.lang.reflect.Method method = world.getClass().getMethod("setBlock", int.class, int.class, int.class, BlockType.class);
                method.invoke(world, worldX, worldY, worldZ, blockType);
            } catch (Exception e) {
                // Ignore les erreurs de placement
            }
        }
    }

    /**
     * Vérifie si un bloc est dans les limites de la boîte.
     * @param x Coordonnée X locale
     * @param y Coordonnée Y locale
     * @param z Coordonnée Z locale
     * @return true si le bloc est dans les limites
     */
    public boolean isBlockInBounds(int x, int y, int z) {
        return x >= 0 && x < getMaxX() - getMinX() &&
               y >= 0 && y < getMaxY() - getMinY() &&
               z >= 0 && z < getMaxZ() - getMinZ();
    }

    /**
     * Vérifie si cette boîte intersecte une autre boîte.
     * @param other L'autre boîte
     * @return true si les boîtes s'intersectent
     */
    public boolean intersects(StructureBoundingBox other) {
        return this.minX <= other.maxX && this.maxX >= other.minX &&
               this.minY <= other.maxY && this.maxY >= other.minY &&
               this.minZ <= other.maxZ && this.maxZ >= other.minZ;
    }

    /**
     * Fusionne cette boîte avec une autre.
     * @param other L'autre boîte à fusionner
     */
    public void merge(StructureBoundingBox other) {
        this.minX = Math.min(this.minX, other.minX);
        this.minY = Math.min(this.minY, other.minY);
        this.minZ = Math.min(this.minZ, other.minZ);
        this.maxX = Math.max(this.maxX, other.maxX);
        this.maxY = Math.max(this.maxY, other.maxY);
        this.maxZ = Math.max(this.maxZ, other.maxZ);
    }

    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }

    public int getWidth() { return maxX - minX + 1; }
    public int getHeight() { return maxY - minY + 1; }
    public int getDepth() { return maxZ - minZ + 1; }
}
