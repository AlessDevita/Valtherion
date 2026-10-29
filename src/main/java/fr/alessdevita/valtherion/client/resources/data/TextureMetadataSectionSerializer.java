package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Sérialiseur pour les métadonnées de texture.
 */
public final class TextureMetadataSectionSerializer
        extends BaseMetadataSectionSerializer<TextureMetadataSection> {

    @Override
    public String getSectionName() {
        return TextureMetadataSection.SECTION_NAME;
    }

    @Override
    public TextureMetadataSection deserialize(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return new TextureMetadataSection(false, false);
        }
        JsonObject object = json.getAsJsonObject();
        boolean blur = object.has("blur") && object.get("blur").getAsBoolean();
        boolean clamp = object.has("clamp") && object.get("clamp").getAsBoolean();
        return new TextureMetadataSection(blur, clamp);
    }
}
