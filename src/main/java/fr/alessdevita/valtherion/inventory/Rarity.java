package fr.alessdevita.valtherion.inventory;

import org.joml.Vector3f;

/**
 * Énumération des niveaux de rareté des objets dans Valtherion.
 * Définit les catégories de rareté pour les objets, avec des propriétés comme la couleur et la valeur.
 */
public enum Rarity {

    // --- Niveaux de rareté ---

    /**
     * Commun : Objet très courant.
     * Exemples : Bâton, pierre, terre, herbe, etc.
     */
    COMMON(
            "Commun",
            new Vector3f(0.8f, 0.8f, 0.8f),  // Gris
            1.0f,
            "Objet très courant, facile à trouver."
    ),

    /**
     * Peu commun : Objet moins courant.
     * Exemples : Minerai de fer, planche, viande crue, etc.
     */
    UNCOMMON(
            "Peu commun",
            new Vector3f(0.0f, 1.0f, 0.0f),  // Vert
            2.0f,
            "Objet moins courant, nécessite un peu d'effort pour être trouvé."
    ),

    /**
     * Rare : Objet difficile à trouver.
     * Exemples : Minerai d'or, diamant, émeraude, etc.
     */
    RARE(
            "Rare",
            new Vector3f(0.0f, 0.0f, 1.0f),  // Bleu
            4.0f,
            "Objet rare, difficile à trouver."
    ),

    /**
     * Épique : Objet très puissant ou précieux.
     * Exemples : Équipement enchanté, objets légendaires, etc.
     */
    EPIC(
            "Épique",
            new Vector3f(0.8f, 0.0f, 1.0f),  // Violet
            8.0f,
            "Objet très puissant ou précieux, très rare."
    ),

    /**
     * Légendaire : Objet unique ou extrêmement puissant.
     * Exemples : Artefacts, objets uniques, etc.
     */
    LEGENDARY(
            "Légendaire",
            new Vector3f(1.0f, 0.8f, 0.0f),  // Or
            16.0f,
            "Objet unique ou extrêmement puissant, presque impossible à trouver."
    ),

    // --- Niveaux de rareté spéciaux ---

    /**
     * Unique : Objet unique, il n'en existe qu'un seul dans le jeu.
     * Exemples : Artefacts uniques, objets de quête spéciaux, etc.
     */
    UNIQUE(
            "Unique",
            new Vector3f(1.0f, 0.0f, 0.0f),  // Rouge
            32.0f,
            "Objet unique, il n'en existe qu'un seul dans le jeu."
    ),

    /**
     * Mythique : Objet mythique, souvent lié à des légendes ou des histoires.
     * Exemples : Objets divins, artefacts anciens, etc.
     */
    MYTHIC(
            "Mythique",
            new Vector3f(1.0f, 0.5f, 0.0f),  // Orange
            64.0f,
            "Objet mythique, souvent lié à des légendes ou des histoires."
    ),

    /**
     * Admin : Objet réservé aux administrateurs ou aux développeurs.
     * Exemples : Objets de debug, objets de test, etc.
     */
    ADMIN(
            "Admin",
            new Vector3f(0.5f, 0.5f, 0.5f),  // Gris clair
            0.0f,
            "Objet réservé aux administrateurs ou aux développeurs."
    );

    // Nom affiché du niveau de rareté
    private final String displayName;

    // Couleur associée au niveau de rareté (pour l'affichage)
    private final Vector3f color;

    // Multiplicateur de valeur pour les objets de ce niveau de rareté
    private final float valueMultiplier;

    // Description du niveau de rareté
    private final String description;

    /**
     * Constructeur de l'énumération.
     * @param displayName Nom affiché du niveau de rareté.
     * @param color Couleur associée au niveau de rareté.
     * @param valueMultiplier Multiplicateur de valeur.
     * @param description Description du niveau de rareté.
     */
    Rarity(String displayName, Vector3f color, float valueMultiplier, String description) {
        this.displayName = displayName;
        this.color = color;
        this.valueMultiplier = valueMultiplier;
        this.description = description;
    }

    // --- Getters ---

    public String getDisplayName() {
        return displayName;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getValueMultiplier() {
        return valueMultiplier;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Vérifie si ce niveau de rareté est supérieur ou égal à un autre.
     * @param other Niveau de rareté à comparer.
     * @return true si ce niveau de rareté est supérieur ou égal, false sinon.
     */
    public boolean isAtLeast(Rarity other) {
        return this.ordinal() >= other.ordinal();
    }

    /**
     * Vérifie si ce niveau de rareté est supérieur à un autre.
     * @param other Niveau de rareté à comparer.
     * @return true si ce niveau de rareté est supérieur, false sinon.
     */
    public boolean isHigherThan(Rarity other) {
        return this.ordinal() > other.ordinal();
    }

    /**
     * Récupère le niveau de rareté suivant.
     * @return Niveau de rareté suivant ou null si c'est le dernier.
     */
    public Rarity getNextRarity() {
        int nextIndex = this.ordinal() + 1;
        if (nextIndex < Rarity.values().length) {
            return Rarity.values()[nextIndex];
        }
        return null;
    }

    /**
     * Récupère le niveau de rareté précédent.
     * @return Niveau de rareté précédent ou null si c'est le premier.
     */
    public Rarity getPreviousRarity() {
        int previousIndex = this.ordinal() - 1;
        if (previousIndex >= 0) {
            return Rarity.values()[previousIndex];
        }
        return null;
    }

    /**
     * Applique le multiplicateur de valeur à une valeur de base.
     * @param baseValue Valeur de base.
     * @return Valeur ajustée en fonction du niveau de rareté.
     */
    public float applyValueMultiplier(float baseValue) {
        return baseValue * valueMultiplier;
    }
}