package fr.alessdevita.valtherion.client.gui.inventory;

import java.util.Collections;
import java.util.List;

public class TileEntityBeacon {
    private int primaryEffect;
    private int secondaryEffect;
    private int levels;

    public List getEffects() {
        return Collections.emptyList();
    }

    public void setPrimaryEffect(int id) {
        this.primaryEffect = id;
    }

    public void setSecondaryEffect(int id) {
        this.secondaryEffect = id;
    }

    public int getPrimaryEffect() {
        return this.primaryEffect;
    }

    public int getSecondaryEffect() {
        return this.secondaryEffect;
    }

    public int getLevels() {
        return this.levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public void openInventory(Object player) {
    }

    public void markDirty() {
    }

    public boolean isUseableByPlayer(Object player) {
        return true;
    }
}
