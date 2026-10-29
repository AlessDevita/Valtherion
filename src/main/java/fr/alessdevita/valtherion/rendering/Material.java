package fr.alessdevita.valtherion.rendering;

import org.joml.Math;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;

import java.util.*;

/**
 * Classe pour gérer les matériaux dans Valtherion.
 * Un matériau définit l'apparence d'un maillage 3D (couleurs, textures, etc.).
 */
public class Material {

    public static Object air;
    // Couleur diffuse du matériau
    private Vector4f diffuseColor;

    // Couleur spéculaire du matériau
    private Vector4f specularColor;

    // Couleur ambiante du matériau
    private Vector4f ambientColor;

    // Facteur de brillance (shininess)
    private float shininess;

    // Transparence du matériau (0.0 = opaque, 1.0 = transparent)
    private float transparency;

    // Réflexivité du matériau
    private float reflectivity;

    // Map des textures du matériau (type de texture -> Texture)
    private final Map<Texture.TextureType, Texture> textures;

    // Nom du matériau
    private String name;

    /**
     * Constructeur du matériau.
     */
    public Material() {
        this.diffuseColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc par défaut
        this.specularColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc par défaut
        this.ambientColor = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f); // Gris foncé par défaut
        this.shininess = 32.0f;
        this.transparency = 0.0f;
        this.reflectivity = 0.0f;
        this.textures = new HashMap<>();
        this.name = "DefaultMaterial";
    }

    /**
     * Constructeur du matériau avec une couleur diffuse.
     * @param diffuseColor Couleur diffuse.
     */
    public Material(Vector4f diffuseColor) {
        this();
        this.diffuseColor = diffuseColor;
    }

    /**
     * Constructeur du matériau avec une couleur diffuse et spéculaire.
     * @param diffuseColor Couleur diffuse.
     * @param specularColor Couleur spéculaire.
     */
    public Material(Vector4f diffuseColor, Vector4f specularColor) {
        this(diffuseColor);
        this.specularColor = specularColor;
    }

    /**
     * Ajoute une texture au matériau.
     * @param type Type de la texture.
     * @param texture Texture à ajouter.
     */
    public void addTexture(Texture.TextureType type, Texture texture) {
        textures.put(type, texture);
    }

    /**
     * Récupère une texture du matériau.
     * @param type Type de la texture.
     * @return Texture ou null si non trouvée.
     */
    public Texture getTexture(Texture.TextureType type) {
        return textures.get(type);
    }

    /**
     * Lie toutes les textures du matériau.
     */
    public void bindTextures() {
        int textureUnit = 0;
        for (Map.Entry<Texture.TextureType, Texture> entry : textures.entrySet()) {
            entry.getValue().bind(GL13.GL_TEXTURE0 + textureUnit);
            textureUnit++;
        }
    }

    /**
     * Délie toutes les textures du matériau.
     */
    public void unbindTextures() {
        for (Texture texture : textures.values()) {
            texture.unbind();
        }
    }

    /**
     * Applique le matériau à un shader.
     * @param shader Shader à utiliser.
     */
    public void applyToShader(Shader shader) {
        // Appliquer les couleurs au shader
        shader.setUniform("material.diffuse", diffuseColor);
        shader.setUniform("material.specular", specularColor);
        shader.setUniform("material.ambient", ambientColor);
        shader.setUniform("material.shininess", shininess);
        shader.setUniform("material.transparency", transparency);
        shader.setUniform("material.reflectivity", reflectivity);

        // Appliquer les textures au shader
        int textureUnit = 0;
        for (Map.Entry<Texture.TextureType, Texture> entry : textures.entrySet()) {
            String uniformName = "material." + entry.getKey().name().toLowerCase();
            shader.setUniform(uniformName, textureUnit);
            textureUnit++;
        }
    }

    // --- Getters et Setters ---

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = Math.max(0.0f, shininess);
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = Math.max(0.0f, Math.min(1.0f, transparency));
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = Math.max(0.0f, Math.min(1.0f, reflectivity));
    }

    public Map<Texture.TextureType, Texture> getTextures() {
        return textures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne une représentation textuelle du matériau.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Material{" +
                "name='" + name + '\'' +
                ", diffuseColor=" + diffuseColor +
                ", specularColor=" + specularColor +
                ", shininess=" + shininess +
                ", textures=" + textures.size() +
                '}';
    }

    // --- Matériaux prédéfinis ---

    /**
     * Matériau par défaut pour les objets opaques.
     */
    public static Material DEFAULT_OPAQUE = new Material(
            new Vector4f(0.8f, 0.8f, 0.8f, 1.0f),
            new Vector4f(0.2f, 0.2f, 0.2f, 1.0f)
    );

    /**
     * Matériau par défaut pour les objets transparents.
     */
    public static Material DEFAULT_TRANSPARENT = new Material(
            new Vector4f(0.5f, 0.5f, 0.5f, 0.5f),
            new Vector4f(0.1f, 0.1f, 0.1f, 0.5f)
    );

    /**
     * Matériau pour l'eau.
     */
    public static Material WATER = new Material(
            new Vector4f(0.0f, 0.5f, 1.0f, 0.7f),
            new Vector4f(0.5f, 0.5f, 0.5f, 0.7f)
    );

    static {
        DEFAULT_OPAQUE.setName("DefaultOpaque");
        DEFAULT_TRANSPARENT.setName("DefaultTransparent");
        WATER.setName("Water");
        WATER.setTransparency(0.7f);
    }

    public void cleanup() {

    }
}