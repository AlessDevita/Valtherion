package fr.alessdevita.valtherion.client.resources.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Sérialiseur pour les métadonnées de langue.
 */
public final class LanguageMetadataSectionSerializer
        extends BaseMetadataSectionSerializer<LanguageMetadataSection> {

    @Override
    public String getSectionName() {
        return LanguageMetadataSection.SECTION_NAME;
    }

    @Override
    public LanguageMetadataSection deserialize(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return new LanguageMetadataSection(new ArrayList<>());
        }
        JsonObject object = json.getAsJsonObject();
        List<LanguageMetadataSection.Language> languages = new ArrayList<>();

        if (object.has("languages") && object.get("languages").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("languages");
            for (JsonElement element : array) {
                if (!element.isJsonObject()) continue;
                JsonObject langObj = element.getAsJsonObject();
                String code = langObj.has("code") ? langObj.get("code").getAsString() : "";
                String name = langObj.has("name") ? langObj.get("name").getAsString() : code;
                boolean rtl = langObj.has("bidirectional") && langObj.get("bidirectional").getAsBoolean();
                languages.add(new LanguageMetadataSection.Language(code, name, rtl));
            }
        }

        return new LanguageMetadataSection(languages);
    }
}
