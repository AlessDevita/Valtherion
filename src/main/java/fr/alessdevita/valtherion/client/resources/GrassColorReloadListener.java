package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * Listener de rechargement qui lit la colormap d'herbe
 * (textures/misc/grasscolor.png) et l'expose sous forme de tableau RGB.
 */
public final class GrassColorReloadListener implements IResourceManagerReloadListener {
    private static final ResourceLocation GRASS_COLOR_LOCATION =
            new ResourceLocation("valtherion", "misc/grasscolor.png");

    private int[] grassColors = new int[256 * 256];

    public int[] getGrassColors() {
        return this.grassColors;
    }

    public int getGrassColor(double temperature, double humidity) {
        humidity *= temperature;
        int x = (int) ((1.0 - temperature) * 255.0);
        int y = (int) ((1.0 - humidity) * 255.0);
        x = Math.max(0, Math.min(255, x));
        y = Math.max(0, Math.min(255, y));
        return this.grassColors[y * 256 + x];
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        if (resourceManager == null) return;
        IResource resource = resourceManager.getResource(GRASS_COLOR_LOCATION);
        if (resource == null) {
            this.grassColors = new int[256 * 256];
            return;
        }
        try (InputStream stream = resource.getInputStream()) {
            this.grassColors = FoliageColorReloadListener.loadColormap(stream);
        } catch (IOException e) {
            this.grassColors = new int[256 * 256];
        }
    }
}
