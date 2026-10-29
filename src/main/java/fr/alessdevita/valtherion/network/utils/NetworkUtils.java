package fr.alessdevita.valtherion.network.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.*;

/**
 * Utilitaires réseau pour Valtherion.
 * Fournit des fonctions pour la validation, la compression, le chiffrement, etc.
 */
public class NetworkUtils {

    // Clé secrète pour le chiffrement (à remplacer par une clé sécurisée en production)
    private static final String SECRET_KEY = "ValtherionSecretKey1234567890";

    // Algorithme de chiffrement
    private static final String ALGORITHM = "AES";

    /**
     * Valide une adresse IP.
     * @param ipAddress Adresse IP à valider.
     * @return true si l'adresse IP est valide, false sinon.
     */
    public static boolean isValidIpAddress(String ipAddress) {
        try {
            if (ipAddress == null || ipAddress.isEmpty()) {
                return false;
            }

            // Vérifier si l'adresse IP est valide
            String[] parts = ipAddress.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String part : parts) {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valide un port.
     * @param port Port à valider.
     * @return true si le port est valide, false sinon.
     */
    public static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }

    /**
     * Vérifie si une adresse IP est locale.
     * @param ipAddress Adresse IP à vérifier.
     * @return true si l'adresse IP est locale, false sinon.
     */
    public static boolean isLocalIpAddress(String ipAddress) {
        if (!isValidIpAddress(ipAddress)) {
            return false;
        }

        // Vérifier si l'adresse IP est dans la plage locale
        String[] localRanges = {
                "127.0.0.0/8",    // localhost
                "10.0.0.0/8",     // Réseau privé
                "172.16.0.0/12",  // Réseau privé
                "192.168.0.0/16"  // Réseau privé
        };

        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            byte[] bytes = address.getAddress();

            for (String range : localRanges) {
                String[] parts = range.split("/");
                InetAddress networkAddress = InetAddress.getByName(parts[0]);
                byte[] networkBytes = networkAddress.getAddress();
                int prefixLength = Integer.parseInt(parts[1]);

                boolean match = true;
                for (int i = 0; i < prefixLength / 8; i++) {
                    if (bytes[i] != networkBytes[i]) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return true;
                }
            }
        } catch (UnknownHostException e) {
            return false;
        }

        return false;
    }

    /**
     * Compresse des données avec GZIP.
     * @param data Données à compresser.
     * @return Données compressées.
     * @throws IOException Si une erreur se produit lors de la compression.
     */
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data);
        }
        return byteStream.toByteArray();
    }

    /**
     * Décompresse des données avec GZIP.
     * @param compressedData Données compressées.
     * @return Données décompressées.
     * @throws IOException Si une erreur se produit lors de la décompression.
     */
    public static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        }
    }

    /**
     * Chiffre des données avec AES.
     * @param data Données à chiffrer.
     * @return Données chiffrées.
     * @throws Exception Si une erreur se produit lors du chiffrement.
     */
    public static byte[] encrypt(byte[] data) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * Déchiffre des données avec AES.
     * @param encryptedData Données chiffrées.
     * @return Données déchiffrées.
     * @throws Exception Si une erreur se produit lors du déchiffrement.
     */
    public static byte[] decrypt(byte[] encryptedData) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    /**
     * Génère une clé de chiffrement à partir de la clé secrète.
     * @return Clé de chiffrement.
     * @throws Exception Si une erreur se produit lors de la génération de la clé.
     */
    private static SecretKeySpec generateKey() throws Exception {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16); // AES utilise des clés de 16, 24 ou 32 octets
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Génère un ID unique pour une connexion.
     * @return ID unique.
     */
    public static String generateConnectionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Vérifie si un hôte est accessible.
     * @param host Hôte à vérifier.
     * @param port Port à vérifier.
     * @param timeout Timeout en millisecondes.
     * @return true si l'hôte est accessible, false sinon.
     */
    public static boolean isHostReachable(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Récupère l'adresse IP locale.
     * @return Adresse IP locale.
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    /**
     * Récupère le nom de l'hôte local.
     * @return Nom de l'hôte local.
     */
    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * Convertit une adresse IP en entier.
     * @param ipAddress Adresse IP à convertir.
     * @return Entier représentant l'adresse IP.
     */
    public static int ipToInt(String ipAddress) {
        if (!isValidIpAddress(ipAddress)) {
            return 0;
        }

        String[] parts = ipAddress.split("\\.");
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) | Integer.parseInt(parts[i]);
        }
        return result;
    }

    /**
     * Convertit un entier en adresse IP.
     * @param ipInt Entier représentant l'adresse IP.
     * @return Adresse IP.
     */
    public static String intToIp(int ipInt) {
        return ((ipInt >> 24) & 0xFF) + "." +
                ((ipInt >> 16) & 0xFF) + "." +
                ((ipInt >> 8) & 0xFF) + "." +
                (ipInt & 0xFF);
    }
}