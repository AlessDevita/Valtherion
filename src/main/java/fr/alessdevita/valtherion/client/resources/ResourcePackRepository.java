package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;
import fr.alessdevita.valtherion.client.settings.GameSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Dépôt de packs de ressources : scanne un dossier à la recherche de packs
 * (.zip ou dossiers) et maintient la liste des entrées disponibles.
 */
public final class ResourcePackRepository {
    private final File folder;
    private final DefaultResourcePack defaultResourcePack;
    private final IMetadataSerializer metadataSerializer;
    private final GameSettings settings;
    private final List<Entry> repositoryEntries = new ArrayList<>();
    private final List<IResourcePack> activePacks = new ArrayList<>();

    public ResourcePackRepository(File folder, File serverResourcePacks,
                                  DefaultResourcePack defaultResourcePack,
                                  IMetadataSerializer metadataSerializer,
                                  GameSettings settings) {
        this.folder = folder;
        this.defaultResourcePack = defaultResourcePack;
        this.metadataSerializer = metadataSerializer;
        this.settings = settings;
        scanFolder();
    }

    /** Scanne le dossier et crée les entrées pour chaque pack trouvé. */
    private void scanFolder() {
        this.repositoryEntries.clear();
        if (this.folder != null && this.folder.isDirectory()) {
            File[] files = this.folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    Entry entry = new Entry(file);
                    this.repositoryEntries.add(entry);
                }
            }
        }
    }

    public List<Entry> getRepositoryEntries() {
        return Collections.unmodifiableList(this.repositoryEntries);
    }

    /** Retourne les entrées déjà sélectionnées (actives). */
    public List<Entry> getRepositoryEntriesAlreadySelected() {
        List<Entry> selected = new ArrayList<>();
        for (Entry entry : this.repositoryEntries) {
            if (entry.isActive()) selected.add(entry);
        }
        return selected;
    }

    /**
     * Reconstruit la liste des packs actifs depuis les entrées sélectionnées.
     */
    public void setRepositories(List<?> repositories) {
        this.activePacks.clear();
        if (this.defaultResourcePack != null) {
            this.activePacks.add(this.defaultResourcePack);
        }
        for (Entry entry : this.repositoryEntries) {
            if (entry.isActive()) {
                IResourcePack pack = entry.createResourcePack();
                if (pack != null) {
                    this.activePacks.add(pack);
                }
            }
        }
    }

    /** Liste des packs actifs (défaut + sélectionnés). */
    public List<IResourcePack> getActiveResourcePacks() {
        return Collections.unmodifiableList(this.activePacks);
    }

    public DefaultResourcePack getDefaultResourcePack() {
        return this.defaultResourcePack;
    }

    public IMetadataSerializer getMetadataSerializer() {
        return this.metadataSerializer;
    }

    public GameSettings getSettings() {
        return this.settings;
    }

    public File getFolder() {
        return this.folder;
    }

    public void close() {
        for (IResourcePack pack : this.activePacks) {
            if (pack != this.defaultResourcePack) {
                pack.close();
            }
        }
        this.activePacks.clear();
    }

    /**
     * Une entrée du dépôt : un fichier ou dossier pouvant devenir un pack.
     */
    public final class Entry {
        private final File file;
        private boolean active;
        private IResourcePack resourcePack;

        public Entry() {
            this(null);
        }

        public Entry(File file) {
            this.file = file;
        }

        public File getFile() {
            return this.file;
        }

        public String getName() {
            return this.file == null ? "?" : this.file.getName();
        }

        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        /** {@code true} si c'est un pack ZIP. */
        public boolean isZip() {
            return this.file != null && this.file.isFile()
                    && this.file.getName().toLowerCase().endsWith(".zip");
        }

        /** {@code true} si c'est un dossier de pack. */
        public boolean isFolder() {
            return this.file != null && this.file.isDirectory();
        }

        /** Crée le {@link IResourcePack} correspondant à cette entrée. */
        public IResourcePack createResourcePack() {
            if (this.resourcePack != null) return this.resourcePack;
            if (this.file == null) return null;
            if (isZip()) {
                this.resourcePack = new FileResourcePack(this.file, ResourcePackRepository.this.metadataSerializer);
            } else if (isFolder()) {
                this.resourcePack = new FolderResourcePack(this.file, ResourcePackRepository.this.metadataSerializer);
            }
            return this.resourcePack;
        }

        public ResourcePackRepository getRepository() {
            return ResourcePackRepository.this;
        }
    }
}
