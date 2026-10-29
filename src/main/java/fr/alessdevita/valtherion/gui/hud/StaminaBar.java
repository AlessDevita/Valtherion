package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;

import org.joml.Vector3f;

/**
 * Composant pour la barre d'endurance dans le HUD de Valtherion.
 * Affiche le niveau d'endurance du joueur.
 */
public class StaminaBar extends HUD.HUDElement {

    // Joueur associé à la barre d'endurance
    private final Player player;

    // Endurance actuelle du joueur
    private float currentStamina;

    // Endurance maximale du joueur
    private float maxStamina;

    // Couleur de la barre d'endurance
    private Vector3f staminaColor;

    // Couleur de fond de la barre
    private Vector3f backgroundColor;

    // Couleur de la bordure
    private Vector3f borderColor;

    // Épaisseur de la bordure
    private float borderThickness;

    // Indique si l'endurance est en train de se régénérer
    private boolean isRegenerating;

    // Vitesse de régénération de l'endurance
    private float regenerationRate;

    /**
     * Constructeur de la barre d'endurance.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la barre.
     * @param height Hauteur de la barre.
     * @param player Joueur associé.
     */
    public StaminaBar(float x, float y, float width, float height, Player player) {
        super(x, y, width, height);
        this.player = player;
        this.currentStamina = 100.0f; // Endurance initiale
        this.maxStamina = 100.0f;
        this.staminaColor = new Vector3f(0.0f, 1.0f, 0.0f); // Vert
        this.backgroundColor = new Vector3f(0.2f, 0.2f, 0.2f); // Gris foncé
        this.borderColor = new Vector3f(0.0f, 0.0f, 0.0f); // Noir
        this.borderThickness = 1.0f;
        this.isRegenerating = false;
        this.regenerationRate = 0.5f; // Régénération de 0.5 par seconde
    }

    /**
     * Met à jour la barre d'endurance.
     */
    @Override
    public void update() {
        // Régénérer l'endurance si elle est en dessous du maximum
        if (currentStamina < maxStamina) {
            isRegenerating = true;
            currentStamina += regenerationRate * 0.016f; // Supposons un deltaTime de 16ms (60 FPS)
            currentStamina = Math.min(currentStamina, maxStamina);
        } else {
            isRegenerating = false;
        }
    }

    /**
     * Utilise de l'endurance.
     * @param amount Montant d'endurance à utiliser.
     */
    public void useStamina(float amount) {
        currentStamina -= amount;
        currentStamina = Math.max(0, currentStamina);
    }

    /**
     * Rend la barre d'endurance.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Calculer le pourcentage d'endurance
        float staminaPercentage = currentStamina / maxStamina;

        // Calculer la largeur de la barre d'endurance
        float staminaWidth = width * staminaPercentage;

        // Logique pour rendre la barre d'endurance
        System.out.println("Rendu de la barre d'endurance : " + (staminaPercentage * 100) + "%");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);
        System.out.println("Couleur de l'endurance : " + staminaColor);

        // Rendre le texte de l'endurance
        String staminaText = (int) currentStamina + "/" + (int) maxStamina;
        fontRenderer.renderText(
                staminaText,
                x + width / 2 - 20,
                y + height / 2 - 10,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    // --- Getters et Setters ---

    public float getCurrentStamina() {
        return currentStamina;
    }

    public void setCurrentStamina(float currentStamina) {
        this.currentStamina = Math.max(0, Math.min(currentStamina, maxStamina));
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(float maxStamina) {
        this.maxStamina = maxStamina;
        this.currentStamina = Math.min(currentStamina, maxStamina);
    }

    public Vector3f getStaminaColor() {
        return staminaColor;
    }

    public void setStaminaColor(Vector3f staminaColor) {
        this.staminaColor = staminaColor;
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

    public boolean isRegenerating() {
        return isRegenerating;
    }

    public float getRegenerationRate() {
        return regenerationRate;
    }

    public void setRegenerationRate(float regenerationRate) {
        this.regenerationRate = regenerationRate;
    }
}