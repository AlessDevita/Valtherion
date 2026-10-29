package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome enneigé dans Valtherion.
 * Un biome froid avec de la neige, de la glace et des arbres gelés.
 */
public class SnowBiome extends Biome {

    // Type de biome enneigé (NORMAL, TUNDRA, GLACIER)
    private SnowBiomeType snowBiomeType;

    // Énumération des types de biome enneigé
    public enum SnowBiomeType {
        NORMAL,   // Biome enneigé normal
        TUNDRA,   // Toundra
        GLACIER   // Glacier
    }

    /**
     * Constructeur du biome enneigé.
     */
    public SnowBiome() {
        this(SnowBiomeType.NORMAL);
    }

    /**
     * Constructeur du biome enneigé avec un type spécifique.
     * @param snowBiomeType Type de biome enneigé.
     */
    public SnowBiome(SnowBiomeType snowBiomeType) {
        super(BiomeType.SNOW, "Neige", "Un biome froid avec de la neige et de la glace.");
        this.snowBiomeType = snowBiomeType;

        // Définir les propriétés du biome en fonction du type
        switch (snowBiomeType) {
            case NORMAL:
                setBaseHeight(70);
                setHeightVariation(10.0f);
                setTemperature(0.1f);
                setHumidity(0.7f);
                setGenerationProbability(0.1f);
                setColor(0xFFFFFF); // Blanc
                break;
            case TUNDRA:
                setBaseHeight(65);
                setHeightVariation(5.0f);
                setTemperature(0.2f);
                setHumidity(0.6f);
                setGenerationProbability(0.15f);
                setColor(0xF0F8FF); // Blanc azuré
                break;
            case GLACIER:
                setBaseHeight(80);
                setHeightVariation(20.0f);
                setTemperature(0.0f);
                setHumidity(0.8f);
                setGenerationProbability(0.05f);
                setColor(0xE0FFFF); // Bleu clair
                break;
        }

        setSkyColor(0xE0F7FA); // Bleu très clair
        setFogColor(0xFFFFFF); // Blanc
        setAmbientLightLevel(0.9f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome enneigé
        switch (snowBiomeType) {
            case NORMAL:
                addBlockType(BlockType.SNOW, 0.7f);
                addBlockType(BlockType.ICE, 0.1f);
                addBlockType(BlockType.DIRT, 0.1f);
                addBlockType(BlockType.STONE, 0.08f);
                addBlockType(BlockType.WOOD, 0.02f);
                break;
            case TUNDRA:
                addBlockType(BlockType.SNOW, 0.6f);
                addBlockType(BlockType.DIRT, 0.2f);
                addBlockType(BlockType.STONE, 0.15f);
                addBlockType(BlockType.GRASS, 0.05f);
                break;
            case GLACIER:
                addBlockType(BlockType.ICE, 0.8f);
                addBlockType(BlockType.SNOW, 0.15f);
                addBlockType(BlockType.STONE, 0.05f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome enneigé
        switch (snowBiomeType) {
            case NORMAL:
                addEntityType("WOLF", 0.03f);
                addEntityType("RABBIT", 0.02f);
                addEntityType("POLAR_BEAR", 0.005f);
                break;
            case TUNDRA:
                addEntityType("RABBIT", 0.03f);
                addEntityType("DEER", 0.01f);
                break;
            case GLACIER:
                addEntityType("POLAR_BEAR", 0.01f);
                addEntityType("SEAL", 0.01f);
                break;
        }
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des collines enneigées
        return (float) (Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 0.8f +
                Math.sin(x * 0.05f) * Math.cos(z * 0.05f) * 0.2f);
    }

    // --- Getters et Setters ---

    public SnowBiomeType getSnowBiomeType() {
        return snowBiomeType;
    }

    public void setSnowBiomeType(SnowBiomeType snowBiomeType) {
        this.snowBiomeType = snowBiomeType;
    }
}