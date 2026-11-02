package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ecouteur notifie lors du chargement des ressources.
 * Permet de suivre la progression du chargement dans l'ecran de chargement.
 */
public interface IResourceLoadListener {
    /**
     * Appele avant le debut du chargement des ressources.
     * @param totalResources Nombre total de ressources a charger.
     */
    void onLoadingStart(int totalResources);

    /**
     * Appele apres le chargement d'une ressource.
     * @param loadedResources Nombre de ressources deja chargees.
     * @param totalResources Nombre total de ressources.
     * @param resourceName Nom de la ressource chargee.
     */
    void onResourceLoaded(int loadedResources, int totalResources, String resourceName);

    /**
     * Appele a la fin du chargement de toutes les ressources.
     */
    void onLoadingComplete();
}
