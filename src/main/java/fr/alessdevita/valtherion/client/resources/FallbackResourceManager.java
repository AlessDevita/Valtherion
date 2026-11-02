package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestionnaire de ressources avec repli : interroge les packs dans l'ordre
 * et renvoie la première ressource trouvée. Permet aussi de lister toutes
 * les variantes d'une ressource. Supporte le rechargement avec notification
 * des listeners enregistrés.
 */
public final class FallbackResourceManager implements IReloadableResourceManager {
    private final List<IResourcePack> resourcePacks = new ArrayList<>();
    private final Set<String> domains = new LinkedHashSet<>();
    private final List<IResourceManagerReloadListener> reloadListeners = new CopyOnWriteArrayList<>();

    public FallbackResourceManager() {
    }

    /**
     * Constructeur avec une liste initiale de packs de ressources.
     *
     * @param packs les packs à ajouter au démarrage
     */
    public FallbackResourceManager(List<IResourcePack> packs) {
        if (packs != null) {
            for (IResourcePack pack : packs) {
                addResourcePack(pack);
            }
        }
    }

    /**
     * Ajoute un pack de ressources à la fin de la liste.
     * Les packs ajoutés en dernier ont la plus haute priorité.
     */
    public void addResourcePack(IResourcePack pack) {
        if (pack == null) return;
        this.resourcePacks.add(pack);
        this.domains.addAll(pack.getResourceDomains());
    }

    /**
     * Insère un pack de ressources au début de la liste (haute priorité).
     */
    public void insertResourcePack(IResourcePack pack) {
        if (pack == null) return;
        this.resourcePacks.add(0, pack);
        this.domains.addAll(pack.getResourceDomains());
    }

    /**
     * Supprime un pack de ressources et nettoie ses domaines.
     */
    public void removeResourcePack(IResourcePack pack) {
        if (pack == null) return;
        this.resourcePacks.remove(pack);
        // Recalculer les domaines restants
        this.domains.clear();
        for (IResourcePack p : this.resourcePacks) {
            this.domains.addAll(p.getResourceDomains());
        }
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

    /**
     * Vérifie si une ressource existe dans l'un des packs.
     */
    public boolean resourceExists(ResourceLocation location) {
        return getResource(location) != null;
    }

    /**
     * Retourne le nombre de packs de ressources enregistrés.
     */
    public int getResourcePackCount() {
        return this.resourcePacks.size();
    }

    /**
     * Vide tous les packs de ressources et leurs domaines.
     */
    public void clearResourcePacks() {
        this.resourcePacks.clear();
        this.domains.clear();
    }

    // ------------------------------------------------------------------------
    // IReloadableResourceManager
    // ------------------------------------------------------------------------

    /**
     * Enregistre un listener appelé lors des rechargements de ressources.
     */
    @Override
    public void registerReloadListener(IResourceManagerReloadListener listener) {
        if (listener != null) {
            this.reloadListeners.add(listener);
        }
    }

    /**
     * Recharge toutes les ressources depuis les packs donnés et notifie les listeners.
     *
     * @param resources la nouvelle liste de packs, ou {@code null} pour ne pas modifier les packs existants
     */
    @Override
    public void reloadResources(List<IResourcePack> resources) {
        if (resources != null) {
            this.resourcePacks.clear();
            this.domains.clear();
            for (IResourcePack pack : resources) {
                if (pack != null) {
                    this.resourcePacks.add(pack);
                    this.domains.addAll(pack.getResourceDomains());
                }
            }
        }
        notifyReloadListeners();
    }

    /**
     * Recharge les ressources en conservant les packs actuels et notifie les listeners.
     */
    public void reloadResources() {
        notifyReloadListeners();
    }

    /**
     * Notifie tous les listeners enregistrés que les ressources ont été rechargées.
     */
    public void notifyReloadListeners() {
        for (IResourceManagerReloadListener listener : this.reloadListeners) {
            try {
                listener.onResourceManagerReload(this);
            } catch (RuntimeException e) {
                System.err.println("Erreur lors du reload listener: " + e.getMessage());
            }
        }
    }

    /**
     * Retourne la liste des listeners de rechargement (en lecture seule).
     */
    public List<IResourceManagerReloadListener> getReloadListeners() {
        return java.util.Collections.unmodifiableList(this.reloadListeners);
    }

    /**
     * Supprime un listener de rechargement.
     */
    public void unregisterReloadListener(IResourceManagerReloadListener listener) {
        this.reloadListeners.remove(listener);
    }
}
