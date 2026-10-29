package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonElement;

/**
 * Sérialiseur / désérialiseur pour une section de métadonnées.
 *
 * @param <T> le type de {@link IMetadataSection} produit
 */
public interface IMetadataSectionSerializer<T extends IMetadataSection> {

    /** Nom de la section tel qu'il apparaît dans le JSON (ex: "animation"). */
    String getSectionName();

    /** Désérialise un élément JSON en une section de métadonnées. */
    T deserialize(JsonElement json);

    /** Sérialise une section de métadonnées en élément JSON. */
    JsonElement serialize(T section);
}
