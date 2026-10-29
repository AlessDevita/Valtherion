package fr.alessdevita.valtherion.client.resources;

/**
 * Listener appelé lorsque les ressources sont rechargées.
 */
@FunctionalInterface
public interface IResourceManagerReloadListener {

    /** Appelé lorsque le gestionnaire de ressources a fini de recharger. */
    void onResourceManagerReload(IResourceManager resourceManager);
}
