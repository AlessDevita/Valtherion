package fr.alessdevita.valtherion.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe principale du moteur Valtherion.
 * Gère la boucle de jeu, les systèmes principaux et les ressources.
 */
public class Configuration {
    // Paramètres par défaut
    private String windowTitle = "Valtherion";
    private int windowWidth = 1280;
    private int windowHeight = 720;
    private boolean fullscreen = false;
    private boolean vsync = true;
    private int targetFPS = 60;
    private int targetUPS = 60;
    private boolean multiplayerEnabled = false;
    private String serverIP = "localhost";
    private int serverPort = 25565;
    private long worldSeed = System.currentTimeMillis(); // Graine aléatoire par défaut
    private float masterVolume = 1.0f;
    private float musicVolume = 0.8f;
    private float soundVolume = 1.0f;
    private String language = "fr";

    // Logger
    private final Logger logger;

    /**
     * Constructeur par défaut.
     */
    public Configuration() {
        this.logger = new Logger("Configuration");
    }

    /**
     * Charge la configuration depuis un fichier JSON.
     * @param filePath Chemin vers le fichier de configuration.
     */
    public void load(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject config = (JSONObject) parser.parse(reader);

            // Charger les paramètres de la fenêtre
            if (config.containsKey("window")) {
                JSONObject window = (JSONObject) config.get("window");
                this.windowTitle = (String) window.getOrDefault("title", this.windowTitle);
                this.windowWidth = ((Long) window.getOrDefault("width", (long) this.windowWidth)).intValue();
                this.windowHeight = ((Long) window.getOrDefault("height", (long) this.windowHeight)).intValue();
                this.fullscreen = (boolean) window.getOrDefault("fullscreen", this.fullscreen);
                this.vsync = (boolean) window.getOrDefault("vsync", this.vsync);
            }

            // Charger les paramètres de performance
            if (config.containsKey("performance")) {
                JSONObject performance = (JSONObject) config.get("performance");
                this.targetFPS = ((Long) performance.getOrDefault("targetFPS", (long) this.targetFPS)).intValue();
                this.targetUPS = ((Long) performance.getOrDefault("targetUPS", (long) this.targetUPS)).intValue();
            }

            // Charger les paramètres réseau
            if (config.containsKey("network")) {
                JSONObject network = (JSONObject) config.get("network");
                this.multiplayerEnabled = (boolean) network.getOrDefault("enabled", this.multiplayerEnabled);
                this.serverIP = (String) network.getOrDefault("serverIP", this.serverIP);
                this.serverPort = ((Long) network.getOrDefault("serverPort", (long) this.serverPort)).intValue();
            }

            // Charger les paramètres du monde
            if (config.containsKey("world")) {
                JSONObject world = (JSONObject) config.get("world");
                this.worldSeed = (long) world.getOrDefault("seed", this.worldSeed);
            }

            // Charger les paramètres audio
            if (config.containsKey("audio")) {
                JSONObject audio = (JSONObject) config.get("audio");
                this.masterVolume = ((Double) audio.getOrDefault("masterVolume", (double) this.masterVolume)).floatValue();
                this.musicVolume = ((Double) audio.getOrDefault("musicVolume", (double) this.musicVolume)).floatValue();
                this.soundVolume = ((Double) audio.getOrDefault("soundVolume", (double) this.soundVolume)).floatValue();
            }

            // Charger les paramètres de langue
            if (config.containsKey("language")) {
                this.language = (String) config.get("language");
            }

            logger.info("Configuration chargée depuis : " + filePath);

        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier de configuration : " + e.getMessage());
        } catch (ParseException e) {
            logger.error("Erreur lors de l'analyse du fichier JSON : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde la configuration dans un fichier JSON.
     * @param filePath Chemin vers le fichier de configuration.
     */
    public void save(String filePath) {
        JSONObject config = new JSONObject();

        // Sauvegarder les paramètres de la fenêtre
        JSONObject window = new JSONObject();
        window.put("title", this.windowTitle);
        window.put("width", this.windowWidth);
        window.put("height", this.windowHeight);
        window.put("fullscreen", this.fullscreen);
        window.put("vsync", this.vsync);
        config.put("window", window);

        // Sauvegarder les paramètres de performance
        JSONObject performance = new JSONObject();
        performance.put("targetFPS", this.targetFPS);
        performance.put("targetUPS", this.targetUPS);
        config.put("performance", performance);

        // Sauvegarder les paramètres réseau
        JSONObject network = new JSONObject();
        network.put("enabled", this.multiplayerEnabled);
        network.put("serverIP", this.serverIP);
        network.put("serverPort", this.serverPort);
        config.put("network", network);

        // Sauvegarder les paramètres du monde
        JSONObject world = new JSONObject();
        world.put("seed", this.worldSeed);
        config.put("world", world);

        // Sauvegarder les paramètres audio
        JSONObject audio = new JSONObject();
        audio.put("masterVolume", this.masterVolume);
        audio.put("musicVolume", this.musicVolume);
        audio.put("soundVolume", this.soundVolume);
        config.put("audio", audio);

        // Sauvegarder la langue
        config.put("language", this.language);

        // Écrire dans le fichier
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(config.toJSONString());
            logger.info("Configuration sauvegardée dans : " + filePath);
        } catch (IOException e) {
            logger.error("Erreur lors de l'écriture du fichier de configuration : " + e.getMessage());
        }
    }

    // --- Getters et Setters ---

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public int getTargetFPS() {
        return targetFPS;
    }

    public void setTargetFPS(int targetFPS) {
        this.targetFPS = targetFPS;
    }

    public int getTargetUPS() {
        return targetUPS;
    }

    public void setTargetUPS(int targetUPS) {
        this.targetUPS = targetUPS;
    }

    public boolean isMultiplayerEnabled() {
        return multiplayerEnabled;
    }

    public void setMultiplayerEnabled(boolean multiplayerEnabled) {
        this.multiplayerEnabled = multiplayerEnabled;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public void setWorldSeed(long worldSeed) {
        this.worldSeed = worldSeed;
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, masterVolume));
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume));
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = Math.max(0.0f, Math.min(1.0f, soundVolume));
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Object getServerport() {
        return serverPort;
    }
}
