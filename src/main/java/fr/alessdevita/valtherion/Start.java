package fr.alessdevita.valtherion;

import fr.alessdevita.valtherion.launch.ValtherionClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Start {
    private static final String APP_NAME = "Valtherion";
    private static final Path APP_DATA_ROOT = resolveAppDataRoot();
    private static final Path APP_DIR = APP_DATA_ROOT.resolve(APP_NAME);
    private static final Path DIR_DIR = APP_DIR.resolve("dir");
    private static final Path ASSETS_DIR = APP_DIR.resolve("assets");
    private static final Path NATIVES_DIR = APP_DIR.resolve("natives");
    private static final Path LIBS_DIR = APP_DIR.resolve("libs");

    public static void main(String[] args) {
        try {
            prepareEnvironment();
            ValtherionClient.main(args);
        } catch (Exception e) {
            System.err.println("[Valtherion] Impossible de lancer l'application : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void prepareEnvironment() throws IOException {
        Files.createDirectories(APP_DIR);
        Files.createDirectories(DIR_DIR);
        Files.createDirectories(ASSETS_DIR);
        Files.createDirectories(NATIVES_DIR);
        Files.createDirectories(LIBS_DIR);

        installFromClasspath("assets", ASSETS_DIR);
        installFromClasspath("natives", NATIVES_DIR);
        installFromClasspath("libs", LIBS_DIR);

        copyDirectoryIfExists(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "assets"), ASSETS_DIR);
        copyDirectoryIfExists(Paths.get(System.getProperty("user.dir"), "libs", "natives"), NATIVES_DIR);
        copyDirectoryIfExists(Paths.get(System.getProperty("user.dir"), "libs"), LIBS_DIR);

        downloadPackageIfConfigured("assets", ASSETS_DIR);
        downloadPackageIfConfigured("natives", NATIVES_DIR);
        downloadPackageIfConfigured("libs", LIBS_DIR);
    }

    private static Path resolveAppDataRoot() {
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isBlank()) {
            return Paths.get(appData);
        }
        String home = System.getProperty("user.home");
        if (home != null && !home.isBlank()) {
            return Paths.get(home, "AppData", "Roaming");
        }
        return Paths.get(System.getProperty("user.dir"));
    }

    private static void installFromClasspath(String resourcePath, Path targetDir) throws IOException {
        URL resource = Start.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            return;
        }

        if ("file".equalsIgnoreCase(resource.getProtocol())) {
            try {
                Path source = Paths.get(resource.toURI());
                copyDirectoryIfExists(source, targetDir);
            } catch (Exception e) {
                throw new IOException("Impossible de lire la ressource locale : " + resource, e);
            }
            return;
        }

        if ("jar".equalsIgnoreCase(resource.getProtocol())) {
            String jarPath = resource.getPath();
            String pathInsideJar = jarPath.substring(jarPath.indexOf("!") + 2);
            String decodedJarPath = URLDecoder.decode(jarPath.substring(5, jarPath.indexOf("!")), StandardCharsets.UTF_8);

            try (JarFile jarFile = new JarFile(decodedJarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (!entryName.startsWith(resourcePath + "/") || entry.isDirectory()) {
                        continue;
                    }
                    Path targetPath = targetDir.resolve(entryName.substring(resourcePath.length() + 1));
                    Files.createDirectories(targetPath.getParent());
                    if (!Files.exists(targetPath)) {
                        try (InputStream inputStream = Start.class.getClassLoader().getResourceAsStream(entryName)) {
                            if (inputStream == null) {
                                continue;
                            }
                            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }

    private static void downloadPackageIfConfigured(String packageName, Path targetDir) {
        String urlProperty = System.getProperty("valtherion." + packageName + ".url");
        if (urlProperty == null || urlProperty.isBlank()) {
            return;
        }

        try {
            URI uri = URI.create(urlProperty);
            Path tempZip = Files.createTempFile("valtherion-" + packageName, ".zip");
            try (InputStream in = uri.toURL().openStream(); OutputStream out = Files.newOutputStream(tempZip, StandardOpenOption.TRUNCATE_EXISTING)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            extractZip(tempZip, targetDir);
            Files.deleteIfExists(tempZip);
        } catch (Exception e) {
            System.err.println("[Valtherion] Téléchargement du package '" + packageName + "' ignoré : " + e.getMessage());
        }
    }

    private static void extractZip(Path zipFile, Path targetDir) throws IOException {
        if (!Files.exists(zipFile)) {
            return;
        }
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    Files.createDirectories(targetDir.resolve(entry.getName()));
                    continue;
                }
                Path targetPath = targetDir.resolve(entry.getName()).normalize();
                Files.createDirectories(targetPath.getParent());
                try (OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = zis.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
            }
        }
    }

    private static void copyDirectoryIfExists(Path source, Path target) throws IOException {
        if (source == null || !Files.exists(source) || !Files.isDirectory(source)) {
            return;
        }

        Files.createDirectories(target);
        List<Path> paths = Files.walk(source)
                .sorted(Comparator.reverseOrder())
                .toList();

        for (Path sourcePath : paths) {
            Path relative = source.relativize(sourcePath);
            Path targetPath = target.resolve(relative.toString()).normalize();

            if (Files.isDirectory(sourcePath)) {
                Files.createDirectories(targetPath);
            } else {
                Files.createDirectories(targetPath.getParent());
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }
}
