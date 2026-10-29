package fr.alessdevita.valtherion.client.renderer.texture;

import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Charge, met en cache et lie les textures du client. */
public class TextureManager implements Closeable {
    private final Map<String, ITextureObject> textures = new ConcurrentHashMap<>();
    private final AtomicLong dynamicTextureId = new AtomicLong();

    public void bindTexture(String path) {
        if (path == null || path.isBlank()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            return;
        }
        ITextureObject texture = this.textures.computeIfAbsent(normalize(path), SimpleTexture::new);
        try {
            if (!texture.isLoaded()) texture.loadTexture();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
        } catch (IOException | RuntimeException exception) {
            this.textures.remove(normalize(path), texture);
            System.err.println("Impossible de charger la texture " + path + " : " + exception.getMessage());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }

    public void loadTexture(String path, ITextureObject texture) throws IOException {
        if (path == null || path.isBlank() || texture == null) throw new IllegalArgumentException("Texture ou chemin absent.");
        String key = normalize(path);
        ITextureObject previous = this.textures.put(key, texture);
        if (previous != null && previous != texture) previous.deleteGlTexture();
        texture.loadTexture();
    }

    public ITextureObject getTexture(String path) { return this.textures.get(normalize(path)); }

    public String getDynamicTextureLocation(String name, DynamicTexture texture) {
        if (name == null || name.isBlank() || texture == null) throw new IllegalArgumentException("Texture dynamique invalide.");
        String key = "dynamic/" + name + "_" + this.dynamicTextureId.incrementAndGet();
        this.textures.put(key, texture);
        texture.updateDynamicTexture();
        return key;
    }

    public String getDynamicTextureLocation(String name, BufferedImage image) {
        return getDynamicTextureLocation(name, new DynamicTexture(image));
    }

    public void tick() {
        for (ITextureObject texture : this.textures.values()) {
            if (texture instanceof ITickableTextureObject) ((ITickableTextureObject) texture).tick();
        }
    }

    public void deleteTexture(String path) {
        ITextureObject texture = this.textures.remove(normalize(path));
        if (texture != null) texture.deleteGlTexture();
    }

    public int getTextureCount() { return this.textures.size(); }

    @Override
    public void close() {
        for (ITextureObject texture : this.textures.values()) texture.deleteGlTexture();
        this.textures.clear();
    }

    private static String normalize(String path) {
        if (path == null) return "";
        String normalized = path.replace('\\', '/');
        while (normalized.startsWith("/")) normalized = normalized.substring(1);
        return normalized;
    }
}
