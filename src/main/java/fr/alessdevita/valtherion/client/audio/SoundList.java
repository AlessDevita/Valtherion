package fr.alessdevita.valtherion.client.audio;

import java.util.*;

/**
 * Classe pour gérer une liste de sons dans Valtherion.
 * Stocke les sons par catégorie et permet de les récupérer aléatoirement.
 */
public class SoundList {

    // Nom de la liste de sons
    private String name;

    // Map des sons par catégorie (catégorie -> liste de chemins de sons)
    private final Map<SoundCategory, List<String>> soundsByCategory;

    // Liste de tous les sons (toutes catégories confondues)
    private final List<String> allSounds;

    /**
     * Constructeur de la liste de sons.
     */
    public SoundList() {
        this.name = "DefaultSoundList";
        this.soundsByCategory = new HashMap<>();
        this.allSounds = new ArrayList<>();
    }

    /**
     * Constructeur de la liste de sons avec un nom.
     * @param name Nom de la liste de sons.
     */
    public SoundList(String name) {
        this();
        this.name = name;
    }

    /**
     * Ajoute un son à une catégorie.
     * @param category Catégorie de sons.
     * @param soundPath Chemin du fichier son.
     */
    public void addSound(SoundCategory category, String soundPath) {
        soundsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(soundPath);
        allSounds.add(soundPath);
    }

    /**
     * Ajoute un son à la catégorie par défaut (MASTER).
     * @param soundPath Chemin du fichier son.
     */
    public void addSound(String soundPath) {
        addSound(SoundCategory.MASTER, soundPath);
    }

    /**
     * Récupère un son aléatoire pour une catégorie.
     * @param category Catégorie de sons.
     * @return Chemin du fichier son ou null si la catégorie est vide.
     */
    public String getRandomSound(SoundCategory category) {
        List<String> sounds = soundsByCategory.get(category);
        if (sounds == null || sounds.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return sounds.get(random.nextInt(sounds.size()));
    }

    /**
     * Récupère un son aléatoire parmi tous les sons.
     * @return Chemin du fichier son ou null si la liste est vide.
     */
    public String getRandomSound() {
        if (allSounds.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return allSounds.get(random.nextInt(allSounds.size()));
    }

    /**
     * Récupère tous les sons d'une catégorie.
     * @param category Catégorie de sons.
     * @return Liste des chemins de sons.
     */
    public List<String> getSounds(SoundCategory category) {
        return soundsByCategory.getOrDefault(category, new ArrayList<>());
    }

    /**
     * Récupère tous les sons.
     * @return Liste de tous les chemins de sons.
     */
    public List<String> getAllSounds() {
        return allSounds;
    }

    /**
     * Supprime un son d'une catégorie.
     * @param category Catégorie de sons.
     * @param soundPath Chemin du fichier son.
     */
    public void removeSound(SoundCategory category, String soundPath) {
        List<String> sounds = soundsByCategory.get(category);
        if (sounds != null) {
            sounds.remove(soundPath);
        }
        allSounds.remove(soundPath);
    }

    /**
     * Supprime un son de toutes les catégories.
     * @param soundPath Chemin du fichier son.
     */
    public void removeSound(String soundPath) {
        for (SoundCategory category : SoundCategory.values()) {
            removeSound(category, soundPath);
        }
    }

    /**
     * Supprime tous les sons d'une catégorie.
     * @param category Catégorie de sons.
     */
    public void clearCategory(SoundCategory category) {
        List<String> sounds = soundsByCategory.get(category);
        if (sounds != null) {
            allSounds.removeAll(sounds);
            sounds.clear();
        }
    }

    /**
     * Supprime tous les sons.
     */
    public void clear() {
        soundsByCategory.clear();
        allSounds.clear();
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<SoundCategory, List<String>> getSoundsByCategory() {
        return soundsByCategory;
    }

    /**
     * Retourne une représentation textuelle de la liste de sons.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundList{" +
                "name='" + name + '\'' +
                ", soundsByCategory=" + soundsByCategory.size() +
                ", allSounds=" + allSounds.size() +
                '}';
    }
}
