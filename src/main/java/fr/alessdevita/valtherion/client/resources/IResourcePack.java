package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Représente un pack de ressources (dossier, zip, ou pack par défaut).
 */
public interface IResourcePack {

    /** Le flux de la ressource demandée, ou {@code null} si absente. */
    InputStream getInputStream(ResourceLocation location) throws IOException;

    /** {@code true} si la ressource existe dans ce pack. */
    boolean resourceExists(ResourceLocation location);

    /** Les domaines (namespaces) gérés par ce pack. */
    Set<String> getResourceDomains();

    /** Le nom d'affichage du pack. */
    String getPackName();

    /**
     * Lit une section de métadonnées depuis ce pack.
     *
     * @param location la ressource source
     * @param sectionClass la classe de la section attendue
     * @return la section, ou {@code null}
     */
    <T extends IMetadataSection> T getPackMetadata(Class<T> sectionClass) throws IOException;

    void close();
}
