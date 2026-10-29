package fr.alessdevita.valtherion.client.shader;

import org.lwjgl.opengl.GL20;

/**
 * Représente une variable uniform d'un programme shader GLSL.
 * Wrappe l'emplacement ({@code glGetUniformLocation}) et fournit
 * des méthodes typées pour définir sa valeur.
 * <p>
 * Non-final pour permettre à {@link ShaderDefault} de surcharger
 * les setters en no-op.
 */
public class ShaderUniform {
    private final String name;
    private final int programId;
    private final int location;

    /**
     * Construit un uniform pour un programme et un nom donnés.
     * L'emplacement est résolu via {@code glGetUniformLocation}.
     *
     * @param programId l'ID du programme GLSL
     * @param name      le nom de l'uniform dans le shader
     */
    public ShaderUniform(int programId, String name) {
        this.programId = programId;
        this.name = name == null ? "" : name;
        this.location = programId > 0 ? GL20.glGetUniformLocation(programId, this.name) : -1;
    }

    /**
     * Constructeur interne pour spécifier directement le location.
     *
     * @param programId l'ID du programme GLSL
     * @param name      le nom de l'uniform
     * @param location  l'emplacement de l'uniform (-1 = invalide)
     */
    protected ShaderUniform(int programId, String name, int location) {
        this.programId = programId;
        this.name = name == null ? "" : name;
        this.location = location;
    }

    /** @return l'emplacement de l'uniform, ou -1 s'il n'existe pas. */
    public int getLocation() {
        return this.location;
    }

    /** @return le nom de l'uniform. */
    public String getName() {
        return this.name;
    }

    /** @return l'ID du programme GLSL. */
    public int getProgramId() {
        return this.programId;
    }

    /** @return {@code true} si l'uniform existe dans le programme. */
    public boolean isValid() {
        return this.location >= 0;
    }

    // --- Float setters ---

    public void set(float x) {
        if (this.location >= 0) GL20.glUniform1f(this.location, x);
    }

    public void set(float x, float y) {
        if (this.location >= 0) GL20.glUniform2f(this.location, x, y);
    }

    public void set(float x, float y, float z) {
        if (this.location >= 0) GL20.glUniform3f(this.location, x, y, z);
    }

    public void set(float x, float y, float z, float w) {
        if (this.location >= 0) GL20.glUniform4f(this.location, x, y, z, w);
    }

    // --- Int setters ---

    public void set(int x) {
        if (this.location >= 0) GL20.glUniform1i(this.location, x);
    }

    public void set(int x, int y) {
        if (this.location >= 0) GL20.glUniform2i(this.location, x, y);
    }

    public void set(int x, int y, int z) {
        if (this.location >= 0) GL20.glUniform3i(this.location, x, y, z);
    }

    public void set(int x, int y, int z, int w) {
        if (this.location >= 0) GL20.glUniform4i(this.location, x, y, z, w);
    }

    // --- Array setters ---

    public void set(float[] values) {
        if (this.location < 0 || values == null) return;
        java.nio.FloatBuffer buffer = org.lwjgl.BufferUtils.createFloatBuffer(values.length);
        buffer.put(values).flip();
        switch (values.length) {
            case 1 -> GL20.glUniform1fv(this.location, buffer);
            case 2 -> GL20.glUniform2fv(this.location, buffer);
            case 3 -> GL20.glUniform3fv(this.location, buffer);
            case 4 -> GL20.glUniform4fv(this.location, buffer);
            default -> GL20.glUniform1fv(this.location, buffer);
        }
    }

    public void set(int[] values) {
        if (this.location < 0 || values == null) return;
        java.nio.IntBuffer buffer = org.lwjgl.BufferUtils.createIntBuffer(values.length);
        buffer.put(values).flip();
        switch (values.length) {
            case 1 -> GL20.glUniform1iv(this.location, buffer);
            case 2 -> GL20.glUniform2iv(this.location, buffer);
            case 3 -> GL20.glUniform3iv(this.location, buffer);
            case 4 -> GL20.glUniform4iv(this.location, buffer);
            default -> GL20.glUniform1iv(this.location, buffer);
        }
    }

    @Override
    public String toString() {
        return "ShaderUniform[" + this.name + "@loc=" + this.location + "]";
    }
}
