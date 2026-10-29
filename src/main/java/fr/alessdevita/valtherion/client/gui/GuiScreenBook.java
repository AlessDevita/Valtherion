package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * GuiScreenBook — le livre editable (livre et plume) : pages navigables,
 * zone de texte editable par page, boutons page suivante/precedente,
 * sign/signature.
 */
public class GuiScreenBook extends GuiScreen
{
    /** Pages du livre (String par page). */
    private final List<String> bookPages = Lists.newArrayList();
    /** Page courante. */
    private int currPage;
    /** Champ texte de la page courante. */
    private GuiTextField field_146159_z;
    private final GuiScreen parentScreen;

    public GuiScreenBook(GuiScreen parent, List<String> pages)
    {
        this.parentScreen = parent;
        this.bookPages.addAll(pages);

        if (this.bookPages.isEmpty())
        {
            this.bookPages.add("");
        }
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 40,
                95, 20, "Page prec."));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 40,
                95, 20, "Page suiv."));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height - 20,
                200, 20, "Signer et fermer"));
        this.updatePage();
    }

    /** Recree le champ texte avec le contenu de la page courante. */
    private void updatePage()
    {
        String text = this.bookPages.get(this.currPage);
        this.field_146159_z = new GuiTextField((FontRenderer) this.valtherion.fontRenderer,
                (this.width - 220) / 2, 40, 220, this.height - 90);
        this.field_146159_z.setMaxStringLength(256);
        this.field_146159_z.setFocused(true);
        this.field_146159_z.setText(text);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.field_146159_z.updateCursorCounter();
    }

    @Override
    public void keyTyped(int keyCode)
    {
        this.field_146159_z.textboxKeyTyped(keyCode, (char) 0);

        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER)
        {
            // Sauvegarde de la page avant navigation eventuelle
            this.bookPages.set(this.currPage, this.field_146159_z.getText());
        }
        else
        {
            this.bookPages.set(this.currPage, this.field_146159_z.getText());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        // Sauvegarde la page courante avant toute action
        this.bookPages.set(this.currPage, this.field_146159_z.getText());

        if (button.id == 0 && this.currPage > 0)
        {
            --this.currPage;
            this.updatePage();
        }
        else if (button.id == 1 && this.currPage < this.bookPages.size() - 1)
        {
            ++this.currPage;
            this.updatePage();
        }
        else if (button.id == 1 && this.currPage == this.bookPages.size() - 1)
        {
            // Nouvelle page
            this.bookPages.add("");
            ++this.currPage;
            this.updatePage();
        }
        else if (button.id == 2)
        {
            // TODO : signer le livre (envoi au serveur / sauvegarde NBT)
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        // Fond du livre (page papier)
        Gui.drawRect((this.width - 260) / 2, 20,
                (this.width + 260) / 2, this.height - 50, 0xFFF0EBC0);
        this.drawString(this.valtherion.fontRenderer,
                "Page " + (this.currPage + 1) + "/" + this.bookPages.size(),
                (this.width - 220) / 2, 26, 0x606060);
        this.field_146159_z.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawString(Object fontRenderer, String s, int i, int i1, int i2) {
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}