package fr.alessdevita.valtherion.client.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Allocation et libération des ressources OpenGL créées par le rendu. */
public final class GLAllocation {
    private static final Set<Integer> TEXTURES = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> DISPLAY_LISTS = ConcurrentHashMap.newKeySet();

    private GLAllocation() { }

    public static int generateDisplayLists(int count) {
        if (count < 1) throw new IllegalArgumentException("Le nombre de listes doit être positif.");
        int id = GL11.glGenLists(count);
        if (id != 0) DISPLAY_LISTS.add(id);
        return id;
    }

    public static int generateTexture() {
        int id = GL11.glGenTextures();
        if (id != 0) TEXTURES.add(id);
        return id;
    }

    public static void deleteDisplayLists(int id, int count) {
        if (id != 0 && count > 0) {
            GL11.glDeleteLists(id, count);
            DISPLAY_LISTS.remove(id);
        }
    }

    public static void deleteTexture(int id) {
        if (id != 0) {
            GL11.glDeleteTextures(id);
            TEXTURES.remove(id);
        }
    }

    public static ByteBuffer createDirectByteBuffer(int size) { return BufferUtils.createByteBuffer(size); }
    public static IntBuffer createDirectIntBuffer(int size) { return BufferUtils.createIntBuffer(size); }
    public static FloatBuffer createDirectFloatBuffer(int size) { return BufferUtils.createFloatBuffer(size); }

    public static void deleteTexturesAndDisplayLists() {
        for (int texture : TEXTURES) deleteTexture(texture);
        for (int list : DISPLAY_LISTS) deleteDisplayLists(list, 1);
    }
}
