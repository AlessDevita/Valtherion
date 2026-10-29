package fr.alessdevita.valtherion.save;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Classe pour sérialiser et désérialiser les objets dans Valtherion.
 * Utilise Gson pour la sérialisation JSON.
 */
public class Serializer {

    // Instance de Gson pour la sérialisation
    private Gson gson;

    // Map des adaptateurs de type personnalisés
    private final Map<Type, Object> typeAdapters;

    /**
     * Constructeur du sérialiseur.
     */
    public Serializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.enableComplexMapKeySerialization();

        // Ajouter des adaptateurs de type personnalisés
        this.typeAdapters = new HashMap<>();

        this.gson = builder.create();
    }

    /**
     * Sérialise un objet en JSON.
     * @param object Objet à sérialiser.
     * @return Chaîne JSON.
     */
    public String serializeToJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Désérialise un objet depuis une chaîne JSON.
     * @param json Chaîne JSON.
     * @param classOfT Classe de l'objet.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * Désérialise un objet depuis une chaîne JSON avec un type personnalisé.
     * @param json Chaîne JSON.
     * @param typeOfT Type de l'objet.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    /**
     * Sérialise un objet en une Map.
     * @param object Objet à sérialiser.
     * @return Map des données sérialisées.
     */
    public Map<String, Object> serializeToMap(Object object) {
        if (object == null) {
            return null;
        }

        // Si l'objet est déjà une Map, le retourner directement
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        }

        // Sérialiser l'objet en JSON, puis le convertir en Map
        String json = serializeToJson(object);
        return deserializeFromJson(json, Map.class);
    }

    /**
     * Désérialise un objet depuis une Map.
     * @param map Map des données sérialisées.
     * @param classOfT Classe de l'objet.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromMap(Map<String, Object> map, Class<T> classOfT) {
        if (map == null) {
            return null;
        }

        // Convertir la Map en JSON, puis désérialiser
        String json = serializeToJson(map);
        return deserializeFromJson(json, classOfT);
    }

    /**
     * Ajoute un adaptateur de type personnalisé.
     * @param type Type de l'objet.
     * @param adapter Adaptateur à ajouter.
     */
    public void addTypeAdapter(Type type, Object adapter) {
        typeAdapters.put(type, adapter);
        // Reconstruire Gson avec les nouveaux adaptateurs
        rebuildGson();
    }

    /**
     * Reconstruit Gson avec les adaptateurs de type actuels.
     */
    private void rebuildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.enableComplexMapKeySerialization();

        // Ajouter les adaptateurs de type
        for (Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }

        this.gson = builder.create();
    }

    /**
     * Sérialise une liste d'objets.
     * @param objects Liste d'objets à sérialiser.
     * @return Liste des objets sérialisés.
     */
    public List<Object> serializeList(List<?> objects) {
        List<Object> serializedList = new ArrayList<>();
        for (Object object : objects) {
            serializedList.add(serializeToMap(object));
        }
        return serializedList;
    }

    /**
     * Désérialise une liste d'objets.
     * @param serializedList Liste des objets sérialisés.
     * @param classOfT Classe des objets.
     * @return Liste des objets désérialisés.
     */
    public <T> List<T> deserializeList(List<Map<String, Object>> serializedList, Class<T> classOfT) {
        List<T> deserializedList = new ArrayList<>();
        for (Map<String, Object> map : serializedList) {
            deserializedList.add(deserializeFromMap(map, classOfT));
        }
        return deserializedList;
    }
}