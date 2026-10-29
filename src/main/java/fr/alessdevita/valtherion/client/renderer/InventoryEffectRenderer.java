package fr.alessdevita.valtherion.client.renderer;

/** Calcule la zone centrée d'un panneau d'inventaire et réserve la colonne d'effets. */
public class InventoryEffectRenderer {
    protected int guiLeft, guiTop, xSize = 176, ySize = 166;
    protected int width, height;
    private boolean potionEffectsVisible;

    public InventoryEffectRenderer() { }
    public InventoryEffectRenderer(int width, int height) { setResolution(width, height); }

    public void setResolution(int width, int height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        updateGuiPosition();
    }

    public void setGuiSize(int width, int height) {
        this.xSize = Math.max(1, width);
        this.ySize = Math.max(1, height);
        updateGuiPosition();
    }

    public void setPotionEffectsVisible(boolean visible) {
        this.potionEffectsVisible = visible;
        updateGuiPosition();
    }

    protected void updateGuiPosition() {
        int sidePanel = this.potionEffectsVisible ? 124 : 0;
        this.guiLeft = Math.max(0, (this.width - this.xSize - sidePanel) / 2 + sidePanel);
        this.guiTop = Math.max(0, (this.height - this.ySize) / 2);
    }

    public int getGuiLeft() { return this.guiLeft; }
    public int getGuiTop() { return this.guiTop; }
    public int getGuiWidth() { return this.xSize; }
    public int getGuiHeight() { return this.ySize; }
    public boolean hasPotionEffects() { return this.potionEffectsVisible; }
}
