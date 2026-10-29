package fr.alessdevita.valtherion.world.chunk.storage;

import fr.alessdevita.valtherion.world.storage.ISaveFormat;
import fr.alessdevita.valtherion.world.storage.ISaveHandler;
import fr.alessdevita.valtherion.world.storage.WorldInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** File based save format used by the integrated client. */
public class AnvilSaveConverter implements ISaveFormat {
    private static final String INFO_FILE = "world.properties";
    private final File savesDirectory;

    public AnvilSaveConverter() {
        this(new File("saves"));
    }

    public AnvilSaveConverter(File savesDirectory) {
        this.savesDirectory = savesDirectory == null ? new File("saves") : savesDirectory;
    }

    @Override
    public ISaveHandler getSaveLoader(String saveName, boolean storePlayerData) {
        return new SaveHandler(resolveWorldDirectory(saveName));
    }

    @Override
    public WorldInfo getWorldInfo(String saveName) {
        return getSaveLoader(saveName, false).loadWorldInfo();
    }

    @Override
    public void flushCache() {
        // This format does not keep open region files or an in-memory cache.
    }

    @Override
    public boolean deleteWorldDirectory(String saveName) {
        File directory = resolveWorldDirectory(saveName);
        if (!directory.exists()) {
            return true;
        }
        return deleteRecursively(directory);
    }

    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    @Override
    public String getName() {
        return "Valtherion world saves";
    }

    @Override
    public List<String> getWorldList() {
        File[] directories = this.savesDirectory.listFiles(File::isDirectory);
        if (directories == null) return Collections.emptyList();
        List<String> names = new ArrayList<>();
        for (File directory : directories) {
            if (new File(directory, INFO_FILE).isFile()) names.add(directory.getName());
        }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return Collections.unmodifiableList(names);
    }

    private File resolveWorldDirectory(String saveName) {
        if (saveName == null || saveName.trim().isEmpty()) {
            throw new IllegalArgumentException("World name must not be empty");
        }
        File directory = new File(this.savesDirectory, saveName).getAbsoluteFile();
        File root = this.savesDirectory.getAbsoluteFile();
        if (!directory.toPath().normalize().startsWith(root.toPath().normalize())) {
            throw new IllegalArgumentException("World name resolves outside the saves directory");
        }
        return directory;
    }

    private static boolean deleteRecursively(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if (!deleteRecursively(child)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    private static final class SaveHandler implements ISaveHandler {
        private final File worldDirectory;

        private SaveHandler(File worldDirectory) {
            this.worldDirectory = worldDirectory;
        }

        @Override
        public File getWorldDirectory() {
            return this.worldDirectory;
        }

        @Override
        public WorldInfo loadWorldInfo() {
            File infoFile = new File(this.worldDirectory, INFO_FILE);
            if (!infoFile.isFile()) {
                return null;
            }
            Properties properties = new Properties();
            try (FileInputStream input = new FileInputStream(infoFile)) {
                properties.load(input);
                String name = properties.getProperty("name", this.worldDirectory.getName());
                long seed = Long.parseLong(properties.getProperty("seed", "0"));
                return new WorldInfo(name, seed);
            } catch (IOException | NumberFormatException exception) {
                return null;
            }
        }

        @Override
        public void saveWorldInfo(WorldInfo worldInfo) {
            if (worldInfo == null) {
                throw new IllegalArgumentException("World info must not be null");
            }
            if (!this.worldDirectory.exists() && !this.worldDirectory.mkdirs()) {
                throw new IllegalStateException("Could not create world directory: " + this.worldDirectory);
            }
            Properties properties = new Properties();
            properties.setProperty("name", worldInfo.getWorldName());
            properties.setProperty("seed", Long.toString(worldInfo.getSeed()));
            try (FileOutputStream output = new FileOutputStream(new File(this.worldDirectory, INFO_FILE))) {
                properties.store(output, "Valtherion world metadata");
            } catch (IOException exception) {
                throw new IllegalStateException("Could not save world metadata", exception);
            }
        }
    }
}
