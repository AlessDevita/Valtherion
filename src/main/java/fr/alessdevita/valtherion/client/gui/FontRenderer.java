package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import fr.alessdevita.valtherion.client.resources.ResourceManager;
import fr.alessdevita.valtherion.client.settings.GameSettings;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.FileInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * FontRenderer utilisant STB TrueType pour rendre du texte avec de vraies polices.
 */
public class FontRenderer {

    public static final int FONT_HEIGHT = 16;

    private static final int ATLAS_WIDTH = 512;
    private static final int ATLAS_HEIGHT = 256;
    private static final int FIRST_CHAR = 32;
    private static final int CHAR_COUNT = 95;

    private float fontSize;
    private float charSpacing;
    private Vector4f textColor;
    private boolean isInitialized;
    private int fontTextureId;
    private STBTTBakedChar.Buffer charDataBuffer;
    private float maxGlyphHeight;

    public FontRenderer(float fontSize) {
        this.fontSize = fontSize;
        this.charSpacing = 1.0f;
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.isInitialized = false;
        this.fontTextureId = 0;
        this.charDataBuffer = null;
    }

    public FontRenderer(GameSettings gameSettings, String s, ResourceManager resourceManager, boolean b) {
        this.fontSize = 16.0f;
        this.charSpacing = 1.0f;
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.isInitialized = false;
        this.fontTextureId = 0;
        this.charDataBuffer = null;
    }

    public FontRenderer() {
        this(16.0f);
    }

    public FontRenderer(GameSettings gameSettings, String s, TextureManager renderEngine, boolean b) {
        this.fontSize = 16.0f;
        this.charSpacing = 1.0f;
        this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.isInitialized = false;
        this.fontTextureId = 0;
        this.charDataBuffer = null;
    }

    public static boolean isAllowedCharacter(char typedChar) {
        return typedChar >= FIRST_CHAR && typedChar <= 126;
    }

    public void init() {
        if (isInitialized) return;

        try {
            File fontFile = new File("C:/Windows/Fonts/arial.ttf");
            if (!fontFile.exists()) {
                fontFile = new File("C:/Windows/Fonts/arialbd.ttf");
            }
            if (!fontFile.exists()) {
                fontFile = new File("C:/Windows/Fonts/times.ttf");
            }
            if (!fontFile.exists()) {
                System.err.println("[FontRenderer] Police systeme non trouvee");
                this.isInitialized = true;
                this.maxGlyphHeight = this.fontSize;
                return;
            }

            ByteBuffer fontBuffer;
            try (FileInputStream fis = new FileInputStream(fontFile)) {
                fontBuffer = fis.getChannel().map(
                    java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fis.getChannel().size());
            }

            ByteBuffer atlasBuffer = org.lwjgl.BufferUtils.createByteBuffer(ATLAS_WIDTH * ATLAS_HEIGHT);
            atlasBuffer.clear();

            this.charDataBuffer = STBTTBakedChar.calloc(CHAR_COUNT);

            int result = STBTruetype.stbtt_BakeFontBitmap(
                fontBuffer, (float) this.fontSize, atlasBuffer,
                ATLAS_WIDTH, ATLAS_HEIGHT, FIRST_CHAR, charDataBuffer
            );

            if (result < 0) {
                System.err.println("[FontRenderer] stbtt_BakeFontBitmap echoue: " + result);
                this.isInitialized = true;
                this.maxGlyphHeight = this.fontSize;
                return;
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                STBTTFontinfo fontInfo = STBTTFontinfo.mallocStack(stack);
                STBTruetype.stbtt_InitFont(fontInfo, fontBuffer, 0);

                IntBuffer ascent = stack.mallocInt(1);
                IntBuffer descent = stack.mallocInt(1);
                IntBuffer lineGap = stack.mallocInt(1);
                STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

                float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, (int) this.fontSize);
                this.maxGlyphHeight = (ascent.get(0) - descent.get(0)) * scale;
            }

            this.fontTextureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA,
                ATLAS_WIDTH, ATLAS_HEIGHT, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, atlasBuffer);

            this.isInitialized = true;
            System.out.println("[FontRenderer] Police chargee avec succes, taille: " + fontSize + "px");

        } catch (Exception e) {
            System.err.println("[FontRenderer] Erreur: " + e.getMessage());
            e.printStackTrace();
            this.isInitialized = true;
            this.maxGlyphHeight = this.fontSize;
        }
    }

    public void renderText(String text, float x, float y) {
        if (!isInitialized || charDataBuffer == null || text == null) {
            return;
        }

        float cursorX = x;
        float baseY = y;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureId);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);

        float[] xPos = new float[]{cursorX};
        float[] yPos = new float[]{baseY};
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c < FIRST_CHAR || c > 126) {
                xPos[0] += this.fontSize * 0.6f;
                continue;
            }

            int idx = c - FIRST_CHAR;
            if (idx < 0 || idx >= charDataBuffer.capacity()) {
                xPos[0] += this.fontSize * 0.6f;
                continue;
            }

            // Utiliser stbtt_GetBakedQuad pour calculer les coordonnées correctement
            STBTTAlignedQuad quad = STBTTAlignedQuad.calloc();
            STBTruetype.stbtt_GetBakedQuad(charDataBuffer, ATLAS_WIDTH, ATLAS_HEIGHT, idx,
                xPos, yPos, quad, true);
            
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(quad.s0(), quad.t0());
            GL11.glVertex2f(quad.x0(), quad.y0());
            GL11.glTexCoord2f(quad.s1(), quad.t0());
            GL11.glVertex2f(quad.x1(), quad.y0());
            GL11.glTexCoord2f(quad.s1(), quad.t1());
            GL11.glVertex2f(quad.x1(), quad.y1());
            GL11.glTexCoord2f(quad.s0(), quad.t1());
            GL11.glVertex2f(quad.x0(), quad.y1());
            GL11.glEnd();
            
            quad.free();

            xPos[0] += charSpacing;
        }
    }

    public float getTextWidth(String text) {
        if (text == null) return 0;
        if (!isInitialized || charDataBuffer == null) return text.length() * fontSize * 0.6f;

        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= FIRST_CHAR && c <= 126) {
                int idx = c - FIRST_CHAR;
                if (idx >= 0 && idx < charDataBuffer.capacity()) {
                    STBTTBakedChar bc = charDataBuffer.get(idx);
                    width += bc.xadvance() + charSpacing;
                } else {
                    width += fontSize * 0.6f + charSpacing;
                }
            } else {
                width += fontSize * 0.6f + charSpacing;
            }
        }
        return width;
    }

    public float getTextHeight() {
        return maxGlyphHeight > 0 ? maxGlyphHeight : fontSize;
    }

    public float getFontSize() { return fontSize; }
    public void setFontSize(float fontSize) { this.fontSize = fontSize; this.isInitialized = false; }
    public float getCharSpacing() { return charSpacing; }
    public void setCharSpacing(float charSpacing) { this.charSpacing = charSpacing; }
    public Vector4f getTextColor() { return textColor; }
    public void setTextColor(Vector4f textColor) { this.textColor = textColor; }
    public boolean isInitialized() { return isInitialized; }

    public void cleanup() {
        if (fontTextureId != 0) {
            GL11.glDeleteTextures(fontTextureId);
            fontTextureId = 0;
        }
        isInitialized = false;
        charDataBuffer = null;
    }

    @Override
    public String toString() {
        return "FontRenderer{fontSize=" + fontSize + ", isInitialized=" + isInitialized + '}';
    }

    public int getCharWidth(char c) {
        if (!isInitialized || charDataBuffer == null || c < FIRST_CHAR || c > 126) return (int) (fontSize * 0.6f);
        int idx = c - FIRST_CHAR;
        if (idx >= 0 && idx < charDataBuffer.capacity()) {
            STBTTBakedChar bc = charDataBuffer.get(idx);
            return (int) bc.xadvance();
        }
        return (int) (fontSize * 0.6f);
    }

    public int getStringWidth(String text) {
        if (text == null) return 0;
        return (int) getTextWidth(text);
    }

    public void drawStringWithShadow(String renderText, int textX, int textY, int textColor) {
        if (renderText == null) return;
        drawString(renderText, textX + 1, textY + 1, 0x000000);
        drawString(renderText, textX, textY, textColor);
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        if (text == null) return;
        drawString(text, x - getStringWidth(text) / 2, y, color);
    }

    public void drawString(String text, int x, int y, int color) {
        if (text == null) return;
        Vector4f previous = this.textColor;
        this.textColor = new Vector4f((color >> 16 & 255) / 255.0F,
                (color >> 8 & 255) / 255.0F,
                (color & 255) / 255.0F,
                1.0F);
        renderText(text, x, y);
        this.textColor = previous;
    }
}
