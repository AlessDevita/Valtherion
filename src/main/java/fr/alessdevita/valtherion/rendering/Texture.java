package fr.alessdevita.valtherion.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

/**
 * Classe pour gérer les textures dans Valtherion.
 * Charge, lie et libère les textures en utilisant LWJGL et STB Image.
 */
public class Texture {

    // ID de la texture OpenGL
    private final int textureId;

    // Largeur de la texture
    private int width;

    // Hauteur de la texture
    private int height;

    // Nombre de canaux (3 pour RGB, 4 pour RGBA)
    private int channels;

    // Chemin du fichier de la texture
    private String filePath;

    // Type de texture (DIFFUSE, SPECULAR, NORMAL, etc.)
    private TextureType type;

    public String getPath() {
        return "";
    }

    // Énumération des types de texture
    public enum TextureType {
        DIFFUSE,    // Texture de couleur diffuse
        SPECULAR,   // Texture spéculaire
        NORMAL,     // Texture de normales
        HEIGHT,     // Texture de hauteur
        AMBIENT,    // Texture ambiante
        EMISSIVE    // Texture émissive
    }

    /**
     * Constructeur de la texture.
     *
     * @param filePath Chemin du fichier de la texture.
     * @throws IOException Si une erreur se produit lors du chargement.
     */
    public Texture(String filePath) throws IOException {
        this.filePath = filePath;
        this.type = type;

        // Charger la texture
        this.textureId = loadTexture(filePath);
    }

    /**
     * Charge une texture depuis un fichier.
     * @param filePath Chemin du fichier de la texture.
     * @return ID de la texture OpenGL.
     * @throws IOException Si une erreur se produit lors du chargement.
     */
    private int loadTexture(String filePath) throws IOException {
        // Lire le fichier de la texture
        ByteBuffer imageBuffer = null;
        try {
            imageBuffer = FileUtils.readFileToByteBuffer(filePath);
        } catch (IOException e) {
            throw new IOException("Impossible de lire le fichier de la texture : " + filePath, e);
        }

        // Décoder l'image avec STB Image
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer imageData = STBImage.stbi_load_from_memory(
                imageBuffer,
                widthBuffer,
                heightBuffer,
                channelsBuffer,
                STBImage.STBI_rgb_alpha
        );

        if (imageData == null) {
            throw new IOException("Impossible de charger la texture : " + filePath +
                    " (Erreur STB : " + STBImage.stbi_failure_reason() + ")");
        }

        this.width = widthBuffer.get(0);
        this.height = heightBuffer.get(0);
        this.channels = channelsBuffer.get(0);

        // Créer la texture OpenGL
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Définir les paramètres de la texture
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // Charger les données de l'image dans la texture OpenGL
        if (channels == 3) {
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGB,
                    width,
                    height,
                    0,
                    GL11.GL_RGB,
                    GL11.GL_UNSIGNED_BYTE,
                    imageData
            );
        } else if (channels == 4) {
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGBA,
                    width,
                    height,
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    imageData
            );
        }

        // Générer les mipmaps
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Libérer les ressources
        STBImage.stbi_image_free(imageData);

        return textureId;
    }

    /**
     * Lie la texture à une unité de texture.
     * @param textureUnit Unité de texture (GL_TEXTURE0, GL_TEXTURE1, etc.).
     */
    public void bind(int textureUnit) {
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    /**
     * Lie la texture à l'unité de texture par défaut (GL_TEXTURE0).
     */
    public void bind() {
        bind(GL13.GL_TEXTURE0);
    }

    /**
     * Délie la texture.
     */
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Libère les ressources de la texture.
     */
    public void cleanup() {
        GL11.glDeleteTextures(textureId);
    }

    // --- Getters ---

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getChannels() {
        return channels;
    }

    public String getFilePath() {
        return filePath;
    }

    public TextureType getType() {
        return type;
    }

    /**
     * Retourne une représentation textuelle de la texture.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Texture{" +
                "textureId=" + textureId +
                ", filePath='" + filePath + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", channels=" + channels +
                ", type=" + type +
                '}';
    }

    // --- Classe utilitaire pour lire les fichiers ---

    /**
     * Classe utilitaire pour lire les fichiers en ByteBuffer.
     */
    private static class FileUtils {
        private static ByteBuffer readFileToByteBuffer(String filePath) throws IOException {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            ByteBuffer buffer = BufferUtils.createByteBuffer(fileBytes.length);
            buffer.put(fileBytes);
            buffer.flip();
            return buffer;
        }
    }
}