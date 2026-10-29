package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Nouvelle interface de chat dans Valtherion.
 * Permet d'afficher les messages avec des couleurs personnalisées,
 * un historique, et des suggestions de commandes.
 */
public class GuiNewChat extends Gui {

    // Liste des messages de chat
    private final List<ChatMessage> chatMessages;

    // Message en cours de saisie
    private String inputMessage;

    // Historique des messages
    private final List<String> messageHistory;

    // Index de l'historique actuel
    private int historyIndex;

    // Champ de texte pour la saisie
    private GuiTextField inputField;

    // Bouton pour envoyer le message
    private GuiButton sendButton;

    // Liste des suggestions de commandes
    private final List<String> commandSuggestions;

    // Index de la suggestion actuelle
    private int suggestionIndex;

    // Indique si le chat est ouvert
    private boolean isOpen;

    // Couleur de fond du chat
    private Vector4f chatBackgroundColor;

    // Couleur de fond de la zone de saisie
    private Vector4f inputBackgroundColor;

    // Rendu de texte pour le chat
    private FontRenderer fontRenderer;

    // Hauteur maximale du chat (en nombre de lignes)
    private int maxChatLines;

    // Temps d'affichage des messages (en secondes)
    private float messageDisplayTime;

    /**
     * Constructeur de la nouvelle interface de chat.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiNewChat(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.chatMessages = new ArrayList<>();
        this.inputMessage = "";
        this.messageHistory = new ArrayList<>();
        this.historyIndex = -1;
        this.commandSuggestions = new ArrayList<>();
        this.suggestionIndex = -1;
        this.isOpen = false;
        this.chatBackgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.7f);
        this.inputBackgroundColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.9f);
        this.maxChatLines = 10;
        this.messageDisplayTime = 10.0f;

        // Initialiser les suggestions de commandes
        initCommandSuggestions();

        // Initialiser les éléments
        initElements();
    }

    @Override
    public void init() {
        super.init();
        try {
            this.fontRenderer = new FontRenderer(16.0f);
            fontRenderer.init();
        } catch (Exception e) {
            System.err.println("Impossible d'initialiser le rendu de texte pour le chat : " + e.getMessage());
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
    }

    /**
     * Initialise les éléments de l'interface.
     */
    private void initElements() {
        // Champ de texte pour la saisie
        inputField = new GuiTextField(10.0f, height - 30.0f, width - 20.0f, 20.0f, "");
        addElement(inputField);

        // Bouton pour envoyer le message
        sendButton = new GuiButton(
                width - 30.0f,
                height - 30.0f,
                20.0f,
                20.0f,
                ">"
        );
        sendButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        sendButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        sendButton.setOnClick(button -> sendMessage());
        addElement(sendButton);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Mettre à jour les messages de chat
        for (int i = chatMessages.size() - 1; i >= 0; i--) {
            ChatMessage message = chatMessages.get(i);
            message.update(deltaTime);

            // Supprimer les messages invisibles
            if (!message.isVisible()) {
                chatMessages.remove(i);
            }
        }
    }

    @Override
    public void render() {
        if (!isVisible) {
            return;
        }

        // Dessiner le fond du chat
        drawChatBackground();

        // Dessiner les messages de chat
        drawChatMessages();

        // Dessiner la zone de saisie si le chat est ouvert
        if (isOpen) {
            drawInputArea();
        }

        // Dessiner les éléments de la GUI
        super.render();
    }

    /**
     * Dessine le fond du chat.
     */
    private void drawChatBackground() {
        float chatWidth = width / 2.0f;
        float chatHeight = Math.min(maxChatLines * 20.0f, height / 2.0f);
        float chatX = 10.0f;
        float chatY = height - chatHeight - 10.0f;

        GL11.glColor4f(
                chatBackgroundColor.x,
                chatBackgroundColor.y,
                chatBackgroundColor.z,
                chatBackgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(chatX, chatY);
        GL11.glVertex2f(chatX + chatWidth, chatY);
        GL11.glVertex2f(chatX + chatWidth, chatY + chatHeight);
        GL11.glVertex2f(chatX, chatY + chatHeight);
        GL11.glEnd();
    }

    /**
     * Dessine les messages de chat.
     */
    private void drawChatMessages() {
        if (fontRenderer == null) {
            return;
        }

        float chatWidth = width / 2.0f;
        float chatX = 15.0f;
        float chatY = height - 20.0f;

        for (int i = chatMessages.size() - 1; i >= 0; i--) {
            ChatMessage message = chatMessages.get(i);
            if (message.isVisible()) {
                float alpha = message.getAlpha();
                Vector4f color = new Vector4f(
                        message.getColor().x,
                        message.getColor().y,
                        message.getColor().z,
                        alpha
                );
                fontRenderer.setTextColor(color);
                fontRenderer.renderText(message.getText(), chatX, chatY);
                chatY -= 20.0f;
            }
        }
    }

    /**
     * Dessine la zone de saisie.
     */
    private void drawInputArea() {
        float inputWidth = width - 40.0f;
        float inputHeight = 20.0f;
        float inputX = 10.0f;
        float inputY = height - inputHeight - 10.0f;

        // Dessiner le fond de la zone de saisie
        GL11.glColor4f(
                inputBackgroundColor.x,
                inputBackgroundColor.y,
                inputBackgroundColor.z,
                inputBackgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(inputX, inputY);
        GL11.glVertex2f(inputX + inputWidth, inputY);
        GL11.glVertex2f(inputX + inputWidth, inputY + inputHeight);
        GL11.glVertex2f(inputX, inputY + inputHeight);
        GL11.glEnd();

        // Dessiner le texte de saisie
        if (fontRenderer != null) {
            fontRenderer.setTextColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
            fontRenderer.renderText("> " + inputMessage, inputX + 5.0f, inputY + 5.0f);
        }
    }

    /**
     * Ajoute un message de chat.
     * @param message Texte du message.
     * @param color Couleur du message.
     */
    public void addChatMessage(String message, Vector4f color) {
        chatMessages.add(new ChatMessage(message, messageDisplayTime, color));

        // Limiter le nombre de messages
        if (chatMessages.size() > maxChatLines) {
            chatMessages.remove(0);
        }
    }

    /**
     * Ajoute un message de chat avec la couleur par défaut.
     * @param message Texte du message.
     */
    public void addChatMessage(String message) {
        addChatMessage(message, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    /**
     * Ajoute un message à l'historique.
     * @param message Message à ajouter.
     */
    public void addToHistory(String message) {
        messageHistory.add(message);
        historyIndex = -1;
    }

    /**
     * Envoye le message actuel.
     */
    private void sendMessage() {
        if (inputMessage.isEmpty()) {
            return;
        }

        // Ajouter le message à l'historique
        addToHistory(inputMessage);

        // Ajouter le message au chat
        addChatMessage("[" + valtherion.getPlayer().getClass().getSimpleName() + "]: " + inputMessage);

        // Envoyer le message au serveur (si en mode multijoueur)
        if (valtherion.getGameClient() != null && valtherion.getGameClient().isConnected()) {
            valtherion.getGameClient().sendChatMessage(inputMessage);
        }

        // Réinitialiser le message de saisie
        inputMessage = "";
        inputField.setText("");
        suggestionIndex = -1;
    }

    /**
     * Ouvre le chat.
     */
    public void openChat() {
        isOpen = true;
        isActive = true;
    }

    /**
     * Ferme le chat.
     */
    public void closeChat() {
        isOpen = false;
        isActive = false;
        inputMessage = "";
        inputField.setText("");
        historyIndex = -1;
        suggestionIndex = -1;
    }

    /**
     * Bascule l'état du chat (ouvert/fermé).
     */
    public void toggleChat() {
        if (isOpen) {
            closeChat();
        } else {
            openChat();
        }
    }

    /**
     * Gère les entrées clavier pour le chat.
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
                    // Fermer le chat
                    closeChat();
                    break;
                case GLFW.GLFW_KEY_ENTER:
                    // Envoyer le message
                    sendMessage();
                    break;
                case GLFW.GLFW_KEY_BACKSPACE:
                    // Supprimer le dernier caractère
                    if (!inputMessage.isEmpty()) {
                        inputMessage = inputMessage.substring(0, inputMessage.length() - 1);
                        inputField.setText(inputMessage);
                    }
                    break;
                case GLFW.GLFW_KEY_UP:
                    // Naviguer dans l'historique (message précédent)
                    navigateHistory(-1);
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    // Naviguer dans l'historique (message suivant)
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
     * Gère les entrées de caractères pour le chat.
     * @param character Caractère saisi.
     */
    public void handleCharInput(char character) {
        if (!isOpen || !isActive) {
            return;
        }

        inputMessage += character;
        inputField.setText(inputMessage);
        updateSuggestions();
    }

    /**
     * Navigue dans l'historique des messages.
     * @param direction Direction (-1 pour le message précédent, 1 pour le message suivant).
     */
    private void navigateHistory(int direction) {
        if (messageHistory.isEmpty()) {
            return;
        }

        if (historyIndex == -1) {
            // Sauvegarder le message actuel
            String currentMessage = inputMessage;
            historyIndex = messageHistory.size() - 1;
            inputMessage = messageHistory.get(historyIndex);
            inputField.setText(inputMessage);
        } else {
            historyIndex += direction;
            historyIndex = Math.max(0, Math.min(historyIndex, messageHistory.size() - 1));
            inputMessage = messageHistory.get(historyIndex);
            inputField.setText(inputMessage);
        }
    }

    /**
     * Navigue dans les suggestions de commandes.
     * @param direction Direction (1 pour la suggestion suivante).
     */
    private void navigateSuggestions(int direction) {
        if (commandSuggestions.isEmpty() || !inputMessage.startsWith("/")) {
            return;
        }

        suggestionIndex = (suggestionIndex + direction) % commandSuggestions.size();
        inputMessage = commandSuggestions.get(suggestionIndex);
        inputField.setText(inputMessage);
    }

    /**
     * Met à jour les suggestions de commandes.
     */
    private void updateSuggestions() {
        if (inputMessage.startsWith("/")) {
            String input = inputMessage.substring(1).toLowerCase();
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

    // --- Getters et Setters ---

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
        inputField.setText(inputMessage);
    }

    public List<String> getMessageHistory() {
        return messageHistory;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Vector4f getChatBackgroundColor() {
        return chatBackgroundColor;
    }

    public void setChatBackgroundColor(Vector4f chatBackgroundColor) {
        this.chatBackgroundColor = chatBackgroundColor;
    }

    public Vector4f getInputBackgroundColor() {
        return inputBackgroundColor;
    }

    public void setInputBackgroundColor(Vector4f inputBackgroundColor) {
        this.inputBackgroundColor = inputBackgroundColor;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public int getMaxChatLines() {
        return maxChatLines;
    }

    public void setMaxChatLines(int maxChatLines) {
        this.maxChatLines = maxChatLines;
    }

    public float getMessageDisplayTime() {
        return messageDisplayTime;
    }

    public void setMessageDisplayTime(float messageDisplayTime) {
        this.messageDisplayTime = messageDisplayTime;
    }

    /**
     * Nettoie les ressources de l'interface de chat.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (fontRenderer != null) {
            fontRenderer.cleanup();
        }
        chatMessages.clear();
        messageHistory.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface de chat.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiNewChat{" +
                "chatMessages=" + chatMessages.size() +
                ", inputMessage='" + inputMessage + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }

    // --- Classe interne pour les messages de chat ---

    /**
     * Classe pour représenter un message de chat.
     */
    public static class ChatMessage {
        private String text;
        private float displayTime;
        private Vector4f color;
        private float alpha;

        public ChatMessage(String text, float displayTime, Vector4f color) {
            this.text = text;
            this.displayTime = displayTime;
            this.color = color;
            this.alpha = 1.0f;
        }

        /**
         * Met à jour le message.
         * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
         */
        public void update(float deltaTime) {
            displayTime -= deltaTime;
            alpha = Math.min(1.0f, displayTime / 2.0f);
        }

        // Getters et Setters
        public String getText() { return text; }
        public float getDisplayTime() { return displayTime; }
        public Vector4f getColor() { return color; }
        public float getAlpha() { return alpha; }
        public boolean isVisible() { return displayTime > 0.0f; }
    }

    // --- Classe interne pour le champ de texte ---

    /**
     * Classe pour les champs de texte de la GUI.
     */
    public class GuiTextField extends GuiElement {
        private String text;
        private boolean isSelected;
        private Vector4f textColor;
        private Vector4f backgroundColor;

        public GuiTextField(float x, float y, float width, float height, String text) {
            super(x, y, width, height);
            this.text = text;
            this.isSelected = false;
            this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.backgroundColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.8f);
        }

        @Override
        public void render() {
            // Dessiner le fond du champ
            GL11.glColor4f(
                    backgroundColor.x,
                    backgroundColor.y,
                    backgroundColor.z,
                    backgroundColor.w
            );
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();

            // Dessiner le texte (simplifié)
            GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x + 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 15.0f);
            GL11.glVertex2f(position.x + 5.0f, position.y + 15.0f);
            GL11.glEnd();
        }

        // Getters et Setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { this.isSelected = selected; }
    }
}
