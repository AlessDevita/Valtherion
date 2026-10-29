package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour la barre de vie dans le HUD de Valtherion.
 */
public class HealthBar extends HUD.HUDElement {

    // Joueur associé à la barre de vie
    private final Player player;

    // Couleur de la barre de vie
    private Vector3f healthColor;

    // Couleur de fond de la barre
    private Vector3f backgroundColor;

    // Couleur de la bordure
    private Vector3f borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    /**
     * Constructeur de la barre de vie.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param player Joueur associé.
     */
    public HealthBar(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.healthColor = new Vector3f(1.0f, 0.0f, 0.0f); // Rouge
        this.backgroundColor = new Vector3f(0.2f, 0.2f, 0.2f); // Gris foncé
        this.borderColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.borderThickness = 1.0f;
    }

    /**
     * Met à jour la barre de vie.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la barre de vie
    }

    /**
     * Rend la barre de vie.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Calculer le pourcentage de vie
        float healthPercentage = player.getHealth() / player.getMaxHealth();

        // Calculer la largeur de la barre de vie
        float healthWidth = width * healthPercentage;

        // Logique pour rendre la barre de vie
        System.out.println("Rendu de la barre de vie : " + (healthPercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur de la vie : " + healthColor);

        // Rendre le texte de la vie
        String healthText = (int) player.getHealth() + "/" + (int) player.getMaxHealth();
        fontRenderer.renderText(
                healthText,
                x + width / 2 - 20,
                y + height / 2 - 10,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    // --- Getters et Setters ---

    public Vector3f getHealthColor() {
        return healthColor;
    }

    public void setHealthColor(Vector3f healthColor) {
        this.healthColor = healthColor;
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vector3f getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Vector3f borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }
}