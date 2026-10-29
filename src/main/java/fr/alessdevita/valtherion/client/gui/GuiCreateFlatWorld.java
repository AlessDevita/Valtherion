package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

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

    // Bouton pour appliquer le préréglage
    private GuiButton applyPresetButton;

    // Bouton pour ajouter une couche
    private GuiButton addLayerButton;

    // Bouton pour supprimer une couche
    private GuiButton removeLayerButton;

    /**
     * Constructeur de l'interface de création de monde plat.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiCreateFlatWorld(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.flatLayers = new ArrayList<>();
        this.flatPresets = new ArrayList<>();
        this.selectedPresetIndex = 0;

        // Initialiser les préréglages par défaut
        initDefaultPresets();

        // Initialiser les boutons
        initButtons();
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
        flatPresets.add(standardPreset);

        // Préréglage "Monde Plat Désert"
        FlatPreset desertPreset = new FlatPreset("Monde Plat Désert");
        desertPreset.addLayer(new FlatLayer("SAND", 4));
        desertPreset.addLayer(new FlatLayer("SANDSTONE", 2));
        flatPresets.add(desertPreset);

        // Préréglage "Monde Plat Océan"
        FlatPreset oceanPreset = new FlatPreset("Monde Plat Océan");
        oceanPreset.addLayer(new FlatLayer("WATER", 1));
        oceanPreset.addLayer(new FlatLayer("SAND", 2));
        oceanPreset.addLayer(new FlatLayer("GRAVEL", 1));
        flatPresets.add(oceanPreset);

        // Appliquer le préréglage par défaut
        applyPreset(0);
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour appliquer le préréglage
        applyPresetButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 100.0f,
                buttonWidth,
                buttonHeight,
                "Appliquer le Préréglage"
        );
        applyPresetButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        applyPresetButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        applyPresetButton.setOnClick(button -> applyPreset(selectedPresetIndex));

        // Bouton pour ajouter une couche
        addLayerButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 100.0f,
                buttonWidth,
                buttonHeight,
                "Ajouter une Couche"
        );
        addLayerButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.8f, 1.0f));
        addLayerButton.setHoverColor(new Vector4f(0.4f, 0.8f, 1.0f, 1.0f));
        addLayerButton.setOnClick(button -> addLayer());

        // Bouton pour supprimer une couche
        removeLayerButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 150.0f,
                buttonWidth,
                buttonHeight,
                "Supprimer une Couche"
        );
        removeLayerButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        removeLayerButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        removeLayerButton.setOnClick(button -> removeLayer());

        // Ajouter les boutons à la GUI
        addElement(applyPresetButton);
        addElement(addLayerButton);
        addElement(removeLayerButton);
    }

    /**
     * Applique un préréglage.
     * @param presetIndex Index du préréglage.
     */
    private void applyPreset(int presetIndex) {
        if (presetIndex < 0 || presetIndex >= flatPresets.size()) {
            return;
        }

        selectedPresetIndex = presetIndex;
        flatLayers.clear();
        flatLayers.addAll(flatPresets.get(presetIndex).getLayers());
    }

    /**
     * Ajoute une couche.
     */
    private void addLayer() {
        flatLayers.add(new FlatLayer("GRASS", 1));
    }

    /**
     * Supprime une couche.
     */
    private void removeLayer() {
        if (!flatLayers.isEmpty()) {
            flatLayers.remove(flatLayers.size() - 1);
        }
    }

    @Override
    public void render() {
        super.render();

        // Dessiner les couches
        drawLayers();
    }

    /**
     * Dessine les couches du monde plat.
     */
    private void drawLayers() {
        float startX = width / 2.0f + 50.0f;
        float startY = height / 2.0f;
        float layerHeight = 20.0f;

        for (int i = 0; i < flatLayers.size(); i++) {
            FlatLayer layer = flatLayers.get(i);

            // Dessiner la couche
            GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, startY - i * layerHeight);
            GL11.glVertex2f(startX + 100.0f, startY - i * layerHeight);
            GL11.glVertex2f(startX + 100.0f, startY - i * layerHeight + layerHeight);
            GL11.glVertex2f(startX, startY - i * layerHeight + layerHeight);
            GL11.glEnd();
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

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        flatLayers.clear();
        flatPresets.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiCreateFlatWorld{" +
                "flatLayers=" + flatLayers.size() +
                ", selectedPresetIndex=" + selectedPresetIndex +
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
