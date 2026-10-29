package fr.alessdevita.valtherion.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

/**
 * Classe d'utilitaires pour manipuler les fichiers dans Valtherion.
 */
public class FileUtils {

    /**
     * Lit un fichier texte.
     * @param filePath Chemin du fichier.
     * @return Contenu du fichier.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public static String readFileToString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    /**
     * Lit un fichier texte avec un encodage spécifique.
     * @param filePath Chemin du fichier.
     * @param charset Encodage.
     * @return Contenu du fichier.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public static String readFileToString(String filePath, String charset) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), charset);
    }

    /**
     * Écrit un texte dans un fichier.
     * @param filePath Chemin du fichier.
     * @param content Contenu à écrire.
     * @throws IOException Si une erreur se produit lors de l'écriture.
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Écrit un texte dans un fichier avec un encodage spécifique.
     * @param filePath Chemin du fichier.
     * @param content Contenu à écrire.
     * @param charset Encodage.
     * @throws IOException Si une erreur se produit lors de l'écriture.
     */
    public static void writeStringToFile(String filePath, String content, String charset) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(charset));
    }

    /**
     * Vérifie si un fichier existe.
     * @param filePath Chemin du fichier.
     * @return true si le fichier existe, false sinon.
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Vérifie si un fichier est un répertoire.
     * @param filePath Chemin du fichier.
     * @return true si le fichier est un répertoire, false sinon.
     */
    public static boolean isDirectory(String filePath) {
        return Files.isDirectory(Paths.get(filePath));
    }

    /**
     * Crée un répertoire.
     * @param dirPath Chemin du répertoire.
     * @throws IOException Si une erreur se produit lors de la création.
     */
    public static void createDirectory(String dirPath) throws IOException {
        Files.createDirectories(Paths.get(dirPath));
    }

    /**
     * Supprime un fichier.
     * @param filePath Chemin du fichier.
     * @throws IOException Si une erreur se produit lors de la suppression.
     */
    public static void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * Copie un fichier.
     * @param sourcePath Chemin du fichier source.
     * @param targetPath Chemin du fichier cible.
     * @throws IOException Si une erreur se produit lors de la copie.
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        Files.copy(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Déplace un fichier.
     * @param sourcePath Chemin du fichier source.
     * @param targetPath Chemin du fichier cible.
     * @throws IOException Si une erreur se produit lors du déplacement.
     */
    public static void moveFile(String sourcePath, String targetPath) throws IOException {
        Files.move(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Liste les fichiers dans un répertoire.
     * @param dirPath Chemin du répertoire.
     * @return Liste des chemins des fichiers.
     * @throws IOException Si une erreur se produit lors de la liste.
     */
    public static List<String> listFiles(String dirPath) throws IOException {
        List<String> filePaths = new ArrayList<>();
        Files.list(Paths.get(dirPath)).forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }

    /**
     * Liste les fichiers dans un répertoire avec un filtre.
     * @param dirPath Chemin du répertoire.
     * @param filter Filtre pour les fichiers.
     * @return Liste des chemins des fichiers.
     * @throws IOException Si une erreur se produit lors de la liste.
     */
    public static List<String> listFiles(String dirPath, java.nio.file.DirectoryStream.Filter<Path> filter) throws IOException {
        List<String> filePaths = new ArrayList<>();
        Files.newDirectoryStream(Paths.get(dirPath), filter).forEach(path -> filePaths.add(path.toString()));
        return filePaths;
    }

    /**
     * Liste les fichiers dans un répertoire avec une extension spécifique.
     * @param dirPath Chemin du répertoire.
     * @param extension Extension des fichiers.
     * @return Liste des chemins des fichiers.
     * @throws IOException Si une erreur se produit lors de la liste.
     */
    public static List<String> listFilesWithExtension(String dirPath, String extension) throws IOException {
        return listFiles(dirPath, path -> path.toString().endsWith("." + extension));
    }

    /**
     * Récupère le nom du fichier depuis un chemin.
     * @param filePath Chemin du fichier.
     * @return Nom du fichier.
     */
    public static String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }

    /**
     * Récupère le nom du fichier sans l'extension.
     * @param filePath Chemin du fichier.
     * @return Nom du fichier sans l'extension.
     */
    public static String getFileNameWithoutExtension(String filePath) {
        String fileName = getFileName(filePath);
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    /**
     * Récupère l'extension du fichier.
     * @param filePath Chemin du fichier.
     * @return Extension du fichier.
     */
    public static String getFileExtension(String filePath) {
        String fileName = getFileName(filePath);
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "";
    }

    /**
     * Récupère le chemin du répertoire parent.
     * @param filePath Chemin du fichier.
     * @return Chemin du répertoire parent.
     */
    public static String getParentDirectory(String filePath) {
        return Paths.get(filePath).getParent().toString();
    }

    /**
     * Récupère la taille d'un fichier.
     * @param filePath Chemin du fichier.
     * @return Taille du fichier en octets.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public static long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }

    /**
     * Récupère la date de dernière modification d'un fichier.
     * @param filePath Chemin du fichier.
     * @return Date de dernière modification en millisecondes.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public static long getLastModified(String filePath) throws IOException {
        return Files.getLastModifiedTime(Paths.get(filePath)).toMillis();
    }

    /**
     * Crée un fichier temporaire.
     * @param prefix Préfixe du fichier.
     * @param suffix Suffixe du fichier.
     * @return Chemin du fichier temporaire.
     * @throws IOException Si une erreur se produit lors de la création.
     */
    public static String createTempFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        return tempFile.getAbsolutePath();
    }

    /**
     * Crée un fichier temporaire dans un répertoire spécifique.
     * @param prefix Préfixe du fichier.
     * @param suffix Suffixe du fichier.
     * @param directory Répertoire pour le fichier temporaire.
     * @return Chemin du fichier temporaire.
     * @throws IOException Si une erreur se produit lors de la création.
     */
    public static String createTempFile(String prefix, String suffix, String directory) throws IOException {
        File tempDir = new File(directory);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File tempFile = File.createTempFile(prefix, suffix, tempDir);
        return tempFile.getAbsolutePath();
    }
}
