package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;

import org.joml.Vector3f;

import java.util.*;

/**
 * Classe de base pour le HUD (Head-Up Display) dans Valtherion.
 * Gère l'affichage des barres de vie, d'expérience, de nourriture, et d'autres éléments du HUD.
 */
public class HUD {

    // Joueur associé au HUD
    private final Player player;

    // Monde associé au HUD
    private final World world;

    // Rendu des textes
    private final FontRenderer fontRenderer;

    // Liste des éléments du HUD
    private final List<HUDElement> elements;

    // État du HUD (visible ou masqué)
    private boolean isVisible;

    /**
     * Constructeur du HUD.
     * @param player Joueur associé.
     * @param world Monde associé.
     * @param fontRenderer Rendu des textes.
     */
    public HUD(Player player, World world, FontRenderer fontRenderer) {
        this.player = player;
        this.world = world;
        this.fontRenderer = fontRenderer;
        this.elements = new ArrayList<>();
        this.isVisible = true;

        // Initialiser les éléments du HUD
        initDefaultElements();
    }

    /**
     * Initialise les éléments par défaut du HUD.
     */
    private void initDefaultElements() {
        // Ajouter la barre de vie
        elements.add(new HealthBar(10, 10, 200, 20, player));

        // Ajouter la barre d'expérience
        elements.add(new ExperienceBar(10, 40, 200, 10, player));

        // Ajouter la barre de nourriture
        elements.add(new HungerBar(10, 60, 200, 10, player));

        // Ajouter l'affichage des FPS
        elements.add(new FPSCounter(10, 80, 100, 20));

        // Ajouter l'affichage des coordonnées
        elements.add(new CoordinatesDisplay(10, 110, 200, 20, player));
    }

    /**
     * Met à jour le HUD.
     */
    public void update() {
        if (!isVisible) {
            return;
        }

        // Mettre à jour tous les éléments du HUD
        for (HUDElement element : elements) {
            element.update();
        }
    }

    /**
     * Rend le HUD.
     */
    public void render() {
        if (!isVisible) {
            return;
        }

        // Rendre tous les éléments du HUD
        for (HUDElement element : elements) {
            element.render(fontRenderer);
        }
    }

    /**
     * Ajoute un élément au HUD.
     * @param element Élément à ajouter.
     */
    public void addElement(HUDElement element) {
        elements.add(element);
    }

    /**
     * Retire un élément du HUD.
     * @param element Élément à retirer.
     */
    public void removeElement(HUDElement element) {
        elements.remove(element);
    }

    /**
     * Définit la visibilité du HUD.
     * @param visible true pour afficher le HUD, false pour le masquer.
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    // --- Classe de base pour les éléments du HUD ---
    public abstract static class HUDElement {
        protected float x, y; // Position de l'élément
        protected float width, height; // Taille de l'élément

        public HUDElement(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        // Méthode pour mettre à jour l'élément
        public abstract void update();

        // Méthode pour rendre l'élément
        public abstract void render(FontRenderer fontRenderer);
    }

    // --- Getters et Setters ---

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public List<HUDElement> getElements() {
        return elements;
    }

    public boolean isVisible() {
        return isVisible;
    }
}