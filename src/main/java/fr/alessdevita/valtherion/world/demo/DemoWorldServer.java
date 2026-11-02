package fr.alessdevita.valtherion.world.demo;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.WorldSettings;
import fr.alessdevita.valtherion.world.WorldProvider;
import fr.alessdevita.valtherion.world.WorldProviderSurface;
import fr.alessdevita.valtherion.world.WorldGenerator;
import fr.alessdevita.valtherion.world.SpawnerAnimals;
import fr.alessdevita.valtherion.world.WorldSavedData;
import fr.alessdevita.valtherion.world.chunk.Chunk;
import fr.alessdevita.valtherion.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Monde serveur de démonstration pour Valtherion.
 * Version simplifiée de WorldServer destinée à la démonstration du moteur.
 * Fournit un environnement de test avec des fonctionnalités réduites mais fonctionnelles.
 */
public class DemoWorldServer extends World {

    /**
     * Fournisseur de monde associé.
     */
    private final WorldProvider provider;

    /**
     * Générateur de monde associé.
     */
    private final WorldGenerator generator;

    /**
     * Spawner des entités.
     */
    private final SpawnerAnimals spawner;

    /**
     * Données sauvegardées du monde.
     */
    private final Map<String, WorldSavedData> savedData;

    /**
     * Compteur de ticks du monde.
     */
    private long tickCount;

    /**
     * Paramètres du monde.
     */
    private final WorldSettings settings;

    /**
     * Constructeur du monde serveur de démonstration.
     *
     * @param name     Nom du monde.
     * @param settings Paramètres du monde.
     */
    public DemoWorldServer(String name, WorldSettings settings) {
        super(name != null && !name.isEmpty() ? name : "DemoWorld");
        this.settings = settings != null ? settings : new WorldSettings(42L);
        this.provider = new WorldProviderSurface();
        this.generator = new WorldGenerator(this, this.settings.getSeed());
        this.spawner = new SpawnerAnimals(this);
        this.savedData = new HashMap<>();
        this.tickCount = 0;
    }

    /**
     * Initialise le monde de démonstration.
     * Configure le provider et prépare le monde pour l'utilisation.
     */
    public void init() {
        provider.init(this);
        setWorldGenerator(generator);
        super.init();
    }

    /**
     * Met à jour le monde de démonstration.
     * Appelé à chaque tick du jeu.
     *
     * @param deltaTime Temps écoulé depuis le dernier tick en secondes.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        tickCount++;

        // Mettre à jour le spawner
        spawner.update(deltaTime);

        // Mettre à jour les chunks chargés
        for (Chunk chunk : getChunks().values()) {
            if (chunk != null) {
                chunk.update();
            }
        }

        // Mettre à jour les entités
        for (Entity entity : getEntities()) {
            if (entity != null && entity.isAlive()) {
                entity.update();
            }
        }
    }

    /**
     * Charge des données sauvegardées.
     *
     * @param name Nom des données.
     * @return Données sauvegardées.
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

    /**
     * Nettoie les ressources du monde.
     * Appelé lors de la fermeture du monde.
     */
    @Override
    public void cleanup() {
        save();
        savedData.clear();
        super.cleanup();
    }

    /**
     * Ajoute une entité au monde.
     *
     * @param entity Entité à ajouter.
     */
    @Override
    public void addEntity(Entity entity) {
        if (entity != null) {
            super.addEntity(entity);
        }
    }

    /**
     * Retire une entité du monde.
     *
     * @param entity Entité à retirer.
     */
    @Override
    public void removeEntity(Entity entity) {
        if (entity != null) {
            super.removeEntity(entity);
        }
    }

    // --- Getters ---

    /**
     * Retourne le fournisseur de monde.
     *
     * @return Le fournisseur de monde.
     */
    public WorldProvider getProvider() {
        return provider;
    }

    /**
     * Retourne le générateur de monde.
     *
     * @return Le générateur de monde.
     */
    public WorldGenerator getGenerator() {
        return generator;
    }

    /**
     * Retourne le spawner des entités.
     *
     * @return Le spawner.
     */
    public SpawnerAnimals getSpawner() {
        return spawner;
    }

    /**
     * Retourne le compteur de ticks.
     *
     * @return Le nombre de ticks.
     */
    public long getTickCount() {
        return tickCount;
    }

    /**
     * Retourne les paramètres du monde.
     *
     * @return Les paramètres.
     */
    public WorldSettings getSettings() {
        return settings;
    }

    /**
     * Retourne les données sauvegardées.
     *
     * @return La carte des données sauvegardées.
     */
    public Map<String, WorldSavedData> getSavedData() {
        return savedData;
    }

    /**
     * Retourne une représentation textuelle du monde.
     *
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "DemoWorldServer{" +
                "name='" + getName() + '\'' +
                ", tickCount=" + tickCount +
                ", entities=" + getEntities().size() +
                ", chunks=" + getChunks().size() +
                '}';
    }
}
