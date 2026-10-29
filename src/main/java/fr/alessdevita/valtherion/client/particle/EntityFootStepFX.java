package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

/** Trace de pas discrète qui se dissipe près du sol. */
public class EntityFootStepFX extends EntityFX {
    public EntityFootStepFX() { this(null, 0, 0, 0); }

    public EntityFootStepFX(WorldClient world, double x, double y, double z) {
        super(world, x, y + 0.015, z, 0, 0, 0);
        setColor(0.45F, 0.43F, 0.4F);
        setAlpha(0.35F);
        this.particleScale = 0.18F;
        this.gravity = 0F;
        this.friction = 1F;
        this.maxAge = 32;
        this.noClip = true;
    }
}
