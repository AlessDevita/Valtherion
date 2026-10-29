package fr.alessdevita.valtherion.client.renderer.entity;

import fr.alessdevita.valtherion.entity.Entity;
import org.lwjgl.opengl.GL11;

/** Renderer base for living actors; provides a colored body and head. */
public class RendererLivingEntity extends RenderEntity {
    protected float red = 0.55F, green = 0.62F, blue = 0.68F;

    @Override
    protected void renderModel(Entity entity, float partialTicks) {
        GL11.glColor3f(this.red, this.green, this.blue);
        drawBox(-0.28F, 0.35F, -0.18F, 0.28F, 1.35F, 0.18F);
        drawBox(-0.22F, 1.35F, -0.22F, 0.22F, 1.75F, 0.22F);
        drawBox(-0.36F, 0.35F, -0.12F, -0.22F, 1.25F, 0.12F);
        drawBox(0.22F, 0.35F, -0.12F, 0.36F, 1.25F, 0.12F);
        drawBox(-0.24F, 0F, -0.14F, -0.04F, 0.38F, 0.14F);
        drawBox(0.04F, 0F, -0.14F, 0.24F, 0.38F, 0.14F);
    }

    public void setColor(float red, float green, float blue) {
        this.red = Math.max(0F, Math.min(1F, red));
        this.green = Math.max(0F, Math.min(1F, green));
        this.blue = Math.max(0F, Math.min(1F, blue));
    }
}
