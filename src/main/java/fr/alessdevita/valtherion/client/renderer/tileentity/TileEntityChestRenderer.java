package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/** Rendu simplifié d'un coffre (boîte + couvercle). */
public class TileEntityChestRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(Object tileEntity, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        bindTexture("textures/entity/chest/normal.png");
        drawChestBody();
        GL11.glTranslatef(0F, 0.625F, 0.0625F);
        GL11.glRotatef(-35F, 1F, 0F, 0F);
        drawLid();
        GL11.glPopMatrix();
    }

    private static void drawChestBody() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(140, 90, 40, 255);
        box(tessellator, 0.0625F, 0F, 0.0625F, 0.9375F, 0.625F, 0.9375F);
        tessellator.draw();
    }

    private static void drawLid() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(160, 105, 50, 255);
        box(tessellator, 0.0625F, 0F, 0F, 0.9375F, 0.25F, 0.875F);
        tessellator.draw();
    }

    private static void box(Tessellator t, float minX, float minY, float minZ,
                            float maxX, float maxY, float maxZ) {
        t.addVertexWithUV(minX, minY, minZ, 0, 0); t.addVertexWithUV(maxX, minY, minZ, 1, 0);
        t.addVertexWithUV(maxX, maxY, minZ, 1, 1); t.addVertexWithUV(minX, maxY, minZ, 0, 1);
        t.addVertexWithUV(minX, minY, maxZ, 0, 0); t.addVertexWithUV(minX, maxY, maxZ, 0, 1);
        t.addVertexWithUV(maxX, maxY, maxZ, 1, 1); t.addVertexWithUV(maxX, minY, maxZ, 1, 0);
        t.addVertexWithUV(minX, minY, maxZ, 0, 0); t.addVertexWithUV(minX, minY, minZ, 1, 0);
        t.addVertexWithUV(minX, maxY, minZ, 1, 1); t.addVertexWithUV(minX, maxY, maxZ, 0, 1);
        t.addVertexWithUV(maxX, minY, minZ, 0, 0); t.addVertexWithUV(maxX, minY, maxZ, 1, 0);
        t.addVertexWithUV(maxX, maxY, maxZ, 1, 1); t.addVertexWithUV(maxX, maxY, minZ, 0, 1);
        t.addVertexWithUV(minX, maxY, minZ, 0, 0); t.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        t.addVertexWithUV(maxX, maxY, maxZ, 1, 1); t.addVertexWithUV(minX, maxY, maxZ, 0, 1);
        t.addVertexWithUV(minX, minY, maxZ, 0, 0); t.addVertexWithUV(maxX, minY, maxZ, 1, 0);
        t.addVertexWithUV(maxX, minY, minZ, 1, 1); t.addVertexWithUV(minX, minY, minZ, 0, 1);
    }
}
