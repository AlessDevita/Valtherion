package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.QuestComponent;
import fr.alessdevita.valtherion.ecs.components.InventoryComponent;
import fr.alessdevita.valtherion.quest.Quest;
import fr.alessdevita.valtherion.quest.QuestManager;
import fr.alessdevita.valtherion.quest.QuestState;

import java.util.*;

/**
 * Système pour gérer les quêtes des entités dans Valtherion.
 * Met à jour les quêtes, vérifie les objectifs, et applique les récompenses.
 */
public class QuestSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Gestionnaire des quêtes
    private QuestManager questManager;

    // Liste des entités avec un composant de quête
    private List<Entity> questEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    // Liste des observateurs de quête
    private final List<QuestListener> listeners;

    /**
     * Constructeur du système de quêtes.
     */
    public QuestSystem() {
        this.questEntities = new ArrayList<>();
        this.isEnabled = true;
        this.listeners = new ArrayList<>();
    }

    /**
     * Constructeur du système de quêtes avec un gestionnaire de quêtes.
     * @param questManager Gestionnaire des quêtes.
     */
    public QuestSystem(QuestManager questManager) {
        this();
        this.questManager = questManager;
    }

    @Override
    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    @Override
    public ECSManager getECSManager() {
        return ecsManager;
    }

    @Override
    public void init() {
        if (questManager == null) {
            questManager = new QuestManager();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null || questManager == null) {
            return;
        }

        // Récupérer les entités avec un composant de quête
        questEntities = ecsManager.getEntitiesWithComponent(QuestComponent.class);

        // Mettre à jour les quêtes de chaque entité
        for (Entity entity : questEntities) {
            updateEntityQuests(entity, deltaTime);
        }
    }

    /**
     * Met à jour les quêtes d'une entité.
     * @param entity Entité à mettre à jour.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour.
     */
    private void updateEntityQuests(Entity entity, float deltaTime) {
        QuestComponent questComponent = entity.getComponent(QuestComponent.class);
        if (questComponent == null) {
            return;
        }

        // Mettre à jour chaque quête de l'entité
        for (Quest quest : questComponent.getQuests()) {
            if (quest != null) {
                quest.update(deltaTime);

                // Vérifier si la quête est complétée
                if (quest.getState() == QuestState.COMPLETED) {
                    applyQuestReward(entity, quest);
                    notifyQuestListeners(entity, quest);
                }
            }
        }
    }

    /**
     * Applique les récompenses d'une quête à une entité.
     * @param entity Entité à qui appliquer les récompenses.
     * @param quest Quête complétée.
     */
    private void applyQuestReward(Entity entity, Quest quest) {
        // Vérifier si l'entité a un composant d'inventaire
        if (entity.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
            // Appliquer les récompenses à l'inventaire
            // Note: Il faut adapter cette partie selon ton implémentation de QuestReward
            // Exemple: quest.getReward().applyReward(inventoryComponent.getInventory());
            java.lang.System.out.println("Récompenses appliquées pour la quête : " + quest.getName());
        }
    }

    /**
     * Ajoute une quête à une entité.
     * @param entityId ID de l'entité.
     * @param quest Quête à ajouter.
     */
    public void addQuestToEntity(String entityId, Quest quest) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent((Class<? extends Component>) QuestComponent.class)) {
            QuestComponent questComponent = entity.getComponent(QuestComponent.class);
            questComponent.addQuest(quest);
            quest.start(null); // Note: Passer le joueur si nécessaire
            notifyQuestListeners(entity, quest);
        }
    }

    /**
     * Retire une quête d'une entité.
     * @param entityId ID de l'entité.
     * @param questId ID de la quête à retirer.
     */
    public void removeQuestFromEntity(String entityId, String questId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent((Class<? extends Component>) QuestComponent.class)) {
            QuestComponent questComponent = entity.getComponent(QuestComponent.class);
            questComponent.removeQuest(questId);
            notifyQuestListeners(entity, null);
        }
    }

    /**
     * Vérifie si une entité a une quête.
     * @param entityId ID de l'entité.
     * @param questId ID de la quête.
     * @return true si l'entité a la quête, false sinon.
     */
    public boolean hasQuest(String entityId, String questId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent((Class<? extends Component>) QuestComponent.class)) {
            QuestComponent questComponent = entity.getComponent(QuestComponent.class);
            return questComponent.hasQuest(questId);
        }
        return false;
    }

    /**
     * Notifie les observateurs des quêtes.
     * @param entity Entité associée.
     * @param quest Quête associée (peut être null).
     */
    private void notifyQuestListeners(Entity entity, Quest quest) {
        for (QuestListener listener : listeners) {
            listener.onQuestUpdate(entity, quest);
        }
    }

    /**
     * Ajoute un observateur de quête.
     * @param listener Observateur à ajouter.
     */
    public void addQuestListener(QuestListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de quête.
     * @param listener Observateur à retirer.
     */
    public void removeQuestListener(QuestListener listener) {
        listeners.remove(listener);
    }

    // --- Getters et Setters ---

    public QuestManager getQuestManager() {
        return questManager;
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getQuestEntities() {
        return questEntities;
    }

    // --- Interface pour les observateurs de quête ---
    public interface QuestListener {
        void onQuestUpdate(Entity entity, Quest quest);
    }
}