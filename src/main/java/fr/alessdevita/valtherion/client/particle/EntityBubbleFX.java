package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

import java.util.concurrent.ThreadLocalRandom;

/** Bulle d'air qui remonte dans l'eau. */
public class EntityBubbleFX extends EntityFX {
    public EntityBubbleFX() { this(null, 0, 0, 0, 0, 0.025, 0); }

    public EntityBubbleFX(WorldClient world, double x, double y, double z,
                          double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        setColor(0.55F, 0.8F, 1F);
        setAlpha(0.72F);
        this.particleScale = 0.035F + random.nextFloat() * 0.045F;
        this.gravity = -0.035F;
        this.friction = 0.99F;
        this.maxAge = 24 + random.nextInt(28);
        this.noClip = true;
    }
}
