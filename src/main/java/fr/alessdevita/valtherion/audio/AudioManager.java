package fr.alessdevita.valtherion.audio;


import fr.alessdevita.valtherion.core.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Gère la lecture des sons et de la musique.
 */
public class AudioManager {
    // Logger pour les messages de debug
    private final Logger logger;

    // Volume principal (0.0 à 1.0)
    private float masterVolume;

    // Volume de la musique (0.0 à 1.0)
    private float musicVolume;

    // Volume des effets sonores (0.0 à 1.0)
    private float soundVolume;

    // Musique actuelle en lecture
    private Music currentMusic;

    // Liste des sons en cours de lecture
    private final List<Sound> playingSounds;

    // Chargeur de fichiers audio
    private final AudioLoader audioLoader;

    // État de la musique (en lecture ou non)
    private boolean isMusicPlaying;

    /**
     * Constructeur du gestionnaire audio.
     */
    public AudioManager() {
        this.logger = new Logger("AudioManager");
        this.masterVolume = 1.0f;
        this.musicVolume = 0.8f;
        this.soundVolume = 1.0f;
        this.playingSounds = new ArrayList<>();
        this.audioLoader = new AudioLoader();
        this.isMusicPlaying = false;
        logger.info("Gestionnaire audio initialisé.");
    }

    /**
     * Met à jour le gestionnaire audio.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        // Mettre à jour les sons en cours de lecture
        for (Iterator<Sound> iterator = playingSounds.iterator(); iterator.hasNext();) {
            Sound sound = iterator.next();
            if (sound.isFinished()) {
                iterator.remove();
                logger.debug("Son terminé : " + sound.getName());
            }
        }

        // Mettre à jour la musique
        if (currentMusic != null && isMusicPlaying) {
            currentMusic.update();
        }
    }

    /**
     * Joue un son.
     * @param soundId ID du son à jouer.
     * @param x Coordonnée X de la source du son.
     * @param y Coordonnée Y de la source du son.
     * @param z Coordonnée Z de la source du son.
     */
    public void playSound(String soundId, float x, float y, float z) {
        Sound sound = audioLoader.loadSound(soundId);
        if (sound == null) {
            logger.warn("Son introuvable : " + soundId);
            return;
        }

        sound.setVolume(soundVolume * masterVolume);
        sound.play(x, y, z);
        playingSounds.add(sound);
        logger.debug("Son joué : " + soundId + " à (" + x + ", " + y + ", " + z + ").");
    }

    /**
     * Joue un son sans position (son global).
     * @param soundId ID du son à jouer.
     */
    public void playSound(String soundId) {
        playSound(soundId, 0, 0, 0);
    }

    /**
     * Joue une musique.
     * @param musicId ID de la musique à jouer.
     */
    public void playMusic(String musicId) {
        Music music = audioLoader.loadMusic(musicId);
        if (music == null) {
            logger.warn("Musique introuvable : " + musicId);
            return;
        }

        // Arrêter la musique actuelle si elle est en lecture
        if (currentMusic != null && isMusicPlaying) {
            currentMusic.stop();
        }

        currentMusic = music;
        currentMusic.setVolume(musicVolume * masterVolume);
        currentMusic.play();
        isMusicPlaying = true;
        logger.debug("Musique jouée : " + musicId);
    }

    /**
     * Arrête la musique actuelle.
     */
    public void stopMusic() {
        if (currentMusic != null && isMusicPlaying) {
            currentMusic.stop();
            isMusicPlaying = false;
            logger.debug("Musique arrêtée.");
        }
    }

    /**
     * Arrête tous les sons.
     */
    public void stopAllSounds() {
        for (Sound sound : playingSounds) {
            sound.stop();
        }
        playingSounds.clear();
        logger.debug("Tous les sons ont été arrêtés.");
    }

    /**
     * Arrête un son spécifique.
     * @param soundId ID du son à arrêter.
     */
    public void stopSound(String soundId) {
        for (Iterator<Sound> iterator = playingSounds.iterator(); iterator.hasNext();) {
            Sound sound = iterator.next();
            if (sound.getName().equals(soundId)) {
                sound.stop();
                iterator.remove();
                logger.debug("Son arrêté : " + soundId);
                break;
            }
        }
    }

    // --- Getters et Setters ---

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, masterVolume));
        logger.debug("Volume principal défini à : " + this.masterVolume);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.musicVolume * masterVolume);
        }
        logger.debug("Volume de la musique défini à : " + this.musicVolume);
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = Math.max(0.0f, Math.min(1.0f, soundVolume));
        logger.debug("Volume des sons défini à : " + this.soundVolume);
    }

    public boolean isMusicPlaying() {
        return isMusicPlaying;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public List<Sound> getPlayingSounds() {
        return Collections.unmodifiableList(playingSounds);
    }

    /**
     * Libère les ressources audio.
     */
    public void cleanup() {
        stopMusic();
        stopAllSounds();
        audioLoader.cleanup();
        logger.info("Ressources audio libérées.");
    }
}