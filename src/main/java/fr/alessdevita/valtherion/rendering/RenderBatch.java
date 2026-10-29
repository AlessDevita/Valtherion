package fr.alessdevita.valtherion.rendering;

import org.joml.*;
import org.lwjgl.opengl.*;

import java.util.*;

/**
 * Classe pour gérer les lots de rendu dans Valtherion.
 * Regroupe plusieurs objets avec le même matériau ou shader pour optimiser le rendu.
 */
public class RenderBatch {

    // Shader utilisé pour le lot
    private Shader shader;

    // Matériau utilisé pour le lot
    private Material material;

    // Liste des objets rendables dans le lot
    private final List<Renderable> renderables;

    // Matrice de projection
    private Matrix4f projectionMatrix;

    // Matrice de vue
    private Matrix4f viewMatrix;

    // Indique si le lot est visible
    private boolean isVisible;

    /**
     * Constructeur du lot de rendu.
     * @param shader Shader utilisé pour le lot.
     */
    public RenderBatch(Shader shader) {
        this.shader = shader;
        this.renderables = new ArrayList<>();
        this.isVisible = true;
    }

    /**
     * Constructeur du lot de rendu avec un matériau.
     * @param shader Shader utilisé pour le lot.
     * @param material Matériau utilisé pour le lot.
     */
    public RenderBatch(Shader shader, Material material) {
        this(shader);
        this.material = material;
    }

    /**
     * Ajoute un objet rendable au lot.
     * @param renderable Objet rendable à ajouter.
     */
    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    /**
     * Retire un objet rendable du lot.
     * @param renderable Objet rendable à retirer.
     */
    public void removeRenderable(Renderable renderable) {
        renderables.remove(renderable);
    }

    /**
     * Rend le lot.
     */
    public void render() {
        if (!isVisible || renderables.isEmpty()) {
            return;
        }

        // Utiliser le shader du lot
        shader.bind();

        // Définir les matrices de projection et de vue dans le shader
        shader.setUniform("projection", projectionMatrix);
        shader.setUniform("view", viewMatrix);

        // Appliquer le matériau au shader si nécessaire
        if (material != null) {
            material.applyToShader(shader);
            material.bindTextures();
        }

        // Rendre chaque objet du lot
        for (Renderable renderable : renderables) {
            if (renderable.isVisible()) {
                Matrix4f modelMatrix = renderable.getModelMatrix();
                shader.setUniform("model", modelMatrix);

                // Si l'objet est un Mesh, utiliser la méthode de rendu optimisée
                if (renderable instanceof Mesh) {
                    Mesh mesh = (Mesh) renderable;
                    GL30.glBindVertexArray(mesh.getVaoId());
                    GL11.glDrawElements(
                            GL11.GL_TRIANGLES,
                            mesh.getIndexCount(),
                            GL11.GL_UNSIGNED_INT,
                            0
                    );
                    GL30.glBindVertexArray(0);
                } else {
                    // Sinon, utiliser la méthode de rendu standard
                    renderable.render(shader, projectionMatrix, viewMatrix, new Matrix4f());
                }
            }
        }

        // Délier le matériau si nécessaire
        if (material != null) {
            material.unbindTextures();
        }

        // Désactiver le shader
        shader.unbind();
    }

    /**
     * Met à jour le lot.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        for (Renderable renderable : renderables) {
            renderable.update(deltaTime);
        }
    }

    // --- Getters et Setters ---

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Matrix4f viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    /**
     * Libère les ressources du lot.
     */
    public void cleanup() {
        renderables.clear();
    }

    /**
     * Retourne une représentation textuelle du lot de rendu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "RenderBatch{" +
                "shader=" + (shader != null ? shader.getVertexShaderPath() : "null") +
                ", material=" + (material != null ? material.getName() : "null") +
                ", renderables=" + renderables.size() +
                '}';
    }
}