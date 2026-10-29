package fr.alessdevita.valtherion.client.resources;

import java.util.List;

/**
 * Gestionnaire de ressources rechargeable : peut enregistrer des listeners
 * et déclencher un rechargement.
 */
public interface IReloadableResourceManager extends IResourceManager {

    /** Enregistre un listener appelé lors des rechargements. */
    void registerReloadListener(IResourceManagerReloadListener listener);

    /**
     * Recharge toutes les ressources depuis les packs enregistrés.
     *
     * @param resources liste optionnelle de packs à recharger
     */
    void reloadResources(List<IResourcePack> resources);
}
