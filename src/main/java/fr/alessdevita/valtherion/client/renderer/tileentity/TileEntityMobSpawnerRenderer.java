package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/** Cage de spawner + orbe interne animé. */
public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(Object tileEntity, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        bindTexture("textures/blocks/mob_spawner.png");
        drawCage();
        float spin = (System.currentTimeMillis() % 3600L) * 0.1F;
        GL11.glTranslatef(0.5F, 0.4F, 0.5F);
        GL11.glRotatef(spin, 0F, 1F, 0F);
        GL11.glScalef(0.35F, 0.35F, 0.35F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(80, 200, 120, 220);
        float s = 0.5F;
        tessellator.addVertexWithUV(-s, -s, -s, 0, 0);
        tessellator.addVertexWithUV(s, -s, -s, 1, 0);
        tessellator.addVertexWithUV(s, s, -s, 1, 1);
        tessellator.addVertexWithUV(-s, s, -s, 0, 1);
        tessellator.addVertexWithUV(-s, -s, s, 0, 0);
        tessellator.addVertexWithUV(-s, s, s, 0, 1);
        tessellator.addVertexWithUV(s, s, s, 1, 1);
        tessellator.addVertexWithUV(s, -s, s, 1, 0);
        tessellator.draw();
        GL11.glPopMatrix();
    }

    private static void drawCage() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(40, 40, 50, 180);
        float inset = 0.05F;
        tessellator.addVertexWithUV(inset, inset, inset, 0, 0);
        tessellator.addVertexWithUV(1F - inset, inset, inset, 1, 0);
        tessellator.addVertexWithUV(1F - inset, 1F - inset, inset, 1, 1);
        tessellator.addVertexWithUV(inset, 1F - inset, inset, 0, 1);
        tessellator.addVertexWithUV(inset, inset, 1F - inset, 0, 0);
        tessellator.addVertexWithUV(inset, 1F - inset, 1F - inset, 0, 1);
        tessellator.addVertexWithUV(1F - inset, 1F - inset, 1F - inset, 1, 1);
        tessellator.addVertexWithUV(1F - inset, inset, 1F - inset, 1, 0);
        tessellator.draw();
    }
}
