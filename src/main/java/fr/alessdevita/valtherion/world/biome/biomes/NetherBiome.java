package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome du Nether dans Valtherion.
 * Un biome infernal avec de la lave, des roches sombres et des créatures hostiles.
 */
public class NetherBiome extends Biome {

    // Type de Nether (NORMAL, BASALT, WARPED)
    private NetherType netherType;

    // Énumération des types de Nether
    public enum NetherType {
        NORMAL,   // Nether normal
        BASALT,   // Nether de basalte
        WARPED    // Nether déformé
    }

    /**
     * Constructeur du biome du Nether.
     */
    public NetherBiome() {
        this(NetherType.NORMAL);
    }

    /**
     * Constructeur du biome du Nether avec un type spécifique.
     * @param netherType Type de Nether.
     */
    public NetherBiome(NetherType netherType) {
        super(BiomeType.NETHER, "Nether", "Un biome infernal avec de la lave et des roches sombres.");
        this.netherType = netherType;

        // Définir les propriétés du biome en fonction du type
        switch (netherType) {
            case NORMAL:
                setBaseHeight(30);
                setHeightVariation(20.0f);
                setTemperature(2.0f); // Très chaud
                setHumidity(0.0f);
                setGenerationProbability(0.0f); // Le Nether est un monde séparé
                setColor(0x8B0000); // Rouge foncé
                break;
            case BASALT:
                setBaseHeight(25);
                setHeightVariation(25.0f);
                setTemperature(2.2f);
                setHumidity(0.0f);
                setGenerationProbability(0.0f);
                setColor(0x483D8B); // Bleu foncé
                break;
            case WARPED:
                setBaseHeight(35);
                setHeightVariation(15.0f);
                setTemperature(1.8f);
                setHumidity(0.0f);
                setGenerationProbability(0.0f);
                setColor(0x9400D3); // Violet
                break;
        }

        setSkyColor(0xFF4500); // Rouge orangé
        setFogColor(0x8B0000); // Rouge foncé
        setAmbientLightLevel(0.5f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome du Nether
        switch (netherType) {
            case NORMAL:
                addBlockType(BlockType.NETHER_RACK, 0.7f);
                addBlockType(BlockType.LAVA, 0.1f);
                addBlockType(BlockType.NETHER_BRICK, 0.05f);
                addBlockType(BlockType.SOUL_SAND, 0.05f);
                addBlockType(BlockType.GLOWSTONE, 0.02f);
                break;
            case BASALT:
                addBlockType(BlockType.BASALT, 0.8f);
                addBlockType(BlockType.LAVA, 0.1f);
                addBlockType(BlockType.BLACKSTONE, 0.05f);
                addBlockType(BlockType.GOLD_ORE, 0.03f);
                break;
            case WARPED:
                addBlockType(BlockType.WARPED_STONE, 0.7f);
                addBlockType(BlockType.WARPED_BRICK, 0.1f);
                addBlockType(BlockType.WARPED_VINE, 0.05f);
                addBlockType(BlockType.NETHER_GOLD_ORE, 0.03f);
                break;
        }
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome du Nether
        switch (netherType) {
            case NORMAL:
                addEntityType("ZOMBIFIED_PIGLIN", 0.05f);
                addEntityType("GHAST", 0.01f);
                addEntityType("MAGMA_CUBE", 0.01f);
                addEntityType("WITHER_SKELETON", 0.005f);
                break;
            case BASALT:
                addEntityType("ZOMBIFIED_PIGLIN", 0.03f);
                addEntityType("GHAST", 0.02f);
                addEntityType("STRIDER", 0.01f);
                break;
            case WARPED:
                addEntityType("ENDERMAN", 0.01f);
                addEntityType("PIGLIN", 0.03f);
                addEntityType("HOG LIN", 0.01f);
                break;
        }
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer un terrain du Nether chaotique
        return (float) (Math.sin(x * 0.2f) * Math.cos(z * 0.2f) * 2.0f +
                Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 1.0f +
                Math.sin(x * 0.05f) * Math.cos(z * 0.05f) * 0.5f);
    }

    // --- Getters et Setters ---

    public NetherType getNetherType() {
        return netherType;
    }

    public void setNetherType(NetherType netherType) {
        this.netherType = netherType;
    }
}