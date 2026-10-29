package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.LightComponent;
import fr.alessdevita.valtherion.rendering.Shader;

import org.joml.Vector3f;

import java.util.*;

/**
 * Système pour gérer les lumières des entités dans Valtherion.
 */
public class LightSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Shader utilisé pour les lumières
    private Shader shader;

    // Liste des entités avec un composant de lumière
    private List<Entity> lightEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Nombre maximal de lumières
    private int maxLights;

    /**
     * Constructeur du système de lumière.
     * @param shader Shader utilisé pour les lumières.
     */
    public LightSystem(Shader shader) {
        this.shader = shader;
        this.lightEntities = new ArrayList<>();
        this.isEnabled = true;
        this.maxLights = 16; // Nombre maximal de lumières supportées par le shader
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
        if (!isEnabled || ecsManager == null || shader == null) {
            return;
        }

        // Récupérer les entités avec les composants Light et Transform
        lightEntities = ecsManager.getEntitiesWithComponents(
                LightComponent.class,
                TransformComponent.class
        );

        // Mettre à jour les lumières dans le shader
        updateLightsInShader();
    }

    /**
     * Met à jour les lumières dans le shader.
     */
    private void updateLightsInShader() {
        // Limiter le nombre de lumières
        int lightCount = Math.min(lightEntities.size(), maxLights);

        // Définir le nombre de lumières dans le shader
        shader.bind();
        shader.setInt("lightCount", lightCount);

        // Définir les propriétés de chaque lumière
        for (int i = 0; i < lightCount; i++) {
            Entity entity = lightEntities.get(i);
            LightComponent lightComponent = entity.getComponent(LightComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            if (lightComponent != null && transformComponent != null && lightComponent.isEnabled()) {
                String lightPrefix = "lights[" + i + "].";

                // Définir le type de lumière
                shader.setInt(lightPrefix + "type", lightComponent.getType().ordinal());

                // Définir la position de la lumière
                shader.setVector3f(lightPrefix + "position", transformComponent.getPosition());

                // Définir la direction de la lumière
                shader.setVector3f(lightPrefix + "direction", lightComponent.getDirection());

                // Définir la couleur de la lumière
                shader.setVector3f(lightPrefix + "color", lightComponent.getColor());

                // Définir l'intensité de la lumière
                shader.setFloat(lightPrefix + "intensity", lightComponent.getIntensity());

                // Définir les paramètres d'atténuation (pour les lumières ponctuelles)
                shader.setFloat(lightPrefix + "constant", lightComponent.getConstant());
                shader.setFloat(lightPrefix + "linear", lightComponent.getLinear());
                shader.setFloat(lightPrefix + "quadratic", lightComponent.getQuadratic());

                // Définir les angles d'ouverture (pour les lumières spot)
                shader.setFloat(lightPrefix + "cutOff", lightComponent.getCutOff());
                shader.setFloat(lightPrefix + "outerCutOff", lightComponent.getOuterCutOff());
            }
        }

        shader.unbind();
    }

    /**
     * Ajoute une lumière à une entité.
     * @param entityId ID de l'entité.
     * @param lightComponent Composant de lumière.
     */
    public void addLight(String entityId, LightComponent lightComponent) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null) {
            entity.addComponent(lightComponent);
        }
    }

    /**
     * Retire une lumière d'une entité.
     * @param entityId ID de l'entité.
     */
    public void removeLight(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(LightComponent.class)) {
            entity.removeComponent(LightComponent.class);
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

    public int getMaxLights() {
        return maxLights;
    }

    public void setMaxLights(int maxLights) {
        this.maxLights = maxLights;
    }

    public List<Entity> getLightEntities() {
        return lightEntities;
    }
}