package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de Gravité Inversée - inverse la gravité dans sa zone d'effet.
 */
public class ReverseGravityBlock extends Block {
    private float affectRadius;
    private float gravityMultiplier;
    private boolean isActive;
    private Vector3f center;

    /**
     * Crée un bloc de gravité inversée.
     */
    public ReverseGravityBlock() {
        super(BlockType.OBSIDIAN);
        this.affectRadius = 15.0f;
        this.gravityMultiplier = -1.0f;
        this.isActive = true;
        this.center = new Vector3f();
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            affectRadius = Math.min(20.0f, affectRadius + deltaTime * 0.5f);
        }
    }

    public float getAffectRadius() {
        return affectRadius;
    }

    public void setAffectRadius(float radius) {
        this.affectRadius = radius;
    }

    public float getGravityMultiplier() {
        return gravityMultiplier;
    }

    public void setGravityMultiplier(float multiplier) {
        this.gravityMultiplier = multiplier;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(float x, float y, float z) {
        this.center.set(x, y, z);
    }

    public boolean affectsPosition(float x, float y, float z) {
        float distance = (float) Math.sqrt(
            (x - center.x()) * (x - center.x()) +
            (y - center.y()) * (y - center.y()) +
            (z - center.z()) * (z - center.z())
        );
        return distance <= affectRadius;
    }
}
