package fr.alessdevita.valtherion.client.audio;

/**
 * Classe pour les sons positionnés dans l'espace 3D dans Valtherion.
 * Permet de jouer un son à une position spécifique pour un effet spatialisé.
 */
public class PositionedSound implements ISound {

    // Chemin du fichier son
    private String soundPath;

    // Volume du son
    private float volume;

    // Pitch du son
    private float pitch;

    // Position du son (x, y, z)
    private float x, y, z;

    // Catégorie du son
    private SoundCategory category;

    // Indique si le son est en boucle
    private boolean isLooping;

    // Indique si le son est en cours de lecture
    private boolean isPlaying;

    // ID du son (pour le gestionnaire audio)
    private int soundId;

    /**
     * Constructeur du son positionné.
     * @param soundPath Chemin du fichier son.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public PositionedSound(String soundPath, float x, float y, float z) {
        this.soundPath = soundPath;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.category = SoundCategory.BLOCKS;
        this.isLooping = false;
        this.isPlaying = false;
    }

    @Override
    public void play() {
        // Logique pour jouer le son (à implémenter avec une bibliothèque audio comme LWJGL OpenAL)
        System.out.println("Jouer le son : " + soundPath + " à la position (" + x + ", " + y + ", " + z + ")");
        isPlaying = true;
    }

    @Override
    public void stop() {
        // Logique pour arrêter le son
        System.out.println("Arrêter le son : " + soundPath);
        isPlaying = false;
    }

    @Override
    public void pause() {
        // Logique pour mettre en pause le son
        System.out.println("Mettre en pause le son : " + soundPath);
    }

    @Override
    public void resume() {
        // Logique pour reprendre le son
        System.out.println("Reprendre le son : " + soundPath);
    }

    @Override
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = Math.max(0.5f, Math.min(2.0f, pitch));
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float[] getPosition() {
        return new float[] {x, y, z};
    }

    @Override
    public void setSoundCategory(SoundCategory category) {
        this.category = category;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return category;
    }

    @Override
    public void setLooping(boolean loop) {
        this.isLooping = loop;
    }

    @Override
    public boolean isLooping() {
        return isLooping;
    }

    @Override
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void cleanup() {
        // Libérer les ressources du son
        System.out.println("Nettoyer le son : " + soundPath);
    }

    // --- Getters et Setters supplémentaires ---

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    /**
     * Retourne une représentation textuelle du son positionné.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PositionedSound{" +
                "soundPath='" + soundPath + '\'' +
                ", volume=" + volume +
                ", pitch=" + pitch +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", category=" + category +
                ", isLooping=" + isLooping +
                ", isPlaying=" + isPlaying +
                '}';
    }
}