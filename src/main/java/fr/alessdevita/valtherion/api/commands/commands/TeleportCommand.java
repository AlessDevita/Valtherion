package fr.alessdevita.valtherion.api.commands.commands;

import fr.alessdevita.valtherion.api.commands.Command;
import fr.alessdevita.valtherion.api.commands.CommandSender;
import fr.alessdevita.valtherion.entity.living.Player;

import java.util.Arrays;

/**
 * Commande pour téléporter un joueur à une position donnée.
 * Syntaxe : /teleport <x> <y> <z> [joueur]
 * Alias : /tp
 */
public class TeleportCommand extends Command {

    /**
     * Constructeur de la commande de téléportation.
     */
    public TeleportCommand() {
        super(
                "teleport",
                "Téléporte un joueur à une position donnée.",
                "/teleport <x> <y> <z> [joueur]",
                Arrays.asList("tp")
        );
        setPermission("Valtherion.command.teleport");
    }

    /**
     * Exécute la commande.
     * @param sender Expéditeur de la commande (joueur ou console).
     * @param args Arguments de la commande.
     * @return true si la commande a été exécutée avec succès, false sinon.
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Vérification des arguments
        if (args.length < 3) {
            sender.sendMessage("§cUsage : " + getUsage());
            return false;
        }
        try {
            // Récupérer les coordonnées
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            // Récupérer le joueur cible
            Player target;
            if (args.length >= 4) {
                // Si un joueur est spécifié, le téléporter
                target = getPlayerByName(args[3]);
                if (target == null) {
                    sender.sendMessage("§cJoueur introuvable : " + args[3]);
                    return false;
                }
            } else {
                // Sinon, téléporter l'expéditeur (s'il est un joueur)
                if (!sender.isPlayer()) {
                    sender.sendMessage("§cCette commande doit être éxécutée par un joueur ou avec un joueur cible.");
                    return false;
                }
                target = (Player) sender;
            }

            // Téléporter le joueur
            target.teleport(x, y, z);
            sender.sendMessage("§aTéléportation réussie de " + target.getName() + " à (" + x + ", " + y + ", " + z + ")");
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cLes coordonnées doivent être des nombres.");
            return false;
        }
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
