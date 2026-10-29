package fr.alessdevita.valtherion.world.structure;

import fr.alessdevita.valtherion.world.World;
import java.util.*;

/**
 * Gère la génération des structures dans le monde.
 */
public class StructureGenerator {
    private long seed;
    private Random random;
    private List<Structure> structures;
    private Map<String, Integer> structureFrequency;

    /**
     * Crée un générateur de structures.
     * @param seed Graine pour la génération
     */
    public StructureGenerator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.structures = new ArrayList<>();
        this.structureFrequency = new HashMap<>();
    }

    /**
     * Enregistre une structure pour la génération.
     * @param structure Structure à enregistrer
     * @param frequency Fréquence de génération (0.0 à 1.0)
     */
    public void registerStructure(Structure structure, float frequency) {
        structures.add(structure);
        structureFrequency.put(structure.getName(), (int)(frequency * 100));
    }

    /**
     * Génère les structures dans une région.
     * @param world Monde
     * @param regionX Coordonnée X de la région
     * @param regionZ Coordonnée Z de la région
     */
    public void generateStructures(World world, int regionX, int regionZ) {
        for (Structure structure : structures) {
            int frequency = structureFrequency.getOrDefault(structure.getName(), 50);
            if (random.nextInt(100) < frequency) {
                int x = regionX * 16 + random.nextInt(16);
                int z = regionZ * 16 + random.nextInt(16);
                int y = world.getHeightAt(x, z);
                structure.setPosition(x, y, z);
                structure.generate(world, x, y, z);
            }
        }
    }

    /**
     * Retourne le nombre de structures enregistrées.
     * @return Nombre de structures
     */
    public int getStructureCount() {
        return structures.size();
    }

    /**
     * Retourne une structure par son nom.
     * @param name Nom de la structure
     * @return Structure ou null
     */
    public Structure getStructure(String name) {
        return structures.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
