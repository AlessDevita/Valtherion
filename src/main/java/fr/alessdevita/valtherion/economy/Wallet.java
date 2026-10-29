package fr.alessdevita.valtherion.economy;

import fr.alessdevita.valtherion.entity.living.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Portefeuille des joueurs pour gérer leurs soldes dans différentes devises.
 */
public class Wallet {

    // Joueur propriétaire du portefeuille
    private final Player owner;

    // Soldes dans différentes devises (devise -> montant)
    private final Map<String, Float> balances;

    // Historique des transactions
    private final java.util.List<Transaction> transactionHistory;

    /**
     * Constructeur du portefeuille.
     * @param owner Joueur propriétaire du portefeuille.
     */
    public Wallet(Player owner) {
        this.owner = owner;
        this.balances = new HashMap<String, Float>();
        this.transactionHistory = new java.util.ArrayList<>();
    }

    /**
     * Ajoute une devise au portefeuille avec un solde initial.
     * @param currency Devise à ajouter.
     * @param initialBalance Solde initial.
     */
    public void addCurrency(String currency, float initialBalance) {
        if (currency == null) {
            throw new IllegalArgumentException("La devise ne peut pas être nulle.");
        }
        balances.put(currency, initialBalance);
    }

    /**
     * Retourne le solde dans une devise donnée.
     * @param currency Devise.
     * @return Solde dans la devise.
     */
    public float getBalance(String currency) {
        if (currency == null) {
            throw new IllegalArgumentException("La devise ne peut pas être nulle.");
        }
        return balances.getOrDefault(currency, 0.0f);
    }

    /**
     * Définit le solde dans une devise donnée.
     * @param currency Devise.
     * @param amount Montant.
     */
    public void setBalance(String currency, float amount) {
        if (currency == null) {
            throw new IllegalArgumentException("La devise ne peut pas être nulle.");
        }
        balances.put(currency, amount);
    }

    /**
     * Ajoute un montant à une devise.
     * @param currency Devise.
     * @param amount Montant à ajouter.
     */
    public void addBalance(String currency, float amount) {
        if (currency == null) {
            throw new IllegalArgumentException("La devise ne peut pas être nulle.");
        }
        float currentBalance = getBalance(currency);
        setBalance(currency, currentBalance + amount);
    }

    /**
     * Retire un montant d'une devise.
     * @param currency Devise.
     * @param amount Montant à retirer.
     * @return true si le retrait a réussi, false si le solde est insuffisant.
     */
    public boolean subtractBalance(Currency currency, float amount) {
        if (currency == null) {
            throw new IllegalArgumentException("La devise ne peut pas être nulle.");
        }
        float currentBalance = getBalance(String.valueOf(currency));
        if (currentBalance < amount) {
            return false; // Solde insuffisant
        }
        setBalance(String.valueOf(currency), currentBalance - amount);
        return true;
    }

    /**
     * Effectue une transaction vers un autre portefeuille.
     * @param targetWallet Portefeuille cible.
     * @param currency Devise.
     * @param amount Montant.
     * @return true si la transaction a réussi, false sinon.
     */
    public boolean transfer(Wallet targetWallet, Currency currency, float amount) {
        if (targetWallet == null || currency == null) {
            throw new IllegalArgumentException("Le portefeuille cible ou la devise ne peut pas être nul(le).");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        // Vérifier si le solde est suffisant
        if (!subtractBalance(currency, amount)) {
            return false; // Solde insuffisant
        }

        // Ajouter le montant au portefeuille cible
        targetWallet.addBalance(String.valueOf(currency), amount);

        // Enregistrer la transaction
        Transaction transaction = new Transaction(
                this.owner,
                targetWallet.getOwner(),
                currency,
                amount,
                "Transfert"
        );
        this.transactionHistory.add(transaction);
        targetWallet.getTransactionHistory().add(transaction);

        return true;
    }

    /**
     * Retourne le propriétaire du portefeuille.
     * @return Joueur propriétaire.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Retourne l'historique des transactions.
     * @return Liste des transactions.
     */
    public java.util.List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Retourne une représentation textuelle du portefeuille.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Wallet{" +
                "owner=" + (owner != null ? owner.getName() : "null") +
                ", balances=" + balances +
                '}';
    }
}