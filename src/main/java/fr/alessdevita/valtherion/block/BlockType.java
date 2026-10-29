package fr.alessdevita.valtherion.block;

/**
 * Définit les types de blocs disponibles.
 */
public enum BlockType {
    AIR(false, false),
    DIRT(true, true),
    GRASS(true, true),
    SAND(true, true),
    STONE(true, true),
    WATER(false, true),
    WOOD(true, true),
    LEAVES(true, true), LAVA(true, true), FIRE(true, true), ICE(true, true), FLOWER_RED(true, true), FLOWER_YELLOW(true, true), FLOWER_BLUE(true, true), BUSH(true, true), COBBLESTONE(true, true), GRAVEL(true, true), SNOW(true, true), OBSIDIAN(true, true), SANDSTONE(true, true), CACTUS(true, true), GOLD_ORE(true, true), CORAL(true, true), WATER_PLANT(true, true), MUSHROOM(true, true), MUSHROOM_RED(true, true), MUSHROOM_BROWN(true, true), POISON(true, true), VINE(true, true), BAMBOO(true, true), NETHER_RACK(true, true), NETHER_BRICK(true, true), SOUL_SAND(true, true), GLOWSTONE(true, true), BASALT(true, true), BLACKSTONE(true, true), WARPED_STONE(true, true), WARPED_BRICK(true, true), WARPED_VINE(true, true), NETHER_GOLD_ORE(true, true), ASTRAL_ROCK(true, true), BLUE_GLOWING_STONE(true, true), HANGING_CRYSTAL(true, true), LUMINOUS_MUSHROOM(true, true), PURPLE_GLOWING_STONE(true, true);

    private final boolean solid;
    private final boolean opaque;

    BlockType(boolean solid, boolean opaque) {
        this.solid = solid;
        this.opaque = opaque;
    }

    public BlockProperties getDefaultProperties() {
        return new BlockProperties(solid, opaque);
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isOpaque() {
        return opaque;
    }
}