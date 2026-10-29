package fr.alessdevita.valtherion.client.renderer.texture;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Texture composée de plusieurs calques superposés depuis le classpath. */
public class LayeredTexture extends AbstractTexture {
    private final List<String> layeredTextureNames;

    public LayeredTexture(String... textureNames) {
        if (textureNames == null || textureNames.length == 0) {
            throw new IllegalArgumentException("Au moins un calque est requis.");
        }
        this.layeredTextureNames = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(textureNames)));
    }

    public List<String> getLayeredTextureNames() {
        return this.layeredTextureNames;
    }

    @Override
    public void loadTexture() throws IOException {
        BufferedImage composed = null;
        Graphics2D graphics = null;
        try {
            for (String path : this.layeredTextureNames) {
                BufferedImage layer = loadImage(path);
                if (composed == null) {
                    composed = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    graphics = composed.createGraphics();
                }
                graphics.drawImage(layer, 0, 0, null);
            }
            if (composed == null) throw new IOException("Aucun calque chargeable.");
            TextureUtil.uploadTextureImageAllocate(getGlTextureId(), composed, true, true);
            setLoaded(true);
        } finally {
            if (graphics != null) graphics.dispose();
        }
    }

    private static BufferedImage loadImage(String resourcePath) throws IOException {
        String path = resourcePath == null ? "" : resourcePath.replace('\\', '/');
        if (path.startsWith("/")) path = path.substring(1);
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Calque introuvable : " + resourcePath);
            BufferedImage image = ImageIO.read(stream);
            if (image == null) throw new IOException("Format d'image non reconnu : " + resourcePath);
            return image;
        }
    }
}
