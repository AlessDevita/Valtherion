package fr.alessdevita.valtherion.network.packet.inventory;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;
import java.util.*;

/**
 * Paquet de mise à jour d'inventaire dans Valtherion.
 * Utilisé pour synchroniser les inventaires entre le client et le serveur.
 */
public class InventoryUpdatePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 4001;

    // ID de l'entité (joueur ou coffre)
    private String entityId;

    // Liste des objets dans l'inventaire (ID de l'objet -> quantité)
    private final Map<String, Integer> items;

    // Métadonnées des objets (ID de l'objet -> métadonnées)
    private final Map<String, Map<String, String>> itemMetadata;

    // Type de mise à jour (FULL, PARTIAL, REMOVE)
    private UpdateType updateType;

    // Énumération des types de mise à jour
    public enum UpdateType {
        FULL,      // Mise à jour complète de l'inventaire
        PARTIAL,   // Mise à jour partielle (ajout/suppression d'un objet)
        REMOVE     // Suppression d'un objet
    }

    /**
     * Constructeur du paquet de mise à jour d'inventaire.
     */
    public InventoryUpdatePacket() {
        super(PACKET_ID);
        this.items = new HashMap<>();
        this.itemMetadata = new HashMap<>();
        this.updateType = UpdateType.FULL;
    }

    /**
     * Constructeur du paquet de mise à jour d'inventaire avec un ID d'entité.
     * @param entityId ID de l'entité.
     */
    public InventoryUpdatePacket(String entityId) {
        this();
        this.entityId = entityId;
    }

    /**
     * Ajoute un objet à l'inventaire.
     * @param itemId ID de l'objet.
     * @param quantity Quantité.
     */
    public void addItem(String itemId, int quantity) {
        items.put(itemId, quantity);
    }

    /**
     * Ajoute des métadonnées à un objet.
     * @param itemId ID de l'objet.
     * @param metadata Métadonnées.
     */
    public void addItemMetadata(String itemId, Map<String, String> metadata) {
        itemMetadata.put(itemId, metadata);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID de l'entité
        dataStream.writeUTF(entityId != null ? entityId : "");

        // Écrire le type de mise à jour
        dataStream.writeByte(updateType.ordinal());

        // Écrire le nombre d'objets
        dataStream.writeInt(items.size());
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeInt(entry.getValue());
        }

        // Écrire le nombre d'objets avec métadonnées
        dataStream.writeInt(itemMetadata.size());
        for (Map.Entry<String, Map<String, String>> entry : itemMetadata.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            Map<String, String> metadata = entry.getValue();
            dataStream.writeInt(metadata.size());
            for (Map.Entry<String, String> metadataEntry : metadata.entrySet()) {
                dataStream.writeUTF(metadataEntry.getKey());
                dataStream.writeUTF(metadataEntry.getValue());
            }
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID de l'entité
        this.entityId = dataStream.readUTF();

        // Lire le type de mise à jour
        this.updateType = UpdateType.values()[dataStream.readByte()];

        // Lire les objets
        int itemCount = dataStream.readInt();
        for (int i = 0; i < itemCount; i++) {
            String itemId = dataStream.readUTF();
            int quantity = dataStream.readInt();
            items.put(itemId, quantity);
        }

        // Lire les métadonnées des objets
        int metadataCount = dataStream.readInt();
        for (int i = 0; i < metadataCount; i++) {
            String itemId = dataStream.readUTF();
            int metadataSize = dataStream.readInt();
            Map<String, String> metadata = new HashMap<>();
            for (int j = 0; j < metadataSize; j++) {
                String key = dataStream.readUTF();
                String value = dataStream.readUTF();
                metadata.put(key, value);
            }
            itemMetadata.put(itemId, metadata);
        }
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la mise à jour de l'inventaire
        System.out.println("Mise à jour de l'inventaire pour l'entité " + entityId +
                " (type : " + updateType + ")");

        // Ici, tu peux mettre à jour l'inventaire côté client
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null && entity.hasComponent(InventoryComponent.class)) {
        //     InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        //     for (Map.Entry<String, Integer> entry : items.entrySet()) {
        //         inventory.addItem(entry.getKey(), entry.getValue());
        //     }
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la mise à jour de l'inventaire
        System.out.println("Mise à jour de l'inventaire pour l'entité " + entityId +
                " reçue du client (type : " + updateType + ")");

        // Ici, tu peux mettre à jour l'inventaire côté serveur
        // et rediffuser la mise à jour aux autres clients si nécessaire
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null && entity.hasComponent(InventoryComponent.class)) {
        //     InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        //     for (Map.Entry<String, Integer> entry : items.entrySet()) {
        //         inventory.addItem(entry.getKey(), entry.getValue());
        //     }
        // }

        // Rediffuser la mise à jour aux autres clients si nécessaire
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de la mise à jour de l'inventaire : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public Map<String, Map<String, String>> getItemMetadata() {
        return itemMetadata;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    /**
     * Retourne une représentation textuelle du paquet de mise à jour d'inventaire.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "InventoryUpdatePacket{" +
                "entityId='" + entityId + '\'' +
                ", updateType=" + updateType +
                ", items=" + items.size() +
                ", itemMetadata=" + itemMetadata.size() +
                '}';
    }
}