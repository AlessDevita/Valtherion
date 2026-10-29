package fr.alessdevita.valtherion.client.gui;

import java.io.IOException;

/**
 * GuiShareToLan — "Ouvrir au LAN" : choix du mode de jeu et de la
 * commande autorisee, puis demarrage du serveur integre LAN.
 */
public class GuiShareToLan extends GuiScreen
{
    /** Modes disponibles. */
    private static final String[] GAME_MODES = new String[] {"Survie", "Creatif", "Aventure"};
    private static final String[] ALLOWED = new String[] {"Autoriser les commandes",
            "Desactiver les commandes"};
    private final GuiScreen parentScreen;
    private final String title = "Ouvrir au LAN";
    /** Index du mode choisi. */
    private int gameModeIndex = 0;
    private int allowCommandsIndex = 0;

    public GuiShareToLan(GuiScreen parent)
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
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, 100, 150, 20,
                "Mode de jeu : " + GAME_MODES[this.gameModeIndex]));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 5, 100, 150, 20,
                ALLOWED[this.allowCommandsIndex]));
        this.buttonList.add(new GuiButton(104, this.width / 2 - 155, 140, 150, 20,
                "Mot de passe optionnel..."));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, 180, 200, 20,
                "Ouvrir au LAN"));
        this.buttonList.add(new GuiButton(201, this.width / 2 - 100, this.height / 4 + 132,
                200, 20, "Annuler"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 101)
        {
            this.gameModeIndex = (this.gameModeIndex + 1) % GAME_MODES.length;
            button.displayString = "Mode de jeu : " + GAME_MODES[this.gameModeIndex];
        }
        else if (button.id == 102)
        {
            this.allowCommandsIndex = (this.allowCommandsIndex + 1) % ALLOWED.length;
            button.displayString = ALLOWED[this.allowCommandsIndex];
        }
        else if (button.id == 104)
        {
            // TODO : ecran de mot de passe si vous hebergez en LAN protege
        }
        else if (button.id == 200)
        {
            // TODO : lancer le serveur integre en mode LAN
            // (IntegratedServer.shareToLAN(GAME_MODES[gameModeIndex],
            //     allowCommandsIndex == 0))
            this.valtherion.displayGuiScreen(null);
            this.valtherion.setIngameFocus();
        }
        else if (button.id == 201)
        {
            this.valtherion.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.title, this.width / 2, 50, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String title, int i, int i1, int i2) {

    }
}