package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Classe de base pour les interfaces graphiques (GUI) dans Valtherion.
 * Gère le rendu 2D, les entrées utilisateur, et les éléments interactifs.
 */
public abstract class Gui {

    // Instance du jeu Valtherion
    protected Valtherion valtherion;

    // Largeur de la fenêtre
    protected int width;

    // Hauteur de la fenêtre
    protected int height;

    // Position de la GUI (x, y)
    protected Vector2f position;

    // Taille de la GUI (largeur, hauteur)
    protected Vector2f size;

    // Couleur de fond de la GUI
    protected Vector4f backgroundColor;

    // Indique si la GUI est visible
    protected boolean isVisible;

    // Indique si la GUI est active (peut recevoir des entrées)
    protected boolean isActive;

    // Liste des éléments de la GUI
    protected final List<GuiElement> elements;

    // Éléments actuellement sélectionnés (pour les interactions)
    protected GuiElement selectedElement;

    // Éléments actuellement survolés par la souris
    protected GuiElement hoveredElement;

    // Temps écoulé depuis la dernière mise à jour (en secondes)
    protected float timeSinceLastUpdate;

    /**
     * Constructeur de la classe Gui.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la GUI.
     * @param height Hauteur de la GUI.
     */
    public Gui(Valtherion valtherion, int width, int height) {
        this.valtherion = valtherion;
        this.width = width;
        this.height = height;
        this.position = new Vector2f(0.0f, 0.0f);
        this.size = new Vector2f(width, height);
        this.backgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.5f); // Noir semi-transparent
        this.isVisible = true;
        this.isActive = true;
        this.elements = new ArrayList<>();
        this.timeSinceLastUpdate = 0.0f;
    }

    public Gui() {
        this(null, 800, 600); // Valeurs par défaut
    }

    public static void drawRect(int x, int i, int i1, int i2, int i3) {
        int left = Math.min(x, i1);
        int right = Math.max(x, i1);
        int top = Math.min(i, i2);
        int bottom = Math.max(i, i2);
        float alpha = (float) (i3 >>> 24 & 255) / 255.0F;
        float red = (float) (i3 >>> 16 & 255) / 255.0F;
        float green = (float) (i3 >>> 8 & 255) / 255.0F;
        float blue = (float) (i3 & 255) / 255.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f((float) left, (float) bottom);
        GL11.glVertex2f((float) right, (float) bottom);
        GL11.glVertex2f((float) right, (float) top);
        GL11.glVertex2f((float) left, (float) top);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Initialise la GUI.
     */
    public void init() {
        // Initialiser les éléments de la GUI
        for (GuiElement element : elements) {
            element.init();
        }
    }

    /**
     * Met à jour la GUI.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isVisible) {
            return;
        }

        timeSinceLastUpdate += deltaTime;

        // Mettre à jour les éléments de la GUI
        for (GuiElement element : elements) {
            element.update(deltaTime);
        }
    }

    /**
     * Rend la GUI.
     */
    public void render() {
        if (!isVisible) {
            return;
        }

        // Sauvegarder l'état OpenGL
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Configurer la matrice de projection pour le rendu 2D
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        // Dessiner le fond de la GUI
        drawBackground();

        // Dessiner les éléments de la GUI
        for (GuiElement element : elements) {
            element.render();
        }

        // Restaurer l'état OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();

        GL11.glPopAttrib();
    }

    /**
     * Dessine le fond de la GUI.
     */
    protected void drawBackground() {
        GL11.glColor4f(
                backgroundColor.x,
                backgroundColor.y,
                backgroundColor.z,
                backgroundColor.w
        );

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(position.x, position.y);
        GL11.glVertex2f(position.x + size.x, position.y);
        GL11.glVertex2f(position.x + size.x, position.y + size.y);
        GL11.glVertex2f(position.x, position.y + size.y);
        GL11.glEnd();
    }

    /**
     * Gère les entrées clavier.
     * @param key Touche pressée.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     */
    public void handleKeyInput(int key, int action) {
        if (!isVisible || !isActive) {
            return;
        }

        // Gérer les entrées clavier pour les éléments de la GUI
        for (GuiElement element : elements) {
            element.handleKeyInput(key, action);
        }
    }

    /**
     * Gère les entrées souris (clics).
     * @param button Bouton de la souris.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     * @param x Position X de la souris.
     * @param y Position Y de la souris.
     */
    public void handleMouseInput(int button, int action, double x, double y) {
        if (!isVisible || !isActive) {
            return;
        }

        // Convertir les coordonnées de la souris en coordonnées relatives à la GUI
        float mouseX = (float) x - position.x;
        float mouseY = (float) (height - y) - position.y; // Inverser l'axe Y

        // Vérifier si la souris est dans les limites de la GUI
        if (mouseX >= 0 && mouseX <= size.x && mouseY >= 0 && mouseY <= size.y) {
            // Mettre à jour l'élément survolé
            hoveredElement = getElementAt(mouseX, mouseY);

            // Gérer les clics sur les éléments
            if (action == GLFW.GLFW_PRESS) {
                if (hoveredElement != null) {
                    hoveredElement.onClick(button);
                    selectedElement = hoveredElement;
                }
            }
        } else {
            hoveredElement = null;
        }
    }

    /**
     * Gère les mouvements de la souris.
     * @param x Position X de la souris.
     * @param y Position Y de la souris.
     */
    public void handleMouseMove(double x, double y) {
        if (!isVisible || !isActive) {
            return;
        }

        // Convertir les coordonnées de la souris en coordonnées relatives à la GUI
        float mouseX = (float) x - position.x;
        float mouseY = (float) (height - y) - position.y; // Inverser l'axe Y

        // Mettre à jour l'élément survolé
        if (mouseX >= 0 && mouseX <= size.x && mouseY >= 0 && mouseY <= size.y) {
            hoveredElement = getElementAt(mouseX, mouseY);
        } else {
            hoveredElement = null;
        }
    }

    /**
     * Récupère l'élément de la GUI à une position donnée.
     * @param x Position X relative à la GUI.
     * @param y Position Y relative à la GUI.
     * @return Élément de la GUI ou null si aucun élément n'est trouvé.
     */
    public GuiElement getElementAt(float x, float y) {
        for (GuiElement element : elements) {
            if (element.isVisible() && element.contains(x, y)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Ajoute un élément à la GUI.
     * @param element Élément à ajouter.
     */
    public void addElement(GuiElement element) {
        elements.add(element);
        element.setGui(this);
    }

    /**
     * Supprime un élément de la GUI.
     * @param element Élément à supprimer.
     */
    public void removeElement(GuiElement element) {
        elements.remove(element);
        element.setGui(null);
    }

    /**
     * Supprime tous les éléments de la GUI.
     */
    public void clearElements() {
        for (GuiElement element : elements) {
            element.setGui(null);
        }
        elements.clear();
    }

    /**
     * Ouvre la GUI.
     */
    public void open() {
        isVisible = true;
        isActive = true;
    }

    /**
     * Ferme la GUI.
     */
    public void close() {
        isVisible = false;
        isActive = false;
    }

    // --- Getters et Setters ---

    public Valtherion getValtherion() {
        return valtherion;
    }

    public void setValtherion(Valtherion valtherion) {
        this.valtherion = valtherion;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    public GuiElement getSelectedElement() {
        return selectedElement;
    }

    public GuiElement getHoveredElement() {
        return hoveredElement;
    }

    /**
     * Nettoie les ressources de la GUI.
     */
    public void cleanup() {
        clearElements();
    }

    /**
     * Retourne une représentation textuelle de la GUI.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Gui{" +
                "width=" + width +
                ", height=" + height +
                ", position=" + position +
                ", elements=" + elements.size() +
                ", isVisible=" + isVisible +
                '}';
    }

    // --- Classe interne pour les éléments de la GUI ---

    /**
     * Classe de base pour les éléments de la GUI.
     */
    public abstract static class GuiElement {

        // Position de l'élément (relative à la GUI)
        protected Vector2f position;

        // Taille de l'élément
        protected Vector2f size;

        // Couleur de l'élément
        protected Vector4f color;

        // Indique si l'élément est visible
        protected boolean isVisible;

        // Indique si l'élément est activé
        protected boolean isEnabled;

        // GUI à laquelle l'élément appartient
        protected Gui gui;

        /**
         * Constructeur de l'élément de la GUI.
         * @param x Position X.
         * @param y Position Y.
         * @param width Largeur.
         * @param height Hauteur.
         */
        public GuiElement(float x, float y, float width, float height) {
            this.position = new Vector2f(x, y);
            this.size = new Vector2f(width, height);
            this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc
            this.isVisible = true;
            this.isEnabled = true;
        }

        public GuiElement() {
            this(0.0f, 0.0f, 100.0f, 30.0f); // Valeurs par défaut
        }

        /**
         * Initialise l'élément.
         */
        public void init() {
            // À implémenter dans les classes dérivées
        }

        /**
         * Met à jour l'élément.
         * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
         */
        public void update(float deltaTime) {
            // À implémenter dans les classes dérivées
        }

        /**
         * Rend l'élément.
         */
        public void render() {
            if (!isVisible) {
                return;
            }

            // Dessiner un rectangle pour l'élément
            GL11.glColor4f(color.x, color.y, color.z, color.w);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();
        }

        /**
         * Gère les entrées clavier.
         * @param key Touche pressée.
         * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
         */
        public void handleKeyInput(int key, int action) {
            // À implémenter dans les classes dérivées
        }

        /**
         * Gère les clics de la souris.
         * @param button Bouton de la souris.
         */
        public void onClick(int button) {
            // À implémenter dans les classes dérivées
        }

        /**
         * Vérifie si l'élément contient un point donné.
         * @param x Position X.
         * @param y Position Y.
         * @return true si l'élément contient le point, false sinon.
         */
        public boolean contains(float x, float y) {
            return x >= position.x && x <= position.x + size.x &&
                    y >= position.y && y <= position.y + size.y;
        }

        // --- Getters et Setters ---

        public Vector2f getPosition() {
            return position;
        }

        public void setPosition(Vector2f position) {
            this.position = position;
        }

        public Vector2f getSize() {
            return size;
        }

        public void setSize(Vector2f size) {
            this.size = size;
        }

        public Vector4f getColor() {
            return color;
        }

        public void setColor(Vector4f color) {
            this.color = color;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean visible) {
            this.isVisible = visible;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            this.isEnabled = enabled;
        }

        public Gui getGui() {
            return gui;
        }

        public void setGui(Gui gui) {
            this.gui = gui;
        }
    }
}
