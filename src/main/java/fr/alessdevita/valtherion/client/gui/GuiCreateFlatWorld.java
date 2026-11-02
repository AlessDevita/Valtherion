package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.world.WorldType;

import java.util.*;

/**
 * Interface pour créer un monde plat dans Valtherion.
 * Étend GuiCreateWorld pour ajouter des options spécifiques aux mondes plats.
 */
public class GuiCreateFlatWorld extends GuiCreateWorld {

    // Liste des couches du monde plat
    private final List<FlatLayer> flatLayers;

    // Préréglages pour les mondes plats
    private final List<FlatPreset> flatPresets;

    // Index du préréglage sélectionné
    private int selectedPresetIndex;

    // Boutons
    private GuiButton applyPresetButton;
    private GuiButton addLayerButton;
    private GuiButton removeLayerButton;

    /**
     * Constructeur de l'interface de création de monde plat.
     * @param parentScreen Écran parent.
     */
    public GuiCreateFlatWorld(GuiScreen parentScreen) {
        super(parentScreen);
        this.flatLayers = new ArrayList<>();
        this.flatPresets = new ArrayList<>();
        this.selectedPresetIndex = 0;

        // Initialiser les préréglages par défaut
        initDefaultPresets();
    }

    /**
     * Initialise les préréglages par défaut.
     */
    private void initDefaultPresets() {
        FlatPreset standardPreset = new FlatPreset("Monde Plat Standard");
        standardPreset.addLayer(new FlatLayer("GRASS", 1));
        standardPreset.addLayer(new FlatLayer("DIRT", 3));
        standardPreset.addLayer(new FlatLayer("STONE", 5));
        flatPresets.add(standardPreset);

        FlatPreset desertPreset = new FlatPreset("Monde Plat Désert");
        desertPreset.addLayer(new FlatLayer("SAND", 4));
        desertPreset.addLayer(new FlatLayer("SANDSTONE", 2));
        flatPresets.add(desertPreset);

        FlatPreset oceanPreset = new FlatPreset("Monde Plat Océan");
        oceanPreset.addLayer(new FlatLayer("WATER", 1));
        oceanPreset.addLayer(new FlatLayer("SAND", 2));
        oceanPreset.addLayer(new FlatLayer("GRAVEL", 1));
        flatPresets.add(oceanPreset);

        applyPreset(0);
    }

    @Override
    public void initGui() {
        super.initGui();

        int centerX = this.width / 2;
        int buttonWidth = 150;

        // Bouton Appliquer le préréglage (id=20)
        this.buttonList.add(this.applyPresetButton = new GuiButton(20, centerX - 100, this.height - 150, buttonWidth, 20, "Appliquer"));

        // Bouton Ajouter une couche (id=21)
        this.buttonList.add(this.addLayerButton = new GuiButton(21, centerX - 100, this.height - 175, buttonWidth, 20, "Ajouter couche"));

        // Bouton Supprimer une couche (id=22)
        this.buttonList.add(this.removeLayerButton = new GuiButton(22, centerX + 5, this.height - 175, buttonWidth, 20, "Supprimer couche"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 20:
                applyPreset(selectedPresetIndex);
                break;
            case 21:
                addLayer();
                break;
            case 22:
                removeLayer();
                break;
            default:
                super.actionPerformed(button);
                break;
        }
    }

    /**
     * Applique un préréglage.
     */
    private void applyPreset(int presetIndex) {
        if (presetIndex < 0 || presetIndex >= flatPresets.size()) {
            return;
        }
        selectedPresetIndex = presetIndex;
        flatLayers.clear();
        flatLayers.addAll(flatPresets.get(presetIndex).getLayers());
    }

    private void addLayer() {
        flatLayers.add(new FlatLayer("GRASS", 1));
    }

    private void removeLayer() {
        if (!flatLayers.isEmpty()) {
            flatLayers.remove(flatLayers.size() - 1);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Afficher les couches
        if (this.valtherion.fontRenderer != null) {
            this.valtherion.fontRenderer.drawString(
                "Couches : " + flatLayers.size(),
                this.width / 2 - 100,
                150,
                0xAAAAAA
            );
        }
    }

    // --- Getters et Setters ---

    public List<FlatLayer> getFlatLayers() {
        return flatLayers;
    }

    public List<FlatPreset> getFlatPresets() {
        return flatPresets;
    }

    public int getSelectedPresetIndex() {
        return selectedPresetIndex;
    }

    public void setSelectedPresetIndex(int selectedPresetIndex) {
        this.selectedPresetIndex = selectedPresetIndex;
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

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<FlatLayer> getLayers() { return layers; }
    }
}
