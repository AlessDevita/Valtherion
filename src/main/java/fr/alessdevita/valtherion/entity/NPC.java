package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.economy.Shop;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.quest.Quest;
import fr.alessdevita.valtherion.world.World;

import java.util.ArrayList;
import java.util.List;

public class NPC extends LivingEntity {

    // Nom du PNJ
    private String npcName;

    // Description du PNJ
    private String description;

    // Type de PNJ (ex: MARCHAND, QUEST_GIVER, VILLAGER, ENNEMY, etc.)
    private NPCType npcType;

    // Dialogues du PNJ
    private final List<Dialogue> dialogues;

    // Quêtes disponibles auprès du PNJ
    private final List<Quest> availableQuests;

    // Quêtes en cours pour le PNJ
    private final List<Quest> activeQuests;

    // Boutique du PNJ (si c'est un marchand)
    private Shop shop;

    // IA du PNJ
    private EntityAI ai;

    // Cible actuelle du PNJ (pour les PNJ hostiles ou amicaux)
    private LivingEntity target;

    // État du PNJ (ex: IDLE, TALKING, TRADING, etc.)
    private NPCState currentState;

    // Niveau du PNJ (pour ajuster les récompenses ou les dialogues)
    private int level;

    // Énumération des types de PNJ
    public enum NPCType {
        VILLAGER,      // Villageois
        MERCHANT,      // Marchand
        QUEST_GIVER,   // Donneur de quêtes
        BLACKSMITH,    // Forgeron
        ENNEMY,        // Ennemi (PNJ hostile)
        FRIENDLY,      // Amiable
        NEUTRAL        // Neutre
    }

    // Énumération des états du PNJ
    public enum NPCState {
        IDLE,       // Inactif
        WANDER,     // Errance aléatoire
        TALKING,    // En train de parler
        TRADING,    // En train de commercer
        ATTACKING,  // En train d'attaquer
        FLEEING,    // En train de fuir
        GIVING_QUEST // En train de donner une quête
    }

    /**
     * Constructeur du PNJ.
     * @param world Monde dans lequel le PNJ existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param npcName Nom du PNJ.
     * @param npcType Type de PNJ.
     */
    public NPC(World world, double x, double y, double z, String npcName, NPCType npcType) {
        super(EntityType.NPC, world, x, y, z);
        this.npcName = npcName;
        this.npcType = npcType;
        this.description = "Un PNJ de Valtherion.";
        this.dialogues = new ArrayList<>();
        this.availableQuests = new ArrayList<>();
        this.activeQuests = new ArrayList<>();
        this.currentState = NPCState.IDLE;
        this.level = 1;

        // Initialiser l'IA du PNJ
        Pathfinder pathfinder = new Pathfinder(world);
        this.ai = new EntityAI(this, world, pathfinder);
        this.ai.setDetectionRange(10.0f);
        this.ai.setSpeed(0.05f);
    }

    /**
     * Met à jour le PNJ.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour l'IA du PNJ
        ai.update();

        // Mettre à jour l'état du PNJ
        updateState();
    }

    @Override
    protected void levelUp() {
    }

    /**
     * Met à jour l'état du PNJ.
     */
    private void updateState() {
        switch (currentState) {
            case IDLE:
                // Comportement en mode IDLE
                break;
            case WANDER:
                // Comportement en mode WANDER
                break;
            case TALKING:
                // Comportement en mode TALKING
                break;
            case TRADING:
                // Comportement en mode TRADING
                break;
            case ATTACKING:
                // Comportement en mode ATTACKING
                if (target != null && !target.isAlive()) {
                    currentState = NPCState.IDLE;
                }
                break;
            case FLEEING:
                // Comportement en mode FLEEING
                break;
            case GIVING_QUEST:
                // Comportement en mode GIVING_QUEST
                break;
        }
    }

    /**
     * Ajoute un dialogue au PNJ.
     * @param dialogue Dialogue à ajouter.
     */
    public void addDialogue(Dialogue dialogue) {
        if (dialogue != null) {
            dialogues.add(dialogue);
        }
    }

    /**
     * Commence un dialogue avec le PNJ.
     * @param player Joueur avec lequel le PNJ parle.
     */
    public void startDialogue(Player player) {
        if (dialogues.isEmpty()) {
            return;
        }

        currentState = NPCState.TALKING;

        // Commencer le premier dialogue
        Dialogue dialogue = dialogues.get(0);
        dialogue.start(this, player);
    }

    /**
     * Ajoute une quête disponible au PNJ.
     * @param quest Quête à ajouter.
     */
    public void addAvailableQuest(Quest quest) {
        if (quest != null) {
            availableQuests.add(quest);
        }
    }

    /**
     * Donne une quête au joueur.
     * @param player Joueur à qui donner la quête.
     * @param questIndex Index de la quête dans la liste des quêtes disponibles.
     */
    public void giveQuest(Player player, int questIndex) {
        if (questIndex < 0 || questIndex >= availableQuests.size()) {
            return;
        }

        Quest quest = availableQuests.get(questIndex);
        if (quest != null) {
            currentState = NPCState.GIVING_QUEST;
            player.addQuest(quest);
            activeQuests.add(quest);
            availableQuests.remove(quest);
        }
    }

    /**
     * Définit la boutique du PNJ.
     * @param shop Boutique à définir.
     */
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    /**
     * Ouvre la boutique du PNJ pour un joueur.
     * @param player Joueur qui ouvre la boutique.
     */
    public void openShop(Player player) {
        if (shop != null) {
            currentState = NPCState.TRADING;
            shop.openForPlayer(player);
        }
    }

    /**
     * Définit la cible du PNJ.
     * @param target Cible à définir.
     */
    public void setTarget(LivingEntity target) {
        this.target = target;
        if (target != null) {
            if (npcType == NPCType.ENNEMY) {
                currentState = NPCState.ATTACKING;
                ai.setTarget(target);
                ai.setState(EntityAI.AIState.CHASE);
            } else if (npcType == NPCType.FRIENDLY) {
                currentState = NPCState.FLEEING;
                ai.setTarget(target);
                ai.setState(EntityAI.AIState.FLEE);
            }
        }
    }

    /**
     * Définit l'état du PNJ.
     * @param state État à définir.
     */
    public void setState(NPCState state) {
        this.currentState = state;
    }

    // --- Getters et Setters ---

    public String getNpcName() {
        return npcName;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NPCType getNpcType() {
        return npcType;
    }

    public void setNpcType(NPCType npcType) {
        this.npcType = npcType;
    }

    public List<Dialogue> getDialogues() {
        return dialogues;
    }

    public List<Quest> getAvailableQuests() {
        return availableQuests;
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public Shop getShop() {
        return shop;
    }

    public EntityAI getAi() {
        return ai;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public NPCState getCurrentState() {
        return currentState;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Retourne une représentation textuelle du PNJ.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "NPC{" +
                "name='" + npcName + '\'' +
                ", type=" + npcType +
                ", state=" + currentState +
                ", level=" + level +
                '}';
    }

    // --- Classe interne pour les dialogues ---

    /**
     * Classe pour les dialogues des PNJ.
     */
    public static class Dialogue {
        private final String id;
        private final String text;
        private final List<DialogueOption> options;

        public Dialogue(String id, String text) {
            this.id = id;
            this.text = text;
            this.options = new ArrayList<>();
        }

        public void addOption(DialogueOption option) {
            options.add(option);
        }

        public void start(NPC npc, Player player) {
            // Logique pour commencer le dialogue
            System.out.println(npc.getNpcName() + " : " + text);

            // Afficher les options de dialogue
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i).getText());
            }
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public List<DialogueOption> getOptions() {
            return options;
        }
    }

    /**
     * Classe pour les options de dialogue.
     */
    public static class DialogueOption {
        private final String text;
        private final Runnable action;

        public DialogueOption(String text, Runnable action) {
            this.text = text;
            this.action = action;
        }

        public String getText() {
            return text;
        }

        public Runnable getAction() {
            return action;
        }
    }
}
