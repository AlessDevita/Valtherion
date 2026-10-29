package fr.alessdevita.valtherion.world.biome.biomes;

import fr.alessdevita.valtherion.world.biome.Biome;
import fr.alessdevita.valtherion.world.biome.BiomeType;
import fr.alessdevita.valtherion.block.BlockType;

/**
 * Biome de plaine dans Valtherion.
 * Un biome plat et ouvert avec de l'herbe, des fleurs et quelques arbres.
 */
public class PlainsBiome extends Biome {

    /**
     * Constructeur du biome de plaine.
     */
    public PlainsBiome() {
        super(BiomeType.PLAINS, "Plaine", "Un biome plat et ouvert avec de l'herbe et des fleurs.");

        // Définir les propriétés du biome
        setBaseHeight(64);
        setHeightVariation(5.0f);
        setTemperature(0.7f);
        setHumidity(0.5f);
        setGenerationProbability(0.3f);
        setColor(0x7CFC00); // Vert clair
        setSkyColor(0x87CEEB); // Bleu ciel
        setFogColor(0xFFFFFF); // Blanc
        setAmbientLightLevel(1.0f);

        // Initialiser le biome
        init();
    }

    @Override
    protected void initDefaultBlocks() {
        // Ajouter les blocs par défaut du biome de plaine
        addBlockType(BlockType.GRASS, 0.8f);
        addBlockType(BlockType.DIRT, 0.15f);
        addBlockType(BlockType.STONE, 0.05f);

        // Ajouter des fleurs
        addBlockType(BlockType.FLOWER_RED, 0.01f);
        addBlockType(BlockType.FLOWER_YELLOW, 0.01f);
        addBlockType(BlockType.FLOWER_BLUE, 0.005f);
    }

    @Override
    protected void initDefaultEntities() {
        // Ajouter les entités par défaut du biome de plaine
        addEntityType("COW", 0.05f);
        addEntityType("PIG", 0.05f);
        addEntityType("SHEEP", 0.05f);
        addEntityType("CHICKEN", 0.03f);
    }

    @Override
    protected float generatePerlinNoise(float x, float z) {
        // Utiliser un bruit de Perlin pour générer des collines douces
        return (float) (Math.sin(x * 0.05f) * Math.cos(z * 0.05f) * 0.5f +
                Math.sin(x * 0.1f) * Math.cos(z * 0.1f) * 0.3f);
    }
}