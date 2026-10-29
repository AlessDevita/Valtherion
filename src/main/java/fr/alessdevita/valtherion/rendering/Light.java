package fr.alessdevita.valtherion.rendering;

import org.joml.Math;
import org.joml.Vector3f;

/**
 * Classe pour gérer les lumières dans Valtherion.
 * Supporte les lumières directionnelles, ponctuelles et spot.
 */
public class Light {

    // Type de lumière (DIRECTIONAL, POINT, SPOT)
    private LightType type;

    // Position de la lumière
    private Vector3f position;

    // Direction de la lumière (pour les lumières directionnelles et spot)
    private Vector3f direction;

    // Couleur de la lumière
    private Vector3f color;

    // Intensité de la lumière
    private float intensity;

    // Atténuation de la lumière (constant, linear, quadratic)
    private float constantAttenuation;
    private float linearAttenuation;
    private float quadraticAttenuation;

    // Angle d'ouverture de la lumière spot (en degrés)
    private float cutOff;

    // Angle d'ouverture externe de la lumière spot (en degrés)
    private float outerCutOff;

    // Énumération des types de lumière
    public enum LightType {
        DIRECTIONAL,  // Lumière directionnelle (ex: soleil)
        POINT,        // Lumière ponctuelle (ex: lampe)
        SPOT          // Lumière spot (ex: projecteur)
    }

    /**
     * Constructeur de la lumière.
     */
    public Light() {
        this.type = LightType.POINT;
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.direction = new Vector3f(0.0f, -1.0f, 0.0f);
        this.color = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc par défaut
        this.intensity = 1.0f;
        this.constantAttenuation = 1.0f;
        this.linearAttenuation = 0.0f;
        this.quadraticAttenuation = 0.0f;
        this.cutOff = 45.0f;
        this.outerCutOff = 60.0f;
    }

    /**
     * Constructeur de la lumière avec un type, une position et une couleur.
     * @param type Type de la lumière.
     * @param position Position de la lumière.
     * @param color Couleur de la lumière.
     */
    public Light(LightType type, Vector3f position, Vector3f color) {
        this();
        this.type = type;
        this.position = position;
        this.color = color;
    }

    /**
     * Applique la lumière à un shader.
     * @param shader Shader à utiliser.
     * @param index Index de la lumière (pour les uniforms du shader).
     */
    public void applyToShader(Shader shader, int index) {
        String prefix = "lights[" + index + "].";

        // Définir le type de lumière
        shader.setUniform(prefix + "type", type.ordinal());

        // Définir la position de la lumière
        shader.setUniform(prefix + "position", position);

        // Définir la direction de la lumière
        shader.setUniform(prefix + "direction", direction);

        // Définir la couleur de la lumière
        shader.setUniform(prefix + "color", color);

        // Définir l'intensité de la lumière
        shader.setUniform(prefix + "intensity", intensity);

        // Définir l'atténuation de la lumière
        shader.setUniform(prefix + "constant", constantAttenuation);
        shader.setUniform(prefix + "linear", linearAttenuation);
        shader.setUniform(prefix + "quadratic", quadraticAttenuation);

        // Définir les angles de la lumière spot
        shader.setUniform(prefix + "cutOff", (float) Math.cos(Math.toRadians(cutOff)));
        shader.setUniform(prefix + "outerCutOff", (float) Math.cos(Math.toRadians(outerCutOff)));
    }

    // --- Méthodes statiques pour créer des lumières prédéfinies ---

    /**
     * Crée une lumière directionnelle (soleil).
     * @param direction Direction de la lumière.
     * @param color Couleur de la lumière.
     * @return Lumière directionnelle.
     */
    public static Light createDirectionalLight(Vector3f direction, Vector3f color) {
        Light light = new Light();
        light.type = LightType.DIRECTIONAL;
        light.direction = direction.normalize();
        light.color = color;
        light.intensity = 1.0f;
        return light;
    }

    /**
     * Crée une lumière ponctuelle (lampe).
     * @param position Position de la lumière.
     * @param color Couleur de la lumière.
     * @return Lumière ponctuelle.
     */
    public static Light createPointLight(Vector3f position, Vector3f color) {
        Light light = new Light();
        light.type = LightType.POINT;
        light.position = position;
        light.color = color;
        light.intensity = 1.0f;
        light.constantAttenuation = 1.0f;
        light.linearAttenuation = 0.09f;
        light.quadraticAttenuation = 0.032f;
        return light;
    }

    /**
     * Crée une lumière spot (projecteur).
     * @param position Position de la lumière.
     * @param direction Direction de la lumière.
     * @param color Couleur de la lumière.
     * @return Lumière spot.
     */
    public static Light createSpotLight(Vector3f position, Vector3f direction, Vector3f color) {
        Light light = new Light();
        light.type = LightType.SPOT;
        light.position = position;
        light.direction = direction.normalize();
        light.color = color;
        light.intensity = 1.0f;
        light.constantAttenuation = 1.0f;
        light.linearAttenuation = 0.09f;
        light.quadraticAttenuation = 0.032f;
        light.cutOff = 45.0f;
        light.outerCutOff = 60.0f;
        return light;
    }

    // --- Getters et Setters ---

    public LightType getType() {
        return type;
    }

    public void setType(LightType type) {
        this.type = type;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction.normalize();
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = Math.max(0.0f, intensity);
    }

    public float getConstantAttenuation() {
        return constantAttenuation;
    }

    public void setConstantAttenuation(float constantAttenuation) {
        this.constantAttenuation = Math.max(0.0f, constantAttenuation);
    }

    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    public void setLinearAttenuation(float linearAttenuation) {
        this.linearAttenuation = Math.max(0.0f, linearAttenuation);
    }

    public float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    public void setQuadraticAttenuation(float quadraticAttenuation) {
        this.quadraticAttenuation = Math.max(0.0f, quadraticAttenuation);
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = Math.max(0.0f, Math.min(90.0f, cutOff));
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = Math.max(0.0f, Math.min(90.0f, outerCutOff));
    }

    /**
     * Retourne une représentation textuelle de la lumière.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Light{" +
                "type=" + type +
                ", position=" + position +
                ", direction=" + direction +
                ", color=" + color +
                ", intensity=" + intensity +
                '}';
    }
}