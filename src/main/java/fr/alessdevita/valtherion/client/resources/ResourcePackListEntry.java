package fr.alessdevita.valtherion.client.resources;

import java.io.File;

/**
 * Entrée de liste de packs de ressources : représente un pack
 * sélectionnable dans l'écran des options.
 */
public class ResourcePackListEntry {
    private final ResourcePackRepository repository;
    private final IResourcePack resourcePack;

    private boolean selected;
    private boolean active;

    public ResourcePackListEntry(ResourcePackRepository repository, IResourcePack resourcePack) {
        this.repository = repository;
        this.resourcePack = resourcePack;
    }

    public ResourcePackRepository getRepository() {
        return this.repository;
    }

    public IResourcePack getResourcePack() {
        return this.resourcePack;
    }

    public String getPackName() {
        return this.resourcePack == null ? "?" : this.resourcePack.getPackName();
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Retourne le fichier sous-jacent si le pack provient du disque.
     */
    public File getPackFile() {
        if (this.resourcePack instanceof FileResourcePack filePack) {
            return getPackFile(filePack);
        }
        if (this.resourcePack instanceof FolderResourcePack folderPack) {
            return folderPack.getRootFolder();
        }
        return null;
    }

    private File getPackFile(FileResourcePack filePack) {
        try {
            java.lang.reflect.Field f = FileResourcePack.class.getDeclaredField("zipFile");
            f.setAccessible(true);
            return (File) f.get(filePack);
        } catch (Exception e) {
            return null;
        }
    }
}
