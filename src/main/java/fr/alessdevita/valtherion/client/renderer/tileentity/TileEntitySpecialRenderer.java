package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

/** Base des renderers de tile entities. */
public abstract class TileEntitySpecialRenderer {
    protected TileEntityDispatcher dispatcher;
    protected TextureManager textureManager;

    public void setDispatcher(TileEntityDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        if (dispatcher != null) this.textureManager = dispatcher.getTextureManager();
    }

    public abstract void renderTileEntityAt(Object tileEntity, double x, double y, double z,
                                            float partialTicks);

    protected void bindTexture(String path) {
        if (this.textureManager != null) this.textureManager.bindTexture(path);
        else GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
