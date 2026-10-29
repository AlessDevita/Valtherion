package fr.alessdevita.valtherion.client.shader;

import org.lwjgl.opengl.GL20;

/**
 * Charge et compile les sources de shaders GLSL (vertex et fragment).
 */
public final class ShaderLoader {
    private ShaderLoader() { }

    /**
     * Compile un shader depuis son code source.
     *
     * @param type       le type de shader ({@link GL20#GL_VERTEX_SHADER} ou {@link GL20#GL_FRAGMENT_SHADER})
     * @param source     le code source GLSL
     * @return l'ID du shader compilé
     * @throws RuntimeException si la compilation échoue
     */
    public static int compileShader(int type, String source) {
        if (source == null || source.isBlank()) {
            throw new RuntimeException("Source shader vide");
        }
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);

        int status = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
        if (status == 0) {
            String log = GL20.glGetShaderInfoLog(shaderId, 32768);
            GL20.glDeleteShader(shaderId);
            throw new RuntimeException("Erreur de compilation du shader: " + log);
        }

        return shaderId;
    }

    /**
     * Compile un vertex shader.
     *
     * @param source le code source GLSL
     * @return l'ID du shader
     */
    public static int loadVertexShader(String source) {
        return compileShader(GL20.GL_VERTEX_SHADER, source);
    }

    /**
     * Compile un fragment shader.
     *
     * @param source le code source GLSL
     * @return l'ID du shader
     */
    public static int loadFragmentShader(String source) {
        return compileShader(GL20.GL_FRAGMENT_SHADER, source);
    }

    /**
     * Compile un vertex shader et un fragment shader, puis retourne un
     * {@link Shader} non lié.
     *
     * @param vertexSource   le code source du vertex shader
     * @param fragmentSource le code source du fragment shader
     * @param name           le nom logique du shader
     * @return un tableau {vertexShaderId, fragmentShaderId}
     */
    public static int[] loadShaders(String vertexSource, String fragmentSource, String name) {
        int vertexId = loadVertexShader(vertexSource);
        int fragmentId = loadFragmentShader(fragmentSource);
        return new int[]{vertexId, fragmentId};
    }
}
