package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Faille Dimensionnelle - bloc qui crée une faille entre les dimensions.
 */
public class DimensionalRift extends Block {
    private float riftPower;
    private float destabilization;
    private Vector3f linkedLocation;
    private boolean isOpen;

    /**
     * Crée une faille dimensionnelle.
     */
    public DimensionalRift() {
        super(BlockType.OBSIDIAN);
        this.riftPower = 0.5f;
        this.destabilization = 0.0f;
        this.linkedLocation = new Vector3f();
        this.isOpen = false;
    }

    @Override
    public void update(float deltaTime) {
        if (isOpen) {
            destabilization += deltaTime * 0.2f;
            if (destabilization > 1.0f) {
                collapse();
            }
        }
    }

    public float getRiftPower() {
        return riftPower;
    }

    public void setRiftPower(float power) {
        this.riftPower = Math.max(0.0f, Math.min(1.0f, power));
    }

    public float getDestabilization() {
        return destabilization;
    }

    public Vector3f getLinkedLocation() {
        return linkedLocation;
    }

    public void setLinkedLocation(float x, float y, float z) {
        this.linkedLocation.set(x, y, z);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
        destabilization = 0.0f;
    }

    public void close() {
        isOpen = false;
        destabilization = 0.0f;
    }

    private void collapse() {
        close();
    }
}
