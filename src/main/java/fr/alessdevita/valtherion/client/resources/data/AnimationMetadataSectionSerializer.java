package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Sérialiseur pour les métadonnées d'animation de texture.
 */
public final class AnimationMetadataSectionSerializer
        extends BaseMetadataSectionSerializer<AnimationMetadataSection> {

    private static final int DEFAULT_FRAME_TIME = 1;

    @Override
    public String getSectionName() {
        return AnimationMetadataSection.SECTION_NAME;
    }

    @Override
    public AnimationMetadataSection deserialize(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return new AnimationMetadataSection(0, 0, DEFAULT_FRAME_TIME, null, false);
        }
        JsonObject object = json.getAsJsonObject();

        int width = object.has("width") ? object.get("width").getAsInt() : 0;
        int height = object.has("height") ? object.get("height").getAsInt() : 0;
        int frameTime = object.has("frametime") ? object.get("frametime").getAsInt() : DEFAULT_FRAME_TIME;
        boolean interpolate = object.has("interpolate") && object.get("interpolate").getAsBoolean();

        List<AnimationFrame> frames = new ArrayList<>();
        if (object.has("frames")) {
            JsonArray framesArray = object.getAsJsonArray("frames");
            for (JsonElement frameElement : framesArray) {
                if (frameElement.isJsonPrimitive()) {
                    frames.add(new AnimationFrame(frameElement.getAsInt()));
                } else if (frameElement.isJsonObject()) {
                    JsonObject frameObj = frameElement.getAsJsonObject();
                    int index = frameObj.has("index") ? frameObj.get("index").getAsInt() : 0;
                    int time = frameObj.has("time") ? frameObj.get("time").getAsInt() : -1;
                    frames.add(new AnimationFrame(index, time));
                }
            }
        }

        return new AnimationMetadataSection(width, height, frameTime, frames, interpolate);
    }
}
