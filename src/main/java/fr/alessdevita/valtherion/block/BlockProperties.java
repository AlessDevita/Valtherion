package fr.alessdevita.valtherion.block;

/**
 * Propriétés physiques et visuelles d'un bloc.
 */
public class BlockProperties {
    private boolean solid;
    private boolean opaque;
    private float hardness;
    private float lightLevel;

    public BlockProperties(boolean solid, boolean opaque) {
        this.solid = solid;
        this.opaque = opaque;
        this.hardness = 1.0f;
        this.lightLevel = 0.0f;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public float getHardness() {
        return hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    /**
     * Retourne le niveau de lumière émis par le bloc.
     * @return Niveau de lumière (0.0 à 1.0)
     */
    public float getLightLevel() {
        return lightLevel;
    }

    /**
     * Définit le niveau de lumière émis par le bloc.
     * @param lightLevel Niveau de lumière (0.0 à 1.0)
     */
    public void setLightLevel(float lightLevel) {
        this.lightLevel = lightLevel;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }
}