package fr.alessdevita.valtherion.audio;

import fr.alessdevita.valtherion.core.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Chargeur de fichiers audio pour Valtherion.
 * Charge les sons et les musiques à partir de leurs IDs.
 */
public class AudioLoader {

    // Logger pour les messages de debug
    private final Logger logger;

    // Map des sons chargés (ID -> Sound)
    private final Map<String, Sound> sounds;

    // Map des musiques chargées (ID -> Music)
    private final Map<String, Music> musics;

    /**
     * Constructeur du chargeur audio.
     */
    public AudioLoader() {
        this.logger = new Logger("AudioLoader");
        this.sounds = new HashMap<>();
        this.musics = new HashMap<>();
    }

    /**
     * Charge un son à partir de son ID.
     * @param soundId ID du son.
     * @return Son chargé ou null si introuvable.
     */
    public Sound loadSound(String soundId) {
        if (soundId == null || soundId.isEmpty()) {
            logger.warn("ID de son invalide : null ou vide.");
            return null;
        }

        // Vérifier si le son est déjà chargé
        if (sounds.containsKey(soundId)) {
            return sounds.get(soundId);
        }

        // Charger le son (ex: depuis un fichier ou une ressource)
        Sound sound = createSound(soundId);
        if (sound != null) {
            sounds.put(soundId, sound);
            logger.debug("Son chargé : " + soundId);
        } else {
            logger.warn("Impossible de charger le son : " + soundId);
        }

        return sound;
    }

    /**
     * Charge une musique à partir de son ID.
     * @param musicId ID de la musique.
     * @return Musique chargée ou null si introuvable.
     */
    public Music loadMusic(String musicId) {
        if (musicId == null || musicId.isEmpty()) {
            logger.warn("ID de musique invalide : null ou vide.");
            return null;
        }

        // Vérifier si la musique est déjà chargée
        if (musics.containsKey(musicId)) {
            return musics.get(musicId);
        }

        // Charger la musique (ex: depuis un fichier ou une ressource)
        Music music = createMusic(musicId);
        if (music != null) {
            musics.put(musicId, music);
            logger.debug("Musique chargée : " + musicId);
        } else {
            logger.warn("Impossible de charger la musique : " + musicId);
        }

        return music;
    }

    /**
     * Crée un son à partir de son ID.
     * @param soundId ID du son.
     * @return Son créé ou null si introuvable.
     */
    private Sound createSound(String soundId) {
        // Logique pour créer un son à partir de son ID
        // Exemple : Charger un fichier audio et créer un objet Sound
        // À implémenter selon ta bibliothèque audio (OpenAL, LWJGL, etc.)
        return new Sound(soundId, soundId);
    }

    /**
     * Crée une musique à partir de son ID.
     * @param musicId ID de la musique.
     * @return Musique créée ou null si introuvable.
     */
    private Music createMusic(String musicId) {
        // Logique pour créer une musique à partir de son ID
        // Exemple : Charger un fichier audio et créer un objet Music
        // À implémenter selon ta bibliothèque audio (OpenAL, LWJGL, etc.)
        return new Music(musicId, musicId);
    }

    /**
     * Libère les ressources audio.
     */
    public void cleanup() {
        sounds.clear();
        musics.clear();
        logger.info("Ressources audio libérées.");
    }
}