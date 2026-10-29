package fr.alessdevita.valtherion.audio;

/**
 * Classe de base pour les sons dans Valtherion.
 * Représente un son ou un effet sonore à jouer.
 */
public class Sound {

    // Nom du son
    private final String name;

    // ID du son (utilisé pour le chargement)
    private final String soundId;

    // Volume du son (0.0 à 1.0)
    private float volume;

    // Position de la source du son
    private float x, y, z;

    // État du son
    private boolean isPlaying;
    private boolean isFinished;

    // ID de la source OpenAL (pour la lecture audio)
    private int sourceId;

    /**
     * Constructeur du son.
     * @param name Nom du son.
     * @param soundId ID du son.
     */
    public Sound(String name, String soundId) {
        this.name = name;
        this.soundId = soundId;
        this.volume = 1.0f;
        this.isPlaying = false;
        this.isFinished = false;
    }

    /**
     * Joue le son à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void play(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isPlaying = true;
        this.isFinished = false;

        // Logique pour jouer le son avec OpenAL ou une autre bibliothèque audio
        // Exemple : sourceId = AudioSystem.playSound(soundId, x, y, z, volume);
    }

    /**
     * Joue le son sans position (son global).
     */
    public void play() {
        play(0, 0, 0);
    }

    /**
     * Arrête le son.
     */
    public void stop() {
        this.isPlaying = false;

        // Logique pour arrêter le son avec OpenAL ou une autre bibliothèque audio
        // Exemple : AudioSystem.stopSound(sourceId);
    }

    /**
     * Met à jour le son.
     */
    public void update() {
        // Vérifier si le son est terminé
        // Exemple : isFinished = AudioSystem.isSoundFinished(sourceId);
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public String getSoundId() {
        return soundId;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    /**
     * Retourne une représentation textuelle du son.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Sound{" +
                "name='" + name + '\'' +
                ", soundId='" + soundId + '\'' +
                ", volume=" + volume +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                ", isPlaying=" + isPlaying +
                '}';
    }
}