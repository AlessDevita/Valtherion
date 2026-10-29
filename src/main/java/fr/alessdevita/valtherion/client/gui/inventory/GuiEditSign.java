package fr.alessdevita.valtherion.client.gui.inventory;

import fr.alessdevita.valtherion.client.gui.FontRenderer;

import fr.alessdevita.valtherion.client.gui.GuiButton;
import fr.alessdevita.valtherion.client.gui.GuiScreen;

/**
 * GuiEditSign — l'edition de pancarte : champ de saisie mono-ligne,
 * les 4 lignes de la pancarte affichees, bouton Terminer.
 */
public class GuiEditSign extends GuiScreen
{
    /** La pancarte editee (TileEntitySign-equivalent). */
    private final Object tileSign;
    /** Ligne en cours d'edition (0-3). */
    private int editLine = 0;
    /** Texte de la ligne courante. */
    private String lineText = "";
    private int updateCounter;

    public GuiEditSign(Object sign)
    {
        this.tileSign = sign;
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
                this.height / 4 + 120, 200, 20, "Terminer"));
    }

    @Override
    public void updateScreen()
    {
        ++this.updateCounter;
    }

    @Override
    public void keyTyped(int keyCode)
    {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER)
        {
            // Valide la ligne et passe a la suivante (retour au jeu apres la 4e)
            this.setLine(this.editLine, this.lineText);
            ++this.editLine;

            if (this.editLine >= 4)
            {
                this.saveSign();
            }
            else
            {
                this.lineText = this.getLine(this.editLine);
            }
        }
        else if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE)
        {
            if (this.lineText.length() > 0)
            {
                this.lineText = this.lineText.substring(0, this.lineText.length() - 1);
            }
        }
        else if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE)
        {
            this.saveSign();
        }
        else
        {
            // Caractere imprime
            // TODO : filtrer via FontRenderer.isAllowedCharacter + limite ~15 chars
            if (this.lineText.length() < 15)
            {
                this.lineText += (char) keyCode;
            }
        }
    }

    private String getLine(int line)
    {
        // TODO : lire depuis la TileEntitySign
        return "";
    }

    private void setLine(int line, String text)
    {
        // TODO : ecrire dans la TileEntitySign
    }

    private void saveSign()
    {
        // TODO : envoyer le packet de mise a jour de la pancarte
        this.tileSign.getClass(); // marqueur
        this.valtherion.displayGuiScreen(null);
        this.valtherion.setIngameFocus();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.saveSign();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, "Modifier le message de la pancarte",
                this.width / 2, 40, 0xFFFFFF);

        // Apercu des 4 lignes, curseur clignotant sur la ligne courante
        for (int i = 0; i < 4; ++i)
        {
            String line = i == this.editLine
                    ? (this.updateCounter / 12 % 2 == 0 ? this.lineText + "_" : this.lineText)
                    : this.getLine(i);
            this.drawCenteredString(this.valtherion.fontRenderer, line,
                    this.width / 2, 70 + i * 16, 0xE0E0E0);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(FontRenderer fontRenderer, String s, int i, int i1, int i2) {
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}