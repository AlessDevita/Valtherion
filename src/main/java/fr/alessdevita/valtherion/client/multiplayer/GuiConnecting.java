package fr.alessdevita.valtherion.client.multiplayer;

import fr.alessdevita.valtherion.client.gui.GuiScreen;
import fr.alessdevita.valtherion.client.gui.GuiButton;

/** Screen shown while a server connection is being established. */
public class GuiConnecting extends GuiScreen {
    private String status = "Connexion au serveur...";

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 24,
                200, 20, "Annuler"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button != null && button.id == 0) {
            this.valtherion.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (this.valtherion.fontRenderer != null) {
            float textWidth = this.valtherion.fontRenderer.getTextWidth(this.status);
            this.valtherion.fontRenderer.renderText(this.status,
                    (this.width - textWidth) / 2.0F, this.height / 2.0F - 12.0F);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setStatus(String status) {
        if (status != null && !status.trim().isEmpty()) {
            this.status = status.trim();
        }
    }
}
