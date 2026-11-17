package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.entity.Entity;
import org.lwjgl.opengl.GL11;

public class EntityRenderer {
    private final Valtherion client;
    public final ItemRenderer itemRenderer;

    public EntityRenderer(Valtherion valtherion) {
        this.client = valtherion;
        this.itemRenderer = new ItemRenderer(valtherion);
    }

    public void updateCameraAndRender(Object renderPartialTicks, long l) {
        float partialTicks = renderPartialTicks instanceof Number
                ? ((Number) renderPartialTicks).floatValue() : 0.0F;
        updateCameraAndRender(partialTicks);
    }

    public void updateCameraAndRender(float partialTicks) {
        int width = Math.max(1, this.client.displayWidth);
        int height = Math.max(1, this.client.displayHeight);
        double near = 0.05D;
        double top = near * Math.tan(Math.toRadians(70.0D) * 0.5D);
        double right = top * (double) width / height;

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glFrustum(-right, right, -top, top, near, 512.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Entity camera = this.client.renderViewEntity;
        if (camera != null) {
            org.joml.Vector3f position = camera.getPosition();

            // Appliquer la rotation de la caméra (yaw et pitch)
            if (camera instanceof fr.alessdevita.valtherion.client.entity.AbstractClientPlayer) {
                fr.alessdevita.valtherion.client.entity.AbstractClientPlayer player = 
                    (fr.alessdevita.valtherion.client.entity.AbstractClientPlayer) camera;
                float yaw = player.getRotationYaw();
                float pitch = player.getRotationPitch();

                // Rotation autour de l'axe Y (yaw - rotation horizontale)
                GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
                // Rotation autour de l'axe X (pitch - rotation verticale)
                GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
            }

            // Translation après la rotation pour déplacer la caméra
            GL11.glTranslatef(-position.x, -(position.y + 1.5F), -position.z);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        ActiveRenderInfo.updateRenderInfo(camera, false);
        if (this.client.renderGlobal != null) this.client.renderGlobal.renderWorld(partialTicks);
        if (this.client.effectRenderer != null) this.client.effectRenderer.renderParticles(partialTicks);
        this.itemRenderer.renderItemInFirstPerson(partialTicks);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void getMouseOver(float partialTicks) {
    }

    public void deactivateShader() {
    }

    public void updateRenderer() {
        this.itemRenderer.updateEquippedItem();
    }
}
