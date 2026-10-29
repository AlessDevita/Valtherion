package fr.alessdevita.valtherion.utils;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;

/** Result of a ray trace against a block or entity. */
public class MovingObjectPosition {
    public enum MovingObjectType { MISS, BLOCK, ENTITY }

    public final MovingObjectType typeOfHit;
    public final int blockX;
    public final int blockY;
    public final int blockZ;
    public final Object sideHit;
    public final Vector3f hitVec;
    public final Entity entityHit;

    public MovingObjectPosition(Entity entity) {
        this(entity, null);
    }

    public MovingObjectPosition(Entity entity, Vector3f hitVec) {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.entityHit = entity;
        this.hitVec = hitVec == null ? new Vector3f() : new Vector3f(hitVec);
        this.blockX = this.blockY = this.blockZ = 0;
        this.sideHit = null;
    }

    public MovingObjectPosition(int x, int y, int z, Object side, Vector3f hitVec) {
        this.typeOfHit = MovingObjectType.BLOCK;
        this.entityHit = null;
        this.hitVec = hitVec == null ? new Vector3f(x, y, z) : new Vector3f(hitVec);
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
        this.sideHit = side;
    }

    public MovingObjectPosition() {
        this.typeOfHit = MovingObjectType.MISS;
        this.entityHit = null;
        this.hitVec = new Vector3f();
        this.blockX = this.blockY = this.blockZ = 0;
        this.sideHit = null;
    }
}
