package fr.alessdevita.valtherion.client.gui;

import java.util.*;

/**
 * Classe pour les boutons avec gestion des langues dans Valtherion.
 * Étend GuiButton pour ajouter la gestion des traductions.
 */
public class GuiButtonLanguage extends GuiButton {

    // Clé de traduction du bouton
    private String translationKey;

    // Gestionnaire de langues
    private static LanguageManager languageManager;

    /**
     * Constructeur du bouton avec gestion des langues.
     * @param x Position X.
     * @param y Position Y.
     * @param width Largeur.
     * @param height Hauteur.
     * @param translationKey Clé de traduction du bouton.
     */
    public GuiButtonLanguage(float x, float y, float width, float height, String translationKey) {
        super(x, y, width, height, "");
        this.translationKey = translationKey;
        updateText();
    }

    /**
     * Constructeur du bouton avec gestion des langues et un rendu de texte.
     * @param x Position X.
     * @param y Position Y.
     * @param width Largeur.
     * @param height Hauteur.
     * @param translationKey Clé de traduction du bouton.
     * @param fontRenderer Rendu de texte pour le bouton.
     */
    public GuiButtonLanguage(float x, float y, float width, float height, String translationKey, FontRenderer fontRenderer) {
        super(x, y, width, height, "", fontRenderer);
        this.translationKey = translationKey;
        updateText();
    }

    /**
     * Met à jour le texte du bouton en fonction de la langue actuelle.
     */
    private void updateText() {
        if (languageManager != null) {
            setText(languageManager.getTranslation(translationKey));
        }
    }

    /**
     * Définit le gestionnaire de langues.
     * @param manager Gestionnaire de langues.
     */
    public static void setLanguageManager(LanguageManager manager) {
        languageManager = manager;
        // Mettre à jour tous les boutons existants
        // (Dans un jeu réel, tu pourrais stocker une liste de tous les boutons)
    }

    /**
     * Met à jour le texte de tous les boutons avec la nouvelle langue.
     */
    public static void updateAllButtons() {
        // Dans un jeu réel, tu parcourrais une liste de tous les boutons
        // et appellerais updateText() sur chacun
    }

    // --- Getters et Setters ---

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        updateText();
    }

    public static LanguageManager getLanguageManager() {
        return languageManager;
    }

    /**
     * Retourne une représentation textuelle du bouton avec gestion des langues.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiButtonLanguage{" +
                "translationKey='" + translationKey + '\'' +
                ", text='" + getText() + '\'' +
                '}';
    }

    // --- Classe interne pour le gestionnaire de langues ---

    /**
     * Gestionnaire de langues pour Valtherion.
     */
    public static class LanguageManager {

        // Langue actuelle
        private String currentLanguage;

        // Map des traductions (langue -> (clé -> traduction))
        private final Map<String, Map<String, String>> translations;

        /**
         * Constructeur du gestionnaire de langues.
         */
        public LanguageManager() {
            this.currentLanguage = "fr"; // Langue par défaut : français
            this.translations = new HashMap<>();
        }

        /**
         * Ajoute une traduction.
         * @param language Langue.
         * @param key Clé de traduction.
         * @param translation Traduction.
         */
        public void addTranslation(String language, String key, String translation) {
            translations.computeIfAbsent(language, k -> new HashMap<>()).put(key, translation);
        }

        /**
         * Récupère une traduction.
         * @param key Clé de traduction.
         * @return Traduction ou la clé si non trouvée.
         */
        public String getTranslation(String key) {
            Map<String, String> languageTranslations = translations.get(currentLanguage);
            if (languageTranslations != null) {
                return languageTranslations.getOrDefault(key, key);
            }
            return key;
        }

        /**
         * Définit la langue actuelle.
         * @param language Langue.
         */
        public void setCurrentLanguage(String language) {
            this.currentLanguage = language;
            updateAllButtons();
        }

        /**
         * Récupère la langue actuelle.
         * @return Langue actuelle.
         */
        public String getCurrentLanguage() {
            return currentLanguage;
        }

        /**
         * Ajoute des traductions pour une langue.
         * @param language Langue.
         * @param translations Map des traductions (clé -> traduction).
         */
        public void addTranslations(String language, Map<String, String> translations) {
            this.translations.put(language, translations);
        }
    }
}
