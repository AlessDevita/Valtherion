package fr.alessdevita.valtherion.network.core;

import fr.alessdevita.valtherion.network.packet.chat.ChatMessagePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntityMovePacket;
import fr.alessdevita.valtherion.network.packet.entity.EntitySpawnPacket;
import fr.alessdevita.valtherion.network.packet.inventory.InventoryUpdatePacket;
import fr.alessdevita.valtherion.network.packet.login.LoginRequestPacket;
import fr.alessdevita.valtherion.network.packet.login.LoginResponsePacket;
import fr.alessdevita.valtherion.network.packet.world.BlockUpdatePacket;
import fr.alessdevita.valtherion.network.packet.world.ChunkDataPacket;

import java.util.*;

/**
 * Registre des paquets réseau dans Valtherion.
 * Associe chaque ID de paquet à sa classe correspondante.
 */
public class PacketRegistry {

    // Map des IDs de paquets vers leurs classes
    private static final Map<Integer, Class<? extends fr.alessdevita.valtherion.network.core.Packet>> packetClasses;

    // Map des classes de paquets vers leurs IDs
    private static final Map<Class<? extends fr.alessdevita.valtherion.network.core.Packet>, Integer> packetIds;

    // Compteur pour les IDs de paquets
    private static int nextPacketId = 0;

    // Initialisation des maps
    static {
        packetClasses = new HashMap<>();
        packetIds = new HashMap<>();

        // Enregistrer les paquets par défaut
        registerPacket(LoginRequestPacket.class);
        registerPacket(LoginResponsePacket.class);
        registerPacket(EntityMovePacket.class);
        registerPacket(EntitySpawnPacket.class);
        registerPacket(InventoryUpdatePacket.class);
        registerPacket(BlockUpdatePacket.class);
        registerPacket(ChunkDataPacket.class);
        registerPacket(ChatMessagePacket.class);
    }

    /**
     * Enregistre un paquet.
     * @param packetClass Classe du paquet.
     */
    public static void registerPacket(Class<? extends fr.alessdevita.valtherion.network.core.Packet> packetClass) {
        try {
            // Créer une instance du paquet pour récupérer son ID
            fr.alessdevita.valtherion.network.core.Packet packet = packetClass.getDeclaredConstructor().newInstance();
            int packetId = packet.getPacketId();

            packetClasses.put(packetId, packetClass);
            packetIds.put(packetClass, packetId);
        } catch (Exception e) {
            System.err.println("Impossible d'enregistrer le paquet : " + packetClass.getName());
            e.printStackTrace();
        }
    }

    /**
     * Crée un paquet en fonction de son ID.
     * @param packetId ID du paquet.
     * @return Nouvelle instance du paquet ou null si non trouvé.
     */
    public static fr.alessdevita.valtherion.network.core.Packet createPacket(int packetId) {
        Class<? extends fr.alessdevita.valtherion.network.core.Packet> packetClass = packetClasses.get(packetId);
        if (packetClass != null) {
            try {
                return packetClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.println("Impossible de créer le paquet avec l'ID : " + packetId);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Récupère l'ID d'un paquet.
     * @param packetClass Classe du paquet.
     * @return ID du paquet ou -1 si non trouvé.
     */
    public static int getPacketId(Class<? extends fr.alessdevita.valtherion.network.core.Packet> packetClass) {
        return packetIds.getOrDefault(packetClass, -1);
    }

    /**
     * Récupère tous les IDs de paquets enregistrés.
     * @return Collection des IDs de paquets.
     */
    public static Collection<Integer> getRegisteredPacketIds() {
        return packetClasses.keySet();
    }

    /**
     * Récupère toutes les classes de paquets enregistrées.
     * @return Collection des classes de paquets.
     */
    public static Collection<Class<? extends Packet>> getRegisteredPacketClasses() {
        return packetClasses.values();
    }
}