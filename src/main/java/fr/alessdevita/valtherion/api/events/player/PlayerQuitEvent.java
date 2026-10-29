package fr.alessdevita.valtherion.api.events.player;

import fr.alessdevita.valtherion.api.events.Event;
import fr.alessdevita.valtherion.entity.living.Player;

/**
 * Événement déclenché lorsqu'un joueur quitte le jeu.
 */
public class PlayerQuitEvent extends Event {

    // Joueur qui quitte le jeu
    private final Player player;

    // Message de départ personnalisé (optionnel)
    private String quitMessage;

    // Raison de la déconnexion (optionnel)
    private String reason;

    /**
     * Constructeur de l'événement de déconnexion d'un joueur.
     * @param player Joueur qui quitte le jeu.
     */
    public PlayerQuitEvent(Player player) {
        this(player, "§c" + player.getName() + " a quitté le jeu.", "Déconnexion");
    }

    /**
     * Constructeur de l'événement de déconnexion avec une raison.
     * @param player Joueur qui quitte le jeu.
     * @param quitMessage Message de départ.
     * @param reason Raison de la déconnexion.
     */
    public PlayerQuitEvent(Player player, String quitMessage, String reason) {
        super("PlayerQuitEvent");
        this.player = player;
        this.quitMessage = quitMessage;
        this.reason = reason;
    }

    /**
     * Retourne le joueur qui quitte le jeu.
     * @return Joueur.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retourne le message de départ.
     * @return Message de départ.
     */
    public String getQuitMessage() {
        return quitMessage;
    }

    /**
     * Définit le message de départ.
     * @param quitMessage Message de départ.
     */
    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

    /**
     * Retourne la raison de la déconnexion.
     * @return Raison de la déconnexion.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Définit la raison de la déconnexion.
     * @param reason Raison de la déconnexion.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Retourne une représentation textuelle de l'événement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PlayerQuitEvent{" +
                "player=" + (player != null ? player.getName() : "null") +
                ", quitMessage='" + quitMessage + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
