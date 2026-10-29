package fr.alessdevita.valtherion.input;

/**
 * Classe pour gérer les entrées de la souris dans Valtherion.
 * Gère les mouvements, les clics, et la molette de la souris.
 */
public class MouseInput {

    // Position de la souris
    private float mouseX, mouseY;

    // Décalage de la souris
    private float mouseDX, mouseDY;

    // État des boutons de la souris
    private final boolean[] mouseButtonStates;

    // Nombre de boutons de la souris
    private static final int MOUSE_BUTTON_COUNT = 3; // Gauche, droit, milieu

    // Indique si la souris est en mode "capturé" (pour le jeu)
    private boolean isCaptured;

    // Sensibilité de la souris
    private float sensitivity;

    /**
     * Constructeur des entrées de la souris.
     */
    public MouseInput() {
        this.mouseX = 0.0f;
        this.mouseY = 0.0f;
        this.mouseDX = 0.0f;
        this.mouseDY = 0.0f;
        this.mouseButtonStates = new boolean[MOUSE_BUTTON_COUNT];
        this.isCaptured = false;
        this.sensitivity = 1.0f;
    }

    /**
     * Initialise les entrées de la souris.
     */
    public void init() {
        System.out.println("Entrées de la souris initialisées.");
    }

    /**
     * Met à jour les entrées de la souris.
     */
    public void update() {
        // Réinitialiser les décalages
        mouseDX = 0.0f;
        mouseDY = 0.0f;
    }

    /**
     * Appelé lorsque la souris est déplacée.
     * @param x Coordonnée X de la souris.
     * @param y Coordonnée Y de la souris.
     */
    public void onMouseMove(float x, float y) {
        if (isCaptured) {
            // Calculer le décalage en tenant compte de la sensibilité
            mouseDX = (x - mouseX) * sensitivity;
            mouseDY = (y - mouseY) * sensitivity;
        }

        mouseX = x;
        mouseY = y;
    }

    /**
     * Appelé lorsque un bouton de la souris est pressé.
     * @param button Code du bouton.
     */
    public void onMousePress(int button) {
        if (button >= 0 && button < MOUSE_BUTTON_COUNT) {
            mouseButtonStates[button] = true;
        }
    }

    /**
     * Appelé lorsque un bouton de la souris est relâché.
     * @param button Code du bouton.
     */
    public void onMouseRelease(int button) {
        if (button >= 0 && button < MOUSE_BUTTON_COUNT) {
            mouseButtonStates[button] = false;
        }
    }

    /**
     * Appelé lorsque la molette de la souris est utilisée.
     * @param deltaX Décalage horizontal de la molette.
     * @param deltaY Décalage vertical de la molette.
     */
    public void onMouseScroll(float deltaX, float deltaY) {
        // Logique pour gérer la molette de la souris
    }

    /**
     * Vérifie si un bouton de la souris est enfoncé.
     * @param button Code du bouton.
     * @return true si le bouton est enfoncé, false sinon.
     */
    public boolean isMouseButtonDown(int button) {
        if (button >= 0 && button < MOUSE_BUTTON_COUNT) {
            return mouseButtonStates[button];
        }
        return false;
    }

    /**
     * Vérifie si un bouton de la souris a été cliqué.
     * @param button Code du bouton.
     * @return true si le bouton a été cliqué, false sinon.
     */
    public boolean isMouseButtonClicked(int button) {
        // À implémenter selon ta logique de gestion des entrées
        return false;
    }

    /**
     * Capture ou libère la souris.
     * @param captured true pour capturer la souris, false pour la libérer.
     */
    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }

    /**
     * Définit la sensibilité de la souris.
     * @param sensitivity Sensibilité de la souris.
     */
    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    // --- Getters ---

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public float getMouseDX() {
        return mouseDX;
    }

    public float getMouseDY() {
        return mouseDY;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void move(double x, double y) {

    }
}