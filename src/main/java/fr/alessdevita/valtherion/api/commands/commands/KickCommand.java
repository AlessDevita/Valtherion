package fr.alessdevita.valtherion.api.commands.commands;

import fr.alessdevita.valtherion.api.commands.Command;
import fr.alessdevita.valtherion.api.commands.CommandSender;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.Arrays;

/**
 * Commande pour expulser un joueur du serveur.
 * Syntaxe : /kick <joueur> [raison]
 */
public class KickCommand extends Command {

    /**
     * Constructeur de la commande pour expulser un joueur.
     */
    public KickCommand() {
        super(
                "kick",
                "Expulse un joueur du serveur.",
                "/kick <joueur> [raison]",
                Arrays.asList()
        );
        setPermission("valtherion.kick");
    }

    /**
     * Exécute la commande.
     * @param sender Expéditeur de la commande.
     * @param args Arguments de la commande.
     * @return true si la commande a été exécutée avec succès, false sinon.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Vérifier les arguments
        if (args.length < 1) {
            sender.sendMessage("§cUsage : " + getUsage());
            return false;
        }

        // Récupérer le joueur à expulser
        Player target = getPlayerByName(args[0]);
        if (target == null) {
            sender.sendMessage("§cJoueur introuvable : " + args[0]);
            return false;
        }

        // Récupérer la raison (optionnelle)
        String reason = args.length >= 2 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "Expulsé par un administrateur.";

        // Expulser le joueur
        target.kick(reason);
        sender.sendMessage("§a" + target.getName() + " a été expulsé du serveur. Raison : " + reason);
        return true;
    }

    /**
     * Récupère un joueur par son nom.
     * @param name Nom du joueur.
     * @return Joueur ou null si introuvable.
     */
    private Player getPlayerByName(String name) {
        // Logique pour récupérer un joueur par son nom
        // Exemple : return Server.getInstance().getPlayer(name);
        // À implémenter selon ton système de gestion des joueurs
        return null;
    }
}