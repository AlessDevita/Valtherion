package fr.alessdevita.valtherion.block;

import fr.alessdevita.valtherion.block.material.Material;

/**
 * Représente un bloc individuel dans le monde.
 */
public abstract class Block {
    private final BlockType type;
    private final BlockProperties properties;

    public Block(BlockType type) {
        this.type = type;
        this.properties = type.getDefaultProperties();
    }

    public BlockType getType() {
        return type;
    }

    public BlockProperties getProperties() {
        return properties;
    }

    public boolean isLiquid() {
        return this.type == BlockType.WATER || this.type == BlockType.LAVA;
    }

    public boolean isWater() {
        return this.type == BlockType.WATER;
    }

    public boolean isLava() {
        return this.type == BlockType.LAVA;
    }

    public boolean isSolid() {
        return this.type.isSolid();
    }

    public Material getMaterial() {
        if (this.type == BlockType.AIR) {
            return Material.air;
        }
        if (this.type == BlockType.WATER) {
            return Material.water;
        }
        return Material.rock;
    }

    public int getExperienceDropped() {
        return 0;
    }

    public abstract void update(float deltaTime);
}
