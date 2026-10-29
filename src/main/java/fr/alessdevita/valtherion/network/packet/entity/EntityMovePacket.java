package fr.alessdevita.valtherion.network.packet.entity;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;

/**
 * Paquet de mouvement d'entité dans Valtherion.
 * Utilisé pour synchroniser les mouvements des entités entre le client et le serveur.
 */
public class EntityMovePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 2001;

    // ID de l'entité
    private String entityId;

    // Position de l'entité (x, y, z)
    private float x, y, z;

    // Rotation de l'entité (rx, ry, rz) en degrés
    private float rx, ry, rz;

    // Vélocité de l'entité (vx, vy, vz)
    private float vx, vy, vz;

    // Indique si le mouvement est absolu (true) ou relatif (false)
    private boolean isAbsolute;

    // Timestamp du mouvement (pour l'interpolation)
    private long timestamp;

    // Indique si l'entité est en train de sauter
    private boolean isJumping;

    // Indique si l'entité est en train de sprinter
    private boolean isSprinting;

    // Indique si l'entité est en train de se croucher
    private boolean isCrouching;

    /**
     * Constructeur du paquet de mouvement d'entité.
     */
    public EntityMovePacket() {
        super(PACKET_ID);
        this.isAbsolute = true;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructeur du paquet de mouvement d'entité avec un ID et une position.
     * @param entityId ID de l'entité.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     */
    public EntityMovePacket(String entityId, float x, float y, float z) {
        this();
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructeur du paquet de mouvement d'entité avec un ID, une position et une rotation.
     * @param entityId ID de l'entité.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     * @param rx Rotation X.
     * @param ry Rotation Y.
     * @param rz Rotation Z.
     */
    public EntityMovePacket(String entityId, float x, float y, float z, float rx, float ry, float rz) {
        this(entityId, x, y, z);
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    /**
     * Constructeur du paquet de mouvement d'entité avec un ID, une position, une rotation et une vélocité.
     * @param entityId ID de l'entité.
     * @param x Position X.
     * @param y Position Y.
     * @param z Position Z.
     * @param rx Rotation X.
     * @param ry Rotation Y.
     * @param rz Rotation Z.
     * @param vx Vélocité X.
     * @param vy Vélocité Y.
     * @param vz Vélocité Z.
     */
    public EntityMovePacket(String entityId, float x, float y, float z, float rx, float ry, float rz, float vx, float vy, float vz) {
        this(entityId, x, y, z, rx, ry, rz);
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public EntityMovePacket(int id, float x, float y, float z, float rotation, float rotation1, float rotation2) {
        super();
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID de l'entité
        dataStream.writeUTF(entityId != null ? entityId : "");

        // Écrire la position
        dataStream.writeFloat(x);
        dataStream.writeFloat(y);
        dataStream.writeFloat(z);

        // Écrire la rotation
        dataStream.writeFloat(rx);
        dataStream.writeFloat(ry);
        dataStream.writeFloat(rz);

        // Écrire la vélocité
        dataStream.writeFloat(vx);
        dataStream.writeFloat(vy);
        dataStream.writeFloat(vz);

        // Écrire le flag de mouvement absolu/relatif
        dataStream.writeBoolean(isAbsolute);

        // Écrire le timestamp
        dataStream.writeLong(timestamp);

        // Écrire les flags d'état (saut, sprint, crouch)
        byte stateFlags = 0;
        if (isJumping) {
            stateFlags |= 0x01;
        }
        if (isSprinting) {
            stateFlags |= 0x02;
        }
        if (isCrouching) {
            stateFlags |= 0x04;
        }
        dataStream.writeByte(stateFlags);
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID de l'entité
        this.entityId = dataStream.readUTF();

        // Lire la position
        this.x = dataStream.readFloat();
        this.y = dataStream.readFloat();
        this.z = dataStream.readFloat();

        // Lire la rotation
        this.rx = dataStream.readFloat();
        this.ry = dataStream.readFloat();
        this.rz = dataStream.readFloat();

        // Lire la vélocité
        this.vx = dataStream.readFloat();
        this.vy = dataStream.readFloat();
        this.vz = dataStream.readFloat();

        // Lire le flag de mouvement absolu/relatif
        this.isAbsolute = dataStream.readBoolean();

        // Lire le timestamp
        this.timestamp = dataStream.readLong();

        // Lire les flags d'état
        byte stateFlags = dataStream.readByte();
        this.isJumping = (stateFlags & 0x01) != 0;
        this.isSprinting = (stateFlags & 0x02) != 0;
        this.isCrouching = (stateFlags & 0x04) != 0;
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite le mouvement de l'entité
        // Exemple : Mettre à jour la position de l'entité locale
        System.out.println("Mouvement de l'entité " + entityId + " reçu : (" + x + ", " + y + ", " + z + ")");

        // Ici, tu peux mettre à jour la position de l'entité côté client
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null) {
        //     TransformComponent transform = entity.getComponent(TransformComponent.class);
        //     if (transform != null) {
        //         transform.setPosition(x, y, z);
        //         transform.setRotation(rx, ry, rz);
        //     }
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite le mouvement de l'entité
        System.out.println("Mouvement de l'entité " + entityId + " reçu du client : (" + x + ", " + y + ", " + z + ")");

        // Ici, tu peux mettre à jour la position de l'entité côté serveur
        // et rediffuser le paquet aux autres clients
        // Exemple :
        // Entity entity = ecsManager.getEntity(entityId);
        // if (entity != null) {
        //     TransformComponent transform = entity.getComponent(TransformComponent.class);
        //     if (transform != null) {
        //         transform.setPosition(x, y, z);
        //         transform.setRotation(rx, ry, rz);
        //     }
        // }

        // Rediffuser le mouvement aux autres clients
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion du mouvement : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
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

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getVz() {
        return vz;
    }

    public void setVz(float vz) {
        this.vz = vz;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public void setAbsolute(boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public void setSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public boolean isCrouching() {
        return isCrouching;
    }

    public void setCrouching(boolean isCrouching) {
        this.isCrouching = isCrouching;
    }

    /**
     * Retourne une représentation textuelle du paquet de mouvement d'entité.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityMovePacket{" +
                "entityId='" + entityId + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", rx=" + rx +
                ", ry=" + ry +
                ", rz=" + rz +
                ", isAbsolute=" + isAbsolute +
                ", isJumping=" + isJumping +
                ", isSprinting=" + isSprinting +
                ", isCrouching=" + isCrouching +
                '}';
    }
}