package fr.alessdevita.valtherion.client.renderer;

import org.lwjgl.opengl.GL11;

/** État d'éclairage OpenGL fixe pour les rendus compatibles immédiats. */
public final class RenderHelper {
    private RenderHelper() { }

    public static void enableStandardItemLighting() {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
        setLight(GL11.GL_LIGHT0, 0.35F, 0.7F, 0.8F, 0.8F);
        setLight(GL11.GL_LIGHT1, -0.35F, 0.4F, -0.6F, 0.5F);
    }

    public static void disableStandardItemLighting() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glDisable(GL11.GL_LIGHT1);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
    }

    public static void enableGUIStandardItemLighting() {
        GL11.glPushMatrix();
        GL11.glRotatef(30F, 1F, 0F, 0F);
        GL11.glRotatef(225F, 0F, 1F, 0F);
        enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private static void setLight(int light, float x, float y, float z, float intensity) {
        org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush();
        try {
            java.nio.FloatBuffer position = stack.floats(x, y, z, 0F);
            java.nio.FloatBuffer diffuse = stack.floats(intensity, intensity, intensity, 1F);
            GL11.glLightfv(light, GL11.GL_POSITION, position);
            GL11.glLightfv(light, GL11.GL_DIFFUSE, diffuse);
        } finally {
            stack.close();
        }
    }
}
