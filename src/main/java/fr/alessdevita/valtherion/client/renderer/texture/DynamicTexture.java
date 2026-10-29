package fr.alessdevita.valtherion.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

/** Texture dont le contenu est fourni ou modifié en mémoire. */
public class DynamicTexture extends AbstractTexture {
    private BufferedImage image;

    public DynamicTexture(int width, int height) {
        this(new BufferedImage(validDimension(width), validDimension(height), BufferedImage.TYPE_INT_ARGB));
    }

    public DynamicTexture(BufferedImage image) {
        if (image == null) throw new IllegalArgumentException("Image absente.");
        this.image = image;
    }

    public int getTextureData() { return getGlTextureId(); }
    public int getWidth() { return this.image.getWidth(); }
    public int getHeight() { return this.image.getHeight(); }
    public BufferedImage getBufferedImage() { return this.image; }

    public void updateDynamicTexture() {
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), this.image, true, true);
        setLoaded(true);
    }

    @Override
    public void loadTexture() throws IOException { updateDynamicTexture(); }

    public void replaceImage(BufferedImage image) {
        if (image == null) throw new IllegalArgumentException("Image absente.");
        this.image = image;
        updateDynamicTexture();
    }

    private static int validDimension(int value) {
        if (value < 1) throw new IllegalArgumentException("La taille de texture doit être positive.");
        return value;
    }
}
