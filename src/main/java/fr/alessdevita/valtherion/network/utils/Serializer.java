package fr.alessdevita.valtherion.network.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Sérialiseur générique pour Valtherion.
 * Permet de sérialiser et désérialiser des objets en JSON ou en bytes.
 */
public class Serializer {

    // Instance de Gson pour la sérialisation JSON
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
     * Désérialise un objet depuis une chaîne JSON avec un TypeToken.
     * @param json Chaîne JSON.
     * @param typeOfT TypeToken de l'objet.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromJson(String json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT.getType());
    }

    /**
     * Sérialise un objet en bytes.
     * @param object Objet à sérialiser.
     * @return Tableau de bytes.
     * @throws IOException Si une erreur se produit lors de la sérialisation.
     */
    public byte[] serializeToBytes(Object object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(object);
        objectStream.close();
        return byteStream.toByteArray();
    }

    /**
     * Désérialise un objet depuis des bytes.
     * @param bytes Tableau de bytes.
     * @return Objet désérialisé.
     * @throws IOException Si une erreur se produit lors de la désérialisation.
     * @throws ClassNotFoundException Si la classe de l'objet n'est pas trouvée.
     */
    public Object deserializeFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object object = objectStream.readObject();
        objectStream.close();
        return object;
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
        return deserializeFromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
    }

    private Map<String, Object> deserializeFromJson(String json, Type type) {
        return gson.fromJson(json, type);
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
     * Sérialise une liste d'objets en JSON.
     * @param objects Liste d'objets à sérialiser.
     * @return Chaîne JSON.
     */
    public String serializeListToJson(List<?> objects) {
        return gson.toJson(objects);
    }

    /**
     * Désérialise une liste d'objets depuis une chaîne JSON.
     * @param json Chaîne JSON.
     * @param classOfT Classe des objets.
     * @return Liste des objets désérialisés.
     */
    public <T> List<T> deserializeListFromJson(String json, Class<T> classOfT) {
        Type listType = new TypeToken<List<T>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    /**
     * Sérialise une liste d'objets en bytes.
     * @param objects Liste d'objets à sérialiser.
     * @return Tableau de bytes.
     * @throws IOException Si une erreur se produit lors de la sérialisation.
     */
    public byte[] serializeListToBytes(List<?> objects) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(objects);
        objectStream.close();
        return byteStream.toByteArray();
    }

    /**
     * Désérialise une liste d'objets depuis des bytes.
     * @param bytes Tableau de bytes.
     * @return Liste des objets désérialisés.
     * @throws IOException Si une erreur se produit lors de la désérialisation.
     * @throws ClassNotFoundException Si la classe des objets n'est pas trouvée.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> deserializeListFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        List<T> objects = (List<T>) objectStream.readObject();
        objectStream.close();
        return objects;
    }

    /**
     * Sérialise un objet en JSON avec un encodage spécifique.
     * @param object Objet à sérialiser.
     * @param charset Encodage à utiliser.
     * @return Tableau de bytes.
     */
    public byte[] serializeToJsonBytes(Object object, String charset) throws UnsupportedEncodingException {
        return serializeToJson(object).getBytes(charset);
    }

    /**
     * Sérialise un objet en JSON avec l'encodage UTF-8.
     * @param object Objet à sérialiser.
     * @return Tableau de bytes.
     */
    public byte[] serializeToJsonBytes(Object object) throws UnsupportedEncodingException {
        return serializeToJsonBytes(object, StandardCharsets.UTF_8.name());
    }

    /**
     * Désérialise un objet depuis des bytes JSON.
     * @param bytes Tableau de bytes.
     * @param classOfT Classe de l'objet.
     * @param charset Encodage à utiliser.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromJsonBytes(byte[] bytes, Class<T> classOfT, String charset) throws UnsupportedEncodingException {
        String json = new String(bytes, charset);
        return deserializeFromJson(json, classOfT);
    }

    /**
     * Désérialise un objet depuis des bytes JSON avec l'encodage UTF-8.
     * @param bytes Tableau de bytes.
     * @param classOfT Classe de l'objet.
     * @return Objet désérialisé.
     */
    public <T> T deserializeFromJsonBytes(byte[] bytes, Class<T> classOfT) throws UnsupportedEncodingException {
        return deserializeFromJsonBytes(bytes, classOfT, StandardCharsets.UTF_8.name());
    }
}