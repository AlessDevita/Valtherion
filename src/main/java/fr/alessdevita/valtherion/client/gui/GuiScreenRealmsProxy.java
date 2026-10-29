package fr.alessdevita.valtherion.client.gui;

/**
 * GuiScreenRealmsProxy — wrapper Realms autour d'un GuiScreen normal.
 * Toutes les methodes deleguent au GuiScreen emballe.
 */
public class GuiScreenRealmsProxy extends GuiScreen
{
    private final GuiScreen guiScreen;

    public GuiScreenRealmsProxy(GuiScreen screen)
    {
        this.guiScreen = screen;
    }

    public GuiScreen getGui()
    {
        return this.guiScreen;
    }

    @Override
    public void initGui()
    {
        this.guiScreen.setWorldAndResolution(this.valtherion, this.width, this.height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.guiScreen.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleInput(int keyCode)
    {
        this.guiScreen.handleInput(keyCode);
    }

    @Override
    public void handleMouseInput(int button, int action)
    {
        this.guiScreen.handleMouseInput(button, action);
    }

    @Override
    public void onGuiClosed()
    {
        this.guiScreen.onGuiClosed();
    }

    @Override
    public void updateScreen()
    {
        this.guiScreen.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return this.guiScreen.doesGuiPauseGame();
    }
}