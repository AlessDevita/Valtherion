package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/** Menu principal Valtherion, adapté à l'API GUI du projet. */
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    private static final String BACKGROUND_RESOURCE = "/textures/gui/title/background/basalt_menu.png";
    private static final int IMAGE_WIDTH = 1672;
    private static final int IMAGE_HEIGHT = 940;
    private static int backgroundTexture;
    private float imageX;
    private float imageY;
    private float imageScale;

    @Override
    public void initGui() {
        this.buttonList.clear();
        // L'image contient déjà les libellés et le dessin des boutons.
        this.imageScale = Math.min((float) this.width / IMAGE_WIDTH, (float) this.height / IMAGE_HEIGHT);
        this.imageX = (this.width - IMAGE_WIDTH * this.imageScale) / 2.0F;
        this.imageY = (this.height - IMAGE_HEIGHT * this.imageScale) / 2.0F;
        addMenuButton(1, 642, 397, 1029, 476);
        addMenuButton(2, 642, 487, 1030, 558);
        addMenuButton(0, 642, 574, 1030, 644);
        addMenuButton(4, 642, 662, 1030, 733);
    }

    private void addMenuButton(int id, int left, int top, int right, int bottom) {
        int x = Math.round(this.imageX + left * this.imageScale);
        int y = Math.round(this.imageY + top * this.imageScale);
        int width = Math.round((right - left) * this.imageScale);
        int height = Math.round((bottom - top) * this.imageScale);
        this.buttonList.add(new MenuButton(id, x, y, width, height));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == null || !button.enabled) return;
        switch (button.id) {
            case 0: this.valtherion.displayGuiScreen(new GuiOptions(this)); break;
            case 1: this.valtherion.displayGuiScreen(new GuiSelectWorld(this)); break;
            case 2: this.valtherion.displayGuiScreen(new GuiMultiplayer(this.valtherion, this.width, this.height)); break;
            case 4: this.valtherion.shutdown(); break;
            default: break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        int texture = getBackgroundTexture();
        if (texture == 0) {
            drawRect(0, 0, this.width, this.height, 0xFF101827);
            return;
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glBegin(GL11.GL_QUADS);
        float imageRight = this.imageX + IMAGE_WIDTH * this.imageScale;
        float imageBottom = this.imageY + IMAGE_HEIGHT * this.imageScale;
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(this.imageX, this.imageY);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(imageRight, this.imageY);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(imageRight, imageBottom);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(this.imageX, imageBottom);
        GL11.glEnd();
        // Pas de dessin de boutons ici : l'illustration contient leur apparence.
    }

    private static int getBackgroundTexture() {
        if (backgroundTexture != 0) return backgroundTexture;
        try (InputStream input = GuiMainMenu.class.getResourceAsStream(BACKGROUND_RESOURCE)) {
            if (input == null) return 0;
            byte[] bytes = input.readAllBytes();
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger le fond du menu : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                try {
                    backgroundTexture = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            width.get(0), height.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (IOException | RuntimeException exception) {
            System.err.println("Impossible de charger le fond du menu : " + exception.getMessage());
            return 0;
        }
        return backgroundTexture;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        this.valtherion.displayGuiScreen(this);
    }

    private static final class MenuButton extends GuiButton {
        private final int x, y, buttonWidth, buttonHeight;

        private MenuButton(int id, int x, int y, int width, int height) {
            super(x, y, width, height, "");
            this.id = id;
            this.x = x;
            this.y = y;
            this.buttonWidth = width;
            this.buttonHeight = height;
            this.enabled = true;
        }

        @Override
        public void drawButton(Valtherion client, int mouseX, int mouseY) { }

        @Override
        public boolean mousePressed(Valtherion client, int mouseX, int mouseY) {
            return this.enabled && mouseX >= this.x && mouseX < this.x + this.buttonWidth
                    && mouseY >= this.y && mouseY < this.y + this.buttonHeight;
        }
    }
}
