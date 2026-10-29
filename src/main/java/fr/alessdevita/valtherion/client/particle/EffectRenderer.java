package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.client.multiplayer.WorldClient;
import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Gère le cycle de vie et le rendu des particules du monde client. */
public class EffectRenderer {
    private final List<EntityFX> effects = new ArrayList<>();
    private WorldClient world;
    private final TextureManager textureManager;

    public EffectRenderer() { this(null, null); }

    public EffectRenderer(WorldClient world, TextureManager textureManager) {
        this.world = world;
        this.textureManager = textureManager;
    }

    public synchronized void clearEffects(WorldClient world) {
        this.effects.clear();
        this.world = world;
    }

    public synchronized void addEffect(EntityFX effect) {
        if (effect != null) this.effects.add(effect);
    }

    public synchronized void updateEffects() {
        for (int i = this.effects.size() - 1; i >= 0; i--) {
            EntityFX effect = this.effects.get(i);
            effect.onUpdate();
            if (effect.isDead()) this.effects.remove(i);
        }
    }

    /** Dessine les particules dans la matrice caméra actuellement active. */
    public synchronized void renderParticles(float partialTicks) {
        if (this.effects.isEmpty()) return;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        for (EntityFX effect : this.effects) effect.renderParticle(partialTicks);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }

    /** Produit un petit nuage de débris autour de la face frappée. */
    public void addBlockHitEffects(int x, int y, int z, Object side) {
        WorldClient currentWorld = this.world;
        if (currentWorld == null) return;
        Block block = currentWorld.getBlock(x, y, z);
        if (block == null || block.getType() == BlockType.AIR) return;
        int color = EntityBlockDustFX.colorFor(block.getType());
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 8; i++) {
            double px = x + 0.2 + random.nextDouble() * 0.6;
            double py = y + 0.2 + random.nextDouble() * 0.6;
            double pz = z + 0.2 + random.nextDouble() * 0.6;
            double mx = (random.nextDouble() - 0.5) * 0.12;
            double my = random.nextDouble() * 0.12 + 0.015;
            double mz = (random.nextDouble() - 0.5) * 0.12;
            addEffect(new EntityBlockDustFX(currentWorld, px, py, pz, mx, my, mz, color));
        }
    }

    public synchronized List<EntityFX> getEffects() {
        return Collections.unmodifiableList(new ArrayList<>(this.effects));
    }

    public synchronized String getStatistics() {
        return this.effects.size() + " particules";
    }

    public TextureManager getTextureManager() { return this.textureManager; }
}
