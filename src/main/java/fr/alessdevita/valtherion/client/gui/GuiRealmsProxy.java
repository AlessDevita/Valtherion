package fr.alessdevita.valtherion.client.gui;

/**
 * GuiRealmsProxy — proxy de compatibilite Realms (comme dans MC 1.7/1.8).
 * Valtherion n'a pas de Realms : ce proxy delegue tout aux ecrans normaux.
 * Il existe uniquement pour garder la structure MC si vous branchez un
 * systeme de serveurs heberges plus tard.
 */
public class GuiRealmsProxy
{
    public GuiScreen toGuiScreen(GuiScreenRealmsProxy proxy)
    {
        return proxy == null ? null : proxy.getGui();
    }

    public GuiScreenRealmsProxy toRealmsScreen(GuiScreen screen)
    {
        return screen == null ? null : new GuiScreenRealmsProxy(screen);
    }
}