package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.ParticleComponent;
import fr.alessdevita.valtherion.ecs.components.ParticleComponent.Particle;
import fr.alessdevita.valtherion.rendering.Shader;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer les particules des entités dans Valtherion.
 */
public class ParticleSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Shader utilisé pour les particules
    private Shader shader;

    // Liste des entités avec un composant de particules
    private List<Entity> particleEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Nombre maximal de particules
    private int maxParticles;

    /**
     * Constructeur du système de particules.
     * @param shader Shader utilisé pour les particules.
     */
    public ParticleSystem(Shader shader) {
        this.shader = shader;
        this.particleEntities = new ArrayList<>();
        this.isEnabled = true;
        this.maxParticles = 1000;
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
    public void init() {
        if (shader != null) {
            shader.init();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec les composants Particle et Transform
        particleEntities = ecsManager.getEntitiesWithComponents(
                ParticleComponent.class,
                TransformComponent.class
        );

        // Mettre à jour les particules
        for (Entity entity : particleEntities) {
            ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
            if (particleComponent != null) {
                particleComponent.update(deltaTime);
            }
        }

        // Rendre les particules
        renderParticles();
    }

    /**
     * Rend les particules.
     */
    private void renderParticles() {
        if (shader == null) {
            return;
        }

        shader.bind();

        // Pour chaque entité avec des particules
        for (Entity entity : particleEntities) {
            ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            if (particleComponent != null && transformComponent != null) {
                // Rendre chaque particule
                for (Particle particle : particleComponent.getParticles()) {
                    if (particle != null && particle.isAlive()) {
                        // Calculer la matrice de modèle pour la particule
                        Vector3f particlePosition = new Vector3f(
                                transformComponent.getX() + particle.getPosition().x,
                                transformComponent.getY() + particle.getPosition().y,
                                transformComponent.getZ() + particle.getPosition().z
                        );

                        // Définir les propriétés de la particule dans le shader
                        shader.setVector3f("particlePosition", particlePosition);
                        shader.setVector3f("particleColor", particle.getColor());
                        shader.setFloat("particleSize", particle.getSize());

                        // Rendre la particule (ex: avec un mesh de particule)
                        // Exemple : particleMesh.render();
                    }
                }
            }
        }

        shader.unbind();
    }

    /**
     * Active l'émission de particules pour une entité.
     * @param entityId ID de l'entité.
     */
    public void startEmitting(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ParticleComponent.class)) {
            ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
            particleComponent.startEmitting();
        }
    }

    /**
     * Désactive l'émission de particules pour une entité.
     * @param entityId ID de l'entité.
     */
    public void stopEmitting(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ParticleComponent.class)) {
            ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
            particleComponent.stopEmitting();
        }
    }

    /**
     * Efface toutes les particules d'une entité.
     * @param entityId ID de l'entité.
     */
    public void clearParticles(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ParticleComponent.class)) {
            ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
            particleComponent.clearParticles();
        }
    }

    // --- Getters et Setters ---

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    public List<Entity> getParticleEntities() {
        return particleEntities;
    }
}