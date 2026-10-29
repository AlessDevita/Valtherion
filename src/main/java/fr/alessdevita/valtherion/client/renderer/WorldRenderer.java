package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.client.renderer.culling.ICamera;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

/** Display list compilée pour un chunk de 16x256x16 blocs. */
public class WorldRenderer {
    private final int posX, posY, posZ;
    private final int displayList;
    private boolean needsUpdate = true;
    private boolean inFrustum = true;
    private boolean skipAllRenderPasses;

    public WorldRenderer() { this(0, 0, 0, 0); }
    public WorldRenderer(int x, int y, int z, int listBase) {
        this.posX = x; this.posY = y; this.posZ = z;
        this.displayList = listBase != 0 ? listBase : GLAllocation.generateDisplayLists(1);
    }

    public void updateRenderer(World world, RenderBlocks blockRenderer) {
        if (world == null || blockRenderer == null || this.displayList == 0) return;
        int chunkX = Math.floorDiv(this.posX, Chunk.SIZE);
        int chunkZ = Math.floorDiv(this.posZ, Chunk.SIZE);
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
        if (chunk != null) {
            for (int x = 0; x < Chunk.SIZE; x++) {
                for (int z = 0; z < Chunk.SIZE; z++) {
                    for (int y = 0; y < Chunk.HEIGHT; y++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block != null) blockRenderer.renderBlockByRenderType(block,
                                chunkX * Chunk.SIZE + x, y, chunkZ * Chunk.SIZE + z);
                    }
                }
            }
        }
        GL11.glEndList();
        this.needsUpdate = false;
    }

    public void updateInFrustum(ICamera camera) {
        this.inFrustum = camera == null || camera.isBoundingBoxInFrustum(
                this.posX, this.posY, this.posZ,
                this.posX + Chunk.SIZE, this.posY + Chunk.HEIGHT, this.posZ + Chunk.SIZE);
    }

    public void callDisplayList() { if (this.displayList != 0 && !this.needsUpdate) GL11.glCallList(this.displayList); }
    public void markDirty() { this.needsUpdate = true; }
    public boolean needsUpdate() { return this.needsUpdate; }
    public boolean isInFrustum() { return this.inFrustum; }
    public void setInFrustum(boolean inFrustum) { this.inFrustum = inFrustum; }
    public boolean skipAllRenderPasses() { return this.skipAllRenderPasses; }
    public void setSkipAllRenderPasses(boolean skip) { this.skipAllRenderPasses = skip; }
    public int getDisplayList() { return this.displayList; }
    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public int getPosZ() { return this.posZ; }
    public void delete() { if (this.displayList != 0) GLAllocation.deleteDisplayLists(this.displayList, 1); }
}
