package fr.alessdevita.valtherion.gui.screens;

import fr.alessdevita.valtherion.core.Engine;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.rendering.Camera;
import fr.alessdevita.valtherion.rendering.Renderer;
import fr.alessdevita.valtherion.input.InputManager;

/**
 * Classe pour l'écran de jeu principal dans Valtherion.
 * Gère le rendu du monde, des entités, et des éléments de l'interface utilisateur.
 */
public class GameScreen {

    // Monde du jeu
    private final World world;

    // Joueur principal
    private final Player player;

    // Caméra pour le rendu
    private final Camera camera;

    // Gestionnaire de rendu
    private final Renderer renderer;

    // Interface graphique (GUI)
    private final GUI gui;

    // Gestionnaire des entrées (clavier, souris)
    private final InputManager inputManager;

    // État de l'écran de jeu
    private GameScreenState state;

    // Temps écoulé depuis le dernier tick (en secondes)
    private float deltaTime;

    public void onShutdown() {
        cleanup();
    }

    // Énumération des états de l'écran de jeu
    public enum GameScreenState {
        RUNNING,   // Jeu en cours
        PAUSED,    // Jeu en pause
        LOADING,   // Chargement en cours
        GAME_OVER  // Fin de la partie
    }

    /**
     * Constructeur de l'écran de jeu.
     * @param world Monde du jeu.
     * @param player Joueur principal.
     * @param camera Caméra pour le rendu.
     * @param renderer Gestionnaire de rendu.
     * @param inputManager Gestionnaire des entrées.
     */
    public GameScreen(World world, Player player, Camera camera, Renderer renderer, InputManager inputManager) {
        this.world = world;
        this.player = player;
        this.camera = camera;
        this.renderer = renderer;
        this.gui = new GUI(player, world);
        this.inputManager = inputManager;
        this.state = GameScreenState.RUNNING;
        this.deltaTime = 0.0f;

        // Initialiser la caméra pour suivre le joueur
        camera.setTarget(player.getPosition());
    }

    /**
     * Initialise l'écran de jeu.
     */
    public void init(Engine engine) throws Exception {
        // Initialiser le monde
        world.init();

        // Initialiser le rendu
        renderer.init();

        // Initialiser la GUI
        gui.init();

        // Initialiser le gestionnaire des entrées
        inputManager.init();

        System.out.println("Écran de jeu initialisé.");
    }

    /**
     * Met à jour l'écran de jeu.
     */
    public void update() {
        this.deltaTime = deltaTime;

        // Ne pas mettre à jour si le jeu est en pause ou en cours de chargement
        if (state != GameScreenState.RUNNING) {
            return;
        }

        // Mettre à jour le gestionnaire des entrées
        inputManager.update();

        // Gérer les entrées du joueur
        handleInput();

        // Mettre à jour le monde
        world.update(deltaTime);

        // Mettre à jour le joueur
        player.update();

        // Mettre à jour la caméra
        camera.update();

        // Mettre à jour la GUI
        gui.update();
    }

    /**
     * Gère les entrées du joueur.
     */
    private void handleInput() {
        // Gérer les mouvements du joueur
        if (inputManager.isKeyDown("Z")) {
            player.moveTowards(player.getX(), player.getY(), player.getZ() - 1, player.getWalkSpeed());
        }
        if (inputManager.isKeyDown("S")) {
            player.moveTowards(player.getX(), player.getY(), player.getZ() + 1, player.getWalkSpeed());
        }
        if (inputManager.isKeyDown("A")) {
            player.moveTowards(player.getX() - 1, player.getY(), player.getZ(), player.getWalkSpeed());
        }
        if (inputManager.isKeyDown("D")) {
            player.moveTowards(player.getX() + 1, player.getY(), player.getZ(), player.getWalkSpeed());
        }

        // Gérer le saut
        if (inputManager.isKeyPressed("SPACE")) {
            player.getPhysics().jump();
        }

        // Gérer le sprint
        if (inputManager.isKeyDown("LEFT_SHIFT")) {
            player.setSprinting(true);
        } else {
            player.setSprinting(false);
        }

        // Gérer l'ouverture de l'inventaire
        if (inputManager.isKeyPressed("E")) {
            gui.openInventory();
        }

        // Gérer la fermeture de l'inventaire
        if (inputManager.isKeyPressed("ESCAPE") && gui.getCurrentState() == GUI.GUIState.INVENTORY_OPEN) {
            gui.closeInventory();
        }

        // Gérer la pause
        if (inputManager.isKeyPressed("ESCAPE") && gui.getCurrentState() != GUI.GUIState.INVENTORY_OPEN) {
            togglePause();
        }

        // Gérer les clics de souris
        if (inputManager.isMouseClicked()) {
            gui.handleClick(inputManager.getMouseX(), inputManager.getMouseY());
        }
    }

    /**
     * Rend l'écran de jeu.
     */
    public void render() {
        // Rendre le monde
        renderer.render();

        // Rendre la GUI
        gui.render();

        // Afficher des informations de debug
        if (state == GameScreenState.RUNNING) {
            // Exemple : Afficher les FPS
            System.out.println("FPS: " + (1.0f / deltaTime));
        }
    }

    /**
     * Bascule entre l'état RUNNING et PAUSED.
     */
    public void togglePause() {
        if (state == GameScreenState.RUNNING) {
            state = GameScreenState.PAUSED;
            gui.setState(GUI.GUIState.PAUSED);
            System.out.println("Jeu mis en pause.");
        } else if (state == GameScreenState.PAUSED) {
            state = GameScreenState.RUNNING;
            gui.setState(GUI.GUIState.IN_GAME);
            System.out.println("Jeu repris.");
        }
    }

    /**
     * Met fin à la partie.
     */
    public void gameOver() {
        state = GameScreenState.GAME_OVER;
        System.out.println("Fin de la partie.");
    }

    /**
     * Recommence la partie.
     */
    public void restart() throws Exception {
        // Réinitialiser le monde
        world.init();

        // Réinitialiser le joueur
        player.respawn();

        // Réinitialiser l'état
        state = GameScreenState.RUNNING;
        gui.setState(GUI.GUIState.IN_GAME);

        System.out.println("Partie redémarrée.");
    }

    /**
     * Nettoie les ressources de l'écran de jeu.
     */
    public void cleanup() {
        // Nettoyer le monde
        world.cleanup();

        // Nettoyer le rendu
        renderer.cleanup();

        // Nettoyer la GUI
        gui.cleanup();

        System.out.println("Ressources de l'écran de jeu nettoyées.");
    }

    // --- Getters et Setters ---

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public GUI getGUI() {
        return gui;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public GameScreenState getState() {
        return state;
    }

    public void setState(GameScreenState state) {
        this.state = state;
    }
}