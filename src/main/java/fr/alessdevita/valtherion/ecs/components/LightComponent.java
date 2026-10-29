package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector3f;

/**
 * Composant pour une source de lumière dans Valtherion.
 * Stocke les propriétés de la lumière (couleur, intensité, type, etc.).
 */
public class LightComponent implements Component {

    // Type de lumière
    private LightType type;

    // Couleur de la lumière
    private Vector3f color;

    // Intensité de la lumière
    private float intensity;

    // Position de la lumière (pour les lumières ponctuelles ou spot)
    private Vector3f position;

    // Direction de la lumière (pour les lumières directionnelles ou spot)
    private Vector3f direction;

    // Atténuation de la lumière (pour les lumières ponctuelles)
    private float constant;
    private float linear;
    private float quadratic;

    // Angle d'ouverture de la lumière (pour les lumières spot)
    private float cutOff;
    private float outerCutOff;

    // Indique si la lumière est active
    private boolean isEnabled;

    // Entité associée
    private Entity entity;

    // Énumération des types de lumières
    public enum LightType {
        DIRECTIONAL,  // Lumière directionnelle (ex: soleil)
        POINT,        // Lumière ponctuelle (ex: lanterne)
        SPOT          // Lumière spot (ex: lampe torche)
    }

    /**
     * Constructeur du composant de lumière.
     */
    public LightComponent() {
        this.type = LightType.POINT;
        this.color = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc
        this.intensity = 1.0f;
        this.position = new Vector3f(0, 0, 0);
        this.direction = new Vector3f(0, -1, 0); // Vers le bas
        this.constant = 1.0f;
        this.linear = 0.0f;
        this.quadratic = 0.0f;
        this.cutOff = 0.0f;
        this.outerCutOff = 0.0f;
        this.isEnabled = true;
    }

    /**
     * Constructeur du composant de lumière avec un type et une couleur.
     * @param type Type de lumière.
     * @param color Couleur de la lumière.
     */
    public LightComponent(LightType type, Vector3f color) {
        this();
        this.type = type;
        this.color = color;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    // --- Getters et Setters ---

    public LightType getType() {
        return type;
    }

    public void setType(LightType type) {
        this.type = type;
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
        this.intensity = intensity;
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
        this.direction = direction;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public float getOuterCutOff() {
        return outerCutOff;
    }

    public void setOuterCutOff(float outerCutOff) {
        this.outerCutOff = outerCutOff;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "LightComponent{" +
                "type=" + type +
                ", color=" + color +
                ", intensity=" + intensity +
                ", isEnabled=" + isEnabled +
                '}';
    }
}