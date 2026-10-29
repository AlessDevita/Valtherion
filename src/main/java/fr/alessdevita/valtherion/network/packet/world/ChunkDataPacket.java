package fr.alessdevita.valtherion.network.packet.world;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.block.BlockType;

import java.io.*;
import java.util.*;

/**
 * Paquet de données de chunk dans Valtherion.
 * Utilisé pour charger et synchroniser les chunks entre le serveur et les clients.
 */
public class ChunkDataPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 3002;

    // Coordonnées du chunk (x, z)
    private int chunkX, chunkZ;

    // Largeur du chunk (en blocs)
    private int chunkWidth;

    // Hauteur du chunk (en blocs)
    private int chunkHeight;

    // Profondeur du chunk (en blocs)
    private int chunkDepth;

    // Données des blocs du chunk (position -> type de bloc)
    private final Map<String, BlockType> blockData;

    // Données des entités du chunk (ID de l'entité -> type de l'entité)
    private final Map<String, String> entityData;

    // Indique si le chunk est complet
    private boolean isFullChunk;

    // Type de compression (NONE, GZIP, LZ4)
    private CompressionType compressionType;

    public String getX() {
        return String.valueOf(chunkX);
    }

    public String getZ() {
        return String.valueOf(chunkZ);
    }

    // Énumération des types de compression
    public enum CompressionType {
        NONE,   // Pas de compression
        GZIP,   // Compression GZIP
        LZ4     // Compression LZ4
    }

    /**
     * Constructeur du paquet de données de chunk.
     */
    public ChunkDataPacket() {
        super(PACKET_ID);
        this.blockData = new HashMap<>();
        this.entityData = new HashMap<>();
        this.chunkWidth = 16; // Largeur par défaut
        this.chunkHeight = 256; // Hauteur par défaut
        this.chunkDepth = 16; // Profondeur par défaut
        this.isFullChunk = true;
        this.compressionType = CompressionType.NONE;
    }

    /**
     * Constructeur du paquet de données de chunk avec des coordonnées.
     * @param chunkX Coordonnée X du chunk.
     * @param chunkZ Coordonnée Z du chunk.
     */
    public ChunkDataPacket(int chunkX, int chunkZ) {
        this();
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /**
     * Ajoute un bloc au chunk.
     * @param x Position X du bloc dans le chunk.
     * @param y Position Y du bloc dans le chunk.
     * @param z Position Z du bloc dans le chunk.
     * @param blockType Type de bloc.
     */
    public void addBlock(int x, int y, int z, BlockType blockType) {
        String positionKey = x + "," + y + "," + z;
        blockData.put(positionKey, blockType);
    }

    /**
     * Ajoute une entité au chunk.
     * @param entityId ID de l'entité.
     * @param entityType Type de l'entité.
     */
    public void addEntity(String entityId, String entityType) {
        entityData.put(entityId, entityType);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire les coordonnées du chunk
        dataStream.writeInt(chunkX);
        dataStream.writeInt(chunkZ);

        // Écrire les dimensions du chunk
        dataStream.writeInt(chunkWidth);
        dataStream.writeInt(chunkHeight);
        dataStream.writeInt(chunkDepth);

        // Écrire le flag de chunk complet
        dataStream.writeBoolean(isFullChunk);

        // Écrire le type de compression
        dataStream.writeByte(compressionType.ordinal());

        // Écrire le nombre de blocs
        dataStream.writeInt(blockData.size());
        for (Map.Entry<String, BlockType> entry : blockData.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeUTF(entry.getValue() != null ? entry.getValue().name() : "");
        }

        // Écrire le nombre d'entités
        dataStream.writeInt(entityData.size());
        for (Map.Entry<String, String> entry : entityData.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeUTF(entry.getValue() != null ? entry.getValue() : "");
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire les coordonnées du chunk
        this.chunkX = dataStream.readInt();
        this.chunkZ = dataStream.readInt();

        // Lire les dimensions du chunk
        this.chunkWidth = dataStream.readInt();
        this.chunkHeight = dataStream.readInt();
        this.chunkDepth = dataStream.readInt();

        // Lire le flag de chunk complet
        this.isFullChunk = dataStream.readBoolean();

        // Lire le type de compression
        this.compressionType = CompressionType.values()[dataStream.readByte()];

        // Lire les blocs
        int blockCount = dataStream.readInt();
        for (int i = 0; i < blockCount; i++) {
            String positionKey = dataStream.readUTF();
            String blockTypeName = dataStream.readUTF();
            BlockType blockType = blockTypeName.isEmpty() ? null : BlockType.valueOf(blockTypeName);
            blockData.put(positionKey, blockType);
        }

        // Lire les entités
        int entityCount = dataStream.readInt();
        for (int i = 0; i < entityCount; i++) {
            String entityId = dataStream.readUTF();
            String entityType = dataStream.readUTF();
            entityData.put(entityId, entityType);
        }
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite les données du chunk
        System.out.println("Données du chunk reçues pour : (" + chunkX + ", " + chunkZ + ")");

        // Ici, tu peux charger le chunk côté client
        // Exemple :
        // Chunk chunk = world.getOrCreateChunk(chunkX, chunkZ);
        // for (Map.Entry<String, BlockType> entry : blockData.entrySet()) {
        //     String[] coords = entry.getKey().split(",");
        //     int x = Integer.parseInt(coords[0]);
        //     int y = Integer.parseInt(coords[1]);
        //     int z = Integer.parseInt(coords[2]);
        //     chunk.setBlock(x, y, z, entry.getValue());
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur ne traite pas ce paquet (il l'envoie)
    }

    // --- Getters et Setters ---

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public int getChunkWidth() {
        return chunkWidth;
    }

    public void setChunkWidth(int chunkWidth) {
        this.chunkWidth = chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }

    public void setChunkHeight(int chunkHeight) {
        this.chunkHeight = chunkHeight;
    }

    public int getChunkDepth() {
        return chunkDepth;
    }

    public void setChunkDepth(int chunkDepth) {
        this.chunkDepth = chunkDepth;
    }

    public Map<String, BlockType> getBlockData() {
        return blockData;
    }

    public Map<String, String> getEntityData() {
        return entityData;
    }

    public boolean isFullChunk() {
        return isFullChunk;
    }

    public void setFullChunk(boolean isFullChunk) {
        this.isFullChunk = isFullChunk;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    /**
     * Retourne une représentation textuelle du paquet de données de chunk.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ChunkDataPacket{" +
                "chunkX=" + chunkX +
                ", chunkZ=" + chunkZ +
                ", chunkWidth=" + chunkWidth +
                ", chunkHeight=" + chunkHeight +
                ", chunkDepth=" + chunkDepth +
                ", blockCount=" + blockData.size() +
                ", entityCount=" + entityData.size() +
                ", isFullChunk=" + isFullChunk +
                ", compressionType=" + compressionType +
                '}';
    }
}