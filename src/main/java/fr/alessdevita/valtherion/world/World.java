package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.physics.PhysicsEngine;
import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import org.joml.Vector3f;

import java.util.*;

/**
 * Classe pour le monde du jeu dans Valtherion.
 * Gère les entités, les blocs, les chunks, et les systèmes de physique.
 */
public class World {

    // Nom du monde
    private String name;

    // Liste des entités dans le monde
    private final List<Entity> entities;

    // Liste des joueurs dans le monde
    private final List<Player> players;

    // Moteur physique du monde
    private PhysicsEngine physicsEngine;

    // Grille des chunks (coordonnées XZ -> Chunk)
    private final Map<String, Chunk> chunks;

    // Taille d'un chunk (en blocs)
    private int chunkSize;

    // Gravité du monde
    private Vector3f gravity;

    // Temps du monde (en secondes)
    private float worldTime;

    // État du monde (jour/nuit, météo, etc.)
    private WorldState state;

    public int getHeightAt(int x, int z) {
        return x;
    }

    // Énumération des états du monde
    public enum WorldState {
        DAY,        // Jour
        NIGHT,      // Nuit
        DAWN,       // Aube
        DUSK,       // Crépuscule
        RAINING,    // Pluie
        SNOWING,    // Neige
        STORMY      // Tempête
    }

    // Générateur de monde
    private WorldGenerator worldGenerator;

    // Liste des observateurs du monde
    private final List<WorldListener> listeners;

    /**
     * Constructeur du monde.
     */
    public World() {
        this.name = "Default World";
        this.entities = new ArrayList<>();
        this.players = new ArrayList<>();
        this.chunks = new HashMap<>();
        this.chunkSize = 16; // Taille par défaut d'un chunk (16x16 blocs)
        this.gravity = new Vector3f(0, -9.81f, 0); // Gravité terrestre
        this.worldTime = 0.0f;
        this.state = WorldState.DAY;
        this.worldGenerator = new WorldGenerator(this);
        this.listeners = new ArrayList<>();

        // Initialiser le moteur physique
        this.physicsEngine = new PhysicsEngine();
        this.physicsEngine.init(this);
        this.physicsEngine.setGravity(gravity);
    }

    /**
     * Constructeur du monde avec un nom.
     * @param name Nom du monde.
     */
    public World(String name) {
        this();
        this.name = name;
    }

    /**
     * Initialise le monde.
     */
    public void init() {
        // Générer le monde initial
        worldGenerator.generateInitialWorld();

        // Notifier les observateurs
        notifyListeners();

        System.out.println("Monde initialisé : " + name);
    }

    /**
     * Met à jour le monde.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        // Mettre à jour le temps du monde
        worldTime += deltaTime;

        // Mettre à jour l'état du monde (jour/nuit, etc.)
        updateWorldState();

        // Mettre à jour les entités
        for (Entity entity : entities) {
            entity.update();
        }

        // Mettre à jour le moteur physique
        physicsEngine.update(deltaTime);

        // Mettre à jour les chunks
        for (Chunk chunk : chunks.values()) {
            chunk.update();
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Met à jour l'état du monde (jour/nuit, météo, etc.).
     */
    private void updateWorldState() {
        // Calculer l'heure de la journée (en secondes)
        float dayTime = worldTime % 86400; // 86400 secondes = 24 heures

        // Mettre à jour l'état en fonction de l'heure
        if (dayTime >= 0 && dayTime < 18000) { // 0h - 5h : Nuit
            state = WorldState.NIGHT;
        } else if (dayTime >= 18000 && dayTime < 36000) { // 5h - 10h : Aube
            state = WorldState.DAWN;
        } else if (dayTime >= 36000 && dayTime < 64800) { // 10h - 18h : Jour
            state = WorldState.DAY;
        } else if (dayTime >= 64800 && dayTime < 75600) { // 18h - 21h : Crépuscule
            state = WorldState.DUSK;
        } else { // 21h - 24h : Nuit
            state = WorldState.NIGHT;
        }
    }

    /**
     * Ajoute une entité au monde.
     * @param entity Entité à ajouter.
     */
    public void addEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        entities.add(entity);
        entity.setWorld(this);

        // Si l'entité a un corps physique, l'ajouter au moteur physique
        if (entity.hasPhysicsBody()) {
            physicsEngine.addBody(entity.getPhysicsBody());
        }

        // Si l'entité est un joueur, l'ajouter à la liste des joueurs
        if (entity instanceof Player) {
            players.add((Player) entity);
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Retire une entité du monde.
     * @param entity Entité à retirer.
     */
    public void removeEntity(Entity entity) {
        if (entity == null) {
            return;
        }

        entities.remove(entity);

        // Si l'entité a un corps physique, le retirer du moteur physique
        if (entity.hasPhysicsBody()) {
            physicsEngine.removeBody(entity.getPhysicsBody());
        }

        // Si l'entité est un joueur, la retirer de la liste des joueurs
        if (entity instanceof Player) {
            players.remove(entity);
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Récupère une entité par son ID.
     * @param entityId ID de l'entité.
     * @return Entité ou null si non trouvée.
     */
    public Entity getEntity(int entityId) {
        for (Entity entity : entities) {
            if (entity != null && entity.getId() == entityId) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Récupère toutes les entités vivantes dans le monde.
     * @return Tableau des entités vivantes.
     */
    public LivingEntity[] getLivingEntities() {
        List<LivingEntity> livingEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                livingEntities.add((LivingEntity) entity);
            }
        }
        return livingEntities.toArray(new LivingEntity[0]);
    }

    /**
     * Ajoute un joueur au monde.
     * @param player Joueur à ajouter.
     */
    public void addPlayer(Player player) {
        if (player == null) {
            return;
        }

        players.add(player);
        entities.add(player);
        player.setWorld(this);

        // Ajouter le corps physique du joueur au moteur physique
        if (player.hasPhysicsBody()) {
            physicsEngine.addBody(player.getPhysicsBody());
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Retire un joueur du monde.
     * @param player Joueur à retirer.
     */
    public void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        players.remove(player);
        entities.remove(player);

        // Retirer le corps physique du joueur du moteur physique
        if (player.hasPhysicsBody()) {
            physicsEngine.removeBody(player.getPhysicsBody());
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Récupère un joueur par son nom.
     * @param playerName Nom du joueur.
     * @return Joueur ou null si non trouvé.
     */
    public Player getPlayer(String playerName) {
        if (playerName == null) {
            return null;
        }
        for (Player player : players) {
            if (player != null && player.getPlayerName() != null && player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Ajoute un chunk au monde.
     * @param chunk Chunk à ajouter.
     */
    public void addChunk(Chunk chunk) {
        if (chunk == null) {
            return;
        }

        String chunkKey = getChunkKey(chunk.getX(), chunk.getZ());
        chunks.put(chunkKey, chunk);
        chunk.setWorld(this);

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Retire un chunk du monde.
     * @param x Coordonnée X du chunk.
     * @param z Coordonnée Z du chunk.
     */
    public void removeChunk(int x, int z) {
        String chunkKey = getChunkKey(x, z);
        Chunk chunk = chunks.remove(chunkKey);
        if (chunk != null) {
            chunk.cleanup();
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Récupère un chunk par ses coordonnées.
     * @param x Coordonnée X du chunk.
     * @param z Coordonnée Z du chunk.
     * @return Chunk ou null si non trouvé.
     */
    public Chunk getChunk(int x, int z) {
        String chunkKey = getChunkKey(x, z);
        return chunks.get(chunkKey);
    }

    /**
     * Génère une clé unique pour un chunk.
     * @param x Coordonnée X du chunk.
     * @param z Coordonnée Z du chunk.
     * @return Clé unique.
     */
    private String getChunkKey(int x, int z) {
        return x + "," + z;
    }

    /**
     * Définit un bloc à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param blockType Type de bloc.
     */
    public void setBlock(int x, int y, int z, BlockType blockType) {
        int chunkX = (int) Math.floor((float) x / chunkSize);
        int chunkZ = (int) Math.floor((float) z / chunkSize);

        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            // Créer un nouveau chunk si nécessaire
            chunk = new Chunk(chunkX, chunkZ, chunkSize);
            addChunk(chunk);
        }

        int localX = Math.abs(x % chunkSize); // Utiliser Math.abs pour éviter les indices négatifs
        int localY = y;
        int localZ = Math.abs(z % chunkSize);

        chunk.setBlock(localX, localY, localZ, new Block(blockType) {
            @Override
            public void update(float deltaTime) {
                // Implémentation spécifique pour le bloc si nécessaire
            }
        });
    }

    /**
     * Récupère un bloc à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return Bloc ou null si non trouvé.
     */
    public Block getBlock(int x, int y, int z) {
        int chunkX = (int) Math.floor((float) x / chunkSize);
        int chunkZ = (int) Math.floor((float) z / chunkSize);

        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            return null;
        }

        int localX = Math.abs(x % chunkSize);
        int localY = y;
        int localZ = Math.abs(z % chunkSize);

        return chunk.getBlock(localX, localY, localZ);
    }

    /**
     * Vérifie si un bloc est solide à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return true si le bloc est solide, false sinon.
     */
    public boolean isBlockSolid(int x, int y, int z) {
        Block block = getBlock(x, y, z);
        return block != null && block.isSolid();
    }

    /**
     * Ajoute un observateur au monde.
     * @param listener Observateur à ajouter.
     */
    public void addListener(WorldListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Retire un observateur du monde.
     * @param listener Observateur à retirer.
     */
    public void removeListener(WorldListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs du monde.
     */
    private void notifyListeners() {
        for (WorldListener listener : listeners) {
            if (listener != null) {
                listener.onWorldUpdate(this);
            }
        }
    }

    /**
     * Nettoie les ressources du monde.
     */
    public void cleanup() {
        // Nettoyer les entités
        for (Entity entity : entities) {
            if (entity != null) {
                entity.cleanup();
            }
        }
        entities.clear();

        // Nettoyer les joueurs
        players.clear();

        // Nettoyer les chunks
        for (Chunk chunk : chunks.values()) {
            if (chunk != null) {
                chunk.cleanup();
            }
        }
        chunks.clear();

        // Nettoyer le moteur physique
        if (physicsEngine != null) {
            physicsEngine.cleanup();
        }

        System.out.println("Ressources du monde nettoyées : " + name);
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public Map<String, Chunk> getChunks() {
        return chunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Vector3f getGravity() {
        return gravity;
    }

    public void setGravity(Vector3f gravity) {
        this.gravity = gravity;
        if (physicsEngine != null) {
            physicsEngine.setGravity(gravity);
        }
    }

    public float getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(float worldTime) {
        this.worldTime = worldTime;
    }

    public WorldState getState() {
        return state;
    }

    public void setState(WorldState state) {
        this.state = state;
    }

    public WorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    public void setWorldGenerator(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
    }

    // --- Interface pour les observateurs du monde ---
    public interface WorldListener {
        void onWorldUpdate(World world);
    }
}