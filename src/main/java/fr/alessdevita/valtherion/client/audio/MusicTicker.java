package fr.alessdevita.valtherion.client.audio;

import java.util.*;

/**
 * Classe pour gérer les musiques de fond dans Valtherion.
 * Permet de jouer des musiques en boucle, de changer de musique après un certain temps,
 * et de contrôler le volume.
 */
public class MusicTicker {

    // Instance unique du gestionnaire de musiques
    private static MusicTicker instance;

    // Gestionnaire de sons
    private final SoundManager soundManager;

    // Musique actuelle
    private ISound currentMusic;

    // Liste des musiques disponibles
    private final List<String> musicList;

    // Index de la musique actuelle
    private int currentMusicIndex;

    // Temps écoulé depuis le début de la musique actuelle (en secondes)
    private float currentMusicTime;

    // Durée de la musique actuelle (en secondes)
    private float currentMusicDuration;

    // Temps avant de changer de musique (en secondes)
    private float timeBeforeNextMusic;

    // Volume de la musique
    private float musicVolume;

    // Indique si le gestionnaire est en cours de lecture
    private boolean isPlaying;

    // Indique si le gestionnaire est initialisé
    private boolean isInitialized;

    /**
     * Constructeur privé pour le singleton.
     */
    public MusicTicker() {
        this.soundManager = SoundManager.getInstance();
        this.musicList = new ArrayList<>();
        this.currentMusicIndex = 0;
        this.currentMusicTime = 0.0f;
        this.currentMusicDuration = 0.0f;
        this.timeBeforeNextMusic = 0.0f;
        this.musicVolume = 1.0f;
        this.isPlaying = false;
        this.isInitialized = false;
    }

    /**
     * Récupère l'instance unique du gestionnaire de musiques.
     * @return Instance unique.
     */
    public static MusicTicker getInstance() {
        if (instance == null) {
            instance = new MusicTicker();
        }
        return instance;
    }

    /**
     * Initialise le gestionnaire de musiques.
     */
    public void init() {
        if (isInitialized) {
            return;
        }

        // Ajouter des musiques par défaut
        addMusic("sounds/music/ambient1.ogg");
        addMusic("sounds/music/ambient2.ogg");
        addMusic("sounds/music/ambient3.ogg");

        // Définir le temps avant de changer de musique (ex: 180 secondes = 3 minutes)
        timeBeforeNextMusic = 180.0f;

        isInitialized = true;
    }

    /**
     * Ajoute une musique à la liste.
     * @param musicPath Chemin du fichier musique.
     */
    public void addMusic(String musicPath) {
        musicList.add(musicPath);
    }

    /**
     * Supprime une musique de la liste.
     * @param musicPath Chemin du fichier musique.
     */
    public void removeMusic(String musicPath) {
        musicList.remove(musicPath);

        // Si la musique supprimée est la musique actuelle, arrêter la lecture
        if (currentMusic != null && currentMusic.getSoundPath().equals(musicPath)) {
            stop();
        }
    }

    /**
     * Joue la musique actuelle.
     */
    public void play() {
        if (!isInitialized || musicList.isEmpty()) {
            return;
        }

        // Arrêter la musique actuelle si elle est en cours de lecture
        if (currentMusic != null && isPlaying) {
            currentMusic.stop();
        }

        // Charger et jouer la musique actuelle
        String musicPath = musicList.get(currentMusicIndex);
        currentMusic = soundManager.loadSound(musicPath);
        currentMusic.setSoundCategory(SoundCategory.MUSIC);
        currentMusic.setVolume(musicVolume);
        currentMusic.setLooping(true);
        currentMusic.play();

        isPlaying = true;
        currentMusicTime = 0.0f;
    }

    /**
     * Arrête la musique actuelle.
     */
    public void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
        isPlaying = false;
    }

    /**
     * Met en pause la musique actuelle.
     */
    public void pause() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    /**
     * Reprend la musique actuelle.
     */
    public void resume() {
        if (currentMusic != null) {
            currentMusic.resume();
        }
    }

    /**
     * Passe à la musique suivante.
     */
    public void nextMusic() {
        if (musicList.isEmpty()) {
            return;
        }

        currentMusicIndex = (currentMusicIndex + 1) % musicList.size();
        play();
    }

    /**
     * Passe à la musique précédente.
     */
    public void previousMusic() {
        if (musicList.isEmpty()) {
            return;
        }

        currentMusicIndex = (currentMusicIndex - 1 + musicList.size()) % musicList.size();
        play();
    }

    /**
     * Définit le volume de la musique.
     * @param volume Volume (0.0 à 1.0).
     */
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

    /**
     * Récupère le volume de la musique.
     * @return Volume (0.0 à 1.0).
     */
    public float getMusicVolume() {
        return musicVolume;
    }

    /**
     * Définit le temps avant de changer de musique.
     * @param timeBeforeNextMusic Temps en secondes.
     */
    public void setTimeBeforeNextMusic(float timeBeforeNextMusic) {
        this.timeBeforeNextMusic = timeBeforeNextMusic;
    }

    /**
     * Récupère le temps avant de changer de musique.
     * @return Temps en secondes.
     */
    public float getTimeBeforeNextMusic() {
        return timeBeforeNextMusic;
    }

    /**
     * Met à jour le gestionnaire de musiques.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isPlaying) {
            return;
        }

        // Mettre à jour le temps écoulé pour la musique actuelle
        currentMusicTime += deltaTime;

        // Si le temps écoulé dépasse le temps avant de changer de musique, passer à la musique suivante
        if (currentMusicTime >= timeBeforeNextMusic) {
            nextMusic();
        }
    }

    // --- Getters ---

    public ISound getCurrentMusic() {
        return currentMusic;
    }

    public List<String> getMusicList() {
        return musicList;
    }

    public int getCurrentMusicIndex() {
        return currentMusicIndex;
    }

    public float getCurrentMusicTime() {
        return currentMusicTime;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Nettoie les ressources du gestionnaire de musiques.
     */
    public void cleanup() {
        stop();
        musicList.clear();
        isInitialized = false;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire de musiques.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "MusicTicker{" +
                "currentMusicIndex=" + currentMusicIndex +
                ", musicList=" + musicList.size() +
                ", musicVolume=" + musicVolume +
                ", isPlaying=" + isPlaying +
                '}';
    }
}
