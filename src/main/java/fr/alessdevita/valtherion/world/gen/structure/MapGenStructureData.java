package fr.alessdevita.valtherion.world.gen.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * Données d'une structure générée.
 * Stocke les informations nécessaires pour sauvegarder et charger une structure.
 */
public class MapGenStructureData {

    private final String structureId;
    private final int x;
    private final int z;
    private final int componentCount;
    private final Map<String, Object> components;

    /**
     * Crée des données de structure.
     * @param structureId Identifiant de la structure
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @param componentCount Nombre de composants
     */
    public MapGenStructureData(String structureId, int x, int z, int componentCount) {
        this.structureId = structureId;
        this.x = x;
        this.z = z;
        this.componentCount = componentCount;
        this.components = new HashMap<>();
    }

    /**
     * Ajoute un composant aux données.
     * @param key Clé du composant
     * @param value Données du composant
     */
    public void addComponent(String key, Object value) {
        components.put(key, value);
    }

    /**
     * Obtient les données d'un composant.
     * @param key Clé du composant
     * @return Les données du composant
     */
    public Object getComponent(String key) {
        return components.get(key);
    }

    public String getStructureId() {
        return structureId;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getComponentCount() {
        return componentCount;
    }

    public Map<String, Object> getComponents() {
        return components;
    }
}
