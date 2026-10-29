package fr.alessdevita.valtherion.rendering;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

/**
 * Classe pour gérer les modèles 3D dans Valtherion.
 * Un modèle est composé de plusieurs maillages et peut avoir une hiérarchie de nœuds.
 */
public class Model implements Renderable {

    // Liste des maillages du modèle
    private final List<Mesh> meshes;

    // Nom du modèle
    private String name;

    // Matrice de modèle du modèle
    private Matrix4f modelMatrix;

    // Indique si le modèle est visible
    private boolean isVisible;

    // Position du modèle
    private Vector3f position;

    // Rotation du modèle (en degrés)
    private Vector3f rotation;

    // Échelle du modèle
    private Vector3f scale;

    // Nœuds enfants (pour les modèles hiérarchiques)
    private final List<Model> children;

    // Nœud parent (pour les modèles hiérarchiques)
    private Model parent;

    /**
     * Constructeur du modèle.
     */
    public Model() {
        this.meshes = new ArrayList<>();
        this.name = "DefaultModel";
        this.modelMatrix = new Matrix4f();
        this.isVisible = true;
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
        this.children = new ArrayList<>();
    }

    /**
     * Constructeur du modèle avec un nom.
     * @param name Nom du modèle.
     */
    public Model(String name) {
        this();
        this.name = name;
    }

    /**
     * Ajoute un maillage au modèle.
     * @param mesh Maillage à ajouter.
     */
    public void addMesh(Mesh mesh) {
        meshes.add(mesh);
    }

    /**
     * Retire un maillage du modèle.
     * @param mesh Maillage à retirer.
     */
    public void removeMesh(Mesh mesh) {
        meshes.remove(mesh);
    }

    /**
     * Ajoute un nœud enfant au modèle.
     * @param child Nœud enfant à ajouter.
     */
    public void addChild(Model child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Retire un nœud enfant du modèle.
     * @param child Nœud enfant à retirer.
     */
    public void removeChild(Model child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * Définit le nœud parent du modèle.
     * @param parent Nœud parent.
     */
    public void setParent(Model parent) {
        this.parent = parent;
    }

    /**
     * Met à jour la matrice de modèle.
     */
    public void updateModelMatrix() {
        modelMatrix = new Matrix4f();
        modelMatrix.translate(position);
        modelMatrix.rotateX((float) Math.toRadians(rotation.x));
        modelMatrix.rotateY((float) Math.toRadians(rotation.y));
        modelMatrix.rotateZ((float) Math.toRadians(rotation.z));
        modelMatrix.scale(scale);

        // Appliquer la matrice du parent si nécessaire
        if (parent != null) {
            modelMatrix = parent.getModelMatrix().mul(modelMatrix, new Matrix4f());
        }
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        if (!isVisible) {
            return;
        }

        // Mettre à jour la matrice de modèle
        updateModelMatrix();

        // Appliquer la matrice de modèle parent si nécessaire
        Matrix4f currentModelMatrix = this.modelMatrix.mul(modelMatrix, new Matrix4f());

        // Rendre chaque maillage du modèle
        for (Mesh mesh : meshes) {
            mesh.render(shader, projectionMatrix, viewMatrix, currentModelMatrix);
        }

        // Rendre les nœuds enfants
        for (Model child : children) {
            child.render(shader, projectionMatrix, viewMatrix, currentModelMatrix);
        }
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Renderable.super.render(shader, projectionMatrix, viewMatrix);
    }

    @Override
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public void render(Camera camera) {

    }

    // --- Getters et Setters ---

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateModelMatrix();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        updateModelMatrix();
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        updateModelMatrix();
    }

    public List<Model> getChildren() {
        return children;
    }

    public Model getParent() {
        return parent;
    }

    /**
     * Met à jour le modèle.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {
        // Mettre à jour les nœuds enfants
        for (Model child : children) {
            child.update(deltaTime);
        }
    }

    /**
     * Libère les ressources du modèle.
     */
    public void cleanup() {
        for (Mesh mesh : meshes) {
            mesh.cleanup();
        }
        meshes.clear();

        for (Model child : children) {
            child.cleanup();
        }
        children.clear();
    }

    /**
     * Retourne une représentation textuelle du modèle.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", meshes=" + meshes.size() +
                ", children=" + children.size() +
                ", position=" + position +
                '}';
    }

    // --- Méthodes statiques pour créer des modèles simples ---

    /**
     * Crée un modèle de cube.
     * @return Modèle de cube.
     */
    public static Model createCube() {
        Model model = new Model("Cube");
        model.addMesh(Mesh.createCube());
        return model;
    }

    /**
     * Crée un modèle de plan.
     * @return Modèle de plan.
     */
    public static Model createPlane() {
        Model model = new Model("Plane");
        model.addMesh(Mesh.createPlane());
        return model;
    }

    public void setMaterial(Material material) {

    }

    public void render() {

    }
}