package fr.alessdevita.valtherion.client.renderer;

import org.lwjgl.opengl.GL;

/** Résumé des capacités OpenGL disponibles après création du contexte. */
public class OpenGlCapsChecker {
    public String checkOpenGLCaps() {
        try {
            org.lwjgl.opengl.GLCapabilities caps = GL.getCapabilities();
            return "OpenGL " + org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_VERSION)
                    + " | shaders=" + caps.OpenGL20 + " | VBO=" + caps.OpenGL15;
        } catch (IllegalStateException exception) {
            return "Contexte OpenGL non initialisé";
        }
    }

    public boolean hasOpenGLVersion(int major, int minor) {
        try {
            org.lwjgl.opengl.GLCapabilities caps = GL.getCapabilities();
            if (major <= 1) return true;
            if (major == 2) return caps.OpenGL20 && (minor <= 0 || minor == 1 && caps.OpenGL21);
            if (major == 3) return minor == 0 ? caps.OpenGL30 : minor == 1 ? caps.OpenGL31
                    : minor == 2 ? caps.OpenGL32 : minor == 3 && caps.OpenGL33;
            if (major == 4) return minor == 0 ? caps.OpenGL40 : minor == 1 ? caps.OpenGL41
                    : minor == 2 ? caps.OpenGL42 : minor == 3 && caps.OpenGL43;
            return false;
        } catch (IllegalStateException exception) {
            return false;
        }
    }
}
