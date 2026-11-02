package fr.alessdevita.valtherion.world;

import org.joml.Vector3f;

/**
 * Provider pour la dimension de surface (Overworld).
 * Ciel bleu, gravité normale, cycle jour/nuit.
 */
public class WorldProviderSurface extends WorldProvider {

    public WorldProviderSurface() {
        this.hasSkyLight = true;
        this.isHellWorld = false;
        this.isSurfaceWorld = true;
        this.gravity = new Vector3f(0, -9.81f, 0);
        this.skyColor = 0x87CEEB;
        this.fogColor = 0xFFFFFF;
        this.temperature = 0.5f;
        this.humidity = 0.5f;
    }
}
