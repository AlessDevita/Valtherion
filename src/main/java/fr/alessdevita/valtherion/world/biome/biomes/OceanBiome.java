package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome océanique dans Valtherion.
 * Un biome aquatique profond avec de l'eau salée, des poissons et des coraux.
 */
public class OceanBiome extends Biome {

    // Type d'océan (NORMAL, DEEP, FROZEN)
    private OceanType oceanType;

    // Énumération des types d'océan
    public enum OceanType {
        NORMAL,  // Océan normal
        DEEP,    // Océan profond
        FROZEN   // Océan gelé
    }

    /**
     * Constructeur du biome océanique.
     */
    public OceanBiome() {
        this(OceanType.NORMAL);
    }

    /**
     * Constructeur du biome océanique avec un type spécifique.
     * @param oceanType Type d'océan.
     */
    public OceanBiome(OceanType oceanType) {
        super(BiomeType.OCEAN, "Océan", "Un biome aquatique profond avec de l'eau salée.");
        this.oceanType = oceanType;

        // Définir les propriétés du biome en fonction du type
        switch (oceanType) {
            case NORMAL:
                setBaseHeight(0);
                setHeightVariation(5.0f);
                setTemperature(0.6f);
                setHumidity(1.0f);
                setGenerationProbability(0.25f);
                setColor(0x1E90FF); // Bleu océan
                break;
            case DEEP:
                setBaseHeight(-30);
                setHeightVariation(10.0f);
                setTemperature(0.4f);
                setHumidity(1.0f);
                setGenerationProbability(0.2f);
                setColor(0x000080); // Bleu foncé
                break;
            case FROZEN:
                setBaseHeight(0);
                setHeightVariation(2.0f);
                setTemperature(0.0f);
                setHumidity(0.8f);
                setGenerationProbability(0.1f);
                setColor(0xADD8E6); // Bleu clair
                break;
        }

        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xE0F7FA); // Bleu très clair
        setAmbientLightLevel(0.7f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome océanique
        switch (oceanType) {
            case NORMAL:
                addBlockType(BlockType.WATER, 0.95f);
                addBlockType(BlockType.SAND, 0.03f);
                addBlockType(BlockType.GRAVEL, 0.02f);
                break;
            case DEEP:
                addBlockType(BlockType.WATER, 0.98f);
                addBlockType(BlockType.STONE, 0.01f);
                addBlockType(BlockType.CORAL, 0.01f);
                break;
            case FROZEN:
                addBlockType(BlockType.ICE, 0.9f);
                addBlockType(BlockType.WATER, 0.08f);
                addBlockType(BlockType.SNOW, 0.02f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome océanique
        switch (oceanType) {
            case NORMAL:
                addEntityType("FISH", 0.1f);
                addEntityType("DOLPHIN", 0.01f);
                addEntityType("SHARK", 0.005f);
                break;
            case DEEP:
                addEntityType("FISH", 0.15f);
                addEntityType("SQUID", 0.02f);
                addEntityType("WHALE", 0.001f);
                break;
            case FROZEN:
                addEntityType("FISH", 0.05f);
                addEntityType("SEAL", 0.01f);
                break;
        }
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des vagues
        return (float) (Math.sin(x * 0.02f) * Math.cos(z * 0.02f) * 0.1f);
    }

    // --- Getters et Setters ---

    public OceanType getOceanType() {
        return oceanType;
    }

    public void setOceanType(OceanType oceanType) {
        this.oceanType = oceanType;
    }
}