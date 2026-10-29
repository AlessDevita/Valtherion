package fr.alessdevita.valtherion.client.resources;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Locale courante du client : maintient la langue active et ses traductions.
 * Délègue à {@link I18n} pour la résolution globale.
 */
public final class Locale {
    private static final Map<String, Language> LANGUAGES = new ConcurrentHashMap<>();
    private static volatile String currentLanguageCode = "en_US";

    private Locale() { }

    /**
     * Enregistre une langue disponible.
     */
    public static void registerLanguage(Language language) {
        if (language != null && language.getCode() != null) {
            LANGUAGES.put(language.getCode(), language);
        }
    }

    /**
     * Définit la langue courante et charge ses traductions dans {@link I18n}.
     */
    public static void setCurrentLanguage(String code) {
        if (code == null) return;
        currentLanguageCode = code;
        I18n.setCurrentLanguage(code);
        Language language = LANGUAGES.get(code);
        if (language != null) {
            I18n.setTranslations(language.getTranslations());
        }
    }

    public static String getCurrentLanguage() {
        return currentLanguageCode;
    }

    public static Language getLanguage(String code) {
        return LANGUAGES.get(code);
    }

    public static Language getCurrentLanguageInstance() {
        return LANGUAGES.get(currentLanguageCode);
    }

    /**
     * Charge des traductions depuis un objet {@link Properties}.
     *
     * @param code le code de langue cible
     * @param properties les traductions à charger
     */
    public static void loadTranslations(String code, Properties properties) {
        if (code == null || properties == null) return;
        Language language = LANGUAGES.computeIfAbsent(code, c -> new Language(c));
        for (String key : properties.stringPropertyNames()) {
            language.addTranslation(key, properties.getProperty(key));
        }
        if (code.equals(currentLanguageCode)) {
            I18n.setTranslations(language.getTranslations());
        }
    }

    /**
     * Charge des traductions depuis une {@link Map}.
     */
    public static void loadTranslations(String code, Map<String, String> translations) {
        if (code == null) return;
        Language language = LANGUAGES.computeIfAbsent(code, c -> new Language(c));
        language.addAllTranslations(translations);
        if (code.equals(currentLanguageCode)) {
            I18n.setTranslations(language.getTranslations());
        }
    }
}
