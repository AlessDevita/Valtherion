package fr.alessdevita.valtherion.client.audio;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Classe pour sérialiser et désérialiser les listes de sons dans Valtherion.
 * Permet de sauvegarder et charger les listes de sons depuis des fichiers JSON.
 */
public class SoundListSerializer {

    // Instance de Gson pour la sérialisation JSON
    private final Gson gson;

    /**
     * Constructeur du sérialiseur de listes de sons.
     */
    public SoundListSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        this.gson = builder.create();
    }

    /**
     * Sérialise une liste de sons en JSON.
     * @param soundList Liste de sons à sérialiser.
     * @return Chaîne JSON.
     */
    public String serializeToJson(SoundList soundList) {
        return gson.toJson(soundList);
    }

    /**
     * Désérialise une liste de sons depuis une chaîne JSON.
     * @param json Chaîne JSON.
     * @return Liste de sons désérialisée.
     */
    public SoundList deserializeFromJson(String json) {
        return gson.fromJson(json, SoundList.class);
    }

    /**
     * Sérialise une liste de sons dans un fichier JSON.
     * @param soundList Liste de sons à sérialiser.
     * @param filePath Chemin du fichier.
     * @throws IOException Si une erreur se produit lors de l'écriture.
     */
    public void serializeToFile(SoundList soundList, String filePath) throws IOException {
        String json = serializeToJson(soundList);
        Files.write(Paths.get(filePath), json.getBytes());
    }

    /**
     * Désérialise une liste de sons depuis un fichier JSON.
     * @param filePath Chemin du fichier.
     * @return Liste de sons désérialisée.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public SoundList deserializeFromFile(String filePath) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return deserializeFromJson(json);
    }

    /**
     * Sérialise une liste de sons dans un format personnalisé.
     * @param soundList Liste de sons à sérialiser.
     * @return Chaîne au format personnalisé.
     */
    public String serializeToCustomFormat(SoundList soundList) {
        StringBuilder sb = new StringBuilder();
        sb.append("SoundList: ").append(soundList.getName()).append("\n");

        for (Map.Entry<SoundCategory, List<String>> entry : soundList.getSoundsByCategory().entrySet()) {
            sb.append("Category: ").append(entry.getKey().name()).append("\n");
            for (String soundPath : entry.getValue()) {
                sb.append("  - ").append(soundPath).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Désérialise une liste de sons depuis un format personnalisé.
     * @param customFormat Chaîne au format personnalisé.
     * @return Liste de sons désérialisée.
     */
    public SoundList deserializeFromCustomFormat(String customFormat) {
        SoundList soundList = new SoundList();

        String[] lines = customFormat.split("\n");
        SoundCategory currentCategory = null;

        for (String line : lines) {
            if (line.startsWith("SoundList:")) {
                soundList.setName(line.substring("SoundList:".length()).trim());
            } else if (line.startsWith("Category:")) {
                String categoryName = line.substring("Category:".length()).trim();
                currentCategory = SoundCategory.valueOf(categoryName);
            } else if (line.startsWith("  - ")) {
                String soundPath = line.substring("  - ".length()).trim();
                if (currentCategory != null) {
                    soundList.addSound(currentCategory, soundPath);
                }
            }
        }

        return soundList;
    }

    /**
     * Sérialise une liste de sons dans un fichier au format personnalisé.
     * @param soundList Liste de sons à sérialiser.
     * @param filePath Chemin du fichier.
     * @throws IOException Si une erreur se produit lors de l'écriture.
     */
    public void serializeToCustomFile(SoundList soundList, String filePath) throws IOException {
        String customFormat = serializeToCustomFormat(soundList);
        Files.write(Paths.get(filePath), customFormat.getBytes());
    }

    /**
     * Désérialise une liste de sons depuis un fichier au format personnalisé.
     * @param filePath Chemin du fichier.
     * @return Liste de sons désérialisée.
     * @throws IOException Si une erreur se produit lors de la lecture.
     */
    public SoundList deserializeFromCustomFile(String filePath) throws IOException {
        String customFormat = new String(Files.readAllBytes(Paths.get(filePath)));
        return deserializeFromCustomFormat(customFormat);
    }
}
