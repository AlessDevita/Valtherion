package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.TransformComponent;
import fr.alessdevita.valtherion.ecs.components.RenderComponent;
import fr.alessdevita.valtherion.rendering.Shader;
import fr.alessdevita.valtherion.rendering.Camera;

import org.joml.Matrix4f;

import java.util.*;

/**
 * Système pour rendre les entités dans Valtherion.
 */
public class RenderSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Shader utilisé pour le rendu
    private Shader shader;

    // Caméra utilisée pour le rendu
    private Camera camera;

    // Matrice de projection
    private Matrix4f projectionMatrix;

    // Liste des entités à rendre
    private List<Entity> renderableEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système de rendu.
     * @param shader Shader utilisé pour le rendu.
     * @param camera Caméra utilisée pour le rendu.
     */
    public RenderSystem(Shader shader, Camera camera) {
        this.shader = shader;
        this.camera = camera;
        this.projectionMatrix = new Matrix4f();
        this.renderableEntities = new ArrayList<>();
        this.isEnabled = true;
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
        if (camera != null) {
            projectionMatrix = camera.getProjectionMatrix();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null || shader == null || camera == null) {
            return;
        }

        // Récupérer les entités avec les composants Transform et Render
        renderableEntities = ecsManager.getEntitiesWithComponents(
                TransformComponent.class,
                RenderComponent.class
        );

        // Lier le shader
        shader.bind();

        // Définir la matrice de projection
        shader.setMatrix4f("projection", projectionMatrix);

        // Définir la matrice de vue
        shader.setMatrix4f("view", camera.getViewMatrix());

        // Rendre chaque entité
        for (Entity entity : renderableEntities) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            RenderComponent render = entity.getComponent(RenderComponent.class);

            if (transform != null && render != null && render.isVisible()) {
                // Calculer la matrice de modèle
                Matrix4f modelMatrix = new Matrix4f()
                        .translate(transform.getPosition())
                        .rotateX((float) Math.toRadians(transform.getRotationX()))
                        .rotateY((float) Math.toRadians(transform.getRotationY()))
                        .rotateZ((float) Math.toRadians(transform.getRotationZ()))
                        .scale(transform.getScale());

                // Définir la matrice de modèle dans le shader
                shader.setMatrix4f("model", modelMatrix);

                // Définir la couleur
                shader.setVector4f("color", render.getColor());

                // Lier les textures si nécessaire
                if (render.getTexture() != null) {
                    render.getTexture().bind();
                }

                // Rendre le mesh
                if (render.getMesh() != null) {
                    render.getMesh().render();
                }
            }
        }

        // Désactiver le shader
        shader.unbind();
    }

    @Override
    public void cleanup() {
        if (shader != null) {
            shader.cleanup();
        }
        renderableEntities.clear();
    }

    // --- Getters et Setters ---

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        if (camera != null) {
            projectionMatrix = camera.getProjectionMatrix();
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getRenderableEntities() {
        return renderableEntities;
    }
}