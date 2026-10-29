package fr.alessdevita.valtherion.api.commands;

import java.util.List;

/**
 * Classe de base pour toutes les commandes de Valtherion.
 * Les commandes peuvent être exécutées par des joueurs ou la console.
 */
public abstract class Command {

    // Nom de la commande (ex: "teleport", "give")
    private final String name;

    // Description de la commande
    private final String description;

    // Syntaxe de la commande (ex: "/teleport <x> <y> <z>")
    private final String usage;

    // Alias de la commande (ex: "tp" pour "teleport")
    private final List<String> aliases;

    // Permission requise pour exécuter la commande (optionnel)
    private String permission;

    /**
     * Constructeur de la commande.
     * @param name Nom de la commande.
     * @param description Description de la commande.
     * @param usage Syntaxe de la commande.
     * @param aliases Alias de la commande.
     */
    public Command(String name, String description, String usage, List<String> aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.permission = null; // Pas de permission par défaut
    }

    /**
     * Exécute la commande.
     * @param sender Expéditeur de la commande (joueur ou console).
     * @param args Arguments de la commande.
     * @return true si la commande a été exécutée avec succès, false sinon.
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Retourne le nom de la commande.
     * @return Nom de la commande.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne la description de la commande.
     * @return Description de la commande.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne la syntaxe de la commande.
     * @return Syntaxe de la commande.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Retourne les alias de la commande.
     * @return Liste des alias.
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Retourne la permission requise pour exécuter la commande.
     * @return Permission requise.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Définit la permission requise pour exécuter la commande.
     * @param permission Permission requise.
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Vérifie si l'expéditeur a la permission d'exécuter la commande.
     * @param sender Expéditeur de la commande.
     * @return true si l'expéditeur a la permission, false sinon.
     */
    public boolean hasPermission(CommandSender sender) {
        return permission == null || sender.hasPermission(permission);
    }

    /**
     * Retourne une représentation textuelle de la commande.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", usage='" + usage + '\'' +
                ", aliases=" + aliases +
                ", permission='" + permission + '\'' +
                '}';
    }
}
