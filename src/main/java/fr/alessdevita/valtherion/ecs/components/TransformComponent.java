package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector3f;

/**
 * Composant pour la transformation d'une entité (position, rotation, échelle).
 */
public class TransformComponent implements Component {

    // Position de l'entité
    private Vector3f position;

    // Rotation de l'entité (en degrés)
    private Vector3f rotation;

    // Échelle de l'entité
    private Vector3f scale;

    // Entité associée
    private Entity entity;

    // Matrice de transformation (pour l'optimisation)
    private boolean isMatrixDirty;

    /**
     * Constructeur du composant de transformation.
     */
    public TransformComponent() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
        this.isMatrixDirty = true;
    }

    /**
     * Constructeur du composant de transformation avec une position.
     * @param position Position initiale.
     */
    public TransformComponent(Vector3f position) {
        this();
        this.position = position;
    }

    /**
     * Constructeur du composant de transformation avec une position et une rotation.
     * @param position Position initiale.
     * @param rotation Rotation initiale.
     */
    public TransformComponent(Vector3f position, Vector3f rotation) {
        this(position);
        this.rotation = rotation;
    }

    /**
     * Constructeur du composant de transformation avec une position, une rotation et une échelle.
     * @param position Position initiale.
     * @param rotation Rotation initiale.
     * @param scale Échelle initiale.
     */
    public TransformComponent(Vector3f position, Vector3f rotation, Vector3f scale) {
        this(position, rotation);
        this.scale = scale;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    // --- Méthodes pour modifier la transformation ---

    /**
     * Définit la position.
     * @param position Nouvelle position.
     */
    public void setPosition(Vector3f position) {
        this.position = position;
        isMatrixDirty = true;
    }

    /**
     * Définit la position.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        isMatrixDirty = true;
    }

    /**
     * Déplace l'entité.
     * @param dx Déplacement en X.
     * @param dy Déplacement en Y.
     * @param dz Déplacement en Z.
     */
    public void translate(float dx, float dy, float dz) {
        position.add(dx, dy, dz);
        isMatrixDirty = true;
    }

    /**
     * Définit la rotation.
     * @param rotation Nouvelle rotation.
     */
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        isMatrixDirty = true;
    }

    /**
     * Définit la rotation.
     * @param x Rotation en X (en degrés).
     * @param y Rotation en Y (en degrés).
     * @param z Rotation en Z (en degrés).
     */
    public void setRotation(float x, float y, float z) {
        this.rotation.set(x, y, z);
        isMatrixDirty = true;
    }

    /**
     * Fait tourner l'entité.
     * @param dx Rotation en X (en degrés).
     * @param dy Rotation en Y (en degrés).
     * @param dz Rotation en Z (en degrés).
     */
    public void rotate(float dx, float dy, float dz) {
        rotation.add(dx, dy, dz);
        isMatrixDirty = true;
    }

    /**
     * Définit l'échelle.
     * @param scale Nouvelle échelle.
     */
    public void setScale(Vector3f scale) {
        this.scale = scale;
        isMatrixDirty = true;
    }

    /**
     * Définit l'échelle.
     * @param x Échelle en X.
     * @param y Échelle en Y.
     * @param z Échelle en Z.
     */
    public void setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
        isMatrixDirty = true;
    }

    /**
     * Redimensionne l'entité.
     * @param dx Facteur d'échelle en X.
     * @param dy Facteur d'échelle en Y.
     * @param dz Facteur d'échelle en Z.
     */
    public void scale(float dx, float dy, float dz) {
        scale.add(dx, dy, dz);
        isMatrixDirty = true;
    }

    // --- Getters ---

    public Vector3f getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getRotationX() {
        return rotation.x;
    }

    public float getRotationY() {
        return rotation.y;
    }

    public float getRotationZ() {
        return rotation.z;
    }

    public Vector3f getScale() {
        return scale;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "TransformComponent{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", scale=" + scale +
                '}';
    }
}