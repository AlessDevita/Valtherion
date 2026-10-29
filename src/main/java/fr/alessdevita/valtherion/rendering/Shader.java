package fr.alessdevita.valtherion.rendering;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

/**
 * Classe pour gérer les shaders dans Valtherion.
 * Charge, compile et lie les shaders Vertex et Fragment.
 */
public class Shader {

    // ID du programme shader
    private final int programId;

    // Map des uniform locations
    private final Map<String, Integer> uniformLocations;

    // Chemin du fichier de shader vertex
    private String vertexShaderPath;

    // Chemin du fichier de shader fragment
    private String fragmentShaderPath;

    // Chemin du fichier de shader geometry (optionnel)
    private String geometryShaderPath;

    // Indique si le shader est actif
    private boolean isActive;

    /**
     * Constructeur du shader.
     * @param vertexShaderPath Chemin du fichier de shader vertex.
     * @param fragmentShaderPath Chemin du fichier de shader fragment.
     * @throws IOException Si une erreur se produit lors de la lecture des fichiers.
     */
    public Shader(String vertexShaderPath, String fragmentShaderPath) throws IOException {
        this.vertexShaderPath = vertexShaderPath;
        this.fragmentShaderPath = fragmentShaderPath;
        this.uniformLocations = new HashMap<>();

        // Charger et compiler les shaders
        int vertexShaderId = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        int fragmentShaderId = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);

        // Créer le programme shader
        this.programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);

        // Vérifier les erreurs de linkage
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            String error = GL20.glGetProgramInfoLog(programId);
            throw new IOException("Erreur de linkage du shader : " + error);
        }

        // Supprimer les shaders après le linkage
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
    }

    /**
     * Constructeur du shader avec un shader geometry.
     * @param vertexShaderPath Chemin du fichier de shader vertex.
     * @param fragmentShaderPath Chemin du fichier de shader fragment.
     * @param geometryShaderPath Chemin du fichier de shader geometry.
     * @throws IOException Si une erreur se produit lors de la lecture des fichiers.
     */
    public Shader(String vertexShaderPath, String fragmentShaderPath, String geometryShaderPath) throws IOException {
        this(vertexShaderPath, fragmentShaderPath);
        this.geometryShaderPath = geometryShaderPath;

        // Charger et compiler le shader geometry
        int geometryShaderId = loadShader(geometryShaderPath, GL32.GL_GEOMETRY_SHADER);

        // Attacher le shader geometry au programme
        GL20.glAttachShader(programId, geometryShaderId);
        GL20.glLinkProgram(programId);

        // Vérifier les erreurs de linkage
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            String error = GL20.glGetProgramInfoLog(programId);
            throw new IOException("Erreur de linkage du shader geometry : " + error);
        }

        // Supprimer le shader geometry après le linkage
        GL20.glDeleteShader(geometryShaderId);
    }

    /**
     * Charge et compile un shader.
     * @param path Chemin du fichier de shader.
     * @param type Type de shader (GL_VERTEX_SHADER, GL_FRAGMENT_SHADER, etc.).
     * @return ID du shader.
     * @throws IOException Si une erreur se produit lors de la lecture du fichier.
     */
    private int loadShader(String path, int type) throws IOException {
        // Lire le fichier de shader
        String shaderSource = new String(Files.readAllBytes(Paths.get(path)));

        // Créer le shader
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        // Vérifier les erreurs de compilation
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            String error = GL20.glGetShaderInfoLog(shaderId);
            throw new IOException("Erreur de compilation du shader " + path + " : " + error);
        }

        return shaderId;
    }

    /**
     * Active le shader.
     */
    public void bind() {
        GL20.glUseProgram(programId);
        isActive = true;
    }

    /**
     * Désactive le shader.
     */
    public void unbind() {
        GL20.glUseProgram(0);
        isActive = false;
    }

    /**
     * Récupère la location d'un uniform.
     * @param name Nom de l'uniform.
     * @return Location de l'uniform.
     */
    public int getUniformLocation(String name) {
        Integer location = uniformLocations.get(name);
        if (location == null) {
            location = GL20.glGetUniformLocation(programId, name);
            uniformLocations.put(name, location);
        }
        return location;
    }

    // --- Méthodes pour définir les uniforms ---

    /**
     * Définit un uniform de type int.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, int value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform1i(location, value);
        }
    }

    /**
     * Définit un uniform de type float.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, float value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform1f(location, value);
        }
    }

    /**
     * Définit un uniform de type Vector2f.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, Vector2f value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform2f(location, value.x, value.y);
        }
    }

    /**
     * Définit un uniform de type Vector3f.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, Vector3f value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform3f(location, value.x, value.y, value.z);
        }
    }

    /**
     * Définit un uniform de type Vector4f.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, Vector4f value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
        }
    }

    /**
     * Définit un uniform de type Matrix4f.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, Matrix4f value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            value.get(buffer);
            GL20.glUniformMatrix4fv(location, false, buffer);
        }
    }

    /**
     * Définit un uniform de type Matrix3f.
     * @param name Nom de l'uniform.
     * @param value Valeur.
     */
    public void setUniform(String name, Matrix3f value) {
        int location = getUniformLocation(name);
        if (location != -1) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
            value.get(buffer);
            GL20.glUniformMatrix3fv(location, false, buffer);
        }
    }

    // --- Getters ---

    public int getProgramId() {
        return programId;
    }

    public String getVertexShaderPath() {
        return vertexShaderPath;
    }

    public String getFragmentShaderPath() {
        return fragmentShaderPath;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Supprime le shader.
     */
    public void cleanup() {
        GL20.glDeleteProgram(programId);
    }

    /**
     * Retourne une représentation textuelle du shader.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Shader{" +
                "programId=" + programId +
                ", vertexShaderPath='" + vertexShaderPath + '\'' +
                ", fragmentShaderPath='" + fragmentShaderPath + '\'' +
                '}';
    }

    public void setMatrix4(String projection, Matrix4f projectionMatrix) {

    }

    public void setVector3f(String ambientLight, Vector3f ambientLight1) {
    }

    public void init() {

    }

    public void setInt(String lightCount, int lightCount1) {

    }

    public void setFloat(String s, float intensity) {

    }

    public void setMatrix4f(String projection, Matrix4f projectionMatrix) {

    }

    public void setVector4f(String color, Vector4f color1) {

    }
}