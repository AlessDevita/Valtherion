package fr.alessdevita.valtherion.network.client;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.network.packet.chat.ChatMessagePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntityMovePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntitySpawnPacket;
import fr.alessdevita.valtherion.network.packet.inventory.InventoryUpdatePacket;
import fr.alessdevita.valtherion.network.packet.login.LoginResponsePacket;
import fr.alessdevita.valtherion.network.packet.world.BlockUpdatePacket;
import fr.alessdevita.valtherion.network.packet.world.ChunkDataPacket;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

/**
 * Client de jeu pour Valtherion.
 * Gère la logique métier côté client et interagit avec le gestionnaire réseau.
 */
public class GameClient {

    // Gestionnaire ECS
    private ECSManager ecsManager;

    // Monde du jeu
    private World world;

    // Gestionnaire réseau côté client
    private ClientNetworkHandler networkHandler;

    // Nom du joueur
    private String playerName;

    // ID du joueur
    private String playerId;

    // Indique si le client est connecté
    private boolean isConnected;

    // Liste des observateurs du client de jeu
    private final List<GameClientListener> listeners;

    // File des paquets à envoyer
    private final List<Packet> packetQueue;

    /**
     * Constructeur du client de jeu.
     * @param playerName Nom du joueur.
     */
    public GameClient(String playerName) {
        this.playerName = playerName;
        this.playerId = "player_" + UUID.randomUUID().toString();
        this.isConnected = false;
        this.listeners = new ArrayList<>();
        this.packetQueue = new ArrayList<>();
    }

    /**
     * Initialise le client de jeu.
     * @param ecsManager Gestionnaire ECS.
     * @param world Monde du jeu.
     */
    public void init(ECSManager ecsManager, World world) {
        this.ecsManager = ecsManager;
        this.world = world;
    }

    /**
     * Connecte le client au serveur.
     * @param serverIp Adresse IP du serveur.
     * @param serverPort Port du serveur.
     * @throws NetworkException Si une erreur se produit lors de la connexion.
     */
    public void connect(String serverIp, int serverPort) throws NetworkException {
        networkHandler = new ClientNetworkHandler(serverIp, serverPort, playerName);
        networkHandler.addListener(new ClientNetworkHandler.ClientListener() {
            @Override
            public void onConnected(ClientNetworkHandler handler, Connection connection) {
                isConnected = true;
                notifyConnected(connection);

                // Envoyer les paquets en attente
                sendPendingPackets();
            }

            @Override
            public void onDisconnected(ClientNetworkHandler handler, Connection connection) {
                isConnected = false;
                notifyDisconnected(connection);
            }

            @Override
            public void onPacketReceived(ClientNetworkHandler handler, Connection connection, Packet packet) {
                handlePacket(packet, connection);
            }

            @Override
            public void onConnected(Connection connection) {

            }

            @Override
            public void onDisconnected(Connection connection) {

            }

            @Override
            public void onPacketReceived(Connection connection, Packet packet) {

            }
        });

        networkHandler.connect();
    }

    /**
     * Déconnecte le client du serveur.
     */
    public void disconnect() {
        if (networkHandler != null) {
            networkHandler.disconnect();
        }
        isConnected = false;
    }

    /**
     * Envoie un paquet au serveur.
     * @param packet Paquet à envoyer.
     */
    public void sendPacket(Packet packet) {
        if (isConnected && networkHandler != null) {
            try {
                networkHandler.sendPacket(packet);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de l'envoi d'un paquet : " + e.getMessage());
            }
        } else {
            // Ajouter le paquet à la file d'attente
            packetQueue.add(packet);
        }
    }

    /**
     * Envoie les paquets en attente.
     */
    private void sendPendingPackets() {
        for (Packet packet : packetQueue) {
            sendPacket(packet);
        }
        packetQueue.clear();
    }

    /**
     * Traite un paquet reçu.
     * @param packet Paquet reçu.
     * @param connection Connexion associée.
     */
    private void handlePacket(Packet packet, Connection connection) {
        if (packet instanceof LoginResponsePacket) {
            handleLoginResponse((LoginResponsePacket) packet);
        } else if (packet instanceof EntitySpawnPacket) {
            handleEntitySpawn((EntitySpawnPacket) packet);
        } else if (packet instanceof EntityMovePacket) {
            handleEntityMove((EntityMovePacket) packet);
        } else if (packet instanceof BlockUpdatePacket) {
            handleBlockUpdate((BlockUpdatePacket) packet);
        } else if (packet instanceof ChunkDataPacket) {
            handleChunkData((ChunkDataPacket) packet);
        } else if (packet instanceof ChatMessagePacket) {
            handleChatMessage((ChatMessagePacket) packet);
        } else if (packet instanceof InventoryUpdatePacket) {
            handleInventoryUpdate((InventoryUpdatePacket) packet);
        }
    }

    /**
     * Traite une réponse de connexion.
     * @param packet Paquet de réponse de connexion.
     */
    private void handleLoginResponse(LoginResponsePacket packet) {
        if (packet.isSuccess()) {
            System.out.println("Connexion acceptée : " + packet.getMessage());
            playerId = packet.getPlayerId();
            notifyLoginSuccess(playerId);
        } else {
            System.err.println("Connexion refusée : " + packet.getMessage());
            notifyLoginFailed(packet.getMessage());
        }
    }

    /**
     * Traite l'apparition d'une entité.
     * @param packet Paquet d'apparition d'entité.
     */
    private void handleEntitySpawn(EntitySpawnPacket packet) {
        if (ecsManager == null) {
            return;
        }

        // Créer une nouvelle entité
        Entity entity = ecsManager.createEntity(packet.getEntityId());

        // Ici, tu peux ajouter les composants à l'entité en fonction des données du paquet
        // Exemple : entity.addComponent(new TransformComponent(packet.getX(), packet.getY(), packet.getZ()));

        notifyEntitySpawned(entity, packet);
    }

    /**
     * Traite le mouvement d'une entité.
     * @param packet Paquet de mouvement d'entité.
     */
    private void handleEntityMove(EntityMovePacket packet) {
        if (ecsManager == null) {
            return;
        }

        Entity entity = ecsManager.getEntity(packet.getEntityId());
        if (entity != null) {
            // Mettre à jour la position de l'entité
            // Exemple : entity.getComponent(TransformComponent.class).setPosition(packet.getX(), packet.getY(), packet.getZ());

            notifyEntityMoved(entity, packet);
        }
    }

    /**
     * Traite la mise à jour d'un bloc.
     * @param packet Paquet de mise à jour de bloc.
     */
    private void handleBlockUpdate(BlockUpdatePacket packet) {
        if (world == null) {
            return;
        }

        // Mettre à jour le bloc dans le monde
        world.setBlock(packet.getX(), packet.getY(), packet.getZ(), packet.getBlockType());

        notifyBlockUpdated(packet);
    }

    /**
     * Traite les données d'un chunk.
     * @param packet Paquet de données de chunk.
     */
    private void handleChunkData(ChunkDataPacket packet) {
        if (world == null) {
            return;
        }

        // Charger le chunk dans le monde
        // Exemple : world.loadChunk(packet.getX(), packet.getZ(), packet.getChunkData());

        notifyChunkLoaded(packet);
    }

    /**
     * Traite un message de chat.
     * @param packet Paquet de message de chat.
     */
    private void handleChatMessage(ChatMessagePacket packet) {
        System.out.println("[" + packet.getSenderName() + "] " + packet.getMessage());

        notifyChatMessageReceived(packet);
    }

    /**
     * Traite la mise à jour d'un inventaire.
     * @param packet Paquet de mise à jour d'inventaire.
     */
    private void handleInventoryUpdate(InventoryUpdatePacket packet) {
        if (ecsManager == null) {
            return;
        }

        Entity entity = ecsManager.getEntity(packet.getEntityId());
        if (entity != null) {
            // Mettre à jour l'inventaire de l'entité
            // Exemple : entity.getComponent(InventoryComponent.class).updateInventory(packet.getInventoryData());

            notifyInventoryUpdated(entity, packet);
        }
    }

    /**
     * Ajoute un observateur du client de jeu.
     * @param listener Observateur à ajouter.
     */
    public void addListener(GameClientListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du client de jeu.
     * @param listener Observateur à retirer.
     */
    public void removeListener(GameClientListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs que le client est connecté.
     * @param connection Connexion associée.
     */
    private void notifyConnected(Connection connection) {
        for (GameClientListener listener : listeners) {
            listener.onConnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs que le client est déconnecté.
     * @param connection Connexion associée.
     */
    private void notifyDisconnected(Connection connection) {
        for (GameClientListener listener : listeners) {
            listener.onDisconnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs que la connexion a réussi.
     * @param playerId ID du joueur.
     */
    private void notifyLoginSuccess(String playerId) {
        for (GameClientListener listener : listeners) {
            listener.onLoginSuccess(this, playerId);
        }
    }

    /**
     * Notifie les observateurs que la connexion a échoué.
     * @param reason Raison de l'échec.
     */
    private void notifyLoginFailed(String reason) {
        for (GameClientListener listener : listeners) {
            listener.onLoginFailed(this, reason);
        }
    }

    /**
     * Notifie les observateurs qu'une entité est apparue.
     * @param entity Entité apparue.
     * @param packet Paquet d'apparition.
     */
    private void notifyEntitySpawned(Entity entity, EntitySpawnPacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onEntitySpawned(this, entity, packet);
        }
    }

    /**
     * Notifie les observateurs qu'une entité a bougé.
     * @param entity Entité déplacée.
     * @param packet Paquet de mouvement.
     */
    private void notifyEntityMoved(Entity entity, EntityMovePacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onEntityMoved(this, entity, packet);
        }
    }

    /**
     * Notifie les observateurs qu'un bloc a été mis à jour.
     * @param packet Paquet de mise à jour de bloc.
     */
    private void notifyBlockUpdated(BlockUpdatePacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onBlockUpdated(this, packet);
        }
    }

    /**
     * Notifie les observateurs qu'un chunk a été chargé.
     * @param packet Paquet de données de chunk.
     */
    private void notifyChunkLoaded(ChunkDataPacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onChunkLoaded(this, packet);
        }
    }

    /**
     * Notifie les observateurs qu'un message de chat a été reçu.
     * @param packet Paquet de message de chat.
     */
    private void notifyChatMessageReceived(ChatMessagePacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onChatMessageReceived(this, packet);
        }
    }

    /**
     * Notifie les observateurs qu'un inventaire a été mis à jour.
     * @param entity Entité associée.
     * @param packet Paquet de mise à jour d'inventaire.
     */
    private void notifyInventoryUpdated(Entity entity, InventoryUpdatePacket packet) {
        for (GameClientListener listener : listeners) {
            listener.onInventoryUpdated(this, entity, packet);
        }
    }

    // --- Getters et Setters ---

    public ECSManager getECSManager() {
        return ecsManager;
    }

    public World getWorld() {
        return world;
    }

    public ClientNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Retourne une représentation textuelle du client de jeu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GameClient{" +
                "playerName='" + playerName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", isConnected=" + isConnected +
                '}';
    }

    public void sendChatMessage(String inputMessage) {
    }

    public void executeCommand(String command) {

    }

    // --- Interface pour les observateurs du client de jeu ---
    public interface GameClientListener {
        void onConnected(GameClient client, Connection connection);
        void onDisconnected(GameClient client, Connection connection);
        void onLoginSuccess(GameClient client, String playerId);
        void onLoginFailed(GameClient client, String reason);
        void onEntitySpawned(GameClient client, Entity entity, EntitySpawnPacket packet);
        void onEntityMoved(GameClient client, Entity entity, EntityMovePacket packet);
        void onBlockUpdated(GameClient client, BlockUpdatePacket packet);
        void onChunkLoaded(GameClient client, ChunkDataPacket packet);
        void onChatMessageReceived(GameClient client, ChatMessagePacket packet);
        void onInventoryUpdated(GameClient client, Entity entity, InventoryUpdatePacket packet);
    }
}