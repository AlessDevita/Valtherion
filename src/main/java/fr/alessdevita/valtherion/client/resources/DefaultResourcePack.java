package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;
import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pack de ressources par défaut : charge les assets depuis une map en mémoire.
 */
public final class DefaultResourcePack extends AbstractResourcePack {
    private final Map<String, byte[]> assetsMap;
    private final Set<String> domains;

    public DefaultResourcePack(Map<String, byte[]> assetsMap) {
        this(null, assetsMap);
    }

    public DefaultResourcePack(IMetadataSerializer metadataSerializer, Map<String, byte[]> assetsMap) {
        super(metadataSerializer);
        this.assetsMap = assetsMap == null ? Collections.emptyMap() : new HashMap<>(assetsMap);
        this.domains = new HashSet<>();
        // Déduit les domaines depuis les chemins: "assets/<domain>/..."
        for (String key : this.assetsMap.keySet()) {
            String domain = extractDomain(key);
            if (domain != null) this.domains.add(domain);
        }
    }

    @Override
    public String getPackName() {
        return "Default";
    }

    @Override
    protected InputStream getResourceStream(String path) {
        if (path == null) return null;
        byte[] data = this.assetsMap.get(path);
        return data == null ? null : new ByteArrayInputStream(data);
    }

    @Override
    protected boolean hasResource(String path) {
        return path != null && this.assetsMap.containsKey(path);
    }

    @Override
    public Set<String> getResourceDomains() {
        return Collections.unmodifiableSet(this.domains);
    }

    /**
     * Compatibilité : retourne un flux directement depuis le {@link ResourceLocation}.
     */
    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        if (resourceLocation == null) return null;
        String path = toPath(resourceLocation);
        byte[] data = this.assetsMap.get(path);
        if (data == null) return null;
        return new ByteArrayInputStream(data);
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(Class<T> sectionClass) throws IOException {
        if (this.metadataSerializer == null) return null;
        byte[] data = this.assetsMap.get("pack.mcmeta");
        if (data == null) return null;
        String json = new String(data, java.nio.charset.StandardCharsets.UTF_8);
        return this.metadataSerializer.parseMetadata(json, sectionClass);
    }

    @Override
    protected Set<String> listResourcesInternal(String domain, String path) {
        Set<String> result = new HashSet<>();
        if (domain == null) return result;

        // Le préfixe à chercher : assets/<domain>/<path>
        String prefix = "assets/" + domain;
        if (path != null && !path.isEmpty()) {
            prefix += "/" + path;
        }
        // S'assurer que le préfixe se termine par / pour ne pas matcher partiellement
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        for (String key : this.assetsMap.keySet()) {
            if (key.startsWith(prefix)) {
                // Extraire le chemin relatif depuis assets/<domain>/
                String domainPrefix = "assets/" + domain + "/";
                String relPath = key.substring(domainPrefix.length());
                result.add(relPath);
            }
        }

        return result;
    }

    private static String extractDomain(String key) {
        if (key == null) return null;
        if (key.startsWith("assets/")) {
            int start = "assets/".length();
            int end = key.indexOf('/', start);
            return end > start ? key.substring(start, end) : null;
        }
        return "valtherion";
    }
}
