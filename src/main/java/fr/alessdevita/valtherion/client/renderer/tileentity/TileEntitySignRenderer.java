package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.model.ModelSign;
import org.lwjgl.opengl.GL11;

/** Rendu d'un panneau via {@link ModelSign}. */
public class TileEntitySignRenderer extends TileEntitySpecialRenderer {
    private final ModelSign model = new ModelSign();

    @Override
    public void renderTileEntityAt(Object tileEntity, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.75D, z + 0.5D);
        bindTexture("textures/entity/sign.png");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        float scale = 0.0625F;
        this.model.renderSignBoard(scale);
        this.model.renderSignStick(scale);
        GL11.glPopMatrix();
    }
}
