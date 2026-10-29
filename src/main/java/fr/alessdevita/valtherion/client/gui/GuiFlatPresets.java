package fr.alessdevita.valtherion.client.gui;

import java.util.*;

/**
 * Classe pour gérer les préréglages des mondes plats dans Valtherion.
 * Stocke les préréglages et permet de les appliquer à un monde plat.
 */
public class GuiFlatPresets {

    // Liste des préréglages
    private final List<FlatPreset> presets;

    /**
     * Constructeur des préréglages des mondes plats.
     */
    public GuiFlatPresets() {
        this.presets = new ArrayList<>();

        // Initialiser les préréglages par défaut
        initDefaultPresets();
    }

    /**
     * Initialise les préréglages par défaut.
     */
    private void initDefaultPresets() {
        // Préréglage "Monde Plat Standard"
        FlatPreset standardPreset = new FlatPreset("Monde Plat Standard");
        standardPreset.addLayer(new FlatLayer("GRASS", 1));
        standardPreset.addLayer(new FlatLayer("DIRT", 3));
        standardPreset.addLayer(new FlatLayer("STONE", 5));
        presets.add(standardPreset);

        // Préréglage "Monde Plat Désert"
        FlatPreset desertPreset = new FlatPreset("Monde Plat Désert");
        desertPreset.addLayer(new FlatLayer("SAND", 4));
        desertPreset.addLayer(new FlatLayer("SANDSTONE", 2));
        presets.add(desertPreset);

        // Préréglage "Monde Plat Océan"
        FlatPreset oceanPreset = new FlatPreset("Monde Plat Océan");
        oceanPreset.addLayer(new FlatLayer("WATER", 1));
        oceanPreset.addLayer(new FlatLayer("SAND", 2));
        oceanPreset.addLayer(new FlatLayer("GRAVEL", 1));
        presets.add(oceanPreset);

        // Préréglage "Monde Plat Neige"
        FlatPreset snowPreset = new FlatPreset("Monde Plat Neige");
        snowPreset.addLayer(new FlatLayer("SNOW", 1));
        snowPreset.addLayer(new FlatLayer("ICE", 1));
        snowPreset.addLayer(new FlatLayer("STONE", 3));
        presets.add(snowPreset);
    }

    /**
     * Ajoute un préréglage.
     * @param preset Préréglage à ajouter.
     */
    public void addPreset(FlatPreset preset) {
        presets.add(preset);
    }

    /**
     * Supprime un préréglage.
     * @param presetIndex Index du préréglage à supprimer.
     */
    public void removePreset(int presetIndex) {
        if (presetIndex >= 0 && presetIndex < presets.size()) {
            presets.remove(presetIndex);
        }
    }

    /**
     * Récupère un préréglage.
     * @param presetIndex Index du préréglage.
     * @return Préréglage ou null si non trouvé.
     */
    public FlatPreset getPreset(int presetIndex) {
        if (presetIndex >= 0 && presetIndex < presets.size()) {
            return presets.get(presetIndex);
        }
        return null;
    }

    // --- Getters ---

    public List<FlatPreset> getPresets() {
        return presets;
    }

    /**
     * Retourne une représentation textuelle des préréglages.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiFlatPresets{" +
                "presets=" + presets.size() +
                '}';
    }

    // --- Classes internes ---

    /**
     * Classe pour représenter une couche de monde plat.
     */
    public static class FlatLayer {
        private String blockType;
        private int height;

        public FlatLayer(String blockType, int height) {
            this.blockType = blockType;
            this.height = height;
        }

        // Getters et Setters
        public String getBlockType() { return blockType; }
        public void setBlockType(String blockType) { this.blockType = blockType; }
        public int getHeight() { return height; }
        public void setHeight(int height) { this.height = height; }
    }

    /**
     * Classe pour représenter un préréglage de monde plat.
     */
    public static class FlatPreset {
        private String name;
        private final List<FlatLayer> layers;

        public FlatPreset(String name) {
            this.name = name;
            this.layers = new ArrayList<>();
        }

        public void addLayer(FlatLayer layer) {
            layers.add(layer);
        }

        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<FlatLayer> getLayers() { return layers; }
    }
}
