package fr.alessdevita.valtherion.network.packet.entity;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;
import java.util.*;

/**
 * Paquet d'apparition d'entité dans Valtherion.
 * Utilisé pour créer une nouvelle entité côté client ou serveur.
 */
public class EntitySpawnPacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 2002;

    // ID de l'entité
    private String entityId;

    // Type de l'entité (ex: "PLAYER", "MOB", "ITEM")
    private String entityType;

    // Position de l'entité (x, y, z)
    private float x, y, z;

    // Rotation de l'entité (rx, ry, rz) en degrés
    private float rx, ry, rz;

    // Échelle de l'entité (sx, sy, sz)
    private float sx, sy, sz;

    // Composants de l'entité (nom du composant -> données sérialisées)
    private final Map<String, byte[]> components;

    // Métadonnées de l'entité (ex: nom, santé, etc.)
    private final Map<String, String> metadata;

    /**
     * Constructeur du paquet d'apparition d'entité.
     */
    public EntitySpawnPacket() {
        super(PACKET_ID);
        this.components = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Constructeur du paquet d'apparition d'entité avec un ID, un type et une position.
     * @param entityId ID de l'entité.
     * @param entityType Type de l'entité.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     */
    public EntitySpawnPacket(String entityId, String entityType, float x, float y, float z) {
        this();
        this.entityId = entityId;
        this.entityType = entityType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sx = 1.0f;
        this.sy = 1.0f;
        this.sz = 1.0f;
    }

    /**
     * Ajoute un composant à l'entité.
     * @param componentName Nom du composant.
     * @param componentData Données sérialisées du composant.
     */
    public void addComponent(String componentName, byte[] componentData) {
        components.put(componentName, componentData);
    }

    /**
     * Ajoute une métadonnée à l'entité.
     * @param key Clé de la métadonnée.
     * @param value Valeur de la métadonnée.
     */
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID de l'entité
        dataStream.writeUTF(entityId != null ? entityId : "");

        // Écrire le type de l'entité
        dataStream.writeUTF(entityType != null ? entityType : "");

        // Écrire la position
        dataStream.writeFloat(x);
        dataStream.writeFloat(y);
        dataStream.writeFloat(z);

        // Écrire la rotation
        dataStream.writeFloat(rx);
        dataStream.writeFloat(ry);
        dataStream.writeFloat(rz);

        // Écrire l'échelle
        dataStream.writeFloat(sx);
        dataStream.writeFloat(sy);
        dataStream.writeFloat(sz);

        // Écrire le nombre de composants
        dataStream.writeInt(components.size());
        for (Map.Entry<String, byte[]> entry : components.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeInt(entry.getValue().length);
            dataStream.write(entry.getValue());
        }

        // Écrire le nombre de métadonnées
        dataStream.writeInt(metadata.size());
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeUTF(entry.getValue());
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID de l'entité
        this.entityId = dataStream.readUTF();

        // Lire le type de l'entité
        this.entityType = dataStream.readUTF();

        // Lire la position
        this.x = dataStream.readFloat();
        this.y = dataStream.readFloat();
        this.z = dataStream.readFloat();

        // Lire la rotation
        this.rx = dataStream.readFloat();
        this.ry = dataStream.readFloat();
        this.rz = dataStream.readFloat();

        // Lire l'échelle
        this.sx = dataStream.readFloat();
        this.sy = dataStream.readFloat();
        this.sz = dataStream.readFloat();

        // Lire les composants
        int componentCount = dataStream.readInt();
        for (int i = 0; i < componentCount; i++) {
            String componentName = dataStream.readUTF();
            int componentDataLength = dataStream.readInt();
            byte[] componentData = new byte[componentDataLength];
            dataStream.readFully(componentData);
            components.put(componentName, componentData);
        }

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
        // Le client traite l'apparition de l'entité
        System.out.println("Apparition de l'entité " + entityId + " (type : " + entityType +
                ") à la position (" + x + ", " + y + ", " + z + ")");

        // Ici, tu peux créer l'entité côté client avec les données du paquet
        // Exemple :
        // Entity entity = ecsManager.createEntity(entityId);
        // entity.addComponent(new TransformComponent(x, y, z, rx, ry, rz, sx, sy, sz));
        // for (Map.Entry<String, byte[]> entry : components.entrySet()) {
        //     // Désérialiser et ajouter le composant à l'entité
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite l'apparition de l'entité
        System.out.println("Apparition de l'entité " + entityId + " (type : " + entityType +
                ") reçue du client à la position (" + x + ", " + y + ", " + z + ")");

        // Ici, tu peux créer l'entité côté serveur avec les données du paquet
        // et rediffuser le paquet aux autres clients
        // Exemple :
        // Entity entity = ecsManager.createEntity(entityId);
        // entity.addComponent(new TransformComponent(x, y, z, rx, ry, rz, sx, sy, sz));
        // for (Map.Entry<String, byte[]> entry : components.entrySet()) {
        //     // Désérialiser et ajouter le composant à l'entité
        // }

        // Rediffuser l'apparition aux autres clients
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de l'apparition : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getSx() {
        return sx;
    }

    public void setSx(float sx) {
        this.sx = sx;
    }

    public float getSy() {
        return sy;
    }

    public void setSy(float sy) {
        this.sy = sy;
    }

    public float getSz() {
        return sz;
    }

    public void setSz(float sz) {
        this.sz = sz;
    }

    public Map<String, byte[]> getComponents() {
        return components;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Retourne une représentation textuelle du paquet d'apparition d'entité.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntitySpawnPacket{" +
                "entityId='" + entityId + '\'' +
                ", entityType='" + entityType + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", components=" + components.size() +
                ", metadata=" + metadata.size() +
                '}';
    }
}