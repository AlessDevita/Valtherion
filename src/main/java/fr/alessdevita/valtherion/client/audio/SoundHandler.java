package fr.alessdevita.valtherion.client.audio;

import fr.alessdevita.valtherion.entity.Entity;

import java.util.*;

/**
 * Gestionnaire des sons dans Valtherion.
 * Gère les sons liés aux actions du jeu (pas, attaques, etc.).
 */
public class SoundHandler {

    // Instance unique du gestionnaire de sons
    private static SoundHandler instance;

    // Gestionnaire de sons principal
    private final SoundManager soundManager;

    // Map des sons liés aux entités (ID de l'entité -> liste de sons)
    private final Map<String, List<ISound>> entitySounds;

    // Map des sons liés aux actions (nom de l'action -> chemin du son)
    private final Map<String, String> actionSounds;

    /**
     * Constructeur privé pour le singleton.
     */
    public SoundHandler() {
        this.soundManager = SoundManager.getInstance();
        this.entitySounds = new HashMap<>();
        this.actionSounds = new HashMap<>();

        // Initialiser les sons par défaut pour les actions
        initDefaultActionSounds();
    }

    /**
     * Récupère l'instance unique du gestionnaire de sons.
     * @return Instance unique.
     */
    public static SoundHandler getInstance() {
        if (instance == null) {
            instance = new SoundHandler();
        }
        return instance;
    }

    /**
     * Initialise les sons par défaut pour les actions.
     */
    private void initDefaultActionSounds() {
        // Sons de pas
        actionSounds.put("walk", "sounds/entity/player/walk.ogg");
        actionSounds.put("run", "sounds/entity/player/run.ogg");

        // Sons d'attaques
        actionSounds.put("attack", "sounds/entity/player/attack.ogg");

        // Sons de blessures
        actionSounds.put("hurt", "sounds/entity/player/hurt.ogg");

        // Sons de mort
        actionSounds.put("death", "sounds/entity/player/death.ogg");

        // Sons de sauts
        actionSounds.put("jump", "sounds/entity/player/jump.ogg");
    }

    /**
     * Joue un son lié à une action.
     * @param action Nom de l'action.
     */
    public void playActionSound(String action) {
        String soundPath = actionSounds.get(action);
        if (soundPath != null) {
            soundManager.playSound(soundPath);
        }
    }

    /**
     * Joue un son lié à une action à une position spécifique.
     * @param action Nom de l'action.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void playActionSound(String action, float x, float y, float z) {
        String soundPath = actionSounds.get(action);
        if (soundPath != null) {
            soundManager.playSound(soundPath, x, y, z);
        }
    }

    /**
     * Joue un son lié à une action pour une entité.
     * @param action Nom de l'action.
     * @param entity Entité associée.
     */
    public void playActionSound(String action, Entity entity) {
        String soundPath = actionSounds.get(action);
        if (soundPath != null) {
            soundManager.playSound(soundPath, entity);
        }
    }

    /**
     * Joue un son de pas pour une entité.
     * @param entity Entité associée.
     */
    public void playWalkSound(Entity entity) {
        playActionSound("walk", entity);
    }

    /**
     * Joue un son de course pour une entité.
     * @param entity Entité associée.
     */
    public void playRunSound(Entity entity) {
        playActionSound("run", entity);
    }

    /**
     * Joue un son d'attaque pour une entité.
     * @param entity Entité associée.
     */
    public void playAttackSound(Entity entity) {
        playActionSound("attack", entity);
    }

    /**
     * Joue un son de blessure pour une entité.
     * @param entity Entité associée.
     */
    public void playHurtSound(Entity entity) {
        playActionSound("hurt", entity);
    }

    /**
     * Joue un son de mort pour une entité.
     * @param entity Entité associée.
     */
    public void playDeathSound(Entity entity) {
        playActionSound("death", entity);
    }

    /**
     * Joue un son de saut pour une entité.
     * @param entity Entité associée.
     */
    public void playJumpSound(Entity entity) {
        playActionSound("jump", entity);
    }

    /**
     * Ajoute un son à une entité.
     * @param entityId ID de l'entité.
     * @param sound Son à ajouter.
     */
    public void addSoundToEntity(String entityId, ISound sound) {
        entitySounds.computeIfAbsent(entityId, k -> new ArrayList<>()).add(sound);
    }

    /**
     * Supprime un son d'une entité.
     * @param entityId ID de l'entité.
     * @param sound Son à supprimer.
     */
    public void removeSoundFromEntity(String entityId, ISound sound) {
        List<ISound> sounds = entitySounds.get(entityId);
        if (sounds != null) {
            sounds.remove(sound);
        }
    }

    /**
     * Arrête tous les sons d'une entité.
     * @param entityId ID de l'entité.
     */
    public void stopEntitySounds(String entityId) {
        List<ISound> sounds = entitySounds.get(entityId);
        if (sounds != null) {
            for (ISound sound : sounds) {
                sound.stop();
            }
        }
    }

    /**
     * Met à jour tous les sons liés aux entités.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        soundManager.update(deltaTime);
    }

    /**
     * Définit un son pour une action.
     * @param action Nom de l'action.
     * @param soundPath Chemin du fichier son.
     */
    public void setActionSound(String action, String soundPath) {
        actionSounds.put(action, soundPath);
    }

    /**
     * Récupère le chemin du son pour une action.
     * @param action Nom de l'action.
     * @return Chemin du fichier son ou null si non trouvé.
     */
    public String getActionSound(String action) {
        return actionSounds.get(action);
    }

    /**
     * Nettoie les ressources du gestionnaire de sons.
     */
    public void cleanup() {
        entitySounds.clear();
        actionSounds.clear();
    }

    public void resumeSounds() {
    }

    public void stopAllSounds() {
    }

    public void setListener(Object entity, float partialTicks) {
    }

    // --- Getters ---

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Map<String, List<ISound>> getEntitySounds() {
        return entitySounds;
    }

    public Map<String, String> getActionSounds() {
        return actionSounds;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire de sons.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundHandler{" +
                "entitySounds=" + entitySounds.size() +
                ", actionSounds=" + actionSounds.size() +
                '}';
    }
}
