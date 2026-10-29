package fr.alessdevita.valtherion.world.biome;

import fr.alessdevita.valtherion.block.BlockType;

import java.util.*;

/**
 * Classe de base pour les biomes dans Valtherion.
 * Définit les propriétés et comportements communs à tous les biomes.
 */
public class Biome {

    // Type du biome
    private final BiomeType type;

    // Nom du biome
    private final String name;

    // Description du biome
    private final String description;

    // Hauteur de base du terrain
    private int baseHeight;

    // Variation de hauteur du terrain
    private float heightVariation;

    // Température du biome (0.0 = froid, 1.0 = chaud)
    private float temperature;

    // Humidité du biome (0.0 = sec, 1.0 = humide)
    private float humidity;

    // Probabilité de génération du biome (0.0 à 1.0)
    private float generationProbability;

    // Liste des types de blocs que le biome peut générer
    private final Map<BlockType, Float> blockTypes;

    // Liste des entités vivantes que le biome peut générer
    private final Map<String, Float> entityTypes;

    // Couleur du biome (pour la carte ou les effets visuels)
    private int color;

    // Couleur du ciel du biome
    private int skyColor;

    // Couleur du brouillard du biome
    private int fogColor;

    // Niveau de lumière ambiante du biome
    private float ambientLightLevel;

    // Liste des observateurs du biome
    private final List<BiomeListener> listeners;

    /**
     * Constructeur du biome.
     * @param type Type du biome.
     * @param name Nom du biome.
     * @param description Description du biome.
     */
    public Biome(BiomeType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.baseHeight = 64;
        this.heightVariation = 10.0f;
        this.temperature = 0.5f;
        this.humidity = 0.5f;
        this.generationProbability = 1.0f;
        this.blockTypes = new HashMap<>();
        this.entityTypes = new HashMap<>();
        this.color = 0x00FF00; // Vert par défaut
        this.skyColor = 0x87CEEB; // Bleu ciel par défaut
        this.fogColor = 0xFFFFFF; // Blanc par défaut
        this.ambientLightLevel = 1.0f;
        this.listeners = new ArrayList<>();
    }

    /**
     * Initialise le biome avec des valeurs par défaut.
     */
    public void init() {
        // Initialiser les blocs et entités par défaut
        initDefaultBlocks();
        initDefaultEntities();
    }

    /**
     * Initialise les blocs par défaut du biome.
     */
    protected void initDefaultBlocks() {
        // À implémenter dans les classes dérivées
    }

    /**
     * Initialise les entités par défaut du biome.
     */
    protected void initDefaultEntities() {
        // À implémenter dans les classes dérivées
    }

    /**
     * Ajoute un type de bloc au biome.
     * @param blockType Type de bloc.
     * @param probability Probabilité de génération (0.0 à 1.0).
     */
    public void addBlockType(BlockType blockType, float probability) {
        blockTypes.put(blockType, probability);
    }

    /**
     * Ajoute un type d'entité au biome.
     * @param entityType Type d'entité.
     * @param probability Probabilité de génération (0.0 à 1.0).
     */
    public void addEntityType(String entityType, float probability) {
        entityTypes.put(entityType, probability);
    }

    /**
     * Récupère un type de bloc aléatoire en fonction des probabilités.
     * @return Type de bloc.
     */
    public BlockType getRandomBlockType() {
        if (blockTypes.isEmpty()) {
            return null;
        }

        float totalProbability = (float) blockTypes.values().stream().mapToDouble(Float::doubleValue).sum();
        float randomValue = (float) (Math.random() * totalProbability);
        float cumulativeProbability = 0.0f;

        for (Map.Entry<BlockType, Float> entry : blockTypes.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Récupère un type d'entité aléatoire en fonction des probabilités.
     * @return Type d'entité.
     */
    public String getRandomEntityType() {
        if (entityTypes.isEmpty()) {
            return null;
        }

        float totalProbability = (float) entityTypes.values().stream().mapToDouble(Float::doubleValue).sum();
        float randomValue = (float) (Math.random() * totalProbability);
        float cumulativeProbability = 0.0f;

        for (Map.Entry<String, Float> entry : entityTypes.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Génère une hauteur de terrain pour une position donnée.
     * @param x Coordonnée X.
     * @param z Coordonnée Z.
     * @return Hauteur du terrain.
     */
    public int generateHeight(int x, int z) {
        // Utiliser un bruit de Perlin pour générer la hauteur
        float noise = generatePerlinNoise(x * 0.1f, z * 0.1f);
        return baseHeight + (int) (noise * heightVariation);
    }

    /**
     * Génère un bruit de Perlin pour une position donnée.
     * @param x Coordonnée X.
     * @param z Coordonnée Z.
     * @return Valeur du bruit de Perlin.
     */
    protected float generatePerlinNoise(float x, float z) {
        // Implémentation simplifiée du bruit de Perlin
        // Pour une implémentation complète, utilise une bibliothèque comme FastNoise ou JOML Noise
        return (float) (Math.sin(x * 0.1f) * Math.cos(z * 0.1f)) * 2.0f;
    }

    /**
     * Ajoute un observateur au biome.
     * @param listener Observateur à ajouter.
     */
    public void addListener(BiomeListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du biome.
     * @param listener Observateur à retirer.
     */
    public void removeListener(BiomeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs du biome.
     */
    protected void notifyListeners() {
        for (BiomeListener listener : listeners) {
            listener.onBiomeUpdate(this);
        }
    }

    // --- Getters et Setters ---

    public BiomeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(int baseHeight) {
        this.baseHeight = baseHeight;
    }

    public float getHeightVariation() {
        return heightVariation;
    }

    public void setHeightVariation(float heightVariation) {
        this.heightVariation = heightVariation;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getGenerationProbability() {
        return generationProbability;
    }

    public void setGenerationProbability(float generationProbability) {
        this.generationProbability = generationProbability;
    }

    public Map<BlockType, Float> getBlockTypes() {
        return blockTypes;
    }

    public Map<String, Float> getEntityTypes() {
        return entityTypes;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSkyColor() {
        return skyColor;
    }

    public void setSkyColor(int skyColor) {
        this.skyColor = skyColor;
    }

    public int getFogColor() {
        return fogColor;
    }

    public void setFogColor(int fogColor) {
        this.fogColor = fogColor;
    }

    public float getAmbientLightLevel() {
        return ambientLightLevel;
    }

    public void setAmbientLightLevel(float ambientLightLevel) {
        this.ambientLightLevel = ambientLightLevel;
    }

    /**
     * Retourne une représentation textuelle du biome.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Biome{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", baseHeight=" + baseHeight +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }

    public void addBlock(BlockType blockType, float v) {
    }

    public BlockType getSurfaceBlock() {
        BlockType surfaceBlock = null;
        return surfaceBlock;
    }

    public BlockType getRandomBlock(Random random) {
        return null;
    }

    // --- Interface pour les observateurs du biome ---
    public interface BiomeListener {
        void onBiomeUpdate(Biome biome);
    }
}