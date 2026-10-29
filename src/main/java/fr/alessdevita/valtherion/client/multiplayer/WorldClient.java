package fr.alessdevita.valtherion.client.multiplayer;

import fr.alessdevita.valtherion.crash.CrashReport;
import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.world.World;

import java.util.ArrayList;

/** Client-side world view with the tick and entity hooks used by the client loop. */
public class WorldClient extends World {
    private int dimensionId;
    private String providerName = "Valtherion";

    public WorldClient() {
        super("Client World");
    }

    public void tick() {
        update(1.0F / 20.0F);
    }

    public void updateEntities() {
        for (Entity entity : new ArrayList<Entity>(getEntities())) {
            if (entity != null) {
                entity.update(1.0F / 20.0F);
            }
        }
    }

    public boolean spawnEntityInWorld(Entity entity) {
        if (entity == null) {
            return false;
        }
        addEntity(entity);
        return true;
    }

    public void joinEntityInSurroundings(Entity entity) {
        if (entity != null && !getEntities().contains(entity)) {
            addEntity(entity);
        }
    }

    public void setSpawnLocation() {
    }

    public void removeAllEntities() {
        for (Entity entity : new ArrayList<Entity>(getEntities())) {
            removeEntity(entity);
        }
    }

    public void removeEntity(int entityId) {
        Entity entity = getEntity(entityId);
        if (entity != null) {
            removeEntity(entity);
        }
    }

    public String getProviderName() {
        return this.providerName;
    }

    public void setProviderName(String providerName) {
        if (providerName != null && !providerName.trim().isEmpty()) {
            this.providerName = providerName.trim();
        }
    }

    public int getDebugLoadedEntities() {
        return getEntities().size();
    }

    public void addWorldInfoToCrashReport(CrashReport report) {
        if (report != null) {
            report.makeCategory("World").addCrashSection("Dimension", this.dimensionId);
            report.makeCategory("World").addCrashSection("Provider", this.providerName);
        }
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }
}
