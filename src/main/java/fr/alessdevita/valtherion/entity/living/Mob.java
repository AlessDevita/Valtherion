package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.entity.EntityAI;
import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Mob extends LivingEntity {

    private String mobType;

    protected EntityAI ai;

    private LivingEntity target;

    private float detectionRange;

    private int experienceReward;

    protected final List<Drop> drops;

    protected MobState currentState;

    public enum MobState {
        IDLE,
        WANDER,
        CHASING,
        ATTACKING,
        FLEEING,
        EATING,
        SLEEPING,
        INTERACTING
    }

    public static class Drop {
        private final String itemId;
        private final float probability;
        private final int minQuantity;
        private final int maxQuantity;

        public Drop(String itemId, float probability, int minQuantity, int maxQuantity) {
            this.itemId = itemId;
            this.probability = probability;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
        }

        public String getItemId() {
            return itemId;
        }

        public float getProbability() {
            return probability;
        }

        public int getMinQuantity() {
            return minQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public boolean shouldDrop() {
            return new Random().nextFloat() <= probability;
        }

        public int getRandomQuantity() {
            return minQuantity + new Random().nextInt(maxQuantity - minQuantity + 1);
        }
    }

    public Mob(EntityType type, World world, double x, double y, double z, String mobType) {
        super(type, world, x, y, z);
        this.mobType = mobType;
        this.detectionRange = 16.0f;
        this.experienceReward = 0;
        this.drops = new ArrayList<>();
        this.currentState = MobState.IDLE;

        Pathfinder pathfinder = new Pathfinder(world);
        this.ai = new EntityAI(this, world, pathfinder);
        this.ai.setDetectionRange(detectionRange);
    }

    @Override
    public void update() {
        super.update();

        if (ai != null) {
            ai.update();
        }

        update();
    }

    protected abstract void updateState();

    protected LivingEntity findNearestTarget() {
        if (getWorld() == null) {
            return null;
        }

        LivingEntity nearestTarget = null;
        float nearestDistance = Float.MAX_VALUE;

        for (LivingEntity entity : getWorld().getLivingEntities()) {
            if (entity == this) {
                continue;
            }

            if (isValidTarget(entity)) {
                float distance = (float) getDistanceTo(entity);
                if (distance < nearestDistance && distance <= detectionRange) {
                    nearestDistance = distance;
                    nearestTarget = entity;
                }
            }
        }

        return nearestTarget;
    }

    protected boolean isValidTarget(LivingEntity entity) {
        return false;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
        if (ai != null && target != null) {
            ai.setTarget(target);
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();

        dropItems();

        if (target != null && target instanceof Player) {
            Player player = (Player) target;
            player.addExperience(experienceReward);
        }
    }

    protected void dropItems() {
        for (Drop drop : drops) {
            if (drop.shouldDrop()) {
                int quantity = drop.getRandomQuantity();
                System.out.println(getName() + " lâche " + quantity + " x " + drop.getItemId());
            }
        }
    }

    public void addDrop(Drop drop) {
        drops.add(drop);
    }

    public void addDrop(String itemId, float probability, int minQuantity, int maxQuantity) {
        drops.add(new Drop(itemId, probability, minQuantity, maxQuantity));
    }

    public String getMobType() {
        return mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public EntityAI getAi() {
        return ai;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
        if (ai != null) {
            ai.setDetectionRange(detectionRange);
        }
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(int experienceReward) {
        this.experienceReward = experienceReward;
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public MobState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MobState currentState) {
        this.currentState = currentState;
    }

    @Override
    public String toString() {
        return "Mob{" +
                "type=" + mobType +
                ", name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", state=" + currentState +
                '}';
    }
}
