package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.living.LivingEntity;

import org.joml.Vector3f;

import java.util.Random;

/**
 * Gère le spawn naturel des animaux et mobs dans le monde.
 * Vérifie les conditions de spawn et place les entités de manière sécurisée.
 */
public class SpawnerAnimals {

    private final World world;
    private final Random random;

    // Ticks entre chaque tentative de spawn
    private static final int SPAWN_INTERVAL = 20;
    private int spawnTimer;

    // Nombre maximum d'entités par chunk
    private static final int MAX_ENTITIES_PER_CHUNK = 64;

    // Rayon de spawn autour des joueurs
    private static final int SPAWN_RADIUS = 128;

    /**
     * Crée un spawner pour un monde.
     * @param world Monde cible
     */
    public SpawnerAnimals(World world) {
        this.world = world;
        this.random = new Random();
        this.spawnTimer = 0;
    }

    /**
     * Met à jour le spawner.
     * @param deltaTime Temps écoulé en secondes
     */
    public void update(float deltaTime) {
        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            trySpawn();
        }
    }

    /**
     * Tente de faire spawn des entités.
     */
    private void trySpawn() {
        // Tenter de spawn autour de chaque joueur
        for (int i = 0; i < world.getPlayers().size(); i++) {
            fr.alessdevita.valtherion.entity.living.Player player = world.getPlayers().get(i);
            if (player == null) continue;

            Vector3f pos = player.getPosition();
            spawnAroundPlayer(pos);
        }
    }

    /**
     * Tente de spawn des entités autour d'un joueur.
     * @param playerPosition Position du joueur
     */
    private void spawnAroundPlayer(Vector3f playerPosition) {
        int attempts = 10;
        for (int i = 0; i < attempts; i++) {
            // Position aléatoire autour du joueur
            int x = (int) playerPosition.x + random.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = (int) playerPosition.z + random.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;

            // Vérifier le nombre d'entités dans le chunk
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            if (getEntityCountInChunk(chunkX, chunkZ) >= MAX_ENTITIES_PER_CHUNK) {
                continue;
            }

            // Trouver la hauteur du terrain
            int y = world.getHeightAt(x, z);
            if (y <= 0) continue;

            // Vérifier que la position est sûre
            Vector3f spawnPos = new Vector3f(x, y + 1, z);
            if (!isSafeSpawnPosition(spawnPos)) {
                continue;
            }

            // Choisir et spawn une entité
            Entity entity = selectEntity(spawnPos);
            if (entity != null) {
                entity.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
                world.addEntity(entity);
            }
        }
    }

    /**
     * Sélectionne une entité à spawn selon la position.
     * @param position Position de spawn
     * @return Entité à spawn ou null
     */
    private Entity selectEntity(Vector3f position) {
        int y = (int) position.y;
        World.WorldState state = world.getState();

        // Nuit = mobs hostiles
        if (state == World.WorldState.NIGHT) {
            return createHostileMob();
        }

        // Jour = animaux passifs
        if (y > 60) {
            return createPassiveMob();
        }

        return null;
    }

    /**
     * Crée un mob hostile.
     * @return Entité hostile
     */
    private Entity createHostileMob() {
        // À implémenter avec les classes d'entités réelles
        return null;
    }

    /**
     * Crée un mob passif.
     * @return Entité passive
     */
    private Entity createPassiveMob() {
        // À implémenter avec les classes d'entités réelles
        return null;
    }

    /**
     * Vérifie si une position est sûre pour le spawn.
     * @param position Position à vérifier
     * @return true si sûre
     */
    private boolean isSafeSpawnPosition(Vector3f position) {
        int x = (int) position.x;
        int y = (int) position.y;
        int z = (int) position.z;

        // Bloc en dessous solide, bloc actuel et au-dessus air
        return world.isBlockSolid(x, y - 1, z)
                && !world.isBlockSolid(x, y, z)
                && !world.isBlockSolid(x, y + 1, z);
    }

    /**
     * Compte le nombre d'entités dans un chunk.
     * @param chunkX Coordonnée X du chunk
     * @param chunkZ Coordonnée Z du chunk
     * @return Nombre d'entités
     */
    private int getEntityCountInChunk(int chunkX, int chunkZ) {
        int count = 0;
        for (Entity entity : world.getEntities()) {
            if (entity == null) continue;
            Vector3f pos = entity.getPosition();
            int ex = (int) pos.x >> 4;
            int ez = (int) pos.z >> 4;
            if (ex == chunkX && ez == chunkZ) {
                count++;
            }
        }
        return count;
    }

    public World getWorld() {
        return world;
    }
}
