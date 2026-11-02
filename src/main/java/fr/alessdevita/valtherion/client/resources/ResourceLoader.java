package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Charge toutes les ressources du jeu et notifie les écouteurs de la progression.
 * Parcourt les packs de ressources, compte les fichiers, et charge chacun en notifiant
 * les listeners pour mettre à jour la barre de progression.
 */
public class ResourceLoader {

    private final FallbackResourceManager resourceManager;
    private final List<IResourceLoadListener> listeners = new CopyOnWriteArrayList<>();

    public ResourceLoader(FallbackResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Ajoute un écouteur pour suivre la progression du chargement.
     */
    public void addListener(IResourceLoadListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Retire un écouteur.
     */
    public void removeListener(IResourceLoadListener listener) {
        listeners.remove(listener);
    }

    /**
     * Charge toutes les ressources disponibles dans les packs.
     * Retourne la liste des ressources chargées avec succès.
     */
    public List<ResourceLocation> loadAllResources() {
        List<ResourceLocation> loadedResources = new ArrayList<>();

        // Étape 1 : Collecter toutes les ressources à charger
        List<ResourceLocation> allResources = collectAllResources();
        int total = allResources.size();

        if (total == 0) {
            for (IResourceLoadListener listener : listeners) {
                listener.onLoadingStart(0);
                listener.onLoadingComplete();
            }
            return loadedResources;
        }

        // Notifier le début
        for (IResourceLoadListener listener : listeners) {
            listener.onLoadingStart(total);
        }

        // Étape 2 : Charger chaque ressource
        int loadedCount = 0;
        for (ResourceLocation location : allResources) {
            try (InputStream stream = resourceManager.openResource(location)) {
                // Lire le flux pour s'assurer que la ressource est bien chargée
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    // Le simple fait de lire le flux suffit pour valider la ressource
                }
                loadedResources.add(location);
            } catch (IOException e) {
                System.err.println("[ResourceLoader] Erreur lors du chargement de " + location + " : " + e.getMessage());
            }

            loadedCount++;
            for (IResourceLoadListener listener : listeners) {
                listener.onResourceLoaded(loadedCount, total, location.toString());
            }
        }

        // Notifier la fin
        for (IResourceLoadListener listener : listeners) {
            listener.onLoadingComplete();
        }

        return loadedResources;
    }

    /**
     * Collecte toutes les ressources de tous les packs en utilisant listResources().
     */
    private List<ResourceLocation> collectAllResources() {
        Set<ResourceLocation> resourceSet = new LinkedHashSet<>();

        for (IResourcePack pack : resourceManager.getResourcePacks()) {
            Set<String> domains = pack.getResourceDomains();
            for (String domain : domains) {
                // Utiliser listResources pour obtenir toutes les ressources du domaine
                Set<String> resourcePaths = pack.listResources(domain, "");
                for (String resourcePath : resourcePaths) {
                    ResourceLocation location = new ResourceLocation(domain, resourcePath);
                    resourceSet.add(location);
                }
            }
        }

        return new ArrayList<>(resourceSet);
    }

    /**
     * Charge les ressources d'un domaine spécifique.
     */
    public void loadDomain(String domain, String... paths) {
        List<ResourceLocation> resources = new ArrayList<>();

        for (String path : paths) {
            ResourceLocation location = new ResourceLocation(domain, path);
            if (resourceManager.getResource(location) != null) {
                resources.add(location);
            }
        }

        int total = resources.size();
        if (total == 0) return;

        for (IResourceLoadListener listener : listeners) {
            listener.onLoadingStart(total);
        }

        int loadedCount = 0;
        for (ResourceLocation location : resources) {
            try (InputStream stream = resourceManager.openResource(location)) {
                byte[] buffer = new byte[4096];
                while (stream.read(buffer) != -1) {
                    // Lire pour valider
                }
            } catch (IOException e) {
                System.err.println("[ResourceLoader] Erreur : " + location + " : " + e.getMessage());
            }

            loadedCount++;
            for (IResourceLoadListener listener : listeners) {
                listener.onResourceLoaded(loadedCount, total, location.toString());
            }
        }

        for (IResourceLoadListener listener : listeners) {
            listener.onLoadingComplete();
        }
    }
}
