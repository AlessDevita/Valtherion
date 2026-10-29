package fr.alessdevita.valtherion.client.audio;

/**
 * Interface de base pour les sons dans Valtherion.
 * Définit les méthodes nécessaires pour contrôler un son.
 */
public interface ISound {

    /**
     * Joue le son.
     */
    void play();

    /**
     * Arrête le son.
     */
    void stop();

    /**
     * Met en pause le son.
     */
    void pause();

    /**
     * Reprend le son après une pause.
     */
    void resume();

    /**
     * Définit le volume du son.
     * @param volume Volume (0.0 à 1.0).
     */
    void setVolume(float volume);

    /**
     * Récupère le volume du son.
     * @return Volume (0.0 à 1.0).
     */
    float getVolume();

    /**
     * Définit le pitch du son.
     * @param pitch Pitch (0.5 à 2.0).
     */
    void setPitch(float pitch);

    /**
     * Récupère le pitch du son.
     * @return Pitch.
     */
    float getPitch();

    /**
     * Définit la position du son (pour les sons 3D).
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    void setPosition(float x, float y, float z);

    /**
     * Récupère la position du son.
     *
     * @return Tableau de coordonnées [x, y, z].
     */
    float[] getPosition();

    /**
     * Définit la catégorie du son.
     * @param category Catégorie de son.
     */
    void setSoundCategory(SoundCategory category);

    /**
     * Récupère la catégorie du son.
     * @return Catégorie de son.
     */
    SoundCategory getSoundCategory();

    /**
     * Définit si le son doit être répété.
     * @param loop true pour répéter le son.
     */
    void setLooping(boolean loop);

    /**
     * Récupère si le son est en boucle.
     * @return true si le son est en boucle.
     */
    boolean isLooping();

    /**
     * Définit si le son est en cours de lecture.
     * @param playing true si le son est en cours de lecture.
     */
    void setPlaying(boolean playing);

    /**
     * Récupère si le son est en cours de lecture.
     * @return true si le son est en cours de lecture.
     */
    boolean isPlaying();

    /**
     * Libère les ressources du son.
     */
    void cleanup();

    String getSoundPath();
}