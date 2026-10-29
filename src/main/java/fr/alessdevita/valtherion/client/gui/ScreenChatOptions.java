package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * ScreenChatOptions — "Options de chat" : visibilite du chat, couleurs,
 * liens web, opacity, scale et hauteur du chat.
 */
public class ScreenChatOptions extends GuiScreen
{
    private final GuiScreen parentScreen;
    private final String title = "Options de chat";

    private static final GameSettings.Options[] chatOptions = new GameSettings.Options[]
            {
                    GameSettings.Options.CHAT_VISIBILITY,
                    GameSettings.Options.CHAT_COLOR,
                    GameSettings.Options.CHAT_LINKS,
                    GameSettings.Options.CHAT_OPACITY,
                    GameSettings.Options.CHAT_SCALE,
                    GameSettings.Options.CHAT_HEIGHT_FOCUSED,
                    GameSettings.Options.CHAT_HEIGHT_UNFOCUSED
            };

    public ScreenChatOptions(GuiScreen parent)
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

        for (int i = 0; i < chatOptions.length; ++i)
        {
            int x = this.width / 2 - 155 + i % 2 * 160;
            int y = this.height / 6 + 24 * (i >> 1);
            this.buttonList.add(new GuiOptionButton(100 + i, x, y, chatOptions[i], ""));
        }

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
                this.height / 6 + 24 * ((chatOptions.length + 1) / 2) + 12,
                200, 20, "Terminer"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button instanceof GuiOptionButton)
            {
                GameSettings.Options option = ((GuiOptionButton) button).returnEnumOptions();
                this.valtherion.gameSettings.notifyAll();
                button.displayString = GameSettings.getKeyBinding(option);
            }
            else if (button.id == 200)
            {
                this.valtherion.gameSettings.notifySettingsChanged();
                this.valtherion.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.valtherion.fontRenderer, this.title,
                this.width / 2, 15, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCenteredString(Object fontRenderer, String title, int i, int i1, int i2) {

    }
}