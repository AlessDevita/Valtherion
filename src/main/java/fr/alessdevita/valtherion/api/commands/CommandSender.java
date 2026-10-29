package fr.alessdevita.valtherion.api.commands;

/**
 * Interface pour les expéditeurs de commandes (joueur, console, etc.).
 * Permet d'envoyer des messages et de vérifier les permissions.
 */
public interface CommandSender {

    /**
     * Envoie un message à l'expéditeur.
     * @param message Message à envoyer.
     */
    void sendMessage(String message);

    /**
     * Vérifie si l'expéditeur a une permission spécifique.
     * @param permission Permission à vérifier.
     * @return true si l'expéditeur a la permission, false sinon.
     */
    boolean hasPermission(String permission);

    /**
     * Retourne le nom de l'expéditeur.
     * @return Nom de l'expéditeur.
     */
    String getName();

    /**
     * Vérifie si l'expéditeur est un joueur.
     * @return true si l'expéditeur est un joueur, false sinon.
     */
    boolean isPlayer();
}