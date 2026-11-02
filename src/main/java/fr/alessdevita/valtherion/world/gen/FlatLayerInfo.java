package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.block.BlockType;

/**
 * Définit une couche de blocs pour un monde plat.
 */
public class FlatLayerInfo {

    private final int thickness;
    private final BlockType blockType;
    private final int startHeight;

    /**
     * Crée une couche de blocs.
     * @param thickness Épaisseur de la couche en blocs
     * @param blockType Type de bloc
     */
    public FlatLayerInfo(int thickness, BlockType blockType) {
        this(thickness, blockType, 0);
    }

    /**
     * Crée une couche de blocs avec une hauteur de départ.
     * @param thickness Épaisseur de la couche
     * @param blockType Type de bloc
     * @param startHeight Hauteur de départ
     */
    public FlatLayerInfo(int thickness, BlockType blockType, int startHeight) {
        this.thickness = Math.max(1, thickness);
        this.blockType = blockType != null ? blockType : BlockType.STONE;
        this.startHeight = startHeight;
    }

    public int getThickness() {
        return thickness;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getStartHeight() {
        return startHeight;
    }
}
