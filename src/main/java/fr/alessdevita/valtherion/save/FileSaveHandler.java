package fr.alessdevita.valtherion.save;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.save.exceptions.SaveException;
import fr.alessdevita.valtherion.save.SaveHandler;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour gérer les sauvegardes dans des fichiers dans Valtherion.
 * Permet de sauvegarder et charger les données depuis des fichiers JSON.
 */
public class FileSaveHandler implements SaveHandler {

    // Chemin du dossier de sauvegarde
    private String saveDirectory;

    // Sérialiseur utilisé pour convertir les objets en JSON
    private final Serializer serializer;

    /**
     * Constructeur du gestionnaire de sauvegarde dans des fichiers.
     */
    public FileSaveHandler() {
        this.saveDirectory = "saves";
        this.serializer = new Serializer();

        // Créer le dossier de sauvegarde s'il n'existe pas
        createSaveDirectory();
    }

    /**
     * Constructeur du gestionnaire de sauvegarde avec un chemin personnalisé.
     * @param saveDirectory Chemin du dossier de sauvegarde.
     */
    public FileSaveHandler(String saveDirectory) {
        this();
        this.saveDirectory = saveDirectory;
        createSaveDirectory();
    }

    /**
     * Crée le dossier de sauvegarde s'il n'existe pas.
     */
    private void createSaveDirectory() {
        try {
            Path path = Paths.get(saveDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Impossible de créer le dossier de sauvegarde : " + saveDirectory);
            e.printStackTrace();
        }
    }

    @Override
    public void save(SaveData saveData) throws SaveException {
        if (saveData == null) {
            throw new SaveException("Les données de sauvegarde ne peuvent pas être null.");
        }

        String saveName = saveData.getSaveName();
        if (saveName == null || saveName.isEmpty()) {
            saveName = "save_" + saveData.getTimestamp();
        }

        // Convertir les données de sauvegarde en JSON
        String json = serializer.serializeToJson(saveData);

        // Chemin du fichier de sauvegarde
        String filePath = saveDirectory + File.separator + saveName + ".json";

        // Écrire le JSON dans le fichier
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
            System.out.println("Sauvegarde réussie : " + filePath);
        } catch (IOException e) {
            throw new SaveException("Impossible de sauvegarder les données dans le fichier : " + filePath, e);
        }
    }

    @Override
    public SaveData load(String saveName) throws SaveException {
        if (saveName == null || saveName.isEmpty()) {
            throw new SaveException("Le nom de la sauvegarde ne peut pas être null ou vide.");
        }

        // Chemin du fichier de sauvegarde
        String filePath = saveDirectory + File.separator + saveName + ".json";

        // Lire le JSON depuis le fichier
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new SaveException("Impossible de charger les données depuis le fichier : " + filePath, e);
        }

        // Désérialiser le JSON en SaveData
        SaveData saveData = serializer.deserializeFromJson(json, SaveData.class);
        if (saveData == null) {
            throw new SaveException("Impossible de désérialiser les données de sauvegarde.");
        }

        System.out.println("Chargement réussi : " + filePath);
        return saveData;
    }

    @Override
    public boolean delete(String saveName) throws SaveException {
        if (saveName == null || saveName.isEmpty()) {
            throw new SaveException("Le nom de la sauvegarde ne peut pas être null ou vide.");
        }

        // Chemin du fichier de sauvegarde
        String filePath = saveDirectory + File.separator + saveName + ".json";

        try {
            Files.deleteIfExists(Paths.get(filePath));
            System.out.println("Suppression réussie : " + filePath);
            return true;
        } catch (IOException e) {
            throw new SaveException("Impossible de supprimer le fichier de sauvegarde : " + filePath, e);
        }
    }

    @Override
    public List<String> listSaves() throws SaveException {
        List<String> saveNames = new ArrayList<>();

        try {
            // Lister tous les fichiers JSON dans le dossier de sauvegarde
            Files.list(Paths.get(saveDirectory))
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        saveNames.add(fileName.substring(0, fileName.length() - 5)); // Retirer l'extension .json
                    });
        } catch (IOException e) {
            throw new SaveException("Impossible de lister les sauvegardes dans le dossier : " + saveDirectory, e);
        }

        return saveNames;
    }

    @Override
    public boolean exists(String saveName) throws SaveException {
        if (saveName == null || saveName.isEmpty()) {
            return false;
        }

        // Chemin du fichier de sauvegarde
        String filePath = saveDirectory + File.separator + saveName + ".json";

        return Files.exists(Paths.get(filePath));
    }

    @Override
    public void init(ECSManager ecsManager) {
        // Initialisation du gestionnaire de sauvegarde avec le ECSManager si nécessaire
        // Par exemple, vous pouvez enregistrer des systèmes ou des composants spécifiques pour la sauvegarde
    }

    // --- Getters et Setters ---

    public String getSaveDirectory() {
        return saveDirectory;
    }

    public void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
        createSaveDirectory();
    }

    public Serializer getSerializer() {
        return serializer;
    }
}