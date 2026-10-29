package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * GuiOptionButton — bouton lie a une option du GameSettings :
 * le libelle se met a jour automatiquement avec la valeur de l'option.
 */
public class GuiOptionButton extends GuiButton {
    private final GameSettings.Options enumOptions;
    public int xPosition;
    public int yPosition;
    private String displayString;

    public GuiOptionButton(int buttonId, int x, int y, GameSettings.Options option)
    {
        this(buttonId, x, y, 150, 20, option, option == null ? "" : option.getEnumString());
    }

    public GuiOptionButton(int buttonId, int x, int y, GameSettings.Options option, String text)
    {
        super(buttonId, x, y, 150, 20, text);
        this.enumOptions = option;
        this.displayString = text;
    }

    public GuiOptionButton(int buttonId, int x, int y, int width, int height,
                           GameSettings.Options option, String text)
    {
        super(buttonId, x, y, width, height, text);
        this.enumOptions = option;
        this.displayString = text;
    }

    /** Force le refresh du libelle (nom MC : func_146119_a / updateText). */
    public void updateDisplayString()
    {
        this.displayString = this.enumOptions == null
                ? ""
                : String.valueOf(GameSettings.getKeyBinding(this.enumOptions));
        setText(this.displayString);
    }

    public GameSettings.Options returnEnumOptions()
    {
        return this.enumOptions;
    }

    @Override
    public void drawButton(Valtherion mc, int mouseX, int mouseY)
    {
        if (this.enumOptions != null)
        {
            this.displayString = this.enumOptions.getEnumString() + ": "
                    + String.valueOf(GameSettings.getKeyBinding(this.enumOptions));
            setText(this.displayString);
        }

        super.drawButton(mc, mouseX, mouseY);
    }
}