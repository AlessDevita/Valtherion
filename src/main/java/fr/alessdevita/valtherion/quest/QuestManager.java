package fr.alessdevita.valtherion.quest;

import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

/**
 * Classe pour gérer les quêtes dans Valtherion.
 * Permet de créer, modifier, et supprimer des quêtes, ainsi que de les attribuer aux joueurs.
 */
public class QuestManager {

    // Map des quêtes (ID -> quête)
    private final Map<String, Quest> quests;

    // Map des quêtes par type
    private final Map<QuestType, List<Quest>> questsByType;

    // Map des quêtes par lieu
    private final Map<String, List<Quest>> questsByLocation;

    // Monde associé au gestionnaire de quêtes
    private World world;

    /**
     * Constructeur du gestionnaire de quêtes.
     */
    public QuestManager() {
        this.quests = new HashMap<>();
        this.questsByType = new HashMap<>();
        this.questsByLocation = new HashMap<>();
    }

    /**
     * Initialise le gestionnaire de quêtes.
     * @param world Monde associé.
     */
    public void init(World world) {
        this.world = world;

        // Initialiser les quêtes par défaut
        initDefaultQuests();
    }

    /**
     * Initialise les quêtes par défaut.
     */
    private void initDefaultQuests() {
        // Exemple : Quête de collecte
        Quest collectQuest = new Quest(
                "collect_wood",
                "Récolter du bois",
                "Récolter 10 bûches pour construire une maison.",
                QuestType.COLLECT
        );
        collectQuest.addObjective(new QuestObjective(
                QuestObjective.QuestObjectiveType.COLLECT_ITEM,
                "Récolter 10 bûches",
                "LOG",
                10
        ));
        collectQuest.getReward().addExperience(100);
        collectQuest.getReward().addGold(50);
        registerQuest(collectQuest);

        // Exemple : Quête d'élimination
        Quest killQuest = new Quest(
                "kill_zombies",
                "Tuer des zombies",
                "Tuer 5 zombies pour nettoyer la zone.",
                QuestType.KILL
        );
        killQuest.addObjective(new QuestObjective(
                QuestObjective.QuestObjectiveType.KILL_ENTITY,
                "Tuer 5 zombies",
                "ZOMBIE",
                5
        ));
        killQuest.getReward().addExperience(200);
        killQuest.getReward().addGold(100);
        registerQuest(killQuest);

        // Exemple : Quête d'exploration
        Quest exploreQuest = new Quest(
                "explore_forest",
                "Explorer la forêt",
                "Explorer la forêt interdite pour découvrir ses secrets.",
                QuestType.EXPLORE
        );
        exploreQuest.addObjective(new QuestObjective(
                QuestObjective.QuestObjectiveType.EXPLORE_AREA,
                "Explorer la forêt interdite",
                "FOREST",
                1
        ));
        exploreQuest.getReward().addExperience(150);
        exploreQuest.getReward().addGold(75);
        registerQuest(exploreQuest);
    }

    /**
     * Enregistre une quête.
     * @param quest Quête à enregistrer.
     */
    public void registerQuest(Quest quest) {
        if (quest == null) {
            return;
        }

        quests.put(quest.getId(), quest);

        // Ajouter la quête à la map des quêtes par type
        questsByType.computeIfAbsent(quest.getType(), k -> new ArrayList<>()).add(quest);

        // Ajouter la quête à la map des quêtes par lieu
        if (!quest.getLocation().isEmpty()) {
            questsByLocation.computeIfAbsent(quest.getLocation(), k -> new ArrayList<>()).add(quest);
        }
    }

    /**
     * Retire une quête.
     * @param questId ID de la quête à retirer.
     */
    public void unregisterQuest(String questId) {
        Quest quest = quests.get(questId);
        if (quest != null) {
            quests.remove(questId);

            // Retirer la quête de la map des quêtes par type
            List<Quest> typeQuests = questsByType.get(quest.getType());
            if (typeQuests != null) {
                typeQuests.remove(quest);
            }

            // Retirer la quête de la map des quêtes par lieu
            if (!quest.getLocation().isEmpty()) {
                List<Quest> locationQuests = questsByLocation.get(quest.getLocation());
                if (locationQuests != null) {
                    locationQuests.remove(quest);
                }
            }
        }
    }

    /**
     * Récupère une quête par son ID.
     * @param questId ID de la quête.
     * @return Quête ou null si non trouvée.
     */
    public Quest getQuest(String questId) {
        return quests.get(questId);
    }

    /**
     * Récupère toutes les quêtes.
     * @return Map des quêtes.
     */
    public Map<String, Quest> getQuests() {
        return quests;
    }

    /**
     * Récupère les quêtes par type.
     * @param type Type des quêtes.
     * @return Liste des quêtes.
     */
    public List<Quest> getQuestsByType(QuestType type) {
        return questsByType.getOrDefault(type, new ArrayList<>());
    }

    /**
     * Récupère les quêtes par lieu.
     * @param location Lieu des quêtes.
     * @return Liste des quêtes.
     */
    public List<Quest> getQuestsByLocation(String location) {
        return questsByLocation.getOrDefault(location, new ArrayList<>());
    }

    /**
     * Récupère les quêtes disponibles pour un joueur.
     * @param player Joueur.
     * @return Liste des quêtes disponibles.
     */
    public List<Quest> getAvailableQuests(Player player) {
        List<Quest> availableQuests = new ArrayList<>();

        for (Quest quest : quests.values()) {
            if (quest.getState() == QuestState.NOT_STARTED ||
                    quest.getState() == QuestState.AVAILABLE) {

                // Vérifier si le joueur a le niveau requis
                if (player.getLevel() >= quest.getRequiredLevel()) {

                    // Vérifier si le joueur a complété les quêtes requises
                    boolean hasRequiredQuests = true;
                    for (String requiredQuestId : quest.getRequiredQuests()) {
                        if (!player.getQuestLog().hasCompletedQuest(requiredQuestId)) {
                            hasRequiredQuests = false;
                            break;
                        }
                    }

                    if (hasRequiredQuests) {
                        availableQuests.add(quest);
                    }
                }
            }
        }

        return availableQuests;
    }

    /**
     * Attribue une quête à un joueur.
     * @param questId ID de la quête.
     * @param player Joueur.
     */
    public void assignQuest(String questId, Player player) {
        Quest quest = getQuest(questId);
        if (quest != null) {
            player.getQuestLog().addQuest(quest);
            quest.start(player);
        }
    }

    /**
     * Nettoie les ressources du gestionnaire de quêtes.
     */
    public void cleanup() {
        quests.clear();
        questsByType.clear();
        questsByLocation.clear();
    }
}