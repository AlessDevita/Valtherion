package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pack de ressources lu depuis un fichier ZIP.
 */
public final class FileResourcePack extends AbstractResourcePack {
    private final File zipFile;
    private FileSystem fileSystem;

    public FileResourcePack(File zipFile, IMetadataSerializer metadataSerializer) {
        super(metadataSerializer);
        this.zipFile = zipFile;
    }

    public FileResourcePack(File zipFile) {
        this(zipFile, null);
    }

    private FileSystem getFileSystem() throws IOException {
        if (this.fileSystem == null) {
            this.fileSystem = FileSystems.newFileSystem(this.zipFile.toPath(), (ClassLoader) null);
        }
        return this.fileSystem;
    }

    @Override
    public String getPackName() {
        return this.zipFile == null ? "FilePack" : this.zipFile.getName();
    }

    @Override
    protected InputStream getResourceStream(String path) throws IOException {
        if (path == null) return null;
        FileSystem fs = getFileSystem();
        Path entry = fs.getPath(path);
        if (!Files.exists(entry)) return null;
        byte[] data = Files.readAllBytes(entry);
        return new ByteArrayInputStream(data);
    }

    @Override
    protected boolean hasResource(String path) {
        if (path == null) return false;
        try {
            FileSystem fs = getFileSystem();
            Path entry = fs.getPath(path);
            return Files.exists(entry);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Set<String> getResourceDomains() {
        Set<String> domains = new HashSet<>();
        try {
            FileSystem fs = getFileSystem();
            Path assetsDir = fs.getPath("assets");
            if (Files.isDirectory(assetsDir)) {
                try (var stream = Files.list(assetsDir)) {
                    stream.filter(Files::isDirectory)
                          .map(Path::getFileName)
                          .map(Path::toString)
                          .forEach(domains::add);
                }
            }
        } catch (IOException ignored) {
            // FS illisible, domains vide.
        }
        return Collections.unmodifiableSet(domains);
    }

    @Override
    public void close() {
        if (this.fileSystem != null) {
            try {
                this.fileSystem.close();
            } catch (IOException ignored) {
            }
            this.fileSystem = null;
        }
    }
}
