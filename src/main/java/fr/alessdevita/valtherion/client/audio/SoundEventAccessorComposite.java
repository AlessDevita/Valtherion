package fr.alessdevita.valtherion.client.audio;

import java.util.*;

/**
 * Classe pour gérer les accesseurs composites d'événements sonores dans Valtherion.
 * Permet de regrouper plusieurs accesseurs d'événements sonores et de les jouer simultanément ou aléatoirement.
 */
public class SoundEventAccessorComposite {

    // Nom de l'événement composite
    private final String compositeEventName;

    // Liste des accesseurs d'événements sonores
    private final List<SoundEventAccessor> soundEventAccessors;

    // Mode de lecture (SIMULTANEOUS, RANDOM, SEQUENTIAL)
    private PlayMode playMode;

    // Index du son actuel (pour le mode SEQUENTIAL)
    private int currentIndex;

    // Énumération des modes de lecture
    public enum PlayMode {
        SIMULTANEOUS,  // Joue tous les sons simultanément
        RANDOM,        // Joue un son aléatoire
        SEQUENTIAL     // Joue les sons dans l'ordre
    }

    /**
     * Constructeur de l'accesseur composite d'événements sonores.
     * @param compositeEventName Nom de l'événement composite.
     */
    public SoundEventAccessorComposite(String compositeEventName) {
        this.compositeEventName = compositeEventName;
        this.soundEventAccessors = new ArrayList<>();
        this.playMode = PlayMode.SIMULTANEOUS;
        this.currentIndex = 0;
    }

    /**
     * Ajoute un accesseur d'événement sonore.
     * @param accessor Accesseur à ajouter.
     */
    public void addAccessor(SoundEventAccessor accessor) {
        soundEventAccessors.add(accessor);
    }

    /**
     * Supprime un accesseur d'événement sonore.
     * @param accessor Accesseur à supprimer.
     */
    public void removeAccessor(SoundEventAccessor accessor) {
        soundEventAccessors.remove(accessor);
    }

    /**
     * Joue les sons selon le mode de lecture.
     */
    public void play() {
        switch (playMode) {
            case SIMULTANEOUS:
                playSimultaneous();
                break;
            case RANDOM:
                playRandom();
                break;
            case SEQUENTIAL:
                playSequential();
                break;
        }
    }

    /**
     * Joue tous les sons simultanément.
     */
    private void playSimultaneous() {
        for (SoundEventAccessor accessor : soundEventAccessors) {
            accessor.play();
        }
    }

    /**
     * Joue un son aléatoire.
     */
    private void playRandom() {
        if (!soundEventAccessors.isEmpty()) {
            Random random = new Random();
            SoundEventAccessor accessor = soundEventAccessors.get(random.nextInt(soundEventAccessors.size()));
            accessor.play();
        }
    }

    /**
     * Joue les sons dans l'ordre.
     */
    private void playSequential() {
        if (!soundEventAccessors.isEmpty()) {
            SoundEventAccessor accessor = soundEventAccessors.get(currentIndex);
            accessor.play();
            currentIndex = (currentIndex + 1) % soundEventAccessors.size();
        }
    }

    /**
     * Arrête tous les sons.
     */
    public void stop() {
        for (SoundEventAccessor accessor : soundEventAccessors) {
            accessor.stop();
        }
    }

    /**
     * Met en pause tous les sons.
     */
    public void pause() {
        for (SoundEventAccessor accessor : soundEventAccessors) {
            accessor.pause();
        }
    }

    /**
     * Reprend tous les sons.
     */
    public void resume() {
        for (SoundEventAccessor accessor : soundEventAccessors) {
            accessor.resume();
        }
    }

    // --- Getters et Setters ---

    public String getCompositeEventName() {
        return compositeEventName;
    }

    public List<SoundEventAccessor> getSoundEventAccessors() {
        return soundEventAccessors;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * Retourne une représentation textuelle de l'accesseur composite d'événements sonores.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundEventAccessorComposite{" +
                "compositeEventName='" + compositeEventName + '\'' +
                ", soundEventAccessors=" + soundEventAccessors.size() +
                ", playMode=" + playMode +
                '}';
    }
}
