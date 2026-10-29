package fr.alessdevita.valtherion.client.audio;

/**
 * Classe pour gérer les accesseurs d'événements sonores dans Valtherion.
 * Permet d'associer un son à un événement et de le jouer lorsque l'événement se produit.
 */
public class SoundEventAccessor {

    // Nom de l'événement sonore
    private final String eventName;

    // Chemin du fichier son
    private String soundPath;

    // Volume du son
    private float volume;

    // Pitch du son
    private float pitch;

    // Catégorie du son
    private SoundCategory category;

    // Indique si le son doit être joué en boucle
    private boolean isLooping;

    // Indique si le son est en cours de lecture
    private boolean isPlaying;

    // ID du son (pour le gestionnaire audio)
    private int soundId;

    /**
     * Constructeur de l'accesseur d'événement sonore.
     * @param eventName Nom de l'événement sonore.
     * @param soundPath Chemin du fichier son.
     */
    public SoundEventAccessor(String eventName, String soundPath) {
        this.eventName = eventName;
        this.soundPath = soundPath;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.category = SoundCategory.MASTER;
        this.isLooping = false;
        this.isPlaying = false;
    }

    /**
     * Joue le son associé à l'événement.
     */
    public void play() {
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playSound(soundPath);
        isPlaying = true;
    }

    /**
     * Joue le son à une position spécifique.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void play(float x, float y, float z) {
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playSound(soundPath, x, y, z);
        isPlaying = true;
    }

    /**
     * Arrête le son.
     */
    public void stop() {
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.stopSound(soundPath);
        isPlaying = false;
    }

    /**
     * Met en pause le son.
     */
    public void pause() {
        // À implémenter avec une bibliothèque audio comme OpenAL
        System.out.println("Mettre en pause le son : " + soundPath);
    }

    /**
     * Reprend le son.
     */
    public void resume() {
        // À implémenter avec une bibliothèque audio comme OpenAL
        System.out.println("Reprendre le son : " + soundPath);
    }

    // --- Getters et Setters ---

    public String getEventName() {
        return eventName;
    }

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

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = Math.max(0.5f, Math.min(2.0f, pitch));
    }

    public SoundCategory getCategory() {
        return category;
    }

    public void setCategory(SoundCategory category) {
        this.category = category;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        this.isLooping = looping;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    /**
     * Retourne une représentation textuelle de l'accesseur d'événement sonore.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundEventAccessor{" +
                "eventName='" + eventName + '\'' +
                ", soundPath='" + soundPath + '\'' +
                ", volume=" + volume +
                ", pitch=" + pitch +
                ", category=" + category +
                ", isLooping=" + isLooping +
                ", isPlaying=" + isPlaying +
                '}';
    }
}
