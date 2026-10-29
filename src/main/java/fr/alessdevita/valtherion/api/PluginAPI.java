package fr.alessdevita.valtherion.api;

import fr.alessdevita.valtherion.api.commands.CommandRegistry;
import fr.alessdevita.valtherion.api.events.EventManager;
import fr.alessdevita.valtherion.core.Logger;

/**
 * API principale pour les plugins Valtherion.
 * Permet aux développeurs d'étendre le jeu en enregistrant des événements, des commandes, etc.
 */
public class PluginAPI {

    // Logger pour les messages de l'API
    private final Logger logger;

    // Gestionnaire des événements
    private final EventManager eventManager;

    // Registre des commandes
    private final CommandRegistry commandRegistry;

    // Instance unique de l'API
    private static PluginAPI instance;

    /**
     * Constructeur privé pour le singleton.
     */
    private PluginAPI() {
        this.logger = new Logger("PluginAPI");
        this.eventManager = new EventManager();
        this.commandRegistry = new CommandRegistry();
        logger.info("API des plugins initialisée.");
    }

    /**
     * Retourne l'instance unique de l'API.
     * @return Instance de PluginAPI.
     */
    public static PluginAPI getInstance() {
        if (instance == null) {
            instance = new PluginAPI();
        }
        return instance;
    }

    /**
     * Retourne le gestionnaire des événements.
     * @return EventManager.
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Retourne le registre des commandes.
     * @return CommandRegistry.
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /**
     * Enregistre un plugin.
     * @param plugin Nom du plugin.
     */
    public void registerPlugin(String plugin) {
        logger.info("Plugin enregistré : " + plugin);
    }

    /**
     * Désenregistre un plugin.
     * @param plugin Nom du plugin.
     */
    public void unregisterPlugin(String plugin) {
        logger.info("Plugin désenregistré : " + plugin);
    }
}
