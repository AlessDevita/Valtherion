package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.world.World;

/** Base type shared by all living game entities. */
public class EntityLivingBase extends LivingEntity {
    public EntityLivingBase() {
        this("living-entity", "Living Entity", null);
    }

    public EntityLivingBase(String id, String name, World world) {
        super(id, name, world);
    }
}
