package fr.alessdevita.valtherion.network.utils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.*;

/**
 * Utilitaires de chiffrement pour Valtherion.
 * Fournit des méthodes pour chiffrer, déchiffrer et hacher des données.
 */
public class EncryptionUtils {

    // Algorithme de chiffrement symétrique
    private static final String SYMMETRIC_ALGORITHM = "AES";

    // Algorithme de hachage
    private static final String HASH_ALGORITHM = "SHA-256";

    // Taille de la clé AES (128, 192 ou 256 bits)
    private static final int AES_KEY_SIZE = 256;

    // Clé secrète par défaut (à remplacer par une clé sécurisée en production)
    private static final String DEFAULT_SECRET_KEY = "ValtherionSecretKey123456789012345678901234";

    // Instance unique pour le singleton
    private static EncryptionUtils instance;

    // Clé secrète pour le chiffrement symétrique
    private SecretKey symmetricKey;

    // Paire de clés pour le chiffrement asymétrique (RSA)
    private KeyPair keyPair;

    /**
     * Constructeur privé pour le singleton.
     */
    private EncryptionUtils() {
        try {
            // Générer une clé AES à partir de la clé secrète
            this.symmetricKey = generateAesKey(DEFAULT_SECRET_KEY);

            // Générer une paire de clés RSA
            this.keyPair = generateRsaKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'initialiser EncryptionUtils : " + e.getMessage(), e);
        }
    }

    /**
     * Récupère l'instance unique de EncryptionUtils.
     * @return Instance unique.
     */
    public static EncryptionUtils getInstance() {
        if (instance == null) {
            instance = new EncryptionUtils();
        }
        return instance;
    }

    // --- Méthodes pour le chiffrement symétrique (AES) ---

    /**
     * Génère une clé AES à partir d'une chaîne.
     * @param secretKey Chaîne secrète.
     * @return Clé AES.
     * @throws Exception Si une erreur se produit lors de la génération.
     */
    private SecretKey generateAesKey(String secretKey) throws Exception {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(HASH_ALGORITHM);
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, AES_KEY_SIZE / 8); // Tronquer à 256 bits
        return new SecretKeySpec(keyBytes, SYMMETRIC_ALGORITHM);
    }

    /**
     * Chiffre des données avec AES.
     * @param data Données à chiffrer.
     * @return Données chiffrées.
     * @throws Exception Si une erreur se produit lors du chiffrement.
     */
    public byte[] encryptAes(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        return cipher.doFinal(data);
    }

    /**
     * Déchiffre des données avec AES.
     * @param encryptedData Données chiffrées.
     * @return Données déchiffrées.
     * @throws Exception Si une erreur se produit lors du déchiffrement.
     */
    public byte[] decryptAes(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        return cipher.doFinal(encryptedData);
    }

    // --- Méthodes pour le chiffrement asymétrique (RSA) ---

    /**
     * Génère une paire de clés RSA.
     * @return Paire de clés RSA.
     * @throws NoSuchAlgorithmException Si l'algorithme RSA n'est pas disponible.
     */
    private KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Taille de clé de 2048 bits
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Chiffre des données avec RSA (clé publique).
     * @param data Données à chiffrer.
     * @param publicKey Clé publique.
     * @return Données chiffrées.
     * @throws Exception Si une erreur se produit lors du chiffrement.
     */
    public byte[] encryptRsa(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * Déchiffre des données avec RSA (clé privée).
     * @param encryptedData Données chiffrées.
     * @param privateKey Clé privée.
     * @return Données déchiffrées.
     * @throws Exception Si une erreur se produit lors du déchiffrement.
     */
    public byte[] decryptRsa(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    // --- Méthodes pour le hachage ---

    /**
     * Hache une chaîne avec SHA-256.
     * @param input Chaîne à hacher.
     * @return Chaîne hachée en hexadécimal.
     */
    public String hashSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme de hachage non disponible : " + HASH_ALGORITHM, e);
        }
    }

    /**
     * Hache un mot de passe avec un sel.
     * @param password Mot de passe à hacher.
     * @param salt Sel à utiliser.
     * @return Mot de passe haché en hexadécimal.
     */
    public String hashPasswordWithSalt(String password, String salt) {
        return hashSha256(password + salt);
    }

    /**
     * Génère un sel aléatoire.
     * @return Sel aléatoire.
     */
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 128 bits
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    // --- Méthodes utilitaires ---

    /**
     * Convertit un tableau de bytes en chaîne hexadécimale.
     * @param bytes Tableau de bytes.
     * @return Chaîne hexadécimale.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Convertit une chaîne hexadécimale en tableau de bytes.
     * @param hex Chaîne hexadécimale.
     * @return Tableau de bytes.
     */
    public byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) +
                    Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    // --- Getters ---

    public SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}