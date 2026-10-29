package fr.alessdevita.valtherion.client;

import java.io.*;
import java.util.*;

/**
 * Classe pour récupérer des informations sur le client Valtherion.
 * Permet de récupérer le nom, la version, et le type du client.
 */
public class ClientBrandRetriever {

    // Nom du client
    private static String clientBrand = "Valtherion";

    // Version du client
    private static String clientVersion = "1.0.0";

    // Type du client (VANILLA, MODDED, DEV)
    private static ClientType clientType = ClientType.VANILLA;

    // Énumération des types de client
    public enum ClientType {
        VANILLA,   // Client standard
        MODDED,    // Client avec des mods
        DEV        // Client de développement
    }

    /**
     * Récupère le nom du client.
     * @return Nom du client.
     */
    public static String getClientBrand() {
        return clientBrand;
    }

    public static String getClientModName() { return clientBrand; }

    /**
     * Définit le nom du client.
     * @param brand Nom du client.
     */
    public static void setClientBrand(String brand) {
        clientBrand = brand;
    }

    /**
     * Récupère la version du client.
     * @return Version du client.
     */
    public static String getClientVersion() {
        return clientVersion;
    }

    /**
     * Définit la version du client.
     * @param version Version du client.
     */
    public static void setClientVersion(String version) {
        clientVersion = version;
    }

    /**
     * Récupère le type du client.
     * @return Type du client.
     */
    public static ClientType getClientType() {
        return clientType;
    }

    /**
     * Définit le type du client.
     * @param type Type du client.
     */
    public static void setClientType(ClientType type) {
        clientType = type;
    }

    /**
     * Récupère une chaîne décrivant le client.
     * @return Chaîne décrivant le client.
     */
    public static String getClientDescription() {
        return clientBrand + " " + clientVersion + " (" + clientType.name().toLowerCase() + ")";
    }

    /**
     * Charge les informations du client depuis un fichier.
     * @param filePath Chemin du fichier.
     */
    public static void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("brand:")) {
                    clientBrand = line.substring("brand:".length()).trim();
                } else if (line.startsWith("version:")) {
                    clientVersion = line.substring("version:".length()).trim();
                } else if (line.startsWith("type:")) {
                    String type = line.substring("type:".length()).trim().toUpperCase();
                    try {
                        clientType = ClientType.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Type de client invalide : " + type);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de charger les informations du client depuis : " + filePath);
        }
    }

    /**
     * Sauvegarde les informations du client dans un fichier.
     * @param filePath Chemin du fichier.
     */
    public static void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("brand: " + clientBrand + "\n");
            writer.write("version: " + clientVersion + "\n");
            writer.write("type: " + clientType.name().toLowerCase() + "\n");
        } catch (IOException e) {
            System.err.println("Impossible de sauvegarder les informations du client dans : " + filePath);
        }
    }
}
