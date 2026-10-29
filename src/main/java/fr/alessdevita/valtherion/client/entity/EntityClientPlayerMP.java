package fr.alessdevita.valtherion.client.entity;

import fr.alessdevita.valtherion.network.packet.entity.EntityMovePacket;
import fr.alessdevita.valtherion.network.client.GameClient;
import fr.alessdevita.valtherion.client.network.NetHandlerPlayClient;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.item.ItemStack;
import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe pour le joueur local en mode multijoueur dans Valtherion.
 * Étend AbstractClientPlayer pour ajouter des fonctionnalités spécifiques au mode multijoueur.
 */
public class EntityClientPlayerMP extends AbstractClientPlayer {

    public final Inventory inventory;
    public NetHandlerPlayClient sendQueue;
    public int dimension;
    public Object movementInput;
    private boolean sleeping;
    private boolean usingItem;

    // Client de jeu associé
    private GameClient gameClient;

    // Dernière position synchronisée avec le serveur
    private Vector3f lastSyncedPosition;

    // Dernière rotation synchronisée avec le serveur
    private Vector3f lastSyncedRotation;

    // Temps écoulé depuis la dernière synchronisation (en secondes)
    private float timeSinceLastSync;

    // Intervalle de synchronisation (en secondes)
    private float syncInterval;

    // Indique si le joueur est en train de se synchroniser
    private boolean isSyncing;

    /**
     * Constructeur du joueur local en mode multijoueur.
     * @param id ID de l'entité.
     * @param name Nom de l'entité.
     * @param world Monde dans lequel le joueur évolue.
     * @param gameClient Client de jeu associé.
     */
    public EntityClientPlayerMP(String id, String name, World world, GameClient gameClient) {
        super(id, name, world);
        this.inventory = getInventory();
        this.gameClient = gameClient;
        this.lastSyncedPosition = new Vector3f(getPosition());
        this.lastSyncedRotation = new Vector3f(getRotation());
        this.timeSinceLastSync = 0.0f;
        this.syncInterval = 0.1f; // 100 ms
        this.isSyncing = false;
    }

    public boolean isPlayerSleeping() {
        return this.sleeping;
    }

    public void setPlayerSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public boolean isUsingItem() {
        return this.usingItem;
    }

    public void setUsingItem(boolean usingItem) {
        this.usingItem = usingItem;
    }

    public boolean isCurrentToolAdventureModeExempt(int x, int y, int z) {
        return false;
    }

    public void swingItem() {
    }

    public void dropOneItem(boolean dropAll) {
        ItemStack current = this.inventory.getCurrentItem();
        if (current != null && current.stackSize > 0) {
            current.stackSize--;
            if (current.stackSize == 0) {
                this.inventory.mainInventory[this.inventory.currentItem] = null;
            }
        }
    }

    public void preparePlayerToSpawn() {
        setPosition(0.0D, 0.0D, 0.0D);
        setHealth(getMaxHealth());
    }

    public fr.alessdevita.valtherion.stats.StatFileWriter getStatFileWriter() {
        return new fr.alessdevita.valtherion.stats.StatFileWriter();
    }

    private float getRotation() {
        // Assuming the parent class has a method to get rotation
        return 0.0f; // Replace with actual implementation
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Mettre à jour le temps écoulé depuis la dernière synchronisation
        timeSinceLastSync += deltaTime;

        // Si le temps écoulé dépasse l'intervalle de synchronisation, envoyer un paquet de mouvement
        if (timeSinceLastSync >= syncInterval) {
            syncWithServer();
            timeSinceLastSync = 0.0f;
        }
    }

    /**
     * Synchronise le joueur avec le serveur.
     */
    private void syncWithServer() {
        if (gameClient != null && gameClient.isConnected()) {
            // Créer un paquet de mouvement
            EntityMovePacket movePacket = new EntityMovePacket(
                    getId(),
                    getPosition().x,
                    getPosition().y,
                    getPosition().z,
                    getRotation(),
                    getRotation(),
                    getRotation()
            );

            // Envoyer le paquet au serveur
            gameClient.sendPacket(movePacket);

            // Mettre à jour la dernière position et rotation synchronisées
            lastSyncedPosition = new Vector3f(getPosition());
            lastSyncedRotation = new Vector3f(getRotation());

            isSyncing = true;
        }
    }

    /**
     * Déplace le joueur en mode multijoueur.
     * @param direction Direction du déplacement.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void move(Vector3f direction, float deltaTime) {
        super.move(direction, deltaTime);
        // La synchronisation avec le serveur est gérée dans la méthode update()
    }

    /**
     * Met à jour la position du joueur depuis le serveur.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void updatePositionFromServer(float x, float y, float z) {
        // Mettre à jour la position du joueur
        setPosition(new Vector3f(x, y, z));

        // Mettre à jour la dernière position synchronisée
        lastSyncedPosition = new Vector3f(x, y, z);
    }

    /**
     * Met à jour la rotation du joueur depuis le serveur.
     * @param x Rotation X.
     * @param y Rotation Y.
     * @param z Rotation Z.
     */
    public void updateRotationFromServer(float x, float y, float z) {
        // Mettre à jour la rotation du joueur
        setRotation(new Vector3f(x, y, z));

        // Mettre à jour la dernière rotation synchronisée
        lastSyncedRotation = new Vector3f(x, y, z);
    }

    private void setRotation(Vector3f vector3f) {
    }

    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    // --- Getters et Setters ---

    public GameClient getGameClient() {
        return gameClient;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public Vector3f getLastSyncedPosition() {
        return lastSyncedPosition;
    }

    public Vector3f getLastSyncedRotation() {
        return lastSyncedRotation;
    }

    public float getTimeSinceLastSync() {
        return timeSinceLastSync;
    }

    public float getSyncInterval() {
        return syncInterval;
    }

    public void setSyncInterval(float syncInterval) {
        this.syncInterval = syncInterval;
    }

    public boolean isSyncing() {
        return isSyncing;
    }

    public void setSyncing(boolean syncing) {
        this.isSyncing = syncing;
    }

    /**
     * Retourne une représentation textuelle du joueur local en mode multijoueur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityClientPlayerMP{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", position=" + getPosition() +
                ", lastSyncedPosition=" + lastSyncedPosition +
                ", syncInterval=" + syncInterval +
                '}';
    }
}
