package fr.alessdevita.valtherion.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * GuiScreen — ecran GUI de base : gestion des boutons, champs texte,
 * input clavier/souris (LWJGL 3 / GLFW), et drawDefaultBackground.
 */
public abstract class GuiScreen extends Gui
{
    private static final int GL_TEXTURE_2D = org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
    private static final int GL_BLEND = org.lwjgl.opengl.GL11.GL_BLEND;
    private static final int GL_ALPHA_TEST = org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
    private static final int GL_SRC_ALPHA = org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
    private static final int GL_ONE_MINUS_SRC_ALPHA = org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
    private static final int GL_SMOOTH = org.lwjgl.opengl.GL11.GL_SMOOTH;
    private static final int GL_QUADS = org.lwjgl.opengl.GL11.GL_QUADS;
    private static final int GL_FLAT = org.lwjgl.opengl.GL11.GL_FLAT;
    protected Valtherion valtherion;
    /** Liste des boutons de l'ecran. */
    protected List<GuiButton> buttonList = Lists.newArrayList();
    public int width;
    public int height;
    /** Tous les elements recevant les clics (boutons + autres). */
    protected List<Gui> field_146292_n = Lists.newArrayList();
    protected boolean allowUserInput;
    /** GuiScreen parent (pour le bouton Retour/Echap). */
    protected GuiScreen parentScreen;
    /** Etat "un bouton est presse ce tick" pour eviter les double-clics. */
    private boolean clickedButton;

    public void updateScreen()
    {
    }

    public void setWorldAndResolution(Valtherion valtherion, int width, int height)
    {
        this.valtherion = valtherion;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.field_146292_n.clear();
        this.initGui();
    }

    public boolean allowsUserInput() { return this.allowUserInput; }

    public void displayProgressMessage(String message)
    {
    }

    public void setLoadingProgress(int progress)
    {
    }

    /** Initialise les boutons et composants — surchargez cette methode. */
    public void initGui()
    {
    }

    /** Appelle drawScreen sur tous les boutons. */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        for (GuiButton guibutton : this.buttonList)
        {
            guibutton.drawButton(this.valtherion, mouseX, mouseY);
        }
    }

    /** Traitement des touches : surchargez handleKeyboardInput pour les votres. */
    public void handleInput(int keyCode)
    {
        this.handleKeyboardInput(keyCode);
    }

    public void handleKeyboardInput(int keyCode)
    {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
        else
        {
            this.keyTyped(keyCode);
        }
    }

    /** Touche tapee — surcharge (nom MC : keyTyped / func_146282). */
    public void keyTyped(int keyCode)
    {
    }

    /** Clic souris : distribue aux boutons/champs (nom MC : mouseClicked). */
    public void handleMouseInput(int button, int action)
    {
        if (action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            double[] xPos = new double[1];
            double[] yPos = new double[1];
            GLFW.glfwGetCursorPos(this.valtherion.getWindowHandle(), xPos, yPos);
            int mouseX = (int) (xPos[0] * this.width / Math.max(1, this.valtherion.displayWidth));
            int mouseY = (int) (yPos[0] * this.height / Math.max(1, this.valtherion.displayHeight));

            for (GuiButton guibutton : this.buttonList)
            {
                if (guibutton.mousePressed(this.valtherion, mouseX, mouseY))
                {
                    guibutton.playPressSound();
                    this.actionPerformed(guibutton);
                    this.clickedButton = true;
                }
            }
        }
        else if (action == GLFW.GLFW_RELEASE && button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            for (GuiButton guibutton : this.buttonList)
            {
                guibutton.mouseReleased(mouseXUnprojected(), mouseYUnprojected());
            }

            this.clickedButton = false;
        }
    }

    private int mouseXUnprojected()
    {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        GLFW.glfwGetCursorPos(this.valtherion.getWindowHandle(), xPos, yPos);
        return (int) (xPos[0] * this.width / Math.max(1, this.valtherion.displayWidth));
    }

    private int mouseYUnprojected()
    {
        double[] xPos = new double[1];
        double[] yPos = new double[1];
        GLFW.glfwGetCursorPos(this.valtherion.getWindowHandle(), xPos, yPos);
        return (int) (yPos[0] * this.height / Math.max(1, this.valtherion.displayHeight));
    }

    /** Action sur un bouton — surchargez selon l'id du bouton. */
    protected void actionPerformed(GuiButton button)
    {
    }

    public void onGuiClosed()
    {
    }

    public void drawDefaultBackground()
    {
        if (this.valtherion.theWorld != null)
        {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -8042536);
        }
        else
        {
            this.drawRect(0, 0, this.width, this.height, 0xFF000000);
        }
    }

    /** Dessine un rectangle degradé vertical (identique MC). */
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        org.lwjgl.opengl.GL11.glDisable(GL_TEXTURE_2D);
        org.lwjgl.opengl.GL11.glEnable(GL_BLEND);
        org.lwjgl.opengl.GL11.glDisable(GL_ALPHA_TEST);
        org.lwjgl.opengl.GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        org.lwjgl.opengl.GL11.glShadeModel(GL_SMOOTH);

        // vertex order as in MC 1.7/1.8 (tessellator replaced by immediate GL)
        org.lwjgl.opengl.GL11.glBegin(GL_QUADS);
        org.lwjgl.opengl.GL11.glColor4f(f1, f2, f3, f);
        org.lwjgl.opengl.GL11.glVertex2f((float) right, (float) top);
        org.lwjgl.opengl.GL11.glVertex2f((float) left, (float) top);
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        org.lwjgl.opengl.GL11.glColor4f(f5, f6, f7, f4);
        org.lwjgl.opengl.GL11.glVertex2f((float) left, (float) bottom);
        org.lwjgl.opengl.GL11.glVertex2f((float) right, (float) bottom);
        org.lwjgl.opengl.GL11.glEnd();

        org.lwjgl.opengl.GL11.glShadeModel(GL_FLAT);
        org.lwjgl.opengl.GL11.glDisable(GL_BLEND);
        org.lwjgl.opengl.GL11.glEnable(GL_ALPHA_TEST);
        org.lwjgl.opengl.GL11.glEnable(GL_TEXTURE_2D);
    }

    /** True si cet ecran met le jeu en pause (single player). */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Confirme une action asynchrone (nom MC : confirmClicked / func_146284).
     */
    public void confirmClicked(boolean result, int id)
    {
    }

    public static String getLocalizedString(String key)
    {
        return key;
    }
}
