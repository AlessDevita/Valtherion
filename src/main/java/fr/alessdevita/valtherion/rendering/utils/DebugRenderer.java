package fr.alessdevita.valtherion.rendering.utils;

import fr.alessdevita.valtherion.rendering.*;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.opengl.*;

import java.util.*;

/**
 * Classe pour le rendu de débogage dans Valtherion.
 * Permet de rendre des lignes, des points, et des formes simples pour le débogage.
 */
public class DebugRenderer implements Renderable {

    // Shader pour le rendu de débogage
    private Shader debugShader;

    // VAO et VBO pour les lignes
    private int lineVaoId;
    private int lineVboId;

    // VAO et VBO pour les points
    private int pointVaoId;
    private int pointVboId;

    // Liste des lignes à rendre
    private final List<DebugLine> lines;

    // Liste des points à rendre
    private final List<DebugPoint> points;

    // Matrice de modèle
    private Matrix4f modelMatrix;

    // Indique si le rendu de débogage est visible
    private boolean isVisible;

    // Taille des points
    private float pointSize;

    // Épaisseur des lignes
    private float lineWidth;

    // --- Classes internes pour les lignes et les points ---

    /**
     * Ligne de débogage.
     */
    private static class DebugLine {
        Vector3f start;
        Vector3f end;
        Vector4f color;

        DebugLine(Vector3f start, Vector3f end, Vector4f color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    /**
     * Point de débogage.
     */
    private static class DebugPoint {
        Vector3f position;
        Vector4f color;

        DebugPoint(Vector3f position, Vector4f color) {
            this.position = position;
            this.color = color;
        }
    }

    /**
     * Constructeur du rendu de débogage.
     */
    public DebugRenderer() {
        this.lines = new ArrayList<>();
        this.points = new ArrayList<>();
        this.modelMatrix = new Matrix4f();
        this.isVisible = true;
        this.pointSize = 5.0f;
        this.lineWidth = 2.0f;

        try {
            // Charger le shader de débogage
            this.debugShader = new Shader(
                    "shaders/debug_vertex.glsl",
                    "shaders/debug_fragment.glsl"
            );

            // Créer le VAO et le VBO pour les lignes
            createLineMesh();

            // Créer le VAO et le VBO pour les points
            createPointMesh();
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le DebugRenderer : " + e.getMessage(), e);
        }
    }

    /**
     * Crée le maillage pour les lignes.
     */
    private void createLineMesh() {
        // Créer le VAO
        lineVaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(lineVaoId);

        // Créer le VBO
        lineVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, lineVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 0, GL15.GL_DYNAMIC_DRAW);

        // Définir les attributs des vertices
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Délier le VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Crée le maillage pour les points.
     */
    private void createPointMesh() {
        // Créer le VAO
        pointVaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(pointVaoId);

        // Créer le VBO
        pointVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pointVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 0, GL15.GL_DYNAMIC_DRAW);

        // Définir les attributs des vertices
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Délier le VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Ajoute une ligne.
     * @param start Point de départ.
     * @param end Point d'arrivée.
     * @param color Couleur de la ligne.
     */
    public void addLine(Vector3f start, Vector3f end, Vector4f color) {
        lines.add(new DebugLine(start, end, color));
    }

    /**
     * Ajoute une ligne avec une couleur par défaut.
     * @param start Point de départ.
     * @param end Point d'arrivée.
     */
    public void addLine(Vector3f start, Vector3f end) {
        addLine(start, end, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
    }

    /**
     * Ajoute un point.
     * @param position Position du point.
     * @param color Couleur du point.
     */
    public void addPoint(Vector3f position, Vector4f color) {
        points.add(new DebugPoint(position, color));
    }

    /**
     * Ajoute un point avec une couleur par défaut.
     * @param position Position du point.
     */
    public void addPoint(Vector3f position) {
        addPoint(position, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
    }

    /**
     * Ajoute un axe 3D.
     * @param position Position de l'origine de l'axe.
     * @param size Taille de l'axe.
     */
    public void addAxis(Vector3f position, float size) {
        // Axe X (rouge)
        addLine(position, new Vector3f(position.x + size, position.y, position.z), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));

        // Axe Y (vert)
        addLine(position, new Vector3f(position.x, position.y + size, position.z), new Vector4f(0.0f, 1.0f, 0.0f, 1.0f));

        // Axe Z (bleu)
        addLine(position, new Vector3f(position.x, position.y, position.z + size), new Vector4f(0.0f, 0.0f, 1.0f, 1.0f));
    }

    /**
     * Ajoute un cube.
     * @param position Position du centre du cube.
     * @param size Taille du cube.
     * @param color Couleur du cube.
     */
    public void addCube(Vector3f position, float size, Vector4f color) {
        float halfSize = size / 2.0f;

        // Face avant
        addLine(
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z + halfSize),
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z + halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z + halfSize),
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z + halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z + halfSize),
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z + halfSize),
                color
        );
        addLine(
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z + halfSize),
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z + halfSize),
                color
        );

        // Face arrière
        addLine(
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z - halfSize),
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z - halfSize),
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z - halfSize),
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z - halfSize),
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z - halfSize),
                color
        );

        // Arêtes de connexion
        addLine(
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z + halfSize),
                new Vector3f(position.x - halfSize, position.y - halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z + halfSize),
                new Vector3f(position.x + halfSize, position.y - halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z + halfSize),
                new Vector3f(position.x + halfSize, position.y + halfSize, position.z - halfSize),
                color
        );
        addLine(
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z + halfSize),
                new Vector3f(position.x - halfSize, position.y + halfSize, position.z - halfSize),
                color
        );
    }

    /**
     * Ajoute un cercle 2D.
     * @param center Centre du cercle.
     * @param radius Rayon du cercle.
     * @param segments Nombre de segments.
     * @param color Couleur du cercle.
     */
    public void addCircle(Vector2f center, float radius, int segments, Vector4f color) {
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (i * 2 * Math.PI / segments);
            float angle2 = (float) ((i + 1) * 2 * Math.PI / segments);

            Vector2f point1 = new Vector2f(
                    center.x + (float) Math.cos(angle1) * radius,
                    center.y + (float) Math.sin(angle1) * radius
            );
            Vector2f point2 = new Vector2f(
                    center.x + (float) Math.cos(angle2) * radius,
                    center.y + (float) Math.sin(angle2) * radius
            );

            addLine(
                    new Vector3f(point1.x, point1.y, 0.0f),
                    new Vector3f(point2.x, point2.y, 0.0f),
                    color
            );
        }
    }

    /**
     * Efface toutes les lignes et les points.
     */
    public void clear() {
        lines.clear();
        points.clear();
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        if (!isVisible || (lines.isEmpty() && points.isEmpty())) {
            return;
        }

        // Utiliser le shader de débogage
        debugShader.bind();

        // Définir les matrices dans le shader
        debugShader.setUniform("projection", projectionMatrix);
        debugShader.setUniform("view", viewMatrix);
        debugShader.setUniform("model", this.modelMatrix.mul(modelMatrix, new Matrix4f()));

        // Rendre les lignes
        if (!lines.isEmpty()) {
            renderLines();
        }

        // Rendre les points
        if (!points.isEmpty()) {
            renderPoints();
        }

        // Désactiver le shader
        debugShader.unbind();
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Renderable.super.render(shader, projectionMatrix, viewMatrix);
    }

    @Override
    public void update(float deltaTime) {
        Renderable.super.update(deltaTime);
    }

    /**
     * Rend les lignes.
     */
    private void renderLines() {
        // Préparer les données des lignes
        float[] lineVertices = new float[lines.size() * 6];
        int index = 0;
        for (DebugLine line : lines) {
            lineVertices[index++] = line.start.x;
            lineVertices[index++] = line.start.y;
            lineVertices[index++] = line.start.z;
            lineVertices[index++] = line.color.x;
            lineVertices[index++] = line.color.y;
            lineVertices[index++] = line.color.z;

            lineVertices[index++] = line.end.x;
            lineVertices[index++] = line.end.y;
            lineVertices[index++] = line.end.z;
            lineVertices[index++] = line.color.x;
            lineVertices[index++] = line.color.y;
            lineVertices[index++] = line.color.z;
        }

        // Mettre à jour le VBO des lignes
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, lineVboId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, lineVertices);

        // Lier le VAO des lignes
        GL30.glBindVertexArray(lineVaoId);

        // Définir l'épaisseur des lignes
        GL11.glLineWidth(lineWidth);

        // Dessiner les lignes
        GL11.glDrawArrays(GL11.GL_LINES, 0, lines.size() * 2);

        // Réinitialiser l'épaisseur des lignes
        GL11.glLineWidth(1.0f);

        // Délier le VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Rend les points.
     */
    private void renderPoints() {
        // Préparer les données des points
        float[] pointVertices = new float[points.size() * 6];
        int index = 0;
        for (DebugPoint point : points) {
            pointVertices[index++] = point.position.x;
            pointVertices[index++] = point.position.y;
            pointVertices[index++] = point.position.z;
            pointVertices[index++] = point.color.x;
            pointVertices[index++] = point.color.y;
            pointVertices[index++] = point.color.z;
        }

        // Mettre à jour le VBO des points
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pointVboId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, pointVertices);

        // Lier le VAO des points
        GL30.glBindVertexArray(pointVaoId);

        // Définir la taille des points
        GL11.glPointSize(pointSize);

        // Dessiner les points
        GL11.glDrawArrays(GL11.GL_POINTS, 0, points.size());

        // Réinitialiser la taille des points
        GL11.glPointSize(1.0f);

        // Délier le VAO
        GL30.glBindVertexArray(0);
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

    public float getPointSize() {
        return pointSize;
    }

    public void setPointSize(float pointSize) {
        this.pointSize = Math.max(1.0f, pointSize);
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = Math.max(1.0f, lineWidth);
    }

    /**
     * Libère les ressources du rendu de débogage.
     */
    public void cleanup() {
        GL30.glDeleteVertexArrays(lineVaoId);
        GL15.glDeleteBuffers(lineVboId);
        GL30.glDeleteVertexArrays(pointVaoId);
        GL15.glDeleteBuffers(pointVboId);
        if (debugShader != null) {
            debugShader.cleanup();
        }
        lines.clear();
        points.clear();
    }

    /**
     * Retourne une représentation textuelle du rendu de débogage.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "DebugRenderer{" +
                "lines=" + lines.size() +
                ", points=" + points.size() +
                ", isVisible=" + isVisible +
                '}';
    }
}