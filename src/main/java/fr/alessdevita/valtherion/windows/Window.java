package fr.alessdevita.valtherion.windows;

import fr.alessdevita.valtherion.input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private MouseInput mouseInput;
    private long handle;
    private int width;
    private int height;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void setMouseInput(MouseInput input) {
        this.mouseInput = input;
    }

    public void create() {
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to init GLFW");

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) throw new IllegalStateException("Window creation failed");

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSetCursorPosCallback(handle, (window, x, y) -> {
            if (mouseInput != null) {
                mouseInput.move(x, y);
            }
        });
        GL.createCapabilities();
        GLFW.glfwSwapInterval(1);

        GL11.glViewport(0, 0, width, height);
    }

    public void showCursor() {
        GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public void hideCursor() {
        GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public boolean hasCursorHidden() {
        return GLFW.glfwGetInputMode(handle, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
    }

    public void showWindow() {
        GLFW.glfwShowWindow(handle);
    }

    public void hideWindow() {
        GLFW.glfwHideWindow(handle);
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    public long handle() {
        return handle;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}


