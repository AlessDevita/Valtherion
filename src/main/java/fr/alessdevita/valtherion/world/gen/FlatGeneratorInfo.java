package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration pour un monde plat (Superflat).
 * Définit les couches de blocs et les options de génération.
 */
public class FlatGeneratorInfo {

    private final List<FlatLayerInfo> layers;
    private boolean generateStructures;

    public FlatGeneratorInfo() {
        this.layers = new ArrayList<>();
        this.generateStructures = false;
        initDefaults();
    }

    public FlatGeneratorInfo(String dataValue) {
        this.layers = new ArrayList<>();
        this.generateStructures = false;
        parse(dataValue);
    }

    private void initDefaults() {
        // 4 blocs de bedrock
        layers.add(new FlatLayerInfo(4, BlockType.STONE));
        // 20 blocs de pierre
        layers.add(new FlatLayerInfo(20, BlockType.STONE));
        // 4 blocs de terre
        layers.add(new FlatLayerInfo(4, BlockType.DIRT));
        // 1 bloc d'herbe
        layers.add(new FlatLayerInfo(1, BlockType.GRASS));
    }

    private void parse(String dataValue) {
        if (dataValue == null || dataValue.isEmpty()) {
            initDefaults();
            return;
        }
        // Format simplifié: "stone;4*stone,4*dirt,1*grass"
        String[] parts = dataValue.split(";");
        if (parts.length > 1) {
            String[] layerDefs = parts[1].split(",");
            for (String def : layerDefs) {
                String[] tokens = def.split("\\*");
                int thickness = tokens.length > 0 ? Integer.parseInt(tokens[0].trim()) : 1;
                BlockType type = BlockType.STONE;
                if (tokens.length > 1) {
                    try {
                        type = BlockType.valueOf(tokens[1].trim().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                layers.add(new FlatLayerInfo(thickness, type));
            }
        }
    }

    public List<FlatLayerInfo> getLayers() {
        return layers;
    }

    public boolean isGenerateStructures() {
        return generateStructures;
    }

    public void setGenerateStructures(boolean generateStructures) {
        this.generateStructures = generateStructures;
    }
}
