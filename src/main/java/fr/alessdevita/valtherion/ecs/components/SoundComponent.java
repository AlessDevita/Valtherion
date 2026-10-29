package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

/**
 * Composant pour le son d'une entité dans Valtherion.
 * Stocke les propriétés du son (clip, volume, boucle, etc.).
 */
public class SoundComponent implements Component {

    // Chemin du fichier audio
    private String soundPath;

    // Volume du son (0.0 à 1.0)
    private float volume;

    // Indique si le son est en boucle
    private boolean isLooping;

    // Indique si le son est en train de jouer
    private boolean isPlaying;

    // Indique si le son est en pause
    private boolean isPaused;

    // Temps de début du son (en secondes)
    private float startTime;

    // Durée du son (en secondes)
    private float duration;

    // Entité associée
    private Entity entity;

    // Type de son (MUSIC, SFX, VOICE, AMBIENT)
    private SoundType type;

    // Énumération des types de sons
    public enum SoundType {
        MUSIC,    // Musique de fond
        SFX,      // Effet sonore
        VOICE,    // Voix (dialogues, etc.)
        AMBIENT   // Son ambiant
    }

    /**
     * Constructeur du composant de son.
     */
    public SoundComponent() {
        this.soundPath = "";
        this.volume = 1.0f;
        this.isLooping = false;
        this.isPlaying = false;
        this.isPaused = false;
        this.startTime = 0.0f;
        this.duration = 0.0f;
        this.type = SoundType.SFX;
    }

    /**
     * Constructeur du composant de son avec un chemin.
     * @param soundPath Chemin du fichier audio.
     */
    public SoundComponent(String soundPath) {
        this();
        this.soundPath = soundPath;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void cleanup() {
        stop();
    }

    /**
     * Joue le son.
     */
    public void play() {
        if (!isPlaying && soundPath != null && !soundPath.isEmpty()) {
            // Logique pour jouer le son
            // Exemple : Utiliser une bibliothèque audio comme LWJGL OpenAL
            isPlaying = true;
            isPaused = false;
            startTime = 0.0f;
        }
    }

    /**
     * Arrête le son.
     */
    public void stop() {
        if (isPlaying) {
            // Logique pour arrêter le son
            isPlaying = false;
            isPaused = false;
            startTime = 0.0f;
        }
    }

    /**
     * Met le son en pause.
     */
    public void pause() {
        if (isPlaying && !isPaused) {
            // Logique pour mettre le son en pause
            isPaused = true;
        }
    }

    /**
     * Reprend le son après une pause.
     */
    public void resume() {
        if (isPaused) {
            // Logique pour reprendre le son
            isPaused = false;
        }
    }

    // --- Getters et Setters ---

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public SoundType getType() {
        return type;
    }

    public void setType(SoundType type) {
        this.type = type;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundComponent{" +
                "soundPath='" + soundPath + '\'' +
                ", volume=" + volume +
                ", isLooping=" + isLooping +
                ", isPlaying=" + isPlaying +
                '}';
    }
}