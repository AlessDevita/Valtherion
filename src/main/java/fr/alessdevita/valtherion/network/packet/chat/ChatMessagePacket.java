package fr.alessdevita.valtherion.network.packet.chat;

import fr.alessdevita.valtherion.network.core.NetworkManager;
import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de message de chat dans Valtherion.
 * Utilisé pour envoyer des messages de chat entre les joueurs ou le serveur.
 */
public class ChatMessagePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 5001;

    // Nom de l'expéditeur
    private String senderName;

    // Contenu du message
    private String message;

    // Type de message (CHAT, WHISPER, SYSTEM)
    private MessageType messageType;

    // Canal de chat (ex: GLOBAL, LOCAL, GUILD)
    private String chatChannel;

    // ID du destinataire (pour les messages privés)
    private String recipientId;

    // Énumération des types de message
    public enum MessageType {
        CHAT,      // Message de chat normal
        WHISPER,   // Message privé (chuchotement)
        SYSTEM     // Message système
    }

    /**
     * Constructeur du paquet de message de chat.
     */
    public ChatMessagePacket() {
        super(PACKET_ID);
        this.messageType = MessageType.CHAT;
        this.chatChannel = "GLOBAL";
    }

    /**
     * Constructeur du paquet de message de chat avec un expéditeur et un message.
     * @param senderName Nom de l'expéditeur.
     * @param message Contenu du message.
     */
    public ChatMessagePacket(String senderName, String message) {
        this();
        this.senderName = senderName;
        this.message = message;
    }

    /**
     * Constructeur du paquet de message de chat avec un expéditeur, un message et un type.
     * @param senderName Nom de l'expéditeur.
     * @param message Contenu du message.
     * @param messageType Type de message.
     */
    public ChatMessagePacket(String senderName, String message, MessageType messageType) {
        this(senderName, message);
        this.messageType = messageType;
    }

    /**
     * Constructeur du paquet de message de chat avec un expéditeur, un message, un type et un canal.
     * @param senderName Nom de l'expéditeur.
     * @param message Contenu du message.
     * @param messageType Type de message.
     * @param chatChannel Canal de chat.
     */
    public ChatMessagePacket(String senderName, String message, MessageType messageType, String chatChannel) {
        this(senderName, message, messageType);
        this.chatChannel = chatChannel;
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire le nom de l'expéditeur
        dataStream.writeUTF(senderName != null ? senderName : "");

        // Écrire le contenu du message
        dataStream.writeUTF(message != null ? message : "");

        // Écrire le type de message
        dataStream.writeByte(messageType.ordinal());

        // Écrire le canal de chat
        dataStream.writeUTF(chatChannel != null ? chatChannel : "");

        // Écrire l'ID du destinataire (null si message public)
        dataStream.writeBoolean(recipientId != null);
        if (recipientId != null) {
            dataStream.writeUTF(recipientId);
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire le nom de l'expéditeur
        this.senderName = dataStream.readUTF();

        // Lire le contenu du message
        this.message = dataStream.readUTF();

        // Lire le type de message
        this.messageType = MessageType.values()[dataStream.readByte()];

        // Lire le canal de chat
        this.chatChannel = dataStream.readUTF();

        // Lire l'ID du destinataire
        if (dataStream.readBoolean()) {
            this.recipientId = dataStream.readUTF();
        }
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite le message de chat
        switch (messageType) {
            case CHAT:
                System.out.println("[" + chatChannel + "] " + senderName + ": " + message);
                break;
            case WHISPER:
                System.out.println("[Chuchotement] " + senderName + " -> Toi: " + message);
                break;
            case SYSTEM:
                System.out.println("[Système] " + message);
                break;
        }

        // Ici, tu peux afficher le message dans le chat du jeu
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite le message de chat
        System.out.println("Message de chat reçu de " + senderName + " : " + message);

        // Rediffuser le message aux autres clients en fonction du type et du canal
        try {
            if (messageType == MessageType.WHISPER && recipientId != null) {
                // Envoyer le message uniquement au destinataire
                for (Connection clientConnection : NetworkManager.getInstance().getServerHandler().getConnections()) {
                    // Ici, tu peux vérifier si clientConnection correspond au destinataire
                    // Exemple : if (getPlayerId(clientConnection).equals(recipientId)) {
                    //     clientConnection.sendPacket(this);
                    // }
                }
            } else {
                // Rediffuser le message à tous les clients du canal
                NetworkManager.getInstance().sendPacket(this);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion du message de chat : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * Retourne une représentation textuelle du paquet de message de chat.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ChatMessagePacket{" +
                "senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                ", chatChannel='" + chatChannel + '\'' +
                '}';
    }
}