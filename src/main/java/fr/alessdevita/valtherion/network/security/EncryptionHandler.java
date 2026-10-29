package fr.alessdevita.valtherion.network.security;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.core.PacketRegistry;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.network.utils.EncryptionUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire de chiffrement pour Valtherion.
 * Gère le chiffrement et le déchiffrement des paquets pour chaque connexion.
 */
public class EncryptionHandler {

    // Instance unique pour le singleton
    private static EncryptionHandler instance;

    // Utilitaires de chiffrement
    private final EncryptionUtils encryptionUtils;

    // Map des connexions vers leurs clés de chiffrement symétrique
    private final Map<Connection, byte[]> connectionKeys;

    // Clé symétrique par défaut (pour les connexions non enregistrées)
    private byte[] defaultKey;

    /**
     * Constructeur privé pour le singleton.
     */
    private EncryptionHandler() {
        this.encryptionUtils = EncryptionUtils.getInstance();
        this.connectionKeys = new ConcurrentHashMap<>();
        try {
            this.defaultKey = encryptionUtils.getSymmetricKey().getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'initialiser EncryptionHandler : " + e.getMessage(), e);
        }
    }

    /**
     * Récupère l'instance unique de EncryptionHandler.
     * @return Instance unique.
     */
    public static EncryptionHandler getInstance() {
        if (instance == null) {
            instance = new EncryptionHandler();
        }
        return instance;
    }

    /**
     * Enregistre une clé de chiffrement pour une connexion.
     * @param connection Connexion.
     * @param key Clé de chiffrement.
     */
    public void registerKey(Connection connection, byte[] key) {
        connectionKeys.put(connection, key);
    }

    /**
     * Supprime la clé de chiffrement pour une connexion.
     * @param connection Connexion.
     */
    public void unregisterKey(Connection connection) {
        connectionKeys.remove(connection);
    }

    /**
     * Chiffre un paquet pour une connexion.
     * @param packet Paquet à chiffrer.
     * @param connection Connexion.
     * @return Paquet chiffré.
     * @throws NetworkException Si une erreur se produit lors du chiffrement.
     */
    public Packet encryptPacket(Packet packet, Connection connection) throws NetworkException {
        try {
            byte[] key = connectionKeys.getOrDefault(connection, defaultKey);
            byte[] packetData = packet.serialize();
            byte[] encryptedData = encryptData(packetData, key);

            // Créer un nouveau paquet avec les données chiffrées
            EncryptedPacket encryptedPacket = new EncryptedPacket(packet.getPacketId(), encryptedData);
            return encryptedPacket;
        } catch (Exception e) {
            throw new NetworkException("Impossible de chiffrer le paquet : " + e.getMessage(), e);
        }
    }

    /**
     * Déchiffre un paquet pour une connexion.
     * @param encryptedPacket Paquet chiffré.
     * @param connection Connexion.
     * @return Paquet déchiffré.
     * @throws NetworkException Si une erreur se produit lors du déchiffrement.
     */
    public Packet decryptPacket(EncryptedPacket encryptedPacket, Connection connection) throws NetworkException {
        try {
            byte[] key = connectionKeys.getOrDefault(connection, defaultKey);
            byte[] decryptedData = decryptData(encryptedPacket.getEncryptedData(), key);

            // Créer un paquet à partir des données déchiffrées
            Packet packet = PacketRegistry.createPacket(encryptedPacket.getPacketId());
            if (packet != null) {
                packet.deserialize(decryptedData);
                return packet;
            }
            throw new NetworkException("Impossible de créer le paquet avec l'ID : " + encryptedPacket.getPacketId());
        } catch (Exception e) {
            throw new NetworkException("Impossible de déchiffrer le paquet : " + e.getMessage(), e);
        }
    }

    /**
     * Chiffre des données avec une clé.
     * @param data Données à chiffrer.
     * @param key Clé de chiffrement.
     * @return Données chiffrées.
     * @throws Exception Si une erreur se produit lors du chiffrement.
     */
    private byte[] encryptData(byte[] data, byte[] key) throws Exception {
        // Utiliser AES pour le chiffrement
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * Déchiffre des données avec une clé.
     * @param encryptedData Données chiffrées.
     * @param key Clé de chiffrement.
     * @return Données déchiffrées.
     * @throws Exception Si une erreur se produit lors du déchiffrement.
     */
    private byte[] decryptData(byte[] encryptedData, byte[] key) throws Exception {
        // Utiliser AES pour le déchiffrement
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * Génère une clé de chiffrement aléatoire.
     * @return Clé de chiffrement.
     */
    public byte[] generateRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16]; // 128 bits pour AES
        random.nextBytes(key);
        return key;
    }

    // --- Classe interne pour les paquets chiffrés ---

    /**
     * Paquet chiffré pour encapsuler les données chiffrées.
     */
    public static class EncryptedPacket extends Packet {

        // Données chiffrées
        private byte[] encryptedData;

        /**
         * Constructeur du paquet chiffré.
         * @param packetId ID du paquet.
         * @param encryptedData Données chiffrées.
         */
        public EncryptedPacket(int packetId, byte[] encryptedData) {
            super(packetId);
            this.encryptedData = encryptedData;
        }

        @Override
        protected void writeData(java.io.DataOutputStream dataStream) throws java.io.IOException {
            dataStream.writeInt(encryptedData.length);
            dataStream.write(encryptedData);
        }

        @Override
        protected void readData(java.io.DataInputStream dataStream) throws java.io.IOException {
            int length = dataStream.readInt();
            encryptedData = new byte[length];
            dataStream.readFully(encryptedData);
        }

        @Override
        public void handleClient(Connection connection) {
            // Le client ne traite pas directement les paquets chiffrés
        }

        @Override
        public void handleServer(Connection connection) {
            // Le serveur ne traite pas directement les paquets chiffrés
        }

        // --- Getter ---

        public byte[] getEncryptedData() {
            return encryptedData;
        }
    }
}