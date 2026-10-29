package fr.alessdevita.valtherion.client.resources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Représente une langue (code, nom, bidirectional) et porte ses traductions.
 */
public final class Language {
    private final String code;
    private final String name;
    private final boolean bidirectional;
    private final Map<String, String> translations = new ConcurrentHashMap<>();

    public Language(String code) {
        this(code, code, false);
    }

    public Language(String code, String name, boolean bidirectional) {
        this.code = code;
        this.name = name == null ? code : name;
        this.bidirectional = bidirectional;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public boolean isBidirectional() {
        return this.bidirectional;
    }

    public Map<String, String> getTranslations() {
        return this.translations;
    }

    public void addTranslation(String key, String value) {
        if (key != null && value != null) {
            this.translations.put(key, value);
        }
    }

    public void addAllTranslations(Map<String, String> map) {
        if (map != null) {
            this.translations.putAll(map);
        }
    }

    public String translate(String key) {
        if (key == null) return "";
        String value = this.translations.get(key);
        return value != null ? value : key;
    }

    @Override
    public String toString() {
        return this.code + " (" + this.name + ")";
    }
}
