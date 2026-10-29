package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de rivière dans Valtherion.
 * Un biome aquatique peu profond avec de l'eau douce, des poissons et des plantes aquatiques.
 */
public class RiverBiome extends Biome {

    // Largeur de la rivière
    private int width;

    // Profondeur de la rivière
    private int depth;

    // Direction de la rivière (en degrés)
    private float direction;

    /**
     * Constructeur du biome de rivière.
     */
    public RiverBiome() {
        super(BiomeType.RIVER, "Rivière", "Un biome aquatique peu profond avec de l'eau douce.");

        // Définir les propriétés du biome
        setBaseHeight(60);
        setHeightVariation(2.0f);
        setTemperature(0.7f);
        setHumidity(0.9f);
        setGenerationProbability(0.1f);
        setColor(0x4169E1); // Bleu rivière
        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xE0F7FA); // Bleu très clair
        setAmbientLightLevel(0.8f);

        this.width = 10;
        this.depth = 3;
        this.direction = 0.0f;

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de rivière
        addBlockType(BlockType.WATER, 0.8f);
        addBlockType(BlockType.SAND, 0.1f);
        addBlockType(BlockType.GRAVEL, 0.05f);
        addBlockType(BlockType.DIRT, 0.03f);
        addBlockType(BlockType.GRASS, 0.02f);

        // Ajouter des plantes aquatiques
        addBlockType(BlockType.WATER_PLANT, 0.01f);
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de rivière
        addEntityType("FISH", 0.15f);
        addEntityType("FROG", 0.02f);
        addEntityType("DUCK", 0.01f);
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer un lit de rivière sinueux
        float angle = (float) Math.toRadians(direction);
        float rotatedX = (float) (x * Math.cos(angle) - z * Math.sin(angle));
        float rotatedZ = (float) (x * Math.sin(angle) + z * Math.cos(angle));

        return (float) (Math.sin(rotatedX * 0.1f) * 0.5f);
    }

    // --- Getters et Setters ---

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}