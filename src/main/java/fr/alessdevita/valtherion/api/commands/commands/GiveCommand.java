package fr.alessdevita.valtherion.api.commands.commands;

import fr.alessdevita.valtherion.api.commands.Command;
import fr.alessdevita.valtherion.api.commands.CommandSender;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.ItemRegistry;

import java.util.Arrays;

/**
 * Commande pour donner un objet à un joueur.
 * Syntaxe : /give <objet> [quantité] [joueur]
 */
public class GiveCommand extends Command {

    /**
     * Constructeur de la commande pour donner un objet.
     */
    public GiveCommand() {
        super(
                "give",
                "Donne un objet à un joueur.",
                "/give <objet> [quantité] [joueur]",
                Arrays.asList()
        );
        setPermission("valtherion.give");
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

        // Récupérer l'ID de l'objet
        String itemId = args[0];

        // Récupérer la quantité (par défaut : 1)
        int quantity = 1;
        if (args.length >= 2) {
            try {
                quantity = Integer.parseInt(args[1]);
                if (quantity <= 0) {
                    sender.sendMessage("§cLa quantité doit être supérieure à 0.");
                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("§cLa quantité doit être un nombre.");
                return false;
            }
        }

        // Récupérer le joueur cible
        Player target;
        if (args.length >= 3) {
            // Si un joueur est spécifié, lui donner l'objet
            target = getPlayerByName(args[2]);
            if (target == null) {
                sender.sendMessage("§cJoueur introuvable : " + args[2]);
                return false;
            }
        } else {
            // Sinon, donner l'objet à l'expéditeur (s'il est un joueur)
            if (!sender.isPlayer()) {
                sender.sendMessage("§cCette commande doit être exécutée par un joueur ou avec un joueur cible.");
                return false;
            }
            target = (Player) sender;
        }

        // Récupérer l'objet depuis le registre
        Item item = ItemRegistry.getInstance().getItem(itemId);
        if (item == null) {
            sender.sendMessage("§cObjet introuvable : " + itemId);
            return false;
        }

        // Donner l'objet au joueur
        target.getInventory().addItem(item, quantity);
        sender.sendMessage("§a" + quantity + " x " + item.getName() + " donné(s) à " + target.getName() + ".");
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