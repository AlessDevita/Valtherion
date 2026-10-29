package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour afficher les coordonnées du joueur dans le HUD de Valtherion.
 */
public class CoordinatesDisplay extends HUD.HUDElement {

    // Joueur associé à l'affichage des coordonnées
    private final Player player;

    // Couleur du texte des coordonnées
    private Vector3f textColor;

    /**
     * Constructeur de l'affichage des coordonnées.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de l'élément.
     * @param height Hauteur de l'élément.
     * @param player Joueur associé.
     */
    public CoordinatesDisplay(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.textColor = new Vector3f(1.0f, 1.0f, 1.0f); // Blanc
    }

    /**
     * Met à jour l'affichage des coordonnées.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour l'affichage des coordonnées
    }

    /**
     * Rend l'affichage des coordonnées.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Récupérer les coordonnées du joueur
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();

        // Formater les coordonnées
        String coordinatesText = String.format("X: %.1f, Y: %.1f, Z: %.1f", playerX, playerY, playerZ);

        // Rendre le texte des coordonnées
        fontRenderer.renderText(
                coordinatesText,
                x,
                y,
                0.8f,
                textColor
        );

        System.out.println("Rendu des coordonnées : " + coordinatesText);
    }

    // --- Getters et Setters ---

    public Vector3f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector3f textColor) {
        this.textColor = textColor;
    }
}