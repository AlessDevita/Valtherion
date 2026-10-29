package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de Téléporteur Local - téléporte entre deux points du monde.
 */
public class LocalTeleporterBlock extends Block {
    private Vector3f destinationPosition;
    private boolean isActive;
    private float teleportCooldown;

    /**
     * Crée un téléporteur local.
     */
    public LocalTeleporterBlock() {
        super(BlockType.OBSIDIAN);
        this.destinationPosition = new Vector3f();
        this.isActive = true;
        this.teleportCooldown = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (teleportCooldown > 0.0f) {
            teleportCooldown -= deltaTime;
        }
    }

    public Vector3f getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(float x, float y, float z) {
        this.destinationPosition.set(x, y, z);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void teleport() {
        if (isActive && teleportCooldown <= 0.0f) {
            teleportCooldown = 2.0f;
        }
    }

    public boolean canTeleport() {
        return isActive && teleportCooldown <= 0.0f;
    }

    public float getTeleportCooldown() {
        return teleportCooldown;
    }
}
