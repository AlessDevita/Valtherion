package fr.alessdevita.valtherion.economy;

import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.*;

/**
 * Système de commerce pour Valtherion.
 * Gère les échanges entre joueurs et les boutiques.
 */
public class TradeSystem {

    // Logger pour les messages de debug
    private final Logger logger;

    // Gestionnaire d'économie
    private final EconomyManager economyManager;

    // Liste des boutiques
    private final List<Shop> shops;

    // Transactions en cours (pour éviter les doublons)
    private final Set<String> pendingTransactions;

    /**
     * Constructeur du système de commerce.
     * @param economyManager Gestionnaire d'économie.
     */
    public TradeSystem(EconomyManager economyManager) {
        this.logger = new Logger("TradeSystem");
        this.economyManager = economyManager;
        this.shops = new ArrayList<>();
        this.pendingTransactions = new HashSet<>();
    }

    /**
     * Ajoute une boutique.
     * @param shop Boutique à ajouter.
     */
    public void addShop(Shop shop) {
        if (shop == null) {
            logger.warn("Impossible d'ajouter une boutique nulle.");
            return;
        }
        shops.add(shop);
        logger.debug("Boutique ajoutée : " + shop.getName());
    }

    /**
     * Retire une boutique.
     * @param shop Boutique à retirer.
     */
    public void removeShop(Shop shop) {
        if (shop == null) {
            logger.warn("Impossible de retirer une boutique nulle.");
            return;
        }
        shops.remove(shop);
        logger.debug("Boutique retirée : " + shop.getName());
    }

    /**
     * Effectue une transaction entre deux joueurs.
     * @param sender Expéditeur.
     * @param receiver Destinataire.
     * @param currency Devise.
     * @param amount Montant.
     * @return true si la transaction a réussi, false sinon.
     */
    public boolean trade(Player sender, Player receiver, Currency currency, float amount) {
        if (sender == null || receiver == null || currency == null) {
            logger.warn("Impossible d'effectuer une transaction avec des paramètres nuls.");
            return false;
        }

        // Générer un ID unique pour la transaction
        String transactionId = generateTransactionId(sender, receiver, amount);

        // Vérifier si la transaction est déjà en cours
        if (pendingTransactions.contains(transactionId)) {
            logger.warn("Transaction déjà en cours : " + transactionId);
            return false;
        }

        // Ajouter la transaction à la liste des transactions en cours
        pendingTransactions.add(transactionId);

        // Effectuer la transaction
        boolean success = economyManager.transfer(sender, receiver, currency, amount);

        // Retirer la transaction de la liste des transactions en cours
        pendingTransactions.remove(transactionId);

        if (success) {
            logger.debug("Transaction réussie : " + amount + " " + currency.getName() +
                    " de " + sender.getName() + " à " + receiver.getName());
        } else {
            logger.warn("Transaction échouée : solde insuffisant.");
        }

        return success;
    }

    /**
     * Génère un ID unique pour une transaction.
     * @param sender Expéditeur.
     * @param receiver Destinataire.
     * @param amount Montant.
     * @return ID unique de la transaction.
     */
    private String generateTransactionId(Player sender, Player receiver, float amount) {
        return sender.getName() + "->" + receiver.getName() + ":" + amount;
    }

    /**
     * Retourne une boutique par son nom.
     * @param name Nom de la boutique.
     * @return Boutique ou null si introuvable.
     */
    public Shop getShopByName(String name) {
        if (name == null) {
            return null;
        }
        for (Shop shop : shops) {
            if (shop.getName().equalsIgnoreCase(name)) {
                return shop;
            }
        }
        return null;
    }

    /**
     * Retourne la liste des boutiques.
     * @return Liste des boutiques.
     */
    public List<Shop> getShops() {
        return Collections.unmodifiableList(shops);
    }

    /**
     * Retourne le gestionnaire d'économie.
     * @return EconomyManager.
     */
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
