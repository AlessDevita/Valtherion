package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de désert dans Valtherion.
 * Un biome sec avec du sable, des cactus et des rochers.
 */
public class DesertBiome extends Biome {

    // Type de désert (NORMAL, HOT, COLD)
    private DesertType desertType;

    // Énumération des types de désert
    public enum DesertType {
        NORMAL,  // Désert normal
        HOT,     // Désert chaud
        COLD     // Désert froid
    }

    /**
     * Constructeur du biome de désert.
     */
    public DesertBiome() {
        this(DesertType.NORMAL);
    }

    /**
     * Constructeur du biome de désert avec un type spécifique.
     * @param desertType Type de désert.
     */
    public DesertBiome(DesertType desertType) {
        super(BiomeType.DESERT, "Désert", "Un biome sec avec du sable et des cactus.");
        this.desertType = desertType;

        // Définir les propriétés du biome en fonction du type
        switch (desertType) {
            case NORMAL:
                setBaseHeight(60);
                setHeightVariation(3.0f);
                setTemperature(0.9f);
                setHumidity(0.1f);
                setGenerationProbability(0.15f);
                setColor(0xF5DEB3); // Beige clair
                break;
            case HOT:
                setBaseHeight(55);
                setHeightVariation(2.0f);
                setTemperature(1.0f);
                setHumidity(0.05f);
                setGenerationProbability(0.1f);
                setColor(0xFFD700); // Or
                break;
            case COLD:
                setBaseHeight(65);
                setHeightVariation(4.0f);
                setTemperature(0.2f);
                setHumidity(0.15f);
                setGenerationProbability(0.1f);
                setColor(0xE0FFFF); // Bleu clair
                break;
        }

        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xF5DEB3); // Beige clair
        setAmbientLightLevel(1.1f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de désert
        addBlockType(BlockType.SAND, 0.9f);
        addBlockType(BlockType.SANDSTONE, 0.05f);
        addBlockType(BlockType.STONE, 0.03f);

        // Ajouter des cactus
        addBlockType(BlockType.CACTUS, 0.01f);

        // Ajouter des blocs spécifiques aux déserts chauds
        if (desertType == DesertType.HOT) {
            addBlockType(BlockType.GOLD_ORE, 0.005f);
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de désert
        addEntityType("SCORPION", 0.02f);
        addEntityType("SNAKE", 0.02f);
        addEntityType("CAMEL", 0.01f);
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des dunes de sable
        return (float) (Math.sin(x * 0.05f) * Math.cos(z * 0.05f) * 0.2f);
    }

    // --- Getters et Setters ---

    public DesertType getDesertType() {
        return desertType;
    }

    public void setDesertType(DesertType desertType) {
        this.desertType = desertType;
    }
}