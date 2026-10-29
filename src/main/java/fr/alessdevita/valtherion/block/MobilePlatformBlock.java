package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de Plateforme Mobile - plateforme qui se déplace.
 */
public class MobilePlatformBlock extends Block {
    private Vector3f position;
    private Vector3f velocity;
    private Vector3f targetPosition;
    private float speed;
    private boolean isMoving;

    /**
     * Crée une plateforme mobile.
     */
    public MobilePlatformBlock() {
        super(BlockType.OBSIDIAN);
        this.position = new Vector3f();
        this.velocity = new Vector3f();
        this.targetPosition = new Vector3f();
        this.speed = 3.0f;
        this.isMoving = false;
    }

    @Override
    public void update(float deltaTime) {
        if (isMoving) {
            Vector3f direction = new Vector3f(targetPosition).sub(position);
            if (direction.length() > 0.1f) {
                direction.normalize().mul(speed * deltaTime);
                position.add(direction);
            } else {
                isMoving = false;
            }
        }
    }

    public void moveTo(float x, float y, float z) {
        targetPosition.set(x, y, z);
        isMoving = true;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTargetPosition() {
        return targetPosition;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return isMoving;
    }
}
