package fr.alessdevita.valtherion.core;

import fr.alessdevita.valtherion.audio.AudioManager;
import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.gui.screens.GameScreen;
import fr.alessdevita.valtherion.input.InputManager;
import fr.alessdevita.valtherion.network.core.NetworkManager;
import fr.alessdevita.valtherion.rendering.Renderer;
import fr.alessdevita.valtherion.world.World;

/**
 * Classe principale du moteur Valtherion.
 * Gère les systèmes bas niveau et délègue le gameplay via IGameplay.
 */
public class Engine {

    private boolean isRunning;
    private boolean isPaused;

    private GameLoop gameLoop;
    private final Configuration config;

    private World world;
    private Renderer renderer;
    private InputManager inputManager;
    private NetworkManager networkManager;
    private AudioManager audioManager;
    private GUI currentScreen;

    private long lastFrameTime;
    private int fps;
    private int ups;

    private final Logger logger;
    private GameScreen gameplay;

    public Engine(Configuration config) {
        this.config = config;
        this.logger = new Logger("Engine");
        this.isRunning = false;
        this.isPaused = false;
        this.lastFrameTime = System.nanoTime();
    }

    /**
     * Démarre le moteur avec une instance de gameplay spécifique.
     */
    public void start() throws Exception {
        if (isRunning) {
            logger.warn("Le moteur est déjà en cours d'exécution.");
            return;
        }

        logger.info("Démarrage du moteur...");

        if (gameplay != null) {
            gameplay.init(this);
        }

        isRunning = true;
        this.gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void stop() {
        if (!isRunning) return;
        logger.info("Arrêt du moteur...");
        
        if (gameplay != null) {
            gameplay.onShutdown();
        }

        isRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
        logger.info("Moteur " + (isPaused ? "en pause" : "repris"));
    }

    public void update() {
        if (isPaused) return;

        // Mise à jour des systèmes bas niveau
        if (inputManager != null) inputManager.update();
        if (networkManager != null) networkManager.update();
        if (audioManager != null) audioManager.update();

        // Délégation au gameplay
        if (gameplay != null) {
            gameplay.update();
        }

        if (currentScreen != null) {
            currentScreen.update();
        }
    }

    public void render() {
        // Le gameplay peut effectuer son propre rendu ou configurer la scène
        if (gameplay != null) {
            gameplay.render();
        }

        if (renderer != null) {
            renderer.render(world, currentScreen);
        }
    }

    public void calculateFPS() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastFrameTime;

        if (elapsedTime >= 1_000_000_000) {
            fps = gameLoop.getFrameCount();
            ups = gameLoop.getUpdateCount();
            gameLoop.resetCounters();
            lastFrameTime = currentTime;
            logger.debug("FPS: " + fps + ", UPS: " + ups);
        }
    }

    // --- Getters et Setters ---
    public boolean isRunning() { return isRunning; }
    public boolean isPaused() { return isPaused; }
    public Configuration getConfig() { return config; }
    public World getWorld() { return world; }
    public void setWorld(World world) { this.world = world; }
    public Renderer getRenderer() { return renderer; }
    public void setRenderer(Renderer renderer) { this.renderer = renderer; }
    public InputManager getInputManager() { return inputManager; }
    public void setInputManager(InputManager inputManager) { this.inputManager = inputManager; }
    public NetworkManager getNetworkManager() { return networkManager; }
    public void setNetworkManager(NetworkManager networkManager) { this.networkManager = networkManager; }
    public AudioManager getAudioManager() { return audioManager; }
    public void setAudioManager(AudioManager audioManager) { this.audioManager = audioManager; }
    public GUI getCurrentScreen() { return currentScreen; }
    public void setCurrentScreen(GUI currentScreen) { this.currentScreen = currentScreen; }
    public int getFps() { return fps; }
    public int getUps() { return ups; }
}