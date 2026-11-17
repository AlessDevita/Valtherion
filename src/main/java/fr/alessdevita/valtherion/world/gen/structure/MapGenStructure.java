package fr.alessdevita.valtherion.world.gen.structure;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import java.util.*;

/**
 * Générateur de base pour les structures.
 * Gère la détection et la génération des structures dans les chunks.
 */
public abstract class MapGenStructure {

    protected final World world;
    protected final Random random;
    protected final Map<String, StructureStart> structureMap;
    protected final Set<Integer> generatedChunks;
    protected int spacing;
    protected int separation;
    protected long seed;

    /**
     * Crée un générateur de structure.
     * @param world Monde associé
     * @param seed Graine pour la génération des structures
     */
    public MapGenStructure(World world, long seed) {
        this.world = world;
        this.random = new Random();
        this.structureMap = new HashMap<>();
        this.generatedChunks = new HashSet<>();
        this.spacing = 32;
        this.separation = 8;
        this.seed = seed;
    }

    /**
     * Initialise le générateur avec une graine et une zone.
     * @param seed Graine aléatoire
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     */
    public void prepare(long seed, int chunkX, int chunkZ) {
        this.random.setSeed(seed);
        this.seed = seed;
    }

    /**
     * Génère la structure pour un chunk donné.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return true si une structure a été générée
     */
    public boolean generate(int chunkX, int chunkZ) {
        random.setSeed(seed);
        int structureX = chunkX * 16 + random.nextInt(16);
        int structureZ = chunkZ * 16 + random.nextInt(16);

        if (canGenerateStructure(chunkX, chunkZ)) {
            StructureStart start = getStructureStart(structureX, structureZ);
            if (start != null) {
                structureMap.put(getStructureKey(chunkX, chunkZ), start);
                return generateStructureComponents(start);
            }
        }
        return false;
    }

    /**
     * Vérifie si une structure peut être générée dans ce chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return true si la structure peut être générée
     */
    protected boolean canGenerateStructure(int chunkX, int chunkZ) {
        long hash = getChunkHash(chunkX, chunkZ);
        int value = (int) (Math.abs(hash) % spacing);
        return value < separation;
    }

    /**
     * Calcule le hash d'un chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return Le hash du chunk
     */
    protected long getChunkHash(int chunkX, int chunkZ) {
        long hash = chunkX * 374761393L + chunkZ * 668265263L;
        hash = hash * hash + hash;
        hash = hash ^ (hash >> 33);
        return hash;
    }

    /**
     * Obtient la clé d'une structure.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return La clé de la structure
     */
    protected String getStructureKey(int chunkX, int chunkZ) {
        return chunkX + "," + chunkZ;
    }

    /**
     * Obtient le point de départ d'une structure.
     * À surcharger dans les sous-classes.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Le point de départ de la structure
     */
    protected abstract StructureStart getStructureStart(int x, int z);

    /**
     * Génère les composants d'une structure.
     * @param start Point de départ de la structure
     * @return true si la génération a réussi
     */
    protected boolean generateStructureComponents(StructureStart start) {
        for (StructureComponent component : start.getComponents()) {
            if (component != null) {
                component.generateStructure(world, random);
            }
        }
        generatedChunks.add(start.getX() + start.getZ() * 1000);
        return true;
    }

    /**
     * Réinitialise le générateur.
     */
    public void reset() {
        structureMap.clear();
        generatedChunks.clear();
    }

    /**
     * Vérifie si une structure a été générée dans un chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return true si une structure a été générée
     */
    public boolean hasGeneratedStructure(int chunkX, int chunkZ) {
        return generatedChunks.contains(chunkX + chunkZ * 1000);
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public int getSeparation() {
        return separation;
    }

    public void setSeparation(int separation) {
        this.separation = separation;
    }
}
