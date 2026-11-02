package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.living.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Monde côté serveur.
 * Gère la logique du monde, le spawn, et la sauvegarde.
 */
public class WorldServer extends World {

    private final WorldProvider provider;
    private final SpawnerAnimals spawner;
    private final Map<String, WorldSavedData> savedData;
    private long tickCount;

    public WorldServer(String name, WorldSettings settings) {
        super(name);
        this.provider = new WorldProviderSurface();
        this.provider.init(this);
        this.spawner = new SpawnerAnimals(this);
        this.savedData = new HashMap<>();
        this.tickCount = 0;
    }

    public WorldServer(String name, WorldSettings settings, WorldProvider provider) {
        super(name);
        this.provider = provider;
        this.provider.init(this);
        this.spawner = new SpawnerAnimals(this);
        this.savedData = new HashMap<>();
        this.tickCount = 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        tickCount++;

        // Mettre à jour le spawner
        spawner.update(deltaTime);
    }

    /**
     * Charge des données sauvegardées.
     * @param name Nom des données
     * @return Données sauvegardées
     */
    public WorldSavedData loadSavedData(String name) {
        return savedData.computeIfAbsent(name, WorldSavedData::new);
    }

    /**
     * Sauvegarde les données modifiées.
     */
    public void save() {
        for (WorldSavedData data : savedData.values()) {
            if (data != null && data.isDirty()) {
                data.markClean();
            }
        }
    }

    public WorldProvider getProvider() {
        return provider;
    }

    public SpawnerAnimals getSpawner() {
        return spawner;
    }

    public long getTickCount() {
        return tickCount;
    }
}
