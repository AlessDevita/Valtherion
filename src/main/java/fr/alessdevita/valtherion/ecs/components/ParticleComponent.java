package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector3f;

import java.util.*;

/**
 * Composant pour les particules d'une entité dans Valtherion.
 * Stocke les propriétés des particules (vitesse, durée de vie, taille, etc.).
 */
public class ParticleComponent implements Component {

    // Liste des particules
    private final List<Particle> particles;

    // Nombre maximal de particules
    private int maxParticles;

    // Vitesse d'émission des particules (particules par seconde)
    private float emissionRate;

    // Durée de vie des particules (en secondes)
    private float particleLifetime;

    // Vitesse initiale des particules
    private Vector3f particleVelocity;

    // Variation de la vitesse des particules
    private Vector3f velocityVariation;

    // Taille initiale des particules
    private float particleSize;

    // Variation de la taille des particules
    private float sizeVariation;

    // Couleur des particules
    private Vector3f particleColor;

    // Variation de la couleur des particules
    private Vector3f colorVariation;

    // Gravité appliquée aux particules
    private Vector3f gravity;

    // Indique si le système de particules est actif
    private boolean isEmitting;

    // Temps écoulé depuis la dernière émission (en secondes)
    private float timeSinceLastEmission;

    // Entité associée
    private Entity entity;

    // Classe pour représenter une particule
    public static class Particle {
        private Vector3f position;
        private Vector3f velocity;
        private float size;
        private Vector3f color;
        private float lifetime;
        private float timeAlive;

        public Particle(Vector3f position, Vector3f velocity, float size, Vector3f color, float lifetime) {
            this.position = position;
            this.velocity = velocity;
            this.size = size;
            this.color = color;
            this.lifetime = lifetime;
            this.timeAlive = 0.0f;
        }

        public void update(float deltaTime) {
            timeAlive += deltaTime;
            position.add(new Vector3f(velocity).mul(deltaTime));
        }

        public boolean isAlive() {
            return timeAlive < lifetime;
        }

        // --- Getters et Setters ---
        public Vector3f getPosition() {
            return position;
        }

        public Vector3f getVelocity() {
            return velocity;
        }

        public float getSize() {
            return size;
        }

        public Vector3f getColor() {
            return color;
        }

        public float getLifetime() {
            return lifetime;
        }

        public float getTimeAlive() {
            return timeAlive;
        }
    }

    /**
     * Constructeur du composant de particules.
     */
    public ParticleComponent() {
        this.particles = new ArrayList<>();
        this.maxParticles = 100;
        this.emissionRate = 10.0f;
        this.particleLifetime = 1.0f;
        this.particleVelocity = new Vector3f(0, 1, 0);
        this.velocityVariation = new Vector3f(0.5f, 0.5f, 0.5f);
        this.particleSize = 0.1f;
        this.sizeVariation = 0.05f;
        this.particleColor = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc
        this.colorVariation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.gravity = new Vector3f(0, -9.81f, 0);
        this.isEmitting = true;
        this.timeSinceLastEmission = 0.0f;
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
        if (!isEmitting) {
            return;
        }

        // Mettre à jour le temps depuis la dernière émission
        timeSinceLastEmission += deltaTime;

        // Émettre de nouvelles particules
        float emissionInterval = 1.0f / emissionRate;
        while (timeSinceLastEmission >= emissionInterval && particles.size() < maxParticles) {
            emitParticle();
            timeSinceLastEmission -= emissionInterval;
        }

        // Mettre à jour les particules existantes
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.update(deltaTime);

            // Appliquer la gravité
            particle.getVelocity().add(new Vector3f(gravity).mul(deltaTime));

            // Retirer les particules mortes
            if (!particle.isAlive()) {
                particles.remove(i);
            }
        }
    }

    /**
     * Émet une nouvelle particule.
     */
    private void emitParticle() {
        if (entity == null || !entity.hasComponent(TransformComponent.class)) {
            return;
        }

        TransformComponent transform = entity.getComponent(TransformComponent.class);
        Vector3f entityPosition = transform.getPosition();

        // Calculer la position de la particule
        Vector3f position = new Vector3f(entityPosition);

        // Calculer la vélocité de la particule avec variation
        Vector3f velocity = new Vector3f(
                particleVelocity.x + (float) (Math.random() * 2 - 1) * velocityVariation.x,
                particleVelocity.y + (float) (Math.random() * 2 - 1) * velocityVariation.y,
                particleVelocity.z + (float) (Math.random() * 2 - 1) * velocityVariation.z
        );

        // Calculer la taille de la particule avec variation
        float size = particleSize + (float) (Math.random() * 2 - 1) * sizeVariation;

        // Calculer la couleur de la particule avec variation
        Vector3f color = new Vector3f(
                Math.max(0, Math.min(1, particleColor.x + (float) (Math.random() * 2 - 1) * colorVariation.x)),
                Math.max(0, Math.min(1, particleColor.y + (float) (Math.random() * 2 - 1) * colorVariation.y)),
                Math.max(0, Math.min(1, particleColor.z + (float) (Math.random() * 2 - 1) * colorVariation.z))
        );

        // Créer la particule
        Particle particle = new Particle(position, velocity, size, color, particleLifetime);
        particles.add(particle);
    }

    /**
     * Arrête l'émission de particules.
     */
    public void stopEmitting() {
        isEmitting = false;
    }

    /**
     * Recommence l'émission de particules.
     */
    public void startEmitting() {
        isEmitting = true;
    }

    /**
     * Efface toutes les particules.
     */
    public void clearParticles() {
        particles.clear();
    }

    // --- Getters et Setters ---

    public List<Particle> getParticles() {
        return particles;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    public float getEmissionRate() {
        return emissionRate;
    }

    public void setEmissionRate(float emissionRate) {
        this.emissionRate = emissionRate;
    }

    public float getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    public Vector3f getParticleVelocity() {
        return particleVelocity;
    }

    public void setParticleVelocity(Vector3f particleVelocity) {
        this.particleVelocity = particleVelocity;
    }

    public Vector3f getVelocityVariation() {
        return velocityVariation;
    }

    public void setVelocityVariation(Vector3f velocityVariation) {
        this.velocityVariation = velocityVariation;
    }

    public float getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(float particleSize) {
        this.particleSize = particleSize;
    }

    public float getSizeVariation() {
        return sizeVariation;
    }

    public void setSizeVariation(float sizeVariation) {
        this.sizeVariation = sizeVariation;
    }

    public Vector3f getParticleColor() {
        return particleColor;
    }

    public void setParticleColor(Vector3f particleColor) {
        this.particleColor = particleColor;
    }

    public Vector3f getColorVariation() {
        return colorVariation;
    }

    public void setColorVariation(Vector3f colorVariation) {
        this.colorVariation = colorVariation;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
    }

    public boolean isEmitting() {
        return isEmitting;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ParticleComponent{" +
                "particles=" + particles.size() +
                ", maxParticles=" + maxParticles +
                ", emissionRate=" + emissionRate +
                ", isEmitting=" + isEmitting +
                '}';
    }
}