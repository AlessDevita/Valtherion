package fr.alessdevita.valtherion.client.shader;

import org.lwjgl.opengl.GL20;

/**
 * Helper pour lier (link) un programme shader GLSL.
 */
public final class ShaderLinkHelper {
    private ShaderLinkHelper() { }

    /**
     * Crée un programme, attache les shaders, lie, et retourne l'ID.
     *
     * @param vertexShaderId   l'ID du vertex shader compilé
     * @param fragmentShaderId l'ID du fragment shader compilé
     * @return l'ID du programme lié
     * @throws RuntimeException si le linkage échoue
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programId = GL20.glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Impossible de créer le programme shader");
        }

        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);

        int status = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS);
        if (status == 0) {
            String log = GL20.glGetProgramInfoLog(programId, 32768);
            GL20.glDeleteProgram(programId);
            throw new RuntimeException("Erreur de linkage du programme shader: " + log);
        }

        GL20.glValidateProgram(programId);
        int validateStatus = GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS);
        if (validateStatus == 0) {
            // La validation a échoué mais le programme peut quand même fonctionner.
            String log = GL20.glGetProgramInfoLog(programId, 32768);
            System.err.println("Avertissement: validation du programme shader: " + log);
        }

        return programId;
    }

    /**
     * Crée un programme complet depuis des sources GLSL.
     *
     * @param vertexSource   le code source du vertex shader
     * @param fragmentSource le code source du fragment shader
     * @return l'ID du programme lié
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexId = ShaderLoader.loadVertexShader(vertexSource);
        int fragmentId = ShaderLoader.loadFragmentShader(fragmentSource);
        return linkProgram(vertexId, fragmentId);
    }
}
