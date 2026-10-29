package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de Portail Dimensionnel - téléporte vers une autre dimension.
 */
public class DimensionalPortalBlock extends Block {
    private int targetDimension;
    private Vector3f targetPosition;
    private boolean isOpen;
    private float instability;

    /**
     * Crée un portail dimensionnel.
     */
    public DimensionalPortalBlock() {
        super(BlockType.OBSIDIAN);
        this.targetDimension = 0;
        this.targetPosition = new Vector3f();
        this.isOpen = false;
        this.instability = 0.5f;
    }

    @Override
    public void update(float deltaTime) {
        if (isOpen) {
            instability += deltaTime * 0.1f;
            if (instability > 1.0f) {
                close();
            }
        }
    }

    public int getTargetDimension() {
        return targetDimension;
    }

    public void setTargetDimension(int dimension) {
        this.targetDimension = dimension;
    }

    public Vector3f getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(float x, float y, float z) {
        this.targetPosition.set(x, y, z);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
        this.instability = 0.0f;
    }

    public void close() {
        this.isOpen = false;
    }

    public float getInstability() {
        return instability;
    }
}
