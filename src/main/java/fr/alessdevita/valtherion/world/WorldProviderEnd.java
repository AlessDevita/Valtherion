package fr.alessdevita.valtherion.world;

import org.joml.Vector3f;

/**
 * Provider pour la dimension de l'End.
 * Ciel noir, étoiles, gravité réduite.
 */
public class WorldProviderEnd extends WorldProvider {

    public WorldProviderEnd() {
        this.hasSkyLight = true;
        this.isHellWorld = false;
        this.isSurfaceWorld = false;
        this.gravity = new Vector3f(0, -5.0f, 0); // Gravité réduite
        this.skyColor = 0x000000;
        this.fogColor = 0x000000;
        this.temperature = 0.0f;
        this.humidity = 0.0f;
    }

    @Override
    public int getSkyColor(float worldTime) {
        return 0x000000; // Toujours noir dans l'End
    }
}
