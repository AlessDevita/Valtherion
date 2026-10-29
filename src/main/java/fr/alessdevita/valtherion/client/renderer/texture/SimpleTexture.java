package fr.alessdevita.valtherion.client.renderer.texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/** Texture chargée depuis le classpath ou depuis un chemin de fichier. */
public class SimpleTexture extends AbstractTexture {
    private final String resourcePath;

    public SimpleTexture(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) throw new IllegalArgumentException("Chemin de texture vide.");
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() { return this.resourcePath; }

    @Override
    public void loadTexture() throws IOException {
        BufferedImage image;
        String path = this.resourcePath.startsWith("/") ? this.resourcePath.substring(1) : this.resourcePath;
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Ressource de texture introuvable : " + this.resourcePath);
            image = ImageIO.read(stream);
        }
        if (image == null) throw new IOException("Format d'image non reconnu : " + this.resourcePath);
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), image, true, true);
        setLoaded(true);
    }
}
