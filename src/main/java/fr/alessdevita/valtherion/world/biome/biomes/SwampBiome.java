package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de marais dans Valtherion.
 * Un biome humide avec de l'eau stagnante, des arbres morts et des plantes marécageuses.
 */
public class SwampBiome extends Biome {

    // Type de marais (NORMAL, TOXIC, MANGROVE)
    private SwampType swampType;

    // Énumération des types de marais
    public enum SwampType {
        NORMAL,   // Marais normal
        TOXIC,    // Marais toxique
        MANGROVE  // Mangrove
    }

    /**
     * Constructeur du biome de marais.
     */
    public SwampBiome() {
        this(SwampType.NORMAL);
    }

    /**
     * Constructeur du biome de marais avec un type spécifique.
     * @param swampType Type de marais.
     */
    public SwampBiome(SwampType swampType) {
        super(BiomeType.SWAMP, "Marais", "Un biome humide avec de l'eau stagnante et des arbres morts.");
        this.swampType = swampType;

        // Définir les propriétés du biome en fonction du type
        switch (swampType) {
            case NORMAL:
                setBaseHeight(55);
                setHeightVariation(3.0f);
                setTemperature(0.5f);
                setHumidity(0.9f);
                setGenerationProbability(0.1f);
                setColor(0x2F4F2F); // Vert marécage
                break;
            case TOXIC:
                setBaseHeight(50);
                setHeightVariation(2.0f);
                setTemperature(0.6f);
                setHumidity(1.0f);
                setGenerationProbability(0.05f);
                setColor(0x556B2F); // Vert toxique
                break;
            case MANGROVE:
                setBaseHeight(60);
                setHeightVariation(5.0f);
                setTemperature(0.7f);
                setHumidity(0.95f);
                setGenerationProbability(0.08f);
                setColor(0x228B22); // Vert mangrove
                break;
        }

        setSkyColor(0x808080); // Gris
        setFogColor(0xA9A9A9); // Gris foncé
        setAmbientLightLevel(0.6f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de marais
        switch (swampType) {
            case NORMAL:
                addBlockType(BlockType.WATER, 0.4f);
                addBlockType(BlockType.DIRT, 0.3f);
                addBlockType(BlockType.GRASS, 0.15f);
                addBlockType(BlockType.WOOD, 0.05f);
                addBlockType(BlockType.LEAVES, 0.05f);
                addBlockType(BlockType.MUSHROOM, 0.03f);
                break;
            case TOXIC:
                addBlockType(BlockType.WATER, 0.5f);
                addBlockType(BlockType.DIRT, 0.2f);
                addBlockType(BlockType.GRASS, 0.1f);
                addBlockType(BlockType.MUSHROOM_RED, 0.05f);
                addBlockType(BlockType.MUSHROOM_BROWN, 0.05f);
                addBlockType(BlockType.POISON, 0.01f);
                break;
            case MANGROVE:
                addBlockType(BlockType.WATER, 0.3f);
                addBlockType(BlockType.DIRT, 0.2f);
                addBlockType(BlockType.WOOD, 0.1f);
                addBlockType(BlockType.LEAVES, 0.1f);
                addBlockType(BlockType.MUSHROOM, 0.03f);
                addBlockType(BlockType.VINE, 0.02f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de marais
        switch (swampType) {
            case NORMAL:
                addEntityType("FROG", 0.05f);
                addEntityType("SNAKE", 0.02f);
                addEntityType("SPIDER", 0.01f);
                break;
            case TOXIC:
                addEntityType("SPIDER", 0.03f);
                addEntityType("ZOMBIE", 0.01f);
                addEntityType("SKELETON", 0.005f);
                break;
            case MANGROVE:
                addEntityType("CROCODILE", 0.01f);
                addEntityType("FROG", 0.03f);
                addEntityType("PARROT", 0.01f);
                break;
        }
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer un terrain marécageux
        return (float) (Math.sin(x * 0.08f) * Math.cos(z * 0.08f) * 0.3f +
                Math.sin(x * 0.04f) * Math.cos(z * 0.04f) * 0.2f);
    }

    // --- Getters et Setters ---

    public SwampType getSwampType() {
        return swampType;
    }

    public void setSwampType(SwampType swampType) {
        this.swampType = swampType;
    }
}