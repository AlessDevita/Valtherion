package fr.alessdevita.valtherion.world.biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Registre central pour tous les biomes disponibles.
 */
public class BiomeRegistry {
    private static final Map<String, Biome> biomes = new HashMap<>();
    private static final Map<BiomeType, Biome> biomesByType = new HashMap<>();

    /**
     * Enregistre un biome.
     * @param id Identifiant unique du biome
     * @param biome Instance du biome
     */
    public static void register(String id, Biome biome) {
        biomes.put(id.toLowerCase(), biome);
        biomesByType.put(biome.getType(), biome);
    }

    /**
     * Récupère un biome par son identifiant.
     * @param id Identifiant du biome
     * @return Biome ou null
     */
    public static Biome get(String id) {
        return biomes.get(id.toLowerCase());
    }

    /**
     * Récupère un biome par son type.
     * @param type Type de biome
     * @return Biome ou null
     */
    public static Biome getByType(BiomeType type) {
        return biomesByType.get(type);
    }

    /**
     * Vérifie si un biome existe.
     * @param id Identifiant du biome
     * @return true si le biome existe
     */
    public static boolean exists(String id) {
        return biomes.containsKey(id.toLowerCase());
    }

    /**
     * Retourne tous les biomes enregistrés.
     * @return Collection de biomes
     */
    public static Collection<Biome> getAllBiomes() {
        return biomes.values();
    }

    /**
     * Retourne le nombre de biomes enregistrés.
     * @return Nombre de biomes
     */
    public static int getBiomeCount() {
        return biomes.size();
    }

    /**
     * Désenregistre un biome.
     * @param id Identifiant du biome
     * @return true si le biome a été supprimé
     */
    public static boolean unregister(String id) {
        Biome biome = biomes.remove(id.toLowerCase());
        if (biome != null) {
            biomesByType.remove(biome.getType());
            return true;
        }
        return false;
    }

    /**
     * Réinitialise le registre.
     */
    public static void clear() {
        biomes.clear();
        biomesByType.clear();
    }
}