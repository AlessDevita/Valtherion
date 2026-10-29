package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.world.World;

import java.util.UUID;

/** Lightweight world player state used by world generation and client HUDs. */
public class Player {
    private final World world;
    private final Inventory inventory = new Inventory(36);
    private final UUID uuid = UUID.randomUUID();
    private final double x, y, z;
    private String name;
    private float health = 20.0F, maxHealth = 20.0F;
    private float mana = 100.0F, maxMana = 100.0F;
    private int xp;
    private int maxXp = 100;

    public Player(World world, double x, double y, double z, String name) {
        this.world = world;
        this.x = x; this.y = y; this.z = z;
        this.name = name == null || name.isBlank() ? "Player" : name;
    }
    public World getWorld() { return world; }
    public Inventory getInventory() { return inventory; }
    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public void setName(String name) { if (name != null && !name.isBlank()) this.name = name.trim(); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getHealth() { return health; }
    public void setHealth(float value) { health = Math.max(0.0F, Math.min(maxHealth, value)); }
    public float getMaxHealth() { return maxHealth; }
    public void setMaxHealth(float value) { maxHealth = Math.max(1.0F, value); health = Math.min(health, maxHealth); }
    public float getMana() { return mana; }
    public void setMana(float value) { mana = Math.max(0.0F, Math.min(maxMana, value)); }
    public float getMaxMana() { return maxMana; }
    public void setMaxMana(float value) { maxMana = Math.max(0.0F, value); mana = Math.min(mana, maxMana); }
    public Object getXP() { return xp; }
    public void addXP(int amount) { if (amount > 0) xp = Math.addExact(xp, amount); }
    public float getMaxXP() { return maxXp; }
    public void setMaxXP(int value) { maxXp = Math.max(1, value); }
}
