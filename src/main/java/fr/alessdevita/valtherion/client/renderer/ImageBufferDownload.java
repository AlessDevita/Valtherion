package fr.alessdevita.valtherion.client.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/** Normalise une image de skin et retire l'alpha invalide des calques. */
public class ImageBufferDownload implements IImageBuffer {
    private final Runnable completion;
    public ImageBufferDownload() { this(null); }
    public ImageBufferDownload(Runnable completion) { this.completion = completion; }

    @Override
    public BufferedImage parseUserSkin(BufferedImage source) {
        if (source == null) return null;
        BufferedImage skin = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = skin.createGraphics();
        try { graphics.drawImage(source, 0, 0, 64, 64, null); }
        finally { graphics.dispose(); }
        clearTransparentRegions(skin);
        return skin;
    }

    private static void clearTransparentRegions(BufferedImage image) {
        clearIfOpaque(image, 32, 0, 64, 32);
        clearIfOpaque(image, 0, 32, 16, 48);
        clearIfOpaque(image, 16, 32, 40, 48);
        clearIfOpaque(image, 40, 32, 64, 48);
        clearIfOpaque(image, 0, 48, 16, 64);
        clearIfOpaque(image, 16, 48, 40, 64);
        clearIfOpaque(image, 40, 48, 64, 64);
    }

    private static void clearIfOpaque(BufferedImage image, int left, int top, int right, int bottom) {
        boolean hasTransparency = false;
        for (int y = top; y < bottom && !hasTransparency; y++) {
            for (int x = left; x < right; x++) {
                if ((image.getRGB(x, y) >>> 24) < 128) { hasTransparency = true; break; }
            }
        }
        if (!hasTransparency) {
            for (int y = top; y < bottom; y++) for (int x = left; x < right; x++) {
                image.setRGB(x, y, image.getRGB(x, y) & 0x00FFFFFF);
            }
        }
    }

    @Override public void skinAvailable() { if (this.completion != null) this.completion.run(); }
}
