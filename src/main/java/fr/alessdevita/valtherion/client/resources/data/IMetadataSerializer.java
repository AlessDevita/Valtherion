package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Registre central des sérialiseurs de sections de métadonnées.
 * Permet de lire / écrire des sections typées dans les fichiers
 * {@code pack.mcmeta}, {@code *.mcmeta}, etc.
 */
public final class IMetadataSerializer {
    private final Map<String, IMetadataSectionSerializer<?>> serializersByName = new HashMap<>();
    private final Map<Class<?>, IMetadataSectionSerializer<?>> serializersByType = new HashMap<>();

    /**
     * Enregistre un sérialiseur pour un type de section donné.
     *
     * @param serializer le sérialiseur
     * @param sectionClass la classe de la section produite
     */
    public <T extends IMetadataSection> void registerMetadataSectionType(
            IMetadataSectionSerializer<T> serializer, Class<T> sectionClass) {
        if (serializer == null || sectionClass == null) return;
        this.serializersByName.put(serializer.getSectionName(), serializer);
        this.serializersByType.put(sectionClass, serializer);
    }

    /**
     * @deprecated utiliser {@link #registerMetadataSectionType(IMetadataSectionSerializer, Class)}
     */
    @Deprecated
    public void registerMetadataSectionType(Object serializer, Class<?> sectionType) {
        if (serializer instanceof IMetadataSectionSerializer<?> s) {
            this.serializersByName.put(s.getSectionName(), s);
        }
        this.serializersByType.put(sectionType, (IMetadataSectionSerializer<?>) serializer);
    }

    /**
     * @deprecated utiliser {@link #getMetadataSerializer(Class)}
     */
    @Deprecated
    public Object getMetadataSectionType(Class<?> sectionType) {
        return this.serializersByType.get(sectionType);
    }

    /** Retourne le sérialiseur enregistré pour la classe de section donnée. */
    @SuppressWarnings("unchecked")
    public <T extends IMetadataSection> IMetadataSectionSerializer<T> getMetadataSerializer(Class<T> sectionClass) {
        return (IMetadataSectionSerializer<T>) this.serializersByType.get(sectionClass);
    }

    /** Retourne le sérialiseur portant ce nom de section. */
    public IMetadataSectionSerializer<?> getMetadataSerializer(String sectionName) {
        return this.serializersByName.get(sectionName);
    }

    /**
     * Lit une section de métadonnées depuis un contenu JSON.
     *
     * @param json le contenu JSON brut
     * @param sectionClass la classe de la section attendue
     * @return la section désérialisée, ou {@code null} si absente ou erreur
     */
    @SuppressWarnings("unchecked")
    public <T extends IMetadataSection> T parseMetadata(String json, Class<T> sectionClass) {
        if (json == null || json.isBlank()) return null;
        IMetadataSectionSerializer<T> serializer = getMetadataSerializer(sectionClass);
        if (serializer == null) return null;
        try {
            JsonElement root = JsonParser.parseString(json);
            JsonObject object = root.getAsJsonObject();
            JsonElement section = object.get(serializer.getSectionName());
            if (section == null) return null;
            return serializer.deserialize(section);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Sérialise une section en élément JSON.
     *
     * @param section la section à sérialiser
     * @return l'élément JSON, ou {@code null} si aucun sérialiseur
     */
    @SuppressWarnings("unchecked")
    public JsonElement serialize(IMetadataSection section) {
        if (section == null) return null;
        IMetadataSectionSerializer<IMetadataSection> serializer =
                (IMetadataSectionSerializer<IMetadataSection>) this.serializersByType.get(section.getClass());
        if (serializer == null) return null;
        return serializer.serialize((IMetadataSection) section);
    }
}
