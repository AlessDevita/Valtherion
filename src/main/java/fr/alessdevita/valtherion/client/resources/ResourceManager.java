package fr.alessdevita.valtherion.client.resources;

import java.io.File;

/**
 * Gestionnaire de ressources simple : wrappe un {@link FallbackResourceManager}
 * en exposant un constructeur depuis un dossier d'assets.
 */
public final class ResourceManager implements IResourceManager {
    private final FallbackResourceManager fallback;

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

    /** Constructeur interne pour brancher un FallbackResourceManager existant. */
    public ResourceManager(FallbackResourceManager fallback) {
        this.fallback = fallback != null ? fallback : new FallbackResourceManager();
    }

    @Override
    public java.util.Set<IResource> getResources(fr.alessdevita.valtherion.utils.ResourceLocation location) {
        return this.fallback.getResources(location);
    }

    @Override
    public IResource getResource(fr.alessdevita.valtherion.utils.ResourceLocation location) {
        return this.fallback.getResource(location);
    }

    @Override
    public java.util.Set<String> getResourceDomains() {
        return this.fallback.getResourceDomains();
    }

    @Override
    public void invalidate() {
        this.fallback.invalidate();
    }
}
