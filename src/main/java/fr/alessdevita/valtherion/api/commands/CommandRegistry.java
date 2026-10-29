package fr.alessdevita.valtherion.api.commands;

import fr.alessdevita.valtherion.core.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registre des commandes pour Valtherion.
 * Permet d'enregistrer, désenregistrer et exécuter des commandes.
 */
public class CommandRegistry {

    // Logger pour les messages de debug
    private final Logger logger;

    // Map des commandes enregistrées (nom -> commande)
    private final Map<String, Command> commands;

    // Map des alias (alias -> nom de la commande)
    private final Map<String, String> aliases;

    /**
     * Constructeur du registre des commandes.
     */
    public CommandRegistry() {
        this.logger = new Logger("CommandRegistry");
        this.commands = new HashMap<>();
        this.aliases = new HashMap<>();
    }

    /**
     * Enregistre une commande.
     * @param command Commande à enregistrer.
     */
    public void registerCommand(Command command) {
        if (command == null) {
            logger.warn("Impossible d'enregistrer une commande nulle.");
            return;
        }

        // Enregistrer la commande avec son nom
        commands.put(command.getName().toLowerCase(), command);
        logger.debug("Commande enregistrée : " + command.getName());

        // Enregistrer les alias
        for (String alias : command.getAliases()) {
            aliases.put(alias.toLowerCase(), command.getName().toLowerCase());
            logger.debug("Alias enregistré : " + alias + " -> " + command.getName());
        }
    }

    /**
     * Désenregistre une commande.
     * @param name Nom de la commande à désenregistrer.
     */
    public void unregisterCommand(String name) {
        if (name == null) {
            logger.warn("Impossible de désenregistrer une commande avec un nom nul.");
            return;
        }

        Command command = commands.remove(name.toLowerCase());
        if (command != null) {
            // Supprimer les alias de la commande
            for (String alias : command.getAliases()) {
                aliases.remove(alias.toLowerCase());
            }
            logger.debug("Commande désenregistrée : " + name);
        }
    }

    /**
     * Exécute une commande.
     * @param sender Expéditeur de la commande.
     * @param commandLine Ligne de commande complète (ex: "/teleport 100 64 100").
     * @return true si la commande a été exécutée avec succès, false sinon.
     */
    public boolean executeCommand(CommandSender sender, String commandLine) {
        if (commandLine == null || commandLine.isEmpty()) {
            return false;
        }

        // Supprimer le "/" au début de la commande
        String cleanCommand = commandLine.startsWith("/") ? commandLine.substring(1) : commandLine;

        // Diviser la commande en parties (nom + arguments)
        String[] parts = cleanCommand.split(" ");
        if (parts.length == 0) {
            return false;
        }

        // Récupérer le nom de la commande
        String commandName = parts[0].toLowerCase();

        // Vérifier si c'est un alias
        if (aliases.containsKey(commandName)) {
            commandName = aliases.get(commandName);
        }

        // Récupérer la commande
        Command command = commands.get(commandName);
        if (command == null) {
            sender.sendMessage("§cCommande introuvable : " + parts[0]);
            return false;
        }

        // Vérifier les permissions
        if (!command.hasPermission(sender)) {
            sender.sendMessage("§cVous n'avez pas la permission d'exécuter cette commande.");
            return false;
        }

        // Extraire les arguments (sans le nom de la commande)
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);

        // Exécuter la commande
        try {
            boolean success = command.execute(sender, args);
            if (!success) {
                sender.sendMessage("§cUsage : " + command.getUsage());
            }
            return success;
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution de la commande " + commandName + ": " + e.getMessage());
            sender.sendMessage("§cUne erreur est survenue lors de l'exécution de la commande.");
            return false;
        }
    }

    /**
     * Retourne une commande par son nom.
     * @param name Nom de la commande.
     * @return Commande ou null si introuvable.
     */
    public Command getCommand(String name) {
        if (name == null) {
            return null;
        }

        // Vérifier si c'est un alias
        String commandName = aliases.getOrDefault(name.toLowerCase(), name.toLowerCase());
        return commands.get(commandName);
    }

    /**
     * Retourne la liste de toutes les commandes enregistrées.
     * @return Liste des commandes.
     */
    public List<Command> getCommands() {
        return new ArrayList<>(commands.values());
    }

    /**
     * Vérifie si une commande existe.
     * @param name Nom de la commande.
     * @return true si la commande existe, false sinon.
     */
    public boolean hasCommand(String name) {
        if (name == null) {
            return false;
        }

        // Vérifier si c'est un alias
        String commandName = aliases.getOrDefault(name.toLowerCase(), name.toLowerCase());
        return commands.containsKey(commandName);
    }
}
