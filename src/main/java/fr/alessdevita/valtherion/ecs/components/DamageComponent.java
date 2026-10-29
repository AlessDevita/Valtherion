package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

public class DamageComponent implements Component {
    private Entity entity;
    private boolean enabled = true;
    private float damage = 1.0f;

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
