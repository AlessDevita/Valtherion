package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.client.Valtherion;
import org.lwjgl.opengl.GL11;

/** Rendu de l'objet tenu en main (vue première personne). */
public class ItemRenderer {
    private final Valtherion valtherion;
    private float equippedProgress;
    private float prevEquippedProgress;

    public ItemRenderer(Valtherion valtherion) {
        this.valtherion = valtherion;
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        float target = 1.0F;
        float delta = target - this.equippedProgress;
        if (delta > 0.4F) delta = 0.4F;
        if (delta < -0.4F) delta = -0.4F;
        this.equippedProgress += delta;
    }

    public void renderItemInFirstPerson(float partialTicks) {
        float progress = this.prevEquippedProgress
                + (this.equippedProgress - this.prevEquippedProgress) * partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslatef(0.56F, -0.52F + (1.0F - progress) * -0.6F, -0.72F);
        GL11.glRotatef(45F, 0F, 1F, 0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glColor4f(0.85F, 0.85F, 0.9F, 1F);
        drawHeldCube();
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private static void drawHeldCube() {
        float s = 0.2F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(210, 210, 220, 255);
        tessellator.addVertexWithUV(-s, -s, -s, 0, 0);
        tessellator.addVertexWithUV(s, -s, -s, 1, 0);
        tessellator.addVertexWithUV(s, s, -s, 1, 1);
        tessellator.addVertexWithUV(-s, s, -s, 0, 1);
        tessellator.addVertexWithUV(-s, -s, s, 0, 0);
        tessellator.addVertexWithUV(-s, s, s, 0, 1);
        tessellator.addVertexWithUV(s, s, s, 1, 1);
        tessellator.addVertexWithUV(s, -s, s, 1, 0);
        tessellator.draw();
    }

    public void resetEquippedProgress() {
        this.equippedProgress = 0.0F;
    }

    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0F;
        this.prevEquippedProgress = 0.0F;
    }

    public float getEquippedProgress() {
        return this.equippedProgress;
    }

    public Valtherion getClient() {
        return this.valtherion;
    }
}
