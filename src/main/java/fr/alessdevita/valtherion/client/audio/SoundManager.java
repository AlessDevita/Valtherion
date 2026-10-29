package fr.alessdevita.valtherion.client.audio;

import java.util.*;
/**
 * Gestionnaire des sons dans Valtherion.
 * Charge, joue et gère les sons et les musiques.
 */
public class SoundManager {

    // Instance unique du gestionnaire de sons
    private static SoundManager instance;

    // Map des sons chargés (chemin -> ISound)
    private final Map<String, ISound> sounds;

    // Map des volumes par catégorie
    private final Map<SoundCategory, Float> categoryVolumes;

    // Volume principal
    private float masterVolume;

    // Indique si le gestionnaire est initialisé
    private boolean isInitialized;

    /**
     * Constructeur privé pour le singleton.
     */
    private SoundManager() {
        this.sounds = new HashMap<>();
        this.categoryVolumes = new HashMap<>();
        this.masterVolume = 1.0f;
        this.isInitialized = false;

        // Initialiser les volumes par défaut pour chaque catégorie
        for (SoundCategory category : SoundCategory.values()) {
            categoryVolumes.put(category, 1.0f);
        }
    }

    /**
     * Récupère l'instance unique du gestionnaire de sons.
     * @return Instance unique.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Initialise le gestionnaire de sons.
     */
    public void init() {
        if (isInitialized) {
            return;
        }

        // Initialiser le système audio (ex: OpenAL avec LWJGL)
        System.out.println("Initialisation du gestionnaire de sons...");
        isInitialized = true;
    }

    /**
     * Charge un son.
     * @param soundPath Chemin du fichier son.
     * @return Son chargé.
     */
    public ISound loadSound(String soundPath) {
        if (!isInitialized) {
            throw new IllegalStateException("Le gestionnaire de sons n'est pas initialisé.");
        }

        // Vérifier si le son est déjà chargé
        if (sounds.containsKey(soundPath)) {
            return sounds.get(soundPath);
        }

        // Créer un nouveau son (PositionedSound par défaut)
        ISound sound = new PositionedSound(soundPath, 0.0f, 0.0f, 0.0f);
        sounds.put(soundPath, sound);

        return sound;
    }

    /**
     * Joue un son.
     * @param soundPath Chemin du fichier son.
     */
    public void playSound(String soundPath) {
        ISound sound = loadSound(soundPath);
        sound.play();
    }

    /**
     * Joue un son à une position spécifique.
     * @param soundPath Chemin du fichier son.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void playSound(String soundPath, float x, float y, float z) {
        ISound sound = new PositionedSound(soundPath, x, y, z);
        sound.play();
    }

    /**
     * Joue un son lié à une entité.
     * @param soundPath Chemin du fichier son.
     * @param entity Entité associée.
     */
    public void playSound(String soundPath, fr.alessdevita.valtherion.entity.Entity entity) {
        ISound sound = new MovingSound(soundPath, entity);
        sound.play();
    }

    /**
     * Arrête un son.
     * @param soundPath Chemin du fichier son.
     */
    public void stopSound(String soundPath) {
        ISound sound = sounds.get(soundPath);
        if (sound != null) {
            sound.stop();
        }
    }

    /**
     * Arrête tous les sons.
     */
    public void stopAllSounds() {
        for (ISound sound : sounds.values()) {
            sound.stop();
        }
    }

    /**
     * Met en pause tous les sons.
     */
    public void pauseAllSounds() {
        for (ISound sound : sounds.values()) {
            sound.pause();
        }
    }

    /**
     * Reprend tous les sons.
     */
    public void resumeAllSounds() {
        for (ISound sound : sounds.values()) {
            sound.resume();
        }
    }

    /**
     * Définit le volume d'une catégorie de sons.
     * @param category Catégorie de sons.
     * @param volume Volume (0.0 à 1.0).
     */
    public void setCategoryVolume(SoundCategory category, float volume) {
        categoryVolumes.put(category, Math.max(0.0f, Math.min(1.0f, volume)));
    }

    /**
     * Récupère le volume d'une catégorie de sons.
     * @param category Catégorie de sons.
     * @return Volume (0.0 à 1.0).
     */
    public float getCategoryVolume(SoundCategory category) {
        return categoryVolumes.getOrDefault(category, 1.0f);
    }

    /**
     * Définit le volume principal.
     * @param volume Volume (0.0 à 1.0).
     */
    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    /**
     * Récupère le volume principal.
     * @return Volume (0.0 à 1.0).
     */
    public float getMasterVolume() {
        return masterVolume;
    }

    /**
     * Met à jour tous les sons mis à jour chaque tick.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        for (ISound sound : sounds.values()) {
            if (sound instanceof ITickableSound) {
                ((ITickableSound) sound).tick(deltaTime);
            }
        }
    }

    /**
     * Supprime un son.
     * @param soundPath Chemin du fichier son.
     */
    public void unloadSound(String soundPath) {
        ISound sound = sounds.remove(soundPath);
        if (sound != null) {
            sound.cleanup();
        }
    }

    /**
     * Supprime tous les sons.
     */
    public void clear() {
        for (ISound sound : sounds.values()) {
            sound.cleanup();
        }
        sounds.clear();
    }

    /**
     * Nettoie les ressources du gestionnaire de sons.
     */
    public void cleanup() {
        clear();
        isInitialized = false;
    }

    // --- Getters ---

    public Map<String, ISound> getSounds() {
        return sounds;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire de sons.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundManager{" +
                "sounds=" + sounds.size() +
                ", masterVolume=" + masterVolume +
                ", isInitialized=" + isInitialized +
                '}';
    }
}
