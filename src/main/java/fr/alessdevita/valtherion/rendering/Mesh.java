package fr.alessdevita.valtherion.rendering;

import fr.alessdevita.valtherion.utils.Vector2f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;
import java.util.*;

/**
 * Classe pour gérer les maillages 3D dans Valtherion.
 * Un maillage est composé de vertices, d'indices et d'un matériau.
 */
public class Mesh implements Renderable {

    // ID des VBO (Vertex Buffer Objects)
    private final int vaoId;
    private final int vboId;
    private final int eboId;

    // Nombre de vertices
    private final int vertexCount;

    // Nombre d'indices
    private final int indexCount;

    // Matériau du maillage
    private Material material;

    // Matrice de modèle du maillage
    private Matrix4f modelMatrix;

    // Indique si le maillage est visible
    private boolean isVisible;

    public void render() {

    }

    // Structure d'un vertex
    public static class Vertex {
        public Vector3f position;
        public Vector3f normal;
        public Vector2f textureCoords;
        public Vector3f tangent;
        public Vector3f bitangent;

        public Vertex(Vector3f position, Vector3f normal, Vector2f textureCoords) {
            this.position = position;
            this.normal = normal;
            this.textureCoords = textureCoords;
            this.tangent = new Vector3f();
            this.bitangent = new Vector3f();
        }
    }

    /**
     * Constructeur du maillage.
     * @param vertices Tableau de vertices.
     * @param indices Tableau d'indices.
     */
    public Mesh(Vertex[] vertices, int[] indices) {
        this(vertices, indices, Material.DEFAULT_OPAQUE);
    }

    /**
     * Constructeur du maillage avec un matériau.
     * @param vertices Tableau de vertices.
     * @param indices Tableau d'indices.
     * @param material Matériau du maillage.
     */
    public Mesh(Vertex[] vertices, int[] indices, Material material) {
        this.vertexCount = vertices.length;
        this.indexCount = indices.length;
        this.material = material;
        this.modelMatrix = new Matrix4f();
        this.isVisible = true;

        // Créer le VAO (Vertex Array Object)
        this.vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Créer le VBO (Vertex Buffer Object)
        this.vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

        // Calculer la taille d'un vertex en octets
        int vertexSize = (
                3 * Float.BYTES + // position (x, y, z)
                        3 * Float.BYTES + // normal (x, y, z)
                        2 * Float.BYTES   // textureCoords (u, v)
        );

        // Allouer la mémoire pour le VBO
        LongBuffer vertexBuffer = MemoryUtil.memAllocLong(vertexCount * vertexSize);
        for (Vertex vertex : vertices) {
            vertexBuffer.put(Float.floatToIntBits(vertex.position.x));
            vertexBuffer.put(Float.floatToIntBits(vertex.position.y));
            vertexBuffer.put(Float.floatToIntBits(vertex.position.z));
            vertexBuffer.put(Float.floatToIntBits(vertex.normal.x));
            vertexBuffer.put(Float.floatToIntBits(vertex.normal.y));
            vertexBuffer.put(Float.floatToIntBits(vertex.normal.z));
            vertexBuffer.put(Float.floatToIntBits(vertex.textureCoords.x));
            vertexBuffer.put(Float.floatToIntBits(vertex.textureCoords.y));
        }
        vertexBuffer.flip();

        // Charger les données dans le VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        // Définir les attributs des vertices
        int stride = vertexSize;
        int offset = 0;

        // Attribut de position (location = 0)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, offset);
        GL20.glEnableVertexAttribArray(0);
        offset += 3 * Float.BYTES;

        // Attribut de normale (location = 1)
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, offset);
        GL20.glEnableVertexAttribArray(1);
        offset += 3 * Float.BYTES;

        // Attribut de coordonnées de texture (location = 2)
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, offset);
        GL20.glEnableVertexAttribArray(2);

        // Créer l'EBO (Element Buffer Object)
        this.eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);

        // Charger les indices dans l'EBO
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        // Délier le VAO
        GL30.glBindVertexArray(0);

        // Libérer les buffers temporaires
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        if (!isVisible) {
            return;
        }

        // Appliquer le matériau au shader
        material.applyToShader(shader);

        // Lier les textures du matériau
        material.bindTextures();

        // Définir les matrices de modèle, vue et projection dans le shader
        shader.setUniform("model", this.modelMatrix.mul(modelMatrix, new Matrix4f()));
        shader.setUniform("view", viewMatrix);
        shader.setUniform("projection", projectionMatrix);

        // Lier le VAO
        GL30.glBindVertexArray(vaoId);

        // Dessiner le maillage
        GL11.glDrawElements(
                GL11.GL_TRIANGLES,
                indexCount,
                GL11.GL_UNSIGNED_INT,
                0
        );

        // Délier le VAO
        GL30.glBindVertexArray(0);

        // Délier les textures du matériau
        material.unbindTextures();
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

    // --- Getters et Setters ---

    public int getVaoId() {
        return vaoId;
    }

    public int getVboId() {
        return vboId;
    }

    public int getEboId() {
        return eboId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public void render(Camera camera) {

    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Met à jour le maillage.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {
        // À implémenter si nécessaire
    }

    /**
     * Libère les ressources du maillage.
     */
    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        GL15.glDeleteBuffers(eboId);
    }

    /**
     * Retourne une représentation textuelle du maillage.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Mesh{" +
                "vaoId=" + vaoId +
                ", vertexCount=" + vertexCount +
                ", indexCount=" + indexCount +
                ", material=" + material.getName() +
                '}';
    }

    // --- Méthodes statiques pour créer des maillages simples ---

    /**
     * Crée un maillage de cube.
     * @return Maillage de cube.
     */
    public static Mesh createCube() {
        Vertex[] vertices = {
                // Face avant
                new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),

                // Face arrière
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector2f(1.0f, 1.0f)),

                // Face gauche
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),

                // Face droite
                new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 1.0f)),

                // Face du haut
                new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(1.0f, 1.0f)),

                // Face du bas
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.0f, -1.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector3f(0.0f, -1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector3f(0.0f, -1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector3f(0.0f, -1.0f, 0.0f), new Vector2f(0.0f, 1.0f))
        };

        int[] indices = {
                // Face avant
                0, 1, 2,
                2, 3, 0,

                // Face arrière
                4, 5, 6,
                6, 7, 4,

                // Face gauche
                8, 9, 10,
                10, 11, 8,

                // Face droite
                12, 13, 14,
                14, 15, 12,

                // Face du haut
                16, 17, 18,
                18, 19, 16,

                // Face du bas
                20, 21, 22,
                22, 23, 20
        };

        return new Mesh(vertices, indices);
    }

    /**
     * Crée un maillage de plan.
     * @return Maillage de plan.
     */
    public static Mesh createPlane() {
        Vertex[] vertices = {
                new Vertex(new Vector3f(-0.5f, 0.0f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, 0.0f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex(new Vector3f( 0.5f, 0.0f,  0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f, 0.0f,  0.5f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f))
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        return new Mesh(vertices, indices);
    }
}