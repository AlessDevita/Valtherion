package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.client.multiplayer.WorldClient;

/** Particule de débris à la casse d'un bloc (nom historique conservé). */
public class EntityBreackingFX extends EntityBlockDustFX {
    public EntityBreackingFX() { super(); }

    public EntityBreackingFX(WorldClient world, double x, double y, double z,
                             double motionX, double motionY, double motionZ, BlockType type) {
        super(world, x, y, z, motionX, motionY, motionZ, colorFor(type));
    }
}
