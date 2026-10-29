package fr.alessdevita.valtherion.economy;

import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.*;

/**
 * Gestionnaire principal de l'économie pour Valtherion.
 * Gère les devises, les portefeuilles des joueurs, et les transactions.
 */
public class EconomyManager {

    // Logger pour les messages de debug
    private final Logger logger;

    // Liste des devises disponibles
    private final List<Currency> currencies;

    // Devise principale (par défaut)
    private Currency defaultCurrency;

    // Portefeuilles des joueurs (joueur -> portefeuille)
    private final Map<Player, Wallet> wallets;

    // Système de commerce
    private final TradeSystem tradeSystem;

    /**
     * Constructeur du gestionnaire d'économie.
     */
    public EconomyManager() {
        this.logger = new Logger("EconomyManager");
        this.currencies = new ArrayList<>();
        this.wallets = new HashMap<>();
        this.tradeSystem = new TradeSystem(this);
        logger.info("Gestionnaire d'économie initialisé.");
    }

    /**
     * Ajoute une devise.
     * @param currency Devise à ajouter.
     */
    public void addCurrency(Currency currency) {
        if (currency == null) {
            logger.warn("Impossible d'ajouter une devise nulle.");
            return;
        }
        if (!currencies.contains(currency)) {
            currencies.add(currency);
            logger.debug("Devise ajoutée : " + currency.getName());
        }
    }

    /**
     * Définit la devise principale.
     * @param currency Devise principale.
     */
    public void setDefaultCurrency(Currency currency) {
        if (currency == null) {
            logger.warn("Impossible de définir une devise principale nulle.");
            return;
        }
        this.defaultCurrency = currency;
        logger.debug("Devise principale définie : " + currency.getName());
    }

    /**
     * Crée un portefeuille pour un joueur.
     * @param player Joueur.
     */
    public void createWallet(Player player) {
        if (player == null) {
            logger.warn("Impossible de créer un portefeuille pour un joueur nul.");
            return;
        }
        if (!wallets.containsKey(player)) {
            Wallet wallet = new Wallet(player);

            // Ajouter toutes les devises au portefeuille avec un solde initial de 0
            for (Currency currency : currencies) {
                wallet.addCurrency(String.valueOf(currency), 0.0f);
            }

            wallets.put(player, wallet);
            logger.debug("Portefeuille créé pour : " + player.getName());
        }
    }

    /**
     * Retourne le portefeuille d'un joueur.
     * @param player Joueur.
     * @return Portefeuille du joueur ou null si introuvable.
     */
    public Wallet getWallet(Player player) {
        if (player == null) {
            logger.warn("Impossible de récupérer le portefeuille d'un joueur nul.");
            return null;
        }
        return wallets.get(player);
    }

    /**
     * Ajoute un montant à un joueur.
     * @param player Joueur.
     * @param currency Devise.
     * @param amount Montant.
     */
    public void addBalance(Player player, Currency currency, float amount) {
        Wallet wallet = getWallet(player);
        if (wallet != null) {
            wallet.addBalance(String.valueOf(currency), amount);
            logger.debug("Ajout de " + amount + " " + currency.getName() + " à " + player.getName());
        }
    }

    /**
     * Retire un montant d'un joueur.
     * @param player Joueur.
     * @param currency Devise.
     * @param amount Montant.
     * @return true si le retrait a réussi, false sinon.
     */
    public boolean subtractBalance(Player player, Currency currency, float amount) {
        Wallet wallet = getWallet(player);
        if (wallet != null) {
            return wallet.subtractBalance(currency, amount);
        }
        return false;
    }

    /**
     * Effectue une transaction entre deux joueurs.
     * @param sender Expéditeur.
     * @param receiver Destinataire.
     * @param currency Devise.
     * @param amount Montant.
     * @return true si la transaction a réussi, false sinon.
     */
    public boolean transfer(Player sender, Player receiver, Currency currency, float amount) {
        Wallet senderWallet = getWallet(sender);
        Wallet receiverWallet = getWallet(receiver);

        if (senderWallet == null || receiverWallet == null) {
            logger.warn("Impossible d'effectuer la transaction : portefeuille introuvable.");
            return false;
        }

        return senderWallet.transfer(receiverWallet, currency, amount);
    }

    /**
     * Retourne le système de commerce.
     * @return TradeSystem.
     */
    public TradeSystem getTradeSystem() {
        return tradeSystem;
    }

    /**
     * Retourne la devise principale.
     * @return Devise principale.
     */
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Retourne la liste des devises.
     * @return Liste des devises.
     */
    public List<Currency> getCurrencies() {
        return Collections.unmodifiableList(currencies);
    }

    /**
     * Retourne une devise par son nom.
     * @param name Nom de la devise.
     * @return Devise ou null si introuvable.
     */
    public Currency getCurrencyByName(String name) {
        if (name == null) {
            return null;
        }
        for (Currency currency : currencies) {
            if (currency.getName().equalsIgnoreCase(name)) {
                return currency;
            }
        }
        return null;
    }
}
