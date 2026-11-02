package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;

/**
 * Entrée de la liste des ticks différés pour les blocs.
 * Permet de planifier une mise à jour de bloc à un tick futur.
 */
public class NextTickListEntry implements Comparable<NextTickListEntry> {

    private final int x;
    private final int y;
    private final int z;
    private final long scheduledTick;
    private final int blockMetadata;

    /**
     * Crée une entrée de tick différé.
     * @param x Coordonnée X du bloc
     * @param y Coordonnée Y du bloc
     * @param z Coordonnée Z du bloc
     * @param scheduledTick Tick auquel la mise à jour doit se produire
     * @param blockMetadata Métadonnées du bloc
     */
    public NextTickListEntry(int x, int y, int z, long scheduledTick, int blockMetadata) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scheduledTick = scheduledTick;
        this.blockMetadata = blockMetadata;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getScheduledTick() {
        return scheduledTick;
    }

    public int getBlockMetadata() {
        return blockMetadata;
    }

    /**
     * Vérifie si cette entrée correspond à une position donnée.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return true si la position correspond
     */
    public boolean matches(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    @Override
    public int compareTo(NextTickListEntry other) {
        return Long.compare(this.scheduledTick, other.scheduledTick);
    }

    @Override
    public String toString() {
        return "NextTickListEntry{" + "x=" + x + ", y=" + y + ", z=" + z + ", tick=" + scheduledTick + '}';
    }
}
