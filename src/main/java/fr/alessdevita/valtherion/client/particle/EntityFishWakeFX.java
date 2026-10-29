package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

/** Ondelette laissée à la surface par un poisson. */
public class EntityFishWakeFX extends EntityFX {
    public EntityFishWakeFX() { this(null, 0, 0, 0, 0, 0, 0); }

    public EntityFishWakeFX(WorldClient world, double x, double y, double z,
                            double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        setColor(0.55F, 0.8F, 0.92F);
        setAlpha(0.55F);
        this.particleScale = 0.06F;
        this.gravity = 0F;
        this.friction = 0.985F;
        this.maxAge = 18;
        this.noClip = true;
    }

    @Override
    public void renderParticle(float partialTicks) {
        float originalScale = this.particleScale;
        this.particleScale = originalScale * (1F + (float) this.particleAge / Math.max(1, this.maxAge) * 2.5F);
        super.renderParticle(partialTicks);
        this.particleScale = originalScale;
    }
}
