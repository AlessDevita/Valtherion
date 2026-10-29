package fr.alessdevita.valtherion.client.renderer.tileentity;

/** Accès singleton au renderer de coffre pour l'inventaire / GUI. */
public final class TileEntityRendererChestHelper {
    public static final TileEntityRendererChestHelper instance = new TileEntityRendererChestHelper();

    private final TileEntityChestRenderer chestRenderer = new TileEntityChestRenderer();

    private TileEntityRendererChestHelper() { }

    public void renderChest(double x, double y, double z, float partialTicks) {
        this.chestRenderer.renderTileEntityAt(null, x, y, z, partialTicks);
    }

    public TileEntityChestRenderer getChestRenderer() {
        return this.chestRenderer;
    }
}
