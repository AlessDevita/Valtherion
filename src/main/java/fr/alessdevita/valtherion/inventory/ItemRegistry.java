package fr.alessdevita.valtherion.inventory;

import java.util.*;

/**
 * Classe pour gérer le registre des objets dans Valtherion.
 * Permet de registrer, rechercher, et créer des objets.
 */
public class ItemRegistry {

    // Instance unique du registre (Singleton)
    private static ItemRegistry instance;

    // Map des objets (ID -> objet)
    private static Map<String, Item> items = Map.of();

    // Map des objets par type
    private final Map<Item.ItemType, List<Item>> itemsByType;

    // Map des objets par rareté
    private final Map<Item.Rarity, List<Item>> itemsByRarity;

    /**
     * Constructeur privé pour le Singleton.
     */
    private ItemRegistry() {
        this.items = new HashMap<>();
        this.itemsByType = new HashMap<>();
        this.itemsByRarity = new HashMap<>();
    }

    /**
     * Récupère l'instance unique du registre des objets.
     * @return Instance du registre des objets.
     */
    public static synchronized ItemRegistry getInstance() {
        if (instance == null) {
            instance = new ItemRegistry();
        }
        return instance;
    }

    /**
     * Initialise les objets par défaut.
     */
    public void initDefaultItems() {
        // Exemple : Objets de base
        registerItem(new Item("STICK", "Bâton", Item.ItemType.MATERIAL));
        registerItem(new Item("PLANK", "Planche en bois", Item.ItemType.MATERIAL));
        registerItem(new Item("STONE", "Pierre", Item.ItemType.MATERIAL));

        // Exemple : Nourriture
        Item apple = new Item("APPLE", "Pomme", Item.ItemType.FOOD);
        apple.setUsable(true);
        apple.addEffect(new Item.ItemEffect("HEAL", 5.0f, 0));
        registerItem(apple);

        // Exemple : Armes
        Item woodenSword = new Item("WOODEN_SWORD", "Épée en bois", Item.ItemType.WEAPON);
        woodenSword.setDamage(5);
        woodenSword.setDurability(100);
        woodenSword.setAttackSpeed(1.2f);
        registerItem(woodenSword);

        // Exemple : Blocs
        Item dirt = new Item("DIRT", "Terre", Item.ItemType.BLOCK);
        dirt.setStackable(true);
        dirt.setMaxStackSize(64);
        registerItem(dirt);

        Item stoneBlock = new Item("STONE_BLOCK", "Bloc de pierre", Item.ItemType.BLOCK);
        stoneBlock.setStackable(true);
        stoneBlock.setMaxStackSize(64);
        registerItem(stoneBlock);

        // Exemple : Armures
        Item leatherHelmet = new Item("LEATHER_HELMET", "Casque en cuir", Item.ItemType.ARMOR);
        leatherHelmet.setDurability(200);
        leatherHelmet.setAttribute("ARMOR", 2);
        registerItem(leatherHelmet);

        System.out.println("Objets par défaut enregistrés.");
    }

    /**
     * Enregistre un objet.
     * @param item Objet à enregistrer.
     */
    public void registerItem(Item item) {
        if (item == null) {
            return;
        }

        items.put(item.getId(), item);

        // Ajouter l'objet à la map des objets par type
        itemsByType.computeIfAbsent(item.getType(), k -> new ArrayList<>()).add(item);

        // Ajouter l'objet à la map des objets par rareté
        itemsByRarity.computeIfAbsent(item.getRarity(), k -> new ArrayList<>()).add(item);
    }

    /**
     * Retire un objet du registre.
     * @param itemId ID de l'objet à retirer.
     */
    public void unregisterItem(String itemId) {
        Item item = items.get(itemId);
        if (item != null) {
            items.remove(itemId);

            // Retirer l'objet de la map des objets par type
            List<Item> typeItems = itemsByType.get(item.getType());
            if (typeItems != null) {
                typeItems.remove(item);
            }

            // Retirer l'objet de la map des objets par rareté
            List<Item> rarityItems = itemsByRarity.get(item.getRarity());
            if (rarityItems != null) {
                rarityItems.remove(item);
            }
        }
    }

    /**
     * Récupère un objet par son ID.
     * @param itemId ID de l'objet.
     * @return Objet ou null si non trouvé.
     */
    public static Item getItem(String itemId) {
        return items.get(itemId);
    }

    /**
     * Récupère tous les objets.
     * @return Map des objets.
     */
    public Map<String, Item> getItems() {
        return items;
    }

    /**
     * Récupère les objets par type.
     * @param type Type des objets.
     * @return Liste des objets.
     */
    public List<Item> getItemsByType(Item.ItemType type) {
        return itemsByType.getOrDefault(type, new ArrayList<>());
    }

    /**
     * Récupère les objets par rareté.
     * @param rarity Rareté des objets.
     * @return Liste des objets.
     */
    public List<Item> getItemsByRarity(Item.Rarity rarity) {
        return itemsByRarity.getOrDefault(rarity, new ArrayList<>());
    }

    /**
     * Vérifie si un objet existe dans le registre.
     * @param itemId ID de l'objet.
     * @return true si l'objet existe, false sinon.
     */
    public boolean hasItem(String itemId) {
        return items.containsKey(itemId);
    }

    /**
     * Crée un nouvel objet avec un ID unique.
     * @param name Nom de l'objet.
     * @param type Type de l'objet.
     * @return Nouvel objet.
     */
    public Item createItem(String name, Item.ItemType type) {
        String id = generateUniqueId(name);
        Item item = new Item(id, name, type);
        registerItem(item);
        return item;
    }

    /**
     * Génère un ID unique pour un objet.
     * @param name Nom de l'objet.
     * @return ID unique.
     */
    private String generateUniqueId(String name) {
        // Remplacer les espaces par des underscores
        String baseId = name.toUpperCase().replace(" ", "_");

        // Ajouter un suffixe si nécessaire pour éviter les doublons
        String id = baseId;
        int suffix = 1;
        while (items.containsKey(id)) {
            id = baseId + "_" + suffix;
            suffix++;
        }

        return id;
    }

    /**
     * Nettoie les ressources du registre des objets.
     */
    public void cleanup() {
        items.clear();
        itemsByType.clear();
        itemsByRarity.clear();
    }
}