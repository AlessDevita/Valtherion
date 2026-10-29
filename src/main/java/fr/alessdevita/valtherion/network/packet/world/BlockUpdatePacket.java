package fr.alessdevita.valtherion.network.packet.world;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.block.BlockType;

import java.io.*;
import java.util.*;

/**
 * Paquet de mise à jour de bloc dans Valtherion.
 * Utilisé pour synchroniser les modifications de blocs entre le client et le serveur.
 */
public class BlockUpdatePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 3001;

    // Position du bloc (x, y, z)
    private int x, y, z;

    // Type de bloc
    private BlockType blockType;

    // Type de mise à jour (PLACE, BREAK, UPDATE)
    private UpdateType updateType;

    // Métadonnées du bloc (ex: orientation, état, etc.)
    private final Map<String, String> metadata;

    // Énumération des types de mise à jour
    public enum UpdateType {
        PLACE,   // Placement d'un bloc
        BREAK,   // Destruction d'un bloc
        UPDATE   // Mise à jour d'un bloc (ex: changement de type)
    }

    /**
     * Constructeur du paquet de mise à jour de bloc.
     */
    public BlockUpdatePacket() {
        super(PACKET_ID);
        this.metadata = new HashMap<>();
    }

    /**
     * Constructeur du paquet de mise à jour de bloc avec une position et un type de bloc.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     * @param blockType Type de bloc.
     */
    public BlockUpdatePacket(int x, int y, int z, BlockType blockType) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockType = blockType;
        this.updateType = UpdateType.PLACE;
    }

    /**
     * Constructeur du paquet de mise à jour de bloc avec une position, un type de bloc et un type de mise à jour.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     * @param blockType Type de bloc.
     * @param updateType Type de mise à jour.
     */
    public BlockUpdatePacket(int x, int y, int z, BlockType blockType, UpdateType updateType) {
        this(x, y, z, blockType);
        this.updateType = updateType;
    }

    /**
     * Ajoute une métadonnée au bloc.
     * @param key Clé de la métadonnée.
     * @param value Valeur de la métadonnée.
     */
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire la position
        dataStream.writeInt(x);
        dataStream.writeInt(y);
        dataStream.writeInt(z);

        // Écrire le type de bloc
        dataStream.writeUTF(blockType != null ? blockType.name() : "");

        // Écrire le type de mise à jour
        dataStream.writeByte(updateType.ordinal());

        // Écrire le nombre de métadonnées
        dataStream.writeInt(metadata.size());
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeUTF(entry.getValue());
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire la position
        this.x = dataStream.readInt();
        this.y = dataStream.readInt();
        this.z = dataStream.readInt();

        // Lire le type de bloc
        String blockTypeName = dataStream.readUTF();
        this.blockType = blockTypeName.isEmpty() ? null : BlockType.valueOf(blockTypeName);

        // Lire le type de mise à jour
        this.updateType = UpdateType.values()[dataStream.readByte()];

        // Lire les métadonnées
        int metadataCount = dataStream.readInt();
        for (int i = 0; i < metadataCount; i++) {
            String key = dataStream.readUTF();
            String value = dataStream.readUTF();
            metadata.put(key, value);
        }
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la mise à jour du bloc
        System.out.println("Mise à jour du bloc à (" + x + ", " + y + ", " + z + ") : " +
                (blockType != null ? blockType.name() : "null") +
                " (type : " + updateType + ")");

        // Ici, tu peux mettre à jour le bloc côté client
        // Exemple :
        // if (world != null) {
        //     world.setBlock(x, y, z, blockType);
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la mise à jour du bloc
        System.out.println("Mise à jour du bloc à (" + x + ", " + y + ", " + z + ") reçue du client : " +
                (blockType != null ? blockType.name() : "null") +
                " (type : " + updateType + ")");

        // Ici, tu peux mettre à jour le bloc côté serveur
        // et rediffuser la mise à jour aux autres clients
        // Exemple :
        // if (world != null) {
        //     world.setBlock(x, y, z, blockType);
        // }

        // Rediffuser la mise à jour aux autres clients
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de la mise à jour du bloc : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Retourne une représentation textuelle du paquet de mise à jour de bloc.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "BlockUpdatePacket{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", blockType=" + (blockType != null ? blockType.name() : "null") +
                ", updateType=" + updateType +
                ", metadata=" + metadata.size() +
                '}';
    }
}