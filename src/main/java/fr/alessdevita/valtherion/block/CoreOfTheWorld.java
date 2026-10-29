package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Le Cœur du Monde - bloc signature suprême de Valtherion.
 * Représente la source de toute magie et d'énergie du monde.
 */
public class CoreOfTheWorld extends Block {
    private float energyLevel;
    private float pulseIntensity;
    private float rotationAngle;
    private Vector3f auraColor;

    /**
     * Crée le Cœur du Monde.
     */
    public CoreOfTheWorld() {
        super(BlockType.OBSIDIAN);
        this.energyLevel = 1.0f;
        this.pulseIntensity = 0.5f;
        this.rotationAngle = 0.0f;
        this.auraColor = new Vector3f(1.0f, 0.0f, 1.0f);
    }

    /**
     * Met à jour le cœur (pulsation, rotation).
     * @param deltaTime Temps écoulé
     */
    @Override
    public void update(float deltaTime) {
        pulseIntensity = (float) (0.5f + 0.5f * Math.sin(System.currentTimeMillis() * 0.003f));
        rotationAngle += deltaTime * 30.0f;
        if (rotationAngle > 360.0f) {
            rotationAngle -= 360.0f;
        }
    }

    /**
     * Retourne le niveau d'énergie du cœur.
     * @return Niveau d'énergie (0.0 à 1.0)
     */
    public float getEnergyLevel() {
        return energyLevel;
    }

    /**
     * Définit le niveau d'énergie.
     * @param energyLevel Niveau d'énergie
     */
    public void setEnergyLevel(float energyLevel) {
        this.energyLevel = Math.max(0.0f, Math.min(1.0f, energyLevel));
    }

    /**
     * Retourne l'intensité de pulsation.
     * @return Intensité (0.0 à 1.0)
     */
    public float getPulseIntensity() {
        return pulseIntensity;
    }

    /**
     * Retourne l'angle de rotation.
     * @return Angle en degrés
     */
    public float getRotationAngle() {
        return rotationAngle;
    }

    /**
     * Retourne la couleur de l'aura.
     * @return Couleur RGB
     */
    public Vector3f getAuraColor() {
        return auraColor;
    }

    /**
     * Transmet de l'énergie.
     * @param amount Quantité d'énergie
     */
    public void transmitEnergy(float amount) {
        energyLevel = Math.max(0.0f, energyLevel - amount);
    }

    /**
     * Recharge le cœur.
     * @param amount Quantité d'énergie
     */
    public void recharge(float amount) {
        energyLevel = Math.min(1.0f, energyLevel + amount);
    }

    /**
     * Retourne si le cœur est actif.
     * @return true si actif
     */
    public boolean isActive() {
        return energyLevel > 0.1f;
    }
}
