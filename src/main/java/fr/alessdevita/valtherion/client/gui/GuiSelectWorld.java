package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

/**
 * GuiSelectWorld — Ecran de selection de monde au style MMORPG :
 * fond degrade sombre, bordures ornees, panneaux decoratifs,
 * liste scrollable elegante avec effets de survol et de selection,
 * et boutons thematiques.
 */
public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    protected GuiScreen parentScreen;
    /** Liste des mondes detectes dans le dossier saves/. */
    protected List<String> worldList = Lists.newArrayList();
    /** Liste scrollable. */
    private GuiSlotWorldSelect worldSelectionList;
    /** Index selectionne. */
    private int selectedWorld = -1;
    /** Boutons d'action. */
    private GuiButton selectButton;
    private GuiButton deleteButton;
    private GuiButton createButton;
    private GuiButton refreshButton;
    private GuiButton cancelButton;

    // Couleurs thematiques MMORPG
    private static final int COLOR_GOLD = 0xFFD4A017;       // Or pour les bordures
    private static final int COLOR_GOLD_LIGHT = 0xFFFFE066; // Or clair pour les titres
    private static final int COLOR_DARK_BG = 0xCC1A1A2E;    // Fond sombre semi-transparent
    private static final int COLOR_PANEL_BG = 0xAA0F0F1A;   // Panneau encore plus sombre
    private static final int COLOR_SELECTED = 0x44D4A017;   // Selection or
    private static final int COLOR_HOVER = 0x33D4A017;      // Survol or
    private static final int COLOR_TEXT = 0xFFE8E8E8;       // Texte clair
    private static final int COLOR_TEXT_DIM = 0xFF888899;   // Texte attenu
    private static final int COLOR_ACCENT = 0xFF6A5ACD;     // Accent violet

    public GuiSelectWorld(GuiScreen parent)
    {
        this.parentScreen = parent;
    }

    @Override
    public void displayProgressMessage(String message) {
    }

    @Override
    public void setLoadingProgress(int progress) {
    }

    @Override
    public void initGui()
    {
        this.worldSelectionList = new GuiSlotWorldSelect(this);
        this.buttonList.clear();

        int centerX = this.width / 2;
        int btnW = 150;
        int btnH = 22;

        // Bouton "Jouer" — style or
        this.buttonList.add(this.selectButton = createStyledButton(1, centerX - btnW - 7,
                this.height - 80, btnW, btnH, "⚔ Jouer", COLOR_GOLD, COLOR_GOLD_LIGHT));

        // Bouton "Creer" — style accent
        this.buttonList.add(this.createButton = createStyledButton(2, centerX + 7,
                this.height - 80, btnW, btnH, "✦ Creer", COLOR_ACCENT, 0xFF9370DB));

        // Bouton "Supprimer" — style rouge
        this.buttonList.add(this.deleteButton = createStyledButton(3, centerX - btnW - 7,
                this.height - 54, btnW, btnH, "✕ Supprimer", 0xFF8B0000, 0xFFFF4444));

        // Bouton "Reactualiser" — style neutre
        this.buttonList.add(this.refreshButton = createStyledButton(4, centerX + 7,
                this.height - 54, btnW, btnH, "↻ Actualiser", 0xFF4A4A6A, 0xFF7A7A9A));

        // Bouton "Retour" — style sombre
        this.buttonList.add(this.cancelButton = createStyledButton(8, centerX - 100,
                this.height - 28, 200, btnH, "⟵ Retour", 0xFF2A2A3A, 0xFF4A4A5A));

        this.worldList.clear();
        if (this.valtherion != null && this.valtherion.getSaveFormat() != null)
            this.worldList.addAll(this.valtherion.getSaveFormat().getWorldList());
        if (this.selectedWorld >= this.worldList.size()) this.selectedWorld = -1;
        this.updateButtonStates();
    }

    /**
     * Cree un bouton avec des couleurs thematiques.
     */
    private GuiButton createStyledButton(int id, int x, int y, int w, int h, String text,
                                          int normalColor, int hoverColor)
    {
        GuiButton btn = new GuiButton(id, x, y, w, h, text);
        btn.setNormalColor(new org.joml.Vector4f(
                (float)(normalColor >> 16 & 255) / 255f,
                (float)(normalColor >> 8 & 255) / 255f,
                (float)(normalColor & 255) / 255f, 1f));
        btn.setHoverColor(new org.joml.Vector4f(
                (float)(hoverColor >> 16 & 255) / 255f,
                (float)(hoverColor >> 8 & 255) / 255f,
                (float)(hoverColor & 255) / 255f, 1f));
        btn.setClickColor(new org.joml.Vector4f(
                (float)(normalColor >> 16 & 255) / 383f,
                (float)(normalColor >> 8 & 255) / 383f,
                (float)(normalColor & 255) / 383f, 1f));
        return btn;
    }

    /** Active/desactive les boutons selon la selection. */
    private void updateButtonStates()
    {
        this.selectButton.enabled = this.selectedWorld != -1;
        this.deleteButton.enabled = this.selectedWorld != -1;
    }

    /** Appele par la liste quand un monde est clique. */
    public void selectWorld(int index)
    {
        this.selectedWorld = index;
        this.updateButtonStates();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1 && this.selectedWorld != -1)
        {
            String folder = this.worldList.get(this.selectedWorld);
            fr.alessdevita.valtherion.world.storage.WorldInfo info = this.valtherion.getSaveFormat().getWorldInfo(folder);
            this.valtherion.launchIntegratedServer(folder, info == null ? folder : info.getWorldName(),
                    new fr.alessdevita.valtherion.world.WorldSettings(info == null ? 0L : info.getSeed()));
        }
        else if (button.id == 2)
        {
            this.valtherion.displayGuiScreen(new GuiCreateWorld(this));
        }
        else if (button.id == 3 && this.selectedWorld != -1)
        {
            String worldName = this.worldList.get(this.selectedWorld);
            this.valtherion.displayGuiScreen(new GuiYesNo((GuiYesNoCallback) this,
                    "Supprimer le monde", "'" + worldName + "' sera perdu definitivement !",
                    this.selectedWorld));
        }
        else if (button.id == 4)
        {
            this.valtherion.displayGuiScreen(new GuiSelectWorld(this.parentScreen));
        }
        else if (button.id == 8)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (id >= 0 && id < this.worldList.size()) {
                String folder = this.worldList.get(id);
                this.valtherion.getSaveFormat().deleteWorldDirectory(folder);
                this.worldList.remove(id);
                this.selectedWorld = -1;
                this.updateButtonStates();
            }
        }
        this.valtherion.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // === FOND DEGRADE SOMBRE ===
        this.drawGradientRect(0, 0, this.width, this.height, 0xFF0A0A1A, 0xFF1A1A3E);

        // === PANNEAU CENTRAL PRINCIPAL ===
        int panelX = this.width / 2 - 200;
        int panelY = 50;
        int panelW = 400;
        int panelH = this.height - 130;

        // Arriere-plan du panneau
        this.drawRect(panelX, panelY, panelX + panelW, panelY + panelH, COLOR_PANEL_BG);

        // Bordure ornee du panneau
        drawOrnateBorder(panelX, panelY, panelW, panelH);

        // === TITRE DECORATIF ===
        drawTitlePanel();

        // === LISTE DES MONDES ===
        this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);

        // === COMPTEUR DE MONDES ===
        if (this.worldList.isEmpty()) {
            this.valtherion.fontRenderer.drawCenteredString(
                    "Aucun monde sauvegarde",
                    this.width / 2, panelY + panelH / 2 - 40,
                    COLOR_TEXT_DIM);
            this.valtherion.fontRenderer.drawCenteredString(
                    "Creer un nouveau monde pour commencer !",
                    this.width / 2, panelY + panelH / 2 - 20,
                    COLOR_ACCENT);
        } else {
            this.valtherion.fontRenderer.drawString(
                    "Monde" + (this.worldList.size() == 1 ? "" : "s") + " : " + this.worldList.size(),
                    panelX + 10, panelY + panelH + 5,
                    COLOR_TEXT_DIM);
        }

        // Dessiner les boutons
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Dessine le titre avec un panneau decoratif.
     */
    private void drawTitlePanel()
    {
        int titleY = 15;
        String title = "⚔ Selectionner un Monde ⚔";
        int titleWidth = this.valtherion.fontRenderer.getStringWidth(title);
        int titleX = this.width / 2 - titleWidth / 2 - 20;
        int titleH = 30;

        // Panneau de titre degrade
        this.drawGradientRect(titleX, titleY, titleX + titleWidth + 40, titleY + titleH,
                0xFF1A1A3E, 0xFF2A2A5E);

        // Bordure or du titre
        Gui.drawRect(titleX, titleY, titleX + titleWidth + 40, titleY + 1, COLOR_GOLD);
        Gui.drawRect(titleX, titleY + titleH - 1, titleX + titleWidth + 40, titleY + titleH, COLOR_GOLD);
        Gui.drawRect(titleX, titleY, titleX + 1, titleY + titleH, COLOR_GOLD);
        Gui.drawRect(titleX + titleWidth + 39, titleY, titleX + titleWidth + 40, titleY + titleH, COLOR_GOLD);

        // Ligne decorative sous le titre
        Gui.drawRect(titleX + 10, titleY + titleH, titleX + titleWidth + 30, titleY + titleH + 2, COLOR_GOLD);

        // Texte du titre
        this.valtherion.fontRenderer.drawCenteredString(title, this.width / 2, titleY + 8, COLOR_GOLD_LIGHT);
    }

    /**
     * Dessine une bordure ornee autour d'un panneau.
     */
    private void drawOrnateBorder(int x, int y, int w, int h)
    {
        // Bordure principale
        Gui.drawRect(x, y, x + w, y + 2, COLOR_GOLD);
        Gui.drawRect(x, y + h - 2, x + w, y + h, COLOR_GOLD);
        Gui.drawRect(x, y, x + 2, y + h, COLOR_GOLD);
        Gui.drawRect(x + w - 2, y, x + w, y + h, COLOR_GOLD);

        // Coins ornes
        int cornerSize = 8;
        // Coin haut-gauche
        Gui.drawRect(x, y, x + cornerSize, y + 1, COLOR_GOLD_LIGHT);
        Gui.drawRect(x, y, x + 1, y + cornerSize, COLOR_GOLD_LIGHT);
        // Coin haut-droite
        Gui.drawRect(x + w - cornerSize, y, x + w, y + 1, COLOR_GOLD_LIGHT);
        Gui.drawRect(x + w - 1, y, x + w, y + cornerSize, COLOR_GOLD_LIGHT);
        // Coin bas-gauche
        Gui.drawRect(x, y + h - 1, x + cornerSize, y + h, COLOR_GOLD_LIGHT);
        Gui.drawRect(x, y + h - cornerSize, x + 1, y + h, COLOR_GOLD_LIGHT);
        // Coin bas-droite
        Gui.drawRect(x + w - cornerSize, y + h - 1, x + w, y + h, COLOR_GOLD_LIGHT);
        Gui.drawRect(x + w - 1, y + h - cornerSize, x + w, y + h, COLOR_GOLD_LIGHT);
    }

    /**
     * La liste scrollable interne des mondes — style MMORPG.
     */
    public static class GuiSlotWorldSelect extends GuiSlot
    {
        private final GuiSelectWorld parent;
        private int hoveredSlot = -1;

        public GuiSlotWorldSelect(GuiSelectWorld parent)
        {
            super(parent.valtherion, parent.width, parent.height,
                    52, parent.height - 100, 30, parent.width - 30, 40);
            this.parent = parent;
            this.showSelectionBox = true;
        }

        @Override
        protected int getSize()
        {
            return this.parent.worldList.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
        {
            this.parent.selectWorld(slotIndex);
            if (doubleClick) this.parent.actionPerformed(this.parent.selectButton);
        }

        @Override
        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == this.parent.selectedWorld;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            // Detecter le slot survole
            this.hoveredSlot = -1;
            if (this.isMouseInList(mouseX, mouseY)) {
                int slot = (mouseY - this.top + (int) this.amountScrolled) / this.slotHeight;
                if (slot >= 0 && slot < this.getSize()) {
                    this.hoveredSlot = slot;
                }
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
        {
            String name = this.parent.worldList.get(slotIndex);
            boolean selected = (slotIndex == this.parent.selectedWorld);
            boolean hovered = (slotIndex == this.hoveredSlot);

            // Arriere-plan de l'entree
            if (selected) {
                // Fond selectionne degrade or
                drawGradientRect(x, y, x + l, y + m, 0x66D4A017, 0x44D4A017);
                // Bordure laterale or
                Gui.drawRect(x, y, x + 3, y + m, 0xFFFFFFFF);
            } else if (hovered) {
                // Fond survol
                Gui.drawRect(x, y, x + l, y + m, 0x33D4A017);
            } else {
                // Fond normal alterne
                if (slotIndex % 2 == 0) {
                    Gui.drawRect(x, y, x + l, y + m, 0x1A0F0F1A);
                } else {
                    Gui.drawRect(x, y, x + l, y + m, 0x1A1A1A2E);
                }
            }

            // Ligne separatrice
            Gui.drawRect(x, y + m - 1, x + l, y + m, 0x33D4A017);

            // Icone de monde
            String icon = selected ? "★" : (hovered ? "☆" : "·");
            int iconColor = selected ? 0xFFFFFFFF : 0xFFD4A017;
            this.valtherion.fontRenderer.drawString(icon, x + 6, y + 10, iconColor);

            // Nom du monde
            int textColor = selected ? 0xFFFFFFFF : (hovered ? 0xFFF0E0C0 : 0xFFE8E8E8);
            this.valtherion.fontRenderer.drawString(name, x + 22, y + 10, textColor);

            // Index du monde
            this.valtherion.fontRenderer.drawString(
                    String.valueOf(slotIndex + 1),
                    x + l - 20, y + 10, 0xFF666688);
        }

        /**
         * Dessine un rectangle degrade vertical.
         */
        private void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
            float f = (float) (startColor >> 24 & 255) / 255.0F;
            float f1 = (float) (startColor >> 16 & 255) / 255.0F;
            float f2 = (float) (startColor >> 8 & 255) / 255.0F;
            float f3 = (float) (startColor & 255) / 255.0F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f((float) right, (float) top);
            GL11.glVertex2f((float) left, (float) top);
            float f4 = (float) (endColor >> 24 & 255) / 255.0F;
            float f5 = (float) (endColor >> 16 & 255) / 255.0F;
            float f6 = (float) (endColor >> 8 & 255) / 255.0F;
            float f7 = (float) (endColor & 255) / 255.0F;
            GL11.glColor4f(f5, f6, f7, f4);
            GL11.glVertex2f((float) left, (float) bottom);
            GL11.glVertex2f((float) right, (float) bottom);
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }
}
