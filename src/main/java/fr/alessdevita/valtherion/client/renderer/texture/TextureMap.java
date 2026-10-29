package fr.alessdevita.valtherion.client.renderer.texture;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Atlas de textures blocs et objets construit en grille régulière. */
public class TextureMap extends AbstractTexture implements IIconRegister, ITickableTextureObject {
    private final String basePath;
    private final Map<String, TextureAtlasSprite> sprites = new LinkedHashMap<>();

    public TextureMap() { this("textures/blocks/"); }
    public TextureMap(String basePath) { this.basePath = basePath == null ? "" : basePath; }

    @Override
    public synchronized TextureAtlasSprite registerIcon(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nom de texture vide.");
        return this.sprites.computeIfAbsent(name, TextureAtlasSprite::new);
    }

    public synchronized TextureAtlasSprite getAtlasSprite(String name) {
        return this.sprites.getOrDefault(name, this.sprites.get("missingno"));
    }

    public synchronized Collection<TextureAtlasSprite> getRegisteredSprites() {
        return Collections.unmodifiableCollection(this.sprites.values());
    }

    @Override
    public synchronized void loadTexture() throws IOException {
        if (!this.sprites.containsKey("missingno")) registerIcon("missingno");
        int columns = Math.max(1, (int) Math.ceil(Math.sqrt(this.sprites.size())));
        int rows = (this.sprites.size() + columns - 1) / columns;
        int atlasWidth = columns * 16, atlasHeight = rows * 16;
        BufferedImage atlas = new BufferedImage(atlasWidth, atlasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = atlas.createGraphics();
        try {
            int index = 0;
            for (TextureAtlasSprite sprite : this.sprites.values()) {
                BufferedImage image = loadSprite(sprite.getIconName());
                int x = (index % columns) * 16, y = (index / columns) * 16;
                graphics.drawImage(image, x, y, 16, 16, null);
                sprite.setIconWidth(16); sprite.setIconHeight(16);
                sprite.initSprite(atlasWidth, atlasHeight, x, y, false);
                index++;
            }
        } finally {
            graphics.dispose();
        }
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), atlas, false, false);
        setLoaded(true);
        for (TextureAtlasSprite sprite : this.sprites.values()) sprite.setGlTextureId(getGlTextureId());
    }

    private BufferedImage loadSprite(String name) {
        String path = (this.basePath + name + ".png").replace('\\', '/');
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            BufferedImage image = input == null ? null : ImageIO.read(input);
            if (image != null) return image;
        } catch (IOException ignored) {
            // Missing textures are represented with the checkerboard below.
        }
        BufferedImage missing = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++) {
            missing.setRGB(x, y, ((x / 4 + y / 4) & 1) == 0 ? 0xFFFF00FF : 0xFF202020);
        }
        return missing;
    }

    @Override
    public void tick() { for (TextureAtlasSprite sprite : getRegisteredSprites()) sprite.tick(); }
}
