package fr.alessdevita.valtherion.rendering;

import org.joml.Matrix4f;

/**
 * Interface pour les objets rendables dans Valtherion.
 * Définit les méthodes nécessaires pour rendre un objet.
 */
public interface Renderable {

    /**
     * Rend l'objet.
     * @param shader Shader à utiliser pour le rendu.
     * @param projectionMatrix Matrice de projection.
     * @param viewMatrix Matrice de vue.
     * @param modelMatrix Matrice de modèle.
     */
    void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix);

    /**
     * Rend l'objet avec une matrice de modèle personnalisée.
     * @param shader Shader à utiliser pour le rendu.
     * @param projectionMatrix Matrice de projection.
     * @param viewMatrix Matrice de vue.
     */
    default void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        render(shader, projectionMatrix, viewMatrix, new Matrix4f());
    }

    /**
     * Met à jour l'objet.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    default void update(float deltaTime) {}

    /**
     * Retourne la matrice de modèle de l'objet.
     * @return Matrice de modèle.
     */
    Matrix4f getModelMatrix();

    /**
     * Définit la matrice de modèle de l'objet.
     * @param modelMatrix Matrice de modèle.
     */
    void setModelMatrix(Matrix4f modelMatrix);

    /**
     * Retourne true si l'objet est visible.
     * @return true si l'objet est visible, false sinon.
     */
    default boolean isVisible() {
        return true;
    }

    /**
     * Définit la visibilité de l'objet.
     *
     * @param visible true pour rendre l'objet visible, false sinon.
     */
    default void setVisible(boolean visible) {

    }

    Material getMaterial();

    void render(Camera camera);
}