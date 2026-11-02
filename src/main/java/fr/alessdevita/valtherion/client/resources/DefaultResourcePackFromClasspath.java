package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Pack de ressources qui charge les assets depuis le classpath.
 * Lit les fichiers présents sous "assets/" dans les ressources du classpath
 * et les expose via le gestionnaire de ressources.
 */
public final class DefaultResourcePackFromClasspath extends AbstractResourcePack {

    private final Map<String, byte[]> assetsMap;
    private final Set<String> domains;

    public DefaultResourcePackFromClasspath() {
        super(null);
        this.assetsMap = new HashMap<>();
        this.domains = new HashSet<>();
        loadFromClasspath();
    }

    @Override
    public String getPackName() {
        return "Classpath";
    }

    @Override
    protected InputStream getResourceStream(String path) {
        if (path == null) return null;
        byte[] data = this.assetsMap.get(path);
        return data == null ? null : new java.io.ByteArrayInputStream(data);
    }

    @Override
    protected boolean hasResource(String path) {
        return path != null && this.assetsMap.containsKey(path);
    }

    @Override
    public Set<String> getResourceDomains() {
        return java.util.Collections.unmodifiableSet(this.domains);
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws IOException {
        if (resourceLocation == null) return null;
        String path = toPath(resourceLocation);
        byte[] data = this.assetsMap.get(path);
        if (data == null) return null;
        return new java.io.ByteArrayInputStream(data);
    }

    @Override
    protected Set<String> listResourcesInternal(String domain, String path) {
        Set<String> result = new HashSet<>();
        if (domain == null) return result;

        // Le préfixe à chercher : assets/<domain>/<path>
        String prefix = "assets/" + domain;
        if (path != null && !path.isEmpty()) {
            prefix += "/" + path;
        }
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        for (String key : this.assetsMap.keySet()) {
            if (key.startsWith(prefix)) {
                String domainPrefix = "assets/" + domain + "/";
                String relPath = key.substring(domainPrefix.length());
                result.add(relPath);
            }
        }

        return result;
    }

    /**
     * Charge toutes les ressources disponibles sous "assets/" dans le classpath.
     * Gère à la fois les fichiers système et les JAR.
     */
    private void loadFromClasspath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }

        // Essayer de trouver les ressources "assets/" dans le classpath
        try {
            URL assetsUrl = classLoader.getResource("assets");
            if (assetsUrl == null) {
                System.out.println("[DefaultResourcePackFromClasspath] Aucun dossier 'assets' trouvé dans le classpath.");
                return;
            }

            String protocol = assetsUrl.getProtocol();
            String path = assetsUrl.getPath();

            if ("file".equals(protocol)) {
                // Ressources dans un dossier du système de fichiers
                loadFromDirectory(Paths.get(path), "");
            } else if ("jar".equals(protocol)) {
                // Ressources dans un JAR
                loadFromJar(path);
            } else {
                System.out.println("[DefaultResourcePackFromClasspath] Protocole non supporté: " + protocol);
            }
        } catch (Exception e) {
            System.err.println("[DefaultResourcePackFromClasspath] Erreur lors du chargement du classpath: " + e.getMessage());
        }
    }

    /**
     * Parcourt récursivement un dossier et charge les fichiers dans assetsMap.
     */
    private void loadFromDirectory(java.nio.file.Path dir, String prefix) throws IOException {
        try (java.util.stream.Stream<java.nio.file.Path> stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
                .forEach(file -> {
                    String key = prefix + dir.relativize(file).toString().replace('\\', '/');
                    try {
                        byte[] data = Files.readAllBytes(file);
                        this.assetsMap.put(key, data);
                        // Extraire le domaine
                        String domain = extractDomain(key);
                        if (domain != null) {
                            this.domains.add(domain);
                        }
                    } catch (IOException e) {
                        System.err.println("[DefaultResourcePackFromClasspath] Erreur de lecture: " + file);
                    }
                });
        }
    }

    /**
     * Charge les ressources depuis un JAR.
     */
    private void loadFromJar(String jarPath) throws IOException {
        // Extraire le chemin du JAR (format: file:/path/to.jar!/assets)
        int bangIndex = jarPath.indexOf('!');
        if (bangIndex < 0) return;
        String jarFile = jarPath.substring(0, bangIndex);
        if (jarFile.startsWith("file:")) {
            jarFile = jarFile.substring(5);
        }

        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream()
                .filter(entry -> entry.getName().startsWith("assets/"))
                .filter(entry -> !entry.isDirectory())
                .forEach(entry -> {
                    try (InputStream is = jar.getInputStream(entry)) {
                        byte[] data = is.readAllBytes();
                        this.assetsMap.put(entry.getName(), data);
                        String domain = extractDomain(entry.getName());
                        if (domain != null) {
                            this.domains.add(domain);
                        }
                    } catch (IOException e) {
                        System.err.println("[DefaultResourcePackFromClasspath] Erreur de lecture JAR: " + entry.getName());
                    }
                });
        }
    }

    /**
     * Extrait le domaine (namespace) depuis un chemin de ressource.
     * Ex: "assets/valtherion/textures/block.png" -> "valtherion"
     */
    private static String extractDomain(String key) {
        if (key == null) return null;
        if (key.startsWith("assets/")) {
            int start = "assets/".length();
            int end = key.indexOf('/', start);
            return end > start ? key.substring(start, end) : null;
        }
        return null;
    }
}
