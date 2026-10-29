package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Classe pour l'interface des blocs de commande dans Valtherion.
 * Permet de saisir et d'exécuter des commandes.
 */
public class GuiCommandBlock extends Gui {

    // Commande en cours de saisie
    private String inputCommand;

    // Historique des commandes
    private final List<String> commandHistory;

    // Index de l'historique actuel
    private int historyIndex;

    // Indique si l'interface est ouverte
    private boolean isOpen;

    // Couleur du fond de l'interface
    private Vector4f backgroundColor;

    // Rendu de texte pour l'interface
    private FontRenderer fontRenderer;

    // Liste des suggestions de commandes
    private final List<String> commandSuggestions;

    // Index de la suggestion actuelle
    private int suggestionIndex;

    /**
     * Constructeur de l'interface des blocs de commande.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiCommandBlock(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.inputCommand = "/";
        this.commandHistory = new ArrayList<>();
        this.historyIndex = -1;
        this.isOpen = false;
        this.backgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.8f); // Noir semi-transparent
        this.commandSuggestions = new ArrayList<>();
        this.suggestionIndex = -1;

        // Initialiser les suggestions de commandes
        initCommandSuggestions();
    }

    @Override
    public void init() {
        super.init();
        try {
            this.fontRenderer = new FontRenderer(16.0f);
            fontRenderer.init();
        } catch (Exception e) {
            System.err.println("Impossible d'initialiser le rendu de texte pour l'interface des commandes : " + e.getMessage());
        }
    }

    /**
     * Initialise les suggestions de commandes.
     */
    private void initCommandSuggestions() {
        commandSuggestions.add("/help");
        commandSuggestions.add("/tp");
        commandSuggestions.add("/give");
        commandSuggestions.add("/time");
        commandSuggestions.add("/weather");
        commandSuggestions.add("/gamemode");
    }

    @Override
    public void render() {
        if (!isVisible || !isOpen) {
            return;
        }

        // Dessiner le fond de l'interface
        drawBackground();

        // Dessiner la zone de saisie
        drawInputArea();

        // Dessiner les suggestions de commandes
        drawSuggestions();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        float bgWidth = width / 2.0f;
        float bgHeight = height / 3.0f;
        float bgX = (width - bgWidth) / 2.0f;
        float bgY = (height - bgHeight) / 2.0f;

        GL11.glColor4f(
                backgroundColor.x,
                backgroundColor.y,
                backgroundColor.z,
                backgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(bgX, bgY);
        GL11.glVertex2f(bgX + bgWidth, bgY);
        GL11.glVertex2f(bgX + bgWidth, bgY + bgHeight);
        GL11.glVertex2f(bgX, bgY + bgHeight);
        GL11.glEnd();
    }

    /**
     * Dessine la zone de saisie.
     */
    private void drawInputArea() {
        float inputWidth = width / 2.0f - 40.0f;
        float inputHeight = 20.0f;
        float inputX = (width - inputWidth) / 2.0f;
        float inputY = (height - inputHeight) / 2.0f + 20.0f;

        // Dessiner le fond de la zone de saisie
        GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(inputX, inputY);
        GL11.glVertex2f(inputX + inputWidth, inputY);
        GL11.glVertex2f(inputX + inputWidth, inputY + inputHeight);
        GL11.glVertex2f(inputX, inputY + inputHeight);
        GL11.glEnd();

        // Dessiner le texte de saisie
        if (fontRenderer != null) {
            fontRenderer.setTextColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
            fontRenderer.renderText(inputCommand, inputX + 5.0f, inputY + 5.0f);
        }
    }

    /**
     * Dessine les suggestions de commandes.
     */
    private void drawSuggestions() {
        if (commandSuggestions.isEmpty() || suggestionIndex == -1) {
            return;
        }

        float suggestionWidth = width / 2.0f - 40.0f;
        float suggestionHeight = 20.0f;
        float suggestionX = (width - suggestionWidth) / 2.0f;
        float suggestionY = (height - suggestionHeight) / 2.0f - 20.0f;

        // Dessiner le fond de la suggestion
        GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(suggestionX, suggestionY);
        GL11.glVertex2f(suggestionX + suggestionWidth, suggestionY);
        GL11.glVertex2f(suggestionX + suggestionWidth, suggestionY + suggestionHeight);
        GL11.glVertex2f(suggestionX, suggestionY + suggestionHeight);
        GL11.glEnd();

        // Dessiner le texte de la suggestion
        if (fontRenderer != null) {
            fontRenderer.setTextColor(new Vector4f(0.8f, 0.8f, 0.8f, 1.0f));
            fontRenderer.renderText(commandSuggestions.get(suggestionIndex), suggestionX + 5.0f, suggestionY + 5.0f);
        }
    }

    /**
     * Gère les entrées clavier pour l'interface des commandes.
     * @param key Touche pressée.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     */
    @Override
    public void handleKeyInput(int key, int action) {
        if (!isOpen || !isActive) {
            return;
        }

        if (action == GLFW.GLFW_PRESS) {
            switch (key) {
                case GLFW.GLFW_KEY_ESCAPE:
                    // Fermer l'interface
                    close();
                    break;
                case GLFW.GLFW_KEY_ENTER:
                    // Exécuter la commande
                    executeCommand();
                    break;
                case GLFW.GLFW_KEY_BACKSPACE:
                    // Supprimer le dernier caractère
                    if (!inputCommand.isEmpty() && inputCommand.length() > 1) {
                        inputCommand = inputCommand.substring(0, inputCommand.length() - 1);
                    }
                    break;
                case GLFW.GLFW_KEY_UP:
                    // Naviguer dans l'historique (commande précédente)
                    navigateHistory(-1);
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    // Naviguer dans l'historique (commande suivante)
                    navigateHistory(1);
                    break;
                case GLFW.GLFW_KEY_TAB:
                    // Naviguer dans les suggestions
                    navigateSuggestions(1);
                    break;
            }
        }
    }

    /**
     * Gère les entrées de caractères pour l'interface des commandes.
     * @param character Caractère saisi.
     */
    public void handleCharInput(char character) {
        if (!isOpen || !isActive) {
            return;
        }

        inputCommand += character;
        updateSuggestions();
    }

    /**
     * Navigue dans l'historique des commandes.
     * @param direction Direction (-1 pour la commande précédente, 1 pour la commande suivante).
     */
    private void navigateHistory(int direction) {
        if (commandHistory.isEmpty()) {
            return;
        }

        if (historyIndex == -1) {
            // Sauvegarder la commande actuelle
            String currentCommand = inputCommand;
            historyIndex = commandHistory.size() - 1;
            inputCommand = commandHistory.get(historyIndex);
        } else {
            historyIndex += direction;
            historyIndex = Math.max(0, Math.min(historyIndex, commandHistory.size() - 1));
            inputCommand = commandHistory.get(historyIndex);
        }
    }

    /**
     * Navigue dans les suggestions de commandes.
     * @param direction Direction (1 pour la suggestion suivante).
     */
    private void navigateSuggestions(int direction) {
        if (commandSuggestions.isEmpty()) {
            return;
        }

        suggestionIndex = (suggestionIndex + direction) % commandSuggestions.size();
        inputCommand = commandSuggestions.get(suggestionIndex);
    }

    /**
     * Met à jour les suggestions de commandes.
     */
    private void updateSuggestions() {
        if (inputCommand.startsWith("/")) {
            String input = inputCommand.substring(1).toLowerCase();
            suggestionIndex = -1;

            for (int i = 0; i < commandSuggestions.size(); i++) {
                String suggestion = commandSuggestions.get(i).substring(1).toLowerCase();
                if (suggestion.startsWith(input)) {
                    suggestionIndex = i;
                    break;
                }
            }
        }
    }

    /**
     * Exécute la commande actuelle.
     */
    private void executeCommand() {
        if (inputCommand.isEmpty() || !inputCommand.startsWith("/")) {
            return;
        }

        // Ajouter la commande à l'historique
        commandHistory.add(inputCommand);
        historyIndex = -1;

        // Exécuter la commande
        String command = inputCommand.substring(1); // Supprimer le "/"
        valtherion.getGameClient().executeCommand(command);

        // Réinitialiser la commande de saisie
        inputCommand = "/";
        suggestionIndex = -1;
    }

    /**
     * Ouvre l'interface des commandes.
     */
    public void open() {
        isOpen = true;
        isActive = true;
        inputCommand = "/";
        suggestionIndex = -1;
    }

    /**
     * Ferme l'interface des commandes.
     */
    public void close() {
        isOpen = false;
        isActive = false;
        inputCommand = "/";
        suggestionIndex = -1;
    }

    // --- Getters et Setters ---

    public String getInputCommand() {
        return inputCommand;
    }

    public void setInputCommand(String inputCommand) {
        this.inputCommand = inputCommand;
    }

    public List<String> getCommandHistory() {
        return commandHistory;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public List<String> getCommandSuggestions() {
        return commandSuggestions;
    }

    public void addCommandSuggestion(String suggestion) {
        commandSuggestions.add(suggestion);
    }

    /**
     * Nettoie les ressources de l'interface des commandes.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (fontRenderer != null) {
            fontRenderer.cleanup();
        }
        commandHistory.clear();
        commandSuggestions.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface des commandes.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiCommandBlock{" +
                "inputCommand='" + inputCommand + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}
