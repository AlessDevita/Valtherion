package fr.alessdevita.valtherion.audio;

/**
 * Classe pour la musique de fond dans Valtherion.
 * Représente une musique à jouer en boucle.
 */
public class Music {

    // Nom de la musique
    private final String name;

    // ID de la musique (utilisé pour le chargement)
    private final String musicId;

    // Volume de la musique (0.0 à 1.0)
    private float volume;

    // État de la musique
    private boolean isPlaying;
    private boolean isLooping;

    // ID de la source OpenAL (pour la lecture audio)
    private int sourceId;

    /**
     * Constructeur de la musique.
     * @param name Nom de la musique.
     * @param musicId ID de la musique.
     */
    public Music(String name, String musicId) {
        this.name = name;
        this.musicId = musicId;
        this.volume = 1.0f;
        this.isPlaying = false;
        this.isLooping = true; // La musique est en boucle par défaut
    }

    /**
     * Joue la musique.
     */
    public void play() {
        this.isPlaying = true;

        // Logique pour jouer la musique avec OpenAL ou une autre bibliothèque audio
        // Exemple : sourceId = AudioSystem.playMusic(musicId, volume, isLooping);
    }

    /**
     * Arrête la musique.
     */
    public void stop() {
        this.isPlaying = false;

        // Logique pour arrêter la musique avec OpenAL ou une autre bibliothèque audio
        // Exemple : AudioSystem.stopMusic(sourceId);
    }

    /**
     * Met à jour la musique.
     */
    public void update() {
        // Vérifier si la musique est terminée (si elle n'est pas en boucle)
        if (!isLooping) {
            // Exemple : isPlaying = !AudioSystem.isMusicFinished(sourceId);
        }
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public String getMusicId() {
        return musicId;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    /**
     * Retourne une représentation textuelle de la musique.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", musicId='" + musicId + '\'' +
                ", volume=" + volume +
                ", isPlaying=" + isPlaying +
                ", isLooping=" + isLooping +
                '}';
    }
}