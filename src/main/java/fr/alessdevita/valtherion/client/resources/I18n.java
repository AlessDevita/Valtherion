package fr.alessdevita.valtherion.client.resources;

import java.util.IllegalFormatException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internationalisation : traduit les clés de texte en chaînes localisées.
 */
public final class I18n {
    private static final Map<String, String> TRANSLATIONS = new ConcurrentHashMap<>();
    private static volatile String currentLanguage = "en_US";

    private I18n() { }

    /** Définit les traductions pour la langue courante. */
    public static void setTranslations(Map<String, String> translations) {
        TRANSLATIONS.clear();
        if (translations != null) {
            TRANSLATIONS.putAll(translations);
        }
    }

    /** Ajoute ou remplace une traduction. */
    public static void put(String key, String value) {
        if (key != null && value != null) {
            TRANSLATIONS.put(key, value);
        }
    }

    public static void setCurrentLanguage(String language) {
        if (language != null) currentLanguage = language;
    }

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Traduit une clé en chaîne localisée.
     * Retourne la clé elle-même si aucune traduction n'existe.
     */
    public static String translate(String key) {
        if (key == null) return "";
        String value = TRANSLATIONS.get(key);
        return value != null ? value : key;
    }

    /**
     * Traduit une clé avec des paramètres de formatage.
     *
     * @param key la clé de traduction
     * @param params les paramètres à insérer
     * @return la chaîne formatée
     */
    public static String translate(String key, Object... params) {
        String pattern = translate(key);
        if (params == null || params.length == 0) return pattern;
        try {
            return String.format(pattern, params);
        } catch (IllegalFormatException e) {
            return pattern;
        }
    }

    /** {@code true} si une traduction existe pour cette clé. */
    public static boolean hasKey(String key) {
        return key != null && TRANSLATIONS.containsKey(key);
    }
}
