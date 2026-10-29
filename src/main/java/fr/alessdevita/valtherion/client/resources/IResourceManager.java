package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.util.Set;

/**
 * Gestionnaire de ressources : fournit l'accès aux {@link IResource} et {@link IResourcePack}.
 */
public interface IResourceManager {

    /**
     * Retourne l'ensemble des ressources disponibles pour le chemin donné.
     *
     * @param location l'identifiant de la ressource
     * @return un ensemble non vide d'{@link IResource}, ou vide si introuvable
     */
    Set<IResource> getResources(ResourceLocation location);

    /**
     * Retourne la première ressource trouvée pour le chemin donné, ou {@code null}.
     *
     * @param location l'identifiant de la ressource
     * @return la ressource, ou {@code null}
     */
    IResource getResource(ResourceLocation location);

    /** Les noms de tous les packs de ressources enregistrés. */
    Set<String> getResourceDomains();

    /** Invalide les caches internes. */
    void invalidate();
}
