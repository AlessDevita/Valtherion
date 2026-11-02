package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.entity.Entity;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une explosion dans le monde.
 * Calcule les dégâts et la destruction des blocs autour du point d'explosion.
 */
public class Explosion {

    private final World world;
    private final Vector3f position;
    private final float strength;
    private final boolean causeFire;
    private final boolean destroyBlocks;
    private final List<Entity> affectedEntities;
    private final List<Vector3f> destroyedBlocks;

    /**
     * Crée une explosion.
     * @param world Monde où se produit l'explosion
     * @param position Position de l'explosion
     * @param strength Force de l'explosion (rayon de destruction)
     * @param causeFire true si l'explosion peut causer des incendies
     * @param destroyBlocks true si les blocs sont détruits
     */
    public Explosion(World world, Vector3f position, float strength, boolean causeFire, boolean destroyBlocks) {
        this.world = world;
        this.position = position;
        this.strength = strength;
        this.causeFire = causeFire;
        this.destroyBlocks = destroyBlocks;
        this.affectedEntities = new ArrayList<>();
        this.destroyedBlocks = new ArrayList<>();
    }

    /**
     * Exécute l'explosion.
     */
    public void explode() {
        // Détruire les blocs dans le rayon
        if (destroyBlocks) {
            destroyBlocks();
        }

        // Appliquer les dégâts aux entités
        damageEntities();

        // Causer des incendies si activé
        if (causeFire) {
            causeFire();
        }
    }

    /**
     * Détruit les blocs dans le rayon de l'explosion.
     */
    private void destroyBlocks() {
        int radius = (int) Math.ceil(strength);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    float distance = (float) Math.sqrt(x * x + y * y + z * z);
                    if (distance <= strength) {
                        int blockX = (int) position.x + x;
                        int blockY = (int) position.y + y;
                        int blockZ = (int) position.z + z;

                        Block block = world.getBlock(blockX, blockY, blockZ);
                        if (block != null && block.getType() != BlockType.AIR) {
                            // Réduire la résistance selon la distance
                            float resistance = 1.0f - (distance / strength);
                            if (resistance > 0.2f) {
                                world.setBlock(blockX, blockY, blockZ, BlockType.AIR);
                                destroyedBlocks.add(new Vector3f(blockX, blockY, blockZ));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Applique les dégâts aux entités dans le rayon.
     */
    private void damageEntities() {
        for (Entity entity : world.getEntities()) {
            if (entity == null) continue;

            Vector3f entityPos = entity.getPosition();
            float distance = entityPos.distance(position);

            if (distance <= strength) {
                affectedEntities.add(entity);

                // Appliquer une impulsion
                Vector3f knockback = new Vector3f(entityPos).sub(position).normalize().mul(strength * 2.0f);
                entity.setVelocity(
                    entity.getVelocityX() + knockback.x,
                    entity.getVelocityY() + knockback.y,
                    entity.getVelocityZ() + knockback.z
                );
            }
        }
    }

    /**
     * Cause des incendies autour de l'explosion.
     */
    private void causeFire() {
        int radius = (int) Math.ceil(strength * 0.5f);
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int blockX = (int) position.x + x;
                int blockY = (int) position.y;
                int blockZ = (int) position.z + z;

                Block block = world.getBlock(blockX, blockY, blockZ);
                if (block != null && block.getType() == BlockType.AIR) {
                    // Chance de créer du feu
                    if (Math.random() < 0.3f) {
                        world.setBlock(blockX, blockY, blockZ, BlockType.FIRE);
                    }
                }
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getStrength() {
        return strength;
    }

    public List<Entity> getAffectedEntities() {
        return affectedEntities;
    }

    public List<Vector3f> getDestroyedBlocks() {
        return destroyedBlocks;
    }
}
