package fr.alessdevita.valtherion.quest;

import fr.alessdevita.valtherion.ai.goals.Goal;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.*;

/**
 * Classe de base pour les quêtes dans Valtherion.
 * Définit une quête avec ses objectifs, ses récompenses, et son état.
 */
public class Quest {

    // ID unique de la quête
    private final String id;

    // Nom de la quête
    private String name;

    // Description de la quête
    private String description;

    // Type de la quête
    private QuestType type;

    // État de la quête
    private QuestState state;

    // Liste des objectifs de la quête
    private final List<QuestObjective> objectives;

    // Récompenses de la quête
    private QuestReward reward;

    // Niveau requis pour commencer la quête
    private int requiredLevel;

    // PNJ qui donne la quête
    private String giverNPC;

    // Lieu où la quête est disponible
    private String location;

    // Liste des quêtes nécessaires pour débloquer cette quête
    private final List<String> requiredQuests;

    // Indique si la quête est répétable
    private boolean isRepeatable;

    // Temps limite pour compléter la quête (en secondes, 0 = pas de limite)
    private int timeLimit;

    // Temps écoulé depuis le début de la quête (en secondes)
    private int elapsedTime;

    // Liste des observateurs de la quête
    private final List<QuestListener> listeners;

    /**
     * Constructeur de la quête.
     * @param id ID unique de la quête.
     * @param name Nom de la quête.
     * @param description Description de la quête.
     * @param type Type de la quête.
     */
    public Quest(String id, String name, String description, QuestType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.state = QuestState.NOT_STARTED;
        this.objectives = new ArrayList<>();
        this.reward = new QuestReward();
        this.requiredLevel = 1;
        this.giverNPC = "";
        this.location = "";
        this.requiredQuests = new ArrayList<>();
        this.isRepeatable = false;
        this.timeLimit = 0;
        this.elapsedTime = 0;
        this.listeners = new ArrayList<>();
    }

    /**
     * Initialise la quête.
     */
    public void init() {
        // Initialiser les objectifs
        for (QuestObjective objective : objectives) {
            objective.reset();
        }

        // Réinitialiser l'état et le temps
        state = QuestState.NOT_STARTED;
        elapsedTime = 0;
    }

    /**
     * Démarre la quête.
     * @param player Joueur qui commence la quête.
     */
    public void start(Player player) {
        if (state != QuestState.NOT_STARTED && state != QuestState.AVAILABLE) {
            return;
        }

        // Vérifier si le joueur a le niveau requis
        if (player.getLevel() < requiredLevel) {
            System.out.println("Niveau insuffisant pour commencer la quête : " + name);
            return;
        }

        // Vérifier si le joueur a complété les quêtes requises
        for (String questId : requiredQuests) {
            if (!player.getQuestLog().hasCompletedQuest(questId)) {
                System.out.println("Quête requise non complétée : " + questId);
                return;
            }
        }

        // Démarrer la quête
        state = QuestState.IN_PROGRESS;
        elapsedTime = 0;

        // Notifier les observateurs
        notifyListeners();

        System.out.println("Quête démarrée : " + name);
    }

    /**
     * Met à jour la quête.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (state != QuestState.IN_PROGRESS) {
            return;
        }

        // Mettre à jour le temps écoulé
        elapsedTime += deltaTime;

        // Vérifier si le temps limite est dépassé
        if (timeLimit > 0 && elapsedTime >= timeLimit) {
            fail();
            return;
        }

        // Mettre à jour les objectifs
        for (QuestObjective objective : objectives) {
            objective.update(null); // À implémenter : passer le monde ou le joueur
        }

        // Vérifier si tous les objectifs sont complétés
        if (areAllObjectivesCompleted()) {
            complete();
        }
    }

    /**
     * Vérifie si tous les objectifs sont complétés.
     * @return true si tous les objectifs sont complétés, false sinon.
     */
    private boolean areAllObjectivesCompleted() {
        for (QuestObjective objective : objectives) {
            if (!objective.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Complète la quête.
     */
    public void complete() {
        if (state != QuestState.IN_PROGRESS) {
            return;
        }

        state = QuestState.COMPLETED;

        // Notifier les observateurs
        notifyListeners();

        System.out.println("Quête complétée : " + name);
    }

    /**
     * Échec de la quête.
     */
    public void fail() {
        if (state != QuestState.IN_PROGRESS) {
            return;
        }

        state = QuestState.FAILED;

        // Notifier les observateurs
        notifyListeners();

        System.out.println("Quête échouée : " + name);
    }

    /**
     * Abandonne la quête.
     */
    public void abandon() {
        if (state != QuestState.IN_PROGRESS) {
            return;
        }

        state = QuestState.ABANDONED;

        // Notifier les observateurs
        notifyListeners();

        System.out.println("Quête abandonnée : " + name);
    }

    /**
     * Réinitialise la quête.
     */
    public void reset() {
        init();
    }

    /**
     * Ajoute un objectif à la quête.
     * @param objective Objectif à ajouter.
     */
    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    /**
     * Ajoute une récompense à la quête.
     * @param reward Récompense à ajouter.
     */
    public void setReward(QuestReward reward) {
        this.reward = reward;
    }

    /**
     * Applique les récompenses de la quête à un joueur.
     * @param player Joueur à qui appliquer les récompenses.
     */
    public void applyReward(Player player) {
        if (state == QuestState.COMPLETED) {
            reward.applyReward(player);
        }
    }

    /**
     * Ajoute un observateur à la quête.
     * @param listener Observateur à ajouter.
     */
    public void addListener(QuestListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur de la quête.
     * @param listener Observateur à retirer.
     */
    public void removeListener(QuestListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs de la quête.
     */
    private void notifyListeners() {
        for (QuestListener listener : listeners) {
            listener.onQuestUpdate(this);
        }
    }

    // --- Getters et Setters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public QuestState getState() {
        return state;
    }

    public void setState(QuestState state) {
        this.state = state;
    }

    public List<QuestObjective> getObjectives() {
        return objectives;
    }

    public QuestReward getReward() {
        return reward;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public String getGiverNPC() {
        return giverNPC;
    }

    public void setGiverNPC(String giverNPC) {
        this.giverNPC = giverNPC;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getRequiredQuests() {
        return requiredQuests;
    }

    public void addRequiredQuest(String questId) {
        requiredQuests.add(questId);
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * Retourne la progression globale de la quête en pourcentage.
     * @return Pourcentage de progression.
     */
    public float getProgress() {
        if (objectives.isEmpty()) {
            return 0.0f;
        }

        float totalProgress = 0.0f;
        for (QuestObjective objective : objectives) {
            totalProgress += objective.getProgress();
        }

        return totalProgress / objectives.size();
    }

    /**
     * Retourne une représentation textuelle de la quête.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Quest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", progress=" + (getProgress() * 100) + "%" +
                ", objectives=" + objectives.size() +
                '}';
    }

    public String getCurrencyReward() {
        return reward.getCurrencyReward();
    }

    public int getRewardAmount() {
        return reward.getRewardAmount();
    }

    public int getExperienceReward() {
        return reward.getExperience();
    }

    // --- Interface pour les observateurs de quête ---
    public interface QuestListener {
        void onQuestUpdate(Quest quest);
    }
}