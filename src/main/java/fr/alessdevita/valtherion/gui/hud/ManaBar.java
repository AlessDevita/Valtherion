package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour la barre de mana dans le HUD de Valtherion.
 * Affiche le niveau de mana du joueur.
 */
public class ManaBar extends HUD.HUDElement {

    // Joueur associé à la barre de mana
    private final Player player;

    // Mana actuel du joueur
    private float currentMana;

    // Mana maximal du joueur
    private float maxMana;

    // Couleur de la barre de mana
    private Vector3f manaColor;

    // Couleur de fond de la barre
    private Vector3f backgroundColor;

    // Couleur de la bordure
    private Vector3f borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    /**
     * Constructeur de la barre de mana.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param player Joueur associé.
     */
    public ManaBar(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.currentMana = 100.0f; // Mana initial
        this.maxMana = 100.0f;
        this.manaColor = new Vector3f(0.0f, 0.0f, 1.0f); // Bleu
        this.backgroundColor = new Vector3f(0.2f, 0.2f, 0.2f); // Gris foncé
        this.borderColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.borderThickness = 1.0f;
    }

    /**
     * Met à jour la barre de mana.
     */
    @Override
    public void update() {
        // Mettre à jour le mana actuel (ex: régénération ou utilisation)
        // currentMana = player.getMana(); // À implémenter dans la classe Player
    }

    /**
     * Rend la barre de mana.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Calculer le pourcentage de mana
        float manaPercentage = currentMana / maxMana;

        // Calculer la largeur de la barre de mana
        float manaWidth = width * manaPercentage;

        // Logique pour rendre la barre de mana
        System.out.println("Rendu de la barre de mana : " + (manaPercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur du mana : " + manaColor);

        // Rendre le texte du mana
        String manaText = (int) currentMana + "/" + (int) maxMana;
        fontRenderer.renderText(
                manaText,
                x + width / 2 - 20,
                y + height / 2 - 10,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    // --- Getters et Setters ---

    public float getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(float currentMana) {
        this.currentMana = Math.max(0, Math.min(currentMana, maxMana));
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
        this.currentMana = Math.min(currentMana, maxMana);
    }

    public Vector3f getManaColor() {
        return manaColor;
    }

    public void setManaColor(Vector3f manaColor) {
        this.manaColor = manaColor;
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