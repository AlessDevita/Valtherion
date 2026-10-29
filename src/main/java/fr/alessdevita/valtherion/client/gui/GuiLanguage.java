package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface de sélection des langues dans Valtherion.
 * Permet de choisir la langue de l'interface utilisateur.
 */
public class GuiLanguage extends Gui {

    // Liste des langues disponibles
    private final List<Language> availableLanguages;

    // Langue actuellement sélectionnée
    private Language selectedLanguage;

    // Bouton pour appliquer la langue
    private GuiButton applyButton;

    // Bouton pour annuler
    private GuiButton cancelButton;

    // Gestionnaire de langues
    private LanguageManager languageManager;

    /**
     * Constructeur de l'interface de sélection des langues.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiLanguage(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.availableLanguages = new ArrayList<>();
        this.languageManager = new LanguageManager();

        // Initialiser les langues disponibles
        initAvailableLanguages();

        // Définir la langue par défaut
        selectedLanguage = availableLanguages.get(0);

        // Initialiser les boutons
        initButtons();
    }

    public GuiLanguage(GuiOptions guiOptions, Object gameSettings, List<Language> availableLanguages) {
        this.availableLanguages = availableLanguages;
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les langues disponibles.
     */
    private void initAvailableLanguages() {
        availableLanguages.add(new Language("Français", "fr"));
        availableLanguages.add(new Language("English", "en"));
        availableLanguages.add(new Language("Español", "es"));
        availableLanguages.add(new Language("Deutsch", "de"));
        availableLanguages.add(new Language("Italiano", "it"));
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour appliquer la langue
        applyButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Appliquer"
        );
        applyButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        applyButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        applyButton.setOnClick(button -> applyLanguage());

        // Bouton pour annuler
        cancelButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Annuler"
        );
        cancelButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        cancelButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        cancelButton.setOnClick(button -> valtherion.openOptionsMenu());

        // Ajouter les boutons à la GUI
        addElement(applyButton);
        addElement(cancelButton);
    }

    /**
     * Applique la langue sélectionnée.
     */
    private void applyLanguage() {
        languageManager.setCurrentLanguage(selectedLanguage.getCode());
        valtherion.updateLanguage();
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner les langues disponibles
        drawLanguages();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.1f, 0.1f, 0.2f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le titre de l'interface.
     */
    private void drawTitle() {
        // Dessiner un rectangle pour simuler le titre "Sélection de la Langue"
        float titleWidth = 200.0f;
        float titleHeight = 20.0f;
        float titleX = (width - titleWidth) / 2.0f;
        float titleY = height - 30.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY + titleHeight);
        GL11.glVertex2f(titleX, titleY + titleHeight);
        GL11.glEnd();
    }

    /**
     * Dessine les langues disponibles.
     */
    private void drawLanguages() {
        float startX = (width - 200.0f) / 2.0f;
        float startY = height / 2.0f + 50.0f;
        float lineHeight = 30.0f;

        for (int i = 0; i < availableLanguages.size(); i++) {
            Language language = availableLanguages.get(i);

            // Dessiner la langue
            float y = startY - i * lineHeight;

            // Couleur en fonction de si la langue est sélectionnée
            if (selectedLanguage == language) {
                GL11.glColor4f(0.2f, 0.6f, 1.0f, 1.0f);
            } else {
                GL11.glColor4f(0.5f, 0.5f, 0.8f, 1.0f);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, y);
            GL11.glVertex2f(startX + 200.0f, y);
            GL11.glVertex2f(startX + 200.0f, y + lineHeight);
            GL11.glVertex2f(startX, y + lineHeight);
            GL11.glEnd();
        }
    }

    /**
     * Gère les entrées souris pour sélectionner une langue.
     * @param button Bouton de la souris.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     * @param x Position X de la souris.
     * @param y Position Y de la souris.
     */
    @Override
    public void handleMouseInput(int button, int action, double x, double y) {
        super.handleMouseInput(button, action, x, y);

        if (action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            float startX = (width - 200.0f) / 2.0f;
            float startY = height / 2.0f + 50.0f;
            float lineHeight = 30.0f;

            for (int i = 0; i < availableLanguages.size(); i++) {
                float yPos = startY - i * lineHeight;

                if (x >= startX && x <= startX + 200.0f && y >= yPos && y <= yPos + lineHeight) {
                    selectedLanguage = availableLanguages.get(i);
                    break;
                }
            }
        }
    }

    // --- Getters et Setters ---

    public List<Language> getAvailableLanguages() {
        return availableLanguages;
    }

    public Language getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(Language selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        availableLanguages.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiLanguage{" +
                "selectedLanguage=" + selectedLanguage +
                '}';
    }

    // --- Classes internes ---

    /**
     * Classe pour représenter une langue.
     */
    public static class Language {
        private String name;
        private String code;

        public Language(String name, String code) {
            this.name = name;
            this.code = code;
        }

        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    /**
     * Gestionnaire de langues pour Valtherion.
     */
    public static class LanguageManager {
        private String currentLanguage;
        private final Map<String, Map<String, String>> translations;

        public LanguageManager() {
            this.currentLanguage = "fr";
            this.translations = new HashMap<>();
        }

        public void setCurrentLanguage(String languageCode) {
            this.currentLanguage = languageCode;
        }

        public String getCurrentLanguage() {
            return currentLanguage;
        }

        public void addTranslation(String language, String key, String translation) {
            translations.computeIfAbsent(language, k -> new HashMap<>()).put(key, translation);
        }

        public String getTranslation(String key) {
            Map<String, String> languageTranslations = translations.get(currentLanguage);
            if (languageTranslations != null) {
                return languageTranslations.getOrDefault(key, key);
            }
            return key;
        }
    }
}
