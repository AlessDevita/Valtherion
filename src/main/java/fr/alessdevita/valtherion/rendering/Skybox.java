package fr.alessdevita.valtherion.rendering;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

/**
 * Classe pour gérer le skybox dans Valtherion.
 * Le skybox est un cube texturé avec 6 faces (haut, bas, gauche, droite, avant, arrière).
 */
public class Skybox implements Renderable {

    // ID de la texture cubique OpenGL
    private int textureId;

    // ID du VAO (Vertex Array Object)
    private int vaoId;

    // ID du VBO (Vertex Buffer Object)
    private int vboId;

    // ID du shader du skybox
    private Shader skyboxShader;

    // Chemin du dossier des textures du skybox
    private String texturePath;

    // Extensions des fichiers de texture
    private String textureExtension;

    // Matrice de modèle du skybox
    private Matrix4f modelMatrix;

    // Indique si le skybox est visible
    private boolean isVisible;

    // Taille du skybox
    private float size;

    /**
     * Constructeur du skybox.
     * @param texturePath Chemin du dossier des textures du skybox.
     */
    public Skybox(String texturePath) {
        this(texturePath, ".png");
    }

    /**
     * Constructeur du skybox avec une extension de fichier.
     * @param texturePath Chemin du dossier des textures du skybox.
     * @param textureExtension Extension des fichiers de texture.
     */
    public Skybox(String texturePath, String textureExtension) {
        this.texturePath = texturePath;
        this.textureExtension = textureExtension;
        this.modelMatrix = new Matrix4f();
        this.isVisible = true;
        this.size = 100.0f;

        try {
            // Charger la texture cubique
            this.textureId = loadCubemap();

            // Charger le shader du skybox
            this.skyboxShader = new Shader(
                    "shaders/skybox_vertex.glsl",
                    "shaders/skybox_fragment.glsl"
            );

            // Créer le VAO et le VBO pour le cube du skybox
            createSkyboxMesh();
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le skybox : " + e.getMessage(), e);
        }
    }

    /**
     * Charge une texture cubique.
     * @return ID de la texture cubique OpenGL.
     * @throws IOException Si une erreur se produit lors du chargement.
     */
    private int loadCubemap() throws IOException {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);

        // Faces de la texture cubique
        String[] faces = {
                "right",   // Face droite
                "left",    // Face gauche
                "top",      // Face du haut
                "bottom",  // Face du bas
                "front",   // Face avant
                "back"     // Face arrière
        };

        for (int i = 0; i < faces.length; i++) {
            String facePath = texturePath + faces[i] + textureExtension;

            // Charger l'image avec STB Image
            IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
            IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

            ByteBuffer imageData = STBImage.stbi_load(
                    facePath,
                    widthBuffer,
                    heightBuffer,
                    channelsBuffer,
                    STBImage.STBI_rgb_alpha
            );

            if (imageData == null) {
                throw new IOException("Impossible de charger la texture du skybox : " + facePath +
                        " (Erreur STB : " + STBImage.stbi_failure_reason() + ")");
            }

            int width = widthBuffer.get(0);
            int height = heightBuffer.get(0);
            int channels = channelsBuffer.get(0);

            // Charger l'image dans la face correspondante de la texture cubique
            GL11.glTexImage2D(
                    GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    channels == 4 ? GL11.GL_RGBA : GL11.GL_RGB,
                    width,
                    height,
                    0,
                    channels == 4 ? GL11.GL_RGBA : GL11.GL_RGB,
                    GL11.GL_UNSIGNED_BYTE,
                    imageData
            );

            // Libérer les ressources
            STBImage.stbi_image_free(imageData);
        }

        // Définir les paramètres de la texture cubique
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        return textureId;
    }

    /**
     * Crée le maillage du skybox (un cube).
     */
    private void createSkyboxMesh() {
        // Vertices du cube du skybox
        float[] vertices = {
                // Face avant
                -size,  size,  size,
                size,  size,  size,
                size, -size,  size,
                -size, -size,  size,

                // Face arrière
                -size,  size, -size,
                size,  size, -size,
                size, -size, -size,
                -size, -size, -size
        };

        // Indices pour les triangles
        int[] indices = {
                // Face avant
                0, 1, 2,
                2, 3, 0,

                // Face arrière
                4, 6, 5,
                5, 6, 7,

                // Face gauche
                4, 0, 3,
                3, 7, 4,

                // Face droite
                1, 5, 6,
                6, 2, 1,

                // Face du haut
                4, 5, 1,
                1, 0, 4,

                // Face du bas
                3, 2, 6,
                6, 7, 3
        };

        // Créer le VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Créer le VBO
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Créer l'EBO
        int eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Définir les attributs des vertices
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(0);

        // Délier le VAO
        GL30.glBindVertexArray(0);
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        if (!isVisible) {
            return;
        }

        // Utiliser le shader du skybox
        skyboxShader.bind();

        // Définir la matrice de vue sans la translation (pour centrer le skybox sur la caméra)
        Matrix4f skyboxViewMatrix = new Matrix4f(viewMatrix);
        skyboxViewMatrix.m30(0); // Annuler la translation sur l'axe X
        skyboxViewMatrix.m31(0); // Annuler la translation sur l'axe Y
        skyboxViewMatrix.m32(0); // Annuler la translation sur l'axe Z

        // Définir les matrices dans le shader
        skyboxShader.setUniform("view", skyboxViewMatrix);
        skyboxShader.setUniform("projection", projectionMatrix);
        skyboxShader.setUniform("model", this.modelMatrix.mul(modelMatrix, new Matrix4f()));

        // Lier la texture cubique
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);

        // Lier le VAO
        GL30.glBindVertexArray(vaoId);

        // Dessiner le skybox
        GL11.glDrawElements(
                GL11.GL_TRIANGLES,
                36, // 12 triangles * 3 vertices
                GL11.GL_UNSIGNED_INT,
                0
        );

        // Délier le VAO
        GL30.glBindVertexArray(0);

        // Désactiver le shader
        skyboxShader.unbind();
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Renderable.super.render(shader, projectionMatrix, viewMatrix);
    }

    @Override
    public void update(float deltaTime) {
        Renderable.super.update(deltaTime);
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

    public int getTextureId() {
        return textureId;
    }

    public Shader getSkyboxShader() {
        return skyboxShader;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    /**
     * Libère les ressources du skybox.
     */
    public void cleanup() {
        GL11.glDeleteTextures(textureId);
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        if (skyboxShader != null) {
            skyboxShader.cleanup();
        }
    }

    /**
     * Retourne une représentation textuelle du skybox.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Skybox{" +
                "textureId=" + textureId +
                ", texturePath='" + texturePath + '\'' +
                ", size=" + size +
                '}';
    }
}