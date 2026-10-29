package fr.alessdevita.valtherion.rendering.utils;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

/**
 * Classe d'utilitaires pour le rendu dans Valtherion.
 * Fournit des méthodes pour créer des buffers OpenGL, gérer les états, et créer des formes géométriques.
 */
public class RenderUtils {

    // Couleur par défaut pour le débogage
    private static final float[] DEFAULT_COLOR = {1.0f, 0.0f, 0.0f, 1.0f};

    /**
     * Crée un VBO (Vertex Buffer Object).
     * @param data Données à charger dans le VBO.
     * @param usage Type d'utilisation du VBO (GL_STATIC_DRAW, GL_DYNAMIC_DRAW, etc.).
     * @return ID du VBO.
     */
    public static int createVBO(FloatBuffer data, int usage) {
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vboId;
    }

    /**
     * Crée un VBO avec un tableau de floats.
     * @param data Tableau de floats.
     * @param usage Type d'utilisation du VBO.
     * @return ID du VBO.
     */
    public static int createVBO(float[] data, int usage) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        int vboId = createVBO(buffer, usage);
        MemoryUtil.memFree(buffer);
        return vboId;
    }

    /**
     * Crée un EBO (Element Buffer Object).
     * @param data Données à charger dans l'EBO.
     * @param usage Type d'utilisation de l'EBO.
     * @return ID de l'EBO.
     */
    public static int createEBO(IntBuffer data, int usage) {
        int eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        return eboId;
    }

    /**
     * Crée un EBO avec un tableau d'entiers.
     * @param data Tableau d'entiers.
     * @param usage Type d'utilisation de l'EBO.
     * @return ID de l'EBO.
     */
    public static int createEBO(int[] data, int usage) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        int eboId = createEBO(buffer, usage);
        MemoryUtil.memFree(buffer);
        return eboId;
    }

    /**
     * Crée un VAO (Vertex Array Object).
     * @return ID du VAO.
     */
    public static int createVAO() {
        return GL30.glGenVertexArrays();
    }

    /**
     * Crée un VAO avec des attributs.
     * @param attributes Liste des attributs (position, normale, texture, etc.).
     * @return ID du VAO.
     */
    public static int createVAO(VertexAttribute... attributes) {
        int vaoId = createVAO();
        GL30.glBindVertexArray(vaoId);

        for (VertexAttribute attribute : attributes) {
            GL20.glVertexAttribPointer(
                    attribute.location,
                    attribute.size,
                    attribute.type,
                    attribute.normalized,
                    attribute.stride,
                    attribute.offset
            );
            GL20.glEnableVertexAttribArray(attribute.location);
        }

        GL30.glBindVertexArray(0);
        return vaoId;
    }

    /**
     * Active une fonctionnalité OpenGL.
     * @param capability Fonctionnalité à activer (GL_DEPTH_TEST, GL_BLEND, etc.).
     */
    public static void enable(int capability) {
        GL11.glEnable(capability);
    }

    /**
     * Désactive une fonctionnalité OpenGL.
     * @param capability Fonctionnalité à désactiver.
     */
    public static void disable(int capability) {
        GL11.glDisable(capability);
    }

    /**
     * Vérifie s'il y a une erreur OpenGL.
     * @return true si une erreur est détectée, false sinon.
     */
    public static boolean checkGLError() {
        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            System.err.println("Erreur OpenGL : " + getGLErrorString(error));
            return true;
        }
        return false;
    }

    /**
     * Récupère une chaîne décrivant une erreur OpenGL.
     * @param error Code d'erreur OpenGL.
     * @return Chaîne décrivant l'erreur.
     */
    public static String getGLErrorString(int error) {
        switch (error) {
            case GL11.GL_NO_ERROR:
                return "Aucune erreur";
            case GL11.GL_INVALID_ENUM:
                return "Enum invalide";
            case GL11.GL_INVALID_VALUE:
                return "Valeur invalide";
            case GL11.GL_INVALID_OPERATION:
                return "Opération invalide";
            case GL11.GL_OUT_OF_MEMORY:
                return "Mémoire insuffisante";
            default:
                return "Erreur inconnue : " + error;
        }
    }

    /**
     * Crée un quad plein écran.
     * @return Tableau de vertices et indices pour un quad plein écran.
     */
    public static float[] createFullscreenQuadVertices() {
        return new float[] {
                -1.0f, -1.0f,  0.0f, 0.0f, 0.0f,
                1.0f, -1.0f,  1.0f, 0.0f,
                1.0f,  1.0f,  1.0f, 1.0f,
                -1.0f,  1.0f,  0.0f, 1.0f
        };
    }

    /**
     * Crée les indices pour un quad plein écran.
     * @return Tableau d'indices.
     */
    public static int[] createFullscreenQuadIndices() {
        return new int[] {
                0, 1, 2,
                2, 3, 0
        };
    }

    /**
     * Crée un cube.
     * @return Tableau de vertices et indices pour un cube.
     */
    public static float[] createCubeVertices() {
        return new float[] {
                // Face avant
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f, 0.0f, 1.0f,

                // Face arrière
                -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f, 0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 0.0f, -1.0f,

                // Face gauche
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 1.0f, -1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, -1.0f, 0.0f, 0.0f,

                // Face droite
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1.0f, 0.0f, 0.0f,

                // Face du haut
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f, 0.0f,

                // Face du bas
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f, -1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f, -1.0f, 0.0f
        };
    }

    /**
     * Crée les indices pour un cube.
     * @return Tableau d'indices.
     */
    public static int[] createCubeIndices() {
        return new int[] {
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
    }

    // --- Classe interne pour les attributs des vertices ---

    /**
     * Attribut d'un vertex.
     */
    public static class VertexAttribute {
        public final int location;
        public final int size;
        public final int type;
        public final boolean normalized;
        public final int stride;
        public final long offset;

        public VertexAttribute(int location, int size, int type, boolean normalized, int stride, long offset) {
            this.location = location;
            this.size = size;
            this.type = type;
            this.normalized = normalized;
            this.stride = stride;
            this.offset = offset;
        }
    }
}