package fr.alessdevita.valtherion.init;

import fr.alessdevita.valtherion.inventory.Item;

public class Items {
    private Items() { }
    public static final Item diamond = create("DIAMOND", "Diamant", Item.ItemType.MATERIAL);
    public static final Item emerald = create("EMERALD", "Émeraude", Item.ItemType.MATERIAL);
    public static final Item nether_star = create("NETHER_STAR", "Étoile du Nether", Item.ItemType.MATERIAL);
    public static final Item glass_bottle = create("GLASS_BOTTLE", "Fiole", Item.ItemType.MATERIAL);

    private static Item create(String id, String name, Item.ItemType type) {
        Item item = new Item(id, name, type);
        item.setStackable(true);
        item.setMaxStackSize(64);
        return item;
    }
}
