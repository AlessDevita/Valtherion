package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Listener de rechargement qui lit la colormap de feuillage
 * (textures/misc/foliagecolor.png) et l'expose sous forme de tableau RGB.
 */
public final class FoliageColorReloadListener implements IResourceManagerReloadListener {
    private static final ResourceLocation FOLIAGE_COLOR_LOCATION =
            new ResourceLocation("valtherion", "misc/foliagecolor.png");

    private int[] foliageColors = new int[256 * 256];

    public int[] getFoliageColors() {
        return this.foliageColors;
    }

    public int getFoliageColor(double temperature, double humidity) {
        humidity *= temperature;
        int x = (int) ((1.0 - temperature) * 255.0);
        int y = (int) ((1.0 - humidity) * 255.0);
        x = Math.max(0, Math.min(255, x));
        y = Math.max(0, Math.min(255, y));
        return this.foliageColors[y * 256 + x];
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (resourceManager == null) return;
        IResource resource = resourceManager.getResource(FOLIAGE_COLOR_LOCATION);
        if (resource == null) {
            this.foliageColors = new int[256 * 256];
            return;
        }
        try (InputStream stream = resource.getInputStream()) {
            this.foliageColors = loadColormap(stream);
        } catch (IOException e) {
            this.foliageColors = new int[256 * 256];
        }
    }

    static int[] loadColormap(InputStream stream) throws IOException {
        BufferedImage image = javax.imageio.ImageIO.read(stream);
        if (image == null) return new int[256 * 256];
        int width = Math.min(image.getWidth(), 256);
        int height = Math.min(image.getHeight(), 256);
        int[] colors = new int[256 * 256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colors[y * 256 + x] = image.getRGB(x, y);
            }
        }
        return colors;
    }
}
