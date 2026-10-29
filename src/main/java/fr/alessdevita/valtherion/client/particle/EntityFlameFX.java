package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

import java.util.concurrent.ThreadLocalRandom;

/** Petite flamme qui monte et s'éteint progressivement. */
public class EntityFlameFX extends EntityFX {
    public EntityFlameFX() { this(null, 0, 0, 0, 0, 0.02, 0); }

    public EntityFlameFX(WorldClient world, double x, double y, double z,
                         double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        setColor(1F, 0.35F + random.nextFloat() * 0.35F, 0.04F);
        this.particleScale = 0.08F + random.nextFloat() * 0.08F;
        this.gravity = -0.025F;
        this.friction = 0.94F;
        this.maxAge = 16 + random.nextInt(18);
        this.noClip = true;
    }
}
