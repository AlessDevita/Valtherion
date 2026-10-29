package fr.alessdevita.valtherion.client.audio;

/**
 * Classe pour représenter une entrée dans le pool de sons dans Valtherion.
 * Stocke un son, son état, et son ID unique dans le pool.
 */
public class SoundPoolEntry {

    // ID unique de l'entrée dans le pool
    private final int id;

    // Son associé
    private final ISound sound;

    // État du son (PLAYING, PAUSED, STOPPED)
    private SoundState state;

    // Énumération des états du son
    public enum SoundState {
        PLAYING,   // En cours de lecture
        PAUSED,    // En pause
        STOPPED    // Arrêté
    }

    // Compteur pour générer des IDs uniques
    private static int idCounter = 0;

    /**
     * Constructeur de l'entrée dans le pool de sons.
     * @param sound Son associé.
     */
    public SoundPoolEntry(ISound sound) {
        this.id = idCounter++;
        this.sound = sound;
        this.state = SoundState.STOPPED;
    }

    /**
     * Joue le son.
     */
    public void play() {
        sound.play();
        state = SoundState.PLAYING;
    }

    /**
     * Arrête le son.
     */
    public void stop() {
        sound.stop();
        state = SoundState.STOPPED;
    }

    /**
     * Met en pause le son.
     */
    public void pause() {
        sound.pause();
        state = SoundState.PAUSED;
    }

    /**
     * Reprend le son.
     */
    public void resume() {
        sound.resume();
        state = SoundState.PLAYING;
    }

    // --- Getters et Setters ---

    public int getId() {
        return id;
    }

    public ISound getSound() {
        return sound;
    }

    public SoundState getState() {
        return state;
    }

    public void setState(SoundState state) {
        this.state = state;
    }

    /**
     * Retourne une représentation textuelle de l'entrée dans le pool de sons.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundPoolEntry{" +
                "id=" + id +
                ", sound=" + sound +
                ", state=" + state +
                '}';
    }

    /**
     * Réinitialise le compteur d'IDs.
     */
    public static void resetIdCounter() {
        idCounter = 0;
    }
}