package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.living.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gère la création et l'accès aux différents mondes (dimensions).
 * Permet de basculer entre Overworld, Nether et End.
 */
public class WorldManager {

    private final Map<Integer, World> worlds;
    private World currentWorld;

    // IDs de dimension
    public static final int OVERWORLD_DIM = 0;
    public static final int NETHER_DIM = -1;
    public static final int END_DIM = 1;

    public WorldManager() {
        this.worlds = new HashMap<>();
    }

    /**
     * Crée et enregistre un monde.
     * @param dimensionId ID de la dimension
     * @param name Nom du monde
     * @param settings Paramètres du monde
     * @return Monde créé
     */
    public World createWorld(int dimensionId, String name, WorldSettings settings) {
        if (worlds.containsKey(dimensionId)) {
            return worlds.get(dimensionId);
        }

        World world = new World(name);
        world.setWorldGenerator(new WorldGenerator(world, settings != null ? settings.getSeed() : 0));

        // Configurer le provider selon la dimension
        switch (dimensionId) {
            case NETHER_DIM:
                world.setProvider(new WorldProviderHell());
                break;
            case END_DIM:
                world.setProvider(new WorldProviderEnd());
                break;
            default:
                world.setProvider(new WorldProviderSurface());
                break;
        }

        world.init();
        worlds.put(dimensionId, world);

        if (currentWorld == null) {
            currentWorld = world;
        }

        return world;
    }

    /**
     * Récupère un monde par son ID de dimension.
     * @param dimensionId ID de la dimension
     * @return Monde ou null
     */
    public World getWorld(int dimensionId) {
        return worlds.get(dimensionId);
    }

    /**
     * Retourne le monde actuel.
     * @return Monde actuel
     */
    public World getCurrentWorld() {
        return currentWorld;
    }

    /**
     * Change le monde actuel.
     * @param dimensionId ID de la dimension
     * @return true si le changement a réussi
     */
    public boolean switchWorld(int dimensionId) {
        World world = worlds.get(dimensionId);
        if (world != null) {
            currentWorld = world;
            return true;
        }
        return false;
    }

    /**
     * Retourne le nombre de mondes.
     * @return Nombre de mondes
     */
    public int getWorldCount() {
        return worlds.size();
    }

    /**
     * Retourne la liste de tous les mondes.
     * @return Liste des mondes
     */
    public List<World> getWorlds() {
        return new ArrayList<>(worlds.values());
    }

    /**
     * Vérifie si un monde existe pour une dimension donnée.
     * @param dimensionId ID de la dimension
     * @return true si le monde existe
     */
    public boolean containsWorld(int dimensionId) {
        return worlds.containsKey(dimensionId);
    }

    /**
     * Supprime un monde par son ID de dimension.
     * @param dimensionId ID de la dimension
     * @return true si le monde a été supprimé
     */
    public boolean removeWorld(int dimensionId) {
        World world = worlds.remove(dimensionId);
        if (world != null) {
            world.cleanup();
            if (currentWorld == world) {
                currentWorld = worlds.isEmpty() ? null : worlds.values().iterator().next();
            }
            return true;
        }
        return false;
    }

    /**
     * Ferme tous les mondes.
     */
    public void closeAll() {
        for (World world : worlds.values()) {
            if (world != null) {
                world.cleanup();
            }
        }
        worlds.clear();
        currentWorld = null;
    }
}
