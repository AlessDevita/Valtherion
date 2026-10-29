package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc d'Ascenseur Magique - transporte les joueurs verticalement.
 */
public class MagicElevatorBlock extends Block {
    private Vector3f destination;
    private float speed;
    private boolean isActive;
    private float transportTime;

    /**
     * Crée un ascenseur magique.
     */
    public MagicElevatorBlock() {
        super(BlockType.OBSIDIAN);
        this.destination = new Vector3f();
        this.speed = 5.0f;
        this.isActive = true;
        this.transportTime = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isActive && transportTime > 0.0f) {
            transportTime -= deltaTime;
        }
    }

    public Vector3f getDestination() {
        return destination;
    }

    public void setDestination(float x, float y, float z) {
        this.destination.set(x, y, z);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void transport() {
        if (isActive) {
            transportTime = 2.0f;
        }
    }

    public boolean isTransporting() {
        return transportTime > 0.0f;
    }
}
