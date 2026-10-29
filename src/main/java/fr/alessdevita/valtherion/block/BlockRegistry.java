package fr.alessdevita.valtherion.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Registre central pour tous les types de blocs.
 * Permet la gestion centralisée des blocs et des créations personnalisées.
 */
public class BlockRegistry {
    private static final Map<String, BlockType> blockTypes = new HashMap<>();
    private static final Map<String, BlockType> idToBlockType = new HashMap<>();

    static {
        for (BlockType type : BlockType.values()) {
            String id = type.name().toLowerCase();
            blockTypes.put(id, type);
            idToBlockType.put(id, type);
        }
    }

    /**
     * Récupère un type de bloc par son identifiant.
     * @param id Identifiant du bloc
     * @return Type de bloc ou null
     */
    public static BlockType get(String id) {
        return blockTypes.get(id.toLowerCase());
    }

    /**
     * Enregistre un type de bloc personnalisé.
     * @param id Identifiant unique
     * @param type Type de bloc
     */
    public static void register(String id, BlockType type) {
        blockTypes.put(id.toLowerCase(), type);
    }

    /**
     * Vérifie si un bloc existe.
     * @param id Identifiant du bloc
     * @return true si le bloc existe
     */
    public static boolean exists(String id) {
        return blockTypes.containsKey(id.toLowerCase());
    }

    /**
     * Retourne tous les types de blocs enregistrés.
     * @return Collection de types de blocs
     */
    public static Collection<BlockType> getAllBlockTypes() {
        return blockTypes.values();
    }

    /**
     * Retourne le nombre de blocs enregistrés.
     * @return Nombre de blocs
     */
    public static int getBlockCount() {
        return blockTypes.size();
    }
}