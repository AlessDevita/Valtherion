package fr.alessdevita.valtherion.network.packet.entity;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de disparition d'entité dans Valtherion.
 * Utilisé pour supprimer une entité côté client ou serveur.
 */
public class EntityDespawnPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 2003;

    // ID de l'entité à supprimer
    private String entityId;

    // Raison de la disparition
    private String reason;

    // Indique si l'entité doit être supprimée immédiatement (sans animation)
    private boolean immediate;

    // Type de disparition (DEATH, DISCONNECT, TELEPORT, REMOVE)
    private DespawnType despawnType;

    // Énumération des types de disparition
    public enum DespawnType {
        DEATH,         // Mort de l'entité
        DISCONNECT,    // Déconnexion du joueur
        TELEPORT,      // Téléportation
        REMOVE         // Suppression manuelle
    }

    /**
     * Constructeur du paquet de disparition d'entité.
     */
    public EntityDespawnPacket() {
        super(PACKET_ID);
        this.reason = "Suppression";
        this.immediate = false;
        this.despawnType = DespawnType.REMOVE;
    }

    /**
     * Constructeur du paquet de disparition d'entité avec un ID d'entité.
     * @param entityId ID de l'entité à supprimer.
     */
    public EntityDespawnPacket(String entityId) {
        this();
        this.entityId = entityId;
    }

    /**
     * Constructeur du paquet de disparition d'entité avec un ID d'entité et une raison.
     * @param entityId ID de l'entité à supprimer.
     * @param reason Raison de la disparition.
     */
    public EntityDespawnPacket(String entityId, String reason) {
        this(entityId);
        this.reason = reason;
    }

    /**
     * Constructeur du paquet de disparition d'entité avec un ID d'entité, une raison et un type.
     * @param entityId ID de l'entité à supprimer.
     * @param reason Raison de la disparition.
     * @param despawnType Type de disparition.
     */
    public EntityDespawnPacket(String entityId, String reason, DespawnType despawnType) {
        this(entityId, reason);
        this.despawnType = despawnType;
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID de l'entité
        dataStream.writeUTF(entityId != null ? entityId : "");

        // Écrire la raison
        dataStream.writeUTF(reason != null ? reason : "");

        // Écrire le flag de suppression immédiate
        dataStream.writeBoolean(immediate);

        // Écrire le type de disparition
        dataStream.writeByte(despawnType.ordinal());
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID de l'entité
        this.entityId = dataStream.readUTF();

        // Lire la raison
        this.reason = dataStream.readUTF();

        // Lire le flag de suppression immédiate
        this.immediate = dataStream.readBoolean();

        // Lire le type de disparition
        this.despawnType = DespawnType.values()[dataStream.readByte()];
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la disparition de l'entité
        System.out.println("Disparition de l'entité " + entityId + " : " + reason + " (type : " + despawnType + ")");

        // Ici, tu peux supprimer l'entité côté client
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null) {
        //     if (immediate) {
        //         ecsManager.destroyEntity(entityId);
        //     } else {
        //         // Jouer une animation de disparition
        //         entity.addComponent(new DespawnAnimationComponent());
        //     }
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la disparition de l'entité
        System.out.println("Disparition de l'entité " + entityId + " reçue du client : " + reason + " (type : " + despawnType + ")");

        // Ici, tu peux supprimer l'entité côté serveur
        // et rediffuser la disparition aux autres clients
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null) {
        //     ecsManager.destroyEntity(entityId);
        // }

        // Rediffuser la disparition aux autres clients
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de la disparition : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public DespawnType getDespawnType() {
        return despawnType;
    }

    public void setDespawnType(DespawnType despawnType) {
        this.despawnType = despawnType;
    }

    /**
     * Retourne une représentation textuelle du paquet de disparition d'entité.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityDespawnPacket{" +
                "entityId='" + entityId + '\'' +
                ", reason='" + reason + '\'' +
                ", immediate=" + immediate +
                ", despawnType=" + despawnType +
                '}';
    }
}