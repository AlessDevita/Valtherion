package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de montagne dans Valtherion.
 * Un biome montagneux avec des rochers, des pentes raides et de la neige en altitude.
 */
public class MountainBiome extends Biome {

    // Altitude de la limite des neiges
    private int snowLineAltitude;

    // Type de montagne (NORMAL, HIGH, VOLCANIC)
    private MountainType mountainType;

    // Énumération des types de montagne
    public enum MountainType {
        NORMAL,   // Montagne normale
        HIGH,     // Montagne haute
        VOLCANIC  // Montagne volcanique
    }

    /**
     * Constructeur du biome de montagne.
     */
    public MountainBiome() {
        this(MountainType.NORMAL);
    }

    /**
     * Constructeur du biome de montagne avec un type spécifique.
     * @param mountainType Type de montagne.
     */
    public MountainBiome(MountainType mountainType) {
        super(BiomeType.MOUNTAIN, "Montagne", "Un biome montagneux avec des rochers et des pentes raides.");
        this.mountainType = mountainType;
        this.snowLineAltitude = 100;

        // Définir les propriétés du biome en fonction du type
        switch (mountainType) {
            case NORMAL:
                setBaseHeight(96);
                setHeightVariation(30.0f);
                setTemperature(0.3f);
                setHumidity(0.4f);
                setGenerationProbability(0.15f);
                setColor(0x808080); // Gris
                break;
            case HIGH:
                setBaseHeight(128);
                setHeightVariation(50.0f);
                setTemperature(0.1f);
                setHumidity(0.3f);
                setGenerationProbability(0.1f);
                setColor(0xA9A9A9); // Gris foncé
                snowLineAltitude = 120;
                break;
            case VOLCANIC:
                setBaseHeight(80);
                setHeightVariation(40.0f);
                setTemperature(0.8f);
                setHumidity(0.2f);
                setGenerationProbability(0.05f);
                setColor(0x8B0000); // Rouge foncé
                break;
        }

        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xD3D3D3); // Gris clair
        setAmbientLightLevel(0.9f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de montagne
        addBlockType(BlockType.STONE, 0.7f);
        addBlockType(BlockType.DIRT, 0.15f);
        addBlockType(BlockType.GRASS, 0.05f);

        // Ajouter des blocs spécifiques aux montagnes
        switch (mountainType) {
            case NORMAL:
                addBlockType(BlockType.COBBLESTONE, 0.05f);
                addBlockType(BlockType.GRAVEL, 0.03f);
                break;
            case HIGH:
                addBlockType(BlockType.SNOW, 0.05f);
                addBlockType(BlockType.ICE, 0.02f);
                break;
            case VOLCANIC:
                addBlockType(BlockType.OBSIDIAN, 0.03f);
                addBlockType(BlockType.LAVA, 0.01f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de montagne
        addEntityType("GOAT", 0.03f);
        addEntityType("EAGLE", 0.01f);
        addEntityType("BEAR", 0.01f);
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des montagnes escarpées
        return (float) (Math.sin(x * 0.2f) * Math.cos(z * 0.2f) * 1.5f +
                Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 0.5f);
    }

    @Override
    public int generateHeight(int x, int z) {
        int height = super.generateHeight(x, z);

        // Ajouter de la neige au-dessus de la limite des neiges
        if (height > snowLineAltitude && mountainType != MountainType.VOLCANIC) {
            // Ajouter de la neige en haut des montagnes
            // (Cela sera géré par le générateur de monde)
        }

        return height;
    }

    // --- Getters et Setters ---

    public int getSnowLineAltitude() {
        return snowLineAltitude;
    }

    public void setSnowLineAltitude(int snowLineAltitude) {
        this.snowLineAltitude = snowLineAltitude;
    }

    public MountainType getMountainType() {
        return mountainType;
    }

    public void setMountainType(MountainType mountainType) {
        this.mountainType = mountainType;
    }
}