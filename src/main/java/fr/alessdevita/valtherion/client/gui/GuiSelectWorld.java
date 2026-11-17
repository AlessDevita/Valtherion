package fr.alessdevita.valtherion.client.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

/**
 * GuiSelectWorld — Ecran de selection de monde au style fantasy MMORPG :
 * fond degrade violet/sombre, bordures ornees lumineuses,
 * liste scrollable avec miniatures de monde, modes de jeu,
 * dates de derniere connexion, et boutons thematiques.
 */
public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    protected GuiScreen parentScreen;
    protected List<String> worldList = Lists.newArrayList();
    private GuiSlotWorldSelect worldSelectionList;
    private int selectedWorld = -1;
    private int hoveredSlot = -1;

    // Boutons
    private GuiButton playButton;
    private GuiButton renameButton;
    private GuiButton deleteButton;
    private GuiButton createButton;
    private GuiButton copyButton;
    private GuiButton cancelButton;

    // Couleurs thematiques fantasy MMORPG
    private static final int COLOR_PURPLE_DARK = 0xFF1A0A2E;
    private static final int COLOR_PURPLE_MID = 0xFF2D1B69;
    private static final int COLOR_PURPLE_LIGHT = 0xFF5B21B6;
    private static final int COLOR_PURPLE_GLOW = 0xFF7C3AED;
    private static final int COLOR_BLUE_GLOW = 0xFF3B82F6;
    private static final int COLOR_BORDER_OUTER = 0xFF4C1D95;
    private static final int COLOR_BORDER_INNER = 0xFF7C3AED;
    private static final int COLOR_TEXT_TITLE = 0xFFE9D5FF;
    private static final int COLOR_TEXT_WHITE = 0xFFFFFFFF;
    private static final int COLOR_TEXT_DIM = 0xFFA78BFA;
    private static final int COLOR_TEXT_LIGHT = 0xFFDDD6FE;
    private static final int COLOR_PANEL_BG = 0xCC1A0A2E;
    private static final int COLOR_SLOT_NORMAL = 0x991E1B4B;
    private static final int COLOR_SLOT_HOVER = 0xBB3B0764;
    private static final int COLOR_SLOT_SELECTED = 0xDD7C3AED;
    private static final int COLOR_SCROLLBAR = 0xFF7C3AED;
    private static final int COLOR_SCROLLBAR_BG = 0x991E1B4B;

    // Couleurs des miniatures par mode de jeu
    private static final int[] SURVIE_COLORS = {0xFF22C55E, 0xFF16A34A, 0xFF15803D, 0xFF166534};
    private static final int[] AVENTURE_COLORS = {0xFFA855F7, 0xFF9333EA, 0xFF7E22CE, 0xFF6B21A8};
    private static final int[] CREATIF_COLORS = {0xFF3B82F6, 0xFF2563EB, 0xFF1D4ED8, 0xFF1E40AF};
    private static final int[] HARDCORE_COLORS = {0xFFEF4444, 0xFFDC2626, 0xFFB91C1C, 0xFF991B1B};

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
        this.buttonList.clear();
        this.worldSelectionList = new GuiSlotWorldSelect(this);

        // Charger la liste des mondes
        this.worldList.clear();
        if (this.valtherion != null && this.valtherion.getSaveFormat() != null)
            this.worldList.addAll(this.valtherion.getSaveFormat().getWorldList());

        if (this.selectedWorld >= this.worldList.size())
            this.selectedWorld = -1;

        // Boutons en bas — style fantasy MMORPG
        int btnY = this.height - 70;
        int btnW = 140;
        int btnH = 30;
        int gap = 10;
        int totalWidth = btnW * 3 + gap * 2;
        int startX = (this.width - totalWidth) / 2;

        // Ligne 1 : Jouer, Renommer, Supprimer
        this.buttonList.add(this.playButton = createFantasyButton(
                1, startX, btnY, btnW, btnH, "Jouer",
                new Vector4f(0.23f, 0.65f, 0.37f, 1f),
                new Vector4f(0.35f, 0.8f, 0.5f, 1f),
                new Vector4f(0.15f, 0.45f, 0.25f, 1f)));

        this.buttonList.add(this.renameButton = createFantasyButton(
                3, startX + btnW + gap, btnY, btnW, btnH, "Renommer",
                new Vector4f(0.49f, 0.33f, 0.85f, 1f),
                new Vector4f(0.62f, 0.45f, 0.95f, 1f),
                new Vector4f(0.35f, 0.22f, 0.6f, 1f)));

        this.buttonList.add(this.deleteButton = createFantasyButton(
                4, startX + (btnW + gap) * 2, btnY, btnW, btnH, "Supprimer",
                new Vector4f(0.6f, 0.2f, 0.2f, 1f),
                new Vector4f(0.8f, 0.3f, 0.3f, 1f),
                new Vector4f(0.4f, 0.1f, 0.1f, 1f)));

        // Ligne 2 : Creer, Copier, Retour
        int btnY2 = btnY - 40;
        int btnW2 = 160;
        int totalWidth2 = btnW2 * 3 + gap * 2;
        int startX2 = (this.width - totalWidth2) / 2;

        this.buttonList.add(this.createButton = createFantasyButton(
                2, startX2, btnY2, btnW2, btnH, "Creer un nouveau monde",
                new Vector4f(0.23f, 0.65f, 0.37f, 0.8f),
                new Vector4f(0.35f, 0.8f, 0.5f, 0.9f),
                new Vector4f(0.15f, 0.45f, 0.25f, 0.8f)));

        this.buttonList.add(this.copyButton = createFantasyButton(
                5, startX2 + btnW2 + gap, btnY2, btnW2, btnH, "Copier",
                new Vector4f(0.49f, 0.33f, 0.85f, 0.8f),
                new Vector4f(0.62f, 0.45f, 0.95f, 0.9f),
                new Vector4f(0.35f, 0.22f, 0.6f, 0.8f)));

        this.buttonList.add(this.cancelButton = createFantasyButton(
                10, startX2 + (btnW2 + gap) * 2, btnY2, btnW2, btnH, "Retour",
                new Vector4f(0.3f, 0.3f, 0.4f, 0.8f),
                new Vector4f(0.45f, 0.45f, 0.55f, 0.9f),
                new Vector4f(0.2f, 0.2f, 0.3f, 0.8f)));

        this.updateButtonStates();
    }

    private GuiButton createFantasyButton(int id, int x, int y, int w, int h, String text,
                                          Vector4f normal, Vector4f hover, Vector4f click)
    {
        GuiButton btn = new GuiButton(id, x, y, w, h, text);
        btn.setNormalColor(normal);
        btn.setHoverColor(hover);
        btn.setClickColor(click);
        return btn;
    }

    private void updateButtonStates()
    {
        boolean hasSelection = this.selectedWorld != -1;
        this.playButton.enabled = hasSelection;
        this.renameButton.enabled = hasSelection;
        this.deleteButton.enabled = hasSelection;
        this.copyButton.enabled = hasSelection;
    }

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
            fr.alessdevita.valtherion.world.storage.WorldInfo info =
                    this.valtherion.getSaveFormat().getWorldInfo(folder);
            // Mettre a jour la derniere connexion
            if (info != null) info.setLastPlayed(System.currentTimeMillis());
            this.valtherion.launchIntegratedServer(
                    folder,
                    info == null ? folder : info.getWorldName(),
                    new fr.alessdevita.valtherion.world.WorldSettings(
                            info == null ? 0L : info.getSeed()));
        }
        else if (button.id == 2)
        {
            this.valtherion.displayGuiScreen(new GuiCreateWorld(this));
        }
        else if (button.id == 3 && this.selectedWorld != -1)
        {
            String folder = this.worldList.get(this.selectedWorld);
            this.valtherion.displayGuiScreen(new GuiRenameWorld(this, folder));
        }
        else if (button.id == 4 && this.selectedWorld != -1)
        {
            String worldName = this.worldList.get(this.selectedWorld);
            this.valtherion.displayGuiScreen(new GuiYesNo(
                    (GuiYesNoCallback) this,
                    "Voulez-vous vraiment supprimer ce monde ?",
                    "'" + worldName + "' sera perdu definitivement.",
                    this.selectedWorld));
        }
        else if (button.id == 5 && this.selectedWorld != -1)
        {
            // Copier le monde
            String folder = this.worldList.get(this.selectedWorld);
            this.valtherion.displayGuiScreen(new GuiRenameWorld(this, folder + " (copie)"));
        }
        else if (button.id == 10)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (id >= 0 && id < this.worldList.size())
            {
                String folder = this.worldList.get(id);
                this.valtherion.getSaveFormat().deleteWorldDirectory(folder);
                this.worldList.remove(id);
                if (this.selectedWorld >= this.worldList.size())
                    this.selectedWorld = -1;
                this.updateButtonStates();
            }
        }
        this.valtherion.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // === FOND DEGRADE VIOLET SOMBRE (style fantasy) ===
        this.drawGradientRect(0, 0, this.width, this.height,
                COLOR_PURPLE_DARK, 0xFF0F0520);

        // === TITRE DECORATIF EN HAUT ===
        drawFantasyTitle();

        // === PANNEAU CENTRAL AVEC BORDURE ORNEE ===
        int panelX = this.width / 2 - 260;
        int panelY = 50;
        int panelW = 520;
        int panelH = this.height - 160;

        // Arriere-plan du panneau
        this.drawGradientRect(panelX, panelY, panelX + panelW, panelY + panelH,
                COLOR_PANEL_BG, 0xAA0F0520);

        // Bordure ornee du panneau
        drawOrnateBorder(panelX, panelY, panelW, panelH);

        // === LISTE DES MONDES ===
        this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);

        // === MESSAGE SI AUCUN MONDE ===
        if (this.worldList.isEmpty())
        {
            this.valtherion.fontRenderer.drawCenteredString(
                    "Aucun monde sauvegarde.",
                    this.width / 2, this.height / 2 - 20, COLOR_TEXT_DIM);
            this.valtherion.fontRenderer.drawCenteredString(
                    "Cliquez sur \"Creer un nouveau monde\" pour commencer.",
                    this.width / 2, this.height / 2, COLOR_PURPLE_GLOW);
        }

        // === BOUTONS ===
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Dessine le titre decoratif en haut de l'ecran.
     */
    private void drawFantasyTitle()
    {
        String title = "Selectionner un monde";
        int titleWidth = this.valtherion.fontRenderer.getStringWidth(title);
        int titleX = this.width / 2 - titleWidth / 2 - 30;
        int titleY = 8;
        int titleH = 32;

        // Panneau de titre degrade
        this.drawGradientRect(titleX, titleY, titleX + titleWidth + 60, titleY + titleH,
                0xFF2D1B69, 0xFF1A0A2E);

        // Bordure lumineuse du titre
        Gui.drawRect(titleX, titleY, titleX + titleWidth + 60, titleY + 2, COLOR_PURPLE_GLOW);
        Gui.drawRect(titleX, titleY + titleH - 2, titleX + titleWidth + 60, titleY + titleH, COLOR_PURPLE_GLOW);
        Gui.drawRect(titleX, titleY, titleX + 2, titleY + titleH, COLOR_PURPLE_GLOW);
        Gui.drawRect(titleX + titleWidth + 58, titleY, titleX + titleWidth + 60, titleY + titleH, COLOR_PURPLE_GLOW);

        // Coins lumineux
        int corner = 6;
        Gui.drawRect(titleX, titleY, titleX + corner, titleY + 1, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX, titleY, titleX + 1, titleY + corner, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX + titleWidth + 60 - corner, titleY, titleX + titleWidth + 60, titleY + 1, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX + titleWidth + 59, titleY, titleX + titleWidth + 60, titleY + corner, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX, titleY + titleH - 1, titleX + corner, titleY + titleH, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX, titleY + titleH - corner, titleX + 1, titleY + titleH, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX + titleWidth + 60 - corner, titleY + titleH - 1, titleX + titleWidth + 60, titleY + titleH, COLOR_BLUE_GLOW);
        Gui.drawRect(titleX + titleWidth + 59, titleY + titleH - corner, titleX + titleWidth + 60, titleY + titleH, COLOR_BLUE_GLOW);

        // Ligne decorative sous le titre
        Gui.drawRect(titleX + 15, titleY + titleH, titleX + titleWidth + 45, titleY + titleH + 2, COLOR_PURPLE_GLOW);

        // Texte du titre avec ombre
        this.valtherion.fontRenderer.drawCenteredString(
                title, this.width / 2, titleY + 2, 0x000000);
        this.valtherion.fontRenderer.drawCenteredString(
                title, this.width / 2, titleY + 1, COLOR_TEXT_TITLE);
    }

    /**
     * Dessine une bordure ornee autour du panneau principal.
     */
    private void drawOrnateBorder(int x, int y, int w, int h)
    {
        // Bordure exterieure
        Gui.drawRect(x, y, x + w, y + 3, COLOR_BORDER_OUTER);
        Gui.drawRect(x, y + h - 3, x + w, y + h, COLOR_BORDER_OUTER);
        Gui.drawRect(x, y, x + 3, y + h, COLOR_BORDER_OUTER);
        Gui.drawRect(x + w - 3, y, x + w, y + h, COLOR_BORDER_OUTER);

        // Bordure interieure lumineuse
        Gui.drawRect(x + 3, y + 3, x + w - 3, y + 4, COLOR_BORDER_INNER);
        Gui.drawRect(x + 3, y + h - 4, x + w - 3, y + h - 3, COLOR_BORDER_INNER);
        Gui.drawRect(x + 3, y + 3, x + 4, y + h - 3, COLOR_BORDER_INNER);
        Gui.drawRect(x + w - 4, y + 3, x + w - 3, y + h - 3, COLOR_BORDER_INNER);

        // Coins ornes
        int cornerSize = 12;
        // Coin haut-gauche
        Gui.drawRect(x, y, x + cornerSize, y + 1, COLOR_BLUE_GLOW);
        Gui.drawRect(x, y, x + 1, y + cornerSize, COLOR_BLUE_GLOW);
        Gui.drawRect(x + 1, y + 1, x + cornerSize - 1, y + 2, COLOR_PURPLE_GLOW);
        Gui.drawRect(x + 1, y + 1, x + 2, y + cornerSize - 1, COLOR_PURPLE_GLOW);
        // Coin haut-droite
        Gui.drawRect(x + w - cornerSize, y, x + w, y + 1, COLOR_BLUE_GLOW);
        Gui.drawRect(x + w - 1, y, x + w, y + cornerSize, COLOR_BLUE_GLOW);
        Gui.drawRect(x + w - cornerSize + 1, y + 1, x + w - 1, y + 2, COLOR_PURPLE_GLOW);
        Gui.drawRect(x + w - 2, y + 1, x + w - 1, y + cornerSize - 1, COLOR_PURPLE_GLOW);
        // Coin bas-gauche
        Gui.drawRect(x, y + h - 1, x + cornerSize, y + h, COLOR_BLUE_GLOW);
        Gui.drawRect(x, y + h - cornerSize, x + 1, y + h, COLOR_BLUE_GLOW);
        Gui.drawRect(x + 1, y + h - 2, x + cornerSize - 1, y + h - 1, COLOR_PURPLE_GLOW);
        Gui.drawRect(x + 1, y + h - cornerSize + 1, x + 2, y + h - 1, COLOR_PURPLE_GLOW);
        // Coin bas-droite
        Gui.drawRect(x + w - cornerSize, y + h - 1, x + w, y + h, COLOR_BLUE_GLOW);
        Gui.drawRect(x + w - 1, y + h - cornerSize, x + w, y + h, COLOR_BLUE_GLOW);
        Gui.drawRect(x + w - cornerSize + 1, y + h - 2, x + w - 1, y + h - 1, COLOR_PURPLE_GLOW);
        Gui.drawRect(x + w - 2, y + h - cornerSize + 1, x + w - 1, y + h - 1, COLOR_PURPLE_GLOW);
    }

    /**
     * La liste scrollable interne des mondes — style fantasy MMORPG.
     */
    public static class GuiSlotWorldSelect extends GuiSlot
    {
        private final GuiSelectWorld parent;
        private int hoveredSlot = -1;

        public GuiSlotWorldSelect(GuiSelectWorld parent)
        {
            super(parent.valtherion, parent.width, parent.height,
                    60, parent.height - 150, 15, parent.width / 2 - 250, 70);
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
        }

        @Override
        protected boolean isSelected(int slotIndex)
        {
            return slotIndex == this.parent.selectedWorld;
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            this.hoveredSlot = -1;
            if (this.isMouseInList(mouseX, mouseY))
            {
                int slot = (mouseY - this.top + (int) this.amountScrolled) / this.slotHeight;
                if (slot >= 0 && slot < this.getSize())
                {
                    this.hoveredSlot = slot;
                }
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
        {
            String folder = this.parent.worldList.get(slotIndex);
            fr.alessdevita.valtherion.world.storage.WorldInfo info =
                    this.parent.valtherion.getSaveFormat().getWorldInfo(folder);

            boolean selected = (slotIndex == this.parent.selectedWorld);
            boolean hovered = (slotIndex == this.parent.hoveredSlot);

            // === ARRIERE-PLAN DE L'ENTREE ===
            if (selected)
            {
                drawGradientRect(x + 2, y + 2, x + l - 2, y + m - 2,
                        0xFF4C1D95, 0xFF3B0764);
                // Bordure lumineuse pour la selection
                Gui.drawRect(x + 2, y + 2, x + l - 2, y + 3, COLOR_PURPLE_GLOW);
                Gui.drawRect(x + 2, y + m - 3, x + l - 2, y + m - 2, COLOR_PURPLE_GLOW);
                Gui.drawRect(x + 2, y + 2, x + 3, y + m - 2, COLOR_PURPLE_GLOW);
                Gui.drawRect(x + l - 3, y + 2, x + l - 2, y + m - 2, COLOR_PURPLE_GLOW);
            }
            else if (hovered)
            {
                drawGradientRect(x + 2, y + 2, x + l - 2, y + m - 2,
                        0x993B0764, 0x77312E81);
            }
            else
            {
                // Fond normal alterne
                if (slotIndex % 2 == 0)
                {
                    Gui.drawRect(x + 2, y + 2, x + l - 2, y + m - 2, COLOR_SLOT_NORMAL);
                }
                else
                {
                    Gui.drawRect(x + 2, y + 2, x + l - 2, y + m - 2, 0x771E1B4B);
                }
            }

            // Separateur entre entrees
            Gui.drawRect(x + 5, y + m - 1, x + l - 5, y + m, 0x444C1D95);

            // === MINIATURE DU MONDE (cote gauche) ===
            int thumbX = x + 8;
            int thumbY = y + 8;
            int thumbW = 100;
            int thumbH = 20;
            drawWorldThumbnail(thumbX, thumbY, thumbW, thumbH, info);

            // === NOM DU MONDE ===
            String worldName = info != null ? info.getWorldName() : folder;
            int nameColor = selected ? COLOR_TEXT_WHITE : COLOR_TEXT_LIGHT;
            this.parent.valtherion.fontRenderer.drawString(
                    worldName, x + 120, y + 8, nameColor);

            // === MODE DE JEU ===
            String gameMode = info != null ? info.getGameMode() : "Survie";
            int modeIconColor = getModeColor(gameMode);
            this.parent.valtherion.fontRenderer.drawString(
                    getModeIcon(gameMode) + " " + gameMode,
                    x + 120, y + 20, modeIconColor);

            // === DERNIERE CONNEXION ===
            if (info != null && info.getLastPlayed() > 0)
            {
                String lastPlayed = formatLastPlayed(info.getLastPlayed());
                this.parent.valtherion.fontRenderer.drawString(
                        "Derniere connexion : " + lastPlayed,
                        x + 120, y + 32, COLOR_TEXT_DIM);
            }

            // === NUMERO DU MONDE (cote droit) ===
            this.parent.valtherion.fontRenderer.drawString(
                    String.valueOf(slotIndex + 1),
                    x + l - 22, y + (m - 8) / 2, 0x664C1D95);
        }

        /**
         * Dessine une miniature colorisee pour le monde.
         */
        private void drawWorldThumbnail(int x, int y, int w, int h,
                                        fr.alessdevita.valtherion.world.storage.WorldInfo info)
        {
            String gameMode = info != null ? info.getGameMode() : "Survie";
            int[] colors = getModeColors(gameMode);

            // Fond degrade de la miniature
            drawGradientRect(x, y, x + w, y + h, colors[0], colors[1]);

            // Bordure de la miniature
            Gui.drawRect(x, y, x + w, y + 1, colors[2]);
            Gui.drawRect(x, y + h - 1, x + w, y + h, colors[3]);
            Gui.drawRect(x, y, x + 1, y + h, colors[2]);
            Gui.drawRect(x + w - 1, y, x + w, y + h, colors[3]);

            // Elements decoratifs dans la miniature
            // Petit paysage stylise
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Montagnes
            GL11.glColor4f(0.3f, 0.3f, 0.5f, 0.5f);
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f(x + 10, y + h);
            GL11.glVertex2f(x + 30, y + h - 12);
            GL11.glVertex2f(x + 50, y + h);
            GL11.glEnd();

            GL11.glColor4f(0.25f, 0.25f, 0.45f, 0.5f);
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glVertex2f(x + 35, y + h);
            GL11.glVertex2f(x + 60, y + h - 16);
            GL11.glVertex2f(x + 85, y + h);
            GL11.glEnd();

            // Sol
            GL11.glColor4f(0.2f, 0.4f, 0.2f, 0.6f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x, y + h - 4);
            GL11.glVertex2f(x + w, y + h - 4);
            GL11.glVertex2f(x + w, y + h);
            GL11.glVertex2f(x, y + h);
            GL11.glEnd();

            // Etoiles
            GL11.glColor4f(1f, 1f, 0.8f, 0.7f);
            GL11.glBegin(GL11.GL_POINTS);
            GL11.glVertex2f(x + 15, y + 4);
            GL11.glVertex2f(x + 45, y + 6);
            GL11.glVertex2f(x + 75, y + 3);
            GL11.glVertex2f(x + 90, y + 8);
            GL11.glVertex2f(x + 55, y + 2);
            GL11.glEnd();

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        private int[] getModeColors(String mode)
        {
            switch (mode.toLowerCase())
            {
                case "survie": return SURVIE_COLORS;
                case "aventure": return AVENTURE_COLORS;
                case "creatif": return CREATIF_COLORS;
                case "hardcore": return HARDCORE_COLORS;
                default: return SURVIE_COLORS;
            }
        }

        private int getModeColor(String mode)
        {
            switch (mode.toLowerCase())
            {
                case "survie": return 0xFF22C55E;
                case "aventure": return 0xFFA855F7;
                case "creatif": return 0xFF3B82F6;
                case "hardcore": return 0xFFEF4444;
                default: return 0xFF22C55E;
            }
        }

        private String getModeIcon(String mode)
        {
            switch (mode.toLowerCase())
            {
                case "survie": return "Sword";
                case "aventure": return "Shield";
                case "creatif": return "Star";
                case "hardcore": return "Skull";
                default: return "Sword";
            }
        }

        private String formatLastPlayed(long timestamp)
        {
            if (timestamp <= 0) return "Jamais";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy a HH:mm");
            return sdf.format(new Date(timestamp));
        }

        private void drawGradientRect(int left, int top, int right, int bottom,
                                      int startColor, int endColor)
        {
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
