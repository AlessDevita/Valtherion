package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.utils.Vector2f;

/**
 * Classe de base pour tous les composants d'interface graphique.
 */
public abstract class GUIComponent {
    protected Vector2f position;
    protected Vector2f size;
    protected boolean visible = true;

    public GUIComponent(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    public abstract void update();
    public abstract void render();

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}