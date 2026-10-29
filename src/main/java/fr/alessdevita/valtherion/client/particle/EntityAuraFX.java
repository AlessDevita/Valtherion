package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

import java.util.concurrent.ThreadLocalRandom;

/** Lueur magique violette qui dérive lentement. */
public class EntityAuraFX extends EntityFX {
    public EntityAuraFX() { this(null, 0, 0, 0, 0, 0.01, 0); }

    public EntityAuraFX(WorldClient world, double x, double y, double z,
                        double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        setColor(0.45F + random.nextFloat() * 0.3F, 0.2F, 1F);
        this.particleScale = 0.06F + random.nextFloat() * 0.08F;
        this.gravity = -0.006F;
        this.friction = 0.96F;
        this.maxAge = 24 + random.nextInt(30);
        this.noClip = true;
    }
}
