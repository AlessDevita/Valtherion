package fr.alessdevita.valtherion.client.renderer.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/** Outils de conversion d'images Java en textures RGBA OpenGL. */
public final class TextureUtil {
    private TextureUtil() { }

    public static void uploadTextureImage(int textureId, BufferedImage image) {
        uploadTextureImageAllocate(textureId, image, false, false);
    }

    public static void uploadTextureImageAllocate(int textureId, BufferedImage image,
                                                  boolean blur, boolean clamp) {
        if (textureId == 0) throw new IllegalArgumentException("Identifiant de texture invalide.");
        if (image == null) throw new IllegalArgumentException("Image de texture absente.");
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);
                pixels.put((byte) (argb >> 16));
                pixels.put((byte) (argb >> 8));
                pixels.put((byte) argb);
                pixels.put((byte) (argb >> 24));
            }
        }
        pixels.flip();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        int filter = blur ? GL11.GL_LINEAR : GL11.GL_NEAREST;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);
        int wrap = clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
    }
}
