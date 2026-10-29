package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.InputStream;

/**
 * Représente une ressource (fichier) accessible via un {@link IResourceManager}.
 */
public interface IResource {

    /** Le {@link ResourceLocation} identifiant cette ressource. */
    ResourceLocation getResourceLocation();

    /** Le flux de données de la ressource. L'appelant est responsable de sa fermeture. */
    InputStream getInputStream();

    /** Le nom du pack qui a fourni cette ressource. */
    String getPackName();

    /**
     * Lit une section de métadonnées associée à cette ressource.
     *
     * @param sectionClass la classe de la section attendue
     * @return la section, ou {@code null} si absente
     */
    <T extends IMetadataSection> T getMetadata(Class<T> sectionClass);
}
