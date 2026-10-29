package fr.alessdevita.valtherion.inventory;

/**
 * Énumération des types d'objets dans Valtherion.
 * Définit les catégories principales des objets pour organiser et gérer leur comportement.
 */
public enum ItemType {

    // --- Catégories principales ---

    /**
     * Arme : Objet utilisé pour infliger des dégâts.
     * Exemples : Épée, hache, arc, flèche, etc.
     */
    WEAPON("Arme", true, true, false),

    /**
     * Outil : Objet utilisé pour interagir avec le monde.
     * Exemples : Pioche, hache, pelle, etc.
     */
    TOOL("Outil", true, true, false),

    /**
     * Nourriture : Objet comestible qui restaure la santé ou donne des effets.
     * Exemples : Pomme, pain, viande cuite, etc.
     */
    FOOD("Nourriture", true, true, true),

    /**
     * Bloc : Objet qui peut être placé dans le monde.
     * Exemples : Bloc de terre, bloc de pierre, bois, etc.
     */
    BLOCK("Bloc", true, true, false),

    /**
     * Matériau : Objet utilisé pour le craft ou la construction.
     * Exemples : Bâton, planche, lingot de fer, etc.
     */
    MATERIAL("Matériau", true, true, false),

    /**
     * Potion : Objet qui applique des effets temporaires.
     * Exemples : Potion de soin, potion de force, potion de vitesse, etc.
     */
    POTION("Potion", true, true, true),

    /**
     * Armure : Objet qui protège le joueur.
     * Exemples : Casque, plastron, jambières, bottes, etc.
     */
    ARMOR("Armure", true, false, false),

    /**
     * Objet divers : Catégorie pour les objets qui ne rentrent pas dans les autres catégories.
     * Exemples : Livre, clé, gemme, etc.
     */
    MISC("Divers", true, true, false),

    // --- Catégories avancées ---

    /**
     * Enchantement : Objet utilisé pour améliorer d'autres objets.
     * Exemples : Livre enchanté, pierre d'enchantement, etc.
     */
    ENCHANTMENT("Enchantement", true, false, false),

    /**
     * Monture : Objet qui permet de monter ou de se déplacer rapidement.
     * Exemples : Cheval, bateau, élytra, etc.
     */
    MOUNT("Monture", false, false, false),

    /**
     * Véhicule : Objet qui permet de se déplacer.
     * Exemples : Charrette, voiture, avion, etc.
     */
    VEHICLE("Véhicule", false, false, false),

    /**
     * Quête : Objet lié à une quête.
     * Exemples : Objet de quête, lettre, carte, etc.
     */
    QUEST("Quête", true, true, false),

    /**
     * Monnaie : Objet utilisé pour l'économie.
     * Exemples : Pièce d'or, lingot, gemme, etc.
     */
    CURRENCY("Monnaie", true, true, false),

    /**
     * Plante : Objet végétal.
     * Exemples : Graine, fleur, herbe, etc.
     */
    PLANT("Plante", true, true, true),

    /**
     * Minerai : Objet extrait des mines.
     * Exemples : Minerai de fer, minerai d'or, diamant, etc.
     */
    ORE("Minerai", true, true, false),

    /**
     * Gemme : Objet précieux.
     * Exemples : Diamant, émeraude, rubis, etc.
     */
    GEM("Gemme", true, true, false);

    // Nom affiché du type d'objet
    private final String displayName;

    // Indique si l'objet peut être empilé
    private final boolean isStackable;

    // Indique si l'objet peut être jeté
    private final boolean isDroppable;

    // Indique si l'objet peut être utilisé
    private final boolean isUsable;

    /**
     * Constructeur de l'énumération.
     * @param displayName Nom affiché du type d'objet.
     * @param isStackable Indique si l'objet peut être empilé.
     * @param isDroppable Indique si l'objet peut être jeté.
     * @param isUsable Indique si l'objet peut être utilisé.
     */
    ItemType(String displayName, boolean isStackable, boolean isDroppable, boolean isUsable) {
        this.displayName = displayName;
        this.isStackable = isStackable;
        this.isDroppable = isDroppable;
        this.isUsable = isUsable;
    }

    // --- Getters ---

    public String getDisplayName() {
        return displayName;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public boolean isDroppable() {
        return isDroppable;
    }

    public boolean isUsable() {
        return isUsable;
    }

    /**
     * Vérifie si ce type d'objet peut être utilisé comme ingrédient dans une recette de craft.
     * @return true si le type peut être utilisé comme ingrédient, false sinon.
     */
    public boolean isCraftingIngredient() {
        return this == MATERIAL || this == BLOCK || this == ORE || this == GEM || this == PLANT;
    }

    /**
     * Vérifie si ce type d'objet peut être équipé.
     * @return true si le type peut être équipé, false sinon.
     */
    public boolean isEquippable() {
        return this == WEAPON || this == ARMOR || this == TOOL;
    }
}