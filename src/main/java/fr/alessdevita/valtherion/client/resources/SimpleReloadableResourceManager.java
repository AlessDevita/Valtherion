package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;
import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestionnaire rechargeable : maintient une liste ordonnée de packs
 * et notifie des listeners lors d'un rechargement.
 */
public final class SimpleReloadableResourceManager implements IReloadableResourceManager {
    private final IMetadataSerializer metadataSerializer;
    private final List<IResourcePack> resourcePacks = new CopyOnWriteArrayList<>();
    private final List<IResourceManagerReloadListener> reloadListeners = new CopyOnWriteArrayList<>();
    private final Set<String> domains = new LinkedHashSet<>();

    public SimpleReloadableResourceManager(IMetadataSerializer metadataSerializer) {
        this.metadataSerializer = metadataSerializer;
    }

    public void addResourcePack(IResourcePack pack) {
        if (pack == null) return;
        this.resourcePacks.add(pack);
        this.domains.addAll(pack.getResourceDomains());
    }

    public List<IResourcePack> getResourcePacks() {
        return Collections.unmodifiableList(this.resourcePacks);
    }

    @Override
    public void registerReloadListener(IResourceManagerReloadListener listener) {
        if (listener != null) {
            this.reloadListeners.add(listener);
        }
    }

    /**
     * @deprecated utiliser {@link #registerReloadListener(IResourceManagerReloadListener)}
     */
    @Deprecated
    public void registerReloadListener(Object listener) {
        if (listener instanceof IResourceManagerReloadListener rl) {
            this.reloadListeners.add(rl);
        }
    }

    @Override
    public void reloadResources(List<IResourcePack> resources) {
        this.resourcePacks.clear();
        this.domains.clear();
        if (resources != null) {
            for (IResourcePack pack : resources) {
                if (pack != null) {
                    this.resourcePacks.add(pack);
                    this.domains.addAll(pack.getResourceDomains());
                }
            }
        }
        notifyReloadListeners();
    }

    private void notifyReloadListeners() {
        for (IResourceManagerReloadListener listener : this.reloadListeners) {
            try {
                listener.onResourceManagerReload(this);
            } catch (RuntimeException e) {
                System.err.println("Erreur lors du reload listener: " + e.getMessage());
            }
        }
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
        // Pas de cache à invalider.
    }

    /**
     * Ouvre le flux de la première ressource trouvée.
     */
    public InputStream openResource(ResourceLocation location) throws IOException {
        IResource resource = getResource(location);
        if (resource == null) {
            throw new ResourcePackFileNotFoundException(location.toString());
        }
        return resource.getInputStream();
    }
}
