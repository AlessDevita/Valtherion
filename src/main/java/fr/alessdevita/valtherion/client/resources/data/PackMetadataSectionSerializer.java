package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Sérialiseur pour les métadonnées de pack.
 */
public final class PackMetadataSectionSerializer
        extends BaseMetadataSectionSerializer<PackMetadataSection> {

    @Override
    public String getSectionName() {
        return PackMetadataSection.SECTION_NAME;
    }

    @Override
    public PackMetadataSection deserialize(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return new PackMetadataSection("", 0);
        }
        JsonObject object = json.getAsJsonObject();
        String description = object.has("description") ? object.get("description").getAsString() : "";
        int format = object.has("pack_format") ? object.get("pack_format").getAsInt() : 0;
        return new PackMetadataSection(description, format);
    }
}
