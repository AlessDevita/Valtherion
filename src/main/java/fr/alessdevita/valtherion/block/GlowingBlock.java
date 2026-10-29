package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Classe de base pour les blocs lumineux dans Valtherion.
 * Ces blocs émettent de la lumière et peuvent éclairer leur environnement.
 */
public abstract class GlowingBlock extends Block {

    // Niveau de lumière émis par le bloc (0.0 à 1.0)
    private float lightLevel;

    // Couleur de la lumière émise (RVB)
    private Vector3f lightColor;

    // Rayon de la lumière (en blocs)
    private int lightRadius;

    // Indique si le bloc clignote
    private boolean isFlickering;

    // Fréquence de clignotement (en secondes)
    private float flickerFrequency;

    // Temps écoulé depuis le dernier clignotement (en secondes)
    private float flickerTime;

    /**
     * Constructeur du bloc lumineux.
     * @param type Type du bloc.
     * @param lightLevel Niveau de lumière.
     * @param lightColor Couleur de la lumière.
     * @param lightRadius Rayon de la lumière.
     */
    public GlowingBlock(BlockType type, float lightLevel, Vector3f lightColor, int lightRadius) {
        super(type);
        this.lightLevel = lightLevel;
        this.lightColor = lightColor;
        this.lightRadius = lightRadius;
        this.isFlickering = false;
        this.flickerFrequency = 0.0f;
        this.flickerTime = 0.0f;
    }

    /**
     * Met à jour le bloc lumineux.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {

        // Mettre à jour le clignotement
        if (isFlickering && flickerFrequency > 0) {
            flickerTime += deltaTime;
            if (flickerTime >= flickerFrequency) {
                flickerTime = 0.0f;
                toggleLight();
            }
        }
    }

    /**
     * Active ou désactive le clignotement.
     * @param isFlickering true pour activer le clignotement, false sinon.
     */
    public void setFlickering(boolean isFlickering) {
        this.isFlickering = isFlickering;
    }

    /**
     * Définit la fréquence de clignotement.
     * @param flickerFrequency Fréquence de clignotement (en secondes).
     */
    public void setFlickerFrequency(float flickerFrequency) {
        this.flickerFrequency = flickerFrequency;
    }

    /**
     * Bascule l'état de la lumière.
     */
    public void toggleLight() {
        setLightLevel(lightLevel > 0 ? 0 : 1.0f);
    }

    // --- Getters et Setters ---

    public float getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(float lightLevel) {
        this.lightLevel = Math.max(0.0f, Math.min(1.0f, lightLevel));
    }

    public Vector3f getLightColor() {
        return lightColor;
    }

    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int lightRadius) {
        this.lightRadius = Math.max(0, lightRadius);
    }

    public boolean isFlickering() {
        return isFlickering;
    }

    public float getFlickerFrequency() {
        return flickerFrequency;
    }

    /**
     * Retourne une représentation textuelle du bloc lumineux.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GlowingBlock{" +
                "type=" + getType() +
                ", lightLevel=" + lightLevel +
                ", lightColor=" + lightColor +
                ", lightRadius=" + lightRadius +
                '}';
    }
}