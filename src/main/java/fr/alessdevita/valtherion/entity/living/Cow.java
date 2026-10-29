package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;

/**
 * Classe pour les vaches dans Valtherion.
 * Un mob passif qui peut être trais pour obtenir du lait ou tué pour obtenir du cuir et de la viande.
 */
public class Cow extends PassiveMob {

    // Indique si la vache a été traite récemment
    private boolean isMilked;

    // Temps de cooldown pour la traite (en ticks)
    private int milkCooldown;
    private int currentMilkCooldown;

    // Quantité de lait produite par traite
    private int milkYield;

    /**
     * Constructeur de la vache.
     * @param world Monde dans lequel la vache existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Cow(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z,
                PassiveMobType.COW
        );

        // Configurer les propriétés spécifiques de la vache
        setName("Vache");
        setCanBeMilked(true);
        setCanBeFed(true);
        setFavoriteFood("WHEAT");

        // Configurer les drops de la vache
        addDrop(new Drop("LEATHER", 0.8f, 1, 3));
        addDrop(new Drop("BEEF", 0.5f, 1, 2));

        // Configurer les propriétés de traite
        this.isMilked = false;
        this.milkCooldown = 600; // 30 secondes (600 ticks)
        this.currentMilkCooldown = 0;
        this.milkYield = 1; // 1 lait par traite
    }

    /**
     * Met à jour la vache.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour le cooldown de la traite
        if (isMilked && currentMilkCooldown > 0) {
            currentMilkCooldown--;
            if (currentMilkCooldown == 0) {
                isMilked = false;
            }
        }
    }

    /**
     * Traire la vache.
     * @return true si la vache a été traite, false sinon.
     */
    @Override
    public boolean milk() {
        if (!canBeMilked() || !isAdult() || isMilked) {
            return false;
        }

        // Produire du lait
        for (int i = 0; i < milkYield; i++) {
            // Logique pour lâcher du lait dans le monde
            // Exemple : getWorld().dropItem("MILK", getX(), getY(), getZ(), 1);
            System.out.println(getName() + " a été traite et produit " + milkYield + " lait.");
        }

        // Mettre à jour l'état de la vache
        isMilked = true;
        currentMilkCooldown = milkCooldown;

        return true;
    }

    /**
     * Nourrit la vache.
     * @param food Objet utilisé pour nourrir la vache.
     * @return true si la vache a été nourrie, false sinon.
     */
    @Override
    public boolean feed(fr.alessdevita.valtherion.inventory.Item food) {
        boolean fed = super.feed(food);

        if (fed) {
            // Réduire le cooldown de la traite si la vache est nourrie
            if (isMilked && currentMilkCooldown > 120) { // Réduire de 10 secondes si le cooldown est supérieur à 10 secondes
                currentMilkCooldown -= 120;
                System.out.println(getName() + " a été nourrie et peut être traite plus tôt !");
            }
        }

        return fed;
    }

    /**
     * Méthode appelée lorsque la vache meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();

        // Lâcher des objets spécifiques à la vache
        System.out.println(getName() + " est morte et lâche du cuir et de la viande.");
    }

    // --- Getters et Setters ---

    public boolean isMilked() {
        return isMilked;
    }

    public void setMilked(boolean milked) {
        isMilked = milked;
    }

    public int getMilkCooldown() {
        return milkCooldown;
    }

    public void setMilkCooldown(int milkCooldown) {
        this.milkCooldown = milkCooldown;
    }

    public int getCurrentMilkCooldown() {
        return currentMilkCooldown;
    }

    public void setCurrentMilkCooldown(int currentMilkCooldown) {
        this.currentMilkCooldown = currentMilkCooldown;
    }

    public int getMilkYield() {
        return milkYield;
    }

    public void setMilkYield(int milkYield) {
        this.milkYield = milkYield;
    }

    /**
     * Retourne une représentation textuelle de la vache.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Cow{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", isMilked=" + isMilked +
                ", currentMilkCooldown=" + currentMilkCooldown +
                '}';
    }

    // --- Classe interne pour les types de mobs passifs ---
    public enum PassiveMobType {
        COW, PIG, SHEEP, CHICKEN, RABBIT, HORSE, CAT, DOG
    }
}