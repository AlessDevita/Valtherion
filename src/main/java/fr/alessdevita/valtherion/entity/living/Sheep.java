package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour les moutons dans Valtherion.
 * Un mob passif qui peut être tondu pour obtenir de la laine ou tué pour obtenir de la viande et de la laine.
 */
public class Sheep extends PassiveMob {

    // Couleur de la laine du mouton
    private String woolColor;

    // Indique si le mouton a été tondu récemment
    private boolean isSheared;

    // Temps de repousse de la laine (en ticks)
    private int woolRegrowTime;
    private int currentWoolRegrowTime;

    /**
     * Constructeur du mouton.
     * @param world Monde dans lequel le mouton existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Sheep(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z,
                PassiveMobType.SHEEP
        );

        // Configurer les propriétés spécifiques du mouton
        setName("Mouton");
        setCanBeFed(true);
        setCanBeSheared(true);
        setFavoriteFood("GRASS");

        // Configurer les drops du mouton
        addDrop(new Drop("WOOL", 0.8f, 1, 3));
        addDrop(new Drop("MUTTON", 0.5f, 1, 2));

        // Configurer les propriétés de la laine
        this.woolColor = "WHITE";
        this.isSheared = false;
        this.woolRegrowTime = 600; // 30 secondes (600 ticks)
        this.currentWoolRegrowTime = 0;
    }

    /**
     * Met à jour le mouton.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour la repousse de la laine
        if (isSheared && currentWoolRegrowTime > 0) {
            currentWoolRegrowTime--;
            if (currentWoolRegrowTime == 0) {
                isSheared = false;
                System.out.println(getName() + " a repoussé sa laine.");
            }
        }
    }

    /**
     * Tondre le mouton.
     * @return true si le mouton a été tondu, false sinon.
     */
    @Override
    public boolean shear() {
        if (!canBeSheared() || !isAdult() || isSheared) {
            return false;
        }

        // Produire de la laine
        for (int i = 0; i < 1; i++) { // 1 laine par tonte
            // Logique pour lâcher de la laine dans le monde
            // Exemple : getWorld().dropItem(woolColor + "_WOOL", getX(), getY(), getZ(), 1);
            System.out.println(getName() + " a été tondu et produit 1 " + woolColor + " laine.");
        }

        // Mettre à jour l'état du mouton
        isSheared = true;
        currentWoolRegrowTime = woolRegrowTime;

        return true;
    }

    /**
     * Nourrit le mouton.
     * @param food Objet utilisé pour nourrir le mouton.
     * @return true si le mouton a été nourri, false sinon.
     */
    @Override
    public boolean feed(Item food) {
        boolean fed = super.feed(food);

        if (fed) {
            // Le mouton repousse sa laine plus vite s'il est nourri
            if (isSheared && currentWoolRegrowTime > 120) { // Réduire de 10 secondes si le temps de repousse est supérieur à 10 secondes
                currentWoolRegrowTime -= 120;
                System.out.println(getName() + " a été nourri et repousse sa laine plus vite !");
            }
        }

        return fed;
    }

    /**
     * Méthode appelée lorsque le mouton meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getName() + " est mort et lâche de la laine et de la viande.");
    }

    // --- Getters et Setters ---

    public String getWoolColor() {
        return woolColor;
    }

    public void setWoolColor(String woolColor) {
        this.woolColor = woolColor;
    }

    public boolean isSheared() {
        return isSheared;
    }

    public void setSheared(boolean sheared) {
        isSheared = sheared;
    }

    public int getWoolRegrowTime() {
        return woolRegrowTime;
    }

    public void setWoolRegrowTime(int woolRegrowTime) {
        this.woolRegrowTime = woolRegrowTime;
    }

    public int getCurrentWoolRegrowTime() {
        return currentWoolRegrowTime;
    }

    public void setCurrentWoolRegrowTime(int currentWoolRegrowTime) {
        this.currentWoolRegrowTime = currentWoolRegrowTime;
    }

    /**
     * Retourne une représentation textuelle du mouton.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Sheep{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", woolColor='" + woolColor + '\'' +
                ", isSheared=" + isSheared +
                '}';
    }
}