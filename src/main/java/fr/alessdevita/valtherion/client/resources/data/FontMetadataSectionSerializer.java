package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Sérialiseur pour les métadonnées de police.
 */
public final class FontMetadataSectionSerializer
        extends BaseMetadataSectionSerializer<FontMetadataSection> {

    @Override
    public String getSectionName() {
        return FontMetadataSection.SECTION_NAME;
    }

    @Override
    public FontMetadataSection deserialize(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return new FontMetadataSection(null, null, null, null, null, null, null);
        }
        JsonObject object = json.getAsJsonObject();

        float[] top = readFloatArray(object, "uv_top");
        float[] bottom = readFloatArray(object, "uv_bottom");
        float[] left = readFloatArray(object, "uv_left");
        float[] right = readFloatArray(object, "uv_right");
        float[] offX = readFloatArray(object, "offset_x");
        float[] offY = readFloatArray(object, "offset_y");
        float[] advance = readFloatArray(object, "advance");

        return new FontMetadataSection(top, bottom, left, right, offX, offY, advance);
    }

    private static float[] readFloatArray(JsonObject object, String key) {
        if (!object.has(key) || !object.get(key).isJsonArray()) return null;
        JsonArray array = object.getAsJsonArray(key);
        float[] result = new float[array.size()];
        for (int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsFloat();
        }
        return result;
    }
}
