package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.multiplayer.WorldClient;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.block.Block;

public class RenderGlobal {
    private final Valtherion client;
    private WorldClient world;

    public RenderGlobal(Valtherion valtherion) {
        this.client = valtherion;
    }

    public void deleteAllDisplayLists() {
    }

    /** Dessine les blocs actuellement chargés dans les chunks du monde client. */
    public void renderWorld(float partialTicks) {
        World currentWorld = this.world;
        if (currentWorld == null) return;
        RenderBlocks blockRenderer = new RenderBlocks();
        for (Chunk chunk : currentWorld.getChunks().values()) {
            if (chunk == null) continue;
            int originX = chunk.getChunkX() * Chunk.SIZE;
            int originZ = chunk.getChunkZ() * Chunk.SIZE;
            for (int x = 0; x < Chunk.SIZE; x++) {
                for (int z = 0; z < Chunk.SIZE; z++) {
                    for (int y = 0; y < Chunk.HEIGHT; y++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block != null) blockRenderer.renderBlockByRenderType(block, originX + x, y, originZ + z);
                    }
                }
            }
        }
    }

    public void setWorldAndLoadRenderers(WorldClient world) {
        this.world = world;
    }

    public String getDebugInfoRenders() {
        return this.world == null ? "No world" : this.world.getName();
    }

    public String getDebugInfoEntities() {
        return this.world == null ? "0 entities" : this.world.getDebugLoadedEntities() + " entities";
    }
}
