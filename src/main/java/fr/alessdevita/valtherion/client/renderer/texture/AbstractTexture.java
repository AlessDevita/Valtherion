package fr.alessdevita.valtherion.client.renderer.texture;

import fr.alessdevita.valtherion.client.renderer.GLAllocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/** Base des textures OpenGL avec allocation paresseuse de l'identifiant GPU. */
public abstract class AbstractTexture implements ITextureObject {
    protected int glTextureId;
    private boolean loaded;

    @Override
    public int getGlTextureId() {
        if (this.glTextureId == 0) this.glTextureId = GLAllocation.generateTexture();
        return this.glTextureId;
    }

    protected void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getGlTextureId());
    }

    protected void setLoaded(boolean loaded) { this.loaded = loaded; }
    @Override
    public boolean isLoaded() { return this.loaded; }

    @Override
    public void deleteGlTexture() {
        if (this.glTextureId != 0) {
            GLAllocation.deleteTexture(this.glTextureId);
            this.glTextureId = 0;
        }
        this.loaded = false;
    }

    @Override
    public abstract void loadTexture() throws IOException;
}
