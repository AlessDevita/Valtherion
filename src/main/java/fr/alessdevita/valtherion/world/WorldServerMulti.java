package fr.alessdevita.valtherion.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Monde multi-joueurs.
 * Gère plusieurs mondes (dimensions) et la synchronisation entre joueurs.
 */
public class WorldServerMulti {

    private final List<WorldServer> worlds;
    private WorldServer overworld;

    public WorldServerMulti() {
        this.worlds = new ArrayList<>();
    }

    /**
     * Initialise le serveur multi-mondes.
     * @param settings Paramètres du monde
     */
    public void init(WorldSettings settings) {
        // Créer l'Overworld
        overworld = new WorldServer("Overworld", settings);
        overworld.init();
        worlds.add(overworld);
    }

    /**
     * Retourne l'Overworld.
     * @return Monde Overworld
     */
    public WorldServer getOverworld() {
        return overworld;
    }

    /**
     * Récupère un monde par son index.
     * @param index Index du monde
     * @return Monde ou null
     */
    public WorldServer getWorld(int index) {
        if (index >= 0 && index < worlds.size()) {
            return worlds.get(index);
        }
        return null;
    }

    /**
     * Met à jour tous les mondes.
     * @param deltaTime Temps écoulé
     */
    public void updateAll(float deltaTime) {
        for (WorldServer world : worlds) {
            if (world != null) {
                world.update(deltaTime);
            }
        }
    }

    /**
     * Sauvegarde tous les mondes.
     */
    public void saveAll() {
        for (WorldServer world : worlds) {
            if (world != null) {
                world.save();
            }
        }
    }

    /**
     * Ferme tous les mondes.
     */
    public void closeAll() {
        for (WorldServer world : worlds) {
            if (world != null) {
                world.cleanup();
            }
        }
        worlds.clear();
        overworld = null;
    }

    /**
     * Retourne le nombre de mondes.
     * @return Nombre
     */
    public int getWorldCount() {
        return worlds.size();
    }
}
