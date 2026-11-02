package fr.alessdevita.valtherion.world;

import org.joml.Vector3f;

/**
 * Provider pour la dimension du Nether (Enfer).
 * Pas de ciel, brouillard rouge, température élevée.
 */
public class WorldProviderHell extends WorldProvider {

    public WorldProviderHell() {
        this.hasSkyLight = false;
        this.isHellWorld = true;
        this.isSurfaceWorld = false;
        this.gravity = new Vector3f(0, -9.81f, 0);
        this.skyColor = 0x3F0000;
        this.fogColor = 0x660000;
        this.temperature = 1.0f;
        this.humidity = 0.0f;
    }

    @Override
    public int getSkyColor(float worldTime) {
        return 0x3F0000; // Toujours sombre dans le Nether
    }

    @Override
    public int getSkyHeight() {
        return 128; // Hauteur réduite dans le Nether
    }
}
