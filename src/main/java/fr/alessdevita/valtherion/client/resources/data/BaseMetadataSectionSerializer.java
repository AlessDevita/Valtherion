package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonElement;

/**
 * Base abstraite pour les sérialiseurs de sections de métadonnées.
 * Fournit une sérialisation par défaut renvoyant un objet vide.
 *
 * @param <T> le type de section
 */
public abstract class BaseMetadataSectionSerializer<T extends IMetadataSection>
        implements IMetadataSectionSerializer<T> {

    @Override
    public JsonElement serialize(T section) {
        // Par défaut, les sections sont en lecture seule.
        return new com.google.gson.JsonObject();
    }
}
