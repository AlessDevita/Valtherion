package fr.alessdevita.valtherion.world.gen.structure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire d'I/O pour les structures.
 * Permet de sauvegarder et charger les données des structures.
 */
public class MapGenStructureIO {

    private static final Map<String, StructureStart> LOADABLE_STRUCTURES = new ConcurrentHashMap<>();
    private static final String STRUCTURES_DIR = "saves/structures";

    /**
     * Enregistre une structure dans la carte des structures chargeables.
     * @param key Clé de la structure
     * @param start Point de départ de la structure
     */
    public static void saveStructureData(String key, StructureStart start) {
        if (start != null) {
            LOADABLE_STRUCTURES.put(key, start);
        }
    }

    /**
     * Charge une structure depuis la carte des structures chargeables.
     * @param key Clé de la structure
     * @return Le point de départ de la structure, ou null si non trouvée
     */
    public static StructureStart getStructureData(String key) {
        return LOADABLE_STRUCTURES.get(key);
    }

    /**
     * Sauvegarde les données d'une structure dans un fichier.
     * @param key Clé de la structure
     * @param data Données de la structure
     * @return true si la sauvegarde a réussi
     */
    public static boolean saveStructureToFile(String key, MapGenStructureData data) {
        try {
            File dir = new File(STRUCTURES_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, key + ".dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(data.getStructureId());
                oos.writeInt(data.getX());
                oos.writeInt(data.getZ());
                oos.writeInt(data.getComponentCount());
                oos.writeObject(data.getComponents());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Charge les données d'une structure depuis un fichier.
     * @param key Clé de la structure
     * @return Les données de la structure, ou null si non trouvée
     */
    public static MapGenStructureData loadStructureFromFile(String key) {
        try {
            File file = new File(STRUCTURES_DIR, key + ".dat");
            if (!file.exists()) {
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                String structureId = (String) ois.readObject();
                int x = ois.readInt();
                int z = ois.readInt();
                int componentCount = ois.readInt();
                @SuppressWarnings("unchecked")
                Map<String, Object> components = (Map<String, Object>) ois.readObject();

                MapGenStructureData data = new MapGenStructureData(structureId, x, z, componentCount);
                data.getComponents().putAll(components);
                return data;
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Supprime les données d'une structure.
     * @param key Clé de la structure
     */
    public static void removeStructureData(String key) {
        LOADABLE_STRUCTURES.remove(key);
        File file = new File(STRUCTURES_DIR, key + ".dat");
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Vide la carte des structures chargeables.
     */
    public static void clearStructureCache() {
        LOADABLE_STRUCTURES.clear();
    }

    /**
     * Vérifie si une structure est enregistrée.
     * @param key Clé de la structure
     * @return true si la structure est enregistrée
     */
    public static boolean hasStructureData(String key) {
        return LOADABLE_STRUCTURES.containsKey(key);
    }
}
