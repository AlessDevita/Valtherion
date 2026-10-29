package fr.alessdevita.valtherion.client.gui;

import org.lwjgl.opengl.GL11;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.settings.GameSettings;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * GuiOptionSlider — slider lie a une option numerique du GameSettings
 * (FOV, sensibilite, volume...). Rendu avec la texture widgets.png,
 * bouton glissant, et drag via dragSlider dans GuiScreen parent.
 */
public class GuiOptionSlider extends GuiButton
{
    private final float sliderValue;
    private String displayString;
    private boolean dragging;
    private final GameSettings.Options options;
    private final float minValue;
    private final float maxValue;
    private int xPosition;
    private int width;
    private int height;
    private boolean visible;
    private int yPosition;
    private boolean hovered;

    public GuiOptionSlider(int buttonId, int x, int y, GameSettings.Options option)
    {
        this(buttonId, x, y, option, 0.0F, 1.0F);
    }

    public GuiOptionSlider(int buttonId, int x, int y, GameSettings.Options option,
                           float minVal, float maxVal)
    {
        super(buttonId, x, y, 150, 20, "");
        this.options = option;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = GameSettings.getOptionFloatValue(option);
        this.displayString = option.getEnumString() + ": " + this.getDisplayValue();
    }

    /** Valeur affichee (arrondie selon l'option). */
    private String getDisplayValue()
    {
        return String.valueOf(MathHelper.floor(
                this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
    }

    /**
     * Retourne la valeur normalisee (0..1) — nom MC : func_146136_c.
     */
    public float getSliderValue()
    {
        return this.sliderValue;
    }

    /**
     * Met a jour la valeur pendant le drag — nom MC : func_146134_a
     * (mouseDragged). Appele par GuiScreen quand la souris bouge
     * avec ce bouton enfonce.
     */
    public void dragSlider(Valtherion valtherion, int mouseX)
    {
        int relX = mouseX - this.xPosition;

        if (!this.dragging)
        {
            return;
        }

        relX = MathHelper.clamp_int(relX, 0, this.width);
        float value = (float) relX / (float) this.width;
        GameSettings.setOptionFloatValue(this.options,
                value * (this.maxValue - this.minValue) + this.minValue);
        this.displayString = this.options.getEnumString() + ": " + this.getDisplayValue();
    }

    public void setDragging(boolean dragging)
    {
        this.dragging = dragging;
    }

    public GameSettings.Options getOptions()
    {
        return this.options;
    }

    @Override
    public void drawButton(Valtherion mc, int mouseX, int mouseY)
    {
        if (!this.visible)
        {
            return;
        }

        // Fond du slider (degrade horizontal)
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture("textures/gui/widgets.png");
        this.drawTexturedModalRectSlider(this.xPosition, this.yPosition);

        // Bouton glissant (8px de large)
        int sliderX = this.xPosition + (int) (this.sliderValue * (float) (this.width - 8));
        this.drawTexturedModalRectKnob(sliderX, this.yPosition);

        mc.getFontRenderer().drawCenteredString(this.displayString,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2,
                this.hovered ? 0xFFFFA000 : 0xFFFFFFFF);
    }

    private void drawTexturedModalRectSlider(int x, int y)
    {
        Gui.drawRect(x, y + 4, x + this.width, y + this.height - 4, 0xFF202020);
    }

    private void drawTexturedModalRectKnob(int x, int y)
    {
        Gui.drawRect(x, y, x + 8, y + this.height, 0xFFC0C0C0);
    }
}