package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Gestionnaire de ressources avec repli : interroge les packs dans l'ordre
 * et renvoie la première ressource trouvée. Permet aussi de lister toutes
 * les variantes d'une ressource.
 */
public final class FallbackResourceManager implements IResourceManager {
    private final List<IResourcePack> resourcePacks = new ArrayList<>();
    private final Set<String> domains = new LinkedHashSet<>();

    public FallbackResourceManager() {
    }

    public void addResourcePack(IResourcePack pack) {
        if (pack == null) return;
        this.resourcePacks.add(pack);
        this.domains.addAll(pack.getResourceDomains());
    }

    public List<IResourcePack> getResourcePacks() {
        return java.util.Collections.unmodifiableList(this.resourcePacks);
    }

    @Override
    public Set<IResource> getResources(ResourceLocation location) {
        Set<IResource> result = new LinkedHashSet<>();
        if (location == null) return result;
        for (IResourcePack pack : this.resourcePacks) {
            if (pack.resourceExists(location)) {
                result.add(new SimpleResource(location, pack));
            }
        }
        return result;
    }

    @Override
    public IResource getResource(ResourceLocation location) {
        if (location == null) return null;
        for (IResourcePack pack : this.resourcePacks) {
            if (pack.resourceExists(location)) {
                return new SimpleResource(location, pack);
            }
        }
        return null;
    }

    @Override
    public Set<String> getResourceDomains() {
        return new HashSet<>(this.domains);
    }

    @Override
    public void invalidate() {
        // Pas de cache à invalider dans cette implémentation simple.
    }

    /**
     * Ouvre le flux de la première ressource trouvée.
     *
     * @return le flux, ou lance {@link ResourcePackFileNotFoundException}
     */
    public InputStream openResource(ResourceLocation location) throws IOException {
        IResource resource = getResource(location);
        if (resource == null) {
            throw new ResourcePackFileNotFoundException(location.toString());
        }
        return resource.getInputStream();
    }
}
