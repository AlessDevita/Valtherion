package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de pierre bleutée lumineuse dans Valtherion.
 * Émet une lumière bleue et peut être utilisé pour des constructions magiques.
 */
public class BlueGlowingStoneBlock extends GlowingBlock {

    // Intensité de la lumière bleue (0.0 à 1.0)
    private float blueIntensity;

    // Indique si la pierre peut être utilisée comme source d'énergie
    private boolean isEnergySource;

    /**
     * Constructeur du bloc de pierre bleutée lumineuse.
     */
    public BlueGlowingStoneBlock() {
        super(
                BlockType.BLUE_GLOWING_STONE,
                0.7f, // Niveau de lumière
                new Vector3f(0.0f, 0.5f, 1.0f), // Couleur de la lumière (bleue)
                6 // Rayon de la lumière
        );
        this.blueIntensity = 0.8f;
        this.isEnergySource = true;
    }

    /**
     * Active le clignotement de la pierre bleutée.
     */
    public void enablePulsing() {
        setFlickering(true);
        setFlickerFrequency(1.0f); // Clignotement toutes les secondes
    }

    /**
     * Fournit de l'énergie.
     * @param amount Montant d'énergie à fournir.
     * @return Montant d'énergie fourni.
     */
    public float provideEnergy(float amount) {
        if (!isEnergySource) {
            return 0.0f;
        }

        // Logique pour fournir de l'énergie
        float energyProvided = Math.min(amount, blueIntensity * 10.0f);
        blueIntensity -= energyProvided / 10.0f;

        if (blueIntensity < 0) {
            blueIntensity = 0;
        }

        // Mettre à jour le niveau de lumière en fonction de l'intensité
        setLightLevel(0.7f * blueIntensity);

        return energyProvided;
    }

    /**
     * Recharge l'énergie de la pierre bleutée.
     * @param amount Montant d'énergie à recharger.
     */
    public void rechargeEnergy(float amount) {
        blueIntensity = Math.min(1.0f, blueIntensity + amount / 10.0f);
        setLightLevel(0.7f * blueIntensity);
    }

    // --- Getters et Setters ---

    public float getBlueIntensity() {
        return blueIntensity;
    }

    public void setBlueIntensity(float blueIntensity) {
        this.blueIntensity = Math.max(0.0f, Math.min(1.0f, blueIntensity));
        setLightLevel(0.7f * blueIntensity);
    }

    public boolean isEnergySource() {
        return isEnergySource;
    }

    public void setEnergySource(boolean isEnergySource) {
        this.isEnergySource = isEnergySource;
    }

    /**
     * Retourne une représentation textuelle du bloc de pierre bleutée lumineuse.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "BlueGlowingStoneBlock{" +
                "blueIntensity=" + blueIntensity +
                ", isEnergySource=" + isEnergySource +
                ", lightLevel=" + getLightLevel() +
                '}';
    }
}