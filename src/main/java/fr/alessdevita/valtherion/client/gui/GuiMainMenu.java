package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/** Menu principal Valtherion : background, logo en haut, 4 boutons verticaux avec texture PNG. */
public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private static final String BACKGROUND_RESOURCE = "/assets/valtherion/textures/gui/title/background/background.png";
    private static final String LOGO_RESOURCE = "/assets/valtherion/textures/gui/title/valtherion.png";
    private static final String BUTTON_TEXTURE_RESOURCE = "/assets/valtherion/textures/gui/sprites/widget/button.png";

    private static int backgroundTexture = 0;
    private static int backgroundWidth = 0;
    private static int backgroundHeight = 0;

    private static int logoTexture = 0;
    private static int logoWidth = 0;
    private static int logoHeight = 0;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_SPACING = 2;

    @Override
    public void initGui() {
        this.buttonList.clear();

        // Calculer la position du logo
        int logoDisplayHeight = 120;
        int logoY = (this.height / 2) - 120;

        // Les boutons commencent bien en dessous du logo avec une marge de 20px
        int startY = logoY + logoDisplayHeight + 20;

        // Bouton 1: Jouer (id=1)
        int x = (this.width - BUTTON_WIDTH) / 2;
        this.buttonList.add(new GuiButton(1, x, startY, BUTTON_WIDTH, BUTTON_HEIGHT, "Jouer", BUTTON_TEXTURE_RESOURCE));

        // Bouton 2: Multijoueur (id=2)
        this.buttonList.add(new GuiButton(2, x, startY + BUTTON_HEIGHT + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT, "Multijoueur", BUTTON_TEXTURE_RESOURCE));

        // Bouton 3: Paramètres (id=0)
        this.buttonList.add(new GuiButton(0, x, startY + 2 * (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, "Parametres", BUTTON_TEXTURE_RESOURCE));

        // Bouton 4: Quitter (id=4)
        this.buttonList.add(new GuiButton(4, x, startY + 3 * (BUTTON_HEIGHT + BUTTON_SPACING), BUTTON_WIDTH, BUTTON_HEIGHT, "Quitter", BUTTON_TEXTURE_RESOURCE));
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
        // Dessiner le background du menu
        drawMenuBackground();

        // Dessiner le logo Valtherion centré en haut
        drawLogo();

        // Dessiner les boutons
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawMenuBackground() {
        int texture = getBackgroundTexture();
        if (texture == 0 || backgroundWidth == 0 || backgroundHeight == 0) {
            // Fallback : fond sombre si le background ne peut pas être chargé
            this.drawDefaultBackground();
            return;
        }

        // Ajuster le background pour remplir l'écran tout en conservant le ratio
        float scaleX = (float) this.width / backgroundWidth;
        float scaleY = (float) this.height / backgroundHeight;
        float scale = Math.max(scaleX, scaleY);

        int drawWidth = (int) (backgroundWidth * scale);
        int drawHeight = (int) (backgroundHeight * scale);
        int x = (this.width - drawWidth) / 2;
        int y = (this.height - drawHeight) / 2;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(x + drawWidth, y);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(x + drawWidth, y + drawHeight);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(x, y + drawHeight);
        GL11.glEnd();
    }

    private static int getBackgroundTexture() {
        if (backgroundTexture != 0) return backgroundTexture;
        try (InputStream input = GuiMainMenu.class.getResourceAsStream(BACKGROUND_RESOURCE)) {
            if (input == null) {
                System.err.println("Background non trouvé : " + BACKGROUND_RESOURCE);
                return 0;
            }
            byte[] bytes = input.readAllBytes();
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger le background : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                backgroundWidth = width.get(0);
                backgroundHeight = height.get(0);
                try {
                    backgroundTexture = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTexture);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            backgroundWidth, backgroundHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (IOException | RuntimeException exception) {
            System.err.println("Impossible de charger le background : " + exception.getMessage());
            return 0;
        }
        return backgroundTexture;
    }

    private void drawLogo() {
        int texture = getLogoTexture();
        if (texture == 0 || logoWidth == 0 || logoHeight == 0) {
            // Si le logo ne peut pas être chargé, afficher le texte "Valtherion" à la place
            if (this.valtherion.fontRenderer != null) {
                int titleY = (this.height / 2) - 100;
                this.valtherion.fontRenderer.drawCenteredString("Valtherion", this.width / 2, titleY, 0xFFFFFF);
            }
            return;
        }

        // Calculer la taille d'affichage du logo (hauteur fixe de 120px, largeur proportionnelle)
        int displayHeight = 120;
        int displayWidth = (logoWidth * displayHeight) / logoHeight;
        int logoX = (this.width - displayWidth) / 2;
        int logoY = (this.height / 2) - 120;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0F, 0F); GL11.glVertex2f(logoX, logoY);
        GL11.glTexCoord2f(1F, 0F); GL11.glVertex2f(logoX + displayWidth, logoY);
        GL11.glTexCoord2f(1F, 1F); GL11.glVertex2f(logoX + displayWidth, logoY + displayHeight);
        GL11.glTexCoord2f(0F, 1F); GL11.glVertex2f(logoX, logoY + displayHeight);
        GL11.glEnd();
    }

    private static int getLogoTexture() {
        if (logoTexture != 0) return logoTexture;
        try (InputStream input = GuiMainMenu.class.getResourceAsStream(LOGO_RESOURCE)) {
            if (input == null) {
                System.err.println("Logo non trouvé : " + LOGO_RESOURCE);
                return 0;
            }
            byte[] bytes = input.readAllBytes();
            ByteBuffer encoded = org.lwjgl.BufferUtils.createByteBuffer(bytes.length);
            encoded.put(bytes).flip();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(encoded, width, height, channels, 4);
                if (pixels == null) {
                    System.err.println("Impossible de charger le logo : " + STBImage.stbi_failure_reason());
                    return 0;
                }
                logoWidth = width.get(0);
                logoHeight = height.get(0);
                try {
                    logoTexture = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, logoTexture);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                            logoWidth, logoHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                } finally {
                    STBImage.stbi_image_free(pixels);
                }
            }
        } catch (IOException | RuntimeException exception) {
            System.err.println("Impossible de charger le logo : " + exception.getMessage());
            return 0;
        }
        return logoTexture;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        this.valtherion.displayGuiScreen(this);
    }
}
