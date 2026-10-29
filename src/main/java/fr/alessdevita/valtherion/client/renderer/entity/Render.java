package fr.alessdevita.valtherion.client.renderer.entity;

import fr.alessdevita.valtherion.entity.Entity;
import org.lwjgl.opengl.GL11;

/** Base renderer for a world entity. */
public class Render {
    protected float shadowSize = 0.5F;

    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity == null) return;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(yaw, 0F, 1F, 0F);
        renderModel(entity, partialTicks);
        GL11.glPopMatrix();
    }

    protected void renderModel(Entity entity, float partialTicks) {
        GL11.glColor3f(0.65F, 0.65F, 0.7F);
        drawBox(-0.3F, 0F, -0.3F, 0.3F, 1.6F, 0.3F);
    }

    protected void drawBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(minX,minY,minZ); GL11.glVertex3f(maxX,minY,minZ); GL11.glVertex3f(maxX,maxY,minZ); GL11.glVertex3f(minX,maxY,minZ);
        GL11.glVertex3f(maxX,minY,maxZ); GL11.glVertex3f(minX,minY,maxZ); GL11.glVertex3f(minX,maxY,maxZ); GL11.glVertex3f(maxX,maxY,maxZ);
        GL11.glVertex3f(minX,minY,maxZ); GL11.glVertex3f(minX,minY,minZ); GL11.glVertex3f(minX,maxY,minZ); GL11.glVertex3f(minX,maxY,maxZ);
        GL11.glVertex3f(maxX,minY,minZ); GL11.glVertex3f(maxX,minY,maxZ); GL11.glVertex3f(maxX,maxY,maxZ); GL11.glVertex3f(maxX,maxY,minZ);
        GL11.glVertex3f(minX,maxY,minZ); GL11.glVertex3f(maxX,maxY,minZ); GL11.glVertex3f(maxX,maxY,maxZ); GL11.glVertex3f(minX,maxY,maxZ);
        GL11.glVertex3f(minX,minY,maxZ); GL11.glVertex3f(maxX,minY,maxZ); GL11.glVertex3f(maxX,minY,minZ); GL11.glVertex3f(minX,minY,minZ);
        GL11.glEnd();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public float getShadowSize() { return this.shadowSize; }
    public void setShadowSize(float size) { this.shadowSize = Math.max(0F, size); }
}
