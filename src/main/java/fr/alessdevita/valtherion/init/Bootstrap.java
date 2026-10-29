package fr.alessdevita.valtherion.init;

public class Bootstrap {
    private static boolean registered;
    private Bootstrap() { }

    public static synchronized void register() {
        if (registered) return;
        fr.alessdevita.valtherion.inventory.ItemRegistry registry =
                fr.alessdevita.valtherion.inventory.ItemRegistry.getInstance();
        registry.initDefaultItems();
        registry.registerItem(Items.diamond);
        registry.registerItem(Items.emerald);
        registry.registerItem(Items.nether_star);
        registry.registerItem(Items.glass_bottle);
        registered = true;
    }

    public static synchronized boolean isRegistered() { return registered; }
}
