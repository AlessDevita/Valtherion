package fr.alessdevita.valtherion.network.core;

import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.network.core.NetworkManager;
import fr.alessdevita.valtherion.network.exceptions.NetworkException;
import fr.alessdevita.valtherion.network.packet.chat.ChatMessagePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntityMovePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntitySpawnPacket;
import fr.alessdevita.valtherion.network.packet.inventory.InventoryUpdatePacket;
import fr.alessdevita.valtherion.network.packet.login.LoginRequestPacket;
import fr.alessdevita.valtherion.network.packet.login.LoginResponsePacket;
import fr.alessdevita.valtherion.network.packet.world.BlockUpdatePacket;
import fr.alessdevita.valtherion.network.packet.world.ChunkDataPacket;

import java.util.*;
import java.util.concurrent.*;

/**
 * Gestionnaire de paquets réseau dans Valtherion.
 * Centralise le traitement des paquets reçus et leur acheminement.
 * Utilise un pool de threads pour traiter les paquets de manière asynchrone.
 */
public class PacketHandler {

    // Pool de threads pour traiter les paquets
    public static final ExecutorService executorService = Executors.newCachedThreadPool();

    // Map des gestionnaires de paquets (ID de paquet -> Gestionnaire)
    private static final Map<Integer, PacketProcessor> packetProcessors;

    // Liste des observateurs de paquets
    private static final List<PacketHandlerListener> listeners;

    // Indique si le gestionnaire est initialisé
    private static boolean isInitialized;

    // Initialisation statique
    static {
        packetProcessors = new ConcurrentHashMap<>();
        listeners = new CopyOnWriteArrayList<>();
        isInitialized = false;
    }

    /**
     * Initialise le gestionnaire de paquets.
     */
    public static void init() {
        if (isInitialized) {
            return;
        }

        // Enregistrer les gestionnaires par défaut
        registerPacketProcessor(LoginRequestPacket.PACKET_ID, new LoginRequestProcessor());
        registerPacketProcessor(LoginResponsePacket.PACKET_ID, new LoginResponseProcessor());
        registerPacketProcessor(EntityMovePacket.PACKET_ID, new EntityMoveProcessor());
        registerPacketProcessor(EntitySpawnPacket.PACKET_ID, new EntitySpawnProcessor());
        registerPacketProcessor(InventoryUpdatePacket.PACKET_ID, new InventoryUpdateProcessor());
        registerPacketProcessor(BlockUpdatePacket.PACKET_ID, new BlockUpdateProcessor());
        registerPacketProcessor(ChunkDataPacket.PACKET_ID, new ChunkDataProcessor());
        registerPacketProcessor(ChatMessagePacket.PACKET_ID, new ChatMessageProcessor());

        isInitialized = true;
    }

    /**
     * Enregistre un gestionnaire de paquet.
     * @param packetId ID du paquet.
     * @param processor Gestionnaire de paquet.
     */
    public static void registerPacketProcessor(int packetId, PacketProcessor processor) {
        packetProcessors.put(packetId, processor);
    }

    /**
     * Traite un paquet côté client.
     * @param packet Paquet à traiter.
     * @param connection Connexion associée.
     */
    public static void handlePacketClient(Packet packet, Connection connection) {
        if (packet == null || connection == null) {
            return;
        }

        // Soumettre le traitement du paquet au pool de threads
        executorService.submit(() -> {
            try {
                // Trouver le gestionnaire de paquet
                PacketProcessor processor = packetProcessors.get(packet.getPacketId());
                if (processor != null) {
                    processor.processClient(packet, connection);
                } else {
                    // Gestionnaire par défaut : appeler handleClient du paquet
                    packet.handleClient(connection);
                }

                // Notifier les observateurs
                notifyPacketProcessed(packet, connection, false);
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement du paquet (client) : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Traite un paquet côté serveur.
     * @param packet Paquet à traiter.
     * @param connection Connexion associée.
     */
    public static void handlePacketServer(Packet packet, Connection connection) {
        if (packet == null || connection == null) {
            return;
        }

        // Soumettre le traitement du paquet au pool de threads
        executorService.submit(() -> {
            try {
                // Trouver le gestionnaire de paquet
                PacketProcessor processor = packetProcessors.get(packet.getPacketId());
                if (processor != null) {
                    processor.processServer(packet, connection);
                } else {
                    // Gestionnaire par défaut : appeler handleServer du paquet
                    packet.handleServer(connection);
                }

                // Notifier les observateurs
                notifyPacketProcessed(packet, connection, true);
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement du paquet (serveur) : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Traite un paquet en fonction du mode (client ou serveur).
     * @param packet Paquet à traiter.
     * @param connection Connexion associée.
     * @param isServerMode true si le traitement est côté serveur, false sinon.
     */
    public static void handlePacket(Packet packet, Connection connection, boolean isServerMode) {
        if (isServerMode) {
            handlePacketServer(packet, connection);
        } else {
            handlePacketClient(packet, connection);
        }
    }

    /**
     * Ajoute un observateur de paquets.
     * @param listener Observateur à ajouter.
     */
    public static void addListener(PacketHandlerListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de paquets.
     * @param listener Observateur à retirer.
     */
    public static void removeListener(PacketHandlerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs qu'un paquet a été traité.
     * @param packet Paquet traité.
     * @param connection Connexion associée.
     * @param isServerMode true si le traitement était côté serveur.
     */
    private static void notifyPacketProcessed(Packet packet, Connection connection, boolean isServerMode) {
        for (PacketHandlerListener listener : listeners) {
            listener.onPacketProcessed(packet, connection, isServerMode);
        }
    }

    /**
     * Arrête le gestionnaire de paquets.
     */
    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // --- Interface pour les gestionnaires de paquets ---
    public interface PacketProcessor {
        void processClient(Packet packet, Connection connection);
        void processServer(Packet packet, Connection connection);
    }

    // --- Gestionnaires de paquets par défaut ---

    // Gestionnaire pour LoginRequestPacket
    private static class LoginRequestProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            // Le client ne traite pas ce paquet (il l'envoie)
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            LoginRequestPacket loginPacket = (LoginRequestPacket) packet;
            System.out.println("Traitement de la requête de connexion pour : " + loginPacket.getPlayerName());

            // Ici, tu peux ajouter la logique pour accepter/refuser la connexion
            // Exemple : Vérifier le nom du joueur, la version du client, etc.

            // Envoyer une réponse de connexion
            LoginResponsePacket responsePacket = new LoginResponsePacket(
                    loginPacket.getPlayerName(),
                    true,
                    "Connexion acceptée"
            );
            try {
                connection.sendPacket(responsePacket);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de l'envoi de la réponse de connexion : " + e.getMessage());
            }
        }
    }

    // Gestionnaire pour LoginResponsePacket
    private static class LoginResponseProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            LoginResponsePacket responsePacket = (LoginResponsePacket) packet;
            System.out.println("Réponse de connexion reçue : " + responsePacket.getMessage());

            if (responsePacket.isSuccess()) {
                // Connexion acceptée : initialiser le joueur
                System.out.println("Connexion acceptée pour : " + responsePacket.getPlayerName());
            } else {
                // Connexion refusée : afficher le message d'erreur
                System.err.println("Connexion refusée : " + responsePacket.getMessage());
            }
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            // Le serveur ne traite pas ce paquet (il l'envoie)
        }
    }

    // Gestionnaire pour EntityMovePacket
    private static class EntityMoveProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            EntityMovePacket movePacket = (EntityMovePacket) packet;
            System.out.println("Mouvement de l'entité " + movePacket.getEntityId() +
                    " reçu : (" + movePacket.getX() + ", " + movePacket.getY() + ", " + movePacket.getZ() + ")");

            // Ici, tu peux mettre à jour la position de l'entité côté client
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            EntityMovePacket movePacket = (EntityMovePacket) packet;
            System.out.println("Mouvement de l'entité " + movePacket.getEntityId() +
                    " reçu du client : (" + movePacket.getX() + ", " + movePacket.getY() + ", " + movePacket.getZ() + ")");

            // Ici, tu peux mettre à jour la position de l'entité côté serveur
            // et rediffuser le paquet aux autres clients
            try {
                NetworkManager.getInstance().sendPacketTo(packet, connection);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion du mouvement : " + e.getMessage());
            }
        }
    }

    // Gestionnaire pour EntitySpawnPacket
    private static class EntitySpawnProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            EntitySpawnPacket spawnPacket = (EntitySpawnPacket) packet;
            System.out.println("Apparition de l'entité " + spawnPacket.getEntityId() +
                    " reçue à : (" + spawnPacket.getX() + ", " + spawnPacket.getY() + ", " + spawnPacket.getZ() + ")");

            // Ici, tu peux créer l'entité côté client
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            EntitySpawnPacket spawnPacket = (EntitySpawnPacket) packet;
            System.out.println("Apparition de l'entité " + spawnPacket.getEntityId() +
                    " reçue du client à : (" + spawnPacket.getX() + ", " + spawnPacket.getY() + ", " + spawnPacket.getZ() + ")");

            // Ici, tu peux créer l'entité côté serveur
            // et rediffuser le paquet aux autres clients
            try {
                NetworkManager.getInstance().sendPacketTo(packet, connection);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion de l'apparition : " + e.getMessage());
            }
        }
    }

    // Gestionnaire pour InventoryUpdatePacket
    private static class InventoryUpdateProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            InventoryUpdatePacket inventoryPacket = (InventoryUpdatePacket) packet;
            System.out.println("Mise à jour de l'inventaire reçue pour l'entité : " + inventoryPacket.getEntityId());

            // Ici, tu peux mettre à jour l'inventaire côté client
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            InventoryUpdatePacket inventoryPacket = (InventoryUpdatePacket) packet;
            System.out.println("Mise à jour de l'inventaire reçue du client pour l'entité : " + inventoryPacket.getEntityId());

            // Ici, tu peux mettre à jour l'inventaire côté serveur
            // et rediffuser le paquet aux autres clients si nécessaire
        }
    }

    // Gestionnaire pour BlockUpdatePacket
    private static class BlockUpdateProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            BlockUpdatePacket blockPacket = (BlockUpdatePacket) packet;
            System.out.println("Mise à jour du bloc à (" + blockPacket.getX() + ", " +
                    blockPacket.getY() + ", " + blockPacket.getZ() + ") : " + blockPacket.getBlockType());

            // Ici, tu peux mettre à jour le bloc côté client
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            BlockUpdatePacket blockPacket = (BlockUpdatePacket) packet;
            System.out.println("Mise à jour du bloc à (" + blockPacket.getX() + ", " +
                    blockPacket.getY() + ", " + blockPacket.getZ() + ") reçue du client : " + blockPacket.getBlockType());

            // Ici, tu peux mettre à jour le bloc côté serveur
            // et rediffuser le paquet aux autres clients
            try {
                NetworkManager.getInstance().sendPacketTo(packet, connection);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion de la mise à jour du bloc : " + e.getMessage());
            }
        }
    }

    // Gestionnaire pour ChunkDataPacket
    private static class ChunkDataProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            ChunkDataPacket chunkPacket = (ChunkDataPacket) packet;
            System.out.println("Données du chunk reçues pour : (" + chunkPacket.getX() + ", " + chunkPacket.getZ() + ")");

            // Ici, tu peux charger le chunk côté client
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            // Le serveur ne traite pas ce paquet (il l'envoie)
        }
    }

    // Gestionnaire pour ChatMessagePacket
    private static class ChatMessageProcessor implements PacketProcessor {
        @Override
        public void processClient(Packet packet, Connection connection) {
            ChatMessagePacket chatPacket = (ChatMessagePacket) packet;
            System.out.println("[" + chatPacket.getSenderName() + "] " + chatPacket.getMessage());

            // Ici, tu peux afficher le message dans le chat du jeu
        }

        @Override
        public void processServer(Packet packet, Connection connection) {
            ChatMessagePacket chatPacket = (ChatMessagePacket) packet;
            System.out.println("Message de chat reçu de " + chatPacket.getSenderName() + " : " + chatPacket.getMessage());

            // Ici, tu peux rediffuser le message à tous les clients
            try {
                NetworkManager.getInstance().sendPacket(chatPacket);
            } catch (NetworkException e) {
                System.err.println("Erreur lors de la rediffusion du message de chat : " + e.getMessage());
            }
        }
    }

    // --- Interface pour les observateurs de paquets ---
    public interface PacketHandlerListener {
        void onPacketProcessed(Packet packet, Connection connection, boolean isServerMode);
    }
}