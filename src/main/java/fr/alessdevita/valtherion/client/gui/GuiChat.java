package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Classe pour l'interface de chat dans Valtherion.
 * Gère l'affichage des messages, la saisie de nouveaux messages, et l'historique.
 */
public abstract class GuiChat extends Gui {

    // Liste des lignes de chat
    private final List<ChatLine> chatLines;

    // Message en cours de saisie
    private String inputMessage;

    // Historique des messages
    private final List<String> messageHistory;

    // Index de l'historique actuel
    private int historyIndex;

    // Indique si le chat est ouvert
    private boolean isOpen;

    // Couleur du fond du chat
    private Vector4f chatBackgroundColor;

    // Couleur du fond de la zone de saisie
    private Vector4f inputBackgroundColor;

    // Rendu de texte pour le chat
    private FontRenderer fontRenderer;

    // Hauteur maximale du chat (en nombre de lignes)
    private int maxChatLines;

    // Temps d'affichage des lignes de chat (en secondes)
    private float chatLineDisplayTime;

    /**
     * Constructeur de l'interface de chat.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiChat(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.chatLines = new ArrayList<>();
        this.inputMessage = "";
        this.messageHistory = new ArrayList<>();
        this.historyIndex = -1;
        this.isOpen = false;
        this.chatBackgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.7f); // Noir semi-transparent
        this.inputBackgroundColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.9f); // Gris foncé
        this.maxChatLines = 10;
        this.chatLineDisplayTime = 10.0f;
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Mettre à jour les lignes de chat
        for (int i = chatLines.size() - 1; i >= 0; i--) {
            ChatLine chatLine = chatLines.get(i);
            chatLine.update(deltaTime);

            // Supprimer les lignes invisibles
            if (!chatLine.isVisible()) {
                chatLines.remove(i);
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

        // Dessiner les lignes de chat
        drawChatLines();

        // Dessiner la zone de saisie si le chat est ouvert
        if (isOpen) {
            drawInputArea();
        }
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
     * Dessine les lignes de chat.
     */
    private void drawChatLines() {
        if (fontRenderer == null) {
            return;
        }

        float chatWidth = width / 2.0f;
        float chatX = 15.0f;
        float chatY = height - 20.0f;

        for (int i = chatLines.size() - 1; i >= 0; i--) {
            ChatLine chatLine = chatLines.get(i);
            if (chatLine.isVisible()) {
                float alpha = chatLine.getAlpha();
                Vector4f color = new Vector4f(
                        chatLine.getColor().x,
                        chatLine.getColor().y,
                        chatLine.getColor().z,
                        alpha
                );
                fontRenderer.setTextColor(color);
                fontRenderer.renderText(chatLine.getFormattedMessage(), chatX, chatY);
                chatY -= 20.0f;
            }
        }
    }

    /**
     * Dessine la zone de saisie.
     */
    private void drawInputArea() {
        float inputWidth = width / 2.0f - 20.0f;
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
     * Ajoute une ligne de chat.
     * @param message Message.
     * @param sender Expéditeur.
     */
    public void addChatLine(String message, String sender) {
        ChatLine chatLine = new ChatLine(message, sender, chatLineDisplayTime);
        chatLines.add(chatLine);

        // Limiter le nombre de lignes de chat
        if (chatLines.size() > maxChatLines) {
            chatLines.remove(0);
        }
    }

    /**
     * Ajoute un message à l'historique.
     * @param message Message.
     */
    public void addToHistory(String message) {
        messageHistory.add(message);
        historyIndex = -1;
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
        } else {
            historyIndex += direction;
            historyIndex = Math.max(0, Math.min(historyIndex, messageHistory.size() - 1));
            inputMessage = messageHistory.get(historyIndex);
        }
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
        addChatLine(inputMessage, valtherion.getGameClient().getPlayerName());

        // Envoyer le message au serveur (si en mode multijoueur)
        if (valtherion.getGameClient() != null && valtherion.getGameClient().isConnected()) {
            valtherion.getGameClient().sendChatMessage(inputMessage);
        }

        // Réinitialiser le message de saisie
        inputMessage = "";
        historyIndex = -1;
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
        historyIndex = -1;
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

    // --- Getters et Setters ---

    public List<ChatLine> getChatLines() {
        return chatLines;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
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

    public float getChatLineDisplayTime() {
        return chatLineDisplayTime;
    }

    public void setChatLineDisplayTime(float chatLineDisplayTime) {
        this.chatLineDisplayTime = chatLineDisplayTime;
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
        chatLines.clear();
        messageHistory.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface de chat.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiChat{" +
                "chatLines=" + chatLines.size() +
                ", inputMessage='" + inputMessage + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }

    public abstract void initGui();

    protected abstract void actionPerformed(GuiButton button);

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract boolean doesGuiPauseGame();
}
