package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour la barre d'expérience dans le HUD de Valtherion.
 */
public class ExperienceBar extends HUD.HUDElement {

    // Joueur associé à la barre d'expérience
    private final Player player;

    // Couleur de la barre d'expérience
    private Vector3f experienceColor;

    // Couleur de fond de la barre
    private Vector3f backgroundColor;

    // Couleur de la bordure
    private Vector3f borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    /**
     * Constructeur de la barre d'expérience.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param player Joueur associé.
     */
    public ExperienceBar(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.experienceColor = new Vector3f(0.0f, 0.0f, 1.0f); // Bleu
        this.backgroundColor = new Vector3f(0.2f, 0.2f, 0.2f); // Gris foncé
        this.borderColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.borderThickness = 1.0f;
    }

    /**
     * Met à jour la barre d'expérience.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la barre d'expérience
    }

    /**
     * Rend la barre d'expérience.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Calculer le pourcentage d'expérience
        float experiencePercentage = (float) player.getExperience() / player.getExperienceToNextLevel();

        // Calculer la largeur de la barre d'expérience
        float experienceWidth = width * experiencePercentage;

        // Logique pour rendre la barre d'expérience
        System.out.println("Rendu de la barre d'expérience : " + (experiencePercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur de l'expérience : " + experienceColor);

        // Rendre le texte de l'expérience
        String experienceText = player.getExperience() + "/" + player.getExperienceToNextLevel();
        fontRenderer.renderText(
                experienceText,
                x + width / 2 - 30,
                y + height / 2 - 10,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    // --- Getters et Setters ---

    public Vector3f getExperienceColor() {
        return experienceColor;
    }

    public void setExperienceColor(Vector3f experienceColor) {
        this.experienceColor = experienceColor;
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