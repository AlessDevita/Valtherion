package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;
import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Base abstraite pour les packs de ressources.
 * Implémente la lecture des métadonnées depuis {@code pack.mcmeta}.
 */
public abstract class AbstractResourcePack implements IResourcePack {
    private static final String PACK_META = "pack.mcmeta";

    protected final IMetadataSerializer metadataSerializer;

    protected AbstractResourcePack(IMetadataSerializer metadataSerializer) {
        this.metadataSerializer = metadataSerializer;
    }

    /** Le nom du pack, utilisé pour l'affichage et le logging. */
    @Override
    public abstract String getPackName();

    /**
     * Retourne le flux d'une ressource identifiée par son chemin relatif.
     *
     * @param path chemin relatif (ex: "assets/valtherion/textures/block.png")
     * @return le flux, ou {@code null} si absent
     */
    protected abstract InputStream getResourceStream(String path) throws IOException;

    /**
     * {@code true} si le chemin existe dans ce pack.
     */
    protected abstract boolean hasResource(String path);

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        if (location == null) return null;
        return getResourceStream(toPath(location));
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        if (location == null) return false;
        return hasResource(toPath(location));
    }

    @Override
    public Set<String> getResourceDomains() {
        return new HashSet<>();
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(Class<T> sectionClass) throws IOException {
        if (this.metadataSerializer == null) return null;
        try (InputStream stream = getResourceStream(PACK_META)) {
            if (stream == null) return null;
            String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return this.metadataSerializer.parseMetadata(json, sectionClass);
        }
    }

    @Override
    public void close() {
        // Redéfinition par les sous-classes si nécessaire.
    }

    /** Construit le chemin relatif à partir d'un {@link ResourceLocation}. */
    protected static String toPath(ResourceLocation location) {
        String namespace = location.getNamespace();
        String path = location.getPath();
        return "assets/" + namespace + "/" + path;
    }

    /** Flux depuis un tableau d'octets, ou {@code null}. */
    protected static InputStream toStream(byte[] data) {
        return data == null ? null : new ByteArrayInputStream(data);
    }
}
