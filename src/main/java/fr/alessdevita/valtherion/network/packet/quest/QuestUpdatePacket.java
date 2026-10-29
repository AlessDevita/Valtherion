package fr.alessdevita.valtherion.network.packet.quest;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;

import java.io.*;
import java.util.*;

/**
 * Paquet de mise à jour de quête dans Valtherion.
 * Utilisé pour synchroniser les quêtes entre le serveur et les clients.
 */
public class QuestUpdatePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 6001;

    // ID du joueur
    private String playerId;

    // ID de la quête
    private String questId;

    // Nom de la quête
    private String questName;

    // Type de mise à jour (START, UPDATE, COMPLETE, ABANDON)
    private UpdateType updateType;

    // État de la quête (0-100)
    private int progress;

    // Liste des objectifs de la quête (ID de l'objectif -> progression)
    private final Map<String, Integer> objectives;

    // Indique si la quête est complétée
    private boolean isCompleted;

    // Récompenses de la quête (si complétée)
    private final Map<String, Integer> rewards;

    // Énumération des types de mise à jour
    public enum UpdateType {
        START,      // Début de la quête
        UPDATE,     // Mise à jour de la quête
        COMPLETE,   // Quête complétée
        ABANDON     // Quête abandonnée
    }

    /**
     * Constructeur du paquet de mise à jour de quête.
     */
    public QuestUpdatePacket() {
        super(PACKET_ID);
        this.objectives = new HashMap<>();
        this.rewards = new HashMap<>();
    }

    /**
     * Constructeur du paquet de mise à jour de quête avec un ID de joueur et un ID de quête.
     * @param playerId ID du joueur.
     * @param questId ID de la quête.
     */
    public QuestUpdatePacket(String playerId, String questId) {
        this();
        this.playerId = playerId;
        this.questId = questId;
    }

    /**
     * Ajoute un objectif à la quête.
     * @param objectiveId ID de l'objectif.
     * @param progress Progression de l'objectif.
     */
    public void addObjective(String objectiveId, int progress) {
        objectives.put(objectiveId, progress);
    }

    /**
     * Ajoute une récompense à la quête.
     * @param rewardId ID de la récompense.
     * @param quantity Quantité de la récompense.
     */
    public void addReward(String rewardId, int quantity) {
        rewards.put(rewardId, quantity);
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire l'ID du joueur
        dataStream.writeUTF(playerId != null ? playerId : "");

        // Écrire l'ID de la quête
        dataStream.writeUTF(questId != null ? questId : "");

        // Écrire le nom de la quête
        dataStream.writeUTF(questName != null ? questName : "");

        // Écrire le type de mise à jour
        dataStream.writeByte(updateType != null ? updateType.ordinal() : 0);

        // Écrire la progression
        dataStream.writeInt(progress);

        // Écrire le flag de quête complétée
        dataStream.writeBoolean(isCompleted);

        // Écrire le nombre d'objectifs
        dataStream.writeInt(objectives.size());
        for (Map.Entry<String, Integer> entry : objectives.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeInt(entry.getValue());
        }

        // Écrire le nombre de récompenses
        dataStream.writeInt(rewards.size());
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            dataStream.writeUTF(entry.getKey());
            dataStream.writeInt(entry.getValue());
        }
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire l'ID du joueur
        this.playerId = dataStream.readUTF();

        // Lire l'ID de la quête
        this.questId = dataStream.readUTF();

        // Lire le nom de la quête
        this.questName = dataStream.readUTF();

        // Lire le type de mise à jour
        this.updateType = UpdateType.values()[dataStream.readByte()];

        // Lire la progression
        this.progress = dataStream.readInt();

        // Lire le flag de quête complétée
        this.isCompleted = dataStream.readBoolean();

        // Lire les objectifs
        int objectiveCount = dataStream.readInt();
        for (int i = 0; i < objectiveCount; i++) {
            String objectiveId = dataStream.readUTF();
            int objectiveProgress = dataStream.readInt();
            objectives.put(objectiveId, objectiveProgress);
        }

        // Lire les récompenses
        int rewardCount = dataStream.readInt();
        for (int i = 0; i < rewardCount; i++) {
            String rewardId = dataStream.readUTF();
            int rewardQuantity = dataStream.readInt();
            rewards.put(rewardId, rewardQuantity);
        }
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la mise à jour de la quête
        switch (updateType) {
            case START:
                System.out.println("Quête démarrée : " + questName);
                break;
            case UPDATE:
                System.out.println("Quête mise à jour : " + questName + " (progression : " + progress + "%)");
                break;
            case COMPLETE:
                System.out.println("Quête complétée : " + questName);
                break;
            case ABANDON:
                System.out.println("Quête abandonnée : " + questName);
                break;
        }

        // Ici, tu peux mettre à jour la quête côté client
        // Exemple :
        // Quest quest = questManager.getQuest(questId);
        // if (quest != null) {
        //     quest.setProgress(progress);
        //     if (isCompleted) {
        //         quest.complete();
        //         // Appliquer les récompenses
        //         for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
        //             inventory.addItem(entry.getKey(), entry.getValue());
        //         }
        //     }
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur traite la mise à jour de la quête
        System.out.println("Mise à jour de la quête " + questId + " pour le joueur " + playerId +
                " (type : " + updateType + ")");

        // Ici, tu peux mettre à jour la quête côté serveur
        // et rediffuser la mise à jour aux autres clients si nécessaire
        // Exemple :
        // Quest quest = questManager.getQuest(questId);
        // if (quest != null) {
        //     quest.setProgress(progress);
        //     if (isCompleted) {
        //         quest.complete();
        //     }
        // }

        // Rediffuser la mise à jour aux autres clients si nécessaire
        try {
            connection.sendPacket(this);
        } catch (Exception e) {
            System.err.println("Erreur lors de la rediffusion de la mise à jour de la quête : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(100, progress));
    }

    public Map<String, Integer> getObjectives() {
        return objectives;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Map<String, Integer> getRewards() {
        return rewards;
    }

    /**
     * Retourne une représentation textuelle du paquet de mise à jour de quête.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "QuestUpdatePacket{" +
                "playerId='" + playerId + '\'' +
                ", questId='" + questId + '\'' +
                ", questName='" + questName + '\'' +
                ", updateType=" + updateType +
                ", progress=" + progress +
                ", isCompleted=" + isCompleted +
                ", objectives=" + objectives.size() +
                ", rewards=" + rewards.size() +
                '}';
    }
}