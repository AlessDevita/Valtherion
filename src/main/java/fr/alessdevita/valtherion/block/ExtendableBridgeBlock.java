package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de Pont Extensible - pont qui s'étend et se rétracte.
 */
public class ExtendableBridgeBlock extends Block {
    private Vector3f extendTo;
    private float currentLength;
    private float maxLength;
    private boolean isExtended;
    private float extendSpeed;

    /**
     * Crée un pont extensible.
     */
    public ExtendableBridgeBlock() {
        super(BlockType.OBSIDIAN);
        this.extendTo = new Vector3f();
        this.currentLength = 0.0f;
        this.maxLength = 20.0f;
        this.isExtended = false;
        this.extendSpeed = 5.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isExtended) {
            currentLength = Math.min(maxLength, currentLength + extendSpeed * deltaTime);
            if (currentLength >= maxLength) {
                isExtended = true;
            }
        } else {
            currentLength = Math.max(0.0f, currentLength - extendSpeed * deltaTime);
        }
    }

    public void extend() {
        isExtended = true;
    }

    public void retract() {
        isExtended = false;
    }

    public float getCurrentLength() {
        return currentLength;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(float length) {
        this.maxLength = length;
    }

    public boolean isFullyExtended() {
        return currentLength >= maxLength;
    }

    public Vector3f getExtendTo() {
        return extendTo;
    }

    public void setExtendTo(float x, float y, float z) {
        this.extendTo.set(x, y, z);
    }
}
