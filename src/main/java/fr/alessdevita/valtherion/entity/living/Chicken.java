package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour les poulets dans Valtherion.
 * Un mob passif qui peut être tué pour obtenir de la viande et des plumes.
 */
public class Chicken extends PassiveMob {

    // Indique si le poulet est en train de pondre un œuf
    private boolean isLayingEgg;

    // Temps entre les pondaisons d'œufs (en ticks)
    private int eggLayTime;
    private int currentEggLayTime;

    /**
     * Constructeur du poulet.
     * @param world Monde dans lequel le poulet existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Chicken(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z,
                PassiveMobType.CHICKEN
        );

        // Configurer les propriétés spécifiques du poulet
        setName("Poulet");
        setCanBeFed(true);
        setFavoriteFood("SEEDS");

        // Configurer les drops du poulet
        addDrop(new Drop("FEATHER", 0.8f, 1, 3));
        addDrop(new Drop("CHICKEN", 0.5f, 1, 1));

        // Configurer les propriétés de ponte
        this.isLayingEgg = false;
        this.eggLayTime = 1200; // 60 secondes (1200 ticks)
        this.currentEggLayTime = 0;
    }

    /**
     * Met à jour le poulet.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour le temps de ponte
        if (!isLayingEgg) {
            currentEggLayTime++;
            if (currentEggLayTime >= eggLayTime) {
                layEgg();
                currentEggLayTime = 0;
            }
        }
    }

    /**
     * Pond un œuf.
     */
    private void layEgg() {
        isLayingEgg = true;

        // Logique pour pondre un œuf dans le monde
        // Exemple : getWorld().dropItem("EGG", getX(), getY(), getZ(), 1);
        System.out.println(getName() + " a pondu un œuf.");

        isLayingEgg = false;
    }

    /**
     * Nourrit le poulet.
     * @param food Objet utilisé pour nourrir le poulet.
     * @return true si le poulet a été nourri, false sinon.
     */
    @Override
    public boolean feed(Item food) {
        boolean fed = super.feed(food);

        if (fed) {
            // Le poulet pond des œufs plus vite s'il est nourri
            if (currentEggLayTime > 240) { // Réduire de 12 secondes si le temps de ponte est supérieur à 12 secondes
                currentEggLayTime -= 240;
                System.out.println(getName() + " a été nourri et pondra un œuf plus tôt !");
            }
        }

        return fed;
    }

    /**
     * Méthode appelée lorsque le poulet meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getName() + " est mort et lâche des plumes et de la viande.");
    }

    // --- Getters et Setters ---

    public boolean isLayingEgg() {
        return isLayingEgg;
    }

    public void setLayingEgg(boolean layingEgg) {
        isLayingEgg = layingEgg;
    }

    public int getEggLayTime() {
        return eggLayTime;
    }

    public void setEggLayTime(int eggLayTime) {
        this.eggLayTime = eggLayTime;
    }

    public int getCurrentEggLayTime() {
        return currentEggLayTime;
    }

    public void setCurrentEggLayTime(int currentEggLayTime) {
        this.currentEggLayTime = currentEggLayTime;
    }

    /**
     * Retourne une représentation textuelle du poulet.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Chicken{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", isLayingEgg=" + isLayingEgg +
                '}';
    }
}