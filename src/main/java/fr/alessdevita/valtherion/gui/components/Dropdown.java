package fr.alessdevita.valtherion.gui.components;

import fr.alessdevita.valtherion.gui.GUI;

import java.util.*;

/**
 * Composant pour une liste déroulante dans Valtherion.
 * Permet de sélectionner une option parmi plusieurs.
 */
public class Dropdown extends GUI.GUIElement {

    // Options de la liste déroulante
    private final List<String> options;

    // Option actuellement sélectionnée
    private int selectedIndex;

    // Indique si la liste est ouverte
    private boolean isOpen;

    // Couleur du texte
    private int textColor;

    // Couleur de fond
    private int backgroundColor;

    // Couleur de fond au survol
    private int hoverBackgroundColor;

    // Couleur de la bordure
    private int borderColor;

    // Action à exécuter lorsque l'option change
    private Runnable onChangeAction;

    /**
     * Constructeur de la liste déroulante.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la liste.
     * @param height Hauteur de la liste.
     * @param options Options de la liste.
     */
    public Dropdown(float x, float y, float width, float height, List<String> options) {
        super(x, y, width, height);
        this.options = new ArrayList<>(options);
        this.selectedIndex = 0;
        this.isOpen = false;
        this.textColor = 0xFFFFFF; // Blanc
        this.backgroundColor = 0x505050; // Gris
        this.hoverBackgroundColor = 0x707070; // Gris clair
        this.borderColor = 0x000000; // Noir
        this.onChangeAction = null;
    }

    /**
     * Met à jour la liste déroulante.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la liste déroulante
    }

    /**
     * Rend la liste déroulante.
     */
    @Override
    public void render() {
        // Logique pour rendre la liste déroulante
        System.out.println("Rendu de la liste déroulante");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Option sélectionnée : " + getSelectedOption());
        System.out.println("État : " + (isOpen ? "ouverte" : "fermée"));
    }

    /**
     * Gère un clic sur la liste déroulante.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     */
    @Override
    public void onClick(float mouseX, float mouseY) {
        if (contains(mouseX, mouseY)) {
            if (isOpen) {
                // Sélectionner une option
                int hoveredIndex = getHoveredOptionIndex(mouseX, mouseY);
                if (hoveredIndex != -1) {
                    selectedIndex = hoveredIndex;
                    isOpen = false;

                    // Exécuter l'action si elle est définie
                    if (onChangeAction != null) {
                        onChangeAction.run();
                    }
                }
            } else {
                // Ouvrir la liste
                isOpen = true;
            }
        } else {
            // Fermer la liste si on clique à l'extérieur
            isOpen = false;
        }
    }

    /**
     * Récupère l'index de l'option survolée.
     * @param mouseX Coordonnée X de la souris.
     * @param mouseY Coordonnée Y de la souris.
     * @return Index de l'option survolée ou -1 si aucune.
     */
    private int getHoveredOptionIndex(float mouseX, float mouseY) {
        // Logique pour déterminer quelle option est survolée
        // Exemple : Calculer l'index en fonction de (mouseX, mouseY)
        // À implémenter selon ta logique de rendu
        return -1;
    }

    /**
     * Récupère l'option actuellement sélectionnée.
     * @return Option sélectionnée.
     */
    public String getSelectedOption() {
        if (selectedIndex >= 0 && selectedIndex < options.size()) {
            return options.get(selectedIndex);
        }
        return null;
    }

    /**
     * Définit l'option sélectionnée par son index.
     * @param index Index de l'option.
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < options.size()) {
            this.selectedIndex = index;

            // Exécuter l'action si elle est définie
            if (onChangeAction != null) {
                onChangeAction.run();
            }
        }
    }

    /**
     * Définit l'action à exécuter lorsque l'option change.
     * @param action Action à exécuter.
     */
    public void setOnChangeAction(Runnable action) {
        this.onChangeAction = action;
    }

    /**
     * Ajoute une option à la liste.
     * @param option Option à ajouter.
     */
    public void addOption(String option) {
        options.add(option);
    }

    /**
     * Retire une option de la liste.
     * @param option Option à retirer.
     */
    public void removeOption(String option) {
        options.remove(option);

        // Mettre à jour l'index sélectionné si nécessaire
        if (selectedIndex >= options.size()) {
            selectedIndex = options.size() - 1;
        }
    }

    // --- Getters et Setters ---

    public List<String> getOptions() {
        return options;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(int hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }
}