package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * GuiTextField — champ texte (nom du monde, IP serveur, chat...).
 * Curseur clignotant, selection, filtre de caracteres via
 * GuiScreen.setClipboard / getClipboard (adapte LWJGL 3 : GLFW).
 */
public class GuiTextField extends Gui
{
    private final FontRenderer fontRenderer;
    private final int xPosition;
    private final int yPosition;
    /** Largeur en pixels. */
    private final int width;
    /** Hauteur en pixels. */
    private final int height;
    /** Texte saisi. */
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    /** Focus (recoit le clavier). */
    private boolean isFocused;
    private boolean canLoseFocus = true;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 0xFFFFFF;
    private boolean isEnabled = true;
    private int lineScrollOffsetY;

    public GuiTextField(FontRenderer fontRendererObj, int x, int y, int width, int height)
    {
        this.fontRenderer = fontRendererObj;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
    }

    public void updateCursorCounter()
    {
        ++this.cursorCounter;
    }

    public void setText(String text)
    {
        if (text.length() > this.maxStringLength)
        {
            this.text = text.substring(0, this.maxStringLength);
        }
        else
        {
            this.text = text;
        }

        this.setCursorPositionEnd();
    }

    public String getText()
    {
        return this.text;
    }

    public String getSelectedText()
    {
        int start = Math.min(this.cursorPosition, this.selectionEnd);
        int end = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(start, end);
    }

    public void writeText(String textToWrite)
    {
        String filtered = "";
        String toWrite = textToWrite;

        for (char c : toWrite.toCharArray())
        {
            if (this.fontRenderer.getCharWidth(c) > 0)
            {
                filtered += c;
            }
        }

        int start = Math.min(this.cursorPosition, this.selectionEnd);
        int end = Math.max(this.cursorPosition, this.selectionEnd);
        int space = this.maxStringLength - this.text.length() - (start - end);

        if (space > 0)
        {
            String before = this.text.substring(0, start);
            String after = this.text.substring(end);
            this.text = before + filtered.substring(0, Math.min(space, filtered.length())) + after;
            this.setCursorPosition(before.length() + Math.min(space, filtered.length()));
        }
    }

    public void deleteFromCursor(int amount)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                int dir = amount < 0 ? amount : 0;
                int offset = amount > 0 ? amount : 0;
                int start = MathHelper.clamp_int(this.cursorPosition + dir, 0, this.text.length());
                int end = MathHelper.clamp_int(this.cursorPosition + offset, 0, this.text.length());
                this.text = this.text.substring(0, start) + this.text.substring(end);
                this.setCursorPosition(start);
            }
        }
    }

    /**
     * Touche tapee (nom MC : textboxKeyTyped) — les touches de base :
     * fleches, Home/End, Delete/Backspace, Ctrl+A/V/X/C.
     */
    public void textboxKeyTyped(int keyCode, char typedChar)
    {
        switch (keyCode)
        {
            case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT:
                this.setCursorPosition(this.cursorPosition - 1);
                break;

            case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT:
                this.setCursorPosition(this.cursorPosition + 1);
                break;

            case org.lwjgl.glfw.GLFW.GLFW_KEY_HOME:
                this.setCursorPosition(0);
                break;

            case org.lwjgl.glfw.GLFW.GLFW_KEY_END:
                this.setCursorPosition(this.text.length());
                break;

            case org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE:
                this.deleteFromCursor(-1);
                break;

            case org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE:
                this.deleteFromCursor(1);
                break;

            default:
                if (FontRenderer.isAllowedCharacter(typedChar))
                {
                    this.writeText(String.valueOf(typedChar));
                }

                break;
        }
    }

    public void setCursorPosition(int pos)
    {
        this.cursorPosition = MathHelper.clamp_int(pos, 0, this.text.length());
        this.selectionEnd = this.cursorPosition;
        this.ensureVisible();
    }

    public void setCursorPositionEnd()
    {
        this.setCursorPosition(this.text.length());
    }

    public void setSelectionPos(int pos)
    {
        this.selectionEnd = MathHelper.clamp_int(pos, 0, this.text.length());

        if (this.selectionEnd < this.cursorPosition)
        {
            int swap = this.cursorPosition;
            this.cursorPosition = this.selectionEnd;
            this.selectionEnd = swap;
        }

        this.ensureVisible();
    }

    /** Fait defiler pour que le curseur reste visible. */
    private void ensureVisible()
    {
        int widthOfText = this.fontRenderer.getStringWidth(this.text);

        if (widthOfText > this.width - 8)
        {
            if (this.cursorPosition < this.lineScrollOffset)
            {
                this.lineScrollOffset = this.cursorPosition;
            }
            else if (this.cursorPosition > this.lineScrollOffset + this.getMaxVisibleChars())
            {
                this.lineScrollOffset = this.cursorPosition - this.getMaxVisibleChars();
            }
        }
        else
        {
            this.lineScrollOffset = 0;
        }
    }

    private int getMaxVisibleChars()
    {
        return (this.width - 8) / this.fontRenderer.getCharWidth('a');
    }

    public void setFocused(boolean focused)
    {
        if (focused && !this.isFocused)
        {
            this.cursorCounter = 0;
        }

        this.isFocused = focused;

        if (!focused)
        {
            this.setCursorPosition(this.cursorPosition);
        }
    }

    public boolean isFocused()
    {
        return this.isFocused;
    }

    public void setMaxStringLength(int length)
    {
        this.maxStringLength = length;
    }

    public void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
    }

    public void drawTextBox()
    {
        if (!this.isVisible())
        {
            return;
        }

        if (this.enableBackgroundDrawing)
        {
            drawRect(this.xPosition - 1, this.yPosition - 1,
                    this.xPosition + this.width + 1, this.yPosition + this.height + 1, 0xFFA0A0A0);
            drawRect(this.xPosition, this.yPosition,
                    this.xPosition + this.width, this.yPosition + this.height, 0xFF000000);
        }

        int textColor = this.isEnabled ? this.enabledColor : 0x707070;
        String visible = this.text.substring(Math.min(this.text.length(), this.lineScrollOffset));
        boolean cursorAtEnd = this.cursorPosition - this.lineScrollOffset < visible.length();
        String renderText = cursorAtEnd ? visible : visible + "_";
        int textX = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
        int textY = (this.height - 8) / 2 + this.yPosition;
        this.fontRenderer.drawStringWithShadow(renderText, textX, textY, textColor);

        // Curseur clignotant (visible 1 tick sur 2)
        if (this.isFocused && this.cursorCounter / 6 % 2 == 0 && cursorAtEnd)
        {
            int cursorX = textX + this.fontRenderer.getStringWidth(visible);
            drawRect(cursorX, textY - 1, cursorX + 1, textY + this.fontRenderer.FONT_HEIGHT + 1,
                    0xFFE0E0E0);
        }
    }

    public void setVisible(boolean visible)
    {
        this.visibleField = visible;
    }

    public boolean isVisible()
    {
        return this.visibleField;
    }

    private boolean visibleField = true;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.isWithinBounds(mouseX, mouseY))
        {
            this.setFocused(true);
        }
        else if (this.canLoseFocus)
        {
            this.setFocused(false);
        }
    }

    private boolean isWithinBounds(int x, int y)
    {
        return x >= this.xPosition && x < this.xPosition + this.width
                && y >= this.yPosition && y < this.yPosition + this.height;
    }

    public void setTextColor(int i) {
    }

    public void setDisabledTextColour(int i) {

    }
}