package fr.alessdevita.valtherion.client.stream;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contrôleur de chat : gère la connexion au chat du service de streaming
 * et la réception / envoi des messages.
 */
public final class ChatController {
    private final List<ChatMessage> messages = new CopyOnWriteArrayList<>();
    private final List<ChatListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean connected;
    private volatile String channel;

    /**
     * Représente un message de chat reçu.
     */
    public static final class ChatMessage {
        private final String author;
        private final String content;
        private final long timestamp;
        private final String badge;

        public ChatMessage(String author, String content) {
            this(author, content, null);
        }

        public ChatMessage(String author, String content, String badge) {
            this.author = author == null ? "" : author;
            this.content = content == null ? "" : content;
            this.timestamp = System.currentTimeMillis();
            this.badge = badge;
        }

        public String getAuthor() { return this.author; }
        public String getContent() { return this.content; }
        public long getTimestamp() { return this.timestamp; }
        public String getBadge() { return this.badge; }

        @Override
        public String toString() {
            return "[" + this.author + "] " + this.content;
        }
    }

    /** Listener appelé lors de la réception d'un message. */
    @FunctionalInterface
    public interface ChatListener {
        void onMessage(ChatMessage message);
    }

    /**
     * Connecte au chat de la chaîne donnée.
     *
     * @param channel le nom de la chaîne
     */
    public void connect(String channel) {
        if (channel == null || channel.isBlank()) return;
        this.channel = channel;
        this.connected = true;
        // La connexion IRC/WebSocket serait branchée ici.
    }

    /** Déconnecte du chat. */
    public void disconnect() {
        this.connected = false;
    }

    /** {@code true} si connecté au chat. */
    public boolean isConnected() {
        return this.connected;
    }

    /** @return le nom de la chaîne connectée. */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Reçoit un message du service de chat.
     * (appelé par la couche réseau)
     *
     * @param author   l'auteur du message
     * @param content  le contenu
     * @param badge    le badge (modérateur, sub, etc.), ou {@code null}
     */
    public void receiveMessage(String author, String content, String badge) {
        ChatMessage message = new ChatMessage(author, content, badge);
        this.messages.add(message);
        for (ChatListener listener : this.listeners) {
            try {
                listener.onMessage(message);
            } catch (RuntimeException e) {
                System.err.println("Erreur listener chat: " + e.getMessage());
            }
        }
    }

    /**
     * Envoie un message au chat.
     *
     * @param content le contenu à envoyer
     */
    public void sendMessage(String content) {
        if (!this.connected || content == null || content.isBlank()) return;
        // L'envoi via IRC/WebSocket serait branché ici.
    }

    /**
     * Ajoute un listener de messages.
     *
     * @param listener le listener
     */
    public void addListener(ChatListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    /** Retire un listener. */
    public void removeListener(ChatListener listener) {
        this.listeners.remove(listener);
    }

    /** @return tous les messages reçus. */
    public List<ChatMessage> getMessages() {
        return java.util.Collections.unmodifiableList(this.messages);
    }

    /** Efface l'historique des messages. */
    public void clearMessages() {
        this.messages.clear();
    }

    /** Libère les ressources. */
    public void shutdown() {
        disconnect();
        this.listeners.clear();
        this.messages.clear();
    }

    @Override
    public String toString() {
        return "ChatController[channel=" + this.channel
                + ", connected=" + this.connected
                + ", messages=" + this.messages.size() + "]";
    }
}
