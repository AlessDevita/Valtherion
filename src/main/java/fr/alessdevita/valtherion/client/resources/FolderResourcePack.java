package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Pack de ressources lu depuis un dossier sur disque.
 */
public final class FolderResourcePack extends AbstractResourcePack {
    private final File rootFolder;

    public FolderResourcePack(File folder, IMetadataSerializer metadataSerializer) {
        super(metadataSerializer);
        this.rootFolder = folder;
    }

    public FolderResourcePack(File folder) {
        this(folder, null);
    }

    @Override
    public String getPackName() {
        return this.rootFolder == null ? "FolderPack" : this.rootFolder.getName();
    }

    @Override
    protected InputStream getResourceStream(String path) throws IOException {
        if (path == null || this.rootFolder == null) return null;
        File file = new File(this.rootFolder, path);
        if (!file.isFile()) return null;
        byte[] data = Files.readAllBytes(file.toPath());
        return new ByteArrayInputStream(data);
    }

    @Override
    protected boolean hasResource(String path) {
        if (path == null || this.rootFolder == null) return false;
        return new File(this.rootFolder, path).isFile();
    }

    @Override
    public Set<String> getResourceDomains() {
        Set<String> domains = new HashSet<>();
        if (this.rootFolder == null) return domains;
        File assetsDir = new File(this.rootFolder, "assets");
        if (assetsDir.isDirectory()) {
            File[] children = assetsDir.listFiles(File::isDirectory);
            if (children != null) {
                for (File child : children) {
                    domains.add(child.getName());
                }
            }
        }
        return Collections.unmodifiableSet(domains);
    }

    /** Le dossier racine du pack. */
    public File getRootFolder() {
        return this.rootFolder;
    }
}
