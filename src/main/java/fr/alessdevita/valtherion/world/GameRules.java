package fr.alessdevita.valtherion.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Gère les règles du jeu pour un monde.
 * Permet de configurer le comportement du monde (PvP, drops, etc.).
 */
public class GameRules {

    public static final String DO_MOB_SPAWNING = "doMobSpawning";
    public static final String DO_TILE_DROPS = "doTileDrops";
    public static final String KEEP_INVENTORY = "keepInventory";
    public static final String DO_DAYLIGHT_CYCLE = "doDaylightCycle";
    public static final String DO_WEATHER_CYCLE = "doWeatherCycle";
    public static final String NATURAL_REGENERATION = "naturalRegeneration";
    public static final String DO_FIRE_TICK = "doFireTick";
    public static final String DO_INSOMNIA = "doInsomnia";
    public static final String DO_IMMEDIATE_RESPAWN = "doImmediateRespawn";
    public static final String COMMAND_BLOCK_OUTPUT = "commandBlockOutput";
    public static final String SPECTATORS_GENERATE_CHUNKS = "spectatorsGenerateChunks";

    private final Map<String, String> rules;

    public GameRules() {
        this.rules = new HashMap<>();
        initDefaults();
    }

    private void initDefaults() {
        setRule(DO_MOB_SPAWNING, "true");
        setRule(DO_TILE_DROPS, "true");
        setRule(KEEP_INVENTORY, "false");
        setRule(DO_DAYLIGHT_CYCLE, "true");
        setRule(DO_WEATHER_CYCLE, "true");
        setRule(NATURAL_REGENERATION, "true");
        setRule(DO_FIRE_TICK, "true");
        setRule(DO_INSOMNIA, "true");
        setRule(DO_IMMEDIATE_RESPAWN, "false");
        setRule(COMMAND_BLOCK_OUTPUT, "false");
        setRule(SPECTATORS_GENERATE_CHUNKS, "true");
    }

    /**
     * Définit la valeur d'une règle.
     * @param rule Nom de la règle
     * @param value Valeur ("true" ou "false")
     */
    public void setRule(String rule, String value) {
        if (rule != null && value != null) {
            rules.put(rule, value);
        }
    }

    /**
     * Récupère la valeur d'une règle.
     * @param rule Nom de la règle
     * @return Valeur ou "false" par défaut
     */
    public String getRule(String rule) {
        return rules.getOrDefault(rule, "false");
    }

    /**
     * Récupère la valeur booléenne d'une règle.
     * @param rule Nom de la règle
     * @return true si la règle est activée
     */
    public boolean getBoolean(String rule) {
        return "true".equalsIgnoreCase(getRule(rule));
    }

    /**
     * Vérifie si une règle existe.
     * @param rule Nom de la règle
     * @return true si la règle existe
     */
    public boolean hasRule(String rule) {
        return rules.containsKey(rule);
    }

    /**
     * Retourne le nombre de règles.
     * @return Nombre de règles
     */
    public int getRuleCount() {
        return rules.size();
    }

    /**
     * Réinitialise toutes les règles aux valeurs par défaut.
     */
    public void reset() {
        rules.clear();
        initDefaults();
    }
}
