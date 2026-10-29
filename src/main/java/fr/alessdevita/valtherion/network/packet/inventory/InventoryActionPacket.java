package fr.alessdevita.valtherion.network.packet.inventory;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet d'action d'inventaire dans Valtherion.
 * Utilisé pour gérer les actions sur l'inventaire (ajout, retrait, échange, utilisation).
 */
public class InventoryActionPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 4002;

    // ID de l'entité (joueur ou coffre)
    private String entityId;

    // Type d'action (ADD, REMOVE, SWAP, USE, DROP)
    private ActionType actionType;

    // ID de l'objet concerné
    private String itemId;

    // Quantité (pour les actions ADD ou REMOVE)
    private int quantity;

    // Position source dans l'inventaire (pour les actions SWAP ou USE)
    private int sourceSlot;

    // Position cible dans l'inventaire (pour les actions SWAP)
    private int targetSlot;

    // Énumération des types d'action
    public enum ActionType {
        ADD,      // Ajout d'un objet
        REMOVE,   // Retrait d'un objet
        SWAP,     // Échange de deux objets
        USE,      // Utilisation d'un objet
        DROP      // Abandon d'un objet
    }

    /**
     * Constructeur du paquet d'action d'inventaire.
     */
    public InventoryActionPacket() {
        super(PACKET_ID);
    }

    /**
     * Constructeur du paquet d'action d'inventaire avec un ID d'entité et un type d'action.
     * @param entityId ID de l'entité.
     * @param actionType Type d'action.
     */
    public InventoryActionPacket(String entityId, ActionType actionType) {
        this();
        this.entityId = entityId;
        this.actionType = actionType;
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID de l'entité
        dataStream.writeUTF(entityId != null ? entityId : "");

        // Écrire le type d'action
        dataStream.writeByte(actionType.ordinal());

        // Écrire l'ID de l'objet (null si non applicable)
        dataStream.writeBoolean(itemId != null);
        if (itemId != null) {
            dataStream.writeUTF(itemId);
        }

        // Écrire la quantité (pour ADD ou REMOVE)
        dataStream.writeInt(quantity);

        // Écrire la position source (pour SWAP ou USE)
        dataStream.writeInt(sourceSlot);

        // Écrire la position cible (pour SWAP)
        dataStream.writeInt(targetSlot);
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID de l'entité
        this.entityId = dataStream.readUTF();

        // Lire le type d'action
        this.actionType = ActionType.values()[dataStream.readByte()];

        // Lire l'ID de l'objet
        if (dataStream.readBoolean()) {
            this.itemId = dataStream.readUTF();
        }

        // Lire la quantité
        this.quantity = dataStream.readInt();

        // Lire la position source
        this.sourceSlot = dataStream.readInt();

        // Lire la position cible
        this.targetSlot = dataStream.readInt();
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite l'action d'inventaire
        System.out.println("Action d'inventaire reçue pour l'entité " + entityId +
                " : " + actionType + " (objet : " + itemId + ")");

        // Ici, tu peux appliquer l'action côté client
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null && entity.hasComponent(InventoryComponent.class)) {
        //     InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        //     switch (actionType) {
        //         case ADD:
        //             inventory.addItem(itemId, quantity);
        //             break;
        //         case REMOVE:
        //             inventory.removeItem(itemId, quantity);
        //             break;
        //         case SWAP:
        //             inventory.swapItems(sourceSlot, targetSlot);
        //             break;
        //         case USE:
        //             inventory.useItem(sourceSlot);
        //             break;
        //         case DROP:
        //             inventory.dropItem(sourceSlot);
        //             break;
        //     }
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite l'action d'inventaire
        System.out.println("Action d'inventaire reçue pour l'entité " + entityId +
                " : " + actionType + " (objet : " + itemId + ")");

        // Ici, tu peux appliquer l'action côté serveur
        // et rediffuser l'action aux autres clients si nécessaire
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null && entity.hasComponent(InventoryComponent.class)) {
        //     InventoryComponent inventory = entity.getComponent(InventoryComponent.class);
        //     switch (actionType) {
        //         case ADD:
        //             inventory.addItem(itemId, quantity);
        //             break;
        //         case REMOVE:
        //             inventory.removeItem(itemId, quantity);
        //             break;
        //         case SWAP:
        //             inventory.swapItems(sourceSlot, targetSlot);
        //             break;
        //         case USE:
        //             inventory.useItem(sourceSlot);
        //             break;
        //         case DROP:
        //             inventory.dropItem(sourceSlot);
        //             break;
        //     }
        // }

        // Rediffuser l'action aux autres clients si nécessaire
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de l'action d'inventaire : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSourceSlot() {
        return sourceSlot;
    }

    public void setSourceSlot(int sourceSlot) {
        this.sourceSlot = sourceSlot;
    }

    public int getTargetSlot() {
        return targetSlot;
    }

    public void setTargetSlot(int targetSlot) {
        this.targetSlot = targetSlot;
    }

    /**
     * Retourne une représentation textuelle du paquet d'action d'inventaire.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "InventoryActionPacket{" +
                "entityId='" + entityId + '\'' +
                ", actionType=" + actionType +
                ", itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                ", sourceSlot=" + sourceSlot +
                ", targetSlot=" + targetSlot +
                '}';
    }
}