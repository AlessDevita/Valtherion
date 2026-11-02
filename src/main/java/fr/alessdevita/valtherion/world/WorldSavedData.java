package fr.alessdevita.valtherion.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Données persistantes du monde.
 * Permet de sauvegarder et charger des données personnalisées associées au monde.
 */
public class WorldSavedData {

    private final String name;
    private final Map<String, Object> data;
    private boolean dirty;

    /**
     * Crée un objet de données sauvegardées.
     * @param name Nom unique des données
     */
    public WorldSavedData(String name) {
        this.name = name;
        this.data = new HashMap<>();
        this.dirty = false;
    }

    /**
     * Stocke une valeur.
     * @param key Clé
     * @param value Valeur
     */
    public void set(String key, Object value) {
        data.put(key, value);
        dirty = true;
    }

    /**
     * Récupère une valeur.
     * @param key Clé
     * @return Valeur ou null
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Vérifie si une clé existe.
     * @param key Clé
     * @return true si la clé existe
     */
    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    /**
     * Supprime une clé.
     * @param key Clé
     */
    public void remove(String key) {
        data.remove(key);
        dirty = true;
    }

    /**
     * Retourne le nom des données.
     * @return Nom
     */
    public String getName() {
        return name;
    }

    /**
     * Vérifie si les données ont été modifiées.
     * @return true si modifiées
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Marque les données comme propres.
     */
    public void markClean() {
        dirty = false;
    }

    /**
     * Retourne le nombre de données stockées.
     * @return Nombre
     */
    public int size() {
        return data.size();
    }
}
