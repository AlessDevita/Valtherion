package fr.alessdevita.valtherion.economy;

import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.inventory.Item;

import java.util.*;

/**
 * Classe pour les boutiques dans Valtherion.
 * Permet aux joueurs d'acheter et de vendre des objets.
 */
public class Shop {

    // Nom de la boutique
    private final String name;

    // Propriétaire de la boutique (peut être un joueur ou un PNJ)
    private final String owner;

    // Devise utilisée dans la boutique
    private final Currency currency;

    // Liste des articles en vente (objet -> prix)
    private final Map<Item, Float> itemsForSale;

    // Liste des articles achetés (objet -> prix)
    private final Map<Item, Float> itemsBought;

    /**
     * Constructeur de la boutique.
     * @param name Nom de la boutique.
     * @param owner Propriétaire de la boutique.
     * @param currency Devise utilisée.
     */
    public Shop(String name, String owner, Currency currency) {
        this.name = name;
        this.owner = owner;
        this.currency = currency;
        this.itemsForSale = new HashMap<>();
        this.itemsBought = new HashMap<>();
    }

    /**
     * Ajoute un article en vente.
     * @param item Objet à vendre.
     * @param price Prix de l'objet.
     */
    public void addItemForSale(Item item, float price) {
        if (item == null) {
            throw new IllegalArgumentException("L'objet ne peut pas être nul.");
        }
        itemsForSale.put(item, price);
    }

    /**
     * Retire un article de la vente.
     * @param item Objet à retirer.
     */
    public void removeItemForSale(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("L'objet ne peut pas être nul.");
        }
        itemsForSale.remove(item);
    }

    /**
     * Ajoute un article à acheter.
     * @param item Objet à acheter.
     * @param price Prix de l'objet.
     */
    public void addItemBought(Item item, float price) {
        if (item == null) {
            throw new IllegalArgumentException("L'objet ne peut pas être nul.");
        }
        itemsBought.put(item, price);
    }

    /**
     * Retire un article de l'achat.
     * @param item Objet à retirer.
     */
    public void removeItemBought(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("L'objet ne peut pas être nul.");
        }
        itemsBought.remove(item);
    }

    /**
     * Achete un article depuis la boutique.
     * @param player Joueur qui achète.
     * @param item Objet à acheter.
     * @param economyManager Gestionnaire d'économie.
     * @return true si l'achat a réussi, false sinon.
     */
    public boolean buyItem(Player player, Item item, EconomyManager economyManager) {
        if (player == null || item == null || economyManager == null) {
            throw new IllegalArgumentException("Paramètres invalides.");
        }

        // Vérifier si l'objet est en vente
        if (!itemsForSale.containsKey(item)) {
            return false;
        }

        // Récupérer le prix de l'objet
        float price = itemsForSale.get(item);

        // Vérifier si le joueur a assez d'argent
        Wallet wallet = economyManager.getWallet(player);
        if (wallet == null) {
            return false;
        }

        if (!wallet.subtractBalance(currency, price)) {
            return false; // Solde insuffisant
        }

        // Donner l'objet au joueur
        player.getInventory().addItem(item, 1);
        return true;
    }

    /**
     * Vend un article à la boutique.
     * @param player Joueur qui vend.
     * @param item Objet à vendre.
     * @param economyManager Gestionnaire d'économie.
     * @return true si la vente a réussi, false sinon.
     */
    public boolean sellItem(Player player, Item item, EconomyManager economyManager) {
        if (player == null || item == null || economyManager == null) {
            throw new IllegalArgumentException("Paramètres invalides.");
        }

        // Vérifier si l'objet est acheté par la boutique
        if (!itemsBought.containsKey(item)) {
            return false;
        }

        // Récupérer le prix de l'objet
        float price = itemsBought.get(item);

        // Vérifier si le joueur a l'objet dans son inventaire
        if (!player.getInventory().hasItem(item, 1)) {
            return false;
        }

        // Retirer l'objet de l'inventaire du joueur
        player.getInventory().removeItem(item, 1);

        // Ajouter l'argent au joueur
        Wallet wallet = economyManager.getWallet(player);
        if (wallet != null) {
            wallet.addBalance(String.valueOf(currency), price);
        }

        return true;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Map<Item, Float> getItemsForSale() {
        return Collections.unmodifiableMap(itemsForSale);
    }

    public Map<Item, Float> getItemsBought() {
        return Collections.unmodifiableMap(itemsBought);
    }

    /**
     * Retourne une représentation textuelle de la boutique.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", currency=" + (currency != null ? currency.getName() : "null") +
                ", itemsForSale=" + itemsForSale.size() +
                ", itemsBought=" + itemsBought.size() +
                '}';
    }

    public void openForPlayer(Player player) {
        // Logique pour ouvrir la boutique pour un joueur
    }
}
