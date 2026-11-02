package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Gestionnaire de ressources simple : wrappe un {@link FallbackResourceManager}
 * en exposant un constructeur depuis un dossier d'assets.
 * Implémente {@link IReloadableResourceManager} pour supporter le rechargement
 * avec notification des listeners.
 */
public final class ResourceManager implements IReloadableResourceManager {
    private final FallbackResourceManager fallback;

    /**
     * Constructeur à partir d'un dossier d'assets.
     * Crée un {@link FallbackResourceManager} et ajoute un {@link FolderResourcePack}
     * pour chaque sous-dossier du répertoire donné.
     *
     * @param assets le dossier racine contenant les assets
     */
    public ResourceManager(File assets) {
        this.fallback = new FallbackResourceManager();
        if (assets != null && assets.isDirectory()) {
            // Parcourt les sous-dossiers pour créer des FolderResourcePack.
            File[] children = assets.listFiles(File::isDirectory);
            if (children != null) {
                for (File child : children) {
                    this.fallback.addResourcePack(new FolderResourcePack(child));
                }
            }
        }
    }

    /**
     * Constructeur interne pour brancher un {@link FallbackResourceManager} existant.
     *
     * @param fallback le gestionnaire de ressources avec repli
     */
    public ResourceManager(FallbackResourceManager fallback) {
        this.fallback = fallback != null ? fallback : new FallbackResourceManager();
    }

    /**
     * Retourne le {@link FallbackResourceManager} sous-jacent.
     * Utile pour accéder aux méthodes spécifiques comme {@code addResourcePack()}.
     *
     * @return le FallbackResourceManager
     */
    public FallbackResourceManager getFallbackResourceManager() {
        return this.fallback;
    }

    // ------------------------------------------------------------------------
    // IResourceManager
    // ------------------------------------------------------------------------

    @Override
    public Set<IResource> getResources(ResourceLocation location) {
        return this.fallback.getResources(location);
    }

    @Override
    public IResource getResource(ResourceLocation location) {
        return this.fallback.getResource(location);
    }

    @Override
    public Set<String> getResourceDomains() {
        return this.fallback.getResourceDomains();
    }

    @Override
    public void invalidate() {
        this.fallback.invalidate();
    }

    // ------------------------------------------------------------------------
    // IReloadableResourceManager
    // ------------------------------------------------------------------------

    /**
     * Enregistre un listener appelé lors des rechargements de ressources.
     *
     * @param listener le listener à enregistrer
     */
    @Override
    public void registerReloadListener(IResourceManagerReloadListener listener) {
        this.fallback.registerReloadListener(listener);
    }

    /**
     * Recharge toutes les ressources depuis les packs donnés et notifie les listeners.
     *
     * @param resources la nouvelle liste de packs, ou {@code null} pour ne pas modifier les packs existants
     */
    @Override
    public void reloadResources(List<IResourcePack> resources) {
        this.fallback.reloadResources(resources);
    }

    /**
     * Recharge les ressources en conservant les packs actuels et notifie les listeners.
     */
    public void reloadResources() {
        this.fallback.reloadResources();
    }

    /**
     * Notifie tous les listeners enregistrés que les ressources ont été rechargées.
     */
    public void notifyReloadListeners() {
        this.fallback.notifyReloadListeners();
    }

    /**
     * Retourne la liste des listeners de rechargement (en lecture seule).
     *
     * @return la liste des listeners
     */
    public List<IResourceManagerReloadListener> getReloadListeners() {
        return this.fallback.getReloadListeners();
    }

    /**
     * Supprime un listener de rechargement.
     *
     * @param listener le listener à supprimer
     */
    public void unregisterReloadListener(IResourceManagerReloadListener listener) {
        this.fallback.unregisterReloadListener(listener);
    }

    // ------------------------------------------------------------------------
    // Convenience methods delegated to FallbackResourceManager
    // ------------------------------------------------------------------------

    /**
     * Ajoute un pack de ressources à la fin de la liste.
     *
     * @param pack le pack à ajouter
     */
    public void addResourcePack(IResourcePack pack) {
        this.fallback.addResourcePack(pack);
    }

    /**
     * Insère un pack de ressources au début de la liste (haute priorité).
     *
     * @param pack le pack à insérer
     */
    public void insertResourcePack(IResourcePack pack) {
        this.fallback.insertResourcePack(pack);
    }

    /**
     * Supprime un pack de ressources.
     *
     * @param pack le pack à supprimer
     */
    public void removeResourcePack(IResourcePack pack) {
        this.fallback.removeResourcePack(pack);
    }

    /**
     * Retourne le nombre de packs de ressources enregistrés.
     *
     * @return le nombre de packs
     */
    public int getResourcePackCount() {
        return this.fallback.getResourcePackCount();
    }

    /**
     * Vérifie si une ressource existe dans l'un des packs.
     *
     * @param location l'identifiant de la ressource
     * @return {@code true} si la ressource existe
     */
    public boolean resourceExists(ResourceLocation location) {
        return this.fallback.resourceExists(location);
    }

    /**
     * Vide tous les packs de ressources et leurs domaines.
     */
    public void clearResourcePacks() {
        this.fallback.clearResourcePacks();
    }
}
