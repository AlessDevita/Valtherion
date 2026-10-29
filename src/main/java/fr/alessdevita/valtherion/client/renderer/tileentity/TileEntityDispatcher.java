package fr.alessdevita.valtherion.client.renderer.tileentity;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/** Sélectionne et invoque le renderer adapté à chaque tile entity. */
public class TileEntityDispatcher {
    public static final TileEntityDispatcher instance = new TileEntityDispatcher();

    private final Map<Class<?>, TileEntitySpecialRenderer> specialRendererMap = new HashMap<>();
    private TextureManager textureManager;
    private double entityX, entityY, entityZ;

    private TileEntityDispatcher() {
        // Fallback générique tant que les classes TileEntity métier ne sont pas branchées.
        register(Object.class, new TileEntityChestRenderer());
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
        for (TileEntitySpecialRenderer renderer : this.specialRendererMap.values()) {
            renderer.setDispatcher(this);
        }
    }

    public TextureManager getTextureManager() {
        return this.textureManager;
    }

    public void register(Class<?> tileClass, TileEntitySpecialRenderer renderer) {
        if (tileClass == null || renderer == null) return;
        this.specialRendererMap.put(tileClass, renderer);
        renderer.setDispatcher(this);
    }

    public TileEntitySpecialRenderer getSpecialRenderer(Object tileEntity) {
        if (tileEntity == null) return null;
        Class<?> type = tileEntity.getClass();
        while (type != null) {
            TileEntitySpecialRenderer renderer = this.specialRendererMap.get(type);
            if (renderer != null) return renderer;
            type = type.getSuperclass();
        }
        return this.specialRendererMap.get(Object.class);
    }

    public void cacheActiveRenderInfo(double x, double y, double z) {
        this.entityX = x;
        this.entityY = y;
        this.entityZ = z;
    }

    public void renderTileEntity(Object tileEntity, float partialTicks) {
        renderTileEntityAt(tileEntity, this.entityX, this.entityY, this.entityZ, partialTicks);
    }

    public void renderTileEntityAt(Object tileEntity, double x, double y, double z, float partialTicks) {
        TileEntitySpecialRenderer renderer = getSpecialRenderer(tileEntity);
        if (renderer == null) return;
        try {
            renderer.renderTileEntityAt(tileEntity, x, y, z, partialTicks);
        } catch (RuntimeException exception) {
            System.err.println("Rendu tile entity impossible : " + exception.getMessage());
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }
}
