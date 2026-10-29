package fr.alessdevita.valtherion.client.renderer.entity;

import fr.alessdevita.valtherion.entity.Entity;
import org.lwjgl.opengl.GL11;

/** Orbe d'expérience : petit cube irisé. */
public class RenderXPOrb extends Render {
    public RenderXPOrb() {
        setShadowSize(0.15F);
    }

    @Override
    protected void renderModel(Entity entity, float partialTicks) {
        float pulse = 0.55F + 0.45F * (float) Math.sin(System.currentTimeMillis() * 0.01D);
        GL11.glColor3f(0.2F + pulse * 0.4F, 0.9F, 0.35F + pulse * 0.2F);
        drawBox(-0.12F, 0F, -0.12F, 0.12F, 0.24F, 0.12F);
    }
}
