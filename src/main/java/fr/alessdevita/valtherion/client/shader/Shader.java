package fr.alessdevita.valtherion.client.shader;

import org.lwjgl.opengl.GL20;

/**
 * Programme shader GLSL compilé et lié.
 * <p>
 * Contient un vertex shader et un fragment shader. Fournit
 * l'activation / désactivation et l'accès aux uniforms.
 */
public final class Shader {
    private final int programId;
    private final String name;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    /**
     * Construit un shader depuis un programme déjà lié.
     *
     * @param name             nom logique du shader
     * @param programId        ID du programme GLSL
     * @param vertexShaderId   ID du vertex shader compilé
     * @param fragmentShaderId ID du fragment shader compilé
     */
    public Shader(String name, int programId, int vertexShaderId, int fragmentShaderId) {
        this.name = name;
        this.programId = programId;
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
    }

    /** @return l'ID du programme GLSL. */
    public int getProgramId() {
        return this.programId;
    }

    /** @return le nom logique du shader. */
    public String getName() {
        return this.name;
    }

    /** Active le programme pour le rendu. */
    public void use() {
        GL20.glUseProgram(this.programId);
    }

    /** Désactive le programme (retour au pipeline fixe). */
    public void end() {
        GL20.glUseProgram(0);
    }

    /**
     * Retourne un uniform par son nom. Si l'uniform n'existe pas,
     * retourne un {@link ShaderDefault} no-op.
     *
     * @param uniformName le nom de l'uniform
     * @return l'{@link ShaderUniform} ou un no-op
     */
    public ShaderUniform getUniform(String uniformName) {
        if (uniformName == null) return ShaderDefault.get();
        ShaderUniform uniform = new ShaderUniform(this.programId, uniformName);
        return uniform.isValid() ? uniform : ShaderDefault.get();
    }

    /**
     * Retourne l'emplacement d'un attribut de vertex par son nom.
     *
     * @param attribName le nom de l'attribut
     * @return l'emplacement, ou -1
     */
    public int getAttributeLocation(String attribName) {
        return GL20.glGetAttribLocation(this.programId, attribName);
    }

    /**
     * Lie un attribut de vertex à un emplacement avant le linkage.
     *
     * @param index    l'index d'attribut
     * @param attribName le nom de l'attribut
     */
    public void bindAttribute(int index, String attribName) {
        GL20.glBindAttribLocation(this.programId, index, attribName);
    }

    /** Supprime le programme et les shaders. */
    public void delete() {
        if (this.programId > 0) {
            if (this.vertexShaderId > 0) {
                GL20.glDetachShader(this.programId, this.vertexShaderId);
                GL20.glDeleteShader(this.vertexShaderId);
            }
            if (this.fragmentShaderId > 0) {
                GL20.glDetachShader(this.programId, this.fragmentShaderId);
                GL20.glDeleteShader(this.fragmentShaderId);
            }
            GL20.glDeleteProgram(this.programId);
        }
    }

    @Override
    public String toString() {
        return "Shader[" + this.name + "@id=" + this.programId + "]";
    }
}
