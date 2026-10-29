package fr.alessdevita.valtherion.client.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Index des assets : mappe les noms logiques vers les chemins physiques
 * sur disque (ou vers des données en mémoire).
 */
public final class ResourceIndex {
    private final Map<String, byte[]> assetsMap = new HashMap<>();

    /**
     * Construit un index depuis un dossier d'assets et un nom d'index.
     *
     * @param assetsDirIn le dossier racine des assets
     * @param assetIndexIn le nom de l'index (ex: "1.0")
     */
    public ResourceIndex(File assetsDirIn, String assetIndexIn) {
        if (assetsDirIn == null || assetIndexIn == null) return;
        File indexFile = new File(new File(assetsDirIn, "indexes"), assetIndexIn + ".json");
        if (!indexFile.isFile()) return;
        try (InputStream stream = new FileInputStream(indexFile)) {
            String content = new String(stream.readAllBytes());
            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            if (root.has("objects") && root.get("objects").isJsonObject()) {
                JsonObject objects = root.getAsJsonObject("objects");
                for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
                    String name = entry.getKey();
                    JsonObject obj = entry.getValue().getAsJsonObject();
                    if (obj.has("hash")) {
                        String hash = obj.get("hash").getAsString();
                        String prefix = hash.substring(0, 2);
                        File objectFile = new File(new File(assetsDirIn, "objects"), prefix + "/" + hash);
                        if (objectFile.isFile()) {
                            byte[] data = java.nio.file.Files.readAllBytes(objectFile.toPath());
                            this.assetsMap.put(name, data);
                        }
                    }
                }
            }
        } catch (IOException | RuntimeException ignored) {
            // Index illisible : on garde la map vide.
        }
    }

    /** Index construit directement depuis une map d'assets. */
    public ResourceIndex(Map<String, byte[]> assets) {
        if (assets != null) {
            this.assetsMap.putAll(assets);
        }
    }

    public Map<String, byte[]> getAssetsMap() {
        return Collections.unmodifiableMap(this.assetsMap);
    }

    public byte[] getAssetData(String name) {
        return this.assetsMap.get(name);
    }

    public boolean hasAsset(String name) {
        return this.assetsMap.containsKey(name);
    }
}
