package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Classe pour l'interface des contrôles dans Valtherion.
 * Permet d'afficher et de modifier les contrôles du jeu.
 */
public class GuiControls extends Gui {

    // Map des actions et de leurs touches associées
    private final Map<String, Integer> controls;

    // Action actuellement sélectionnée pour la réassignation
    private String selectedAction;

    // Indique si l'interface est en mode de réassignation
    private boolean isRebinding;

    // Couleur du fond de l'interface
    private Vector4f backgroundColor;

    // Rendu de texte pour l'interface
    private FontRenderer fontRenderer;

    // Liste des actions disponibles
    private final List<String> actions;

    // Bouton pour sauvegarder les contrôles
    private GuiButton saveButton;

    // Bouton pour réinitialiser les contrôles
    private GuiButton resetButton;

    // Bouton pour retourner au menu précédent
    private GuiButton backButton;

    /**
     * Constructeur de l'interface des contrôles.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiControls(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.controls = new HashMap<>();
        this.selectedAction = null;
        this.isRebinding = false;
        this.backgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.8f); // Noir semi-transparent
        this.actions = new ArrayList<>();

        // Initialiser les actions par défaut
        initDefaultControls();

        // Initialiser les boutons
        initButtons();
    }

    public GuiControls(GuiOptions guiOptions, Object gameSettings, Map<String, Integer> controls, List<String> actions) {
        this.controls = controls;
        this.actions = actions;
    }

    @Override
    public void init() {
        super.init();
        try {
            this.fontRenderer = new FontRenderer(16.0f);
            fontRenderer.init();
        } catch (Exception e) {
            System.err.println("Impossible d'initialiser le rendu de texte pour les contrôles : " + e.getMessage());
        }
    }

    /**
     * Initialise les contrôles par défaut.
     */
    private void initDefaultControls() {
        // Actions de mouvement
        actions.add("forward");
        controls.put("forward", GLFW.GLFW_KEY_W);

        actions.add("backward");
        controls.put("backward", GLFW.GLFW_KEY_S);

        actions.add("left");
        controls.put("left", GLFW.GLFW_KEY_A);

        actions.add("right");
        controls.put("right", GLFW.GLFW_KEY_D);

        actions.add("jump");
        controls.put("jump", GLFW.GLFW_KEY_SPACE);

        actions.add("sprint");
        controls.put("sprint", GLFW.GLFW_KEY_LEFT_SHIFT);

        // Actions de combat
        actions.add("attack");
        controls.put("attack", GLFW.GLFW_KEY_LEFT_CONTROL);

        // Actions diverses
        actions.add("inventory");
        controls.put("inventory", GLFW.GLFW_KEY_E);

        actions.add("chat");
        controls.put("chat", GLFW.GLFW_KEY_T);

        actions.add("commands");
        controls.put("commands", GLFW.GLFW_KEY_SLASH);
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;
        float buttonY = height - 50.0f;

        // Bouton pour sauvegarder les contrôles
        saveButton = new GuiButton(
                (width - buttonWidth * 3 - 40.0f) / 2.0f,
                buttonY,
                buttonWidth,
                buttonHeight,
                "Sauvegarder"
        );
        saveButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f)); // Vert
        saveButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        saveButton.setOnClick(button -> saveControls());

        // Bouton pour réinitialiser les contrôles
        resetButton = new GuiButton(
                (width - buttonWidth * 3 - 40.0f) / 2.0f + buttonWidth + 20.0f,
                buttonY,
                buttonWidth,
                buttonHeight,
                "Réinitialiser"
        );
        resetButton.setNormalColor(new Vector4f(0.8f, 0.6f, 0.2f, 1.0f)); // Orange
        resetButton.setHoverColor(new Vector4f(1.0f, 0.8f, 0.4f, 1.0f));
        resetButton.setOnClick(button -> resetControls());

        // Bouton pour retourner au menu précédent
        backButton = new GuiButton(
                (width - buttonWidth * 3 - 40.0f) / 2.0f + buttonWidth * 2 + 40.0f,
                buttonY,
                buttonWidth,
                buttonHeight,
                "Retour"
        );
        backButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f)); // Rouge
        backButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        backButton.setOnClick(button -> valtherion.openMainMenu());

        // Ajouter les boutons à la GUI
        addElement(saveButton);
        addElement(resetButton);
        addElement(backButton);
    }

    @Override
    public void render() {
        if (!isVisible) {
            return;
        }

        // Dessiner le fond de l'interface
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner les contrôles
        drawControls();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(
                backgroundColor.x,
                backgroundColor.y,
                backgroundColor.z,
                backgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le titre de l'interface.
     */
    private void drawTitle() {
        if (fontRenderer == null) {
            return;
        }

        String title = "Contrôles";
        float titleX = (width - fontRenderer.getTextWidth(title)) / 2.0f;
        float titleY = height - 30.0f;

        fontRenderer.setTextColor(new Vector4f(1.0f, 0.8f, 0.0f, 1.0f)); // Or
        fontRenderer.renderText(title, titleX, titleY);
    }

    /**
     * Dessine les contrôles.
     */
    private void drawControls() {
        if (fontRenderer == null) {
            return;
        }

        float startY = height - 80.0f;
        float lineHeight = 20.0f;

        for (String action : actions) {
            String actionName = getActionName(action);
            String keyName = GLFW.glfwGetKeyName(controls.get(action), 0);

            // Dessiner le nom de l'action
            fontRenderer.setTextColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
            fontRenderer.renderText(actionName, 50.0f, startY);

            // Dessiner la touche associée
            fontRenderer.setTextColor(new Vector4f(0.8f, 0.8f, 0.8f, 1.0f));
            fontRenderer.renderText(keyName, width / 2.0f, startY);

            // Dessiner un indicateur si l'action est sélectionnée
            if (selectedAction != null && selectedAction.equals(action)) {
                fontRenderer.setTextColor(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)); // Rouge
                fontRenderer.renderText(">", 30.0f, startY);
            }

            startY -= lineHeight;
        }
    }

    /**
     * Récupère le nom d'une action.
     * @param action Action.
     * @return Nom de l'action.
     */
    private String getActionName(String action) {
        switch (action) {
            case "forward": return "Avancer";
            case "backward": return "Reculer";
            case "left": return "Aller à gauche";
            case "right": return "Aller à droite";
            case "jump": return "Sauter";
            case "sprint": return "Courir";
            case "attack": return "Attaquer";
            case "inventory": return "Ouvrir l'inventaire";
            case "chat": return "Ouvrir le chat";
            case "commands": return "Ouvrir les commandes";
            default: return action;
        }
    }

    /**
     * Gère les entrées clavier pour l'interface des contrôles.
     * @param key Touche pressée.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     */
    @Override
    public void handleKeyInput(int key, int action) {
        if (!isVisible || !isActive) {
            return;
        }

        if (action == GLFW.GLFW_PRESS) {
            if (isRebinding) {
                // Réassigner la touche à l'action sélectionnée
                if (key != GLFW.GLFW_KEY_ESCAPE) {
                    controls.put(selectedAction, key);
                    isRebinding = false;
                    selectedAction = null;
                } else {
                    // Annuler la réassignation
                    isRebinding = false;
                    selectedAction = null;
                }
            } else {
                // Sélectionner une action pour la réassignation
                for (String actionName : actions) {
                    if (controls.get(actionName) == key) {
                        selectedAction = actionName;
                        isRebinding = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Sauvegarde les contrôles.
     */
    private void saveControls() {
        // Dans un jeu réel, tu sauvegarderais les contrôles dans un fichier
        System.out.println("Contrôles sauvegardés.");
    }

    /**
     * Réinitialise les contrôles aux valeurs par défaut.
     */
    private void resetControls() {
        controls.clear();
        initDefaultControls();
        selectedAction = null;
        isRebinding = false;
    }

    // --- Getters et Setters ---

    public Map<String, Integer> getControls() {
        return controls;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    public boolean isRebinding() {
        return isRebinding;
    }

    public void setRebinding(boolean rebinding) {
        this.isRebinding = rebinding;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    /**
     * Nettoie les ressources de l'interface des contrôles.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (fontRenderer != null) {
            fontRenderer.cleanup();
        }
        controls.clear();
        actions.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface des contrôles.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiControls{" +
                "controls=" + controls.size() +
                ", selectedAction='" + selectedAction + '\'' +
                ", isRebinding=" + isRebinding +
                '}';
    }
}
