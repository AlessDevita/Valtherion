package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

import java.util.*;

/**
 * Composant pour les animations d'une entité dans Valtherion.
 * Stocke les animations, l'état actuel, et les propriétés d'animation.
 */
public class AnimationComponent implements Component {

    // Map des animations (nom -> animation)
    private final Map<String, Animation> animations;

    // Animation actuelle
    private Animation currentAnimation;

    // État de l'animation
    private AnimationState state;

    // Vitesse de l'animation
    private float speed;

    // Temps écoulé dans l'animation actuelle (en secondes)
    private float currentTime;

    // Indique si l'animation est en boucle
    private boolean isLooping;

    // Entité associée
    private Entity entity;

    // Énumération des états d'animation
    public enum AnimationState {
        IDLE,       // Inactif
        PLAYING,    // En cours de lecture
        PAUSED,     // En pause
        STOPPED     // Arrêté
    }

    // Classe pour représenter une animation
    public static class Animation {
        private final String name;
        private final float duration; // Durée en secondes
        private final int frameCount; // Nombre de frames
        private final List<Integer> frames; // Liste des indices des frames

        public Animation(String name, float duration, int frameCount) {
            this.name = name;
            this.duration = duration;
            this.frameCount = frameCount;
            this.frames = new ArrayList<>();
            for (int i = 0; i < frameCount; i++) {
                frames.add(i);
            }
        }

        public Animation(String name, float duration, List<Integer> frames) {
            this.name = name;
            this.duration = duration;
            this.frameCount = frames.size();
            this.frames = frames;
        }

        public String getName() {
            return name;
        }

        public float getDuration() {
            return duration;
        }

        public int getFrameCount() {
            return frameCount;
        }

        public List<Integer> getFrames() {
            return frames;
        }

        public int getFrame(float time) {
            if (frames.isEmpty()) {
                return 0;
            }

            float frameTime = duration / frameCount;
            int frameIndex = (int) (time / frameTime) % frameCount;
            return frames.get(frameIndex);
        }
    }

    /**
     * Constructeur du composant d'animation.
     */
    public AnimationComponent() {
        this.animations = new HashMap<>();
        this.currentAnimation = null;
        this.state = AnimationState.STOPPED;
        this.speed = 1.0f;
        this.currentTime = 0.0f;
        this.isLooping = false;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void update(float deltaTime) {
        if (state != AnimationState.PLAYING || currentAnimation == null) {
            return;
        }

        // Mettre à jour le temps écoulé
        currentTime += deltaTime * speed;

        // Vérifier si l'animation est terminée
        if (currentTime >= currentAnimation.getDuration()) {
            if (isLooping) {
                currentTime = 0.0f;
            } else {
                state = AnimationState.STOPPED;
            }
        }
    }

    /**
     * Ajoute une animation.
     * @param name Nom de l'animation.
     * @param animation Animation à ajouter.
     */
    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    /**
     * Joue une animation.
     * @param name Nom de l'animation.
     */
    public void playAnimation(String name) {
        Animation animation = animations.get(name);
        if (animation != null) {
            currentAnimation = animation;
            currentTime = 0.0f;
            state = AnimationState.PLAYING;
        }
    }

    /**
     * Arrête l'animation actuelle.
     */
    public void stopAnimation() {
        state = AnimationState.STOPPED;
        currentTime = 0.0f;
    }

    /**
     * Met l'animation en pause.
     */
    public void pauseAnimation() {
        if (state == AnimationState.PLAYING) {
            state = AnimationState.PAUSED;
        }
    }

    /**
     * Reprend l'animation.
     */
    public void resumeAnimation() {
        if (state == AnimationState.PAUSED) {
            state = AnimationState.PLAYING;
        }
    }

    /**
     * Récupère la frame actuelle de l'animation.
     * @return Indice de la frame actuelle.
     */
    public int getCurrentFrame() {
        if (currentAnimation == null) {
            return 0;
        }
        return currentAnimation.getFrame(currentTime);
    }

    // --- Getters et Setters ---

    public Map<String, Animation> getAnimations() {
        return animations;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public AnimationState getState() {
        return state;
    }

    public void setState(AnimationState state) {
        this.state = state;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "AnimationComponent{" +
                "currentAnimation=" + (currentAnimation != null ? currentAnimation.getName() : "null") +
                ", state=" + state +
                ", speed=" + speed +
                '}';
    }
}