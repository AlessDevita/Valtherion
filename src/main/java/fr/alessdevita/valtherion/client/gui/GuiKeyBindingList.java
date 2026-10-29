package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Liste des liaisons de touches (key bindings) dans Valtherion.
 * Permet d'afficher et de modifier les touches assignées à chaque action.
 */
public class GuiKeyBindingList extends Gui {

    // Map des actions et de leurs touches associées
    private final Map<String, Integer> keyBindings;

    // Action actuellement sélectionnée pour la réassignation
    private String selectedAction;

    // Indique si la liste est en mode de réassignation
    private boolean isRebinding;

    // Bouton pour sauvegarder les liaisons
    private GuiButton saveButton;

    // Bouton pour réinitialiser les liaisons
    private GuiButton resetButton;

    // Bouton pour retourner au menu précédent
    private GuiButton backButton;

    /**
     * Constructeur de la liste des liaisons de touches.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiKeyBindingList(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.keyBindings = new HashMap<>();
        this.selectedAction = null;
        this.isRebinding = false;

        // Initialiser les liaisons par défaut
        initDefaultKeyBindings();

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les liaisons de touches par défaut.
     */
    private void initDefaultKeyBindings() {
        // Actions de mouvement
        keyBindings.put("forward", GLFW.GLFW_KEY_W);
        keyBindings.put("backward", GLFW.GLFW_KEY_S);
        keyBindings.put("left", GLFW.GLFW_KEY_A);
        keyBindings.put("right", GLFW.GLFW_KEY_D);
        keyBindings.put("jump", GLFW.GLFW_KEY_SPACE);
        keyBindings.put("sprint", GLFW.GLFW_KEY_LEFT_SHIFT);

        // Actions de combat
        keyBindings.put("attack", GLFW.GLFW_KEY_LEFT_CONTROL);

        // Actions diverses
        keyBindings.put("inventory", GLFW.GLFW_KEY_E);
        keyBindings.put("chat", GLFW.GLFW_KEY_T);
        keyBindings.put("commands", GLFW.GLFW_KEY_SLASH);
        keyBindings.put("menu", GLFW.GLFW_KEY_ESCAPE);
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour sauvegarder les liaisons
        saveButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Sauvegarder"
        );
        saveButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        saveButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        saveButton.setOnClick(button -> saveKeyBindings());

        // Bouton pour réinitialiser les liaisons
        resetButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Réinitialiser"
        );
        resetButton.setNormalColor(new Vector4f(0.8f, 0.6f, 0.2f, 1.0f));
        resetButton.setHoverColor(new Vector4f(1.0f, 0.8f, 0.4f, 1.0f));
        resetButton.setOnClick(button -> resetKeyBindings());

        // Bouton pour retourner au menu précédent
        backButton = new GuiButton(
                width / 2.0f,
                height - 100.0f,
                buttonWidth,
                buttonHeight,
                "Retour"
        );
        backButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        backButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        backButton.setOnClick(button -> valtherion.openOptionsMenu());

        // Ajouter les boutons à la GUI
        addElement(saveButton);
        addElement(resetButton);
        addElement(backButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner les liaisons de touches
        drawKeyBindings();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.9f);
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
        // Dessiner un rectangle pour simuler le titre "Liaisons de Touches"
        float titleWidth = 200.0f;
        float titleHeight = 20.0f;
        float titleX = (width - titleWidth) / 2.0f;
        float titleY = height - 30.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY + titleHeight);
        GL11.glVertex2f(titleX, titleY + titleHeight);
        GL11.glEnd();
    }

    /**
     * Dessine les liaisons de touches.
     */
    private void drawKeyBindings() {
        float startX = 50.0f;
        float startY = height - 100.0f;
        float lineHeight = 20.0f;

        for (Map.Entry<String, Integer> entry : keyBindings.entrySet()) {
            String action = entry.getKey();
            int key = entry.getValue();
            String keyName = GLFW.glfwGetKeyName(key, 0);

            // Dessiner le nom de l'action
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, startY);
            GL11.glVertex2f(startX + 150.0f, startY);
            GL11.glVertex2f(startX + 150.0f, startY + lineHeight);
            GL11.glVertex2f(startX, startY + lineHeight);
            GL11.glEnd();

            // Dessiner la touche associée
            GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX + 160.0f, startY);
            GL11.glVertex2f(startX + 160.0f + 100.0f, startY);
            GL11.glVertex2f(startX + 160.0f + 100.0f, startY + lineHeight);
            GL11.glVertex2f(startX + 160.0f, startY + lineHeight);
            GL11.glEnd();

            // Dessiner un indicateur si l'action est sélectionnée
            if (selectedAction != null && selectedAction.equals(action)) {
                GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(startX - 10.0f, startY);
                GL11.glVertex2f(startX, startY);
                GL11.glVertex2f(startX, startY + lineHeight);
                GL11.glVertex2f(startX - 10.0f, startY + lineHeight);
                GL11.glEnd();
            }

            startY -= lineHeight + 5.0f;
        }
    }

    /**
     * Gère les entrées clavier pour la réassignation des touches.
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
                    keyBindings.put(selectedAction, key);
                    isRebinding = false;
                    selectedAction = null;
                } else {
                    // Annuler la réassignation
                    isRebinding = false;
                    selectedAction = null;
                }
            } else {
                // Sélectionner une action pour la réassignation
                for (String actionName : keyBindings.keySet()) {
                    if (keyBindings.get(actionName) == key) {
                        selectedAction = actionName;
                        isRebinding = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Sauvegarde les liaisons de touches.
     */
    private void saveKeyBindings() {
        // Dans un jeu réel, tu sauvegarderais les liaisons dans un fichier
        System.out.println("Liaisons de touches sauvegardées.");
    }

    /**
     * Réinitialise les liaisons de touches aux valeurs par défaut.
     */
    private void resetKeyBindings() {
        keyBindings.clear();
        initDefaultKeyBindings();
        selectedAction = null;
        isRebinding = false;
    }

    // --- Getters et Setters ---

    public Map<String, Integer> getKeyBindings() {
        return keyBindings;
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

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        keyBindings.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiKeyBindingList{" +
                "keyBindings=" + keyBindings.size() +
                ", selectedAction='" + selectedAction + '\'' +
                '}';
    }
}
