package fr.alessdevita.valtherion.quest;

import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.entity.living.Player;
import java.util.*;

/**
 * Classe pour les récompenses des quêtes dans Valtherion.
 * Définit les récompenses que le joueur reçoit après avoir complété une quête.
 */
public class QuestReward {

    // Expérience accordée
    private int experience;

    // Pièces d'or accordées
    private int gold;

    // Liste des objets accordés
    private final List<ItemStackReward> items;

    // Réputation accordée (pour les factions)
    private int reputation;

    // Faction associée à la réputation
    private String faction;

    // Points de compétence accordés
    private final Map<String, Integer> skillPoints;

    public int getRewardAmount() {
        int rewardAmount = 0;
        return rewardAmount;
        }

    public String getCurrencyReward() {
        String currencyReward = "";
        return currencyReward;
    }

    // Classe pour représenter une récompense d'objet avec une quantité
    public static class ItemStackReward {
        private final Item item;
        private final int quantity;

        public ItemStackReward(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        // --- Getters ---
        public Item getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    /**
     * Constructeur des récompenses de quête.
     */
    public QuestReward() {
        this.experience = 0;
        this.gold = 0;
        this.items = new ArrayList<>();
        this.reputation = 0;
        this.faction = "";
        this.skillPoints = new HashMap<>();
    }

    /**
     * Ajoute de l'expérience aux récompenses.
     * @param experience Expérience à ajouter.
     */
    public void addExperience(int experience) {
        this.experience += experience;
    }

    /**
     * Ajoute de l'or aux récompenses.
     * @param gold Or à ajouter.
     */
    public void addGold(int gold) {
        this.gold += gold;
    }

    /**
     * Ajoute un objet aux récompenses.
     * @param item Objet à ajouter.
     * @param quantity Quantité de l'objet.
     */
    public void addItem(Item item, int quantity) {
        items.add(new ItemStackReward(item, quantity));
    }

    /**
     * Ajoute de la réputation aux récompenses.
     * @param reputation Réputation à ajouter.
     * @param faction Faction associée.
     */
    public void addReputation(int reputation, String faction) {
        this.reputation += reputation;
        this.faction = faction;
    }

    /**
     * Ajoute des points de compétence aux récompenses.
     * @param skill Nom de la compétence.
     * @param points Points à ajouter.
     */
    public void addSkillPoints(String skill, int points) {
        skillPoints.put(skill, skillPoints.getOrDefault(skill, 0) + points);
    }

    /**
     * Applique les récompenses à un joueur.
     * @param player Joueur à qui appliquer les récompenses.
     */
    public void applyReward(Player player) {
        // Ajouter l'expérience
        if (experience > 0) {
            player.addExperience(experience);
        }

        // Ajouter l'or (si wallet disponible)
        if (gold > 0 && player.getWallet() != null) {
            player.getWallet().addBalance("GOLD", gold);
        }

        // Ajouter les objets
        for (ItemStackReward itemReward : items) {
            player.addItem(itemReward.getItem(), itemReward.getQuantity());
        }

        // Ajouter la réputation
        if (reputation != 0 && !faction.isEmpty()) {
            // Logique pour ajouter de la réputation à la faction
            System.out.println("Réputation ajoutée : " + reputation + " pour la faction " + faction);
        }

        // Ajouter les points de compétence
        for (Map.Entry<String, Integer> entry : skillPoints.entrySet()) {
            player.upgradeSkill(entry.getKey(), entry.getValue());
        }
    }

    // --- Getters et Setters ---

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<ItemStackReward> getItems() {
        return items;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Map<String, Integer> getSkillPoints() {
        return skillPoints;
    }

    /**
     * Retourne une représentation textuelle des récompenses.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QuestReward{ ");
        sb.append("experience=").append(experience).append(", ");
        sb.append("gold=").append(gold).append(", ");
        sb.append("items=[");
        for (ItemStackReward item : items) {
            sb.append(item.getItem().getName()).append(" x").append(item.getQuantity()).append(", ");
        }
        sb.append("], reputation=").append(reputation).append(" (faction: ").append(faction).append("), ");
        sb.append("skillPoints=").append(skillPoints);
        sb.append("} ");
        return sb.toString();
    }
}