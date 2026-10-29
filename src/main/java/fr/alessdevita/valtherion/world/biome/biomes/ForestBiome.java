package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de forêt dans Valtherion.
 * Un biome boisé avec des arbres, des buissons et une végétation dense.
 */
public class ForestBiome extends Biome {

    // Type de forêt (NORMAL, DENSE, SPARSE)
    private ForestType forestType;

    // Énumération des types de forêt
    public enum ForestType {
        NORMAL,  // Forêt normale
        DENSE,   // Forêt dense
        SPARSE   // Forêt clairsemée
    }

    /**
     * Constructeur du biome de forêt.
     */
    public ForestBiome() {
        this(ForestType.NORMAL);
    }

    /**
     * Constructeur du biome de forêt avec un type spécifique.
     * @param forestType Type de forêt.
     */
    public ForestBiome(ForestType forestType) {
        super(BiomeType.FOREST, "Forêt", "Un biome boisé avec des arbres et des buissons.");
        this.forestType = forestType;

        // Définir les propriétés du biome en fonction du type
        switch (forestType) {
            case NORMAL:
                setBaseHeight(70);
                setHeightVariation(10.0f);
                setTemperature(0.6f);
                setHumidity(0.7f);
                setGenerationProbability(0.25f);
                setColor(0x228B22); // Vert forêt
                break;
            case DENSE:
                setBaseHeight(75);
                setHeightVariation(15.0f);
                setTemperature(0.5f);
                setHumidity(0.8f);
                setGenerationProbability(0.15f);
                setColor(0x006400); // Vert foncé
                break;
            case SPARSE:
                setBaseHeight(65);
                setHeightVariation(8.0f);
                setTemperature(0.7f);
                setHumidity(0.6f);
                setGenerationProbability(0.2f);
                setColor(0x32CD32); // Vert lime
                break;
        }

        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xA0A0A0); // Gris
        setAmbientLightLevel(0.8f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de forêt
        addBlockType(BlockType.GRASS, 0.6f);
        addBlockType(BlockType.DIRT, 0.2f);
        addBlockType(BlockType.WOOD, 0.1f);
        addBlockType(BlockType.LEAVES, 0.08f);
        addBlockType(BlockType.STONE, 0.02f);

        // Ajouter des buissons et des fleurs
        addBlockType(BlockType.BUSH, 0.03f);
        addBlockType(BlockType.FLOWER_RED, 0.01f);
        addBlockType(BlockType.FLOWER_YELLOW, 0.01f);
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de forêt
        addEntityType("WOLF", 0.02f);
        addEntityType("FOX", 0.01f);
        addEntityType("RABBIT", 0.05f);
        addEntityType("DEER", 0.01f);
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des collines et des vallées
        return (float) (Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 0.7f +
                Math.sin(x * 0.05f) * Math.cos(z * 0.05f) * 0.3f);
    }

    // --- Getters et Setters ---

    public ForestType getForestType() {
        return forestType;
    }

    public void setForestType(ForestType forestType) {
        this.forestType = forestType;
    }
}