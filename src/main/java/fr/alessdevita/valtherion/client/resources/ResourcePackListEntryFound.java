package fr.alessdevita.valtherion.client.resources;

import java.io.File;

/**
 * Entrée de pack découverte sur le disque dans la liste des packs.
 */
public final class ResourcePackListEntryFound extends ResourcePackListEntry {
    private final File packFile;

    public ResourcePackListEntryFound(ResourcePackRepository repository,
                                      IResourcePack resourcePack, File packFile) {
        super(repository, resourcePack);
        this.packFile = packFile;
    }

    public File getPackFile() {
        return this.packFile;
    }
}
