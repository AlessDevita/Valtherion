package fr.alessdevita.valtherion.client.renderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/** Fonctions OpenGL optionnelles regroupées pour le moteur. */
public final class OpenGlHelper {
    public static int defaultTexUnit = org.lwjgl.opengl.GL13.GL_TEXTURE0;
    public static int lightmapTexUnit = org.lwjgl.opengl.GL13.GL_TEXTURE1;
    public static int GL_FRAMEBUFFER = 0x8D40;
    public static int GL_COLOR_ATTACHMENT0 = 0x8CE0;
    private static boolean shadersSupported;

    private OpenGlHelper() { }

    public static void initializeTextures() {
        shadersSupported = GL.getCapabilities().OpenGL20;
    }

    public static boolean useShaders() {
        try { return shadersSupported || GL.getCapabilities().OpenGL20; }
        catch (IllegalStateException noContext) { return false; }
    }
    public static void setActiveTexture(int textureUnit) { GL13Compat.activeTexture(textureUnit); }
    public static void setClientActiveTexture(int textureUnit) { GL13Compat.clientActiveTexture(textureUnit); }
    public static void glBlendFunc(int source, int destination, int sourceAlpha, int destinationAlpha) {
        if (GL.getCapabilities().OpenGL14) {
            org.lwjgl.opengl.GL14.glBlendFuncSeparate(source, destination, sourceAlpha, destinationAlpha);
        } else {
            GL11.glBlendFunc(source, destination);
        }
    }

    private static final class GL13Compat {
        static void activeTexture(int unit) { org.lwjgl.opengl.GL13.glActiveTexture(unit); }
        static void clientActiveTexture(int unit) { org.lwjgl.opengl.GL13.glClientActiveTexture(unit); }
    }
}
