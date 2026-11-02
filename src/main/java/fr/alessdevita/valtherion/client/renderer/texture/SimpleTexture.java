package fr.alessdevita.valtherion.client.renderer.texture;

import fr.alessdevita.valtherion.client.resources.IResourceManager;
import fr.alessdevita.valtherion.client.resources.IResource;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/** Texture chargée depuis le gestionnaire de ressources ou depuis le classpath. */
public class SimpleTexture extends AbstractTexture {
    private final String resourcePath;
    private final IResourceManager resourceManager;

    /** Constructeur sans gestionnaire de ressources : utilise le classpath. */
    public SimpleTexture(String resourcePath) {
        this(resourcePath, null);
    }

    /** Constructeur avec un gestionnaire de ressources pour charger depuis les packs. */
    public SimpleTexture(String resourcePath, IResourceManager resourceManager) {
        if (resourcePath == null || resourcePath.isBlank()) throw new IllegalArgumentException("Chemin de texture vide.");
        this.resourcePath = resourcePath;
        this.resourceManager = resourceManager;
    }

    public String getResourcePath() { return this.resourcePath; }

    @Override
    public void loadTexture() throws IOException {
        BufferedImage image;
        if (this.resourceManager != null) {
            // Essayer de charger depuis le gestionnaire de ressources
            image = loadFromResourceManager();
            // Si le resource manager ne trouve pas, essayer le classpath
            if (image == null) {
                image = loadFromClasspath();
            }
        } else {
            // Fallback : charger depuis le classpath
            image = loadFromClasspath();
        }
        if (image == null) throw new IOException("Format d'image non reconnu : " + this.resourcePath);
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), image, true, true);
        setLoaded(true);
    }

    /**
     * Tente de charger la texture depuis le gestionnaire de ressources.
     * Le chemin est interprété comme "textures/..." -> ResourceLocation("valtherion", "textures/...").
     */
    private BufferedImage loadFromResourceManager() throws IOException {
        String path = this.resourcePath.startsWith("/") ? this.resourcePath.substring(1) : this.resourcePath;
        // Si le chemin ne contient pas de domaine, on utilise "valtherion" par défaut
        String domain = "valtherion";
        String resourcePath = path;
        int separatorIndex = path.indexOf('/');
        if (separatorIndex > 0 && !path.contains("textures/")) {
            // Le chemin pourrait être "domain/path"
            // On vérifie si c'est un ResourceLocation explicite
        }
        // Construire le ResourceLocation : assets/<domain>/<resourcePath>
        ResourceLocation location = new ResourceLocation(domain, resourcePath);
        IResource resource = this.resourceManager.getResource(location);
        if (resource != null) {
            try (InputStream stream = resource.getInputStream()) {
                return ImageIO.read(stream);
            }
        }
        return null;
    }

    /**
     * Charge la texture depuis le classpath (comportement par défaut).
     */
    private BufferedImage loadFromClasspath() throws IOException {
        String path = this.resourcePath.startsWith("/") ? this.resourcePath.substring(1) : this.resourcePath;
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Ressource de texture introuvable : " + this.resourcePath);
            return ImageIO.read(stream);
        }
    }
}
