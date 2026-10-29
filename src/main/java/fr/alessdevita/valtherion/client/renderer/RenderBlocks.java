package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;

/** Rendu cubique immédiat des blocs, en attendant un atlas de textures complet. */
public class RenderBlocks {
    public boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        return renderStandardBlock(block, x, y, z);
    }

    public boolean renderStandardBlock(Block block, int x, int y, int z) {
        if (block == null || block.getType() == BlockType.AIR) return false;
        Tessellator tessellator = Tessellator.instance;
        float[] color = colorFor(block.getType());
        tessellator.startDrawingQuads();

        // Haut
        shade(tessellator, color, 1F);
        vertex(tessellator, x, y + 1, z, 0, 0); vertex(tessellator, x + 1, y + 1, z, 1, 0);
        vertex(tessellator, x + 1, y + 1, z + 1, 1, 1); vertex(tessellator, x, y + 1, z + 1, 0, 1);
        // Bas
        shade(tessellator, color, 0.5F);
        vertex(tessellator, x, y, z + 1, 0, 0); vertex(tessellator, x + 1, y, z + 1, 1, 0);
        vertex(tessellator, x + 1, y, z, 1, 1); vertex(tessellator, x, y, z, 0, 1);
        // Faces latérales
        shade(tessellator, color, 0.8F);
        vertex(tessellator, x, y, z, 0, 0); vertex(tessellator, x + 1, y, z, 1, 0);
        vertex(tessellator, x + 1, y + 1, z, 1, 1); vertex(tessellator, x, y + 1, z, 0, 1);
        shade(tessellator, color, 0.65F);
        vertex(tessellator, x + 1, y, z + 1, 0, 0); vertex(tessellator, x, y, z + 1, 1, 0);
        vertex(tessellator, x, y + 1, z + 1, 1, 1); vertex(tessellator, x + 1, y + 1, z + 1, 0, 1);
        shade(tessellator, color, 0.72F);
        vertex(tessellator, x, y, z + 1, 0, 0); vertex(tessellator, x, y, z, 1, 0);
        vertex(tessellator, x, y + 1, z, 1, 1); vertex(tessellator, x, y + 1, z + 1, 0, 1);
        vertex(tessellator, x + 1, y, z, 0, 0); vertex(tessellator, x + 1, y, z + 1, 1, 0);
        vertex(tessellator, x + 1, y + 1, z + 1, 1, 1); vertex(tessellator, x + 1, y + 1, z, 0, 1);
        tessellator.draw();
        return true;
    }

    private static void shade(Tessellator tessellator, float[] color, float brightness) {
        tessellator.setColorRGBA((int) (color[0] * brightness * 255),
                (int) (color[1] * brightness * 255), (int) (color[2] * brightness * 255), 255);
    }

    private static void vertex(Tessellator tessellator, double x, double y, double z, double u, double v) {
        tessellator.addVertexWithUV(x, y, z, u, v);
    }

    private static float[] colorFor(BlockType type) {
        switch (type) {
            case GRASS: case LEAVES: case VINE: case BAMBOO: return new float[]{0.36F, 0.62F, 0.27F};
            case DIRT: case SOUL_SAND: return new float[]{0.48F, 0.32F, 0.2F};
            case SAND: case SANDSTONE: return new float[]{0.82F, 0.72F, 0.48F};
            case WATER: case ICE: return new float[]{0.32F, 0.62F, 0.86F};
            case LAVA: case FIRE: return new float[]{0.95F, 0.32F, 0.08F};
            case WOOD: return new float[]{0.48F, 0.34F, 0.2F};
            case BLUE_GLOWING_STONE: return new float[]{0.25F, 0.68F, 0.95F};
            case PURPLE_GLOWING_STONE: return new float[]{0.62F, 0.3F, 0.82F};
            case OBSIDIAN: case BASALT: case BLACKSTONE: return new float[]{0.26F, 0.24F, 0.3F};
            default: return new float[]{0.56F, 0.56F, 0.56F};
        }
    }
}
