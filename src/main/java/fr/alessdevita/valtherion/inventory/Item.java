package fr.alessdevita.valtherion.inventory;

/**
 * Classe de base pour les objets dans Valtherion.
 * Représente un objet qui peut être placé dans un inventaire.
 */
public class Item extends fr.alessdevita.valtherion.item.Item {

    // ID unique de l'objet
    private final String id;

    // Nom de l'objet
    private String name;

    // Description de l'objet
    private String description;

    // Type de l'objet (ex: BLOCK, TOOL, WEAPON, FOOD, etc.)
    private ItemType type;

    // Texture de l'objet
    private String texture;

    // Modèle 3D de l'objet
    private String model;

    // Poids de l'objet
    private float weight;

    // Taille maximale de la pile (stack)
    private int maxStackSize;

    // Indique si l'objet est empilable
    private boolean isStackable;

    // Indique si l'objet est utilisable
    private boolean isUsable;

    // Indique si l'objet est jetable
    private boolean isDroppable;

    public Item(String id) {
        this.id = id;
    }

    public void setDamage(int i) {
    }

    public void setDurability(int i) {
    }

    public void addEffect(ItemEffect heal) {
    }

    public void setAttackSpeed(float v) {
    }

    public void setAttribute(String armor, int i) {
    }

    public Rarity getRarity() {
        return null;
    }

    // Énumération des types d'objets
    public enum ItemType {
        BLOCK,       // Bloc
        TOOL,        // Outil
        WEAPON,      // Arme
        ARMOR,       // Armure
        FOOD,        // Nourriture
        POTION,      // Potion
        MATERIAL,    // Matériau
        MISC         // Divers
    }

    /**
     * Constructeur de l'objet.
     * @param id ID unique de l'objet.
     * @param name Nom de l'objet.
     * @param type Type de l'objet.
     */
    public Item(String id, String name, ItemType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = "";
        this.texture = "items/" + id + ".png";
        this.model = "items/" + id + ".obj";
        this.weight = 1.0f;
        this.maxStackSize = 64;
        this.isStackable = true;
        this.isUsable = false;
        this.isDroppable = true;
    }

    /**
     * Utilise l'objet.
     * @return true si l'objet a été utilisé, false sinon.
     */
    public boolean use() {
        if (!isUsable) {
            return false;
        }

        // Logique pour utiliser l'objet
        // Exemple : Boire une potion, manger de la nourriture, etc.
        System.out.println("Utilisation de l'objet : " + name);
        return true;
    }

    /**
     * Jette l'objet.
     * @return true si l'objet a été jeté, false sinon.
     */
    public boolean drop() {
        if (!isDroppable) {
            return false;
        }

        // Logique pour jeter l'objet
        System.out.println("L'objet a été jeté : " + name);
        return true;
    }

    // --- Getters et Setters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        if (maxStackSize < 1) throw new IllegalArgumentException("La taille de pile doit être positive");
        this.maxStackSize = maxStackSize;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public void setStackable(boolean stackable) {
        isStackable = stackable;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

    public boolean isDroppable() {
        return isDroppable;
    }

    public void setDroppable(boolean droppable) {
        isDroppable = droppable;
    }

    /**
     * Retourne une représentation textuelle de l'objet.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", weight=" + weight +
                ", maxStackSize=" + maxStackSize +
                '}';
    }

    public static class Rarity {
        public static final Rarity COMMON = new Rarity("COMMON", "Commun", 0xFFFFFF);
        public static final Rarity UNCOMMON = new Rarity("UNCOMMON", "Peu Commun", 0x1EFF00);
        public static final Rarity RARE = new Rarity("RARE", "Rare", 0x0070DD);
        public static final Rarity EPIC = new Rarity("EPIC", "Épique", 0xA335EE);
        public static final Rarity LEGENDARY = new Rarity("LEGENDARY", "Légendaire", 0xFF8000);

        private final String id;
        private final String displayName;
        private final int color;

        public Rarity(String id, String displayName, int color) {
            this.id = id;
            this.displayName = displayName;
            this.color = color;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getColor() {
            return color;
        }
    }

    public static class ItemEffect {
        public ItemEffect(String heal, float v, int i) {

        }
    }
}
