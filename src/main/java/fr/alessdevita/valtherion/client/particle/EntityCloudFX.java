package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

import java.util.concurrent.ThreadLocalRandom;

/** Bouffée de fumée qui s'élargit et s'élève. */
public class EntityCloudFX extends EntityFX {
    public EntityCloudFX() { this(null, 0, 0, 0, 0, 0.015, 0); }

    public EntityCloudFX(WorldClient world, double x, double y, double z,
                         double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        float shade = 0.55F + random.nextFloat() * 0.25F;
        setColor(shade, shade, shade);
        this.particleScale = 0.16F + random.nextFloat() * 0.12F;
        this.gravity = -0.004F;
        this.friction = 0.97F;
        this.maxAge = 30 + random.nextInt(25);
        this.noClip = true;
    }

    @Override
    public void renderParticle(float partialTicks) {
        float originalScale = this.particleScale;
        this.particleScale = originalScale * (1F + (float) this.particleAge / Math.max(1, this.maxAge));
        super.renderParticle(partialTicks);
        this.particleScale = originalScale;
    }
}
