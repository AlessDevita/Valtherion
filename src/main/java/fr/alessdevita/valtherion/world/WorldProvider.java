package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.world.biome.BiomeType;

import org.joml.Vector3f;

/**
 * Classe de base pour les providers de monde.
 * Définit les propriétés communes à toutes les dimensions (ciel, gravité, etc.).
 */
public class WorldProvider {

    protected World world;

    // Propriétés de la dimension
    protected boolean hasSkyLight;
    protected boolean isHellWorld;
    protected boolean isSurfaceWorld;
    protected Vector3f gravity;
    protected int skyColor;
    protected int fogColor;
    protected float temperature;
    protected float humidity;

    public WorldProvider() {
        this.hasSkyLight = true;
        this.isHellWorld = false;
        this.isSurfaceWorld = true;
        this.gravity = new Vector3f(0, -9.81f, 0);
        this.skyColor = 0x87CEEB;
        this.fogColor = 0xFFFFFF;
        this.temperature = 0.5f;
        this.humidity = 0.5f;
    }

    /**
     * Initialise le provider avec un monde.
     * @param world Monde associé
     */
    public void init(World world) {
        this.world = world;
    }

    /**
     * Retourne la couleur du ciel à un temps donné.
     * @param worldTime Temps du monde
     * @return Couleur ARGB du ciel
     */
    public int getSkyColor(float worldTime) {
        if (!hasSkyLight) {
            return 0x000000;
        }
        float dayFactor = (worldTime % 86400f) / 86400f;
        // Transition jour/nuit
        float brightness = (float) (0.5 + 0.5 * Math.cos(dayFactor * 2 * Math.PI));
        return modulateColor(skyColor, brightness);
    }

    /**
     * Retourne la couleur du brouillard.
     * @return Couleur ARGB
     */
    public int getFogColor() {
        return fogColor;
    }

    /**
     * Retourne la hauteur du ciel.
     * @return Hauteur
     */
    public int getSkyHeight() {
        return isHellWorld ? 128 : 256;
    }

    /**
     * Retourne le type de ciel.
     * @return Type de ciel
     */
    public EnumSkyBlock getSkyType() {
        return hasSkyLight ? EnumSkyBlock.SKY : EnumSkyBlock.UNDERGROUND;
    }

    private int modulateColor(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = Math.max(0, Math.min(255, (int) (red * factor)));
        green = Math.max(0, Math.min(255, (int) (green * factor)));
        blue = Math.max(0, Math.min(255, (int) (blue * factor)));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public World getWorld() { return world; }
    public boolean hasSkyLight() { return hasSkyLight; }
    public boolean isHellWorld() { return isHellWorld; }
    public boolean isSurfaceWorld() { return isSurfaceWorld; }
    public Vector3f getGravity() { return gravity; }
    public int getSkyColor() { return skyColor; }
    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }

    public void setSkyColor(int skyColor) { this.skyColor = skyColor; }
    public void setFogColor(int fogColor) { this.fogColor = fogColor; }
    public void setTemperature(float temperature) { this.temperature = temperature; }
    public void setHumidity(float humidity) { this.humidity = humidity; }
}
