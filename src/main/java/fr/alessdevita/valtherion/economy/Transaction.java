package fr.alessdevita.valtherion.economy;

import fr.alessdevita.valtherion.entity.living.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe pour les transactions économiques dans Valtherion.
 * Représente une transaction entre deux joueurs ou entités.
 */
public class Transaction {

    // Expéditeur de la transaction
    private final Player sender;

    // Destinataire de la transaction
    private final Player receiver;

    // Devise utilisée pour la transaction
    private final Currency currency;

    // Montant de la transaction
    private final float amount;

    // Description de la transaction
    private final String description;

    // Date et heure de la transaction
    private final LocalDateTime timestamp;

    // Format de date pour l'affichage
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructeur de la transaction.
     * @param sender Expéditeur de la transaction.
     * @param receiver Destinataire de la transaction.
     * @param currency Devise utilisée.
     * @param amount Montant de la transaction.
     * @param description Description de la transaction.
     */
    public Transaction(Player sender, Player receiver, Currency currency, float amount, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters ---

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Currency getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Retourne une représentation textuelle de la transaction.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "sender=" + (sender != null ? sender.getName() : "null") +
                ", receiver=" + (receiver != null ? receiver.getName() : "null") +
                ", currency=" + (currency != null ? currency.getName() : "null") +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp.format(DATE_FORMAT) +
                '}';
    }
}
