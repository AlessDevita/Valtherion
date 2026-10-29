package fr.alessdevita.valtherion.client.audio;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;

/**
 * Classe pour les sons en mouvement dans Valtherion.
 * Représente un son lié à une entité en mouvement (ex: pas d'un joueur, bruit d'un mob).
 */
public class MovingSound implements ITickableSound {

    // Entité associée au son
    private Entity entity;

    // Chemin du fichier son
    private String soundPath;

    // Volume du son
    private float volume;

    // Pitch du son
    private float pitch;

    // Catégorie du son
    private SoundCategory category;

    // Indique si le son est en boucle
    private boolean isLooping;

    // Indique si le son est en cours de lecture
    private boolean isPlaying;

    // Indique si le son doit être mis à jour
    private boolean isTickable;

    // ID du son (pour le gestionnaire audio)
    private int soundId;

    /**
     * Constructeur du son en mouvement.
     * @param soundPath Chemin du fichier son.
     * @param entity Entité associée au son.
     */
    public MovingSound(String soundPath, Entity entity) {
        this.soundPath = soundPath;
        this.entity = entity;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.category = SoundCategory.ENTITIES;
        this.isLooping = false;
        this.isPlaying = false;
        this.isTickable = true;
    }

    @Override
    public void play() {
        if (entity != null) {
            Vector3f position = entity.getPosition();
            System.out.println("Jouer le son : " + soundPath + " à la position (" +
                    position.x() + ", " + position.y() + ", " + position.z() + ")");
        }
        isPlaying = true;
    }

    @Override
    public void stop() {
        System.out.println("Arrêter le son : " + soundPath);
        isPlaying = false;
    }

    @Override
    public void pause() {
        System.out.println("Mettre en pause le son : " + soundPath);
    }

    @Override
    public void resume() {
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
        // Ignorer, car la position est liée à l'entité
    }

    @Override
    public float[] getPosition() {
        if (entity != null) {
            Vector3f position = entity.getPosition();
            return new float[] { position.x(), position.y(), position.z() };
        }
        return new float[] { 0.0f, 0.0f, 0.0f };
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
    public void tick(float deltaTime) {
        if (isTickable && entity != null) {
            // Mettre à jour la position du son en fonction de l'entité
            Vector3f position = entity.getPosition();
            System.out.println("Mettre à jour le son : " + soundPath + " à (" +
                    position.x() + ", " + position.y() + ", " + position.z() + ")");
        }
    }

    @Override
    public void setTickable(boolean tickable) {
        this.isTickable = tickable;
    }

    @Override
    public boolean isTickable() {
        return isTickable;
    }

    @Override
    public void cleanup() {
        System.out.println("Nettoyer le son : " + soundPath);
    }

    // --- Getters et Setters supplémentaires ---

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

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
     * Retourne une représentation textuelle du son en mouvement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "MovingSound{" +
                "soundPath='" + soundPath + '\'' +
                ", volume=" + volume +
                ", pitch=" + pitch +
                ", category=" + category +
                ", isLooping=" + isLooping +
                ", isPlaying=" + isPlaying +
                ", isTickable=" + isTickable +
                '}';
    }
}
