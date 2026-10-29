package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.renderer.Tessellator;
import fr.alessdevita.valtherion.client.renderer.entity.Render;
import fr.alessdevita.valtherion.entity.Entity;
import org.lwjgl.opengl.GL11;

/** Cadre d'objet mural (item frame). */
public class RenderItemFrame extends Render {
    public RenderItemFrame() {
        setShadowSize(0.15F);
    }

    @Override
    protected void renderModel(Entity entity, float partialTicks) {
        GL11.glColor4f(0.75F, 0.6F, 0.35F, 1F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(190, 150, 90, 255);
        float half = 0.45F;
        float depth = 0.05F;
        tessellator.addVertexWithUV(-half, -half, 0F, 0, 0);
        tessellator.addVertexWithUV(half, -half, 0F, 1, 0);
        tessellator.addVertexWithUV(half, half, 0F, 1, 1);
        tessellator.addVertexWithUV(-half, half, 0F, 0, 1);
        tessellator.addVertexWithUV(-half, -half, -depth, 0, 0);
        tessellator.addVertexWithUV(-half, half, -depth, 0, 1);
        tessellator.addVertexWithUV(half, half, -depth, 1, 1);
        tessellator.addVertexWithUV(half, -half, -depth, 1, 0);
        tessellator.draw();

        GL11.glTranslatef(0F, 0F, 0.01F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(220, 220, 230, 255);
        float s = 0.4F;
        tessellator.addVertexWithUV(-s, -s, 0F, 0, 0);
        tessellator.addVertexWithUV(s, -s, 0F, 1, 0);
        tessellator.addVertexWithUV(s, s, 0F, 1, 1);
        tessellator.addVertexWithUV(-s, s, 0F, 0, 1);
        tessellator.draw();
    }
}
