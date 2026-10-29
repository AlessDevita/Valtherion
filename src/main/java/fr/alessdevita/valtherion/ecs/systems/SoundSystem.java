package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.SoundComponent;

import java.util.*;

/**
 * Système pour gérer les sons des entités dans Valtherion.
 */
public class SoundSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant de son
    private List<Entity> soundEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Volume global (0.0 à 1.0)
    private float globalVolume;

    /**
     * Constructeur du système de son.
     */
    public SoundSystem() {
        this.soundEntities = new ArrayList<>();
        this.isEnabled = true;
        this.globalVolume = 1.0f;
    }

    @Override
    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    @Override
    public ECSManager getECSManager() {
        return ecsManager;
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec les composants Sound et Transform
        soundEntities = ecsManager.getEntitiesWithComponents(
                SoundComponent.class,
                TransformComponent.class
        );

        // Mettre à jour les sons
        for (Entity entity : soundEntities) {
            SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
            if (soundComponent != null && soundComponent.isPlaying()) {
                // Mettre à jour la position du son (pour les sons 3D)
                TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
                if (transformComponent != null) {
                    // Logique pour mettre à jour la position du son
                    // Exemple : SoundManager.updateSoundPosition(soundComponent.getSoundId(), transformComponent.getPosition());
                }
            }
        }
    }

    /**
     * Joue un son pour une entité.
     * @param entityId ID de l'entité.
     * @param soundPath Chemin du fichier audio.
     */
    public void playSound(String entityId, String soundPath) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null) {
            SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
            if (soundComponent == null) {
                soundComponent = new SoundComponent(soundPath);
                entity.addComponent(soundComponent);
            }
            soundComponent.setSoundPath(soundPath);
            soundComponent.play();
        }
    }

    /**
     * Arrête un son pour une entité.
     * @param entityId ID de l'entité.
     */
    public void stopSound(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(SoundComponent.class)) {
            SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
            soundComponent.stop();
        }
    }

    /**
     * Met en pause un son pour une entité.
     * @param entityId ID de l'entité.
     */
    public void pauseSound(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(SoundComponent.class)) {
            SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
            soundComponent.pause();
        }
    }

    /**
     * Reprend un son pour une entité.
     * @param entityId ID de l'entité.
     */
    public void resumeSound(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(SoundComponent.class)) {
            SoundComponent soundComponent = entity.getComponent(SoundComponent.class);
            soundComponent.resume();
        }
    }

    /**
     * Définit le volume global.
     * @param volume Volume global (0.0 à 1.0).
     */
    public void setGlobalVolume(float volume) {
        this.globalVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public float getGlobalVolume() {
        return globalVolume;
    }

    public List<Entity> getSoundEntities() {
        return soundEntities;
    }
}