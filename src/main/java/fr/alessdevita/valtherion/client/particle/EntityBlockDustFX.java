package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

import java.util.concurrent.ThreadLocalRandom;

/** Éclat coloré produit par la destruction ou l'impact sur un bloc. */
public class EntityBlockDustFX extends EntityFX {
    public EntityBlockDustFX() { this(null, 0, 0, 0, 0, 0.04, 0, 0xFF8A8175); }

    public EntityBlockDustFX(WorldClient world, double x, double y, double z,
                             double motionX, double motionY, double motionZ, int color) {
        super(world, x, y, z, motionX, motionY, motionZ);
        setColor((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F);
        this.particleScale = 0.045F + ThreadLocalRandom.current().nextFloat() * 0.045F;
        this.gravity = 0.08F;
        this.friction = 0.88F;
        this.maxAge = 12 + ThreadLocalRandom.current().nextInt(12);
    }

    public static int colorFor(BlockType type) {
        if (type == null) return 0xFF8A8175;
        switch (type) {
            case GRASS: case LEAVES: case VINE: case BAMBOO: return 0xFF659447;
            case DIRT: case SOUL_SAND: return 0xFF79563A;
            case SAND: case SANDSTONE: return 0xFFD8C27C;
            case WATER: case ICE: case CORAL: return 0xFF6DB9D1;
            case LAVA: case FIRE: case GLOWSTONE: return 0xFFFF8A32;
            case OBSIDIAN: case BLACKSTONE: case BASALT: return 0xFF514C5D;
            case BLUE_GLOWING_STONE: return 0xFF55BDF1;
            case PURPLE_GLOWING_STONE: case HANGING_CRYSTAL: return 0xFFAE70E8;
            default: return 0xFF96928C;
        }
    }
}
