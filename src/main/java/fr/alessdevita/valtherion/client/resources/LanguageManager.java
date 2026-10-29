package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;
import fr.alessdevita.valtherion.client.resources.data.LanguageMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire de langues : charge les fichiers de traduction depuis les
 * packs de ressources et maintient la langue courante via {@link Locale}.
 */
public final class LanguageManager implements IResourceManagerReloadListener {
    private static final String LANG_PATH_PREFIX = "assets/valtherion/lang/";
    private static final String LANG_EXT = ".lang";

    private final IMetadataSerializer metadataSerializer;
    private final String currentLanguageCode;
    private final Map<String, Language> languages = new ConcurrentHashMap<>();

    public LanguageManager() {
        this(null, "en_US");
    }

    public LanguageManager(IMetadataSerializer metadataSerializer, String language) {
        this.metadataSerializer = metadataSerializer;
        this.currentLanguageCode = (language == null || language.isEmpty()) ? "en_US" : language;
        Locale.setCurrentLanguage(this.currentLanguageCode);
    }

    public String getCurrentLanguage() {
        return this.currentLanguageCode;
    }

    /**
     * Charge les traductions d'une langue depuis un flux (format Properties).
     */
    public Language loadLanguage(String code, InputStream stream) throws IOException {
        if (code == null || stream == null) return null;
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            properties.load(reader);
        }
        Language language = new Language(code);
        for (String key : properties.stringPropertyNames()) {
            language.addTranslation(key, properties.getProperty(key));
        }
        this.languages.put(code, language);
        Locale.registerLanguage(language);
        return language;
    }

    /**
     * Parse les métadonnées de langue d'un pack.
     */
    public LanguageMetadataSection parseLanguageMetadata(String json) {
        if (this.metadataSerializer == null || json == null) return null;
        return this.metadataSerializer.parseMetadata(json, LanguageMetadataSection.class);
    }

    /**
     * Charge les fichiers de traduction depuis un gestionnaire de ressources.
     *
     * @param resourceManager le gestionnaire de ressources à interroger
     */
    public void loadAllLanguages(IResourceManager resourceManager) {
        if (resourceManager == null) return;
        ResourceLocation location = new ResourceLocation(
                "valtherion", "lang/" + this.currentLanguageCode + LANG_EXT);
        IResource resource = resourceManager.getResource(location);
        if (resource == null) return;
        try (InputStream stream = resource.getInputStream()) {
            loadLanguage(this.currentLanguageCode, stream);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la langue: " + e.getMessage());
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        loadAllLanguages(resourceManager);
    }

    public Map<String, Language> getLanguages() {
        return new HashMap<>(this.languages);
    }
}
