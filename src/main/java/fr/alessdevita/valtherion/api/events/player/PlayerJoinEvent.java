package fr.alessdevita.valtherion.api.events.player;

import fr.alessdevita.valtherion.api.events.CancelableEvent;
import fr.alessdevita.valtherion.entity.living.Player;

/**
 * Événement déclenché lorsqu'un joueur rejoint le jeu.
 * Peut être annulé pour empêcher le joueur de se connecter.
 */
public class PlayerJoinEvent extends CancelableEvent {

    // Joueur qui rejoint le jeu
    private final Player player;

    // Message de bienvenue personnalisé (optionnel)
    private String joinMessage;

    /**
     * Constructeur de l'événement de connexion d'un joueur.
     * @param player Joueur qui rejoint le jeu.
     */
    public PlayerJoinEvent(Player player) {
        super("PlayerJoinEvent");
        this.player = player;
        this.joinMessage = "§a" + player.getName() + " a rejoint le jeu."; // Message par défaut
    }

    /**
     * Retourne le joueur qui rejoint le jeu.
     * @return Joueur.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retourne le message de bienvenue.
     * @return Message de bienvenue.
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Définit le message de bienvenue.
     * @param joinMessage Message de bienvenue.
     */
    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    /**
     * Retourne une représentation textuelle de l'événement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PlayerJoinEvent{" +
                "player=" + (player != null ? player.getName() : "null") +
                ", joinMessage='" + joinMessage + '\'' +
                ", cancelled=" + isCancelled() +
                '}';
    }
}
