package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de jungle dans Valtherion.
 * Un biome dense avec des arbres hauts, une végétation épaisse et des animaux exotiques.
 */
public class JungleBiome extends Biome {

    // Type de jungle (NORMAL, RAINFOREST, BAMBOO)
    private JungleType jungleType;

    // Énumération des types de jungle
    public enum JungleType {
        NORMAL,     // Jungle normale
        RAINFOREST, // Forêt tropicale
        BAMBOO      // Jungle de bambou
    }

    /**
     * Constructeur du biome de jungle.
     */
    public JungleBiome() {
        this(JungleType.NORMAL);
    }

    /**
     * Constructeur du biome de jungle avec un type spécifique.
     * @param jungleType Type de jungle.
     */
    public JungleBiome(JungleType jungleType) {
        super(BiomeType.JUNGLE, "Jungle", "Un biome dense avec des arbres hauts et une végétation épaisse.");
        this.jungleType = jungleType;

        // Définir les propriétés du biome en fonction du type
        switch (jungleType) {
            case NORMAL:
                setBaseHeight(65);
                setHeightVariation(15.0f);
                setTemperature(0.8f);
                setHumidity(0.9f);
                setGenerationProbability(0.1f);
                setColor(0x228B22); // Vert jungle
                break;
            case RAINFOREST:
                setBaseHeight(70);
                setHeightVariation(20.0f);
                setTemperature(0.9f);
                setHumidity(1.0f);
                setGenerationProbability(0.08f);
                setColor(0x006400); // Vert forêt tropicale
                break;
            case BAMBOO:
                setBaseHeight(68);
                setHeightVariation(10.0f);
                setTemperature(0.7f);
                setHumidity(0.8f);
                setGenerationProbability(0.05f);
                setColor(0x7CFC00); // Vert bambou
                break;
        }

        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0x90EE90); // Vert clair
        setAmbientLightLevel(0.7f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de jungle
        switch (jungleType) {
            case NORMAL:
                addBlockType(BlockType.GRASS, 0.3f);
                addBlockType(BlockType.DIRT, 0.2f);
                addBlockType(BlockType.WOOD, 0.15f);
                addBlockType(BlockType.LEAVES, 0.15f);
                addBlockType(BlockType.VINE, 0.05f);
                addBlockType(BlockType.FLOWER_RED, 0.02f);
                addBlockType(BlockType.FLOWER_YELLOW, 0.02f);
                break;
            case RAINFOREST:
                addBlockType(BlockType.GRASS, 0.2f);
                addBlockType(BlockType.DIRT, 0.15f);
                addBlockType(BlockType.WOOD, 0.2f);
                addBlockType(BlockType.LEAVES, 0.2f);
                addBlockType(BlockType.VINE, 0.1f);
                addBlockType(BlockType.MUSHROOM, 0.03f);
                break;
            case BAMBOO:
                addBlockType(BlockType.GRASS, 0.3f);
                addBlockType(BlockType.DIRT, 0.2f);
                addBlockType(BlockType.BAMBOO, 0.15f);
                addBlockType(BlockType.LEAVES, 0.1f);
                addBlockType(BlockType.VINE, 0.05f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de jungle
        switch (jungleType) {
            case NORMAL:
                addEntityType("MONKEY", 0.03f);
                addEntityType("PARROT", 0.02f);
                addEntityType("TIGER", 0.005f);
                break;
            case RAINFOREST:
                addEntityType("MONKEY", 0.05f);
                addEntityType("PARROT", 0.03f);
                addEntityType("JAGUAR", 0.005f);
                addEntityType("TOUCAN", 0.01f);
                break;
            case BAMBOO:
                addEntityType("PANDA", 0.005f);
                addEntityType("MONKEY", 0.02f);
                addEntityType("PARROT", 0.01f);
                break;
        }
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer un terrain de jungle dense
        return (float) (Math.sin(x * 0.15f) * Math.cos(z * 0.15f) * 1.0f +
                Math.sin(x * 0.08f) * Math.cos(z * 0.08f) * 0.5f);
    }

    // --- Getters et Setters ---

    public JungleType getJungleType() {
        return jungleType;
    }

    public void setJungleType(JungleType jungleType) {
        this.jungleType = jungleType;
    }
}