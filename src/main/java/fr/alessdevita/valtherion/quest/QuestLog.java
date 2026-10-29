package fr.alessdevita.valtherion.quest;

import fr.alessdevita.valtherion.entity.living.Player;

import java.util.*;

/**
 * Classe pour le journal des quêtes d'un joueur dans Valtherion.
 * Gère les quêtes en cours, complétées, et échouées pour un joueur.
 */
public class QuestLog {

    // Joueur associé au journal des quêtes
    private final Player player;

    // Liste des quêtes en cours
    private final List<Quest> activeQuests;

    // Liste des quêtes complétées
    private final List<Quest> completedQuests;

    // Liste des quêtes échouées
    private final List<Quest> failedQuests;

    // Liste des quêtes abandonnées
    private final List<Quest> abandonedQuests;

    // Quête actuellement sélectionnée
    private Quest selectedQuest;

    // Liste des observateurs du journal des quêtes
    private final List<QuestLogListener> listeners;

    /**
     * Constructeur du journal des quêtes.
     * @param player Joueur associé.
     */
    public QuestLog(Player player) {
        this.player = player;
        this.activeQuests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.failedQuests = new ArrayList<>();
        this.abandonedQuests = new ArrayList<>();
        this.selectedQuest = null;
        this.listeners = new ArrayList<>();
    }

    /**
     * Ajoute une quête au journal.
     * @param quest Quête à ajouter.
     */
    public void addQuest(Quest quest) {
        if (quest == null) {
            return;
        }

        // Vérifier si la quête est déjà dans le journal
        if (hasQuest(quest.getId())) {
            return;
        }

        // Ajouter la quête à la liste des quêtes en cours
        activeQuests.add(quest);

        // Définir la quête sélectionnée si aucune n'est sélectionnée
        if (selectedQuest == null) {
            selectedQuest = quest;
        }

        // Notifier les observateurs
        notifyListeners();
    }

    /**
     * Retire une quête du journal.
     * @param questId ID de la quête à retirer.
     */
    public void removeQuest(String questId) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            activeQuests.remove(quest);

            // Si la quête retirée était sélectionnée, désélectionner
            if (selectedQuest != null && selectedQuest.getId().equals(questId)) {
                selectedQuest = null;
            }

            // Notifier les observateurs
            notifyListeners();
        }
    }

    /**
     * Complète une quête.
     * @param questId ID de la quête à compléter.
     */
    public void completeQuest(String questId) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            quest.complete();
            activeQuests.remove(quest);
            completedQuests.add(quest);

            // Appliquer les récompenses
            quest.applyReward(player);

            // Si la quête complétée était sélectionnée, désélectionner
            if (selectedQuest != null && selectedQuest.getId().equals(questId)) {
                selectedQuest = null;
            }

            // Notifier les observateurs
            notifyListeners();
        }
    }

    /**
     * Échec d'une quête.
     * @param questId ID de la quête à échouer.
     */
    public void failQuest(String questId) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            quest.fail();
            activeQuests.remove(quest);
            failedQuests.add(quest);

            // Si la quête échouée était sélectionnée, désélectionner
            if (selectedQuest != null && selectedQuest.getId().equals(questId)) {
                selectedQuest = null;
            }

            // Notifier les observateurs
            notifyListeners();
        }
    }

    /**
     * Abandonne une quête.
     * @param questId ID de la quête à abandonner.
     */
    public void abandonQuest(String questId) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            quest.abandon();
            activeQuests.remove(quest);
            abandonedQuests.add(quest);

            // Si la quête abandonnée était sélectionnée, désélectionner
            if (selectedQuest != null && selectedQuest.getId().equals(questId)) {
                selectedQuest = null;
            }

            // Notifier les observateurs
            notifyListeners();
        }
    }

    /**
     * Vérifie si le joueur a une quête.
     * @param questId ID de la quête.
     * @return true si le joueur a la quête, false sinon.
     */
    public boolean hasQuest(String questId) {
        return getQuest(questId) != null;
    }

    /**
     * Vérifie si le joueur a complété une quête.
     * @param questId ID de la quête.
     * @return true si le joueur a complété la quête, false sinon.
     */
    public boolean hasCompletedQuest(String questId) {
        for (Quest quest : completedQuests) {
            if (quest.getId().equals(questId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère une quête par son ID.
     * @param questId ID de la quête.
     * @return Quête ou null si non trouvée.
     */
    public Quest getQuest(String questId) {
        for (Quest quest : activeQuests) {
            if (quest.getId().equals(questId)) {
                return quest;
            }
        }
        for (Quest quest : completedQuests) {
            if (quest.getId().equals(questId)) {
                return quest;
            }
        }
        for (Quest quest : failedQuests) {
            if (quest.getId().equals(questId)) {
                return quest;
            }
        }
        for (Quest quest : abandonedQuests) {
            if (quest.getId().equals(questId)) {
                return quest;
            }
        }
        return null;
    }

    /**
     * Sélectionne une quête.
     * @param questId ID de la quête à sélectionner.
     */
    public void selectQuest(String questId) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            selectedQuest = quest;
            notifyListeners();
        }
    }

    /**
     * Désélectionne la quête actuelle.
     */
    public void deselectQuest() {
        selectedQuest = null;
        notifyListeners();
    }

    /**
     * Met à jour le journal des quêtes.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        // Mettre à jour les quêtes en cours
        for (Quest quest : activeQuests) {
            quest.update(deltaTime);
        }
    }

    /**
     * Ajoute un observateur au journal des quêtes.
     * @param listener Observateur à ajouter.
     */
    public void addListener(QuestLogListener listener) {
        listeners.add(listener);
    }

    /**
     * Retire un observateur du journal des quêtes.
     * @param listener Observateur à retirer.
     */
    public void removeListener(QuestLogListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie les observateurs du journal des quêtes.
     */
    private void notifyListeners() {
        for (QuestLogListener listener : listeners) {
            listener.onQuestLogUpdate(this);
        }
    }

    // --- Getters ---

    public Player getPlayer() {
        return player;
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }

    public List<Quest> getFailedQuests() {
        return failedQuests;
    }

    public List<Quest> getAbandonedQuests() {
        return abandonedQuests;
    }

    public Quest getSelectedQuest() {
        return selectedQuest;
    }

    /**
     * Retourne une représentation textuelle du journal des quêtes.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "QuestLog{" +
                "activeQuests=" + activeQuests.size() +
                ", completedQuests=" + completedQuests.size() +
                ", failedQuests=" + failedQuests.size() +
                ", abandonedQuests=" + abandonedQuests.size() +
                '}';
    }

    // --- Interface pour les observateurs du journal des quêtes ---
    public interface QuestLogListener {
        void onQuestLogUpdate(QuestLog questLog);
    }
}