package fr.alessdevita.valtherion.network.core;

import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.network.utils.NetworkUtils;

import java.io.*;

/**
 * Classe de base pour les paquets réseau dans Valtherion.
 * Définit la structure et les méthodes de base pour sérialiser/désérialiser les paquets.
 * Utilise un système de sérialisation optimisé pour réduire la taille des paquets.
 */
public abstract class Packet {

    // ID du paquet (pour identifier le type de paquet)
    private final int packetId;

    // Timestamp du paquet (pour mesurer la latence)
    private long timestamp;

    // Priorité du paquet (0 = basse, 1 = normale, 2 = haute)
    private int priority;

    // Indique si le paquet doit être compressé
    private boolean isCompressed;

    // Indique si le paquet doit être chiffré
    private boolean isEncrypted;

    /**
     * Constructeur du paquet.
     * @param packetId ID du paquet.
     */
    public Packet(int packetId) {
        this.packetId = packetId;
        this.timestamp = System.currentTimeMillis();
        this.priority = 1; // Priorité normale par défaut
        this.isCompressed = false;
        this.isEncrypted = false;
    }

    public Packet(String playerName, boolean b, String connexionAcceptee, int packetId) {
        this.packetId = packetId;
        this.timestamp = System.currentTimeMillis();
        this.priority = 1;
        this.isCompressed = false;
        this.isEncrypted = false;
    }

    public Packet(String playerName, boolean b, String connexionAcceptee) {
        this(playerName, b, connexionAcceptee, 0);
    }

    public Packet() {
        this.packetId = 0;
        this.timestamp = System.currentTimeMillis();
        this.priority = 1;
        this.isCompressed = false;
        this.isEncrypted = false;
    }

    /**
     * Sérialise le paquet en bytes.
     * @return Tableau de bytes représentant le paquet.
     * @throws NetworkException Si une erreur se produit lors de la sérialisation.
     */
    public byte[] serialize() throws NetworkException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            // Écrire l'ID du paquet
            dataStream.writeInt(packetId);

            // Écrire le timestamp
            dataStream.writeLong(timestamp);

            // Écrire la priorité
            dataStream.writeByte(priority);

            // Écrire les flags (compression, chiffrement)
            byte flags = 0;
            if (isCompressed) {
                flags |= 0x01;
            }
            if (isEncrypted) {
                flags |= 0x02;
            }
            dataStream.writeByte(flags);

            // Écrire les données spécifiques du paquet
            writeData(dataStream);

            dataStream.close();

            // Appliquer la compression si nécessaire
            byte[] packetData = byteStream.toByteArray();
            if (isCompressed) {
                packetData = NetworkUtils.compress(packetData);
            }

            // Appliquer le chiffrement si nécessaire
            if (isEncrypted) {
                packetData = NetworkUtils.encrypt(packetData);
            }

            return packetData;
        } catch (IOException e) {
            throw new NetworkException("Impossible de sérialiser le paquet : " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Désérialise le paquet à partir de bytes.
     * @param data Tableau de bytes représentant le paquet.
     * @throws NetworkException Si une erreur se produit lors de la désérialisation.
     */
    public void deserialize(byte[] data) throws NetworkException {
        try {
            // Appliquer le déchiffrement si nécessaire
            if (isEncrypted) {
                data = NetworkUtils.decrypt(data);
            }

            // Appliquer la décompression si nécessaire
            if (isCompressed) {
                data = NetworkUtils.decompress(data);
            }

            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            DataInputStream dataStream = new DataInputStream(byteStream);

            // Lire le timestamp (l'ID est déjà lu par le PacketHandler)
            this.timestamp = dataStream.readLong();

            // Lire la priorité
            this.priority = dataStream.readByte();

            // Lire les flags
            byte flags = dataStream.readByte();
            this.isCompressed = (flags & 0x01) != 0;
            this.isEncrypted = (flags & 0x02) != 0;

            // Lire les données spécifiques du paquet
            readData(dataStream);

            dataStream.close();
        } catch (Exception e) {
            throw new NetworkException("Impossible de désérialiser le paquet : " + e.getMessage(), e);
        }
    }

    /**
     * Écrit les données spécifiques du paquet.
     * @param dataStream Flux de sortie pour écrire les données.
     * @throws IOException Si une erreur se produit lors de l'écriture.
     */
    protected abstract void writeData(DataOutputStream dataStream) throws IOException;

    /**
     * Lit les données spécifiques du paquet.
     * @param dataStream Flux d'entrée pour lire les données.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    protected abstract void readData(DataInputStream dataStream) throws IOException;

    /**
     * Traite le paquet côté client.
     * @param connection Connexion associée.
     */
    public abstract void handleClient(Connection connection);

    /**
     * Traite le paquet côté serveur.
     * @param connection Connexion associée.
     */
    public abstract void handleServer(Connection connection);

    // --- Getters et Setters ---

    public int getPacketId() {
        return packetId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = Math.max(0, Math.min(2, priority));
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setCompressed(boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    /**
     * Retourne une représentation textuelle du paquet.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Packet{packetId=" + packetId +
                ", timestamp=" + timestamp +
                ", priority=" + priority +
                ", isCompressed=" + isCompressed +
                ", isEncrypted=" + isEncrypted +
                '}';
    }
}
