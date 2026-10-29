package fr.alessdevita.valtherion.network.server;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.network.packet.chat.ChatMessagePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntityMovePacket;
import fr.alessdevita.valtherion.network.packet.inventory.InventoryUpdatePacket;
import fr.alessdevita.valtherion.network.packet.login.LoginRequestPacket;
import fr.alessdevita.valtherion.network.packet.login.LoginResponsePacket;
import fr.alessdevita.valtherion.network.packet.world.BlockUpdatePacket;
import fr.alessdevita.valtherion.world.World;

import java.util.*;
import java.util.concurrent.*;

import static fr.alessdevita.valtherion.network.core.PacketHandler.executorService;

/**
 * Serveur de jeu pour Valtherion.
 * Gère la logique métier côté serveur et interagit avec le gestionnaire réseau.
 */
public class GameServer {

    // Gestionnaire ECS
    private ECSManager ecsManager;

    // Monde du jeu
    private World world;

    // Gestionnaire réseau côté serveur
    private ServerNetworkHandler networkHandler;

    // Liste des joueurs connectés (ID du joueur -> Connection)
    private final Map<String, Connection> players;

    // Liste des entités du jeu (ID de l'entité -> Entité)
    private final Map<String, Entity> entities;

    // Indique si le serveur est en cours d'exécution
    private boolean isRunning;

    // Liste des observateurs du serveur de jeu
    private final List<GameServerListener> listeners;

    // Pool de threads pour gérer les tâches du serveur
    private final ExecutorService executorService;

    /**
     * Constructeur du serveur de jeu.
     * @param port Port du serveur.
     */
    public GameServer(int port) {
        this.networkHandler = new ServerNetworkHandler(port);
        this.players = new ConcurrentHashMap<>();
        this.entities = new ConcurrentHashMap<>();
        this.isRunning = false;
        this.listeners = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Initialise le serveur de jeu.
     * @param ecsManager Gestionnaire ECS.
     * @param world Monde du jeu.
     */
    public void init(ECSManager ecsManager, World world) {
        this.ecsManager = ecsManager;
        this.world = world;
    }

    /**
     * Démarre le serveur de jeu.
     * @throws NetworkException Si une erreur se produit lors du démarrage.
     */
    public void start() throws NetworkException {
        if (isRunning) {
            throw new NetworkException("Le serveur est déjà en cours d'exécution.");
        }

        networkHandler.addListener(new ServerNetworkHandler.ServerListener() {
            @Override
            public void onStarted(ServerNetworkHandler handler) {
                isRunning = true;
                notifyStarted();
            }

            @Override
            public void onStopped(ServerNetworkHandler handler) {
                isRunning = false;
                notifyStopped();
            }

            @Override
            public void onClientConnected(ServerNetworkHandler handler, Connection connection) {
                handleClientConnected(connection);
            }

            @Override
            public void onClientDisconnected(ServerNetworkHandler handler, Connection connection) {
                handleClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(ServerNetworkHandler handler, Connection connection, Packet packet) {
                handlePacket(packet, connection);
            }

            @Override
            public void onStarted() {
                isRunning = true;
                notifyStarted();
            }

            @Override
            public void onStopped() {
                isRunning = false;
                notifyStopped();
            }

            @Override
            public void onClientConnected(Connection connection) {
                handleClientConnected(connection);
            }

            @Override
            public void onClientDisconnected(Connection connection) {
                handleClientDisconnected(connection);
            }

            @Override
            public void onPacketReceived(Connection connection, Packet packet) {
                handlePacket(packet, connection);
            }

        });

        networkHandler.start();
    }

    /**
     * Arrête le serveur de jeu.
     */
    public void stop() {
        if (!isRunning) {
            return;
        }

        if (networkHandler != null) {
            networkHandler.stop();
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        isRunning = false;
    }

    /**
     * Traite la connexion d'un client.
     * @param connection Connexion du client.
     */
    private void handleClientConnected(Connection connection) {
        System.out.println("Nouveau client connecté : " + connection.getIpAddress());

        // Ici, tu peux ajouter la logique pour initialiser le joueur
        // Exemple : Créer une entité pour le joueur et l'associer à la connexion

        notifyClientConnected(connection);
    }

    /**
     * Traite la déconnexion d'un client.
     * @param connection Connexion du client.
     */
    private void handleClientDisconnected(Connection connection) {
        System.out.println("Client déconnecté : " + connection.getIpAddress());

        // Retirer le joueur de la liste des joueurs connectés
        players.values().remove(connection);

        notifyClientDisconnected(connection);
    }

    /**
     * Traite un paquet reçu.
     * @param packet Paquet reçu.
     * @param connection Connexion associée.
     */
    private void handlePacket(Packet packet, Connection connection) {
        executorService.submit(() -> {
            if (packet instanceof LoginRequestPacket) {
                handleLoginRequest((LoginRequestPacket) packet, connection);
            } else if (packet instanceof EntityMovePacket) {
                handleEntityMove((EntityMovePacket) packet, connection);
            } else if (packet instanceof BlockUpdatePacket) {
                handleBlockUpdate((BlockUpdatePacket) packet, connection);
            } else if (packet instanceof ChatMessagePacket) {
                handleChatMessage((ChatMessagePacket) packet, connection);
            } else if (packet instanceof InventoryUpdatePacket) {
                handleInventoryUpdate((InventoryUpdatePacket) packet, connection);
            }
        });
    }

    /**
     * Traite une requête de connexion.
     * @param packet Paquet de requête de connexion.
     * @param connection Connexion du client.
     */
    private void handleLoginRequest(LoginRequestPacket packet, Connection connection) {
        String playerName = packet.getPlayerName();
        System.out.println("Requête de connexion reçue pour : " + playerName);

        // Ici, tu peux ajouter la logique pour accepter/refuser la connexion
        // Exemple : Vérifier le nom du joueur, la version du client, etc.

        // Générer un ID unique pour le joueur
        String playerId = "player_" + UUID.randomUUID().toString();

        // Associer le joueur à la connexion
        players.put(playerId, connection);

        // Envoyer une réponse de connexion
        LoginResponsePacket responsePacket = new LoginResponsePacket(
                playerName,
                playerId,
                true,
                "Connexion acceptée"
        );
        try {
            connection.sendPacket(responsePacket);
        } catch (NetworkException e) {
            System.err.println("Erreur lors de l'envoi de la réponse de connexion : " + e.getMessage());
        }

        notifyPlayerConnected(playerId, connection);
    }

    /**
     * Traite le mouvement d'une entité.
     * @param packet Paquet de mouvement d'entité.
     * @param connection Connexion du client.
     */
    private void handleEntityMove(EntityMovePacket packet, Connection connection) {
        if (ecsManager == null) {
            return;
        }

        Entity entity = ecsManager.getEntity(packet.getEntityId());
        if (entity != null) {
            // Mettre à jour la position de l'entité
            // Exemple : entity.getComponent(TransformComponent.class).setPosition(packet.getX(), packet.getY(), packet.getZ());

            // Rediffuser le mouvement aux autres clients
            try {
                networkHandler.broadcastPacket(packet);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion du mouvement : " + e.getMessage());
            }

            notifyEntityMoved(entity, packet, connection);
        }
    }

    /**
     * Traite la mise à jour d'un bloc.
     * @param packet Paquet de mise à jour de bloc.
     * @param connection Connexion du client.
     */
    private void handleBlockUpdate(BlockUpdatePacket packet, Connection connection) {
        if (world == null) {
            return;
        }

        // Mettre à jour le bloc dans le monde
        world.setBlock(packet.getX(), packet.getY(), packet.getZ(), packet.getBlockType());

        // Rediffuser la mise à jour aux autres clients
        try {
            networkHandler.broadcastPacket(packet);
        } catch (NetworkException e) {
            System.err.println("Erreur lors de la rediffusion de la mise à jour du bloc : " + e.getMessage());
        }

        notifyBlockUpdated(packet, connection);
    }

    /**
     * Traite un message de chat.
     * @param packet Paquet de message de chat.
     * @param connection Connexion du client.
     */
    private void handleChatMessage(ChatMessagePacket packet, Connection connection) {
        System.out.println("Message de chat reçu de " + packet.getSenderName() + " : " + packet.getMessage());

        // Rediffuser le message à tous les clients
        try {
            networkHandler.broadcastPacket(packet);
        } catch (NetworkException e) {
            System.err.println("Erreur lors de la rediffusion du message de chat : " + e.getMessage());
        }

        notifyChatMessageReceived(packet, connection);
    }

    /**
     * Traite la mise à jour d'un inventaire.
     * @param packet Paquet de mise à jour d'inventaire.
     * @param connection Connexion du client.
     */
    private void handleInventoryUpdate(InventoryUpdatePacket packet, Connection connection) {
        if (ecsManager == null) {
            return;
        }

        Entity entity = ecsManager.getEntity(packet.getEntityId());
        if (entity != null) {
            // Mettre à jour l'inventaire de l'entité
            // Exemple : entity.getComponent(InventoryComponent.class).updateInventory(packet.getInventoryData());

            // Rediffuser la mise à jour aux autres clients si nécessaire
            try {
                networkHandler.broadcastPacket(packet);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion de la mise à jour de l'inventaire : " + e.getMessage());
            }

            notifyInventoryUpdated(entity, packet, connection);
        }
    }

    /**
     * Ajoute un observateur du serveur de jeu.
     * @param listener Observateur à ajouter.
     */
    public void addListener(GameServerListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du serveur de jeu.
     * @param listener Observateur à retirer.
     */
    public void removeListener(GameServerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs que le serveur a démarré.
     */
    private void notifyStarted() {
        for (GameServerListener listener : listeners) {
            listener.onStarted(this);
        }
    }

    /**
     * Notifie les observateurs que le serveur s'est arrêté.
     */
    private void notifyStopped() {
        for (GameServerListener listener : listeners) {
            listener.onStopped(this);
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est connecté.
     * @param connection Connexion du client.
     */
    private void notifyClientConnected(Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onClientConnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un client s'est déconnecté.
     * @param connection Connexion du client.
     */
    private void notifyClientDisconnected(Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onClientDisconnected(this, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un joueur s'est connecté.
     * @param playerId ID du joueur.
     * @param connection Connexion du joueur.
     */
    private void notifyPlayerConnected(String playerId, Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onPlayerConnected(this, playerId, connection);
        }
    }

    /**
     * Notifie les observateurs qu'une entité a bougé.
     * @param entity Entité déplacée.
     * @param packet Paquet de mouvement.
     * @param connection Connexion du client.
     */
    private void notifyEntityMoved(Entity entity, EntityMovePacket packet, Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onEntityMoved(this, entity, packet, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un bloc a été mis à jour.
     * @param packet Paquet de mise à jour de bloc.
     * @param connection Connexion du client.
     */
    private void notifyBlockUpdated(BlockUpdatePacket packet, Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onBlockUpdated(this, packet, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un message de chat a été reçu.
     * @param packet Paquet de message de chat.
     * @param connection Connexion du client.
     */
    private void notifyChatMessageReceived(ChatMessagePacket packet, Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onChatMessageReceived(this, packet, connection);
        }
    }

    /**
     * Notifie les observateurs qu'un inventaire a été mis à jour.
     * @param entity Entité associée.
     * @param packet Paquet de mise à jour d'inventaire.
     * @param connection Connexion du client.
     */
    private void notifyInventoryUpdated(Entity entity, InventoryUpdatePacket packet, Connection connection) {
        for (GameServerListener listener : listeners) {
            listener.onInventoryUpdated(this, entity, packet, connection);
        }
    }

    // --- Getters et Setters ---

    public ECSManager getECSManager() {
        return ecsManager;
    }

    public World getWorld() {
        return world;
    }

    public ServerNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public Map<String, Connection> getPlayers() {
        return players;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Retourne une représentation textuelle du serveur de jeu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GameServer{" +
                "isRunning=" + isRunning +
                ", playerCount=" + players.size() +
                ", entityCount=" + entities.size() +
                '}';
    }

    // --- Interface pour les observateurs du serveur de jeu ---
    public interface GameServerListener {
        void onStarted(GameServer server);
        void onStopped(GameServer server);
        void onClientConnected(GameServer server, Connection connection);
        void onClientDisconnected(GameServer server, Connection connection);
        void onPlayerConnected(GameServer server, String playerId, Connection connection);
        void onEntityMoved(GameServer server, Entity entity, EntityMovePacket packet, Connection connection);
        void onBlockUpdated(GameServer server, BlockUpdatePacket packet, Connection connection);
        void onChatMessageReceived(GameServer server, ChatMessagePacket packet, Connection connection);
        void onInventoryUpdated(GameServer server, Entity entity, InventoryUpdatePacket packet, Connection connection);
    }
}