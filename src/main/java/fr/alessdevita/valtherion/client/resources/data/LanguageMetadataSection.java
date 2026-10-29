package fr.alessdevita.valtherion.client.resources.data;

import java.util.List;

/**
 * Métadonnées de langue pour un pack de ressources.
 * Liste les codes de langue disponibles.
 */
public final class LanguageMetadataSection implements IMetadataSection {
    public static final String SECTION_NAME = "language";

    private final List<Language> languages;

    public LanguageMetadataSection(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return this.languages;
    }

    /**
     * Représentation d'une langue dans les métadonnées.
     */
    public static final class Language {
        private final String code;
        private final String name;
        private final boolean bidirectional;

        public Language(String code, String name, boolean bidirectional) {
            this.code = code;
            this.name = name;
            this.bidirectional = bidirectional;
        }

        public String getCode() { return this.code; }
        public String getName() { return this.name; }
        public boolean isBidirectional() { return this.bidirectional; }
    }
}
